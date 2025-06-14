package net.hydra.jojomod.client.models.npcs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.npcs.ZombieAestheticianModel;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ZombieAestheticianRenderer<T extends ZombieAesthetician> extends NonJojoNPCRenderer<T> {
    private static final ResourceLocation AES1 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/zombie_aesthetician.png");

    public ZombieAestheticianRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieAestheticianModel<>(context.bakeLayer(ModEntityRendererClient.ZOMBIE_AESTHETICIAN_LAYER)),0.5F);
    }

    @Override
    public void render(T mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.8F, 0.839F, 0.8F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(T t) {
        return AES1;
    }

    protected boolean isShaking(ZombieAesthetician zombieAesthetician) {
        return super.isShaking((T) zombieAesthetician) || zombieAesthetician.isConverting();
    }
}
