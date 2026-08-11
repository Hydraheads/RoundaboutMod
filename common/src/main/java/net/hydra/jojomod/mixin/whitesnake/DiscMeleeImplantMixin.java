package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.item.AbstractBodyDiscItem;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class DiscMeleeImplantMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$implantWithoutDamage(Entity targetEntity, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();
        if (!(targetEntity instanceof LivingEntity target)) return;

        if (stack.getItem() instanceof AbstractBodyDiscItem bodyDisc) {
            bodyDisc.implantFromThrow(stack, target, player);
            ci.cancel();
            return;
        }
        if (!(stack.getItem() instanceof StandDiscItem)) return;

        if (!target.level().isClientSide() && ((StandUser) target).roundabout$getStandDisc().isEmpty()) {
            ItemStack implanted = stack.copy();
            implanted.setCount(1);
            DiscItemData.setOwnerIfMissing(implanted, target);
            if (StandArrowItem.grantStand(implanted, target)) {
                target.level().playSound(null, target.blockPosition(), ModSounds.WHITESNAKE_DISC_INSERT_EVENT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!player.isCreative()) stack.shrink(1);
            }
        }
        ci.cancel();
    }
}
