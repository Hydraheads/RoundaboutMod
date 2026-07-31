package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IWalkAnimationState;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.RoadRollerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

public class KingCrimsonCloneRenderer<T extends KingCrimsonCloneEntity> extends CloneRenderer<T> {

    public KingCrimsonCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }


    @Override
    protected boolean shouldShowName(T $$0) {
        return false;
    }@Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
        if (!entity.turned){
            Player pl = entity.getPlayer();
            if (pl != null) {

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

        super.render(entity,entityYaw,partialTick,matrices,bufferSource,packedLight);
    }
}

