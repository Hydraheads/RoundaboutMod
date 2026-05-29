package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SuspiciousStewItem.class)
public abstract class VampireSuspiciousStewCancel extends Item {


    public VampireSuspiciousStewCancel(Properties $$0) {
        super($$0);
    }

    @Inject(method = "finishUsingItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
    private void rdbt$finishUsingItem(ItemStack $$0, Level $$1, LivingEntity $$2, CallbackInfoReturnable<ItemStack> cir) {
        if (FateTypes.hasBloodHunger($$2)){
            ItemStack $$3 = super.finishUsingItem($$0, $$1, $$2);
            Objects.requireNonNull($$2);
            cir.setReturnValue($$2 instanceof Player && ((Player)$$2).getAbilities().instabuild ? $$3 : new ItemStack(Items.BOWL));
        }
    }
}
