package net.hydra.jojomod.mixin.magicians_red;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.IAbstractFurnaceBlockEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MagiciansRedAbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements
        WorldlyContainer, RecipeHolder, StackedContentsCompatible, IAbstractFurnaceBlockEntity {

    /**Allows the possibility of directly setting a furnace as heated, so that Magician's Red
     * can heat furnaces up.
     * */

    @Unique
    @Override
    public void roundabout$setFurnaceHeatingTime(int heatingTime){
        this.litTime =heatingTime;
        this.litDuration = litTime;
        this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isLit())), 3);
        this.setChanged();
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    int litTime;
    @Shadow
    int litDuration;

    @Shadow protected abstract boolean isLit();

    protected MagiciansRedAbstractFurnaceBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }
}
