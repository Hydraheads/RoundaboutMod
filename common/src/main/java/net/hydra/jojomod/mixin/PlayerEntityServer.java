package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayer.class)
public abstract class PlayerEntityServer extends Player implements IPlayerEntityServer {

    public PlayerEntityServer(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow
    private void initMenu(AbstractContainerMenu $$0) {
    }
    @Shadow
    private void nextContainerCounter() {
    }

    @Override
    @Unique
    public int roundabout$getCounter(){
        return containerCounter;
    }
    @Override
    @Unique
    public void roundabout$nextContainerCounter(){
        nextContainerCounter();
    }
    @Override
    @Unique
    public void roundabout$initMenu(AbstractContainerMenu $$0){
        initMenu($$0);
    }
    @Unique
    private boolean roundabout$initializeDataOnClient = false;
    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Shadow private int containerCounter;
    @Unique private int roundabout$invincibleTicks = 0;

    @Override
    public void roundabout$setInvincibleTicks(int ticks){
        roundabout$invincibleTicks = ticks;
    }
    @Inject(method = "onEffectRemoved", at = @At(value = "HEAD"))
    public void roundabout$oneffectRemoved(MobEffectInstance $$0, CallbackInfo ci) {

    }
    @Inject(method = "isChangingDimension()Z", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$changeDimensions(CallbackInfoReturnable<Boolean> cir) {
        if (roundabout$invincibleTicks > 0){
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void roundabout$tick(CallbackInfo ci) {
        if (!this.level().isClientSide() && !roundabout$initializeDataOnClient && connection !=null && connection.isAcceptingMessages()){
            IPlayerEntity ipe = ((IPlayerEntity)this);
            S2CPacketUtil.sendPowerInventorySettings(
                    ((ServerPlayer)((Player)(Object)this)), ipe.roundabout$getAnchorPlace(),
                    ipe.roundabout$getDistanceOut(),
                    ipe.roundabout$getSizePercent(),
                    ipe.roundabout$getIdleRotation(),
                    ipe.roundabout$getIdleYOffset(),
                    ipe.roundabout$getAnchorPlaceAttack());
            roundabout$initializeDataOnClient = true;
        }
        if (!this.level().isClientSide) {
            if (roundabout$invincibleTicks > 0){
                roundabout$invincibleTicks--;
            }
            ((IPlayerEntity) this).roundabout$getMaskInventory().update();
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
            if (standUserData.roundabout$hasStandOut()  && standUserData.roundabout$getStand() instanceof FollowingStandEntity fe) {

                standUserData.roundabout$updateStandOutPosition(fe, Entity::moveTo);
            }
        }
    }

    /**Make NBT save on death*/
    /** respawn */
    @Inject(method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At(value = "TAIL"))
    public void roundabout$respawn(ServerPlayer $$0, boolean $$1, CallbackInfo info) {
        if ($$0.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_KEEP_STANDS_ON_DEATH)){
            ((StandUser)this).roundabout$setStandDisc(MainUtil.saveToDiscData($$0,((StandUser)$$0).roundabout$getStandDisc()));
        } else {
            if (ClientNetworking.getAppropriateConfig().itemSettings.standDiscsDropWithKeepGameRuleOff){
                ItemStack disc = MainUtil.saveToDiscData($$0,((StandUser)$$0).roundabout$getStandDisc());
                $$0.drop(disc,true,false);
            }
            ((StandUser)this).roundabout$setStandDisc(ItemStack.EMPTY);
        }
        IPlayerEntity ipe = ((IPlayerEntity)this);
        ipe.roundabout$setMaskInventory(((IPlayerEntity)$$0).roundabout$getMaskInventory());
        if (!this.level().isClientSide) {
            ((IPlayerEntity) this).roundabout$setMaskSlot(((IPlayerEntity) $$0).roundabout$getMaskSlot());
            ((IPlayerEntity) this).roundabout$setMaskVoiceSlot(((IPlayerEntity) $$0).roundabout$getMaskVoiceSlot());

            int anchorPlace = ((IPlayerEntity) $$0).roundabout$getAnchorPlace();
            int anchorPlaceAttack = ((IPlayerEntity) $$0).roundabout$getAnchorPlaceAttack();
            float distanceOut = ((IPlayerEntity) $$0).roundabout$getDistanceOut();
            float size = ((IPlayerEntity) $$0).roundabout$getSizePercent();
            float rotat = ((IPlayerEntity) $$0).roundabout$getIdleRotation();
            float yOffset = ((IPlayerEntity) $$0).roundabout$getIdleYOffset();
            byte teamColor = ((IPlayerEntity) $$0).roundabout$getTeamColor();
            ipe.roundabout$setAnchorPlace(anchorPlace);
            ipe.roundabout$setDistanceOut(distanceOut);
            ipe.roundabout$setSizePercent(size);
            ipe.roundabout$setIdleRotation(rotat);
            ipe.roundabout$setIdleYOffset(yOffset);
            ipe.roundabout$setTeamColor(teamColor);
            ipe.roundabout$setAnchorPlaceAttack(anchorPlaceAttack);

            ipe.rdbt$setHairColorX(ipe.rdbt$getHairColorX());
            ipe.rdbt$setHairColorY(ipe.rdbt$getHairColorY());
            ipe.rdbt$setHairColorZ(ipe.rdbt$getHairColorZ());

            S2CPacketUtil.sendPowerInventorySettings(
                        ((ServerPlayer)((Player)(Object)this)), anchorPlace,distanceOut,
                    ipe.roundabout$getSizePercent(),
                    ipe.roundabout$getIdleRotation(),
                    ipe.roundabout$getIdleYOffset(),
                    anchorPlaceAttack);

            if (!this.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_LOSE_FATE_ON_DEATH)) {
                byte strat = ((IPlayerEntity) $$0).rdbt$getRespawnStrategy();
                if (strat < 1) {
                    ipe.roundabout$setFate(((IPlayerEntity) $$0).roundabout$getFate());
                }
            }

            ipe.rdbt$setVampireData(((IPlayerEntity) $$0).rdbt$getVampireData());
            ipe.roundabout$setPower(((IPlayerEntity) $$0).roundabout$getPower());
        }
    }

}