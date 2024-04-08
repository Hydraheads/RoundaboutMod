package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.event.index.PowerIndex;
import net.minecraft.entity.LivingEntity;

public class StandPowers {
    private final LivingEntity self;
    private int attackTime;
    private int activePower = 0;

    public StandPowers(LivingEntity self) {
        this.self = self;
    }
    public LivingEntity getSelf(){
        return this.self;
    }
    public int getAttackTime(){
        return this.attackTime;
    }
    public int getActivePower(){
        return this.activePower;
    }

    public void setAttackTime(int attackTime){
        this.attackTime = attackTime;
    }
    public void setActivePower(int activeMove){
        this.activePower = activeMove;
    }

    public void switchActiveMove(int activeMove){
        this.setActivePower(activeMove);
        this.setAttackTime(0);
    }

    public void tickPower(){
        if (this.activePower != PowerIndex.NONE) {
            if (this.activePower == PowerIndex.ATTACK){
                this.updateAttack();
            } else {
                this.updateUniqueMoves();
            }
            this.attackTime++;
        }
    }
    public void updateAttack(){

    }
    public void updateUniqueMoves(){
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    public void tryPower(int move, boolean forced){
        if (this.activePower == PowerIndex.NONE || forced){
        if (move == PowerIndex.ATTACK){
            genericAttack();
        }}
    }

    private void genericAttack(){
        switchActiveMove(PowerIndex.ATTACK);
    }
}
