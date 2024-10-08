package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IInputEvents;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class ZMultiPlayerGameMode {

    @Shadow
    private boolean isDestroying;
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private ClientPacketListener connection;
    @Shadow
    private BlockPos destroyBlockPos = null;

    @Shadow
    private float destroyProgress;

    /**While your offhand is frozen in stone, you cannot use it*/
    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    public void roundabout$BlockBreak(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {

        byte curse = ((StandUser)$$0).roundabout$getLocacacaCurse();
        if (curse > -1) {
            if ((curse == LocacacaCurseIndex.OFF_HAND && $$0.getMainArm() == HumanoidArm.RIGHT && $$1 == InteractionHand.OFF_HAND)
                    || (curse == LocacacaCurseIndex.MAIN_HAND && $$0.getMainArm() == HumanoidArm.LEFT && $$1 == InteractionHand.OFF_HAND)) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }

    /**Prevents stand mining from making your vanilla attack cooldown reset*/
    @Inject(method = "releaseUsingItem", at = @At("HEAD"), cancellable = true)
    public void roundabout$releaseUsingItem(Player pl, CallbackInfo ci) {
        if (((IInputEvents)Minecraft.getInstance()).roundabout$sameKeyTwo(KeyInputRegistry.guardKey)) {
            ci.cancel();
        }
    }
    /**Prevents stand mining from making your vanilla attack cooldown reset*/
    @Inject(method = "stopDestroyBlock()V", at = @At("HEAD"), cancellable = true)
    public void roundabout$stopDestroyBlock(CallbackInfo ci) {
        if (((StandUser) this.minecraft.player).roundabout$getActive() && ((StandUser) this.minecraft.player).roundabout$getStandPowers().canUseMiningStand()) {
            if (this.isDestroying) {
                BlockState $$0 = this.minecraft.level.getBlockState(this.destroyBlockPos);
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, this.destroyBlockPos, $$0, -1.0F);
                this.connection
                        .send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, Direction.DOWN));
                this.isDestroying = false;
                this.destroyProgress = 0.0F;
                this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, -1);
            }
            ci.cancel();
        }
    }
}
