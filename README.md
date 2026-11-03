# CO Valley

A desktop farming simulation built with **Java 21 and Java Swing** as a five-person academic project. Players explore connected maps, grow crops, manage an inventory, trade items and save their progress.

[ภาษาไทย](docs/README_TH.md) · [Architecture](docs/ARCHITECTURE.md) · [Gameplay checks](docs/PLAYTEST_CHECKLIST.md) · [Credits](CREDITS.md)

![CO Valley running locally on Windows, showing the main menu](docs/screenshots/main-menu.png)

*Main menu captured from the Java application on Windows, 3 November 2026.*

## My contribution

**Nuttha Limkhhunthammo** — game UX/UI design, four-direction character movement and directional animations, movement blocking at fences, transitions between maps, and gameplay testing. The game is a team project; inventory, farming, shops and persistence are described below as project features.

## Features

- Explore multiple scenes with WASD movement and directional character sprites.
- Till soil, plant seeds, water crops and harvest produce.
- Manage a 4 × 6 inventory with item stacks and a six-slot hotbar.
- Buy and sell items in the shop.
- Create a character, use save slots and resume a previous session.
- Access in-game instructions, music settings and team credits.

## Run locally

**Requirements:** Windows, JDK 21 (`java` and `javac` on PATH), and PowerShell. The game uses a 1366 × 768 play area; allow extra screen space for the window frame.

```powershell
git clone https://github.com/Nuttha-lim/co-valley.git
cd co-valley
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\run.ps1
```

The script restores bundled images, fonts and audio from the original ZIP, compiles the Java sources, and starts the game from the repository root. It preserves existing restored assets. No separate Java libraries or database are required.

To compile without opening the game:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\run.ps1 -BuildOnly
```

Alternatively, open `pom.xml` in IntelliJ IDEA, select JDK 21 and run `Project3_6713120.MainApplication` after running `scripts/prepare-assets.ps1`. Set the working directory to the repository root because assets use relative filesystem paths.

## Controls

| Input | Action |
| --- | --- |
| W / A / S / D | Move up / left / down / right |
| E | Interact with the nearby object or plot |
| 1–6 | Select a hotbar slot |
| Tab | Toggle inventory during gameplay |
| Esc | Close inventory or open/exit scene options |

## Code tour

| Path | Responsibility |
| --- | --- |
| `src/main/java/Project3_6713120/MainApplication.java` | Desktop window and application entry point |
| `GameManager.java` in the same package | Scene coordination, input, game updates and saving |
| `world/` | Player movement, interaction, map tiles and farming plots |
| `ui/` | Swing scenes, character rendering, menus and transitions |
| `inventory/` | Item stacks, inventory panels and hotbar |
| `items/` | Item, tool, seed, crop and produce models |
| `data/` | Game data, save snapshots and file persistence |

## Validation and scope

Local verification: **49 Java files compiled with JDK 21**, and the application opened to the main menu on Windows. The build workflow checks compilation; it does not certify gameplay. See the dated [validation record](docs/VALIDATION.md) and the manual [playtest checklist](docs/PLAYTEST_CHECKLIST.md) for the exact evidence and remaining checks.

The original `EGCO213-Project-main.zip` remains available for provenance and bundled media. Java sources are also exposed directly in `src/` so reviewers can inspect them without downloading the archive. Build output, restored media, IDE settings and `.sav` files are excluded from new commits.

Known limitations include fixed-size UI, filesystem-relative resources and Java-serialized save files. Use save files created locally by this game. Standalone JAR packaging and cross-platform gameplay have not been verified.

## Team and assets

See [CREDITS.md](CREDITS.md) for the original team attribution. The archive does not include a project-wide license or a complete media attribution record; no new license is assigned by this documentation update.
