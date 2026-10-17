package net.hydra.jojomod.client.models.badCompanyTroops.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.badCompanyTroops.BCSoldierModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.badCompanyTroops.BCSoldier;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;

public class BCSoldierRenderer extends BCSoldierBaseRenderer{
    public BCSoldierRenderer(EntityRendererProvider.Context context) {
        super(context, new BCSoldierModel<>(context.bakeLayer(ModEntityRendererClient.BAD_COMPANY_SOLDIER_LAYER)), 0f);
    }

    @Override
    public void render(BCSoldier mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BCSoldier entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
