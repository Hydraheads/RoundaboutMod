package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class CenturyBoyStances {


    @Shadow public abstract void knockback(double $$0, double $$1, double $$2);

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void knockbackBoost(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if((Object)this instanceof StandUser user) {
            if (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB){

                Player player = (Player) (Object)this;

                if (PCB.staticMode == 3){
                    if (source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                            source.is(DamageTypes.WITHER) ||
                            source.is(DamageTypes.DRAGON_BREATH) ||
                            source.is(ModDamageTypes.GO_BEYOND) ||
                            source.is(DamageTypes.GENERIC_KILL)
                    ) { cir.setReturnValue(true);}

                   player.hurtMarked = true;

                    Entity entity = source.getEntity();
                    if (entity != null) {
                        double x = entity.getX() - player.getX();
                        double z;
                        for(z = entity.getZ() - player.getZ(); x* x + z * z < 1.0E-4; z = (Math.random() - Math.random()) * 0.01) {
                            x = (Math.random() - Math.random()) * 0.01;
                        }

                        if (entity.getType() == EntityType.IRON_GOLEM){
                            knockback((double) 2.8F,x*2,z*2);
                        }else {
                            knockback((double) 0.8F,x*2,z*2);
                        }
                    }

                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void redstoneActivate(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if ((Object)this instanceof StandUser user){
            if (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB){
                Player player = (Player)(Object)this;


                if (PCB.staticMode == 4){
                    if (source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                            source.is(DamageTypes.WITHER) ||
                            source.is(DamageTypes.DRAGON_BREATH) ||
                            source.is(ModDamageTypes.GO_BEYOND) ||
                            source.is(DamageTypes.GENERIC_KILL)
                    ) { cir.setReturnValue(true);}

                    player.hurtMarked = true;
                    BlockPos playerPos = player.getOnPos();
                    Level level = player.level();

                    int range = 3;
                    BlockPos corner1 = playerPos.offset(-range, -range, -range);
                    BlockPos corner2 = playerPos.offset(range,range,range);

                    for (BlockPos targetPos : BlockPos.betweenClosed(corner1,corner2)){
                        BlockState state = level.getBlockState(targetPos);
                        Block block = state.getBlock();
                        if (block instanceof TntBlock tnt){
                            tnt.explode(level, targetPos);
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(),11);

                        }else if (block instanceof ObserverBlock observer) {
                            if (!level.getBlockTicks().hasScheduledTick(targetPos, observer)) {
                                level.scheduleTick(targetPos, observer, 2);
                            }
                        }else if (block instanceof SculkSensorBlock || block instanceof CalibratedSculkSensorBlock) {
                            if (!level.getBlockTicks().hasScheduledTick(targetPos, block)){
                                level.gameEvent(player, GameEvent.PROJECTILE_LAND, player.position());
                            }
                        }else if (block instanceof DoorBlock door) {
                            if (state.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER){
                                boolean isOpen = state.getValue(DoorBlock.OPEN);
                                door.setOpen(null,level,state,targetPos,!isOpen);

                            }
                        } else if (block instanceof TrapDoorBlock door) {
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
}
