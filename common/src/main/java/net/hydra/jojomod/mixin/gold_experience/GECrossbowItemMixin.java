package net.hydra.jojomod.mixin.gold_experience;

import net.hydra.jojomod.access.IGEProjectile;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public abstract class GECrossbowItemMixin {

    @Unique
    private static final String ROUNDABOUT$GE_TREE_IMBUED_LOADED = "GETreeImbuedLoaded";

    @Inject(method = "loadProjectile", at = @At("HEAD"))
    private static void roundabout$onLoadTreeImbuedArrow(LivingEntity shooter, ItemStack crossbow, ItemStack ammoStack, boolean hasAmmo, boolean isCreative, CallbackInfoReturnable<Boolean> cir) {
        if (!shooter.level().isClientSide() && PowersGoldExperience.isTreeImbuedProjectileItem(ammoStack)) {
            crossbow.getOrCreateTag().putBoolean(ROUNDABOUT$GE_TREE_IMBUED_LOADED, true);
            PowersGoldExperience.clearImbueFromConsumedStack(shooter.level(), ammoStack);
        }
    }

    @Inject(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void roundabout$onShootTreeImbuedArrow(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbow, ItemStack ammoStack,
            float soundPitch, boolean isCreative, float velocity, float inaccuracy, float projectileAngle, CallbackInfo ci, boolean isFirework, Projectile projectile) {
        if (!level.isClientSide() && projectileAngle == 0.0F) {
            CompoundTag tag = crossbow.getTag();
            if (tag != null && tag.getBoolean(ROUNDABOUT$GE_TREE_IMBUED_LOADED)) {
                tag.remove(ROUNDABOUT$GE_TREE_IMBUED_LOADED);
                ((IGEProjectile) projectile).roundabout$setGETreeImbue(true);
                if (projectile instanceof AbstractArrow arrow) {
                    arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }
        }
    }
}