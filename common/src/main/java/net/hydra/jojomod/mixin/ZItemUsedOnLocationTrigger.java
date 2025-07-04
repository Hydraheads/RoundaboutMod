package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemUsedOnLocationTrigger.class)
public abstract class ZItemUsedOnLocationTrigger {
    @Shadow public abstract ResourceLocation getId();

    /**Mandom code to incur placement penalty*/
    @Inject(method = "trigger", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$trigger(ServerPlayer $$0, BlockPos $$1, ItemStack $$2, CallbackInfo ci) {
        if ($$1 != null && getId().getPath().equals("placed_block")){
            ((StandUser)$$0).roundabout$getStandPowers().onPlaceBlock($$0,$$1,$$2);
        }
    }
}
