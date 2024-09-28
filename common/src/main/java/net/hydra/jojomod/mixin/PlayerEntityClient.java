package net.hydra.jojomod.mixin;


import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
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

    private int clashIncrement;
    private boolean bl = false;

    /**When time is stopped, it would be OP if you could place some blocks down*/
    @Unique
    private int roundaboutNoPlaceTSTicks = -1;

    private long clashDisplayExtraTimestamp = -100;
    private float lastClashPower = -1;

    @Unique
    public int getRoundaboutNoPlaceTSTicks(){
        return this.roundaboutNoPlaceTSTicks;
    }

    public long getClashDisplayExtraTimestamp(){
        return this.clashDisplayExtraTimestamp;
    }
    public float getLastClashPower(){
        return this.lastClashPower;
    }
    public void setClashDisplayExtraTimestamp(long set){
        this.clashDisplayExtraTimestamp = set;
    }
    public void setLastClashPower(float set){
        this.lastClashPower = set;
    }



    /**This code mirrors item usage code, and it's why you are slower while eating.
     * The purpose of this mixin is to make stand blocking slow you down.*/
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/Input;)V", shift = At.Shift.AFTER))
    private void RoundaboutTickMovement(CallbackInfo ci) {
        /*Time Stop Levitation*/
        //this.sprintTriggerTime = ((StandUser)this).getStandPowers().inputSpeedModifiers(this.sprintTriggerTime);
        RoundaboutClashJump();
    }
    private void RoundaboutClashJump(){
        if (((StandUser) this).isClashing()) {
            if (!((StandUser)this).getStandPowers().getClashDone()) {
                if (this.clashIncrement < 0) {
                    ++this.clashIncrement;
                    if (this.clashIncrement == 0) {
                        ((StandUser) this).getStandPowers().setClashProgress(0.0f);
                    }
                }
                if (bl && !this.input.jumping) {
                    ((StandUser)this).getStandPowers().setClashDone(true);
                    //this.startRidingJump();
                } else if (!bl && this.input.jumping) {
                    this.clashIncrement = 0;
                    ((StandUser) this).getStandPowers().setClashProgress(0.0f);
                } else if (bl) {
                    ++this.clashIncrement;
                    ((StandUser) this).getStandPowers().setClashProgress(this.clashIncrement < 10 ?
                            (float) this.clashIncrement * 0.1f : 0.8f + 2.0f / (float) (this.clashIncrement - 9) * 0.1f);
                }
                updateClash();
            }
        } else {
            ((StandUser)this).getStandPowers().setClashProgress(0.0f);
            ((StandUser)this).getStandPowers().setClashDone(false);
        }
        bl = this.input.jumping;
    }

    @Inject(method = "sendRidingJump", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutStartRidingJump(CallbackInfo ci) {
        if (((StandUser) this).isClashing()) {
            ci.cancel();
        }
    }

    /**If you are stopping time, make it so that you gain a block placement cooldown for blocks with
     * a certain level of hardness or danger*/
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (!((TimeStop) ((Player)(Object) this).level()).getTimeStoppingEntities().isEmpty() &&
                ((TimeStop) ((Player) (Object) this).level()).isTimeStoppingEntity(((Player) (Object) this))) {
            this.roundaboutNoPlaceTSTicks = 6;
        } else {
            if (this.roundaboutNoPlaceTSTicks > -1){
                this.roundaboutNoPlaceTSTicks--;
            }
        }
    }

    private void updateClash(){
        ModPacketHandler.PACKET_ACCESS.updateClashPacket(
                ((StandUser) this).getStandPowers().getClashProgress(),
                ((StandUser) this).getStandPowers().getClashDone()
        );
    }
}
