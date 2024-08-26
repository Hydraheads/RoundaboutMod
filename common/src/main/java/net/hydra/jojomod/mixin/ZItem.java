package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ZItem {

    @Shadow
    public boolean isEdible() {
        return false;
    }

    /**Hex prevents eating golden apples while full. At level 4, no foods can be eaten while full*/
    @Inject(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$addEatEffect(Level $$0, Player $$1, InteractionHand $$2, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (this.isEdible()) {
            if ($$1.hasEffect(ModEffects.HEX)) {
                ItemStack stack = $$1.getItemInHand($$2);
                int hexLevel = $$1.getEffect(ModEffects.HEX).getAmplifier();
                if ((hexLevel >= 0 && stack.is(Items.ENCHANTED_GOLDEN_APPLE)) || (hexLevel >= 1 && stack.is(Items.GOLDEN_APPLE))
                        || hexLevel >= 3) {
                    if ($$1.canEat(false)) {
                        $$1.startUsingItem($$2);
                        cir.setReturnValue(InteractionResultHolder.consume(stack));
                    } else {
                        cir.setReturnValue(InteractionResultHolder.fail(stack));
                    }
                }
            }
        }
    }
}
