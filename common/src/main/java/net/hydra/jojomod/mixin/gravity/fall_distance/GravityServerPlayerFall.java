package net.hydra.jojomod.mixin.gravity.fall_distance;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class GravityServerPlayerFall extends Player {


    public GravityServerPlayerFall(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Shadow public abstract void doCheckFallDamage(double d, double e, double f, boolean bl);

    // make sure fall distance is correct on server side of the player
    @Inject(
            method = "doCheckFallDamage",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$wrapCheckFallDamage(
            double $$0, double $$1, double $$2, boolean $$3, CallbackInfo ci
    ) {
        Roundabout.LOGGER.info("og = "+$$3+" fd ="+this.fallDistance);
        ServerPlayer this_ = (ServerPlayer) (Object) this;
        Direction gravityDirection = GravityAPI.getGravityDirection(this_);
        if (gravityDirection == Direction.DOWN) return;

        ci.cancel();
        if (!this.touchingUnloadedChunk()) {


            this.checkSupportingBlock($$3, new Vec3($$0, $$1, $$2));
            BlockPos $$4 = this.getOnPosLegacy();
            Vec3 localVec = RotationUtil.vecWorldToPlayer($$0, $$1, $$2, gravityDirection);
            super.checkFallDamage(localVec.y, $$3, this.level().getBlockState($$4), $$4);
        }
    }

}