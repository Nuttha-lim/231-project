# Validation record

Date: 3 November 2026 (Asia/Bangkok).

## Source provenance

Java files were extracted from the repository's original `EGCO213-Project-main.zip`, preserving package names and source contents. The archive is retained.

Archive SHA-256: `2760250cf268ddd8df006ca5e6202ddb7ac4238e4904b84fc5ca9c74f7d22a0d`.

## Reproduction

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\run.ps1 -BuildOnly
```

## Local results

- Environment: Windows, Java/Javac 21.0.8.
- `scripts/run.ps1 -BuildOnly`: exit code 0; restored 352 bundled media/font files and compiled all 49 Java source files.
- Compiler emitted an existing unchecked/unsafe operations warning for `ui/ShopScene.java`; this is not a clean static-analysis result.
- Running `java -cp target/classes Project3_6713120.MainApplication` opened the CO Valley main menu. The [screenshot](screenshots/main-menu.png) was captured from that application window.
- New documentation and scripts passed `git diff --check`. Original Java whitespace was retained from the archive.

The CI workflow runs the same build on Windows with JDK 21. Hosted CI status must be checked in GitHub Actions; the local result above does not substitute for a hosted run.

The [playtest checklist](PLAYTEST_CHECKLIST.md) is a test plan. It does not claim that all gameplay cases have been executed or passed.
