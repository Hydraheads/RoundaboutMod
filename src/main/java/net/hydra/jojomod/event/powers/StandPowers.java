package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.event.index.PowerIndex;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class StandPowers {
    private final LivingEntity self;

    /*The time that passed since using the last attack. It counts up, so that a visual meter can display cooldowns.
    * It is also used to */
    private int attackTime = 0;

    /*The time until the generic ability cooldown passes.
    This exists so you have downtime that non-stand users can get it and attack you during.*/
    private int attackTimeMax = 0;

    /*The id of the move being used. Ex: 1 = punch*/
    private int activePower = 0;

    /*The phase of the move being used, primarily to keep track of which punch you are on in a punch string.*/
    private int activePowerPhase = 0;

    /*Once a move finishes, this turns off in order to prevent a loop of infinite attacks should the move roll over.*/
    private boolean isAttacking = false;

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
    public int getActivePowerPhase(){
        return this.activePowerPhase;
    }

    public void setAttackTime(int attackTime){
        this.attackTime = attackTime;
    }
    public void setAttackTimeMax(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public int getAttackTimeMax(){
        return this.attackTimeMax;
    }
    public boolean getIsAttacking(){
        return this.isAttacking;
    }
    public void setIsAttacking(boolean isAttacking){
        this.isAttacking = isAttacking;
    }
    public void setMaxAttackTime(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public void setActivePower(int activeMove){
        this.activePower = activeMove;
    }
    public void setActivePowerPhase(int activePowerPhase){
        this.activePowerPhase = activePowerPhase;
    }

    public void switchActiveMove(int activeMove){
        this.setActivePower(activeMove);
        this.setAttackTime(0);
    }

    public void tickPower(){
        if (this.activePower != PowerIndex.NONE) {
            if (this.activePower == PowerIndex.ATTACK && this.isAttacking){
                //RoundaboutMod.LOGGER.info("attack4");
                this.updateAttack();
            } else {
                this.updateUniqueMoves();
            }
            this.attackTime++;
        }
    }
    public void updateAttack(){
        if (this.attackTime > this.attackTimeMax) {
            this.setPowerNone();
        } else {
            if (this.attackTime == 4) {
                this.standPunch();
            }
        }
    }

    public void standPunch(){
        float pow;
        if (this.activePowerPhase == 2) {
            this.attackTimeMax = 40;
            pow = 8;
        } else {
            this.activePowerPhase++;
            pow=6;
        }
        this.attackTime = 0;
        this.isAttacking = false;

        Vec3d pointVec = DamageHandler.getRayPoint(self, 3);
        if (!self.getWorld().isClient()){
            ((ServerWorld) self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z, 1,0.0, 0.0, 0.0,1);
        }
        DamageHandler.genHitbox(self, pow, pointVec.x, pointVec.y, pointVec.z, 2, 2, 2);
    }

    public void updateUniqueMoves(){
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    public void tryPower(int move, boolean forced){
        if (this.activePower == PowerIndex.NONE || forced){
        if (move == PowerIndex.ATTACK){
            this.setPowerAttack();
        }}
    }

    private void setPowerNone(){
        this.attackTimeMax = 0;
        this.switchActiveMove(PowerIndex.NONE);
    }
    public void setPowerAttack(){
        this.attackTimeMax = 30;
        this.isAttacking = true;
        this.switchActiveMove(PowerIndex.ATTACK);
    }
}
