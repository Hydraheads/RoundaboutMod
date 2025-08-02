package net.hydra.jojomod.mixin.vanilla_tweaks;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class TameAbstractHorse extends Animal {
    /**Instantly tame horses in creative mode*/

    @Inject(method = "doPlayerRide", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$doPlayerRide(Player $$0, CallbackInfo ci) {
        if (ClientNetworking.getAppropriateConfig().vanillaMinecraftTweaks.mountingHorsesInCreativeTamesThem) {
            if ($$0 != null && !$$0.level().isClientSide() && $$0.isCreative()) {
                if (!this.isTamed()) {
                    this.tameWithName($$0);
                }
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    protected TameAbstractHorse(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    public abstract boolean isTamed();

    @Shadow public abstract boolean tameWithName(Player $$0);
}
