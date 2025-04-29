package net.hydra.jojomod.block;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
                    if (value instanceof StandEntity || value.isInvisible() ||value.getBbHeight() >= 2) {
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
                if (ERA instanceof LivingEntityRenderer) {
                    EntityRenderer<LivingEntity> ER = (EntityRenderer<LivingEntity>) $$7.getRenderer(lv);
                    if (fire.getBlockState().hasProperty(MirrorBlock.FACING)) {
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
                        if (direction.equals(Direction.NORTH)|| (direction.equals(Direction.SOUTH))) {
                            float rot = lv.yBodyRot;
                            float rotO = lv.yBodyRotO;
                            float headrot = lv.yHeadRot;
                            float headrotO = lv.yHeadRotO;
                            lv.setYBodyRot((rot + 180) % 360);
                            lv.yBodyRotO = ((rotO + 180) % 360);
                            lv.yHeadRot = ((headrot + 180) % 360);
                            lv.yHeadRotO = ((headrotO + 180) % 360);
                            ER.render(lv, 0, partialTick, matrices, buffer, 15728880);
                            lv.setYBodyRot(rot);
                            lv.yBodyRotO = rotO;
                            lv.yHeadRot = headrot;
                            lv.yHeadRotO = headrotO;
                        } else {
                            ER.render(lv, 0, partialTick, matrices, buffer, 15728880);
                        }
                    } else {
                        Roundabout.LOGGER.info("I have questions");
                    }
                }
            }
        }
    }
}