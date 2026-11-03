/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.data;

import Project3_6713120.items.*;

import javax.swing.*;
import java.util.ArrayList;

public class GameData {
    public static final ArrayList<Item> ITEMS = new ArrayList<>();
    // Tools
    public static final Item HOE;
    public static final Item WATERING_CAN;

    // Seeds
    public static final Item CARROT_SEEDS;
    public static final Item CORN_SEEDS;
    public static final Item MELON_SEEDS;
    public static final Item PUMPKIN_SEEDS;
    public static final Item BLUEBERRY_SEEDS;
    public static final Item CHERRY_SEEDS;
    public static final Item POTATO_SEEDS;
    public static final Item STRAWBERRY_SEEDS;
    public static final Item BEAN_SEEDS;
    // Fruits/Vegetables
    public static final Item CARROT;
    public static final Item CORN;
    public static final Item MELON;
    public static final Item PUMPKIN;
    public static final Item BLUEBERRY;
    public static final Item CHERRY;
    public static final Item POTATO;
    public static final Item STRAWBERRY;
    public static final Item BEAN;

    static {
        final JFrame window = new JFrame();

        CARROT = register(new VegetableItem("Carrot", 20));
        CORN = register(new VegetableItem("Corn", 40));
        POTATO = register(new VegetableItem("Potato", 60));
        PUMPKIN = register(new VegetableItem("Pumpkin", 80));
        BLUEBERRY = register(new VegetableItem("Blueberry", 30));
        CHERRY = register(new VegetableItem("Cherry", 50));
        MELON = register(new VegetableItem("Melon", 70));
        STRAWBERRY = register(new VegetableItem("Strawberry", 100));
        BEAN = register(new VegetableItem("Bean", 1));
        // === Define Crops ===
        Crop carrotCrop = new Crop("Carrot", 20, (VegetableItem) CARROT);
        Crop cornCrop = new Crop("Corn", 25, (VegetableItem) CORN);
        Crop PotatoCrop = new Crop("Potato", 30, (VegetableItem) POTATO);
        Crop PumpkinCrop = new Crop("Pumpkin", 35, (VegetableItem) PUMPKIN);
        Crop BlueberryCrop = new Crop("Blueberry", 20, (VegetableItem) BLUEBERRY);
        Crop CherryCrop = new Crop("Cherry", 25, (VegetableItem) CHERRY);
        Crop melonCrop = new Crop("Melon", 30, (VegetableItem) MELON);
        Crop StrawberryCrop = new Crop("Strawberry", 40, (VegetableItem) STRAWBERRY);
        Crop BeanCrop = new Crop("Bean", 3, (VegetableItem) BEAN);
        // === Define Seeds ===
        CARROT_SEEDS = register(new SeedItem("Carrot Seeds", carrotCrop, 10));
        CORN_SEEDS = register(new SeedItem("Corn Seeds", cornCrop, 20));
        POTATO_SEEDS = register(new SeedItem("Potato Seeds", PotatoCrop, 30));
        PUMPKIN_SEEDS = register(new SeedItem("Pumpkin Seeds", PumpkinCrop, 40));
        BLUEBERRY_SEEDS = register(new SeedItem("Blueberry Seeds", BlueberryCrop, 15));
        CHERRY_SEEDS = register(new SeedItem("Cherry Seeds", CherryCrop, 25));
        MELON_SEEDS = register(new SeedItem("Melon Seeds", melonCrop, 35));
        STRAWBERRY_SEEDS = register(new SeedItem("Strawberry Seeds", StrawberryCrop, 45));
        BEAN_SEEDS = register(new SeedItem("Bean Seeds", BeanCrop, 5000));
        // === Define Tools ===

        HOE = register(new ToolItem("Hoe", 0));
        WATERING_CAN = register(new ToolItem("Watering Can", 0));
    }

    private static Item register(Item item) {
        ITEMS.add(item);
        return item;
    }

}

