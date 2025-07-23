package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value= BowItem.class, priority = 100)
public class SoftAndWetBowItem {
    /**This mixin makes the encasement bubble pop when you fire a bow while in the bubble.*/
    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    public void roundabout$releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3, CallbackInfo ci) {

        if ($$2 != null && ((StandUser) $$2).roundabout$isBubbleEncased()) {
            StandUser SE = ((StandUser) $$2);
            int $$7 = this.getUseDuration($$0) - $$3;
            float $$8 = getPowerForTime($$7);
            if (!((double) $$8 < 0.1)) {
                if (!$$1.isClientSide()) {
                    SE.roundabout$setBubbleEncased((byte) 0);
                    $$1.playSound(null, $$2.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                    ((ServerLevel) $$1).sendParticles(ModParticles.BUBBLE_POP,
                            $$2.getX(), $$2.getY() + $$2.getBbHeight() * 0.5, $$2.getZ(),
                            5, 0.25, 0.25, 0.25, 0.025);
                }
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

    @Shadow
    public static float getPowerForTime(int $$0) {
        return 0;
    }
}
