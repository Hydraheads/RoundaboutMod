package net.hydra.jojomod.mixin.gold_experience;

import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SnowballItem.class)
public class GESnowballItemMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void roundabout$transferTreeImbueToSnowball(Level level, Player player, InteractionHand hand,
                                                        CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, ItemStack itemStack, Snowball snowball) {
        PowersGoldExperience.transferTreeImbueToProjectile(level, itemStack, snowball);
    }
}
