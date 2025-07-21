package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class JusticeZombie extends Monster {

    /**A workaround to zombie aggro. This is created so that switching to zombies calms them down under proper
     * circumstances, as long as you didnt attack the zombie or anything..
     *
     * This mixin exists in relation to Justice's fog morph ability, the one where it disguises
     * as select humanoid vanilla mobs, based on the JoJo OVA in which Enya disguises as younger.*/
    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        if (this.getTarget() instanceof Player $$0){
            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (shift == ShapeShifts.ZOMBIE) {
                    this.setTarget(null);
                    this.setLastHurtByPlayer(null);
                    this.setLastHurtByMob(null);
                }
            }
        }
    }




    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    protected JusticeZombie(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }
}
