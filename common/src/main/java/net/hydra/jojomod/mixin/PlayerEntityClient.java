package net.hydra.jojomod.mixin;


import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    private int roundabout$clashIncrement;
    @Unique
    private boolean roundabout$bl = false;

    /**When time is stopped, it would be OP if you could place some blocks down*/
    @Unique
    private int roundabout$NoPlaceTSTicks = -1;
    @Unique
    private int roundabout$menuTicks = -1;

    @Unique
    private long roundabout$clashDisplayExtraTimestamp = -100;
    @Unique
    private float roundabout$lastClashPower = -1;

    public PlayerEntityClient(ClientLevel $$0, GameProfile $$1) {
        super($$0, $$1);
    }

    @Unique
    @Override
    public int roundabout$getRoundaboutNoPlaceTSTicks(){
        return this.roundabout$NoPlaceTSTicks;
    }

    @Unique
    @Override
    public long roundabout$getClashDisplayExtraTimestamp(){
        return this.roundabout$clashDisplayExtraTimestamp;
    }

    @Unique
    @Override
    public void roundabout$setMenuTicks(int menuTicks){
        this.roundabout$menuTicks = menuTicks;
    }

    @Unique
    @Override
    public int roundabout$getMenuTicks(){
        return this.roundabout$menuTicks;
    }
    @Unique
    @Override
    public float roundabout$getLastClashPower(){
        return this.roundabout$lastClashPower;
    }
    @Unique
    @Override
    public void roundabout$setClashDisplayExtraTimestamp(long set){
        this.roundabout$clashDisplayExtraTimestamp = set;
    }
    @Unique
    @Override
    public void roundabout$setLastClashPower(float set){
        this.roundabout$lastClashPower = set;
    }



    /**This code mirrors item usage code, and it's why you are slower while eating.
     * The purpose of this mixin is to make stand blocking slow you down.*/
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/Input;)V", shift = At.Shift.AFTER))
    private void roundabout$TickMovement(CallbackInfo ci) {
        /*Time Stop Levitation*/
        //this.sprintTriggerTime = ((StandUser)this).getStandPowers().inputSpeedModifiers(this.sprintTriggerTime);
        roundabout$ClashJump();
    }
    @Unique
    private void roundabout$ClashJump(){
        if (((StandUser) this).roundabout$isClashing()) {
            if (!((StandUser)this).roundabout$getStandPowers().getClashDone()) {
                if (this.roundabout$clashIncrement < 0) {
                    ++this.roundabout$clashIncrement;
                    if (this.roundabout$clashIncrement == 0) {
                        ((StandUser) this).roundabout$getStandPowers().setClashProgress(0.0f);
                    }
                }
                if (roundabout$bl && !this.input.jumping) {
                    ((StandUser)this).roundabout$getStandPowers().setClashDone(true);
                    //this.startRidingJump();
                } else if (!roundabout$bl && this.input.jumping) {
                    this.roundabout$clashIncrement = 0;
                    ((StandUser) this).roundabout$getStandPowers().setClashProgress(0.0f);
                } else if (roundabout$bl) {
                    ++this.roundabout$clashIncrement;
                    ((StandUser) this).roundabout$getStandPowers().setClashProgress(this.roundabout$clashIncrement < 10 ?
                            (float) this.roundabout$clashIncrement * 0.1f : 0.8f + 2.0f / (float) (this.roundabout$clashIncrement - 9) * 0.1f);
                }
                roundabout$updateClash();
            }
        } else {
            ((StandUser)this).roundabout$getStandPowers().setClashProgress(0.0f);
            ((StandUser)this).roundabout$getStandPowers().setClashDone(false);
        }
        roundabout$bl = this.input.jumping;
    }

    @Inject(method = "sendRidingJump", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$startRidingJump(CallbackInfo ci) {
        if (((StandUser) this).roundabout$isClashing()) {
            ci.cancel();
        }
    }

    /**If you are stopping time, make it so that you gain a block placement cooldown for blocks with
     * a certain level of hardness or danger*/
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tick(CallbackInfo ci) {
        if (!((TimeStop) ((Player)(Object) this).level()).getTimeStoppingEntities().isEmpty() &&
                ((TimeStop) ((Player) (Object) this).level()).isTimeStoppingEntity(((Player) (Object) this))) {
            this.roundabout$NoPlaceTSTicks = 6;
        } else {
            if (this.roundabout$NoPlaceTSTicks > -1){
                this.roundabout$NoPlaceTSTicks--;
            }
        }
        if (this.roundabout$menuTicks > -1){
            this.roundabout$menuTicks--;
        }

        StandUser user = ((StandUser) this);
        if (user.roundabout$getStandPowers().isPiloting()){
            Entity ent = user.roundabout$getStandPowers().getPilotingStand();
            if (ent != null){
                roundabout$standControlTick(ent);
                ci.cancel();
            }
        }
    }

    @Unique
    private void roundabout$standControlTick(Entity ent){
        if (this.level().hasChunkAt(this.getBlockX(), this.getBlockZ())) {
            super.tick();
                this.connection.send(new ServerboundMovePlayerPacket.Rot(this.getYRot(), this.getXRot(), this.onGround()));
                this.connection.send(new ServerboundPlayerInputPacket(this.xxa, this.zza, this.input.jumping, this.input.shiftKeyDown));
                if (ent != this) {
                    this.connection.send(new ServerboundMoveVehiclePacket(ent));
                    this.sendIsSprintingIfNeeded();
                }
            this.sendPosition();
            for(AmbientSoundHandler ambientsoundhandler : this.ambientSoundHandlers) {
                ambientsoundhandler.tick();
            }
        }
    }

    @Unique
    private void roundabout$updateClash(){
        ModPacketHandler.PACKET_ACCESS.updateClashPacket(
                ((StandUser) this).roundabout$getStandPowers().getClashProgress(),
                ((StandUser) this).roundabout$getStandPowers().getClashDone()
        );
    }
}
