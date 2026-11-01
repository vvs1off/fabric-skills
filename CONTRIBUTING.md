# Contributing to fabric-skills

Thanks for helping make AI agents better at Fabric modding. The most valuable contributions are
**corrections**: Fabric and Minecraft rename things every release, and a skill that teaches a
stale name is worse than no skill.

## Ground rules

1. **No hardcoded versions to copy.** Skills tell the agent where to look versions up
   (fabricmc.net/develop, meta.fabricmc.net, Modrinth). Version numbers may appear only as
   examples of *format* or of *which era an API belongs to* (e.g. "renamed in 26.1").
2. **Verify every code example** against real sources for the version it claims (see below).
   If you can't verify something, say so in the text ("check the exact name in your sources").
3. **Explain how to detect** which API applies when it differs between versions.
4. **Every topic skill ends with `## Common mistakes`**: the concrete errors LLMs make.
5. **English only**, no personal data, no real tokens - even fake-looking tokens trip the
   secret scanner. Use placeholders like `YOUR_NAME`, `examplemod`, `com.example`.

## Repository layout

```
.claude-plugin/plugin.json        plugin manifest
.claude-plugin/marketplace.json   marketplace entry (source "./")
skills/<topic>/SKILL.md           topic skills (model-invoked)
skills/{new,port,check}/SKILL.md  slash commands (disable-model-invocation: true)
agents/mixin-reviewer.md          subagent
examples/example-mod/             a mod that must always build
scripts/validate_skills.py        frontmatter + hygiene checks (run in CI)
.githooks/pre-commit              local secret guard
```

## Skill format

```markdown
---
name: fabric-something            # must equal the directory name, kebab-case
description: What it covers. Use when <concrete situations that should trigger it>.
---

# Title

Step 0 - detect the version/era when it matters.
Concrete instructions with correct, minimal code.

## Common mistakes
- ...
```

The `description` is what Claude reads to decide whether to load the skill. Name the situations
and keywords a user would actually type ("HUD", "crash", "packet", "port to 26.3").

Slash commands are skills with `disable-model-invocation: true` and an `argument-hint`.

## Verifying code against real sources

Use the example mod as a sandbox for the newest version:

```bash
cd examples/example-mod
./gradlew genSources        # decompiled Minecraft for the version in gradle.properties
```

The sources jars end up under `.gradle/loom-cache/minecraftMaven/`. Search them (or open the
project in IntelliJ IDEA) for every vanilla class, method and descriptor you reference. For
Fabric API names, check the module sources in https://github.com/FabricMC/fabric-api at the tag
for that version. For older versions, change `gradle.properties` locally (do not commit) or use
the version selector on https://docs.fabricmc.net.

## Running the checks locally

```bash
python -m pip install pyyaml
python scripts/validate_skills.py          # frontmatter, names, Common mistakes, secrets
claude plugin validate .                   # official plugin/marketplace validation
(cd examples/example-mod && ./gradlew build)
```

Try your change in Claude Code without publishing:

```bash
claude --plugin-dir .
```

or add the local folder as a marketplace: `/plugin marketplace add ./` then
`/plugin install fabric-skills@fabric-skills`, and `/reload-plugins` after edits.

## Enable the pre-commit hook (recommended)

```bash
git config core.hooksPath .githooks
```

It blocks commits that add `.env`, key stores, private keys or token-like strings, and runs
[gitleaks](https://github.com/gitleaks/gitleaks) too if it is installed. CI runs gitleaks over
the full history on every push and pull request.

## Pull requests

- One topic per PR. Explain *what was wrong* and *how you verified the fix* (version + source).
- Update `CHANGELOG.md` under `## [Unreleased]`.
- If you change the example mod, it must still pass `./gradlew build`.
- Commit messages: imperative mood, e.g. `fix(networking): use clientboundPlay on 26.1+`.

## Code of conduct

Be kind and constructive. Assume good faith; review the code, not the person.
