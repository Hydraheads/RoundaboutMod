package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.UnburnableProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SilverChariotRapierShotEntity extends AbstractArrow implements UnburnableProjectile {
    protected SilverChariotRapierShotEntity(EntityType<? extends AbstractArrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}
