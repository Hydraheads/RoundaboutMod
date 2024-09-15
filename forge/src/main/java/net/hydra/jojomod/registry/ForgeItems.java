package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static net.hydra.jojomod.registry.ForgeCreativeTab.addToDiscTab;
import static net.hydra.jojomod.registry.ForgeCreativeTab.addToTab;

public class ForgeItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Roundabout.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Roundabout.MOD_ID);

    public static final RegistryObject<Item> COFFEE_GUM = addToTab(ITEMS.register("coffee_gum",
            () -> new Item(new Item.Properties()
                    .food(ModFoodComponents.COFFEE_GUM)
            )));

    public static final RegistryObject<Item> STAND_ARROW = addToTab(ITEMS.register("stand_arrow",
            () -> new Item(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> KNIFE = addToTab(ITEMS.register("knife",
            () -> new KnifeItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> KNIFE_BUNDLE = addToTab(ITEMS.register("knife_bundle",
            () -> new KnifeItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<Item> MATCH = addToTab(ITEMS.register("match",
            () -> new MatchItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> MATCH_BUNDLE = addToTab(ITEMS.register("match_bundle",
            () -> new MatchItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<Item> GASOLINE_CAN = addToTab(ITEMS.register("gasoline_can",
            () -> new GasolineCanItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<Item> GASOLINE_BUCKET = addToTab(ITEMS.register("gasoline_bucket",
            () -> new GasolineBucketItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<BlockItem> WIRE_TRAP = addToTab(ITEMS.register("wire_trap",
            () -> new BlockItem(ForgeBlocks.WIRE_TRAP.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> BARBED_WIRE = addToTab(ITEMS.register("barbed_wire",
            () -> new BlockItem(ForgeBlocks.BARBED_WIRE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WIRE_BUNDLE = addToTab(ITEMS.register("barbed_wire_bundle",
            () -> new BlockItem(ForgeBlocks.BARBED_WIRE_BUNDLE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> METEOR_BLOCK_ITEM = addToTab(ITEMS.register("meteor_block",
            () -> new BlockItem(ForgeBlocks.METEOR_BLOCK.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> LOCACACA_CACTUS_ITEM = addToTab(ITEMS.register("locacaca_cactus",
            () -> new BlockItem(ForgeBlocks.LOCACACA_CACTUS.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> GODDESS_STATUE_ITEM = addToTab(ITEMS.register("goddess_statue",
            () -> new BlockItem(ForgeBlocks.GODDESS_STATUE_BLOCK.get(),
                    new Item.Properties().stacksTo(1)
            )));
    public static final RegistryObject<BlockItem> STEREO_ITEM = addToTab(ITEMS.register("stereo",
            () -> new BlockItem(ForgeBlocks.STEREO.get(),
                    new Item.Properties().stacksTo(64)
            )));
    public static final RegistryObject<Item> LUCK_SWORD = addToTab(ITEMS.register("luck_sword",
            () -> new LuckSwordItem(Tiers.IRON, 5F, -2.8F, new Item.Properties())
            ));
    public static final RegistryObject<Item> IRON_GLAIVE = addToTab(ITEMS.register("iron_glaive",
            () -> new GlaiveItem(Tiers.IRON, 4F, -2.9F, new Item.Properties(),7)
    ));
    public static final RegistryObject<Item> DIAMOND_GLAIVE = addToTab(ITEMS.register("diamond_glaive",
            () -> new GlaiveItem(Tiers.DIAMOND, 4F, -2.9F, new Item.Properties(),9)
    ));
    public static final RegistryObject<Item> NETHERITE_GLAIVE = addToTab(ITEMS.register("netherite_glaive",
            () -> new GlaiveItem(Tiers.NETHERITE, 4F, -2.9F, new Item.Properties(),12)
    ));
    public static final RegistryObject<Item> SCISSORS = addToTab(ITEMS.register("scissors",
            () -> new ScissorItem(Tiers.IRON, 0F, -1.6F, new Item.Properties())
    ));
    public static final RegistryObject<Item> HARPOON = addToTab(ITEMS.register("harpoon",
            () -> new HarpoonItem((new Item.Properties()).durability(250))
    ));




    public static final RegistryObject<Item> LUCK_UPGRADE = addToTab(ITEMS.register("luck_upgrade",
            () -> new SmithingTemplateItem(SmithingTemplates.LUCK_UPGRADE_APPLIES_TO, SmithingTemplates.LUCK_UPGRADE_INGREDIENTS, SmithingTemplates.LUCK_UPGRADE, SmithingTemplates.LUCK_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.LUCK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createLuckUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    ));

    public static final RegistryObject<Item> STAND_DISC = addToTab(ITEMS.register("stand_disc",
            () -> new EmptyStandDiscItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> METEORITE = addToTab(ITEMS.register("meteorite",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> LOCACACA_PIT = addToTab(ITEMS.register("locacaca_pit",
            () -> new ItemNameBlockItem(ForgeBlocks.LOCACACA_BLOCK.get(), new Item.Properties())));
    public static final RegistryObject<Item> LOCACACA = addToTab(ITEMS.register("locacaca",
            () -> new LocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA))));
    public static final RegistryObject<Item> LOCACACA_BRANCH = addToTab(ITEMS.register("locacaca_branch",
            () -> new ItemNameBlockItem(ForgeBlocks.NEW_LOCACACA_BLOCK.get(), new Item.Properties())));
    public static final RegistryObject<Item> NEW_LOCACACA = addToTab(ITEMS.register("new_locacaca",
            () -> new NewLocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA))));
    public static final RegistryObject<Item> MUSIC_DISC_TORTURE_DANCE = addToTab(ITEMS.register("music_disc_torture_dance",
            () -> new RecordItem(1, ForgeSounds.TORTURE_DANCE,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 178)));
    public static final RegistryObject<Item> MUSIC_DISC_HALLELUJAH = addToTab(ITEMS.register("music_disc_hallelujah",
            () -> new RecordItem(1, ForgeSounds.HALLELUJAH,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 178)));
    public static final RegistryObject<ForgeSpawnEggItem> TERRIER_SPAWN_EGG = addToTab(ITEMS.register("terrier_spawn_egg",
            () -> new ForgeSpawnEggItem(ForgeEntities.TERRIER_DOG,
                    0xc9c071, 0xfffded, new Item.Properties())));
    public static final RegistryObject<Potion> HEX_POTION = POTIONS.register("roundabout.hex",
            () -> new Potion(new MobEffectInstance(ForgeEffects.HEX.get(), 9600, 0)));
    public static final RegistryObject<Potion> HEX_POTION_EXTENDED = POTIONS.register("roundabout.long_hex",
            () -> new Potion("roundabout.hex", new MobEffectInstance(ForgeEffects.HEX.get(), 9600, 0)));
    public static final RegistryObject<Potion> HEX_POTION_STRONG = POTIONS.register("roundabout.strong_hex",
            () -> new Potion("roundabout.hex", new MobEffectInstance(ForgeEffects.HEX.get(), 4800, 1)));

    public static final RegistryObject<Item> STAND_DISC_STAR_PLATINUM = addToDiscTab(ITEMS.register("star_platinum_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersStarPlatinum(null))));

    public static final RegistryObject<Item> STAND_DISC_THE_WORLD = addToDiscTab(ITEMS.register("the_world_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersTheWorld(null))));

    public static void assignStupidForge(){
        DispenserBlock.registerBehavior(ForgeItems.KNIFE.get(), KNIFE_DIS);


        DispenserBlock.registerBehavior(ForgeItems.KNIFE_BUNDLE.get(), KNIFE_BUNDLE_DIS);



        DispenserBlock.registerBehavior(ForgeItems.MATCH.get(), MATCH_DIS);


        DispenserBlock.registerBehavior(ForgeItems.MATCH_BUNDLE.get(), MATCH_BUNDLE_DIS);


        DispenserBlock.registerBehavior(ForgeItems.GASOLINE_BUCKET.get(),GASOLINE_BUCKET_DIS);

        DispenserBlock.registerBehavior(ForgeItems.GASOLINE_CAN.get(),GASOLINE_CAN_DIS);


        DispenserBlock.registerBehavior(ForgeItems.HARPOON.get(), HARPOON_DIS);
    }


    public static DefaultDispenseItemBehavior KNIFE_DIS = new DefaultDispenseItemBehavior() {
        public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
            Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
            Position position = DispenserBlock.getDispensePosition(p_123556_);
            double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
            double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
            double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
            Level level = p_123556_.getLevel();
            RandomSource randomsource = level.random;
            double d3 = randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D)*2;
            double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*2;
            double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*2;
            KnifeEntity knife = new KnifeEntity(level, d0, d1, d2);
            level.addFreshEntity(Util.make(knife, (p_123552_) -> {
                p_123552_.setDeltaMovement(d3,d4,d5);
                p_123552_.pickup = AbstractArrow.Pickup.ALLOWED;
            }));
            p_123557_.shrink(1);
            return p_123557_;
        }

        protected void playSound(BlockSource p_123554_) {
            p_123554_.getLevel().playSound(null, p_123554_.getPos(), ModSounds.KNIFE_THROW_SOUND_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    };


    public static DefaultDispenseItemBehavior KNIFE_BUNDLE_DIS = new DefaultDispenseItemBehavior() {
        public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
            Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
            Position position = DispenserBlock.getDispensePosition(p_123556_);
            double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
            double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
            double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
            Level level = p_123556_.getLevel();
            RandomSource randomsource = level.random;

            int knifeCount = 4;
            for (int i = 0; i< knifeCount; i++) {
                double d3 = randomsource.triangle((double) direction.getStepX(), 0.13)*1.4;
                double d4 = randomsource.triangle((double) direction.getStepY(), 0.13)*1.4;
                double d5 = randomsource.triangle((double) direction.getStepZ(), 0.13)*1.4;
                KnifeEntity knife = new KnifeEntity(level, d0, d1, d2);
                level.addFreshEntity(Util.make(knife, (p_123552_) -> {
                    p_123552_.setDeltaMovement(d3, d4, d5);
                    p_123552_.pickup = AbstractArrow.Pickup.ALLOWED;
                }));
            }
            p_123557_.shrink(1);
            return p_123557_;
        }

        protected void playSound(BlockSource p_123554_) {
            p_123554_.getLevel().playSound(null, p_123554_.getPos(), ModSounds.KNIFE_BUNDLE_THROW_SOUND_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    };


    public static DefaultDispenseItemBehavior MATCH_DIS = new DefaultDispenseItemBehavior() {
        public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
            Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
            Position position = DispenserBlock.getDispensePosition(p_123556_);
            double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
            double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
            double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
            Level level = p_123556_.getLevel();
            RandomSource randomsource = level.random;
            double d3 = (randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D)*1.4)*0.9;
            double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*1.4*0.9;
            double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*1.4*0.9;
            MatchEntity match = new MatchEntity(level, d0, d1, d2);
            level.addFreshEntity(Util.make(match, (p_123552_) -> {
                p_123552_.setDeltaMovement(d3,d4,d5);
            }));
            p_123557_.shrink(1);
            return p_123557_;
        }

        protected void playSound(BlockSource p_123554_) {
            p_123554_.getLevel().playSound(null, p_123554_.getPos(), ModSounds.MATCH_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    };

    public static DefaultDispenseItemBehavior MATCH_BUNDLE_DIS =
            new DefaultDispenseItemBehavior() {
                public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                    Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                    Position position = DispenserBlock.getDispensePosition(p_123556_);
                    double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                    double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
                    double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
                    Level level = p_123556_.getLevel();
                    RandomSource randomsource = level.random;

                    int knifeCount = 4;
                    for (int i = 0; i< knifeCount; i++) {
                        double d3 = randomsource.triangle((double) direction.getStepX(), 0.13)*0.9;
                        double d4 = randomsource.triangle((double) direction.getStepY(), 0.13)*0.9;
                        double d5 = randomsource.triangle((double) direction.getStepZ(), 0.13)*0.9;
                        MatchEntity match = new MatchEntity(level, d0, d1, d2);
                        level.addFreshEntity(Util.make(match, (p_123552_) -> {
                            p_123552_.setDeltaMovement(d3, d4, d5);
                        }));
                    }
                    p_123557_.shrink(1);
                    return p_123557_;
                }

                protected void playSound(BlockSource p_123554_) {
                    p_123554_.getLevel().playSound(null, p_123554_.getPos(), ModSounds.MATCH_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            };

    public static DefaultDispenseItemBehavior GASOLINE_BUCKET_DIS =
            new DefaultDispenseItemBehavior() {
                private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

                public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                    Level level = p_123556_.getLevel();
                    Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                    Position position = DispenserBlock.getDispensePosition(p_123556_);
                    double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                    double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
                    double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
                    RandomSource randomsource = level.random;
                    double d3 = (randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D)*0.9);
                    double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*0.9;
                    double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*0.9;
                    GasolineSplatterEntity gas = new GasolineSplatterEntity(level, d0, d1, d2);
                    level.addFreshEntity(Util.make(gas, (p_123552_) -> {
                        p_123552_.setDeltaMovement(d3,d4,d5);
                    }));
                    return new ItemStack(Items.BUCKET);
                }
                protected void playSound(BlockSource p_123554_) {
                    p_123554_.getLevel().playSound(null, p_123554_.getPos(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            };

    public static DefaultDispenseItemBehavior GASOLINE_CAN_DIS =
            new DefaultDispenseItemBehavior() {

                public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                    Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                    Position position = DispenserBlock.getDispensePosition(p_123556_);
                    double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                    double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
                    double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
                    Level level = p_123556_.getLevel();
                    RandomSource randomsource = level.random;
                    double d3 = (randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D)*0.6);
                    double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*0.6;
                    double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*0.6;
                    GasolineCanEntity gas = new GasolineCanEntity(level, d0, d1, d2);
                    level.addFreshEntity(Util.make(gas, (p_123552_) -> {
                        p_123552_.setDeltaMovement(d3,d4,d5);
                    }));
                    p_123557_.shrink(1);
                    return p_123557_;
                }

                protected void playSound(BlockSource p_123554_) {
                    p_123554_.getLevel().playSound(null, p_123554_.getPos(), ModSounds.GAS_CAN_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            };

    public static DefaultDispenseItemBehavior HARPOON_DIS =
            new DefaultDispenseItemBehavior() {

                public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                    Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                    Position position = DispenserBlock.getDispensePosition(p_123556_);
                    double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                    double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F );
                    double d2 = position.z() + (double) ((float) direction.getStepZ()  * 0.3F);
                    Level level = p_123556_.getLevel();
                    RandomSource randomsource = level.random;
                    double d3 = (randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D))*1.6;
                    double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*1.6;
                    double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*1.6;
                    if (!p_123557_.hurt(1,level.getRandom(),null)){
                        HarpoonEntity harpoon = new HarpoonEntity(level, null, p_123557_, d0, d1, d2);
                        level.addFreshEntity(Util.make(harpoon, (p_123552_) -> {
                            p_123552_.setDeltaMovement(d3,d4,d5);
                            p_123552_.pickup = AbstractArrow.Pickup.ALLOWED;
                        }));
                    }
                    p_123557_.shrink(1);
                    return p_123557_;
                }

                protected void playSound(BlockSource p_123554_) {
                    p_123554_.getLevel().playSound(null, p_123554_.getPos(), ModSounds.HARPOON_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            };


}
