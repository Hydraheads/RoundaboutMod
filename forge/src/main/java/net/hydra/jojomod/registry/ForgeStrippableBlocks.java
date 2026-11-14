package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = Roundabout.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ForgeStrippableBlocks {

    @SubscribeEvent
    public static void onAxeStrip(BlockEvent.BlockToolModificationEvent event) {

        if (event.getToolAction() != ToolActions.AXE_STRIP) {
            return;
        }

        BlockState state = event.getState();

        if (state.is(ForgeBlocks.GINKGO_LOG.get())) {

            event.setFinalState(
                    ForgeBlocks.STRIPPED_GINKGO_LOG.get()
                            .defaultBlockState()
                            .setValue(
                                    RotatedPillarBlock.AXIS,
                                    state.getValue(RotatedPillarBlock.AXIS)
                            )
            );

        } else if (state.is(ForgeBlocks.GINKGO_WOOD.get())) {

            event.setFinalState(
                    ForgeBlocks.STRIPPED_GINKGO_WOOD.get()
                            .defaultBlockState()
                            .setValue(
                                    RotatedPillarBlock.AXIS,
                                    state.getValue(RotatedPillarBlock.AXIS)
                            )
            );
        }
    }
}