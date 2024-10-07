package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayer.class)
public abstract class PlayerEntityServer extends Player {

    public PlayerEntityServer(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "changeDimension", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$changeDim(ServerLevel $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((Entity)(Object)this) instanceof LivingEntity LE){
            if (((StandUser)this).roundabout$getStand() != null){
                StandEntity stand = ((StandUser)this).roundabout$getStand();
                if (!stand.getHeldItem().isEmpty()) {
                    if (stand.canAcquireHeldItem) {
                        double $$3 = this.getEyeY();
                        ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), stand.getHeldItem().copy());
                        $$4.setPickUpDelay(40);
                        $$4.setThrower(stand.getUUID());
                        this.level().addFreshEntity($$4);
                        stand.setHeldItem(ItemStack.EMPTY);
                    }
                }
            }
        }
    }


    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "TAIL"))
    public void roundabout$teleportTo(ServerLevel p_9000_, double p_9001_, double p_9002_, double p_9003_, float p_9004_, float p_9005_, CallbackInfo info) {
        if (p_9000_ == this.level()) {
            ((StandUser) this).roundabout$getFollowers().forEach($$0 -> {
                for (StandEntity $$1 : ((StandUser) this).roundabout$getFollowers()) {
                    $$1.moveTo(this.getX(),this.getY(),this.getZ());
                }
            });
            StandUser standUserData = ((StandUser) this);
            if (standUserData.roundabout$hasStandOut()) {

                standUserData.roundabout$updateStandOutPosition(standUserData.roundabout$getStand(), Entity::moveTo);
            }
        }
    }

    /**Make NBT save on death*/
    @Inject(method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At(value = "TAIL"))
    public void roundabout$restoreFrom(ServerPlayer $$0, boolean $$1, CallbackInfo info) {
        ((StandUser)this).roundabout$setStandDisc(((StandUser)$$0).roundabout$getStandDisc());
    }

}