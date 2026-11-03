/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.items;

import java.io.Serializable;

public class Crop implements Serializable {
    private final String name;
    private final int growTime; // in days or ticks
    private final VegetableItem harvestProduct;

    public Crop(String name, int growTime, VegetableItem harvestProduct) {
        this.name = name;
        this.growTime = growTime;
        this.harvestProduct = harvestProduct;
    }

    public String getName() {
        return name;
    }

    public int getGrowTime() {
        return growTime;
    }

    public VegetableItem getHarvestProduct() {
        return harvestProduct;
    }

    @Override
    public String toString() {
        return name + " (Grow time: " + growTime + ")";
    }
}
