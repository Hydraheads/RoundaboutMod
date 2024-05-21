package net.hydra.jojomod.mixin;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {


    /** Called every tick on the Server. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */

    @Inject(method = "tickEntity", at = @At(value = "TAIL"))
    private void tickEntity2(Entity entity, CallbackInfo ci) {
        if (entity.isLiving()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (((StandUser) entity).hasStandOut()) {
                this.tickStandIn(livingEntity, Objects.requireNonNull(((StandUser) entity).getStand()));
            }
        }
    }

    private void tickStandIn(LivingEntity entity, StandEntity passenger) {
        if (passenger == null || passenger.isRemoved() || passenger.getUser() != entity) {
            return;
        }
        byte ot = passenger.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            passenger.resetPosition();
            ++passenger.age;
            passenger.tickStandOut();
        }
    }

}
