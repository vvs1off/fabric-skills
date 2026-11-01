# Security policy

## Supported versions

Only the latest release of fabric-skills receives fixes.

## Reporting a vulnerability

Please **do not** open a public issue for security problems. Use GitHub's private
vulnerability reporting instead: on this repository, go to **Security -> Report a
vulnerability**. You should get a first response within 7 days.

Relevant reports include, for example:

- a skill, command or agent that instructs Claude to run unsafe commands, leak credentials,
  or weaken a project's security (e.g. disabling secret handling in release workflows);
- a secret accidentally committed to this repository;
- a supply-chain risk in the CI workflows (unpinned actions, unverified downloads).

## How this repository protects secrets

- No credentials are needed to use or develop the plugin; there is no `.env`.
- Publishing guidance in the skills only ever references tokens as GitHub Actions secrets
  (`${{ secrets.MODRINTH_TOKEN }}`), never as literal values or files.
- `.gitignore` excludes `.env*`, key stores, private keys and local properties.
- `.githooks/pre-commit` blocks secret-like files and token patterns
  (enable with `git config core.hooksPath .githooks`).
- The `Secret scan` workflow runs [gitleaks](https://github.com/gitleaks/gitleaks) over the full
  git history on every push and pull request.
- Third-party GitHub Actions are pinned to full commit SHAs, and downloaded tools are verified
  by checksum.

If you ever find a real token in this repository's history, report it privately; it will be
revoked first and removed from history second.
