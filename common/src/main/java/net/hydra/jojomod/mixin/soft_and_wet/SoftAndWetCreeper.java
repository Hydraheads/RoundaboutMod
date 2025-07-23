package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Creeper.class)
public abstract class SoftAndWetCreeper extends Monster {
    /**Soft and Wet stand using Creepers are silent when they go boom boom, they spawn a sound bubble which the client
     * cant register immediately so they have to not play noise in general too.*/

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(CallbackInfo ci) {
        StandUser user = ((StandUser) this);

        if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW) {
            /**Soft and Wet creepers don't make a sound*/
            ci.cancel();
            if (this.isAlive()) {
                this.oldSwell = this.swell;
                if (this.isIgnited()) {
                    this.setSwellDir(1);
                }

                int $$0 = this.getSwellDir();
                if ($$0 > 0 && this.swell == 0) {
                    PW.creeperSpawnBubble();
                }

                this.swell += $$0;
                if (this.swell < 0) {
                    this.swell = 0;
                }

                if (this.swell >= this.maxSwell) {
                    this.swell = this.maxSwell;
                    this.explodeCreeper();
                }
            }

            super.tick();
        }


    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    protected SoftAndWetCreeper(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    public abstract int getSwellDir();

    @Shadow protected abstract void explodeCreeper();

    @Shadow private int maxSwell;

    @Shadow public abstract boolean isIgnited();

    @Shadow public abstract void setSwellDir(int $$0);

    @Shadow
    private int oldSwell;
    @Shadow
    private int swell;

}
