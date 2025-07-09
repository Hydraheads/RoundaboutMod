package net.hydra.jojomod.stand.powers;

import com.mojang.datafixers.optics.Lens;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;

import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;

import net.hydra.jojomod.mixin.StandUserEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class PowersGreenDay extends NewPunchingStand{
    public PowersGreenDay(LivingEntity self) {super(self);}




    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().guardPoints.d4cDefend;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GREEN_DAY.create(this.getSelf().level());
    }



    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersGreenDay(entity);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        ClientUtil.fx.roundabout$onGUI(context);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_STITCH, PowerIndex.SKILL_4_SNEAK);
        else
            if(isMoldFieldOn())
                setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_MOLD_FIELD_OFF, PowerIndex.SKILL_4);
            else
                setSkillIcon(context, x, y, 4, StandIcons.GREEN_DAY_MOLD_FIELD, PowerIndex.SKILL_4);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_ARM_RETURN_RIGHT, PowerIndex.SKILL_2_SNEAK);
        else
        if(isGuarding())
            setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT, PowerIndex.SKILL_2_GUARD);
        else
            setSkillIcon(context, x, y, 2, StandIcons.GREEN_DAY_MOLD_PUNCH_RIGHT, PowerIndex.SKILL_2);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 3, StandIcons.GREEN_DAY_MOLD_LEAP, PowerIndex.SKILL_3_CROUCH_GUARD);
        else
        if(isGuarding())
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_GUARD);
        else
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_ARM_RETURN_LEFT, PowerIndex.SKILL_1);
        else
        if(isGuarding())
            setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_MOLD_SPIN_LEFT, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_MOLD_PUNCH_LEFT, PowerIndex.SKILL_1);



        super.renderIcons(context, x, y);
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                Roundabout.LOGGER.info("dash");
                dash();

            }

            case SKILL_4_CROUCH, SKILL_4_CROUCH_GUARD -> {
                Stitch();
            }
            case SKILL_4_NORMAL -> {
                toggleMold();
            }
        }
    }
    public boolean IsMoldFieldActive = false;

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {

            case PowerIndex.POWER_4_SNEAK -> {
                return StitchHeal(1.0f, this.self);
            }
            case PowerIndex.POWER_4 -> {
                return toggleMoldField();
            }

        }
        return super.setPowerOther(move,lastMove);
    }
    @Override
    public void tickPower() {
        moldShenanigans();
        super.tickPower();
    }

    public void Stitch(){
        if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK)) {
            this.setCooldown(PowerIndex.SKILL_4_SNEAK, 400);
            this.setCooldown(PowerIndex.SKILL_4_CROUCH_GUARD, 400);
            this.tryPower(PowerIndex.POWER_4_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_4_SNEAK);
        }
    }

    public void toggleMold(){
        if (!this.onCooldown(PowerIndex.SKILL_4)) {
            this.setCooldown(PowerIndex.SKILL_4, 400);
            this.setCooldown(PowerIndex.SKILL_4_GUARD, 400);
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }

    }


    public boolean StitchHeal(float hp, LivingEntity entity) {

        if(!isClient()) {
            float maxhp = entity.getMaxHealth();
            float currenthp = entity.getHealth();

            if (currenthp < maxhp) {
                entity.setHealth(currenthp + 1.0f);
            }
            if (entity.hasEffect(ModEffects.BLEED)) {
                int level = entity.getEffect(ModEffects.BLEED).getAmplifier();
                int duration = entity.getEffect(ModEffects.BLEED).getDuration();
                entity.removeEffect(entity.getEffect(ModEffects.BLEED).getEffect());
                if (level > 0) {
                    entity.addEffect(new MobEffectInstance(ModEffects.BLEED, duration, level - 1));
                }
            }

            double Xangle = Math.toRadians(this.self.getLookAngle().x);
            double Pitch = Math.toRadians(this.self.getLookAngle().y);
            double Zangle = Math.toRadians(this.self.getLookAngle().z);
            double diameter = 0.6d;
            for(int i = 0; i < 11; i = i + 1) {
                ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.STITCH,
                        this.getSelf().getX() + (diameter * Math.sin(i*4)) * Math.cos(Xangle),
                        this.getSelf().getY() + (this.getSelf().getEyeHeight()*0.7),
                        this.getSelf().getZ() + (diameter * Math.cos(i*4)) * Math.cos(Zangle),
                        0,0,0,0,0);
            }


        }
        return true;
    }

    @Override
    public byte getPermaCastContext() {
        return PermanentZoneCastInstance.MOLD_FIELD;
    }

        public boolean toggleMoldField() {
        if (!this.getSelf().level().isClientSide()) {
            IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
            if (!icast.roundabout$isPermaCastingEntity(this.getSelf())) {
                icast.roundabout$addPermaCaster(this.getSelf());

            } else {
                removeMold();
            }
        }
        return true;
    }

    public void removeMold() {
        IPermaCasting icast = ((IPermaCasting) this.getSelf().level());
        icast.roundabout$removePermaCastingEntity(this.getSelf());
    }

    public void moldShenanigans() {
        if (!isClient()) {
            if(isMoldFieldOn()) {
                for(int i = 0; i < 84; i = i + 1) {
                    double randX = Roundabout.RANDOM.nextDouble(-50, 50);
                    double randY = Roundabout.RANDOM.nextDouble(-50, 50);
                    double randZ = Roundabout.RANDOM.nextDouble(-50, 50);
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD_DUST,
                            this.getSelf().getX() + randX,
                            this.getSelf().getY() + randY,
                            this.getSelf().getZ() + randZ,
                            0,1,-1,1,0.12);

                }


                //List<Entity> entityList = DamageHandler.genHitbox(this.getSelf(), this.getSelf().getX(), this.getSelf().getY(),
                 //       this.getSelf().getZ(), 50, 50, 50);
               // if (!entityList.isEmpty()){
             //       for (Entity value : entityList) {
           //             if (!value.is(this.self)){
         //                   if (value instanceof LivingEntity){
       //                         LivingEntity creature = (LivingEntity) value;
     //                           if(value.getY() < this.self.getY() && (value.getDeltaMovement().y<-0.0784000015258789)) {
   //                                 for (int i = 0; i < 3; i = i + 1) {

                              //          double width = value.getBbWidth();
                             //           double height = value.getBbHeight();
                             //           double randomX = Roundabout.RANDOM.nextDouble(0 - (width / 2), width / 2);
                             //           double randomY = Roundabout.RANDOM.nextDouble(0 - (height / 2), height / 2);
                             //           double randomZ = Roundabout.RANDOM.nextDouble(0 - (width / 2), width / 2);
                             //           ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MOLD,
                            //                    value.getX() + randomX, (value.getY() + height / 2) + randomY, value.getZ() + randomZ,
                            //                    0, value.getDeltaMovement().x, value.getDeltaMovement().y, value.getDeltaMovement().z,
                            //                    0.12);

                          //          }
                        //        }

                      //      }

                    //    }
                  //  }
                //}

            }
        }
    }

    public boolean isMoldFieldOn() {
        return((IPermaCasting) this.getSelf().level()).roundabout$isPermaCastingEntity(this.self);
    };




    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "Fish").withStyle(ChatFormatting.YELLOW);
    }

    public static final byte
            PART_FOUR = 1;


    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                PART_FOUR

        );
    }



}
