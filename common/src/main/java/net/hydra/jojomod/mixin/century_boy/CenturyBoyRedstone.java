package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntity.class)
public class CenturyBoyRedstone {


    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void redstoneActivate(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if ((Object)this instanceof StandUser user){
            if (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB){
                Player player = (Player)(Object)this;


                if (PCB.redstoneStance){

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


                        }/** else if (state.hasProperty(BlockStateProperties.POWER)) {
                            BlockState newState = state.setValue(BlockStateProperties.POWER, 15);

                            level.setBlock(targetPos, newState, 3);
                        }
                         isn't working rn, will fix later
                         */
                    }

                    cir.setReturnValue(false);
                }
            }
        }
    }
}
