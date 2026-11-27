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
    private static final EntityDataAccessor<Integer> SBA$SNAKE_OIL_LOCKOUT =
            SynchedEntityData.defineId(AbstractVillager.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void sba$addAbstractVillagerSynched(CallbackInfo ci) {
        AbstractVillager self = (AbstractVillager) (Object) this;
        if (!self.getEntityData().hasItem(SBA$SNAKE_OIL_LOCKOUT)) {
            self.getEntityData().define(SBA$SNAKE_OIL_LOCKOUT, 0);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void sba$saveSnakeOilData(CompoundTag tag, CallbackInfo ci) {
        if (this.sba$getSnakeOilLockout() != 0) {
            tag.putInt("sba.snakeOilLockout", this.sba$getSnakeOilLockout());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void sba$readSnakeOilData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("sba.snakeOilLockout")) {
            this.sba$setSnakeOilLockout(tag.getInt("sba.snakeOilLockout"));
        }
    }

    @Unique
    @Override
    public boolean sba$isSnakeOilLockedOut() {
        return this.sba$getSnakeOilLockout() > 0;
    }

    @Unique
    @Override
    public void sba$setSnakeOilLockout(int ticks) {
        AbstractVillager self = (AbstractVillager) (Object) this;
        self.getEntityData().set(SBA$SNAKE_OIL_LOCKOUT, ticks);
    }

    @Unique
    @Override
    public int sba$getSnakeOilLockout() {
        AbstractVillager self = (AbstractVillager) (Object) this;
        return self.getEntityData().get(SBA$SNAKE_OIL_LOCKOUT);
    }

    @SuppressWarnings("ConstantValue")
    @Unique
    @Override
    public void sba$applySnakeOilScam(Player player) {
        AbstractVillager self = (AbstractVillager) (Object) this;
        AABB box = self.getBoundingBox().inflate(48.0, 16.0, 48.0);
        for (AbstractVillager nearbyMerchant : self.level().getEntitiesOfClass(AbstractVillager.class, box)) {
            ((IVillagerAccess) nearbyMerchant).sba$setSnakeOilLockout(72000);
        }

        if (player != null && self instanceof Villager villager) {
            villager.getGossips().add(player.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
        }
    }
}