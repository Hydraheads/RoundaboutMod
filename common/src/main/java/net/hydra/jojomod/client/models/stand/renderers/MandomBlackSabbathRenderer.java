package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.MandomLayer;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.BlackSabbathModel;
import net.hydra.jojomod.client.models.stand.MandomBlackSabbathModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.HeyYaEntity;
import net.hydra.jojomod.entity.stand.MandomEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class MandomBlackSabbathRenderer extends StandRenderer<MandomEntity>{

    public static ResourceLocation base = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/base.png");
    public static ResourceLocation purple = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/purple.png");
    public static ResourceLocation sky = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/sky.png");
    public static ResourceLocation squid = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/squid.png");
    public static ResourceLocation glow_squid = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/glow_squid.png");
    public static ResourceLocation rose = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/rose.png");
    public static ResourceLocation nautilus = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/nautilus.png");
    public static ResourceLocation alien = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/alien.png");
    public static ResourceLocation dark = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/dark.png");
    public static ResourceLocation jellyfish = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/jellyfish.png");
    public static ResourceLocation happy = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/happy.png");
    public static ResourceLocation melon = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/melon.png");
    public static ResourceLocation eye = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/eye.png");
    public static ResourceLocation esidisi = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/esidisi.png");
    public static ResourceLocation caps = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/caps.png");
    public static ResourceLocation command = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/command.png");
    public static ResourceLocation command2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/emissive/command.png");
    public static ResourceLocation sculk = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/sculk.png");
    public static ResourceLocation sculk2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/emissive/sculk.png");
    public static ResourceLocation tv = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/tv.png");
    public static ResourceLocation tv2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/emissive/tv.png");

    public MandomBlackSabbathRenderer(EntityRendererProvider.Context context) {
        super(context, new MandomBlackSabbathModel<>(context.bakeLayer(ModEntityRendererClient.GRABBED_MANDOM_LAYER)), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(MandomEntity entity) {
        byte BT = entity.getSkin();
        if (BT == MandomEntity.MANGA) {
            return base;
        }
        if (BT == MandomEntity.PURPLE) {
            return purple;
        }
        if (BT == MandomEntity.SKY) {
            return sky;
        }
        if (BT == MandomEntity.SQUID) {
            return squid;
        }
        if (BT == MandomEntity.GLOW_SQUID) {
            return glow_squid;
        }
        if (BT == MandomEntity.ROSE) {
            return rose;
        }
        if (BT == MandomEntity.NAUTILUS) {
            return nautilus;
        }
        if (BT == MandomEntity.ALIEN) {
            return alien;
        }
        if (BT == MandomEntity.DARK) {
            return dark;
        }
        if (BT == MandomEntity.JELLYFISH) {
            return jellyfish;
        }
        if (BT == MandomEntity.HAPPY) {
            return happy;
        }
        if (BT == MandomEntity.EYE) {
            return eye;
        }
        if (BT == MandomEntity.MELON) {
            return melon;
        }
        if (BT == MandomEntity.ESIDISI) {
            return esidisi;
        }
        if (BT == MandomEntity.COMMAND) {
            return command;
        }
        if (BT == MandomEntity.SCULK) {
            return sculk;
        }
        if (BT == MandomEntity.TV) {
            return tv;
        }
        if (BT == MandomEntity.CAPS) {
            return caps;
        }
        return base;
    }

    @Override
    public void render(MandomEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(MandomEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }
    @Override
    public boolean shouldRender(MandomEntity $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        return super.shouldRender($$0,$$1,$$2,$$3,$$4);
    }
}
