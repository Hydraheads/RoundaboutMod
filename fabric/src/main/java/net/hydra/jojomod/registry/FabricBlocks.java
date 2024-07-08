package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class FabricBlocks {
    public static final Block METEOR_BLOCK = registerBlock("meteor_block",new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.METAL)
            )
    );
    public static final Block GASOLINE_SPLATTER = registerBlock("gasoline_splatter",new GasolineBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NONE)
                            .instrument(NoteBlockInstrument.BANJO)
                            .strength(0.1F, 1.0F)
                            .sound(SoundType.SLIME_BLOCK)
                            .replaceable()
                            .pushReaction(PushReaction.DESTROY)
                            .ignitedByLava()
                            .speedFactor(0.4F)
            )
    );


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Roundabout.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID, name),
                new BlockItem(block, new Item.Properties()));
    }

    public static void register(){
        ModBlocks.METEOR_BLOCK = METEOR_BLOCK;
        ModBlocks.GASOLINE_SPLATTER = GASOLINE_SPLATTER;
    }
}
