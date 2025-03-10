package net.hydra.jojomod.mixin;

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
public abstract class ZAbstractFurnaceMenu extends RecipeBookMenu<Container> implements IAbstractFurnaceMenu {
    @Shadow @Final private Container container;

    public ZAbstractFurnaceMenu(MenuType<?> $$0, int $$1) {
        super($$0, $$1);
    }
    @Unique
    @Override
    public Container roundabout$getContainer(){
        return this.container;
    }
}
