/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.world;

import Project3_6713120.GameManager;
import Project3_6713120.items.Item;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Interactable {
    private final List<Item> itemsForSale = new ArrayList<>();

    public void addItem(Item item) {
        itemsForSale.add(item);
    }

    public void interact(GameManager gm) {
        for (int i = 0; i < itemsForSale.size(); i++) {
            Item item = itemsForSale.get(i);
            System.out.println((i + 1) + ". " + item);
        }
    }

    public List<Item> getItemsForSale() {
        return itemsForSale;
    }
}

