package net.hydra.jojomod.client.models.stand.renderers;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.PearlJamModel;
import net.hydra.jojomod.entity.stand.PearlJamEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersPearlJam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class PearlJamRenderer extends StandRenderer<PearlJamEntity> {


    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/pearl_jam/anime.png");
    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/pearl_jam/manga.png");

    public PearlJamRenderer(EntityRendererProvider.Context context) {
        super(context, new PearlJamModel<>(context.bakeLayer(ModEntityRendererClient.PEARL_JAM_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(PearlJamEntity entity) {
        switch (entity.getSkin()){
            case PearlJamEntity.MANGA:
                return MANGA;
            default:
                return ANIME;
        }
    }

    @Override
    public void render(PearlJamEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 1;
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.68f * factor, 0.68f * factor, 0.68f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected @Nullable RenderType getRenderType(PearlJamEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
