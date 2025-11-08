package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;

public class VampireFate extends VampiricFate {


    public VampireFate() {
        super();
    }
    public VampireFate(LivingEntity self) {
        super(self);
    }

    public FatePowers generateFatePowers(LivingEntity entity){
        return new VampireFate(entity);
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                hypnosis();
            }
            case SKILL_2_NORMAL -> {
                suckBlood();
            }
        }
        super.powerActivate(context);
    };
    public static final byte HYPNOSIS = 50;

    public void hypnosis(){
        tryPowerPacket(HYPNOSIS);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == HYPNOSIS) {
            hypnosisServer();
        }
        return super.setPowerOther(move,lastMove);
    }
    public void hypnosisServer(){
        if (isHypnotizing){
            isHypnotizing = false;
            hypnoTicks = 0;
        } else {
            isHypnotizing = true;
            hypnoTicks = 0;
        }
    }
    public boolean isHypnotizing = false;
    public int hypnoTicks = 0;




    @Override
    public void tickPower(){
        tickHypnosis();
        super.tickPower();
    }
    public void tickHypnosis() {
        if (!self.level().isClientSide())
            if (isHypnotizing) {
                if (hypnoTicks % 9 == 0){

                    Vec3 lvec = self.getLookAngle();
                    Position pn = self.getEyePosition().add(lvec.scale(3));
                    Vec3 rev = lvec.reverse();
                    ((ServerLevel) this.self.level()).sendParticles(ModParticles.HYPNO_SWIRL, pn.x(),
                            pn.y(), pn.z(),
                            0,
                            rev.x,rev.y,rev.z,
                            0.08);
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.HYPNOSIS_EVENT, SoundSource.PLAYERS, 1F, 1F);

                    AABB aab = this.getSelf().getBoundingBox().inflate(10.0, 8.0, 10.0);
                    List<? extends LivingEntity> le = this.self.level().getNearbyEntities(Mob.class, hypnosisTargeting, ((LivingEntity)(Object)self), aab);
                    Iterator var4 = le.iterator();
                    while(var4.hasNext()) {
                        Mob nle = (Mob) var4.next();
                        if (!nle.isRemoved() && nle.isAlive() && !nle.isSleeping() &&
                                !(MainUtil.isHypnotismTargetBlacklisted(nle))){
                            if (nle.getTarget() == null || !nle.getTarget().isAlive()
                            || nle.getTarget().isRemoved()){
                                ((IMob) nle).roundabout$setHypnotizedBy(self);
                            }
                        }
                    }

                }
                hypnoTicks++;
            }
    }
    private final TargetingConditions hypnosisTargeting = TargetingConditions.forCombat().range(7);
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.FLESH_BUD, PowerIndex.FATE_1_SNEAK);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.HYPNOTISM, PowerIndex.FATE_1);
        }

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.REGENERATE, PowerIndex.FATE_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.BLOOD_DRINK, PowerIndex.FATE_2);
        }

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.CHEETAH_SPEED, PowerIndex.FATE_3_SNEAK);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 4, StandIcons.HEARING_MODE, PowerIndex.FATE_4_SNEAK);
        } else {
            if (isVisionOn()){
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_ON, PowerIndex.FATE_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.VAMP_VISION_OFF, PowerIndex.FATE_4);
            }
        }
    }
}
