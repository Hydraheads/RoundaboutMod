package net.hydra.jojomod.client;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.HarpoonModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ModItemModels {
    public static ModelResourceLocation HARPOON_IN_HAND = new ModelResourceLocation(Roundabout.MOD_ID, "harpoon_in_hand", "inventory");
    public static ModelResourceLocation STAND_BOW = new ModelResourceLocation(Roundabout.MOD_ID, "stand_bow_arrow", "inventory");
    public static ModelResourceLocation STAND_BEETLE_BOW = new ModelResourceLocation(Roundabout.MOD_ID, "stand_beetle_bow_arrow", "inventory");
    public static ModelResourceLocation WORTHY_BOW = new ModelResourceLocation(Roundabout.MOD_ID, "worthy_bow_arrow", "inventory");
    public static ModelResourceLocation FOG_BLOCK_ICON = new ModelResourceLocation(Roundabout.MOD_ID, "fog_block_icon", "inventory");
    public static ModelResourceLocation STREET_SIGN_DIO_D = new ModelResourceLocation(Roundabout.MOD_ID, "street_sign_dio_item_damaged", "inventory");
    public static ModelResourceLocation STREET_SIGN_DIO_D2 = new ModelResourceLocation(Roundabout.MOD_ID, "street_sign_dio_item_damaged_2", "inventory");
    public static ModelResourceLocation STREET_SIGN_DIO_HELD = new ModelResourceLocation(Roundabout.MOD_ID, "street_sign_dio_item_held", "inventory");
    public static ModelResourceLocation STAND_ARROW_CROSSBOW =
            new ModelResourceLocation(Roundabout.MOD_ID, "crossbow_stand_arrow", "inventory");
    public static ModelResourceLocation STAND_BEETLE_CROSSBOW =
            new ModelResourceLocation(Roundabout.MOD_ID, "crossbow_beetle_arrow", "inventory");
    public static ModelResourceLocation STAND_WORTHY_CROSSBOW =
            new ModelResourceLocation(Roundabout.MOD_ID, "crossbow_worthy_arrow", "inventory");

    public static ModelResourceLocation DREAD_BOOK =
            new ModelResourceLocation(Roundabout.MOD_ID, "dread_book", "inventory");

    public static HarpoonModel HARPOON_MODEL;

}
