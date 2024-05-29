package net.hydra.jojomod.mixin;


import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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


    private long clashDisplayExtraTimestamp = -1;
    private float lastClashPower = -1;

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
        if (((StandUser) this).isDazed()) {
            this.input.leftImpulse = 0;
            this.input.forwardImpulse = 0;
            this.sprintTriggerTime = 0;
        } else if (!(((LocalPlayer)(Object)this).getVehicle() != null && ((LocalPlayer)(Object)this).getControlledVehicle() == null) &&
                (((StandUser) this).isGuarding() && ((LocalPlayer)(Object)this).getVehicle() == null)) {
            this.input.leftImpulse *= 0.3f;
            this.input.forwardImpulse *= 0.3f;
            this.sprintTriggerTime = 0;
        } else if (((StandUser) this).getStandPowers().isBarrageAttacking() || ((StandUser) this).isClashing()){
            this.input.leftImpulse *= 0.2f;
            this.input.forwardImpulse *= 0.2f;
            this.sprintTriggerTime = 0;
        } else if (((StandUser) this).getStandPowers().isBarrageCharging()){
            this.input.leftImpulse *= 0.66f;
            this.input.forwardImpulse *= 0.66f;
            this.sprintTriggerTime = 0;
        }
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

    private void updateClash(){
        ModPacketHandler.PACKET_ACCESS.updateClashPacket(
                ((StandUser) this).getStandPowers().getClashProgress(),
                ((StandUser) this).getStandPowers().getClashDone()
        );
    }
}
