package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.ServerPlayerAccess;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.hydra.jojomod.stand.NBTData.syncModNbt;

@Mixin(ServerPlayerEntity.class)
    public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ServerPlayerAccess {

    /** This code makes sure stand is summoned properly when switching dimensions or performing other tasks
     * @see net.hydra.jojomod.util.EventInit*/
        public int compatSync = 2;

        public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
            super(world, pos, yaw, gameProfile);
        }

        @Inject(method = "playerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;tick()V", shift = At.Shift.AFTER))
        public void playerTickMixin(CallbackInfo info) {
            if (compatSync > 0) {
                compatSync--;
                if (compatSync == 1) {
                }
            }
        }


        @Override
    public void compatSync() {
            //MyComponents.STAND_USER.get(((ServerPlayerEntity) (Object) this)).sync();
            syncModNbt((ServerPlayerEntity) (Object) this);
            MyComponents.STAND_USER.get(((ServerPlayerEntity) (Object) this)).summonStand(this.getWorld(), true,false);
        compatSync = 5;
    }
}