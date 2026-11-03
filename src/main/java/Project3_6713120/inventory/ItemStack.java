/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.inventory;

import Project3_6713120.items.Item;

import java.io.Serializable;

public class ItemStack implements Serializable {

    public Item item;
    public int count;

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public boolean isFull() {
        return item != null && count >= 64;
    }
}
