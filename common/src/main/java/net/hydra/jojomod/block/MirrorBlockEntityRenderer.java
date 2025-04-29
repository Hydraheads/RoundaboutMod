package net.hydra.jojomod.block;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityRenderer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IRenderSystem;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MirrorBlockEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> implements BlockEntityRenderer<MirrorBlockEntity> {

    private final BlockRenderDispatcher itemRenderer;

    public MirrorBlockEntityRenderer(BlockEntityRendererProvider.Context p_173554_) {
        this.itemRenderer = p_173554_.getBlockRenderDispatcher();
    }

    private boolean isFacingDown(MirrorBlockEntity fire) {
        BlockState s = fire.getBlockState();
        return !(s.getValue(StandFireBlock.UP) || s.getValue(StandFireBlock.NORTH) || s.getValue(StandFireBlock.EAST) || s.getValue(StandFireBlock.SOUTH) || s.getValue(StandFireBlock.WEST));
    }

    public void render(MirrorBlockEntity fire, float partialTick, PoseStack matrices, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (fire.getLevel() != null) {

            List<LivingEntity> lvent = fire.getLevel().getEntitiesOfClass(LivingEntity.class,  new AABB(fire.getBlockPos()).inflate(30), (p_148078_) -> {
                return true;
            });
            if (lvent !=null && !lvent.isEmpty()) {
                List<LivingEntity> rement = new ArrayList<>(lvent);
                int totalnumber = lvent.size();
                for (LivingEntity value : lvent) {
                    if (value instanceof StandEntity || value.isInvisible() ||value.getBbHeight() >= 2||value.getBbWidth() >= 2 ||
                            !(value instanceof Mob || value instanceof Player)) {
                        rement.remove(value);
                    }
                }
                lvent = rement;
            }

            LivingEntity lv = fire.getLevel().getNearestEntity(lvent,
                    MainUtil.OFFER_TARGER_CONTEXT, null,
                    fire.getBlockPos().getX(), fire.getBlockPos().getY(), fire.getBlockPos().getZ());
            if (lv != null){
                EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                EntityRenderer<? super T> ERA = $$7.getRenderer(lv);
                if (ERA instanceof LivingEntityRenderer ELA) {
                    EntityRenderer<LivingEntity> ER = (EntityRenderer<LivingEntity>) $$7.getRenderer(lv);
                    if (fire.getBlockState().hasProperty(MirrorBlock.FACING)) {
                        matrices.pushPose();
                        boolean hgui = Minecraft.getInstance().options.hideGui;
                        Minecraft.getInstance().options.hideGui = true;
                        Direction direction = fire.getBlockState().getValue(MirrorBlock.FACING);
                        if (direction.equals(Direction.NORTH)){
                            matrices.translate(0.5,0.1,0.93);
                            matrices.scale(-0.35f, 0.35f, 0.012f);
                        } else if (direction.equals(Direction.EAST)){
                            matrices.translate(0.07,0.1,0.5);
                            matrices.scale(-0.012f, 0.35f, 0.35f);
                        } else if (direction.equals(Direction.SOUTH)){
                            matrices.translate(0.5,0.1,0.07);
                            matrices.scale(-0.35f, 0.35f, 0.012f);
                        } else if (direction.equals(Direction.WEST)){
                            matrices.translate(0.93,0.1,0.5);
                            matrices.scale(-0.012f, 0.35f, 0.35f);
                        }
                        boolean $$18 = !lv.isInvisible();
                        Player pp = Minecraft.getInstance().player;
                        Minecraft $$17 = Minecraft.getInstance();
                        boolean $$19 = !$$18 && (pp != null && !lv.isInvisibleTo(pp));
                        boolean $$20 = false;
                        RenderType $$21 = RenderType.cutout();//((ILivingEntityRenderer)ELA).roundabout$getRenderType(lv, $$18, $$19, $$20);
                        if (direction.equals(Direction.NORTH)|| (direction.equals(Direction.SOUTH))) {
                            matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
                        }

                        Matrix3f norm1 = new Matrix3f(matrices.last().normal());
                        Vector3f[] lighting = ((IRenderSystem)new RenderSystem()).roundabout$getShaderLightDirections();
                        Vector3f first = new Vector3f(lighting[0]);
                        Vector3f second = new Vector3f(lighting[1]);

                        matrices.last().normal().rotate(Axis.YP.rotationDegrees(180F)); // fix for scaling the X axis by a negative amount: otherwise it's always light level 0
                        Lighting.setupLevel(matrices.last().pose());

                        ER.render(lv, Mth.lerp(partialTick, lv.yRotO, lv.getYRot()), partialTick, matrices, buffer, LightTexture.pack(15,15)); // replace with: LightTexture.pack(15, 15)) for fullbright;

                        matrices.last().normal().set(norm1);
                        RenderSystem.setShaderLights(first,second);

                        Minecraft.getInstance().options.hideGui = hgui;
                        matrices.popPose();
                    }
                }
            }
        }
    }
}