package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.IVillagerAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractVillager.class)
public abstract class ZAbstractVillager implements IVillagerAccess {
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$SNAKE_OIL_LOCKOUT =
            SynchedEntityData.defineId(AbstractVillager.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void roundabout$addAbstractVillagerSynched(CallbackInfo ci) {
        AbstractVillager self = (AbstractVillager) (Object) this;
        if (!self.getEntityData().hasItem(ROUNDABOUT$SNAKE_OIL_LOCKOUT)) {
            self.getEntityData().define(ROUNDABOUT$SNAKE_OIL_LOCKOUT, 0);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void roundabout$saveSnakeOilData(CompoundTag tag, CallbackInfo ci) {
        if (this.roundabout$getSnakeOilLockout() != 0) {
            tag.putInt("roundabout.snakeOilLockout", this.roundabout$getSnakeOilLockout());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void roundabout$readSnakeOilData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("roundabout.snakeOilLockout")) {
            this.roundabout$setSnakeOilLockout(tag.getInt("roundabout.snakeOilLockout"));
        }
    }

    @Unique
    @Override
    public boolean roundabout$isSnakeOilLockedOut() {
        return this.roundabout$getSnakeOilLockout() > 0;
    }

    @Unique
    @Override
    public void roundabout$setSnakeOilLockout(int ticks) {
        AbstractVillager self = (AbstractVillager) (Object) this;
        self.getEntityData().set(ROUNDABOUT$SNAKE_OIL_LOCKOUT, ticks);
    }

    @Unique
    @Override
    public int roundabout$getSnakeOilLockout() {
        AbstractVillager self = (AbstractVillager) (Object) this;
        return self.getEntityData().get(ROUNDABOUT$SNAKE_OIL_LOCKOUT);
    }

    @SuppressWarnings("ConstantValue")
    @Unique
    @Override
    public void roundabout$applySnakeOilScam(Player player) {
        AbstractVillager self = (AbstractVillager) (Object) this;
        AABB box = self.getBoundingBox().inflate(48.0, 16.0, 48.0);
        for (AbstractVillager nearbyMerchant : self.level().getEntitiesOfClass(AbstractVillager.class, box)) {
            ((IVillagerAccess) nearbyMerchant).roundabout$setSnakeOilLockout(72000);
        }

        if (player != null && self instanceof Villager villager) {
            villager.getGossips().add(player.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
        }
    }
}