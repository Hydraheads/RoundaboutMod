package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
    /**This is where blocks are listed and called upon.
     * Forge and fabric files must define these variables so they are not empty.**/
    public static final EnumProperty<GoddessStatuePart> GODDESS_STATUE_PART = EnumProperty.create("part", GoddessStatuePart.class);
    public static final IntegerProperty GAS_CAN_LEVEL = IntegerProperty.create("level", 0, 2);
    public static final IntegerProperty BLOOD_LEVEL = IntegerProperty.create("level", 0, 3);
    public static final BooleanProperty IGNITED = BooleanProperty.create("ignited");
    public static final BooleanProperty DECAY = BooleanProperty.create("decay");
    public static Block METEOR_BLOCK;
    public static Block LOCACACA_CACTUS;

    public static Block LOCACACA_BLOCK;
    public static Block NEW_LOCACACA_BLOCK;

    public static Block GASOLINE_SPLATTER;
    public static Block BLOOD_SPLATTER;
    public static Block BLUE_BLOOD_SPLATTER;
    public static Block ENDER_BLOOD_SPLATTER;

    public static Block WIRE_TRAP;
    public static Block BARBED_WIRE;
    public static Block BARBED_WIRE_BUNDLE;
    public static Block GODDESS_STATUE_BLOCK;
    public static Block STEREO;

    public static BlockEntityType<StereoBlockEntity> STEREO_BLOCK_ENTITY;
    public static Block METEOR_BLOCK_PROPERTIES = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
    );

    public static Block LOCACACA_CACTUS_PROPERTIES = new LocacacaCactusBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks().strength(0.4F)
                    .sound(SoundType.WOOL)
                    .pushReaction(PushReaction.DESTROY));

    public static Block LOCACACA_BLOCK_PROPERTIES = new LocacacaBlock(
            BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY)
            );
    public static Block NEW_LOCACACA_BLOCK_PROPERTIES = new NewLocacacaBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
    );

    public static GasolineBlock GASOLINE_SPLATTER_PROPERTIES = new GasolineBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE)
                    .instrument(NoteBlockInstrument.BANJO)
                    .strength(0.1F, 1.0F)
                    .sound(SoundType.SLIME_BLOCK)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY)
                    .ignitedByLava()
                    .speedFactor(0.6F)
    );
    public static BloodBlock BLOOD_SPLATTER_PROPERTIES = new BloodBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(0.01F, 0.5F)
                    .sound(SoundType.EMPTY)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY)
    );
    public static BloodBlock BLUE_BLOOD_SPLATTER_PROPERTIES = new EnderBloodBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(0.01F, 0.5F)
                    .sound(SoundType.EMPTY)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY)
                    .speedFactor(0.7F)
    );
    public static BloodBlock ENDER_BLOOD_SPLATTER_PROPERTIES = new EnderBloodBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(0.01F, 0.5F)
                    .sound(SoundType.EMPTY)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY)
    );
    public static BarbedWireBlock WIRE_TRAP_PROPERTIES = new BarbedWireBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(0.1F, 1.0F)
                    .sound(SoundType.VINE)
                    .forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY),
            1F
    );
    public static BarbedWireBlock BARBED_WIRE_BLOCK_PROPERTIES = new BarbedWireBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(0.5F, 1.0F)
                    .sound(SoundType.METAL)
                    .forceSolidOn().noCollission().requiresCorrectToolForDrops(),
            1.2F
    );
    public static BarbedWireBundleBlock BARBED_WIRE_BUNDLE_PROPERTIES = new BarbedWireBundleBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(1.5F, 1.0F)
                    .sound(SoundType.METAL)
                    .forceSolidOn().noCollission().requiresCorrectToolForDrops()
    );
    public static GoddessStatueBlock GODDESS_STATUE_BLOCK_PROPERTIES = new GoddessStatueBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .noOcclusion()
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .pushReaction(PushReaction.DESTROY)
                    .strength(1.5F, 1.0F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
    );

    public static StereoBlock STEREO_PROPERTIES = new StereoBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).
                    instrument(NoteBlockInstrument.BASS).strength(1.0F, 6.0F));

}
