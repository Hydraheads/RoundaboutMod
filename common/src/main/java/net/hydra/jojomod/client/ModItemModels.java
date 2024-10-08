package net.hydra.jojomod.client;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.HarpoonModel;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ModItemModels {
    public static ModelResourceLocation HARPOON_IN_HAND = new ModelResourceLocation(Roundabout.MOD_ID, "harpoon_in_hand", "inventory");
    public static ModelResourceLocation STAND_BOW = new ModelResourceLocation(Roundabout.MOD_ID, "stand_bow_arrow", "inventory");
    public static ModelResourceLocation STAND_BEETLE_BOW = new ModelResourceLocation(Roundabout.MOD_ID, "stand_beetle_bow_arrow", "inventory");
    public static ModelResourceLocation WORTHY_BOW = new ModelResourceLocation(Roundabout.MOD_ID, "worthy_bow_arrow", "inventory");

    public static HarpoonModel HARPOON_MODEL;
}
