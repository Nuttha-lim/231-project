/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.world.Interactable;
import Project3_6713120.world.Tile;

import javax.swing.*;

public class TileLabel extends JLabel implements Interactable {
    private ImageIcon image;
    // ลบตัวแปร width/height ที่ fix ไว้ออก หรือแก้ให้รับค่าจาก UiConfig
    private final int width = UiConfig.TILE_WIDTH;
    private final int height = UiConfig.TILE_HEIGHT;
    private final GameManager gameManager;
    private int xIndex, yIndex;
    private JLabel cropLabel;
    private Tile tile;
    private int currentStage;

    public TileLabel(int x, int y, int xIndex, int yIndex, GameManager gm, Tile tile) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.gameManager = gm;
        this.tile = tile;

        getTileImage();
        setIcon(image);
        setSize(width, height);

        setBounds(x + xIndex * (width + UiConfig.TILE_GAP),
                y + yIndex * height,
                width, height);
        // ------------------------------------------------

        cropLabel = new JLabel();
        cropLabel.setBounds(0, 0, width, height);
        cropLabel.setVisible(false);
        add(cropLabel);
    }

    private void getTileImage() {
        if (tile.isWatered()) {
            switch (yIndex) {
                case 1:
                    image = UiConfig.SOIL_WATER_2;
                    break;
                case 2:
                    image = UiConfig.SOIL_WATER_3;
                    break;
                case 3:
                    image = UiConfig.SOIL_WATER_4;
                    break;
                default:
                    image = UiConfig.SOIL_WATER_1;
            }
        } else if (tile.isTilled()) {
            switch (yIndex) {
                case 1:
                    image = UiConfig.SOIL_TILL_2;
                    break;
                case 2:
                    image = UiConfig.SOIL_TILL_3;
                    break;
                case 3:
                    image = UiConfig.SOIL_TILL_4;
                    break;
                default:
                    image = UiConfig.SOIL_TILL_1;
            }
        } else {
            switch (yIndex) {
                case 1:
                    image = UiConfig.SOIL_BASE_2;
                    break;
                case 2:
                    image = UiConfig.SOIL_BASE_3;
                    break;
                case 3:
                    image = UiConfig.SOIL_BASE_4;
                    break;
                default:
                    image = UiConfig.SOIL_BASE_1;
            }
        }
    }

    @Override
    public void interact(GameManager gm) {
        ScenePanel scene = gm.getCurrentScene();
        if (scene instanceof PlayableScene) {
            int index = ((PlayableScene) scene).getPlotIndex();
            Tile currentTile = gm.getCurrentSave().getPlots().get(index).getTile(xIndex, yIndex);
            currentTile.interact(gm);
            refresh();
        }
    }

    public void refresh() {
        if (tile.getPlantName() != "") {
            String imageBasePath = UiConfig.Path + "Plant/" + tile.getPlantName() + "/";
            String imagePath = "";
            ImageIcon cropImage;
            int stage = tile.getGrowthStage();
            // Only update if stage changed
            if (currentStage != stage) {
                switch (stage) {
                    case 0:
                        getTileImage();
                        setIcon(image);
                        break;
                    case 2:
                        imagePath = imageBasePath + "2.PNG";
                        break;
                    case 3:
                        imagePath = imageBasePath + "3.PNG";
                        break;
                    case 4:
                        imagePath = imageBasePath + "4.PNG";
                        break;
                    default:
                        imagePath = imageBasePath + "1.PNG";
                }
                getTileImage();
                setIcon(image);
                cropImage = new MyImageIcon(imagePath).resize(width, height);
                cropLabel.setIcon(cropImage);
                cropLabel.setVisible(true);

                currentStage = stage;
            }
        } else {
            if (cropLabel.isVisible()) {
                cropLabel.setVisible(false);
            }
        }
        ImageIcon prevImage = image;

        // Check if watered/tilled/etc
        getTileImage();
        if (prevImage != image) {
            setIcon(image);
        }
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}


