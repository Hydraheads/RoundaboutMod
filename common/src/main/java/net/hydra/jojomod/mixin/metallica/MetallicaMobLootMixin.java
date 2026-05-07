package net.hydra.jojomod.mixin.metallica;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MetallicaMobLootMixin {

    @Inject(method = "dropCustomDeathLoot", at = @At("RETURN"))
    private void roundabout$dropIronNuggetsWithMetallica(DamageSource source, int looting, boolean hitByPlayer, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (hitByPlayer && source.getEntity() instanceof Player player) {

            boolean playerHasMetallica = false;

            if (player instanceof StandUser standUser) {
                if (standUser.roundabout$getStandPowers() instanceof PowersMetallica) {
                    playerHasMetallica = true;
                }
            }

            if (playerHasMetallica) {
                int amount = entity.getRandom().nextInt(7) + 1;

                entity.spawnAtLocation(new ItemStack(Items.IRON_NUGGET, amount));
            }
        }
    }
}