# Gameplay test checklist

Use a fresh save slot for these manual tests. Record the commit, JDK, operating system, actual result and evidence for each case. Execution results are pending.

| ID | Scenario | Expected result |
| --- | --- | --- |
| MOVE-01 | Move in each WASD direction, then release the key | Position and animation match the direction; movement stops on release |
| MOVE-02 | Walk into every visible fence edge and corner | Character stays inside the intended playable boundary |
| MAP-01 | Cross each unlocked exit, then return | Correct destination and usable spawn position; no stuck movement |
| MAP-02 | Try a locked exit | Scene remains unchanged and the restriction is clear |
| UI-01 | Open inventory while moving, then close it | Inventory focus does not leave movement stuck |
| UI-02 | Open and close menus using mouse and Esc | Focus returns to the correct scene |
| FARM-01 | Till, plant, water, wait and harvest | Plot state, crop appearance and inventory update consistently |
| INV-01 | Add items across the stack limit, move and remove them | Quantities remain consistent; no duplication or lost items |
| SHOP-01 | Buy with sufficient and insufficient coins; sell produce | Coins and quantities match the completed transaction |
| SAVE-01 | Save, close, reopen and load the same slot | Character, map, inventory and progress restore consistently |
| SAVE-02 | Open an empty slot | Clear recovery path without a crash |
| AUDIO-01 | Change music settings and navigate scenes | Setting and playback remain consistent |

For each defect, record reproduction steps, expected and actual behavior, frequency, severity and the save slot used. Attach a screenshot or short video.
