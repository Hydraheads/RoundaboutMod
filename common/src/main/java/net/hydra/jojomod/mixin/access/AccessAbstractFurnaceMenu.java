package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IAbstractFurnaceMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractFurnaceMenu.class)
public abstract class AccessAbstractFurnaceMenu extends RecipeBookMenu<Container> implements IAbstractFurnaceMenu {
    /**There is no reason for these to be private or protected, we should be able to tap into them.*/
    @Unique
    @Override
    public Container roundabout$getContainer(){
        return this.container;
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow @Final private Container container;

    public AccessAbstractFurnaceMenu(MenuType<?> $$0, int $$1) {
        super($$0, $$1);
    }
}
