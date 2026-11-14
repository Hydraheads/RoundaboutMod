package net.hydra.jojomod.client.models;

import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.mixin.access.AccessBoatRenderer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.HashMap;
import java.util.Map;

public class GinkgoBoatRenderer extends BoatRenderer {

    public GinkgoBoatRenderer(EntityRendererProvider.Context context, boolean chest) {
        super(context, chest);

        ResourceLocation texture = new ResourceLocation(
                "roundabout",
                chest ? "textures/entity/chest_boat/ginkgo.png"
                        : "textures/entity/boat/ginkgo.png"
        );

        AccessBoatRenderer accessor = (AccessBoatRenderer) this;

        Map<Boat.Type, Pair<ResourceLocation, ListModel<Boat>>> resources =
                new HashMap<>(accessor.roundabout$getBoatResources());

        resources.replaceAll((type, pair) ->
                Pair.of(texture, pair.getSecond()));

        accessor.roundabout$setBoatResources(resources);
    }
}