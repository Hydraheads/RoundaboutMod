package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.access.AccessFateFoodData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FateFoodDataMixin implements AccessFateFoodData {
    /**Makes vampires not replen from food*/
    @Unique
    public Player rdbt$player = null;
    @Unique
    @Override
    public void rdbt$setPlayer(Player pl){
        rdbt$player = pl;
    }

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$eat(Item $$0, ItemStack $$1, CallbackInfo ci) {
        if (rdbt$player != null){
            if (FateTypes.hasBloodHunger(rdbt$player)){
                ci.cancel();
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tickVamp(Player $$0, CallbackInfo ci) {

    }
}
