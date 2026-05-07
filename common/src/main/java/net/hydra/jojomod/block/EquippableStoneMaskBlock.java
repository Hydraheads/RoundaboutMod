package net.hydra.jojomod.block;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class EquippableStoneMaskBlock extends StoneMaskBlock implements Equipable {
    protected EquippableStoneMaskBlock(BlockBehaviour.Properties $$0) {
        super($$0);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
