package net.hydra.jojomod.client.models.npcs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.client.models.visages.ZombieVisageBasisModel;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.item.MaskItem;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ZombieAestheticianRenderer<T extends ZombieAesthetician> extends HumanoidMobRenderer<T, ZombieVisageBasisModel<T>> {
    private static final ResourceLocation AES1 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/npcs/zombie_aesthetician.png");

    public ZombieAestheticianRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieVisageBasisModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), false), 0.5F);
        this.addLayer(new VisagePartLayer<>(context, this));
    }

    @Override
    public void render(T mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        matrixStack.scale(0.8F, 0.839F, 0.8F);
        super.render(mobEntity,f,g,matrixStack,vertexConsumerProvider,i);
    }
    @Override
    public ResourceLocation getTextureLocation(T t) {
        ItemStack visage = t.getBasis();
        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                if (MI.visageData.isCharacterVisage()) {
                    return (new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_skins/"+MI.visageData.getSkinPath()+".png"));
                }
            }
        }
        return null;
    }


    protected boolean isShaking(ZombieAesthetician zombieAesthetician) {
        return super.isShaking((T) zombieAesthetician) || zombieAesthetician.isConverting();
    }
}
