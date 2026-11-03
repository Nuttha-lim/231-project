# Build and startup results

Date: 3 November 2026 (Asia/Bangkok).

## Source archive

The 49 Java source files match those in `EGCO213-Project-main.zip`. The archive also contains the images, audio and fonts used by the game.

Archive SHA-256: `2760250cf268ddd8df006ca5e6202ddb7ac4238e4904b84fc5ca9c74f7d22a0d`.

## Run the build check

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\run.ps1 -BuildOnly
```

## Local results

- Environment: Windows, Java/Javac 21.0.8.
- `scripts/run.ps1 -BuildOnly`: exit code 0; restored 352 bundled media/font files and compiled all 49 Java source files.
- The compiler reported an unchecked/unsafe operations warning in `ui/ShopScene.java`.
- Running `java -cp target/classes Project3_6713120.MainApplication` opened the CO Valley main menu. The [screenshot](screenshots/main-menu.png) was captured from that application window.

The [GitHub Actions workflow](https://github.com/Nuttha-lim/co-valley/actions/workflows/build.yml) runs the same compilation check on Windows with JDK 21. Gameplay cases are listed in the [manual test plan](PLAYTEST_CHECKLIST.md); their execution results have not been recorded here.
