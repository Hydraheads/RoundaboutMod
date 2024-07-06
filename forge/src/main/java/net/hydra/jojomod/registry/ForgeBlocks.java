package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.GasolineBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Roundabout.MOD_ID);

    public static final RegistryObject<Block> METEOR_BLOCK = BLOCKS.register("meteor_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
            ));
    public static final RegistryObject<Block> GASOLINE_SPLATTER = BLOCKS.register("gasoline_splatter",
            () -> new GasolineBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NONE)
                            .instrument(NoteBlockInstrument.BANJO)
                            .strength(0.25F, 1.0F)
                            .sound(SoundType.SLIME_BLOCK)
            ));
}
