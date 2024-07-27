package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class DispenserRegistry {

    public static void init(){
        DispenserBlock.registerBehavior(ModItems.KNIFE, new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                Position position = DispenserBlock.getDispensePosition(p_123556_);
                double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
                double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
                Level level = p_123556_.getLevel();
                RandomSource randomsource = level.random;
                double d3 = randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D)*0.7;
                double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*0.7;
                double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*0.7;
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
        });


        DispenserBlock.registerBehavior(ModItems.KNIFE_BUNDLE, new DefaultDispenseItemBehavior() {
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
                    double d3 = randomsource.triangle((double) direction.getStepX(), 0.13)*0.7;
                    double d4 = randomsource.triangle((double) direction.getStepY(), 0.13)*0.7;
                    double d5 = randomsource.triangle((double) direction.getStepZ(), 0.13)*0.7;
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
        });



        DispenserBlock.registerBehavior(ModItems.MATCH, new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                Position position = DispenserBlock.getDispensePosition(p_123556_);
                double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
                double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
                Level level = p_123556_.getLevel();
                RandomSource randomsource = level.random;
                double d3 = (randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D))*0.6;
                double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*0.6;
                double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*0.6;
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
        });


        DispenserBlock.registerBehavior(ModItems.MATCH_BUNDLE, new DefaultDispenseItemBehavior() {
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
                    double d3 = randomsource.triangle((double) direction.getStepX(), 0.13)*0.6;
                    double d4 = randomsource.triangle((double) direction.getStepY(), 0.13)*0.6;
                    double d5 = randomsource.triangle((double) direction.getStepZ(), 0.13)*0.6;
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
        });


        DispenserBlock.registerBehavior(ModItems.GASOLINE_CAN, new DefaultDispenseItemBehavior() {
            public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                Direction direction = p_123556_.getBlockState().getValue(DispenserBlock.FACING);
                Position position = DispenserBlock.getDispensePosition(p_123556_);
                double d0 = position.x() + (double) ((float) direction.getStepX() * 0.3F);
                double d1 = position.y() + (double) ((float) direction.getStepY() * 0.3F);
                double d2 = position.z() + (double) ((float) direction.getStepZ() * 0.3F);
                Level level = p_123556_.getLevel();
                RandomSource randomsource = level.random;
                double d3 = (randomsource.triangle((double) direction.getStepX(), 0.11485000000000001D))*0.4;
                double d4 = randomsource.triangle((double) direction.getStepY(), 0.11485000000000001D)*0.4;
                double d5 = randomsource.triangle((double) direction.getStepZ(), 0.11485000000000001D)*0.4;
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
        });

    }
}
