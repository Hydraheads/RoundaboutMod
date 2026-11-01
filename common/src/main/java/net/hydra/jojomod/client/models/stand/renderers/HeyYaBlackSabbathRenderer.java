package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.HeyYaBlackSabbathModel;
import net.hydra.jojomod.client.models.stand.MandomBlackSabbathModel;
import net.hydra.jojomod.entity.stand.HeyYaEntity;
import net.hydra.jojomod.entity.stand.MandomEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class HeyYaBlackSabbathRenderer extends StandRenderer<HeyYaEntity>{

    public static ResourceLocation base = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/base.png");
    public static ResourceLocation goth = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/goth.png");
    public static ResourceLocation volume_2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/volume_2.png");
    public static ResourceLocation chapter_24 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/chapter_24.png");
    public static ResourceLocation greener = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/greener.png");
    public static ResourceLocation warden = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/warden.png");
    public static ResourceLocation fire_and_ice = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/fire_and_ice.png");
    public static ResourceLocation world = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/world.png");
    public static ResourceLocation ice_cold = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/ice_cold.png");
    public static ResourceLocation villager = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/villager.png");
    public static ResourceLocation geezer = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/geezer.png");
    public static ResourceLocation skeleton = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/skeleton.png");
    public static ResourceLocation wither = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/wither.png");
    public static ResourceLocation tusk = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/tusk.png");
    public static ResourceLocation devil = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/devil.png");
    public static ResourceLocation hell_nah = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/hell_nah.png");
    public static ResourceLocation alien = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/alien.png");
    public static ResourceLocation america = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/america.png");
    public static ResourceLocation zombie = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/zombie.png");
    public static ResourceLocation anti = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/anti.png");
    public static ResourceLocation grey_ya = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/grey_ya.png");
    public static ResourceLocation angel = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/angel.png");
    public static ResourceLocation knight = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/knight.png");
    public static ResourceLocation saint = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/saint.png");
    public static ResourceLocation vampire = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/hey_ya/vampire.png");

    public HeyYaBlackSabbathRenderer(EntityRendererProvider.Context context) {
        super(context, new HeyYaBlackSabbathModel<>(context.bakeLayer(ModEntityRendererClient.GRABBED_HEY_YA_LAYER)), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(HeyYaEntity entity) {
        byte BT = entity.getSkin();
        if (BT == HeyYaEntity.MANGA) {
            return base;
        }
        if (BT == HeyYaEntity.GOTHIC) {
            return goth;
        }
        if (BT == HeyYaEntity.VOLUME_2) {
            return volume_2;
        }
        if (BT == HeyYaEntity.CHAPTER_24) {
            return chapter_24;
        }
        if (BT == HeyYaEntity.GREENER) {
            return greener;
        }
        if (BT == HeyYaEntity.WARDEN) {
            return warden;
        }
        if (BT == HeyYaEntity.FIRE_AND_ICE) {
            return fire_and_ice;
        }
        if (BT == HeyYaEntity.WORLD) {
            return world;
        }
        if (BT == HeyYaEntity.ICE_COLD) {
            return ice_cold;
        }
        if (BT == HeyYaEntity.VILLAGER) {
            return villager;
        }
        if (BT == HeyYaEntity.GEEZER) {
            return geezer;
        }
        if (BT == HeyYaEntity.SKELETON) {
            return skeleton;
        }
        if (BT == HeyYaEntity.WITHER) {
            return wither;
        }
        if (BT == HeyYaEntity.TUSK) {
            return tusk;
        }
        if (BT == HeyYaEntity.DEVIL) {
            return devil;
        }
        if (BT == HeyYaEntity.HELL_NAH) {
            return hell_nah;
        }
        if (BT == HeyYaEntity.ALIEN) {
            return alien;
        }
        if (BT == HeyYaEntity.AMERICA) {
            return america;
        }
        if (BT == HeyYaEntity.ZOMBIE) {
            return zombie;
        }
        if (BT == HeyYaEntity.ANTI) {
            return anti;
        }
        if (BT == HeyYaEntity.GREY_YA) {
            return grey_ya;
        }
        if (BT == HeyYaEntity.ANGEL) {
            return angel;
        }
        if (BT == HeyYaEntity.KNIGHT) {
            return knight;
        }
        if (BT == HeyYaEntity.SAINT) {
            return saint;
        }
        if (BT == HeyYaEntity.VAMPIRE) {
            return vampire;
        }
        return base;
    }

    @Nullable
    @Override
    protected RenderType getRenderType(HeyYaEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }
    @Override
    public boolean shouldRender(HeyYaEntity $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        return super.shouldRender($$0,$$1,$$2,$$3,$$4);
    }

    @Override
    public void render(HeyYaEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.60f, 0.60f, 0.60f);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
