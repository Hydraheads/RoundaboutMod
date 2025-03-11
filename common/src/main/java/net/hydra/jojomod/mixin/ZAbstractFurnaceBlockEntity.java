package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IAbstractFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class ZAbstractFurnaceBlockEntity extends BaseContainerBlockEntity implements
        WorldlyContainer, RecipeHolder, StackedContentsCompatible, IAbstractFurnaceBlockEntity {


    @Shadow
    int litTime;
    @Shadow
    int litDuration;
    @Shadow
    int cookingProgress;
    @Shadow
    int cookingTotalTime;

    @Shadow protected abstract boolean isLit();

    protected ZAbstractFurnaceBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Unique
    @Override
    public void roundabout$setFurnaceHeatingTime(int heatingTime){
        this.litTime =heatingTime;
        this.litDuration = litTime;
        this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isLit())), 3);
        this.setChanged();
    }
}
