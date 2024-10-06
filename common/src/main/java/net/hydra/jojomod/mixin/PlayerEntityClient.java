package net.hydra.jojomod.mixin;


import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class PlayerEntityClient implements StandUserClientPlayer {
    @Shadow
    public Input input;

    @Shadow
    protected int sprintTriggerTime;

    @Unique
    private int roundabout$clashIncrement;
    @Unique
    private boolean roundabout$bl = false;

    /**When time is stopped, it would be OP if you could place some blocks down*/
    @Unique
    private int roundabout$NoPlaceTSTicks = -1;

    @Unique
    private long roundabout$clashDisplayExtraTimestamp = -100;
    @Unique
    private float roundabout$lastClashPower = -1;

    @Unique
    public int roundabout$getRoundaboutNoPlaceTSTicks(){
        return this.roundabout$NoPlaceTSTicks;
    }

    @Unique
    public long roundabout$getClashDisplayExtraTimestamp(){
        return this.roundabout$clashDisplayExtraTimestamp;
    }


    @Unique
    public float roundabout$getLastClashPower(){
        return this.roundabout$lastClashPower;
    }
    @Unique
    public void roundabout$setClashDisplayExtraTimestamp(long set){
        this.roundabout$clashDisplayExtraTimestamp = set;
    }
    @Unique
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
    }

    @Unique
    private void roundabout$updateClash(){
        ModPacketHandler.PACKET_ACCESS.updateClashPacket(
                ((StandUser) this).roundabout$getStandPowers().getClashProgress(),
                ((StandUser) this).roundabout$getStandPowers().getClashDone()
        );
    }
}
