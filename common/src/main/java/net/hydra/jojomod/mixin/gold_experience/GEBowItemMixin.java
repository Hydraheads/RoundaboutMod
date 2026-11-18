package net.hydra.jojomod.mixin.gold_experience;

import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public abstract class GEBowItemMixin {

    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean roundabout$transferTreeImbueToArrow(Level level, Entity entity, ItemStack bow, Level levelArg, LivingEntity shooter, int timeLeft) {
        if (entity instanceof AbstractArrow arrowEntity && shooter instanceof Player player) {
            ItemStack ammoStack = player.getProjectile(bow);
            if (PowersGoldExperience.isTreeImbuedProjectileItem(ammoStack)) {
                PowersGoldExperience.transferTreeImbueToProjectile(level, ammoStack, arrowEntity);
                arrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                // infinity dupe with tree imbue prevention hopefully maybe
                if (!player.getAbilities().instabuild && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0) {
                    ammoStack.shrink(1);
                    if (ammoStack.isEmpty()) {
                        player.getInventory().removeItem(ammoStack);
                    }
                }
            }
        }
        return level.addFreshEntity(entity);
    }
}