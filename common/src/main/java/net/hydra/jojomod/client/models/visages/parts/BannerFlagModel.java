package net.hydra.jojomod.client.models.visages.parts;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

public class BannerFlagModel extends PsuedoHierarchicalModel {
    private static final int BANNER_WIDTH = 20;
    private static final int BANNER_HEIGHT = 40;
    private static final int MAX_PATTERNS = 16;
    public static final String FLAG = "flag";
    private static final String POLE = "pole";
    private static final String BAR = "bar";
    private final ModelPart bar;
    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart root;

    public BannerFlagModel() {
        ModelPart $$1 = createBodyLayer().bakeRoot();
        this.root = $$1;
        this.flag = $$1.getChild("flag");
        this.pole = $$1.getChild("pole");
        this.bar = $$1.getChild("bar");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("flag", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("bar", CubeListBuilder.create().texOffs(0, 42).addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    @Unique
    public void roundabout$renderInSpot(ItemStack stack, Vec3 pos, Level $$0, float $$1, PoseStack $$2,
                                        MultiBufferSource $$3, int $$4, int $$5, float scale) {
        if (stack.getItem() instanceof BannerItem bi && bi.getBlock() instanceof BannerBlock bb) {
            long $$9 = $$9 = $$0.getGameTime();
            List<Pair<Holder<BannerPattern>, DyeColor>> $$6 = rdbt$createPatterns(bi.getColor(),
                    rdbt$getItemPatterns(stack));
            float $$7 = 0.6666667F;

            $$2.pushPose();

            $$2.pushPose();
            $$2.scale(0.6666667F, -0.6666667F, -0.6666667F);
            VertexConsumer $$14 = ModelBakery.BANNER_BASE.buffer($$3, RenderType::entityTranslucent);
            float $$16 = ((float)Math.floorMod((long)(pos.x() * 7 + pos.y() * 9 + pos.z() * 13) + $$9, 100L) + $$1) / 100.0F;
            this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * $$16)) * (float) Math.PI;
            this.flag.y = -32.0F;
            this.flag.yScale = scale;

            renderPatterns($$2, $$3, $$4, $$5, this.flag, ModelBakery.BANNER_BASE, true, $$6);
            $$2.popPose();
            $$2.popPose();
        }
    }
    @Unique
    @Nullable
    private static ListTag rdbt$getItemPatterns(ItemStack $$0) {
        ListTag $$1 = null;
        CompoundTag $$2 = BlockItem.getBlockEntityData($$0);
        if ($$2 != null && $$2.contains("Patterns", 9)) {
            $$1 = $$2.getList("Patterns", 10).copy();
        }

        return $$1;
    }
    private static List<Pair<Holder<BannerPattern>, DyeColor>> rdbt$createPatterns(DyeColor $$0, @Nullable ListTag $$1) {
        List<Pair<Holder<BannerPattern>, DyeColor>> $$2 = Lists.newArrayList();
        $$2.add(Pair.of(BuiltInRegistries.BANNER_PATTERN.getHolderOrThrow(BannerPatterns.BASE), $$0));
        if ($$1 != null) {
            for (int $$3 = 0; $$3 < $$1.size(); $$3++) {
                CompoundTag $$4 = $$1.getCompound($$3);
                Holder<BannerPattern> $$5 = BannerPattern.byHash($$4.getString("Pattern"));
                if ($$5 != null) {
                    int $$6 = $$4.getInt("Color");
                    $$2.add(Pair.of($$5, DyeColor.byId($$6)));
                }
            }
        }

        return $$2;
    }
    public void render(BannerBlockEntity $$0, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5) {
        List<Pair<Holder<BannerPattern>, DyeColor>> $$6 = $$0.getPatterns();
        float $$7 = 0.6666667F;
        boolean $$8 = $$0.getLevel() == null;
        $$2.pushPose();
        long $$9;
        if ($$8) {
            $$9 = 0L;
            $$2.translate(0.5F, 0.5F, 0.5F);
            this.pole.visible = true;
        } else {
            $$9 = $$0.getLevel().getGameTime();
            BlockState $$11 = $$0.getBlockState();
            if ($$11.getBlock() instanceof BannerBlock) {
                $$2.translate(0.5F, 0.5F, 0.5F);
                float $$12 = -RotationSegment.convertToDegrees($$11.getValue(BannerBlock.ROTATION));
                $$2.mulPose(Axis.YP.rotationDegrees($$12));
                this.pole.visible = true;
            } else {
                $$2.translate(0.5F, -0.16666667F, 0.5F);
                float $$13 = -$$11.getValue(WallBannerBlock.FACING).toYRot();
                $$2.mulPose(Axis.YP.rotationDegrees($$13));
                $$2.translate(0.0F, -0.3125F, -0.4375F);
                this.pole.visible = false;
            }
        }

        $$2.pushPose();
        $$2.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer $$14 = ModelBakery.BANNER_BASE.buffer($$3, RenderType::entitySolid);
        this.pole.render($$2, $$14, $$4, $$5);
        this.bar.render($$2, $$14, $$4, $$5);
        BlockPos $$15 = $$0.getBlockPos();
        float $$16 = ((float)Math.floorMod((long)($$15.getX() * 7 + $$15.getY() * 9 + $$15.getZ() * 13) + $$9, 100L) + $$1) / 100.0F;
        this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * $$16)) * (float) Math.PI;
        this.flag.y = -32.0F;
        renderPatterns($$2, $$3, $$4, $$5, this.flag, ModelBakery.BANNER_BASE, true, $$6);
        $$2.popPose();
        $$2.popPose();
    }

    public static void renderPatterns(
            PoseStack $$0, MultiBufferSource $$1, int $$2, int $$3, ModelPart $$4, Material $$5, boolean $$6, List<Pair<Holder<BannerPattern>, DyeColor>> $$7
    ) {
        renderPatterns($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, false);
    }

    public static void renderPatterns(
            PoseStack $$0,
            MultiBufferSource $$1,
            int $$2,
            int $$3,
            ModelPart $$4,
            Material $$5,
            boolean $$6,
            List<Pair<Holder<BannerPattern>, DyeColor>> $$7,
            boolean $$8
    ) {
        $$4.render($$0, $$5.buffer($$1, RenderType::entitySolid, $$8), $$2, $$3);

        for (int $$9 = 0; $$9 < 17 && $$9 < $$7.size(); $$9++) {
            Pair<Holder<BannerPattern>, DyeColor> $$10 = $$7.get($$9);
            float[] $$11 = $$10.getSecond().getTextureDiffuseColors();
            $$10.getFirst()
                    .unwrapKey()
                    .map($$1x -> $$6 ? Sheets.getBannerMaterial((ResourceKey<BannerPattern>)$$1x) : Sheets.getShieldMaterial((ResourceKey<BannerPattern>)$$1x))
                    .ifPresent($$6x -> $$4.render($$0, $$6x.buffer($$1, RenderType::entityNoOutline), $$2, $$3, $$11[0], $$11[1], $$11[2], 1.0F));
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {

    }
}
