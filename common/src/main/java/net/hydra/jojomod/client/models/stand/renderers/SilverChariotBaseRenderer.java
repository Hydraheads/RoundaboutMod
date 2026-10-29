package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.SilverChariotAfterimageState;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.SilverChariotModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.Random;

public class SilverChariotBaseRenderer<T extends StandEntity> extends StandRenderer<SilverChariotEntity> {
    private static final ResourceLocation PART_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/silver_chariot.png");
    private static final ResourceLocation PART_3_GREY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/anime_silver_chariot.png");
    private static final ResourceLocation PART_3_MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/manga_silver_chariot.png");
    private static final ResourceLocation PART_5 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/part_5_silver_chariot.png");
    private static final ResourceLocation AQUA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/aqua_silver_chariot.png");
    private static final ResourceLocation AZURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/azure_silver_chariot.png");
    private static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/blue_silver_chariot.png");
    private static final ResourceLocation CRYSTAL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/crystal_silver_chariot.png");
    private static final ResourceLocation END_OF_THE_WORLD = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/end_of_the_world_silver_chariot.png");
    private static final ResourceLocation GENESIS_OF_THE_UNIVERSE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/genesis_of_the_universe_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_A = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_a_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_B = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_b_silver_chariot.png");
    private static final ResourceLocation JOJONIUM_C = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/jojonium_c_silver_chariot.png");
    private static final ResourceLocation NIGHTMARE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/nightmare_silver_chariot.png");
    private static final ResourceLocation ORANGE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/orange_silver_chariot.png");
    private static final ResourceLocation PASSIONE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/passione_silver_chariot.png");
    private static final ResourceLocation PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/purple_silver_chariot.png");
    private static final ResourceLocation SANDY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/sandy_silver_chariot.png");
    private static final ResourceLocation TURQUOISE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/turquoise_silver_chariot.png");
    private static final ResourceLocation YELLOW = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/yellow_silver_chariot.png");
    private static final ResourceLocation PHARAOH = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/silver_chariot/pharaoh_silver_chariot.png");

    public SilverChariotBaseRenderer(EntityRendererProvider.Context context, StandModel<SilverChariotEntity> root) {
        // super(context, new SilverChariotModel<>(context.bakeLayer(ModEntityRendererClient.SILVER_CHARIOT_LAYER)), 0.0F);
        super(context, root, 0f);
        /*
        addLayer(
                new SilverChariotItemLayer(

                )
        );
        */
    }

    public static ResourceLocation getSkin(byte bt) {
        switch (bt) {
            case SilverChariotEntity.PART_3 -> {
                return PART_3;
            }
            case SilverChariotEntity.PART_3_GREY -> {
                return PART_3_GREY;
            }
            case SilverChariotEntity.PART_3_MANGA -> {
                return PART_3_MANGA;
            }
            case SilverChariotEntity.PART_5 -> {
                return PART_5;
            }
            case SilverChariotEntity.AQUA -> {
                return AQUA;
            }
            case SilverChariotEntity.AZURE -> {
                return AZURE;
            }
            case SilverChariotEntity.BLUE -> {
                return BLUE;
            }
            case SilverChariotEntity.CRYSTAL -> {
                return CRYSTAL;
            }
            case SilverChariotEntity.END_OF_THE_WORLD -> {
                return END_OF_THE_WORLD;
            }
            case SilverChariotEntity.GENESIS_OF_THE_UNIVERSE -> {
                return GENESIS_OF_THE_UNIVERSE;
            }
            case SilverChariotEntity.JOJONIUM_A -> {
                return JOJONIUM_A;
            }
            case SilverChariotEntity.JOJONIUM_B -> {
                return JOJONIUM_B;
            }
            case SilverChariotEntity.JOJONIUM_C -> {
                return JOJONIUM_C;
            }
            case SilverChariotEntity.NIGHTMARE -> {
                return NIGHTMARE;
            }
            case SilverChariotEntity.ORANGE -> {
                return ORANGE;
            }
            case SilverChariotEntity.PASSIONE -> {
                return PASSIONE;
            }
            case SilverChariotEntity.PURPLE -> {
                return PURPLE;
            }
            case SilverChariotEntity.SANDY -> {
                return SANDY;
            }
            case SilverChariotEntity.TURQUOISE -> {
                return TURQUOISE;
            }
            case SilverChariotEntity.YELLOW -> {
                return YELLOW;
            }
            case SilverChariotEntity.PHARAOH -> {
                return PHARAOH;
            }
        }
        return PART_3;
    }

    @Override
    public ResourceLocation getTextureLocation(SilverChariotEntity entity) {
        byte bt = entity.getSkin();
        return getSkin(bt);
    }

    @Override
    public void render(
            SilverChariotEntity mobEntity,
            float entityYaw,
            float partialTicks,
            PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider,
            int packedLight
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        // super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }

        super.render(mobEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLight);

        // float alpha = 0.50F;
        // float alpha = ConfigManager.getClientConfig().silverChariotSettings.silverChariotAfterimageOpacity;
        float alpha = 1.0F;
        boolean renderAfterimages = ConfigManager.getClientConfig().silverChariotSettings.silverChariotRenderAfterimages;
        boolean renderAfterimagesInFirstPerson = ConfigManager.getClientConfig().silverChariotSettings.silverChariotShouldRenderAfterimagesForUserWhileInFirstPerson;
        if (renderAfterimages && !mobEntity.getArmoured() && mobEntity.getAfterimageStates().size() >= 2) {
            // renderAfterimage1(mobEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLight, alpha, 2.5D, -2.0D, -1.5D);
            // renderAfterimage1(mobEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLight, alpha, 1.5D, -2.0D, 3.5D);
            // renderAfterimage1(mobEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLight, alpha, -1.5D, -2.0D, 3.5D);
            // renderAfterimage1(mobEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLight, alpha, -2.5D, -2.0D, -1.5D);
            // this.model.setAlpha(alpha);
            this.model.stand.visible = !(renderAfterimagesInFirstPerson
                    && mobEntity.getUser() == minecraft.player
                    && minecraft.options.getCameraType().isFirstPerson());
            this.renderAfterimages(mobEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLight, alpha);
            this.model.stand.visible = true;
            this.model.head.visible = !(mobEntity.isRemoteControlled()
                    && mobEntity.getUser() == minecraft.player
                    && minecraft.options.getCameraType().isFirstPerson());
        }
        // this.model.setAlpha(1f);
        // this.model.getHead().visible = true;
        // super.render(mobEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, packedLight);
    }

