package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.powers.GeneralPowers;
import net.minecraft.client.Options;
import net.minecraft.world.entity.LivingEntity;

public class PunchingGeneralPowers extends GeneralPowers {
    public PunchingGeneralPowers(LivingEntity self) {
        super(self);
    }
    public boolean isBrawling(){
        return true;
    }
    public PunchingGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new PunchingGeneralPowers(entity);
    }

    @Override
    public boolean isMining() {
        return true;
    }

    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) { if (this.canAttack()) {
            this.tryPower(PowerIndex.ATTACK);
            tryPowerPacket(PowerIndex.ATTACK);
        }}
    }
    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        StandUser standUser = ((StandUser) this.getSelf());
        if (isGuarding() && this.getSelf().getVehicle() == null) {
            basis*=0.2f;
        }
        return basis;
    }

    public boolean cancelSprintJump(){
        return this.isGuarding();
    }

    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf()) && !this.isGuarding()) {
            buttonInputAttack(keyIsDown, options);
        }
    }
    public void preCheckButtonInputUse(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            buttonInputUse(keyIsDown, options);
        }
    }
    public void preCheckButtonInputBarrage(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            buttonInputBarrage(keyIsDown, options);
        }
    }
    public boolean preCheckButtonInputGuard(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            return buttonInputGuard(keyIsDown, options);
        }
        return false;
    }

    public boolean canGuard(){
        return !this.isBarraging();
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && canGuard()) {
            ((StandUser)this.getSelf()).roundabout$tryPowerP(PowerIndex.GUARD,true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }


    @Override
    public boolean interceptAttack(){
        return true;
    }

    @Override
    /**If the standard right click input should usually be canceled while your stand is active*/
    public boolean interceptGuard(){
        return true;
    }
}
