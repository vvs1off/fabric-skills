## What and why

<!-- What does this change, and what was wrong or missing before? -->

## How it was verified

<!-- Minecraft / Fabric API version, and where you checked names: generated sources, Fabric API
     source at a tag, docs.fabricmc.net page, or a build of examples/example-mod. -->

## Checklist

- [ ] `python scripts/validate_skills.py` passes
- [ ] `claude plugin validate .` passes
- [ ] The example mod still builds (`cd examples/example-mod && ./gradlew build`), if touched
- [ ] No hardcoded versions to copy; version-dependent APIs explain how to detect the right one
- [ ] Topic skills keep their `## Common mistakes` section
- [ ] `CHANGELOG.md` updated under `[Unreleased]`
- [ ] No secrets, tokens, personal names, emails or local paths
