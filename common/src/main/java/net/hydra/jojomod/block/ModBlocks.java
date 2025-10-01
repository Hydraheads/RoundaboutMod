package net.hydra.jojomod.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToIntFunction;

public class ModBlocks {
    /**
     * This is where blocks are listed and called upon.
     * Forge and fabric files must define these variables so they are not empty.
     **/
    public static final EnumProperty<GoddessStatuePart> GODDESS_STATUE_PART = EnumProperty.create("part", GoddessStatuePart.class);
    public static final EnumProperty<StreetSignPart> STREET_SIGN_PART = EnumProperty.create("part", StreetSignPart.class);
    public static final IntegerProperty DAMAGED = IntegerProperty.create("damaged", 0, 2);
    public static final IntegerProperty GAS_CAN_LEVEL = IntegerProperty.create("level", 0, 2);
    public static final IntegerProperty BLOOD_LEVEL = IntegerProperty.create("level", 0, 3);
    public static final BooleanProperty IGNITED = BooleanProperty.create("ignited");
    public static final BooleanProperty DECAY = BooleanProperty.create("decay");
    public static final BooleanProperty IN_FOG = BooleanProperty.create("in_fog");
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 12);
    public static final IntegerProperty FLESH_LAYER = IntegerProperty.create("layers", 1, 4);
    public static Block ANCIENT_METEOR;
    public static Block METEOR_BLOCK;
    public static Block IMPACT_MOUND;
    public static Block REGAL_FLOOR;
    public static Block REGAL_WALL;
    public static Block WOODEN_MANOR_TABLE;
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

    public static Block MINING_ALERT_BLOCK;
    public static Block BUBBLE_SCAFFOLD;
    public static Block INVISIBLOCK;

    public static Block FLESH_BLOCK;

    public static Block STAND_FIRE;
    public static Block ORANGE_FIRE;
    public static Block BLUE_FIRE;
    public static Block PURPLE_FIRE;
    public static Block GREEN_FIRE;
    public static Block DREAD_FIRE;
    public static Block CREAM_FIRE;
    public static Block FOG_DIRT;
    public static Block FOG_DIRT_COATING;
    public static Block FOG_TRAP;

    public static Block EQUIPPABLE_STONE_MASK_BLOCK;

    public static Block D4C_LIGHT_BLOCK;

    public static BlockEntityType<StandFireBlockEntity> STAND_FIRE_BLOCK_ENTITY;
    public static BlockEntityType<StereoBlockEntity> STEREO_BLOCK_ENTITY;
    public static BlockEntityType<MirrorBlockEntity> MIRROR_BLOCK_ENTITY;
    public static BlockEntityType<BubbleScaffoldBlockEntity> BUBBLE_SCAFFOLD_BLOCK_ENTITY;
    public static BlockEntityType<InvisiBlockEntity> INVISIBLE_BLOCK_ENTITY;
    public static BlockEntityType<FogTrapBlockEntity> FOG_TRAP_BLOCK_ENTITY;
    public static BlockEntityType<D4CLightBlockEntity> D4C_LIGHT_BLOCK_ENTITY;
    public static Block ANCIENT_METEOR_PROPERTIES = new AncientMeteorBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
    );
    public static Block IMPACT_MOUND_PROPERTIES = new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).speedFactor(0.7f).strength(0.5F).sound(SoundType.GRAVEL));

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
    public static Block REGAL_WALL_PROPERTIES = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.HARP)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()
    );
    public static Block REGAL_FLOOR_PROPERTIES = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.HARP)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()
    );
    public static Block WOODEN_MANOR_TABLE_PROPERTIES = new ManorTableBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.HARP)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()
    );
    public static Block WOODEN_MANOR_CHAIR_PROPERTIES = new ManorChairBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.HARP)
                    .strength(2.0F).sound(SoundType.WOOD).ignitedByLava()
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

    public static StreetSignBlock getStreetSignBlockProperties() {
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

    public static StreetWallSignBlock getWallStreetSignBlockProperties() {
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

    public static MirrorBlock getMirrorBlockProperties() {
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

    public static FogBlock getFogBlock() {
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

    public static FogBlock getFogCoatingBlock() {
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
    public static FogTrapBlock getFogTrapBlock(){
        return new FogTrapBlock(
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

    public static InvisiBlock INVISIBLE_BLOCK_PROPERTIES = new InvisiBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NONE).forceSolidOn().noOcclusion().strength(-1.0F, 3600000.0F).lightLevel((p_152607_) -> {
                return 1;
            }).noParticlesOnBreak().pushReaction(PushReaction.IGNORE).noParticlesOnBreak().sound(SoundType.EMPTY));
    public static StereoBlock STEREO_PROPERTIES = new StereoBlock(

            BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).
                    instrument(NoteBlockInstrument.BASS).strength(1.0F, 6.0F));
    public static MiningAlertBlock MINING_ALERT_BLOCK_PROPERTIES = new MiningAlertBlock(

            BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).pushReaction(PushReaction.DESTROY).lightLevel((p_152607_) -> {
                        return 15;
                    }).noParticlesOnBreak().
                    instrument(NoteBlockInstrument.BASS).strength(0, 0));
    public static Block FLESH_BLOCK_PROPERTIES = new FleshBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PODZOL)
                    .instrument(NoteBlockInstrument.BANJO)
                    .strength(5.0F, 1.0F)
                    .sound(SoundType.HONEY_BLOCK)
    );
    public static BubbleScaffoldBlock BUBBLE_SCAFFOLD_BLOCK_PROPERTIES = new BubbleScaffoldBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).noCollission().dynamicShape().speedFactor(0.7F).replaceable().instabreak().lightLevel((p_152607_) -> {
                return 1;
            }).noParticlesOnBreak().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY));
    public static D4CLightBlock D4C_LIGHT_BLOCK_PROPERTIES = new D4CLightBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).noCollission().dynamicShape().speedFactor(0.7F).replaceable().instabreak().lightLevel((p_152607_) -> {
                return 1;
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

    public static EquippableStoneMaskBlock EQUIPPABLE_STONE_MASK_PROPERTIES = new EquippableStoneMaskBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(0.35F).sound(SoundType.STONE).pushReaction(PushReaction.DESTROY).lightLevel((L) -> {
                return 1;
            }));

    public static List<String> dontGenState = new ArrayList<String>();
    public static List<String> blockBlacklist = Arrays.asList(
            "soul_fire", "air", "allium", "lava_cauldron", "end_gateway", "cave_air", "void_air", "infested_stone",
            "barrier", "structure_void", "beacon", "glass","spawner","fog_crafting_table_coating","fog_smithing_table_coating",
            "fog_cartography_table_coating","fog_dragon_egg_coating", "fog_fletching_table_coating","tinted_glass","fog_crimson_nylium_coating",
            "fog_warped_nylium_coating","glass_pane",
            "white_stained_glass_pane",
            "orange_stained_glass_pane",
            "magenta_stained_glass_pane",
            "light_blue_stained_glass_pane",
            "yellow_stained_glass_pane",
            "lime_stained_glass_pane",
            "pink_stained_glass_pane",
            "gray_stained_glass_pane",
            "light_gray_stained_glass_pane",
            "cyan_stained_glass_pane",
            "purple_stained_glass_pane",
            "blue_stained_glass_pane",
            "brown_stained_glass_pane",
            "green_stained_glass_pane",
            "red_stained_glass_pane",
            "black_stained_glass_pane",
            "white_stained_glass",
            "orange_stained_glass",
            "magenta_stained_glass",
            "light_blue_stained_glass",
            "yellow_stained_glass",
            "lime_stained_glass",
            "pink_stained_glass",
            "gray_stained_glass",
            "light_gray_stained_glass",
            "cyan_stained_glass",
            "purple_stained_glass",
            "blue_stained_glass",
            "brown_stained_glass",
            "green_stained_glass",
            "red_stained_glass",
            "black_stained_glass",
            "azalea",
            "flowering_azalea",
            "fog_trap",
            "fog_trap_coating"
    );

    public static List<String> dontGen = Arrays.asList("fog_dirt");
    public static List<Item> gennedFogItems = new ArrayList<>();
}
