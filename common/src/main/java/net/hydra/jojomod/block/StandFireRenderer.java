package net.hydra.jojomod.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class StandFireRenderer implements BlockEntityRenderer<StandFireBlockEntity> {

    private final BlockRenderDispatcher itemRenderer;

    public StandFireRenderer(BlockEntityRendererProvider.Context p_173554_) {
        this.itemRenderer = p_173554_.getBlockRenderDispatcher();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("bell_body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F), PartPose.offset(8.0F, 12.0F, 8.0F));
        partdefinition1.addOrReplaceChild("bell_base", CubeListBuilder.create().texOffs(0, 13).addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F), PartPose.offset(-8.0F, -12.0F, -8.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    private boolean isFacingDown(StandFireBlockEntity fire)
    {
        BlockState s = fire.getBlockState();
        return !(s.getValue(StandFireBlock.UP) || s.getValue(StandFireBlock.NORTH) || s.getValue(StandFireBlock.EAST) || s.getValue(StandFireBlock.SOUTH) || s.getValue(StandFireBlock.WEST));
    }

    public void render(StandFireBlockEntity fire, float partialTick, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer lp = client.player;

        if (lp != null && (!((StandUser)lp).roundabout$getStandDisc().isEmpty() ||
            lp.isSpectator() || !ConfigManager.getClientConfig().onlyStandUsersCanSeeStands))
        {
            matrices.pushPose();

            BlockState aboveState = client.level.getBlockState(fire.getBlockPos().above());

            if (!aboveState.isAir() && !(aboveState.getBlock() instanceof StandFireBlock) && isFacingDown(fire))
            {
                matrices.mulPose(Axis.ZP.rotationDegrees(180));
                matrices.translate(-1.f, -1.f, 0.f);
            }

            itemRenderer.renderBatched(ModBlocks.ORANGE_FIRE.withPropertiesOf(fire.getBlockState()), fire.getBlockPos(), client.level, matrices, buffer.getBuffer(RenderType.cutout()), true, lp.getRandom());
            matrices.popPose();
        }
    }


    /**
    private static void renderLidModel(
            FramedChestBlockEntiy be,
            BlockState state,
            PoseStack matrix,
            MultiBufferSource buffer,
            BakedModel model,
            ModelData data
    )
    {
        ModelBlockRenderer renderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
        //noinspection ConstantConditions
        RandomSource rand = be.getLevel().getRandom();

        int color = Minecraft.getInstance().getBlockColors().getColor(state, be.getLevel(), be.getBlockPos(), 0);
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;

        int light = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos());

        for (RenderType type : model.getRenderTypes(state, rand, data))
        {
            RenderType bufferType = RenderTypeHelper.getEntityRenderType(type, false);

            renderer.renderModel(
                    matrix.last(),
                    buffer.getBuffer(bufferType),
                    state,
                    model,
                    red, green, blue,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    data,
                    type
            );
        }
    }
     **/
}