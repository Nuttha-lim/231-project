/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import javax.swing.*;

/**
 *
 * @author jakka
 */
public interface UiConfig {
    static final JFrame window = new JFrame();
    String Path = "src/main/java/Project3_6713120/resource/";
    String bgimg = Path + "Background.png";
    String iconimg = Path + "COValleyLogo.PNG";
    String newbutimg = Path + "New.PNG";
    String loadbutimg = Path + "Load.PNG";
    String creditbutimg = Path + "Credit.PNG";
    String settingbutimg = Path + "Setting.PNG";
    String musicbutimg = Path + "Music.PNG";
    String ofmusicbut = Path + "offmusic.PNG";
    String hopimg = Path + "HowToPlay.png";
    String settingframe = Path + "SettingFrame.PNG";
    String quitimg = Path + "Exit.PNG";
    String houseimg = Path + "Main house.PNG";
    String fenceimg = Path + "Soilfence.PNG";
    String treeimg = Path + "Tree.PNG";
    String bushimg = Path + "Bush.PNG";
    String soundtheam = Path + "theme.wav";
    String clicks = Path + "Click.wav";
    String optionframe = Path + "OptionFrame2.PNG";
    String quitbutop = Path + "QuitButtonwithText.PNG";
    String hommebutop = Path + "HomeButtonwithText.PNG";
    String shoppimg = Path + "BuyAndSellFrame.PNG";
    String buybutimg = Path + "BuyButton.PNG";
    String sellbutimg = Path + "SellButton.PNG";
    String carrotimg = Path + "CarrotFinal.PNG";
    String Cornimg = Path + "CornFinal.PNG";
    String coinslot = Path + "Coins.PNG";
    String coin = Path + "Coin.PNG";
    String Potatoimg = Path + "PotatoFinal.PNG";
    String pumknimg = Path + "pumkin.PNG";
    String blueberryimg = Path + "BlueberryFinal.PNG";
    String cherryimg = Path + "CherryFinal.PNG";
    String melonimg = Path + "MelonFinal.PNG";
    String strawberryimg = Path + "StrawberryFinal.PNG";
    String Beanimg = Path + "Beans.PNG";
    String carrotseed = Path + "CarrotBag.PNG";
    String cornseeds = Path + "CornBag.PNG";
    String potatoseed = Path + "PotatoBag.PNG";
    String pumkinseed = Path + "PumpkinBag.PNG";
    String blueberryseed = Path + "BlueberryBag.PNG";
    String cherryseed = Path + "CherryBag.PNG";
    String melonseed = Path + "MelonBag.PNG";
    String strawberryseed = Path + "StrawberryBag.PNG";
    String Beanseed = Path + "BeanBag.PNG";
    String backimg = Path + "BackButton.png";
    String creditbg = Path + "creditBG.PNG";
    String creditChelsea = Path + "Chelsea.jpg";
    String creditGamyui = Path + "Gamyui.JPG";
    String creditRyan = Path + "Ryan.PNG";
    String creditMix = Path + "Mix.jpg";
    String creditYok = Path + "Yok.jpg";
    String SOIL_BASE_IMG1 = Path + "Soil1.PNG";
    String SOIL_BASE_IMG2 = Path + "Soil2.PNG";
    String SOIL_BASE_IMG3 = Path + "Soil3.PNG";
    String SOIL_BASE_IMG4 = Path + "Soil4.PNG";
    String SOIL_WATER_IMG1 = Path + "SoilWater1.PNG";
    String SOIL_WATER_IMG2 = Path + "SoilWater2.PNG";
    String SOIL_WATER_IMG3 = Path + "SoilWater3.PNG";
    String SOIL_WATER_IMG4 = Path + "SoilWater4.PNG";
    String SOIL_TILL_IMG1 = Path + "SoilTilled1.PNG";
    String SOIL_TILL_IMG2 = Path + "SoilTilled2.PNG";
    String SOIL_TILL_IMG3 = Path + "SoilTilled3.PNG";
    String SOIL_TILL_IMG4 = Path + "SoilTilled4.PNG";
    String rockimg = Path + "Rock.PNG";
    String treeGroupImg = Path + "TreeGroup.PNG";
    String housemain = Path + "housemain.PNG";
    String fenceimgmenu = Path + "fence.png";
    String fenceRightImg = Path + "fenceright.png";
    String fenceBottomImg = Path + "fencecnbt.png";
    String treeGroupBottomImg = Path + "treegroupbt.png";
    String fenceBottomRightImg = Path + "fencebtR.png";
    String sandCastleImg = Path + "sandCas.png";
    String lockRightImg = Path + "Arrow/RightLock.PNG";
    String lockDownImg = Path + "Arrow/DownLock.PNG";
    String lockLeftImg = Path + "Arrow/LeftLock.PNG";
    String arrowRightImg = Path + "Arrow/Right.PNG";
    String arrowDownImg = Path + "Arrow/Down.PNG";
    String arrowLeftImg = Path + "Arrow/Left.PNG";
    String arrowUpImg = Path + "Arrow/Up.PNG";
    String lockUpImg = Path + "Arrow/UpLock.PNG";

