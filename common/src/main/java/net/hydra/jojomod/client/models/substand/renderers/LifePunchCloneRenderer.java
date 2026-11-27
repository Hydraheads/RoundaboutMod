package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.LifePunchCloneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

public class LifePunchCloneRenderer<T extends LifePunchCloneEntity> extends CloneRenderer<T> {

    public LifePunchCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
        if (!entity.turned){
            Player pl = entity.getPlayer();
            if (pl != null) {
                entity.sba$setWidthFactor(((IPlayerEntity)pl).sba$getWidthFactor());
                ((StandUser)entity).sba$setArmVanishTicks(((StandUser)pl).sba$getArmVanishTicks());
                if (pl.isCrouching()){
                    entity.setShiftKeyDown(true);
                    entity.setPose(Pose.CROUCHING);
                }
                // Position
                entity.setPos(pl.getX(), pl.getY(), pl.getZ());
                entity.xOld = pl.xOld;
                entity.yOld = pl.yOld;
                entity.zOld = pl.zOld;

                // Body rotation
                entity.setYRot(pl.getYRot());
                entity.yRotO = pl.yRotO;

                // Pitch
                entity.setXRot(pl.getXRot());
                entity.xRotO = pl.xRotO;

                // Body/head rotations
                entity.yBodyRot = pl.yBodyRot;
                entity.yBodyRotO = pl.yBodyRotO;
                entity.yHeadRot = pl.yHeadRot;
                entity.yHeadRotO = pl.yHeadRotO;

                // Animation
                entity.walkAnimation.setSpeed(pl.walkAnimation.speed());
                entity.walkAnimation.position(pl.walkAnimation.position());
                ILivingEntityAccess entityAndData = ((ILivingEntityAccess) entity);
                ILivingEntityAccess playerAndData = ((ILivingEntityAccess) pl);

                entityAndData.sba$setLerpXRot(playerAndData.sba$getLerpXRot());
                entityAndData.sba$setLerpYRot(playerAndData.sba$getLerpYRot());
                entityAndData.sba$setLerp(new Vector3f(
                        (float) playerAndData.sba$getLerpX(),
                        (float) playerAndData.sba$getLerpY(),
                        (float) playerAndData.sba$getLerpZ()
                ));
                entity.turned = true;
            }
        }

        matrices.pushPose();
        super.render(entity,entityYaw,partialTick,matrices,bufferSource,packedLight);
        matrices.popPose();

    }
}

