package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
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
public abstract class ZIronGolem extends AbstractGolem implements NeutralMob, IIronGolem {
    protected ZIronGolem(EntityType<? extends AbstractGolem> $$0, Level $$1) {
        super($$0, $$1);
    }

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

    @Shadow
    protected void registerGoals() {

    }
    @Unique
    public ShapeShifts roundabout$lastSeenAsMorph = ShapeShifts.PLAYER;
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
}
