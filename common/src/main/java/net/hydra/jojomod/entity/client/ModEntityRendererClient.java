package net.hydra.jojomod.entity.client;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModEntityRendererClient {
    /** Registers the visual component of mobs.
     * Note: Renderers render models, but models are different files.
     * Forge and Fabric need to actually register this info into the game individually.*/
    public static final ModelLayerLocation WOLF_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "wolf"), "main");
    public static final ModelLayerLocation THE_WORLD_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "the_world"), "main");
    public static final ModelLayerLocation STAR_PLATINUM_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "star_platinum"), "main");
    public static final ModelLayerLocation JUSTICE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "justice"), "main");
    public static final ModelLayerLocation MAGICIANS_RED_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "magicians_red"), "main");
    public static final ModelLayerLocation JUSTICE_PIRATE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "justice_pirate"), "main");
    public static final ModelLayerLocation DARK_MIRAGE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "dark_mirage"), "main");
    public static final ModelLayerLocation STAR_PLATINUM_BASEBALL_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "star_platinum_baseball"), "main");
    public static final ModelLayerLocation HARPOON_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "harpoon"), "main");
    public static final ModelLayerLocation KNIFE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "knife"), "main");
    public static final ModelLayerLocation GASOLINE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "gasoline_can"), "main");
    public static final ModelLayerLocation OVA_ENYA_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "jnpc_ova_enya"), "main");
    public static final ModelLayerLocation JOTARO_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "jnpc_jotaro"), "main");
    public static final ModelLayerLocation STEVE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "jnpc_steve"), "main");
    public static final ModelLayerLocation ALEX_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "jnpc_alex"), "main");
    public static final ModelLayerLocation STAND_FIRE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "stand_fire"), "main");


}
