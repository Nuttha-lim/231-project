/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.world;

import Project3_6713120.GameManager;
import Project3_6713120.inventory.ItemStack;
import Project3_6713120.items.SeedItem;
import Project3_6713120.items.ToolItem;
import Project3_6713120.items.VegetableItem;

import java.io.Serializable;

public class Tile implements Interactable, Serializable {
    private boolean tilled;
    private boolean watered;
    private SeedItem plantedSeed;
    private int growthStage;
    private int growthDuration = 0;

    public Tile() {
        this.tilled = false;
        this.watered = false;
        this.growthStage = 0;
    }

    @Override
    public void interact(GameManager gm) {
        ItemStack selected = gm.getHeldItem();
        if (selected != null) {
            if (selected.item instanceof ToolItem tool) {
                if (tool.getName().equalsIgnoreCase("Hoe")) {
                    tilled = true;
                } else if (tool.getName().equalsIgnoreCase("Watering Can")) {
                    if (tilled) {
                        watered = true;
                    }
                }
            } else if (selected.item instanceof SeedItem seed&&selected.item.getName()!="Bean Seeds") {
                if (tilled && plantedSeed == null) {
                    gm.getInventory().remove(seed);
                    plantedSeed = seed;
                    growthStage = 1;
                }
            }
        }

        if (growthStage >= 4) {
            VegetableItem item = harvest();
            gm.getInventory().add(item);
        }
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public String getPlantName() {
        if (plantedSeed == null) {
            return "";
        } else {
            return plantedSeed.getCrop().getName();
        }
    }

    public void grow() {
        if (plantedSeed != null && watered && growthStage < 4) {
            growthDuration++;
            if (growthDuration >= plantedSeed.getCrop().getGrowTime()) {
                growthStage++;
                growthDuration = 0;
            }
        }
    }

    public boolean isWatered() {
        return watered;
    }

    public boolean isTilled() {
        return tilled;
    }

    public VegetableItem harvest() {
        if (growthStage >= 4) {
            VegetableItem veg = plantedSeed.getCrop().getHarvestProduct();
            plantedSeed = null;
            growthStage = 0;
            tilled = false;
            watered = false;
            return veg;
        }
        return null;
    }
}

