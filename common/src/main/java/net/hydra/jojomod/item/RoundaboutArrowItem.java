package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.StandArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RoundaboutArrowItem extends Item {

    public RoundaboutArrowItem(Properties $$0) {
        super($$0);
    }

    public StandArrowEntity createArrow(Level $$0, ItemStack $$1, LivingEntity $$2) {
        StandArrowEntity $$3 = new StandArrowEntity($$0, $$2, $$1);
        return $$3;
    }
}
