package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.item.LuckyLipstickItem;
import net.hydra.jojomod.item.RoundaboutArrowItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class UnenchantAnvilMenu extends ItemCombinerMenu {
    /**This mixin prevents the enchanting of items marked as unenchantable in the mod.
     * In vanilla, items marked unenchantable do not lose their ability to be given
     * enchantments for books because Mojang did not think too hard about that.*/
    private final DataSlot cost = DataSlot.standalone();

    @Inject(method = "createResult()V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$spawnChildFromBreeding(CallbackInfo ci) {
        ItemStack itemstack = this.inputSlots.getItem(0);
        if (!itemstack.isEmpty()) {
            ItemStack itemstack2 = this.inputSlots.getItem(1);
            if (!itemstack2.isEmpty() && itemstack.getItem() instanceof RoundaboutArrowItem && itemstack2.getItem() instanceof EnchantedBookItem) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.cost.set(0);
                    ci.cancel();
            } else if (!itemstack2.isEmpty() && itemstack.getItem() instanceof LuckyLipstickItem && itemstack2.getItem() instanceof EnchantedBookItem) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.cost.set(0);
                ci.cancel();
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public UnenchantAnvilMenu(@Nullable MenuType<?> $$0, int $$1, Inventory $$2, ContainerLevelAccess $$3) {
        super($$0, $$1, $$2, $$3);
    }
}
