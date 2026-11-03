/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.inventory;

public class Slot {

    public ItemStack stack;

    public boolean isEmpty() {
        return stack == null || stack.count <= 0;
    }
}
