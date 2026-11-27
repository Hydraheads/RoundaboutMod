package net.hydra.jojomod.mixin;


import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LocalPlayer.class)
public abstract class PlayerEntityClient extends AbstractClientPlayer implements StandUserClientPlayer {
    @Shadow
    public Input input;

    @Shadow
    protected int sprintTriggerTime;

    @Shadow @Final public ClientPacketListener connection;

    @Shadow protected abstract void sendIsSprintingIfNeeded();

    @Shadow protected abstract void sendPosition();

    @Shadow @Final private List<AmbientSoundHandler> ambientSoundHandlers;
    @Unique
    private int sba$clashIncrement;
    @Unique
    private boolean sba$bl = false;

    /**When time is stopped, it would be OP if you could place some blocks down*/
    @Unique
    private int sba$NoPlaceTSTicks = -1;
    @Unique
    private int sba$menuTicks = -1;

    @Unique
    private long sba$clashDisplayExtraTimestamp = -100;
    @Unique
    private float sba$lastClashPower = -1;


    public PlayerEntityClient(ClientLevel $$0, GameProfile $$1) {
        super($$0, $$1);
    }

    @Unique
    @Override
    public int sba$getSBANoPlaceTSTicks(){
        return this.sba$NoPlaceTSTicks;
    }

    @Unique
    @Override
    public long sba$getClashDisplayExtraTimestamp(){
        return this.sba$clashDisplayExtraTimestamp;
    }

    @Unique
    @Override
    public void sba$setMenuTicks(int menuTicks){
        this.sba$menuTicks = menuTicks;
    }

    @Unique
    @Override
    public int sba$getMenuTicks(){
        return this.sba$menuTicks;
    }
    @Unique
    @Override
    public float sba$getLastClashPower(){
        return this.sba$lastClashPower;
    }
    @Unique
    @Override
    public void sba$setClashDisplayExtraTimestamp(long set){
        this.sba$clashDisplayExtraTimestamp = set;
    }
    @Unique
    @Override
    public void sba$setLastClashPower(float set){
        this.sba$lastClashPower = set;
    }



