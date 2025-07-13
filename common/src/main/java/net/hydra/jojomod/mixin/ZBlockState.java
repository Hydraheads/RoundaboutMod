package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBlockState;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public class ZBlockState implements IBlockState {
    /***
    @Unique
    public boolean roundabout$isInvis = false;
    @Unique
    @Override
    public void roundabout$setInvis(boolean invis){
        roundabout$isInvis = invis;
    }
    @Unique
    @Override
    public boolean roundabout$getInvis(){
        return roundabout$isInvis;
    }
    **/
}
