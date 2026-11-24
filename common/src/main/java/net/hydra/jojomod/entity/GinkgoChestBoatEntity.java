package net.hydra.jojomod.entity;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class GinkgoChestBoatEntity extends ChestBoat {

    public GinkgoChestBoatEntity(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getDropItem() {
        return ModItems.GINKGO_CHEST_BOAT;
    }
}