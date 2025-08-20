package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;

public class DispenserRegistry {

    public static DefaultDispenseItemBehavior KNIFE = new DefaultDispenseItemBehavior() {
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


    public static DefaultDispenseItemBehavior KNIFE_BUNDLE = new DefaultDispenseItemBehavior() {
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


    public static DefaultDispenseItemBehavior MATCH = new DefaultDispenseItemBehavior() {
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

    public static DefaultDispenseItemBehavior MATCH_BUNDLE =
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

    public static DefaultDispenseItemBehavior GASOLINE_BUCKET =
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

    public static DefaultDispenseItemBehavior GASOLINE_CAN =
            new DefaultDispenseItemBehavior() {

                public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                        Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                        Position position = DispenserBlock.getDispensePosition(p_123556_);
                        double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                        double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
                        double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
                        Level level = p_123556_.getLevel();
                        RandomSource randomsource = level.random;
                        double d3 = (randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D) * 0.6);
                        double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D) * 0.6;
                        double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D) * 0.6;
                        GasolineCanEntity gas = new GasolineCanEntity(level, d0, d1, d2);
                        level.addFreshEntity(Util.make(gas, (p_123552_) -> {
                            p_123552_.setDeltaMovement(d3, d4, d5);
                        }));
                        p_123557_.shrink(1);
                    return p_123557_;
                }

                protected void playSound(BlockSource p_123554_) {
                    p_123554_.getLevel().playSound(null, p_123554_.getPos(), ModSounds.GAS_CAN_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            };

    public static DefaultDispenseItemBehavior HARPOON =
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

    public static DefaultDispenseItemBehavior STAND_ARROW =
            new DefaultDispenseItemBehavior() {

                public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {

                    if (!(p_123557_.getDamageValue() >= p_123557_.getMaxDamage())) {
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
                        StandArrowEntity stand_arrow = new StandArrowEntity(level, null, p_123557_, d0, d1, d2);
                        level.addFreshEntity(Util.make(stand_arrow, (p_123552_) -> {
                                p_123552_.setDeltaMovement(d3,d4,d5);
                                p_123552_.pickup = AbstractArrow.Pickup.ALLOWED;
                        }));
                        p_123556_.getLevel().playSound(null, p_123556_.getPos(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        p_123557_.shrink(1);
                    } else {
                        Direction $$2 = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                        Position $$3 = DispenserBlock.getDispensePosition(p_123556_);
                        ItemStack $$4 = p_123557_.split(1);
                        p_123556_.getLevel().levelEvent(1000, p_123556_.getPos(), 0);
                        spawnItem(p_123556_.getLevel(), $$4, 6, $$2, $$3);
                    }
                    return p_123557_;
                }

                protected void playSound(BlockSource p_123554_) {
                }
            };

    public static DefaultDispenseItemBehavior FLESH_BUCKET =
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
                    FleshPileEntity gas = new FleshPileEntity(level, d0, d1, d2);
                    level.addFreshEntity(Util.make(gas, (p_123552_) -> {
                        p_123552_.setDeltaMovement(d3,d4,d5);
                    }));
                    return new ItemStack(Items.BUCKET);
                }
                protected void playSound(BlockSource p_123554_) {
                    p_123554_.getLevel().playSound(null, p_123554_.getPos(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            };

    public static void init(){
        DispenserBlock.registerBehavior(ModItems.KNIFE, KNIFE);

        DispenserBlock.registerBehavior(ModItems.KNIFE_BUNDLE, KNIFE_BUNDLE);

        DispenserBlock.registerBehavior(ModItems.MATCH, MATCH);

        DispenserBlock.registerBehavior(ModItems.MATCH_BUNDLE, MATCH_BUNDLE);

        DispenserBlock.registerBehavior(ModItems.GASOLINE_BUCKET,GASOLINE_BUCKET);

        DispenserBlock.registerBehavior(ModItems.GASOLINE_CAN,GASOLINE_CAN);

        DispenserBlock.registerBehavior(ModItems.HARPOON, HARPOON);

        DispenserBlock.registerBehavior(ModItems.STAND_ARROW, STAND_ARROW);

        DispenserBlock.registerBehavior(ModItems.STAND_BEETLE_ARROW, STAND_ARROW);

        DispenserBlock.registerBehavior(ModItems.WORTHY_ARROW, STAND_ARROW);

        DispenserBlock.registerBehavior(ModItems.FLESH_BUCKET,FLESH_BUCKET);

    }
}
