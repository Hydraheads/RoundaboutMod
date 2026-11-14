package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class GEItemDisguiseMixin {

    @ModifyVariable(
            method = "getModel",
            at = @At("HEAD"),
            argsOnly = true
    )
    private ItemStack roundabout$disguiseGetModel(ItemStack stack) {
        ItemStack fake = PowersGoldExperience.getDisguisedDisplayStack(stack);
        return fake.isEmpty() ? stack : fake;
    }

    @ModifyVariable(
            method = "render",
            at = @At("HEAD"),
            argsOnly = true
    )
    private ItemStack roundabout$disguiseRender(ItemStack stack) {
        ItemStack fake = PowersGoldExperience.getDisguisedDisplayStack(stack);
        return fake.isEmpty() ? stack : fake;
    }
}