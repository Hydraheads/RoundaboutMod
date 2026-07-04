package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.sql.Array;
import java.util.*;

@Mixin(LivingEntity.class)
public abstract class CenturyBoyStances {


    @Shadow
    public abstract void knockback(double $$0, double $$1, double $$2);

    private static final Map<BlockPos, Integer> BLOCK_DMG_MAP = new HashMap<>();
    private static final Map<UUID, Long> GROUND_STANCE_COOLDOWN = new HashMap<>();

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void knockbackStance(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof StandUser user) {
            if (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB) {

                Player player = (Player) (Object) this;

                if (PCB.staticMode == 3) {
                    if (source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                            source.is(DamageTypes.WITHER) ||
                            source.is(DamageTypes.DRAGON_BREATH) ||
                            source.is(ModDamageTypes.GO_BEYOND) ||
                            source.is(DamageTypes.GENERIC_KILL)
                    ) {
                        cir.setReturnValue(true);
                    }

                    player.hurtMarked = true;

                    Entity entity = source.getEntity();
                    if (entity != null) {
                        double x = entity.getX() - player.getX();
                        double z;
                        for (z = entity.getZ() - player.getZ(); x * x + z * z < 1.0E-4; z = (Math.random() - Math.random()) * 0.01) {
                            x = (Math.random() - Math.random()) * 0.01;
                        }

                        if (entity.getType() == EntityType.IRON_GOLEM) {
                            knockback(2.8F, x * 2, z * 2);
                        } else {
                            knockback(0.8F, x * 2, z * 2);
                        }
                    }

                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void redstoneStance(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof StandUser user) {
            if (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB) {
                Player player = (Player) (Object) this;


                if (PCB.staticMode == 4) {
                    if (source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                            source.is(DamageTypes.WITHER) ||
                            source.is(DamageTypes.DRAGON_BREATH) ||
                            source.is(ModDamageTypes.GO_BEYOND) ||
                            source.is(DamageTypes.GENERIC_KILL)
                    ) {
                        cir.setReturnValue(true);
                    }

                    player.hurtMarked = true;
                    BlockPos playerPos = player.getOnPos();
                    Level level = player.level();

                    int range = (amount < 10) ? 2 : (amount <= 20) ? 3 : (amount > 20) ? 4 : 2;
                    BlockPos corner1 = playerPos.offset(-range, -range, -range);
                    BlockPos corner2 = playerPos.offset(range, range, range);

                    for (BlockPos targetPos : BlockPos.betweenClosed(corner1, corner2)) {
                        BlockState state = level.getBlockState(targetPos);
                        Block block = state.getBlock();
                        if (block instanceof TntBlock tnt) {
                            tnt.explode(level, targetPos);
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 11);

                        } else if (block instanceof ObserverBlock observer) {
                            if (!level.getBlockTicks().hasScheduledTick(targetPos, observer)) {
                                level.scheduleTick(targetPos, observer, 2);
                            }
                        } else if (block instanceof SculkSensorBlock || block instanceof CalibratedSculkSensorBlock) {
                            if (!level.getBlockTicks().hasScheduledTick(targetPos, block)) {
                                level.gameEvent(player, GameEvent.PROJECTILE_LAND, player.position());
                            }
                        } else if (block instanceof DoorBlock door) {
                            if (state.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
                                boolean isOpen = state.getValue(DoorBlock.OPEN);
                                door.setOpen(null, level, state, targetPos, !isOpen);

                            }
                        } else if (block instanceof TrapDoorBlock) {
                            boolean isOpen = state.getValue(DoorBlock.OPEN);
                            level.setBlock(targetPos, state.setValue(TrapDoorBlock.OPEN, !isOpen), 3);

                        } else if (block instanceof RedstoneLampBlock lamp) {
                            boolean lit = state.getValue(RedstoneLampBlock.LIT);
                            level.setBlock(targetPos, state.setValue(RedstoneLampBlock.LIT, !lit), 3);

                            level.scheduleTick(targetPos, lamp, 50);
                        }
                    }
                    cir.setReturnValue(false);
                }
            }
        }
    }

    /// there's like so much optimization that can be done here, but i really don't want to mess with this anymore, so we'll leave as is for now
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void groundStance(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof StandUser user) {
            if (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB) {
                Player player = (Player) (Object) this;
                if (PCB.staticMode == 1) {
                    if (source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                            source.is(DamageTypes.WITHER) ||
                            source.is(DamageTypes.DRAGON_BREATH) ||
                            source.is(ModDamageTypes.GO_BEYOND) ||
                            source.is(DamageTypes.GENERIC_KILL)
                    ) {cir.setReturnValue(true);}

                    player.hurtMarked = true;
                    Level level = player.level();

                    long currentTime = System.currentTimeMillis();
                    UUID playerUUID = player.getUUID();
                    long lastTriggered = GROUND_STANCE_COOLDOWN.getOrDefault(playerUUID, 0L);
                    if (currentTime - lastTriggered < 1000) {
                        cir.setReturnValue(false);
                        return;
                    }

                    int breakstat = (amount <= 12) ? 2 : (amount <= 18) ? 3 : (amount <= 35) ? 5 : 10;

                    List<BlockPos> blocks = getClosestImpactBlock(player);

                    for (BlockPos pos : blocks) {
                        BlockPos immut = pos.immutable();
                        BlockState state = level.getBlockState(immut);


                        if (state.getDestroySpeed(level, immut) < 0) continue;

                        int currentDmg = BLOCK_DMG_MAP.getOrDefault(immut, 0);
                        int newDmg = currentDmg + breakstat;


                        int crackId = immut.hashCode();

                        if (newDmg >= 10) {
                            level.destroyBlock(immut, true);
                            BLOCK_DMG_MAP.remove(immut);
                            level.destroyBlockProgress(crackId, immut, -1);
                            GROUND_STANCE_COOLDOWN.put(playerUUID, currentTime);
                        } else {
                            BLOCK_DMG_MAP.put(immut, newDmg);
                            level.destroyBlockProgress(crackId, immut, newDmg);
                        }
                    }
                    cir.setReturnValue(false);
                }
            }
        }
    }

    private List<BlockPos> getClosestImpactBlock(Player player) {
        Level level = player.level();


        BlockPos floor = player.blockPosition().immutable();

        BlockPos[] poss = new BlockPos[]{floor, floor.north(), floor.south(), floor.west(), floor.east(),
                floor.north().west(), floor.south().west(),floor.north().east(), floor.south().east(), floor.above(),
                floor.below(), floor.above().north(), floor.below().north(), floor.above().south(), floor.below().south(),
                floor.above().west(), floor.below().west(),floor.above().east(), floor.below().east(),
                floor.above().north().west(), floor.below().north().west(), floor.above().north().east(), floor.below().north().east(),
                floor.above().south().west(), floor.below().south().west(),floor.above().south().east(), floor.below().south().east()};

        List<BlockPos> trupos = new ArrayList<>();

        for (int i = 0; i < poss.length; i++) {
            if (!level.getBlockState(poss[i]).isAir()){
                trupos.add(poss[i].immutable());
            }
        }

        return trupos;
    }
}