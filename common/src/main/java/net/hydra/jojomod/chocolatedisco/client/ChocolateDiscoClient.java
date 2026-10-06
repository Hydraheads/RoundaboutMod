package net.hydra.jojomod.chocolatedisco.client;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoModel;
import net.hydra.jojomod.chocolatedisco.client.models.stand.ChocolateDiscoSlimModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/** Shared Chocolate Disco client identifiers. Platform entrypoints perform the actual event registration. */
public final class ChocolateDiscoClient {
    public static final ModelLayerLocation CHOCOLATE_DISCO_LAYER =
            new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "chocolate_disco"), "main");
    public static final ModelLayerLocation CHOCOLATE_DISCO_SLIM_LAYER =
            new ModelLayerLocation(new ResourceLocation(Roundabout.MOD_ID, "chocolate_disco_slim"), "main");

    private ChocolateDiscoClient() {}

    public static net.minecraft.client.model.geom.builders.LayerDefinition defaultLayer() {
        return ChocolateDiscoModel.getTexturedModelData();
    }

    public static net.minecraft.client.model.geom.builders.LayerDefinition slimLayer() {
        return ChocolateDiscoSlimModel.getTexturedModelData();
    }
}
