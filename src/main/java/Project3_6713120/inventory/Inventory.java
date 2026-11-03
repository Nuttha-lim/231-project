/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.inventory;

import Project3_6713120.items.Item;

public class Inventory {

    public int rows, cols;
    private Slot[][] slots;

    public Inventory(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        slots = new Slot[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                slots[r][c] = new Slot();
    }

    public Slot getSlot(int r, int c) {
        return slots[r][c];
    }

    // --- แก้ไขตรงนี้: รับ amount เพิ่มเข้ามา ---
    public boolean add(Item item, int amount) {
        int remaining = amount;

        // 1. วนหา "กองเดิม" เพื่อเติมให้เต็มก่อน (Stacking)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (remaining <= 0) return true; // ถ้าของหมดแล้วก็จบเลย

                Slot slot = slots[r][c];

                // ถ้าเจอของชนิดเดียวกัน และกองยังไม่เต็ม (สมมติเต็ม 64)
                if (!slot.isEmpty()
                        && slot.stack.item.getName().equals(item.getName())) {

                    int space = 64 - slot.stack.count; // พื้นที่ว่างในกองนี้

                    if (space > 0) {
                        int toAdd = Math.min(remaining, space); // เติมเท่าที่มี หรือเท่าที่กองรับไหว
                        slot.stack.count += toAdd;
                        remaining -= toAdd;
                    }
                }
            }
        }

        // 2. ถ้ายังมีของเหลือ ให้หา "ช่องว่าง" (Empty Slot)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (remaining <= 0) return true;

                Slot slot = slots[r][c];
                if (slot.isEmpty()) {
                    // สร้างกองใหม่ (ใส่ได้สูงสุด 64 หรือเท่าที่เหลือ)
                    int toAdd = Math.min(remaining, 64);
                    slot.stack = new ItemStack(item, toAdd);
                    remaining -= toAdd;
                }
            }
        }

        // ถ้า remaining == 0 แสดงว่าใส่ครบทุกชิ้น (return true)
        // ถ้าเหลือ แสดงว่ากระเป๋าเต็ม ใส่ได้ไม่ครบ (return false)
        return remaining == 0;
    }

    // overload method เดิมไว้ เผื่อโค้ดเก่าเรียกใช้แบบไม่ระบุจำนวน (ให้ค่า default = 1)
    public boolean add(Item item) {
        return add(item, 1);
    }

    // นับจำนวนรวมของไอเทมตามชื่อ โดยไม่สนว่ามีกี่กอง
    public int getTotalCount(Item target) {
        int total = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                Slot slot = slots[r][c];

                if (!slot.isEmpty() &&
                        slot.stack.item.getName().equals(target.getName())) {

                    total += slot.stack.count;
                }
            }
        }

        return total;
    }

    public boolean remove(Item item, int amount) {
        int remainingToRemove = amount;

        // วนหาไอเทมชิ้นนั้นในกระเป๋า
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (remainingToRemove <= 0) return true; // ลบครบแล้ว

                Slot slot = slots[r][c];

                // ถ้าเจอไอเทมชื่อเดียวกัน
                if (!slot.isEmpty() && slot.stack.item.getName().equals(item.getName())) {

                    if (slot.stack.count > remainingToRemove) {
                        // ถ้าในกองมีเยอะกว่าที่ต้องการลบ -> แค่ลดจำนวนลง
                        slot.stack.count -= remainingToRemove;
                        remainingToRemove = 0;
                    } else {
                        // ถ้าในกองมีน้อยกว่าหรือเท่ากับ -> ลบทั้งกอง (เซ็ตเป็น null)
                        remainingToRemove -= slot.stack.count;
                        slot.stack = null;
                    }
                }
            }
        }

        // ถ้า remainingToRemove == 0 แปลว่าลบได้ครบตามจำนวนที่สั่ง
        return remainingToRemove == 0;
    }

    public boolean remove(Item item) {
        return remove(item, 1);
    }
}