# Changelog

All notable changes to this project are documented here.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this
project follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2026-11-01

### Added

- Claude Code plugin manifest and a self-hosted marketplace
  (`/plugin marketplace add vvs1off/fabric-skills`).
- Eight topic skills: `fabric-project-setup`, `fabric-mixins`, `fabric-client-rendering`,
  `fabric-networking`, `fabric-config-and-keybinds`, `fabric-version-porting`,
  `fabric-crash-debugging` and `fabric-release`. Each covers both the Minecraft 26.1+
  (unobfuscated, Mojang names) era and the older Yarn/remapping era, and ends with a
  "Common mistakes" section.
- Slash commands `/fabric-skills:new`, `/fabric-skills:port` and `/fabric-skills:check`.
- `mixin-reviewer` subagent.
- Example client-side mod for Minecraft 26.3 with a HUD element, key mapping, MixinExtras
  mixin and JSON config.
- CI: example mod build, skill validation and gitleaks secret scanning; local pre-commit hook.

[Unreleased]: https://github.com/vvs1off/fabric-skills/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/vvs1off/fabric-skills/releases/tag/v0.1.0
