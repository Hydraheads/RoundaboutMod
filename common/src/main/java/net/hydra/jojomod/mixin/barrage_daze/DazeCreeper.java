package net.hydra.jojomod.mixin.barrage_daze;

import net.hydra.jojomod.access.ICreeper;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value= Creeper.class, priority = 100)
public abstract class DazeCreeper extends Monster {

    /**This mixin is in relation to barrages disabling mobs from attacking or doing things.
     * The daze that barrages inflict prevent creepers from swelling up and exploding.
     *
     * This mixin also makes it so when the creeper is using barrages or attacks
     * that it shouldn't explode during, it stops.*/
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(CallbackInfo ci) {
        StandUser user = ((StandUser) this);
        if (user.roundabout$isDazed() ||
                (!user.roundabout$getStandDisc().isEmpty() &&
                        user.roundabout$getStandPowers().disableMobAiAttack())) {
            if (this.isAlive()) {
                oldSwell = swell;
            }

            this.swell -= 1;
            if (this.swell < 0) {
                this.swell = 0;
            }
            super.tick();
            ci.cancel();
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    protected DazeCreeper(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Shadow
    private int oldSwell;
    @Shadow
    private int swell;
}