    /**This code mirrors item usage code, and it's why you are slower while eating.
     * The purpose of this mixin is to make stand blocking slow you down.*/
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/Input;)V", shift = At.Shift.AFTER))
    private void sba$TickMovement(CallbackInfo ci) {
        /*Time Stop Levitation*/
        //this.sprintTriggerTime = ((StandUser)this).getStandPowers().inputSpeedModifiers(this.sprintTriggerTime);
        sba$ClashJump();
        /*Soft and Wet friction cancels input*/
        if (MainUtil.canHaveFrictionTaken(((LivingEntity) (Object)this))) {
            if (((ILevelAccess) this.level()).sba$isFrictionPlundered(this.blockPosition()) ||
                    ((ILevelAccess) this.level()).sba$isFrictionPlunderedEntity(this)
            ) {

                if (!((StandUser)this).sba$frictionSave().equals(Vec3.ZERO)) {
                    this.input.forwardImpulse *= 0.1F;
                    this.input.leftImpulse *= 0.1F;
                }
            }
        }
    }

    @Unique
    private void sba$ClashJump(){
        if (((StandUser) this).sba$isClashing()) {
            if (!((StandUser)this).sba$getStandPowers().getClashDone()) {
                if (this.sba$clashIncrement < 0) {
                    ++this.sba$clashIncrement;
                    if (this.sba$clashIncrement == 0) {
                        ((StandUser) this).sba$getStandPowers().setClashProgress(0.0f);
                    }
                }
                if (sba$bl && !this.input.jumping) {
                    ((StandUser)this).sba$getStandPowers().setClashDone(true);
                    //this.startRidingJump();
                } else if (!sba$bl && this.input.jumping) {
                    this.sba$clashIncrement = 0;
                    ((StandUser) this).sba$getStandPowers().setClashProgress(0.0f);
                } else if (sba$bl) {
                    ++this.sba$clashIncrement;
                    ((StandUser) this).sba$getStandPowers().setClashProgress(this.sba$clashIncrement < 10 ?
                            (float) this.sba$clashIncrement * 0.1f : 0.8f + 2.0f / (float) (this.sba$clashIncrement - 9) * 0.1f);
                }
                sba$updateClash();
            }
        } else {
            ((StandUser)this).sba$getStandPowers().setClashProgress(0.0f);
            ((StandUser)this).sba$getStandPowers().setClashDone(false);
        }
        sba$bl = this.input.jumping;
    }

    @Inject(method = "sendRidingJump", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$startRidingJump(CallbackInfo ci) {
        if (((StandUser) this).sba$isClashing()) {
            ci.cancel();
        }
    }
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$playSound(SoundEvent soundEvent, float f, float g,CallbackInfo ci) {
        Entity thrs = ((Entity) (Object)this);
        if(((ILevelAccess)this.level()).sba$isSoundPlunderedEntity(thrs)){
            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).sba$getSoundPlunderedBubbleEntity(((Entity) (Object)this));
            if (sbpe !=null) {
                sbpe.addPlunderBubbleSounds(soundEvent, this.getSoundSource(), f, g);
            }
            ci.cancel();
            return;
        }
        if (PowerTypes.isExistentiallyElsewhere(thrs) && !PowerTypes.isErasingTime(thrs)){
            ClientUtil.playSoundWithInfo(thrs.level(),
                    thrs.getX(),
                    thrs.getY(),
                    thrs.getZ(),
                    soundEvent,
                    this.getSoundSource(),
                    f,
                    g);
        }
    }

    /**Soft and Wet blindness disables sprinting like actual blindness*/
    @Inject(method = "canStartSprinting", at = @At(value = "HEAD"), cancellable = true)
    private void sba$canStartSprinting(CallbackInfoReturnable<Boolean> cir) {

    }

    @Inject(method = "onSyncedDataUpdated", at = @At(value = "TAIL"), require = 0)
    public void sba$onSyncedDataUpdated(EntityDataAccessor<?> $$0, CallbackInfo ci) {
        ((IPlayerEntity)this).rdbt$onSyncedDataUpdated($$0);
        ((StandUser)this).rdbt$onSyncedDataUpdated2($$0);
    }

    /**If you are stopping time, make it so that you gain a block placement cooldown for blocks with
     * a certain level of hardness or danger*/
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$tick(CallbackInfo ci) {
        if (!((TimeStop) ((Player)(Object) this).level()).getTimeStoppingEntities().isEmpty() &&
                ((TimeStop) ((Player) (Object) this).level()).isTimeStoppingEntity(((Player) (Object) this))) {
            this.sba$NoPlaceTSTicks = 6;
        } else {
            if (this.sba$NoPlaceTSTicks > -1){
                this.sba$NoPlaceTSTicks--;
            }
        }
        if (this.sba$menuTicks > -1){
            this.sba$menuTicks--;
        }

        StandUser user = ((StandUser) this);
        if (user.sba$getStandPowers().isPiloting()){
            Entity ent = user.sba$getStandPowers().getPilotingStand();
            if (ent != null){
                sba$standControlTick(ent);
                ci.cancel();
            }
        }
    }

    @Unique
    private void sba$standControlTick(Entity ent){
        if (this.level().hasChunkAt(this.getBlockX(), this.getBlockZ())) {
            super.tick();
                this.connection.send(new ServerboundMovePlayerPacket.Rot(this.getYRot(), this.getXRot(), this.onGround()));
                this.connection.send(new ServerboundPlayerInputPacket(this.xxa, this.zza, this.input.jumping, this.input.shiftKeyDown));
                /**
                if (ent != this) {
                    this.connection.send(new ServerboundMoveVehiclePacket(ent));
                    this.sendIsSprintingIfNeeded();
                }
                 */
            if (this.isPassenger()) {
                Entity $$0 = this.getRootVehicle();
                if ($$0 != this && $$0.isControlledByLocalInstance()) {
                    this.connection.send(new ServerboundMoveVehiclePacket($$0));
                    this.sendIsSprintingIfNeeded();
                }
            }
            this.sendPosition();
            for(AmbientSoundHandler ambientsoundhandler : this.ambientSoundHandlers) {
                ambientsoundhandler.tick();
            }
        }
    }

    @Unique
    private void sba$updateClash(){
        C2SPacketUtil.barrageClashUpdatePacket(((StandUser) this).sba$getStandPowers().getClashProgress(),
                ((StandUser) this).sba$getStandPowers().getClashDone());
    }

    @Inject(method = "sendPosition", at = @At(value = "HEAD"), cancellable = true)
    private void sba$sendPosition(CallbackInfo ci) {
        if (ClientUtil.isUnderRTZ()) {
            ci.cancel();
        }
    }
}
