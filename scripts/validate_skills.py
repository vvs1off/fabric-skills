#!/usr/bin/env python3
"""Validate every skills/*/SKILL.md and agents/*.md in this plugin.

Checks:
  - the file starts with YAML frontmatter that parses
  - `name` matches the directory (skills) or file name (agents), kebab-case
  - `description` is present and long enough to say WHEN to use the component
  - topic skills (not user-invoked commands) have a "## Common mistakes" section
  - no obvious hardcoded secrets or personal paths

Exit code 0 when everything passes, 1 otherwise. Requires PyYAML.
"""

from __future__ import annotations

import re
import sys
from pathlib import Path

import yaml

ROOT = Path(__file__).resolve().parent.parent
NAME_RE = re.compile(r"^[a-z0-9]+(-[a-z0-9]+)*$")
FRONTMATTER_RE = re.compile(r"\A---\r?\n(.*?)\r?\n---\r?\n", re.S)
MIN_DESCRIPTION = 40
MAX_DESCRIPTION = 1024

SUSPICIOUS = [
    (re.compile(r"(?i)(api[_-]?key|token|secret|password)\s*[:=]\s*['\"][A-Za-z0-9_\-]{16,}['\"]"), "hardcoded credential"),
    (re.compile(r"(?i)\b(ghp|gho|ghs|github_pat)_[A-Za-z0-9_]{20,}"), "GitHub token"),
    (re.compile(r"\bmrp_[A-Za-z0-9]{20,}"), "Modrinth token"),
    (re.compile(r"(?i)[A-Z]:\\Users\\[^\\\s]+"), "Windows user path"),
    (re.compile(r"/home/[a-z_][a-z0-9_-]*/"), "Linux home path"),
]


def parse_frontmatter(path: Path, errors: list[str]) -> tuple[dict | None, str]:
    text = path.read_text(encoding="utf-8")
    match = FRONTMATTER_RE.match(text)
    if not match:
        errors.append(f"{path}: missing YAML frontmatter (--- ... ---) at the top")
        return None, text
    try:
        data = yaml.safe_load(match.group(1))
    except yaml.YAMLError as exc:
        errors.append(f"{path}: frontmatter is not valid YAML: {exc}")
        return None, text
    if not isinstance(data, dict):
        errors.append(f"{path}: frontmatter must be a mapping")
        return None, text
    return data, text


def check_common(path: Path, data: dict, expected_name: str, errors: list[str]) -> None:
    name = data.get("name")
    if name is None:
        errors.append(f"{path}: missing `name`")
    elif not isinstance(name, str) or not NAME_RE.match(name):
        errors.append(f"{path}: `name` must be kebab-case, got {name!r}")
    elif name != expected_name:
        errors.append(f"{path}: `name` is {name!r} but should match {expected_name!r}")

    description = data.get("description")
    if not isinstance(description, str) or not description.strip():
        errors.append(f"{path}: missing `description`")
    elif len(description.strip()) < MIN_DESCRIPTION:
        errors.append(f"{path}: `description` is too short to say when to use it ({len(description)} chars)")
    elif len(description) > MAX_DESCRIPTION:
        errors.append(f"{path}: `description` is longer than {MAX_DESCRIPTION} chars")


def check_secrets(path: Path, text: str, errors: list[str]) -> None:
    for pattern, label in SUSPICIOUS:
        for match in pattern.finditer(text):
            line = text.count("\n", 0, match.start()) + 1
            errors.append(f"{path}:{line}: possible {label}: {match.group(0)[:40]!r}")


def main() -> int:
    errors: list[str] = []
    skill_files = sorted((ROOT / "skills").glob("*/SKILL.md"))
    agent_files = sorted((ROOT / "agents").glob("*.md"))

    if not skill_files:
        errors.append("no skills found under skills/*/SKILL.md")

    for path in skill_files:
        data, text = parse_frontmatter(path, errors)
        if data is None:
            continue
        check_common(path, data, path.parent.name, errors)
        user_invoked = data.get("disable-model-invocation") is True
        if not user_invoked and "## Common mistakes" not in text:
            errors.append(f"{path}: topic skills must have a '## Common mistakes' section")
        check_secrets(path, text, errors)

    for path in agent_files:
        data, text = parse_frontmatter(path, errors)
        if data is None:
            continue
        check_common(path, data, path.stem, errors)
        check_secrets(path, text, errors)

    for error in errors:
        print(f"ERROR {error}")

    total = len(skill_files) + len(agent_files)
    if errors:
        print(f"\n{len(errors)} problem(s) in {total} file(s).")
        return 1

    print(f"OK: {len(skill_files)} skill(s) and {len(agent_files)} agent(s) are valid.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
