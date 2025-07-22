package net.hydra.jojomod.mixin.scissors;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TripWireBlock.class)
public abstract class ScissorsTripWireBlock extends Block {
    /**Scissors can be used on tripwires like shears*/

    @Inject(method = "playerWillDestroy", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$playerWillDestroy(Level $$0, BlockPos $$1, BlockState $$2, Player $$3, CallbackInfo ci) {
        if (!$$0.isClientSide && !$$3.getMainHandItem().isEmpty() && $$3.getMainHandItem().is(ModItems.SCISSORS)) {
            $$0.setBlock($$1, $$2.setValue(DISARMED, Boolean.valueOf(true)), 4);
            $$0.gameEvent($$3, GameEvent.SHEAR, $$1);
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    public static BooleanProperty DISARMED;
    public ScissorsTripWireBlock(Properties $$0) {
        super($$0);
    }
}
