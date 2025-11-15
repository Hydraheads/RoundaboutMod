package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.AnubisSlipstreamEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;

public class PowersAnubis extends NewDashPreset {
    public PowersAnubis(LivingEntity self) {
        super(self);
    }


    public static final int MaxPossesionTime = 100;
    public static final int PogoDelay = 8;
    public static final byte SWING = 50;


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersAnubis(entity);
    }

    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public boolean rendersPlayer(){
        return true;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (!isHoldingSneak() ) {
            setSkillIcon(context, x, y, 1, StandIcons.ANUBIS_ALLURING_LIGHT, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.ANUBIS_RAGING_LIGHT, PowerIndex.SKILL_1_SNEAK);
        }
        setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.SKILL_2);
        if (!isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.ANUBIS_BACKFLIP, PowerIndex.GLOBAL_DASH);
        }
        setSkillIcon(context, x, y, 4, StandIcons.NONE, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        if ( ((StandUser)this.getSelf()).roundabout$getActive() && this.getActivePower() != PowerIndex.GUARD) {
            basis *= this.getSelf().isSprinting() ? 1.6F : 1F;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_GUARD -> {
                AlluringLightClient();
            }
            case SKILL_1_CROUCH, SKILL_1_CROUCH_GUARD -> {
                RagingLightClient();
            }

            case SKILL_3_NORMAL -> {
                dash();
            }

            case SKILL_3_CROUCH -> {
                BackflipClient();
            }
        }
    }

    public void AlluringLightClient() {
        if (!onCooldown(PowerIndex.SKILL_1)) {
            this.setCooldown(PowerIndex.SKILL_1,200);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }
    public void AlluringLightServer() {
        this.setCooldown(PowerIndex.SKILL_1,200);
        int radius = 8;
        AABB box = this.getSelf().getBoundingBox().inflate(radius,2,radius);
        for (Mob M : this.getSelf().level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT,this.getSelf(),box)) {
            ((IMob)M).roundabout$setHypnotizedBy(this.getSelf(),200);
        }

        Vec3 pos = this.getSelf().getPosition(1);
        Vector3f[] colors = {
                new Vector3f(0.96F, 0.96F, 0.92F),
                new Vector3f(0.93F, 0.87F, 0.57F)
        };
        //TODO: PARTICLE SPRAY (with colors)
        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.FIREWORK,
                pos.x,
                pos.y + this.getSelf().getEyeHeight(),
                pos.z,
                20, 0, 0, 0, 0.4);
    }

    public void RagingLightClient() {
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            setCooldown(PowerIndex.SKILL_1_SNEAK,200);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
        }
    }
    public void RagingLightServer() {
        this.setCooldown(PowerIndex.SKILL_1_SNEAK,200);
        int radius = 13;
        AABB box = this.getSelf().getBoundingBox().inflate(radius,2,radius);
        for (Mob M : this.getSelf().level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT,this.getSelf(),box)) {
            M.setTarget(this.getSelf());
            M.setLastHurtByMob(this.getSelf());
        }

        Vec3 pos = this.getSelf().getPosition(1);
        Vector3f[] colors = {
                new Vector3f(0.85F, 0.31F, 0.15F),
                new Vector3f(0.31F, 0.22F, 0.20F )
        };
        //TODO: PARTICLE SPRAY (with colors)
        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.FIREWORK,
                pos.x,
                pos.y + this.getSelf().getEyeHeight(),
                pos.z,
                20, 0, 0, 0, 0.4);
    }


    public void BackflipClient() {
        if (!onCooldown(PowerIndex.GLOBAL_DASH)) {
            tryPower(PowerIndex.SNEAK_MOVEMENT,true);
            tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);

        }
    }


    @Override
    public boolean tryPower(int move, boolean forced) {
        StandUser SU = (StandUser) this.getSelf();
        switch (move) {
            case PowerIndex.POWER_1 ->  {
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_ALLURING_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
            }
            case PowerIndex.POWER_1_SNEAK -> {
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_RAGING_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
            }
            case PowerIndex.SNEAK_MOVEMENT -> {
                canPogo = true;
                this.setActivePower(PowerIndex.SNEAK_MOVEMENT);
                this.setAttackTime(0);
                this.setCooldown(PowerIndex.GLOBAL_DASH,260 + (this.getSelf().onGround() ? 0 : 60));
                this.getSelf().level().playSound(null,this.getSelf().blockPosition(), ModSounds.ANUBIS_BACKFLIP_EVENT, SoundSource.PLAYERS,1.0F,1.0F);
               /// this.getStandUserSelf().roundabout$setStandAnimation(PowerIndex.SNEAK_MOVEMENT);
                if (!isClient()) {
                    Vec3 look = getSelf().getLookAngle().multiply(1,0,1).normalize();
                    SU.roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                    SU.roundabout$setLeapIntentionally(true);

                    float strength = 1.25F;
                    if ( Math.abs(look.x) + Math.abs(look.z) == 0  ) {strength *= 0.7F;}

                    MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),strength,look.x*1,-1,look.z*1);
                }
            }
        }
        return super.tryPower(move, forced);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> {
                AlluringLightServer();
            }
            case PowerIndex.POWER_1_SNEAK -> {
                RagingLightServer();
            }

            case PowerIndex.ATTACK, PowerIndex.SNEAK_ATTACK -> {
                tryBasicAttack((byte)move);
            }
            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                tryPogoAttack();
            }
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void onActuallyHurt(DamageSource $$0, float $$1) {
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE && getAttackTimeDuring() < PogoDelay) {
            this.setPowerNone();
        }
    }

    List<Integer> lasthits = new ArrayList<>();

    float slipstreamTimer = 3;
    boolean canPogo = true;
    @Override
    public void tickPower() {

        if (isClient()) {
         //   Roundabout.LOGGER.info("CA: " + this.getActivePower() + " | " + this.getAttackTime() + " | "+ this.getAttackTimeDuring() + "/" + this.getAttackTimeMax());
        }

        if (this.getSelf().onGround() && !canPogo) {
            canPogo = true;
        }

        StandUser SU = this.getStandUserSelf();

        /** slipstream creation */
        if (!this.isClient()) {
            Level level = this.getSelf().level();
            if (this.getSelf().isSprinting() && SU.roundabout$getActive()) {

                float dif = this.getSelf().walkDist-this.getSelf().walkDistO;
                if (dif != 0) {
                    slipstreamTimer -= dif;
                }

                if (slipstreamTimer <= 0) {
                    slipstreamTimer = 3;
                    AnubisSlipstreamEntity AS = new AnubisSlipstreamEntity(ModEntities.ANUBIS_SLIPSTREAM,level);
                    AS.setPos(this.getSelf().getPosition(1));
                    level.addFreshEntity(AS);
                }
            }
        }

        SU.roundabout$setCombatMode(SU.roundabout$getActive());
        if (isGuarding()) {
            getStandUserSelf().roundabout$setStandAnimation(PowerIndex.GUARD);
        } else if (getStandUserSelf().roundabout$getStandAnimation() == PowerIndex.GUARD) {
            getStandUserSelf().roundabout$setStandAnimation(NONE);
        }

        if(SU.roundabout$getStandAnimation() == PowerIndex.SNEAK_MOVEMENT ) {
            if (this.getAttackTime() > 40) {
                SU.roundabout$setStandAnimation(PowerIndex.NONE);
            }
        }

        if (pogoImmunity > 0) {
            pogoImmunity--;
        }
        if (this.getActivePower() == PowerIndex.SNEAK_MOVEMENT) {
            if (this.getAttackTime() > 10 && this.getAttackTime() < 20) {
                if(this.getSelf().isCrouching()) {
                    this.addMomentum(0,-0.075F,0);
                }
            } else if (this.getAttackTime() > 20) {
                this.setPowerNone();
            }
        }

        super.tickPower();
    }





    @Override
    public boolean interceptAttack(){
        return true;
    }
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (this.canAttack()) {

                byte index = PowerIndex.ATTACK;
                if (this.getSelf().isCrouching()) {
                    if (!this.getSelf().onGround() && canPogo) {
                        canPogo = false;
                        index = PowerIndex.SNEAK_ATTACK_CHARGE;
                    } else {
                        index = PowerIndex.SNEAK_ATTACK;
                    }
                }


            this.tryPower(index);
            tryPowerPacket(index);
        }}
    }

    @Override
    public boolean setPowerAttack() {
        return setPowerOther(PowerIndex.ATTACK,this.getActivePower());
    }

    public void tryBasicAttack(byte move) {

        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown;
            } else {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown;
            }

        }

        lasthits.add(1 + (move == PowerIndex.SNEAK_ATTACK ? -2 : 0) );



        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        if (activePowerPhase == 3) {
            this.setActivePower(determineThird(lasthits));
          //  this.setActivePower(move);
        } else {
            this.setActivePower(move);
        }
    }



    @Override
    public void updateAttack() {
        updateUniqueMoves();
    }

    @Override
    public void updateUniqueMoves() {
        switch (getActivePower()) {
            case PowerIndex.ATTACK,PowerIndex.SNEAK_ATTACK -> {
                updateAttacks();
            }
            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                updatePogoAttack();
            }
        }
        if (isVariant(this.getActivePower())) {
            updateAttacks();
        }
    }

    public void updateAttacks() {
        if (!isClient()) {

            if (this.attackTimeDuring > -1) {
                if (this.attackTimeDuring > this.attackTimeMax) {
                    this.attackTime = -1;
                    this.attackTimeMax = 0;
                    lasthits = new ArrayList<>();
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                } else {

                    switch (this.getActivePower()) {
                        case PowersAnubis.HEAVY_SWING -> {
                            if (this.attackTimeDuring > 4) {
                                heavySwing();
                            } else if (this.attackTimeDuring > 2) {
                                Vec3 look = this.getSelf().getLookAngle();
                                look = new Vec3(look.x,0,look.z).normalize();
                                this.addMomentum(look.scale( this.getSelf().onGround() ? 0.6F : 0.2F ));
                            }
                        }

                    }

                    ///  NORMAL AND SNEAK CLICK
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK ||
                            this.getActivePower() == PowerIndex.ATTACK) {
                        /// the actual attack
                        if (getActivePower() == PowerIndex.SNEAK_ATTACK) {
                            SAttack();
                        } else {
                            NAttack();
                        }

                    }

                    if (this.getSelf() instanceof Player) {
                        if (isPacketPlayer()) {
                            this.setAttackTimeDuring(-10);
                            //   C2SPacketUtil.standPunchPacket(getTargetEntityId(15), this.activePowerPhase);
                        }
                    }


                }
            }

        }
    }


    public void NAttack(){
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }

        Entity targetEntity = getTargetEntity(this.self,-1,15);
        punchImpact(targetEntity);
    }
    public void SAttack(){
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }

        Entity targetEntity = getTargetEntity(this.self,-1,15);
        punchImpact(targetEntity);
    }

    public void tryPogoAttack() {
        this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown;

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.setAttackTime(0);
    }

    public int pogoImmunity = 0;
    public void updatePogoAttack() {
        this.getSelf().setNoGravity(this.attackTimeDuring < PogoDelay);
        if (this.attackTimeDuring > -1) {

            if (this.getSelf().onGround()) {
                this.setPowerNone();
                this.attackTime += 5;
            }

            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
            } else {
                if (!isClient()) {
                    /**  Pogo is broken up into 4 stages: Hover, Launch, Attack, and Aftershock */
                    int windup = PogoDelay;
                    if (attackTimeDuring == windup) {
                        PogoLaunch();

                    } else if (attackTimeDuring < windup) {
                        MainUtil.slowTarget(this.getSelf(), 0.8F);

                    } else if (attackTimeDuring < windup + 6) {
                        Vec3 pos = this.getSelf().getEyePosition(0F).add(this.getSelf().getLookAngle().scale(1));
                        List<Entity> targets = MainUtil.genHitbox(this.getSelf().level(),
                                pos.x,pos.y,pos.z,
                                1.5,1.5,1.5);
                        targets.removeIf(entity -> entity.equals(this.getSelf()));
                        targets.removeIf(entity -> !entity.isAttackable());
                        Entity target = null;
                        if (!targets.isEmpty()) {target = targets.get(0);}

                        Options o = Minecraft.getInstance().options;
                        if (target != null) {
                            double strength = this.getSelf().isCrouching() ? 0.5 : 0.9;
                            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),strength,0,-1,0);
                            this.getStandUserSelf().roundabout$setLeapTicks(20);
                            this.getStandUserSelf().roundabout$setLeapIntentionally(true);
                            if (StandDamageEntityAttack(target,4,1,this.getSelf())) {
                                if (target instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers().interceptGuard()
                                        && LE.isBlocking() && !((StandUser) LE).roundabout$isGuarding()){
                                    knockShield2(target, 60);
                                } else {
                                    knockShield2(target, 40);
                                }
                            }
                            this.setPowerNone();
                            if (this.getSelf() instanceof Player P) {
                                S2CPacketUtil.sendIntPowerDataPacket(P, PowerIndex.SNEAK_ATTACK_CHARGE, attackTime + 5);
                            }
                            pogoImmunity = 10;

                        }

                    } else if (attackTimeDuring < windup + 9) { /// Slows the user after a duration
                        MainUtil.slowTarget(this.getSelf(),0.7F);
                        this.getSelf().resetFallDistance();
                    }
                }

            }
        }

    }



    public void PogoLaunch(){
        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }
        float power = 1.5F;
        Vec3 lookAngle = this.getSelf().getLookAngle().reverse();
        this.getSelf().resetFallDistance();
        if (lookAngle.y < -0.15) {
            power *= 0.5F;
        }
        this.pogoImmunity = 5;
        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(),power,lookAngle.x,lookAngle.y,lookAngle.z);
    }

    @Override
    public void punchImpact(Entity entity) {
        this.setAttackTimeDuring(-10);
        float knockbackStrength = 0.2F;
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {knockbackStrength = 0.4F;}
        if (this.getSelf().isSprinting()) {knockbackStrength += 0.05F;}

        List<Entity> entities = getBasicSwordHitBox(this.getActivePower() == PowerIndex.SNEAK_ATTACK);
        for (Entity e : entities ) {
            if (e != null) {
                float pow = getPunchStrength(e) * (this.getActivePower() == PowerIndex.SNEAK_ATTACK ? 1.3F : 1F);
                if (StandDamageEntityAttack(e, pow, 0, this.self)) {
                    if (e instanceof LivingEntity LE) {
                        addEXP(1);
                    }
                    this.takeDeterminedKnockback(this.getSelf(), e, knockbackStrength);
                }

            }
        }
        if (!entities.isEmpty()) {
            if (!isClient()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
            }
        }

    }

    /// ---------------------------------
    /// VARIANTS
    /// ---------------------------------

    public static boolean isVariant(byte b) {
        return b >= PowersAnubis.HEAVY_SWING && b <= PowersAnubis.FLURRY_ALT;
    }
    public static final byte
            HEAVY_SWING = 44,
            THRUST = 45,
            SECOND_SWING = 46,
            UPPERCUT = 47,
            QUICK_SLASH = 48,
            QUICK_SLASH_ALT = 49,
            OVERHEAD = 50,
            OVERHEAD_ALT = 51,
            RADIAL = 52,
            RADIAL_ALT = 53,
            FLURRY = 54,
            FLURRY_ALT = 55;



    public byte determineThird(List<Integer> list) {
        lasthits = new ArrayList<>();
        String id = list.get(0)+""+list.get(1)+list.get(2);
        if (true) {return HEAVY_SWING;}
        return switch (id) {
            case "111" -> HEAVY_SWING;
            case "-1-11" -> THRUST;
            case "-111" -> SECOND_SWING;
            case "1-11" -> UPPERCUT;
            case "-1-1-1" -> QUICK_SLASH;
            case "11-1" -> OVERHEAD;
            case "-11-1" -> RADIAL;
            case "1-1-1" -> FLURRY;
            default -> HEAVY_SWING;
        };
    }


    public void heavySwing() {

        if (this.getSelf() instanceof Player P) {
            S2CPacketUtil.sendIntPowerDataPacket(P,PowersAnubis.SWING,0);
        }

        this.setAttackTimeDuring(-10);
        float knockbackStrength = 0.5F;


        List<Entity> entities =  defaultSwordHitbox(this.getSelf(),1.6, 3.2);
        for (Entity e : entities ) {
            if (e != null) {
                float pow = getHeavyPunchStrength(e);
                if (StandDamageEntityAttack(e, pow, 0, this.self)) {
                    if (e instanceof LivingEntity LE) {
                        addEXP(1);
                    }
                    this.takeDeterminedKnockback(this.getSelf(), e, knockbackStrength);
                } else {
                    if (e instanceof LivingEntity LE) {
                        if (LE.isBlocking()) {
                            MainUtil.knockShieldPlusStand(e,40);
                        }
                    }
                }

            }
        }
        if (!entities.isEmpty()) {
            if (!isClient()) {
                Entity e = entities.get(0);
                Vec3 pos = e.getPosition(0F).add(0,e.getEyeHeight()/2,0);
                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK, pos.x, pos.y, pos.z, 0, 0, 0.0, 0, 0.0);
                if (this.getSelf() instanceof Player P) {
                    P.crit(e);
                }
            }

        }
    }

    @Override
    public boolean setPowerBarrageCharge() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        playBarrageChargeSound();
        return true;
    }

    @Override
    public void setPowerBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        playBarrageCrySound();
    }

    public int getBarrageLength(){
        return 40;
    }



    @Override
    public void updatePowerInt(byte activePower, int data) {
        switch (activePower) {
            case PowersAnubis.SWING -> {
                this.getSelf().swing(InteractionHand.MAIN_HAND);

            }
            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                this.attackTime = data;
                this.setPowerNone();
            }
        }
    }

    public void updateBarrage(){
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getBarrageLength())
                                    * (getBarrageRecoilTime() - 1)));

                    BarrageSpin();
                }
            }
        }
    }

    public void BarrageSpin() {



        boolean lastHit = (attackTimeDuring >= this.getBarrageLength());

        Vec3 pos = this.getSelf().getPosition(1F);
        float radius = 2.4F;
        List<Entity> entities = MainUtil.genHitbox(this.getSelf().level(),pos.x,pos.y,pos.z,radius,radius,radius);
        entities.remove(this.getSelf());
        entities.removeIf(entity -> !entity.isAttackable());
        entities.removeIf(entity -> entity.distanceTo(this.getSelf()) > radius);

        for (Entity entity : entities) {
            DamageSource source = ModDamageTypes.of(this.getSelf().level(),ModDamageTypes.ANUBIS_SPIN,this.getSelf());
            if (entity instanceof LivingEntity LE) {
                ((StandUser) LE).roundabout$setDazed((byte)3);
                float power = LE.getHealth() >  0.4F  ? 0.4F : 0.01F;
                if (LE.hurt(source, 0)) {
                    if (lastHit) {
                        float knockback = 0.8F;
                        Vec3 vec3 = (entity.getPosition(1F).subtract(pos)).normalize().scale(knockback);
                        LE.hurt(source, 4.0F);
                        entity.hasImpulse = true;
                        entity.setDeltaMovement(vec3.x,vec3.y+0.2,vec3.z);
                        entity.hurtMarked = true;
                    } else {
                        LE.hurt(source,power);
                        this.takeKnockbackUp(entity, 0.015);
                    }
                }
            }
        }

        if (!this.getSelf().onGround()) {
            this.addMomentum(0,0.06F,0);
            if (!entities.isEmpty()) {
                this.getSelf().setDeltaMovement(0,0.02,0);
            }
        }



    }

    @Override
    public boolean interceptGuard(){
        return true;
    }

    public boolean canGuard(){
        return !this.isBarraging() && !this.isClashing() && !(this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE);
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && canGuard()) {
            tryPower(PowerIndex.GUARD,true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }

    @Override
    public boolean setPowerNone() {
        /// WARNING: THIS WILL 1,000,000% CONFLICT AT SOME POINT.
        this.getSelf().setNoGravity(false);
        return super.setPowerNone();
    }

    public Component getPosName(byte posID){
        return switch (posID) {
            case (byte) 1 -> Component.translatable("idle.roundabout.anubis_2");
            default -> Component.translatable("idle.roundabout.anubis_1");
        };
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte)0);
        $$1.add((byte)1);
        return $$1;
    }

    public static final byte
            ANIME = 1,
            EVIL = 2;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME,
                EVIL
        );
    }

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case PowersAnubis.EVIL -> Component.translatable("skins.roundabout.anubis.evil");
            default -> Component.translatable("skins.roundabout.anubis.anime");
        };
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public byte worthinessType(){
        return HUMANOID_WORTHY;
    }

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
        return Component.literal(  "Prisma").withStyle(ChatFormatting.YELLOW);
    }

    public List<Entity> getBasicSwordHitBox(boolean crouching) {
        List<Entity> entities = defaultSwordHitbox(this.getSelf(),1.4, 3.5);
        if (crouching) {
            entities = defaultSwordHitbox(this.getSelf(),2.2,2.5);
        }
        return entities;
    }

    public List<Entity> defaultSwordHitbox(Entity e,double width, double forwards) {
        Vec3 pos = e.getEyePosition(0F).add(e.getLookAngle().scale(0.5));
        double yrot = Math.toRadians(this.getSelf().getViewYRot(0F));

        Vec3 forward = new Vec3(Math.cos(yrot+Math.PI/2),0,Math.sin(yrot+Math.PI/2));
        Vec3 left = new Vec3(forward.z,0,forward.x);


        Vec3 offset = Vec3.ZERO;
        offset = offset.add(left.scale(width));
        offset = offset.add(forward.scale(forwards));

        List<Entity> list = MainUtil.genHitbox(e.level(), pos.x, pos.y, pos.z,1+Math.abs(offset.x),2.3,1+Math.abs(offset.z));

        Entity closeRanged = this.getTargetEntity(this.getSelf(),1,15);

        list.removeIf(entity -> !entity.isAlive() || !entity.isAttackable());
        list.removeIf(entity -> entity.getPosition(1F).distanceTo(e.getPosition(1F)) > Math.max(width,forwards) );
        list.remove(e);
        return list;
    }

    public void addMomentum(float x, float y, float z) {
        addMomentum(new Vec3(x,y,z));
    }
    public void addMomentum(Vec3 v) {
        this.getSelf().hasImpulse = true;
        this.getSelf().hurtMarked = true;
        this.getSelf().setDeltaMovement(this.getSelf().getDeltaMovement().add(v));
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = standUser.roundabout$getAttackTimeDuring();
        if (standOn && standUser.roundabout$isClashing()) {
            int ClashTime = 15 - Math.round((attackTimeDuring / 60) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else {
            int barTexture = 0;

            List<Entity> TE = getBasicSwordHitBox(this.getSelf().isCrouching());
            float attackTimeMax = standUser.roundabout$getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = standUser.roundabout$getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {


                    if (standUser.roundabout$getActivePowerPhase() == standUser.roundabout$getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else {
                        if (!TE.isEmpty()) {
                            barTexture = 12;
                        } else {
                            barTexture = 18;
                        }
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (standOn) {
                if (!TE.isEmpty()) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }


}