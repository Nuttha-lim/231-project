/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.items;

public class SeedItem extends Item {
    private final Crop crop;

    public SeedItem(String name, Crop crop, int price) {
        super(name, price);
        this.crop = crop;
    }

    public Crop getCrop() {
        return crop;
    }

    @Override
    public String toString() {
        return name + "(" + price + "G)";
    }

}

