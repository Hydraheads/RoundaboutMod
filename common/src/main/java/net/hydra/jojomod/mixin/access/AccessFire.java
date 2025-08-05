package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireBlock.class)
public class AccessFire implements IFireBlock {
    /**Allows us to both access fire functions and add flammable blocks to the game's fire registry*/
    @Override
    public void roundabout$setFlammableBlock(Block $$0, int $$1, int $$2) {
        this.setFlammable($$0, $$1, $$2);
    }

    @Override
    public int roundabout$getBurnOdds(BlockState $$0){
        return getBurnOdds($$0);
    }
    @Override
    public int roundabout$getIgniteOdds(BlockState $$0){
        return getIgniteOdds($$0);
    }
    @Override
    public boolean roundabout$canBurn(BlockState $$0) {
        return canBurn($$0);
    }



    /**Register Flammable blocks here*/
    public void roundabout$bootstrap() {
        this.roundabout$setFlammableBlock(ModBlocks.GASOLINE_SPLATTER, 15, 100);
        this.roundabout$setFlammableBlock(ModBlocks.REGAL_WALL, 5, 5);
        this.roundabout$setFlammableBlock(ModBlocks.REGAL_FLOOR, 5, 5);
        this.roundabout$setFlammableBlock(ModBlocks.WOODEN_MANOR_TABLE, 5, 5);
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    private int getBurnOdds(BlockState $$0) {
        return 0;
    }
    @Shadow
    private int getIgniteOdds(BlockState $$0) {
        return 0;
    }
    @Shadow
    protected boolean canBurn(BlockState $$0) {
        return true;
    }
    @Shadow
    private void setFlammable(Block $$0, int $$1, int $$2) {
    }
}