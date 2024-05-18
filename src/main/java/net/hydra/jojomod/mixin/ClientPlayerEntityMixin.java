package net.hydra.jojomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.networking.ModMessages;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements StandUserClientPlayer {
    @Shadow
    public Input input;

    @Shadow
    protected int ticksLeftToDoubleTapSprint;

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
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onMovement(Lnet/minecraft/client/input/Input;)V", shift = At.Shift.AFTER))
    private void RoundaboutTickMovement(CallbackInfo ci) {
        if (((StandUser) this).isDazed()) {
            this.input.movementSideways = 0;
            this.input.movementForward = 0;
            this.ticksLeftToDoubleTapSprint = 0;
        } else if (!(((ClientPlayerEntity)(Object)this).getVehicle() != null && ((ClientPlayerEntity)(Object)this).getControllingVehicle() == null) &&
                ((((StandUser) this).isGuarding() && ((ClientPlayerEntity)(Object)this).getVehicle() == null) ||
                ((StandUser) this).isBarraging() || ((StandUser) this).isClashing())) {
            this.input.movementSideways *= 0.2f;
            this.input.movementForward *= 0.2f;
            this.ticksLeftToDoubleTapSprint = 0;
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

    @Inject(method = "startRidingJump", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutStartRidingJump(CallbackInfo ci) {
        if (((StandUser) this).isClashing()) {
            ci.cancel();
        }
    }

    private void updateClash(){
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeFloat(((StandUser) this).getStandPowers().getClashProgress());
        buffer.writeBoolean(((StandUser) this).getStandPowers().getClashDone());
        ClientPlayNetworking.send(ModMessages.BARRAGE_CLASH_UPDATE_PACKET, buffer);
    };
}