    String fullbeangif = Path + "BeanstalkShort.GIF";
    String cup = Path + "Bean1.PNG";
    int TILE_WIDTH = 85;   // ขนาดความกว้าง (เดิม 95) -> ปรับลดลง
    int TILE_HEIGHT = 64;  // ขนาดความสูง (เดิม 74) -> ปรับลดลง
    int TILE_GAP = 65;

    ImageIcon SOIL_WATER_1 = new MyImageIcon(SOIL_WATER_IMG1).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_WATER_2 = new MyImageIcon(SOIL_WATER_IMG2).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_WATER_3 = new MyImageIcon(SOIL_WATER_IMG3).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_WATER_4 = new MyImageIcon(SOIL_WATER_IMG4).resize(TILE_WIDTH, TILE_HEIGHT);

    ImageIcon SOIL_TILL_1 = new MyImageIcon(SOIL_TILL_IMG1).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_TILL_2 = new MyImageIcon(SOIL_TILL_IMG2).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_TILL_3 = new MyImageIcon(SOIL_TILL_IMG3).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_TILL_4 = new MyImageIcon(SOIL_TILL_IMG4).resize(TILE_WIDTH, TILE_HEIGHT);

    ImageIcon SOIL_BASE_1 = new MyImageIcon(SOIL_BASE_IMG1).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_BASE_2 = new MyImageIcon(SOIL_BASE_IMG2).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_BASE_3 = new MyImageIcon(SOIL_BASE_IMG3).resize(TILE_WIDTH, TILE_HEIGHT);
    ImageIcon SOIL_BASE_4 = new MyImageIcon(SOIL_BASE_IMG4).resize(TILE_WIDTH, TILE_HEIGHT);

    String TOP_BEACH = Path + "Beach1.PNG";
    String BOTTOM_BEACH = Path + "Beach2.PNG";
    String VEG_STORE_IMG = Path + "VegetableStore.PNG";
    String TABLE_SET_IMG = Path + "TableSet.PNG";
    String FISHER_HOUSE_IMG = Path + "FishermanHouse.PNG";
    String SHELL_IMG = Path + "Shell.PNG";
    String SWIMTUBE_IMG = Path + "SwimTube.PNG";
    String HTP = Path + "HowToPlayFrame.png";
    String NEWFRAME_IMG = Path + "NewFrame.png";
    String OKBUTTON_IMG = Path + "OKButton.png";
    String TAB_HAIR = Path + "Hair.png";
    String TAB_SHIRT = Path + "Shirt.png";
    String TAB_PANTS = Path + "Pants.png";
    String TAB_SELECTED = Path + "Selected.png";
    String Delete = Path + "DeleteButton.png";
    String Plus = Path + "Plus.png";
    String Play = Path + "PlayButton.png";
    String Load = Path + "AllLoad.png";
    String Back = Path + "BackButton.png";
    String wateringcanimg = Path + "Inventory/Tools&Seeds/WateringCan.PNG";
    String hoeimg = Path + "Inventory/Tools&Seeds/Hoe.PNG";


    String PATH_BODY = Path + "Character/Body/";
    String PATH_HAIR = Path + "Character/Hair/";
    String PATH_SHIRT = Path + "Character/Shirt/";
    String PATH_PANTS = Path + "Character/Skirt&Pants/";

    int borderxbut = 100;
    int borderybut = 280;
    int framew = 1366;
    int frameh = 768;
    int widthbut = 280;
    int heightbut = 85;
    int PERSON_WIDTH = 150;
    int PERSON_HEIGHT = 150;
    int SHOP_HEIGHT = 193;
    int SHOP_WIDTH = 193;

}
