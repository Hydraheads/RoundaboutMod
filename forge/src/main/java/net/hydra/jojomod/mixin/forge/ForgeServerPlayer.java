package net.hydra.jojomod.mixin.forge;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
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
public abstract class ForgeServerPlayer extends Player {
    public ForgeServerPlayer(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(method = "die", at = @At(value = "HEAD"))
    public void roundabout$die(DamageSource $$0, CallbackInfo ci) {
        StandPowers powers = ((StandUser)this).roundabout$getStandPowers();
        if (powers != null && powers.isClashing()){
            powers.endClash();
        }
        if ((((IPlayerEntity)this).roundabout$getVoiceData()) != null){
            ((IPlayerEntity)this).roundabout$getVoiceData().playIfDying($$0);
        }
    }
    @Inject(method = "changeDimension", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void roundabout$changeDim(ServerLevel p_9180_, net.minecraftforge.common.util.ITeleporter teleporter, CallbackInfoReturnable<Entity> cir) {
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
