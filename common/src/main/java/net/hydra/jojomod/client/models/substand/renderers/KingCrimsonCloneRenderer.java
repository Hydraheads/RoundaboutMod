package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

public class KingCrimsonCloneRenderer<T extends KingCrimsonCloneEntity> extends CloneRenderer<T> {

    public KingCrimsonCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }




    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
        if (!entity.turned){
            Player pl = entity.getPlayer();
            if (pl != null) {

                ((StandUser)entity).roundabout$setArmVanishTicks(((StandUser)pl).roundabout$getArmVanishTicks());
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

                entityAndData.roundabout$setLerpXRot(playerAndData.roundabout$getLerpXRot());
                entityAndData.roundabout$setLerpYRot(playerAndData.roundabout$getLerpYRot());
                entityAndData.roundabout$setLerp(new Vector3f(
                        (float) playerAndData.roundabout$getLerpX(),
                        (float) playerAndData.roundabout$getLerpY(),
                        (float) playerAndData.roundabout$getLerpZ()
                ));
                entity.turned = true;
            }
        }

        matrices.pushPose();
        if (entity.getIsJumping()){
            matrices.translate(0,-0.2,0);
        }
        super.render(entity,entityYaw,partialTick,matrices,bufferSource,packedLight);
        matrices.popPose();

    }
}

