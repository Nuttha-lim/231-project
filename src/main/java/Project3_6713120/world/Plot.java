/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.world;

import Project3_6713120.GameManager;

import java.io.Serializable;

public class Plot implements Serializable {
    private final Tile[][] tiles;
    private boolean unlocked;

    public Plot(boolean unlocked, GameManager gm) {
        int width = 4;
        int height = 4;
        this.tiles = new Tile[4][4];
        this.unlocked = unlocked;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile();
            }
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        unlocked = true;
    }

    public void growAll(GameManager gm) {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.grow();
            }
        }
    }
}

