package net.hydra.jojomod.entity;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class GinkgoBoatEntity extends Boat {

    public GinkgoBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return ModItems.GINKGO_BOAT;
    }
}