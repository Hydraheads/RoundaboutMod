package net.hydra.jojomod.block;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GinkgoPressurePlateBlock extends PressurePlateBlock {

    public GinkgoPressurePlateBlock(
            Sensitivity sensitivity,
            BlockBehaviour.Properties properties,
            BlockSetType blockSetType
    ) {
        super(sensitivity, properties, blockSetType);
    }
}
