package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IIronGolem;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(IronGolem.class)
public abstract class JusticeIronGolem extends AbstractGolem implements NeutralMob, IIronGolem {

    /**A workaround to golem aggro. This is created so that switching to zombies and skeletons forces anger, but
     * switching off of them removes it. And switching to a villager calms them down.
     *
     * This mixin exists in relation to Justice's fog morph ability, the one where it disguises
     * as select humanoid vanilla mobs, based on the JoJo OVA in which Enya disguises as younger.*/
    @Inject(method = "aiStep()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/IronGolem;updatePersistentAnger(Lnet/minecraft/server/level/ServerLevel;Z)V",shift = At.Shift.BEFORE))
    protected void roundabout$aiStep(CallbackInfo ci) {
        if (this.getTarget() instanceof Player PE && !PE.isCreative() && !PE.isSpectator()){
            IPlayerEntity ple = ((IPlayerEntity) PE);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (roundabout$lastSeenAsMorph != shift){
                if (ShapeShifts.isZombie(roundabout$lastSeenAsMorph) || ShapeShifts.isSkeleton(roundabout$lastSeenAsMorph)) {
                    if (!ShapeShifts.isZombie(shift) && !ShapeShifts.isSkeleton(shift)){
                        setLastHurtByPlayer(null);
                        setLastHurtByMob(null);
                        setPersistentAngerTarget(null);
                        setTarget(null);
                        setRemainingPersistentAngerTime(0);
                        roundabout$setNegation(true);
                    }
                }
                roundabout$lastSeenAsMorph = shift;
            }

            if (ShapeShifts.isVillager(shift)){
                setLastHurtByPlayer(null);
                setLastHurtByMob(null);
                setPersistentAngerTarget(null);
                setTarget(null);
                setRemainingPersistentAngerTime(0);
                roundabout$setNegation(true);
            }
        }
    }

    /**Have to save some extra variables to get this working*/

    @Unique
    public boolean roundabout$Negation = false;

    @Override
    @Unique
    public boolean roundabout$getNegation(){
        return roundabout$Negation;
    }
    @Override
    @Unique
    public void roundabout$setNegation(boolean negate){
        roundabout$Negation = negate;
    }
    @Unique
    public ShapeShifts roundabout$lastSeenAsMorph = ShapeShifts.PLAYER;


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    protected void registerGoals() {

    }
    protected JusticeIronGolem(EntityType<? extends AbstractGolem> $$0, Level $$1) {
        super($$0, $$1);
    }
}
