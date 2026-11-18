package net.hydra.jojomod.mixin.gold_experience;

import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowItem.class)
public abstract class GEArrowItemMixin {

    @Inject(method = "createArrow", at = @At("RETURN"))
    private void roundabout$transferGETreeImbueToArrow(Level level, ItemStack stack, LivingEntity shooter, CallbackInfoReturnable<AbstractArrow> cir) {
        AbstractArrow arrow = cir.getReturnValue();
        if (arrow != null && PowersGoldExperience.isTreeImbuedProjectileItem(stack)) {
            PowersGoldExperience.transferTreeImbueToProjectile(level, stack, arrow);
        }
    }
}