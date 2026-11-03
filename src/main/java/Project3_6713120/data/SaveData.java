/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.data;

import Project3_6713120.inventory.ItemStack;
import Project3_6713120.items.Item;
import Project3_6713120.world.Player;
import Project3_6713120.world.Plot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // ============================
    // BASIC INFO
    // ============================
    private String farmName;      // ชื่อฟาร์ม (ชื่อเซฟ)
    private String playerName;    // ชื่อผู้เล่น

    private boolean rightSceneUnlocked = false;
    private boolean bottomSceneUnlocked = false;
    private boolean bottomRightSceneUnlocked = false;

    // ============================
    // PLAYER APPEARANCE
    // ============================
    private int skinColor = 0;
    private int hairStyle = 0;
    private int shirtStyle = 0;

    // ============================
    // PLAYER DATA
    // ============================
    private Player player;

    // ============================
    // GAME STATE
    // ============================
    private int money;
    private int buyindex;
    private boolean beanGroundPlanted;
    private ArrayList<Item> inventory = new ArrayList<>();
    private ArrayList<Plot> plots = new ArrayList<>();
    // ============================
    // TIMESTAMP
    // ============================
    private long timestamp;
    // ข้อมูลตัวละคร
    private ItemStack[][] inventoryItems;

    public ItemStack[][] getInventoryItems() {
        return inventoryItems;
    }

    // ========== constructor เดิม ==========
    public SaveData(String farmName) {
        this(farmName, "Ryan");   // default player name เดิม
    }

    public SaveData(String farmName, String playerName) {
        this.farmName = farmName;
        this.playerName = playerName;

        this.player = new Player(playerName);

        this.money = 0;
        this.inventory = new ArrayList<>();
        this.plots = new ArrayList<>();
        this.buyindex = 0;
        beanGroundPlanted = false;
        updateTimestamp();
    }

    /// ///////////ต้นถั่ว

    public boolean isBeanGroundPlanted() {
        return beanGroundPlanted;
    }

    public void setBeanGroundPlanted(boolean planted) {
        this.beanGroundPlanted = planted;
    }

    /// ///////////////////////////////////////////////////
    public void setBuyindex(int index) {
        buyindex = index;
    }

    public int getBuyindex() {
        return buyindex;
    }

    ///
    ///
    public void addmoney(int m) {
        money += m;
    }

    public void removemoney(int m) {
        money -= m;
    }

    // ============================
    // TIMESTAMP
    // ============================
    public long getTimestamp() {
        return timestamp;
    }

    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    // ============================
    // GETTERS / SETTERS
    // ============================

    public String getFarmName() {
        return farmName;
    }

    public String getSaveName() {
        return playerName;
    }

    // --- money ---
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    // --- appearance ---

    public void setSkinColor(int skinColor) {
        this.skinColor = skinColor;
    }

    public int getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(int hairStyle) {
        this.hairStyle = hairStyle;
    }

    public int getShirtStyle() {
        return shirtStyle;
    }

    public void setShirtStyle(int shirtStyle) {
        this.shirtStyle = shirtStyle;
    }

    // --- player ---
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    // --- plots ---
    public void addPlot(Plot plot) {
        plots.add(plot);
    }

    public List<Plot> getPlots() {
        return plots;
    }

    public void setPlots(ArrayList<Plot> plots) {
        this.plots = plots;
    }

    public void setInventoryItems(ItemStack[][] inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public boolean isRightSceneUnlocked() {
        return rightSceneUnlocked;
    }

    public void setRightSceneUnlocked(boolean unlocked) {
        this.rightSceneUnlocked = unlocked;
    }

    public boolean isBottomSceneUnlocked() {
        return bottomSceneUnlocked;
    }

    public void setBottomSceneUnlocked(boolean unlocked) {
        this.bottomSceneUnlocked = unlocked;
    }

    public boolean isBottomRightSceneUnlocked() {
        return bottomRightSceneUnlocked;
    }

    public void setBottomRightSceneUnlocked(boolean unlocked) {
        this.bottomRightSceneUnlocked = unlocked;
    }
}
