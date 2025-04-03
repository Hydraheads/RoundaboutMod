package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class LifeTrackerEntity extends GeneralSimpleStand {
    public LifeTrackerEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
    public boolean isEffectivelyInWater() {
        return this.wasTouchingWater;
    }
    public void tick() {
        boolean client = this.level().isClientSide();
        LivingEntity user = this.getUser();
        if (!client) {
            if (isEffectivelyInWater()) {
                this.discard();
                return;
            }
            if (user != null) {
                if (MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), user.getX(), user.getZ()) > 80
                        || !user.isAlive() || user.isRemoved()) {
                    this.discard();
                    return;
                }
            } else {
                this.discard();
                return;
            }
        }
        super.tick();
    }

}
