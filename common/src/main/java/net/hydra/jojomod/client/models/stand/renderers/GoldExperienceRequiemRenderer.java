package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GoldExperienceBetaModel;
import net.hydra.jojomod.client.models.stand.GoldExperienceModel;
import net.hydra.jojomod.client.models.stand.GoldExperienceRequiemModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.GoldExperienceRequiemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GoldExperienceRequiemRenderer extends GoldExperienceRequiemBaseRenderer {
    public GoldExperienceRequiemRenderer(EntityRendererProvider.Context context) {
        super(context, new GoldExperienceRequiemModel<>(context.bakeLayer(ModEntityRendererClient.GOLD_EXPERIENCE_REQUIEM_LAYER)), 0f);
    }
}