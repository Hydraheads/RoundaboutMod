package net.hydra.jojomod.mixin.stand_users;

import net.hydra.jojomod.access.IInputEvents;
import net.hydra.jojomod.access.IMultiplayerGameMode;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class PowersMultiPlayerGameMode implements IMultiplayerGameMode {
    /**Mixin for stands to change the way certain things work*/

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

    @Unique
    @Override
    public void roundaabout$setDestroyDelay(int destroy){
        this.destroyDelay = destroy;
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

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

    @Shadow private int destroyDelay;
}
