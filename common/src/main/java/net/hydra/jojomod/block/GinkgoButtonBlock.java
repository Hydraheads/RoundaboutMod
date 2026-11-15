package net.hydra.jojomod.block;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GinkgoButtonBlock extends ButtonBlock {

    public GinkgoButtonBlock(
            BlockBehaviour.Properties properties,
            BlockSetType blockSetType,
            int ticksToStayPressed,
            boolean arrowCanPress
    ) {
        super(properties, blockSetType, ticksToStayPressed, arrowCanPress);
    }
}