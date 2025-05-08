package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SoftAndWetRenderer extends SoftAndWetBaseRenderer<SoftAndWetEntity> {
    public SoftAndWetRenderer(EntityRendererProvider.Context context) {
        super(context, new SoftAndWetModel<>(context.bakeLayer(ModEntityRendererClient.SOFT_AND_WET_LAYER)));
    }
}

