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
    public static final ModelLayerLocation HARPOON_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "harpoon"), "main");
    public static final ModelLayerLocation KNIFE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "knife"), "main");
    public static final ModelLayerLocation GASOLINE_LAYER = new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "gasoline_can"), "main");


}