    public void renderAfterimage1(SilverChariotEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, float alpha, double x, double y, double z) {

        matrixStack.pushPose();

        this.setupRotations(mobEntity, matrixStack, mobEntity.tickCount + g, f, g);
        this.scale(mobEntity, matrixStack, g);
        matrixStack.mulPose((Axis.ZP.rotationDegrees(-180.0F)));
        matrixStack.translate(x, y, z);

        // VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(getTextureLocation(mobEntity)));
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(mobEntity)));
        this.model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        matrixStack.popPose();
    }

    public void renderOffhandItem(SilverChariotEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        StandModel<SilverChariotEntity> model = this.getModel();

        matrixStack.pushPose();

        Minecraft minecraft = Minecraft.getInstance();

        minecraft.getItemRenderer().renderStatic(
                mobEntity.getOffhandItem(),
                ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                i,
                OverlayTexture.NO_OVERLAY,
                matrixStack,
                vertexConsumerProvider,
                mobEntity.level(),
                mobEntity.getId()
        );

        matrixStack.popPose();
    }

    private Random rand = new Random();

    public static Vec3 interpolate(
            SilverChariotAfterimageState oldState,
            SilverChariotAfterimageState newState,
            float partialTick
    ) {
        return oldState.getPos().lerp(newState.getPos(), partialTick);
    }

    private void renderAfterimage1(
            SilverChariotEntity mobEntity,
            Deque<SilverChariotAfterimageState> deque,
            int moment,
            float alpha,
            float partialTick,
            PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider,
            int i
    ) {

    }

    public void renderAfterimages(
            SilverChariotEntity mobEntity,
            float entityYaw,
            float g,
            PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider,
            int i,
            float alpha) {
        Vec3 currentPos = mobEntity.getPosition(g);
        // Vec3 currentPos = mobEntity.position();
        int index = 0;

        float yaw = (float) Math.toRadians(-mobEntity.getYRot());

        float xOffset = Mth.cos(yaw);
        float zOffset = Mth.sin(yaw);

        float initOffset = -1.6F;
        float offsetSpacing = 0.8F;

        for (SilverChariotAfterimageState state : mobEntity.getAfterimageStates()) {
            Vec3 prevPos = state.getPos();
            float prevXRot = state.getXRot();
            float prevYRot = state.getYRot();

            Vec3 relativePos = prevPos.subtract(currentPos);

            matrixStack.pushPose();
            matrixStack.translate(
                    relativePos.x,
                    relativePos.y + 1.5D,
                    relativePos.z
            );
            this.setupRotations(mobEntity, matrixStack, mobEntity.tickCount + g, entityYaw, g);
            // this.setupRotations(mobEntity, matrixStack, g, entityYaw, g);
            matrixStack.mulPose((Axis.ZP.rotationDegrees(180.0F)));
            this.scale(mobEntity, matrixStack, g);

            // VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(getTextureLocation(mobEntity)));
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(mobEntity)));
            // this.model.head.visible = true;
            this.model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
            matrixStack.popPose();
            index += 1;
            index = index % 5;
            if (index % 5 == 2) {
                index += 1;
            }
        }
    }

    public void renderAfterimages1(
            SilverChariotEntity mobEntity,
            float entityYaw,
            float g,
            PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider,
            int i,
            float alpha) {
        Vec3 currentPos = mobEntity.getPosition(g);
        // Vec3 currentPos = mobEntity.position();
        int index = 0;

        float yaw = (float) Math.toRadians(-mobEntity.getYRot());

        float xOffset = Mth.cos(yaw);
        float zOffset = Mth.sin(yaw);

        float initOffset = -1.6F;
        float offsetSpacing = 0.8F;

        for (SilverChariotAfterimageState state : mobEntity.getAfterimageStates()) {
            Vec3 prevPos = state.getPos();
            float prevXRot = state.getXRot();
            float prevYRot = state.getYRot();

            Vec3 relativePos = prevPos.subtract(currentPos);

            matrixStack.pushPose();
            matrixStack.translate(
                    relativePos.x,
                    relativePos.y + 1.5D,
                    relativePos.z
            );
            this.setupRotations(mobEntity, matrixStack, mobEntity.tickCount + g, entityYaw, g);
            // this.setupRotations(mobEntity, matrixStack, g, entityYaw, g);
            matrixStack.mulPose((Axis.ZP.rotationDegrees(180.0F)));
            this.scale(mobEntity, matrixStack, g);

            // VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(getTextureLocation(mobEntity)));
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(mobEntity)));
            // this.model.head.visible = true;
            this.model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
            matrixStack.popPose();
            index += 1;
            index = index % 5;
            if (index % 5 == 2) {
                index += 1;
            }
        }
    }

    @Nullable
    @Override
    protected RenderType getRenderType(SilverChariotEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        // return super.getRenderType(entity, showBody, true, showOutline);
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }
}
