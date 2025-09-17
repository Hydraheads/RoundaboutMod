package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class PowersGreenDay extends NewPunchingStand {
    public PowersGreenDay(LivingEntity self) {super(self);}




    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public int getMaxGuardPoints(){
        return 20;
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
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        else
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

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
            case SKILL_3_NORMAL -> {
                Roundabout.LOGGER.info("dash");
                dash();
            }
            case SKILL_3_CROUCH -> {
                Roundabout.LOGGER.info("Separation_Leap");
                tryToStandLeapClient();
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
        if(crawlTimer > 0){
            forceCrawl();
        }
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
            this.setCooldown(PowerIndex.SKILL_4, 0);
            this.setCooldown(PowerIndex.SKILL_4_GUARD, 0);
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }

    }
    public int getLeapLevel(){
        return 3;
    }
    public int bonusLeapCount = -1;
    public int crawlTimer = 0;
    public void bigLeap(LivingEntity entity,float range, float mult){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(1).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y,blockHit.getLocation().z))*0.75+1;

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                ((blockHit.getLocation().x - this.getSelf().getX())/mag)*mult,
                (0.6+Math.max((blockHit.getLocation().y - this.getSelf().getY())/mag,0))*mult,
                ((blockHit.getLocation().z - this.getSelf().getZ())/mag)*mult
        );

    }
    public void tryToStandLeapClient() {

        if (vaultOrFallBraceFails()) {
            if (this.getSelf().onGround()) {
                boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpAndDashShareCooldown;
                if ((jojoveinLikeKeys && !this.onCooldown(PowerIndex.SKILL_3)) ||
                        (!jojoveinLikeKeys && !this.onCooldown(PowerIndex.GLOBAL_DASH))) {
                    if (canExecuteMoveWithLevel(getLeapLevel())) {
                        if (jojoveinLikeKeys) {
                            this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                        } else {
                            this.setCooldown(PowerIndex.GLOBAL_DASH, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                        }
                        crawlTimer = 120;
                        bonusLeapCount = 3;
                        bigLeap(this.getSelf(), 20, 1);
                        ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                        ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                        tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
                    }
                }
            }
        }
    }

    public void forceCrawl(){
        this.self.setSwimming(true);
        this.self.setPose(Pose.SWIMMING);
        crawlTimer --;
        


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
