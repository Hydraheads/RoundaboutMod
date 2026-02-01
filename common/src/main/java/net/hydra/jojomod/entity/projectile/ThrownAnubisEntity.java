package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrownAnubisEntity extends ThrownObjectEntity {

    public ThrownAnubisEntity(LivingEntity living, Level $$1, ItemStack itemStack) {
        super(living, $$1, itemStack, false);
    }
    public ThrownAnubisEntity(EntityType<ThrownAnubisEntity> thrownAnubisEntityEntityType, Level level) {
        super(thrownAnubisEntityEntityType,level);
    }

}
