package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PowersMagiciansRed;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StandFireBlockEntity extends BlockEntity{
    public StandFireBlockEntity(BlockPos $$0, BlockState $$1)
    {
        super(ModBlocks.STAND_FIRE_BLOCK_ENTITY, $$0, $$1);
    }

    public StandFireBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    int ticksStored = 0;
    int nextTarget = 0;
    int iterated = 0;
    int hardcap = 1200;

    public byte fireColorType = StandFireType.ORANGE.id;
    public int snapNumber = 0;
    public int fireIDNumber = 0;
    public LivingEntity standUser = null;

    public static void tickFire(Level lvl, BlockPos bp, BlockState bs, StandFireBlockEntity sf) {
        sf.tick(bs, lvl, bp, sf, lvl.getRandom());
    }

    public byte getFireColorType(BlockState $$0){
        return $$0.getValue(StandFireBlock.COLOR).byteValue();
    }
    public void rollNextTarget(){
        nextTarget = (int) (20 + Math.round(Math.random()*20));
    }
    public void tick(BlockState $$0, Level $$1, BlockPos $$2, StandFireBlockEntity sf, RandomSource $$3) {
        if (!$$1.isClientSide()) {
            if ($$0.getBlock() instanceof StandFireBlock fb) {
                BlockState $$4 = $$1.getBlockState($$2.below());
                boolean $$5 = $$4.is($$1.dimensionType().infiniburn());
                if (!$$5 && $$1.isRaining() && fb.isNearRain($$1, $$2)) {
                    $$1.removeBlock($$2, false);
                    return;
                }
                if (nextTarget == 0) {
                    rollNextTarget();
                }
                ticksStored++;
                if (iterated >= hardcap) {
                    $$1.removeBlock($$2, false);
                    return;
                } else if (standUser != null && standUser.isAlive() && !standUser.isRemoved()) {
                    if (((StandUser) standUser).roundabout$getStandPowers() instanceof PowersMagiciansRed PM) {
                        int maxFlames = ClientNetworking.getAppropriateConfig().maxMagiciansRedFlames;
                        int maxDist = ClientNetworking.getAppropriateConfig().maxMagiciansRedFlameDistance;
                        if (PM.snapNumber != snapNumber) {
                            $$1.removeBlock($$2, false);
                            return;
                        } else if (maxFlames >= 0 && (PM.fireIDNumber - fireIDNumber) > maxFlames){
                            $$1.removeBlock($$2, false);
                            return;
                        } else if (maxFlames >= 0 && MainUtil.cheapDistanceTo2($$2.getX(),$$2.getZ(),standUser.getX(),standUser.getZ()) > maxDist){
                            $$1.removeBlock($$2, false);
                            return;
                        }
                    } else {
                        standUser = null;
                    }
                } else {
                    $$1.removeBlock($$2, false);
                    return;
                }

                if (this.ticksStored >= nextTarget) {
                    ticksStored = 0;
                    rollNextTarget();
                    if ($$1.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
                        if (!$$0.canSurvive($$1, $$2)) {
                            $$1.removeBlock($$2, false);
                            return;
                        }

                        int $$6 = (Integer) $$0.getValue(StandFireBlock.AGE);
                        int color = (Integer) $$0.getValue(StandFireBlock.COLOR);
                        if (!$$5 && $$1.isRaining() && fb.isNearRain($$1, $$2) && $$3.nextFloat() < 0.2F + (float) $$6 * 0.03F) {
                            $$1.removeBlock($$2, false);
                        } else {
                            int $$7 = Math.min(15, $$6 + $$3.nextInt(3) / 2);
                            if ($$6 != $$7) {
                                iterated++;
                                if (standUser == null) {
                                    $$0 = (BlockState) $$0.setValue(StandFireBlock.AGE, $$7).setValue(StandFireBlock.COLOR, color);
                                    $$1.setBlockAndUpdate($$2, $$0);
                                }
                            }

                            if (!$$5) {
                                if (!fb.isValidFireLocation($$1, $$2)) {
                                    BlockPos $$8 = $$2.below();
                                    if (!$$1.getBlockState($$8).isFaceSturdy($$1, $$8, Direction.UP) || $$6 > 3) {
                                        $$1.removeBlock($$2, false);
                                    }

                                    return;
                                }

                                if ($$6 == 15 && $$3.nextInt(4) == 0 && !fb.canBurn($$1.getBlockState($$2.below()))) {
                                    $$1.removeBlock($$2, false);
                                    return;
                                }
                            }

                            boolean $$9 = $$1.getBiome($$2).is(BiomeTags.INCREASED_FIRE_BURNOUT);
                            int $$10 = $$9 ? -50 : 0;
                            fb.checkBurnOut($$1, $$2.east(), 300 + $$10, $$3, $$6,this);
                            fb.checkBurnOut($$1, $$2.west(), 300 + $$10, $$3, $$6,this);
                            fb.checkBurnOut($$1, $$2.below(), 250 + $$10, $$3, $$6,this);
                            fb.checkBurnOut($$1, $$2.above(), 250 + $$10, $$3, $$6,this);
                            fb.checkBurnOut($$1, $$2.north(), 300 + $$10, $$3, $$6,this);
                            fb.checkBurnOut($$1, $$2.south(), 300 + $$10, $$3, $$6,this);
                            BlockPos.MutableBlockPos $$11 = new BlockPos.MutableBlockPos();

                            for (int $$12 = -1; $$12 <= 1; ++$$12) {
                                for (int $$13 = -1; $$13 <= 1; ++$$13) {
                                    for (int $$14 = -1; $$14 <= 4; ++$$14) {
                                        if ($$12 != 0 || $$14 != 0 || $$13 != 0) {
                                            int $$15 = 100;
                                            if ($$14 > 1) {
                                                $$15 += ($$14 - 1) * 100;
                                            }

                                            $$11.setWithOffset($$2, $$12, $$14, $$13);
                                            int $$16 = fb.getIgniteOdds($$1, $$11);
                                            if ($$16 > 0) {
                                                int $$17 = ($$16 + 40 + $$1.getDifficulty().getId() * 7) / ($$6 + 30);
                                                if ($$9) {
                                                    $$17 /= 2;
                                                }

                                                if (standUser != null && ((StandUser)standUser).roundabout$getStandPowers() instanceof PowersMagiciansRed PM) {
                                                    if ($$17 > 0 && $$3.nextInt($$15) <= $$17 && (!$$1.isRaining() || !fb.isNearRain($$1, $$11))) {
                                                        int $$18 = Math.min(15, $$6 + $$3.nextInt(5) / 4);
                                                        Roundabout.LOGGER.info("3 Smurf: "+color);
                                                        $$1.setBlockAndUpdate($$11, fb.getStateWithAge($$1, $$11, $$18).setValue(StandFireBlock.COLOR, color));
                                                        BlockEntity be = this.level.getBlockEntity($$11);
                                                        if (be instanceof StandFireBlockEntity sfbe) {
                                                            sfbe.snapNumber = this.snapNumber;
                                                            sfbe.standUser = this.standUser;
                                                            sfbe.fireColorType = this.fireColorType;
                                                            sfbe.fireIDNumber = PM.getNewFireId();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
