package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.stand.powers.WhitesnakeControlInventory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.RecordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class DiscItemLifetimeMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void roundaboutWhitesnake$keepDiscsFromDespawning(CallbackInfo ci) {
        ItemEntity item = (ItemEntity) (Object) this;
        if (!ClientNetworking.getAppropriateConfig().whitesnakeSettings.doDiscsDespawn
                && !(item.getItem().getItem() instanceof RecordItem)
                && WhitesnakeControlInventory.isDisc(item.getItem())) {
            item.setUnlimitedLifetime();
        }
    }
}
