package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.ToIntFunction;

public class ModBlocks {
    /**This is where blocks are listed and called upon.
     * Forge and fabric files must define these variables so they are not empty.**/
    public static final EnumProperty<GoddessStatuePart> GODDESS_STATUE_PART = EnumProperty.create("part", GoddessStatuePart.class);
    public static final EnumProperty<StreetSignPart> STREET_SIGN_PART = EnumProperty.create("part", StreetSignPart.class);
    public static final IntegerProperty DAMAGED = IntegerProperty.create("damaged", 0, 2);
    public static final IntegerProperty GAS_CAN_LEVEL = IntegerProperty.create("level", 0, 2);
    public static final IntegerProperty BLOOD_LEVEL = IntegerProperty.create("level", 0, 3);
    public static final BooleanProperty IGNITED = BooleanProperty.create("ignited");
    public static final BooleanProperty DECAY = BooleanProperty.create("decay");
    public static final BooleanProperty IN_FOG = BooleanProperty.create("in_fog");
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 12);
    public static Block ANCIENT_METEOR;
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
    public static Block STREET_SIGN_DIO;
    public static Block STREET_SIGN_RIGHT;
    public static Block STREET_SIGN_STOP;
    public static Block STREET_SIGN_YIELD;
    public static Block STREET_SIGN_DANGER;
    public static Block WALL_STREET_SIGN_DIO;
    public static Block WALL_STREET_SIGN_RIGHT;
    public static Block WALL_STREET_SIGN_STOP;
    public static Block WALL_STREET_SIGN_YIELD;
    public static Block WALL_STREET_SIGN_DANGER;
    public static Block CEILING_LIGHT;
    public static Block MIRROR;
    public static Block STEREO;

    public static Block STAND_FIRE;
    public static Block ORANGE_FIRE;
    public static Block BLUE_FIRE;
    public static Block PURPLE_FIRE;
    public static Block GREEN_FIRE;
    public static Block DREAD_FIRE;
    public static Block CREAM_FIRE;
    public static Block FOG_DIRT;
    public static Block FOG_DIRT_COATING;
    public static Block FOG_CLAY;
    public static Block FOG_CLAY_COATING;
    public static Block FOG_GRAVEL;
    public static Block FOG_GRAVEL_COATING;
    public static Block FOG_SAND;
    public static Block FOG_SAND_COATING;
    public static Block FOG_OAK_PLANKS;
    public static Block FOG_OAK_PLANKS_COATING;
    public static Block FOG_SPRUCE_PLANKS;
    public static Block FOG_SPRUCE_PLANKS_COATING;
    public static Block FOG_BIRCH_PLANKS;
    public static Block FOG_BIRCH_PLANKS_COATING;
    public static Block FOG_JUNGLE_PLANKS;
    public static Block FOG_JUNGLE_PLANKS_COATING;
    public static Block FOG_ACACIA_PLANKS;
    public static Block FOG_ACACIA_PLANKS_COATING;
    public static Block FOG_DARK_OAK_PLANKS;
    public static Block FOG_DARK_OAK_PLANKS_COATING;
    public static Block FOG_MANGROVE_PLANKS;
    public static Block FOG_MANGROVE_PLANKS_COATING;
    public static Block FOG_CHERRY_PLANKS;
    public static Block FOG_CHERRY_PLANKS_COATING;
    public static Block FOG_STONE;
    public static Block FOG_STONE_COATING;
    public static Block FOG_COBBLESTONE;
    public static Block FOG_COBBLESTONE_COATING;
    public static Block FOG_MOSSY_COBBLESTONE;
    public static Block FOG_MOSSY_COBBLESTONE_COATING;
    public static Block FOG_DEEPSLATE;
    public static Block FOG_DEEPSLATE_COATING;
    public static Block FOG_COAL_ORE;
    public static Block FOG_IRON_ORE;
    public static Block FOG_GOLD_ORE;
    public static Block FOG_LAPIS_ORE;
    public static Block FOG_DIAMOND_ORE;
    public static Block FOG_STONE_BRICKS;
    public static Block FOG_STONE_BRICKS_COATING;
    public static Block FOG_NETHERRACK;
    public static Block FOG_NETHERRACK_COATING;
    public static Block FOG_NETHER_BRICKS;
    public static Block FOG_NETHER_BRICKS_COATING;

    public static BlockEntityType<StandFireBlockEntity> STAND_FIRE_BLOCK_ENTITY;
    public static BlockEntityType<StereoBlockEntity> STEREO_BLOCK_ENTITY;
    public static BlockEntityType<MirrorBlockEntity> MIRROR_BLOCK_ENTITY;
    public static BlockEntityType<BubbleScaffoldBlockEntity> BUBBLE_SCAFFOLD_BLOCK_ENTITY;
    public static Block ANCIENT_METEOR_PROPERTIES = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
    );

    private static ToIntFunction<BlockState> litBlockEmission(int p_50760_) {
        return (p_50763_) -> {
            return p_50763_.getValue(BlockStateProperties.LIT) ? p_50760_ : 0;
        };
    }

    public static CeilingLightBlock CEILING_LIGHT_BLOCK_PROPERTIES = new CeilingLightBlock(
            BlockBehaviour.Properties.of().
                    mapColor(MapColor.SAND).
                    strength(0.1F).
                    lightLevel(litBlockEmission(15)
                    ).
                    pushReaction(PushReaction.DESTROY).
                    sound(SoundType.COPPER)
    );
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
    public static BloodBlock BLUE_BLOOD_SPLATTER_PROPERTIES = new BloodBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(0.01F, 0.5F)
                    .sound(SoundType.EMPTY)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY)
                    .speedFactor(0.4F)
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
    public static StreetSignBlock getStreetSignBlockProperties(){
        return new StreetSignBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .noOcclusion()
                        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                        .pushReaction(PushReaction.DESTROY)
                        .strength(0.01F, 0.01F)
                        .sound(SoundType.METAL)
                        .lightLevel((L) -> {
                            return 1;
                        })
        );
    }
    public static StreetWallSignBlock getWallStreetSignBlockProperties(){
        return new StreetWallSignBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .noOcclusion()
                        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                        .pushReaction(PushReaction.DESTROY)
                        .strength(0.01F, 0.01F)
                        .sound(SoundType.METAL)
                        .noCollission()
                        .lightLevel((L) -> {
                            return 1;
                        })
        );
    }
    public static MirrorBlock getMirrorBlockProperties(){
        return new MirrorBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .noOcclusion()
                        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                        .pushReaction(PushReaction.DESTROY)
                        .strength(0.01F, 0.01F)
                        .sound(SoundType.GLASS)
                        .noCollission()
                        .lightLevel((L) -> {
                            return 1;
                        })
        );
    }
    public static FogBlock getFogBlock(){
        return new FogBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
                        .strength(0F, 0F)
                        .sound(SoundType.EMPTY)
                        .noCollission()
                        .replaceable()
        );
    }
    public static FogBlock getFogCoatingBlock(){
        return new FogCoatBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
                        .strength(0F, 0F)
                        .sound(SoundType.EMPTY)
                        .noCollission()
                        .replaceable()
        );
    }
    public static StereoBlock STEREO_PROPERTIES = new StereoBlock(

            BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).
                    instrument(NoteBlockInstrument.BASS).strength(1.0F, 6.0F));
    public static StandFireBlock BUBBLE_SCAFFOLD_BLOCK_PROPERTIES = new StandFireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static StandFireBlock STAND_FIRE_PROPERTIES = new StandFireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static FireBlock ORANGE_FIRE_PROPERTIES = new FireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static FireBlock BLUE_FIRE_PROPERTIES = new FireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static FireBlock PURPLE_FIRE_PROPERTIES = new FireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static FireBlock GREEN_FIRE_PROPERTIES = new FireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static FireBlock DREAD_FIRE_PROPERTIES = new FireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static FireBlock CREAM_FIRE_PROPERTIES = new FireBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().instabreak().lightLevel((p_152607_) -> {
                return 15;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));

    public static void registerDynamicFogBlocks()
    {
        // TODO: dynamically generate blockstates and then it's ready for use
        for (Block b : BuiltInRegistries.BLOCK)
        {
            ResourceLocation i = BuiltInRegistries.BLOCK.getKey(b);
            // fix for not registering our own blocks as fog blocks, would result in a deadlock (or an error tbh)
            if (i.getNamespace().equals("roundabout") || BuiltInRegistries.BLOCK.containsKey(new ResourceLocation("roundabout", "fog_" + i.getPath())))
                continue;

            //Roundabout.LOGGER.info("Registering block \"roundabout:fog_{}\"",i.getPath());

            if (b.defaultBlockState().getProperties().isEmpty()) {
                Registry.register(BuiltInRegistries.BLOCK,
                        new ResourceLocation(Roundabout.MOD_ID, "fog_" + i.getPath()),
                        getFogBlock());
            } else {
                //Roundabout.LOGGER.warn("Skipping block {} as it has unsupported properties", i);
                continue;
            }
        }
    }
}
