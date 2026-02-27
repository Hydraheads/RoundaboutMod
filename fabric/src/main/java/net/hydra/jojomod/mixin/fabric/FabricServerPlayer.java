package net.hydra.jojomod.mixin.fabric;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
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
public abstract class FabricServerPlayer extends Player {
    public FabricServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "die", at = @At(value = "HEAD"))
    public void roundabout$die(DamageSource $$0, CallbackInfo ci) {
        MainUtil.onDeath(this,$$0);
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
}
