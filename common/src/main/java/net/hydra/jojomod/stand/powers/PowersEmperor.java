package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.projectile.EmperorBulletEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersEmperor extends NewDashPreset {
    public PowersEmperor(LivingEntity self) {
        super(self);
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().emperorSettings.enableEmperor;
    }

    private boolean controlMode;
    private boolean autoMode;

    public boolean isControlMode() {
        return controlMode;
    }

    public boolean holdDownClick = false;
    public boolean consumeClickInput = false;

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {

        if (!consumeClickInput) {
            if (keyIsDown) {
                if (!holdDownClick) {
                    holdDownClick = true;
                    if (inShootingMode()) {
                        if (confirmShot(getUseTicks())) {

                            if (this.self instanceof Player PE) {
                                IPlayerEntity ipe = ((IPlayerEntity) PE);
                                ipe.roundabout$getBubbleShotAim().stop();
                                ipe.roundabout$setBubbleShotAimPoints(10);
                            }

                            this.tryPower(PowerIndex.POWER_4_EXTRA, true);
                            tryPowerPacket(PowerIndex.POWER_4_EXTRA);
                        }
                    }
                }
            } else {
                holdDownClick = false; // reset on release
            }


        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    private LivingEntity findTarget(Vec3 bulletPos) {

        double range = 10;

        List<LivingEntity> entities = self.level().getEntitiesOfClass(
                LivingEntity.class,
                new net.minecraft.world.phys.AABB(
                        bulletPos.x - range, bulletPos.y - range, bulletPos.z - range,
                        bulletPos.x + range, bulletPos.y + range, bulletPos.z + range
                ),
                e -> e != self && e.isAlive()
        );

        LivingEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity entity : entities) {

            double dist = entity.distanceToSqr(bulletPos);

            if (dist < closestDist) {
                closestDist = dist;
                closest = entity;
            }
        }

        return closest;
    }
    @Override
    public void tickPower() {
        super.tickPower();

        if (this.self instanceof Player PE && PE.isCreative()) {
            setShootTicks(0);
        } else {
            if (getShootTicks() > 0) {
                setShootTicks(getShootTicks() - getLowerTicks());
            }
        }

        if (self.level().isClientSide) {

            Minecraft mc = Minecraft.getInstance();

            holdingRightClick =
                    mc.options.keyUse.isDown() &&
                            ((StandUser) self).roundabout$getActive() &&
                            ((StandUser) self).roundabout$getStandPowers() instanceof PowersEmperor;

            if (holdingRightClick) {
                self.setDeltaMovement(
                        self.getDeltaMovement().multiply(0.4D, 1.0D, 0.65D)
                );
            }
        }

        if (controlMode) {
            bulletList.removeIf(b -> b == null || !b.isAlive());

            for (EmperorBulletEntity bullet : bulletList) {
                if (bullet == null || !bullet.isAlive()) continue;

                Vec3 target = self.getEyePosition().add(self.getLookAngle().scale(60));
                Vec3 toTarget = target.subtract(bullet.position()).normalize();

                double speed = bullet.getDeltaMovement().length();
                Vec3 newMotion = toTarget.scale(speed);

                bullet.setDeltaMovement(newMotion);
            }
        }

        if (autoMode) {

            bulletList.removeIf(b -> b == null || !b.isAlive());

            for (EmperorBulletEntity bullet : bulletList) {

                LivingEntity target = findTarget(bullet.position());

                if (target == null) continue;

                Vec3 toTarget = target.getEyePosition()
                        .subtract(bullet.position())
                        .normalize();

                double speed = Math.max(0.5, bullet.getDeltaMovement().length());
                Vec3 newMotion = toTarget.scale(speed);

                bullet.setDeltaMovement(newMotion);
                bullet.hasImpulse = true;
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1) {
            tripleShot = !tripleShot;
            return true;
        } else if (move == PowerIndex.POWER_2) {
            controlModeToggle();
            return true;
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            autoModeToggle();
            return true;
        } else if (move == PowerIndex.POWER_4_EXTRA) {
            return this.shootEmperorBullet();
        }
        else if (move == PowerIndex.POWER_4) {
            bulletSpeedUp();
            return true;
        }
        else if (move == PowerIndex.POWER_4_SNEAK) {
            bulletSpeedDown();
            return true;
        }
        return super.setPowerOther(move,lastMove);
    }

    public static final byte
            SHOOT = PowerIndex.POWER_4_EXTRA;

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        PowersEmperor PA = new PowersEmperor(entity);

        PA.controlMode = true;
        PA.autoMode = false;

        ((StandUser)entity).roundabout$setStandSkin((byte) 1);
        return PA;
    }

    private static final int POWER_INT_AUTO = 0;
    private static final int POWER_INT_CONTROL = 1;
    private static final int POWER_INT_SPEED = 2;

    private int speedLevel = 2;

    public int getSpeedLevel() {
        return speedLevel;
    }

    private boolean tripleShot;

    private void tripleShotToggle() {
        if (getSelf() instanceof StandUser su) {
            su.roundabout$setUniqueStandModeToggle(!su.roundabout$getUniqueStandModeToggle());
        }
    }

    public void updatePowerInt(int id, int value) {
        super.updatePowerInt((byte) id, value);

        switch (id) {

            case POWER_INT_AUTO -> {
                autoMode = value == 1;
            }

            case POWER_INT_CONTROL -> {
                controlMode = value == 1;
            }

            case POWER_INT_SPEED -> {
                speedLevel = value;
            }
        }
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (tripleShotActive()) {
            setSkillIcon(context, x, y, 1, StandIcons.EMPEROR_TRIPLE, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.EMPEROR_SINGLE, PowerIndex.SKILL_1);
        }

        if (isHoldingSneak()) {
            if (autoMode) {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_AUTO_MODE_ON, PowerIndex.SKILL_2_SNEAK);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_AUTO_MODE_OFF, PowerIndex.SKILL_2_SNEAK);
            }
        } else {
            if (isControlMode()) {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_CONTROL_MODE_ON, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_CONTROL_MODE_OFF, PowerIndex.SKILL_2);
            }
        }

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 4, StandIcons.EMPEROR_SPEED_DOWN, PowerIndex.SKILL_4_SNEAK);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.EMPEROR_SPEED_UP, PowerIndex.SKILL_4);
        }
    }

    private boolean holdingRightClick;
    
    public boolean emperorZoomActive() {
        return holdingRightClick;
    }

    public boolean isWip() {
        return true;
    }

    @Override

    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("BzBoy").withStyle(ChatFormatting.YELLOW);
    }

    public boolean canSummonStandAsEntity() {return false;}

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move) {
            case PowersEmperor.SHOOT -> {
                if (!canShootBullet(getUseTicks())) {
                    return false;
                }
            }
        }

        return super.tryPower(move, forced);
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {

            case SKILL_1_NORMAL, SKILL_1_CROUCH -> {
                this.tryPower(PowerIndex.POWER_1, true);
                tryPowerPacket(PowerIndex.POWER_1);
            }

            case SKILL_2_NORMAL -> {
                this.tryPower(PowerIndex.POWER_2, true);
                tryPowerPacket(PowerIndex.POWER_2);
            }

            case SKILL_2_CROUCH -> {
                this.tryPower(PowerIndex.POWER_2_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_2_SNEAK);
            }

            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }


            case SKILL_4_NORMAL -> {
                bulletSpeedUp();
            }
            case SKILL_4_CROUCH -> {
                bulletSpeedDown();
            }
        }
    }

    private void controlModeToggle() {
        controlMode = !controlMode;
    }

    private void bulletSpeedUp() {
        speedLevel = Mth.clamp(speedLevel + 1, 0, 5);
        updatePowerInt(POWER_INT_SPEED, speedLevel);
        tryPowerPacket(PowerIndex.POWER_4);
    }

    private void bulletSpeedDown() {
        speedLevel = Mth.clamp(speedLevel - 1, 0, 5);
        updatePowerInt(POWER_INT_SPEED, speedLevel);
        tryPowerPacket(PowerIndex.POWER_4_SNEAK);
    }

    private void autoModeToggle() {
        autoMode = !autoMode;
    }

    public boolean tripleShotActive() {
        return tripleShot;
    }

    @Override
    public boolean rendersPlayer() {return true;}

    @Override
    public boolean hasPassiveCombatMode() {return true;}

    @Override
    public boolean hasShootingModeVisually(HumanoidArm arm){
        return arm == this.getSelf().getMainArm();
    }

    private boolean inShootingMode() {
        if (hasPassiveCombatMode()) return true;

        else {return false;
        }
    }

    public byte worthinessType(){return HUMANOID_WORTHY;}

    public List<EmperorBulletEntity> bulletList = new ArrayList<>();

    public void bulletListInit(){
        if (bulletList == null) {
            bulletList = new ArrayList<>();
        }
    }

    public float multiplyPowerByStandConfigShooting(float power) {
        return (float) (power * (ClientNetworking.getAppropriateConfig().
                emperorSettings.emperorShootingModePower * 0.01));
    }

    public int getUseTicks() {return ClientNetworking.getAppropriateConfig().emperorSettings.heatGainedPerShot;}

    public EmperorBulletEntity getEmperorBullet(){
        EmperorBulletEntity bullet = new EmperorBulletEntity(this.self.level(), this.self);
        bullet.setOwner(this.self);
        return bullet;
    }

    public boolean canShootBullet(int useTicks){
        if ((shootTicks+useTicks) <= getMaxShootTicks()){
            return true;
        }
        return false;
    }

    public boolean canShoot() {
        return canShootBullet(getUseTicks());
    }

    public boolean confirmShot(int useTicks){
        if (canShootBullet(getUseTicks())){
            setShootTicks((shootTicks+getUseTicks()));
            return true;
        }
        return false;
    }
    public float getEmperorBulletStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigShooting(multiplyPowerByStandConfigPlayers(1.35F)));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigShooting(multiplyPowerByStandConfigMobs(3F)));
        }
    }
    public int shootTicks = 0;
    public int getShootTicks(){return shootTicks;}
    public void setShootTicks(int shootTicks){this.shootTicks = Mth.clamp(shootTicks,0,getMaxShootTicks());}
    public int getMaxShootTicks(){return 5000;}
    public int getLowerTicks(){return ClientNetworking.getAppropriateConfig().emperorSettings.heatTickDownRate;}

    private float getSpeedMultiplier() {
        return switch (speedLevel) {

            case 0 -> 0.1F;
            case 1 -> 0.5F;
            case 2 -> 1.0F;
            case 3 -> 1.5F;
            case 4 -> 2.2F;
            case 5 -> 3.0F;

            default -> 1.0F;
        };
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.EMPEROR_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    public float getBulletSpeed() {
        return 0.8F;
    }

    public boolean shootEmperorBullet() {

        if (self.level().isClientSide) {
            return true;
        }

        this.setCooldown(PowerIndex.SKILL_4, 3);

        this.poseStand(OffsetIndex.FOLLOW);
        this.setAttackTimeDuring(-10);
        this.setActivePower(PowerIndex.POWER_4_EXTRA);

        float speed = getBulletSpeed() * getSpeedMultiplier();

        this.bulletListInit();

        if (tripleShotActive()) {

            for (int i = -1; i <= 1; i++) {

                EmperorBulletEntity bullet = getEmperorBullet();

                float spread = i * 20.0f;

                bullet.shootFromRotation(
                        (Player) this.self,
                        this.self.getXRot(),
                        this.self.getYRot() + spread,
                        0.0F,
                        speed,
                        0.0F
                );

                bullet.setNoGravity(true);
                bullet.setBaseDamage(getEmperorBulletStrength(this.self) * getSpeedMultiplier());

                this.bulletList.add(bullet);
                bullet.setOwner(this.self);

                this.getSelf().level().addFreshEntity(bullet);
            }

        } else {

            EmperorBulletEntity bullet = getEmperorBullet();

            bullet.setOwner(this.self);

            bullet.shootFromRotation(
                    (Player) this.self,
                    this.self.getXRot(),
                    this.self.getYRot(),
                    0.0F,
                    speed,
                    0.0F
            );

            bullet.setNoGravity(true);
            bullet.setBaseDamage(getEmperorBulletStrength(this.self) * getSpeedMultiplier());
            this.bulletList.add(bullet);
            this.getSelf().level().addFreshEntity(bullet);
        }

        this.self.level().playSound(
                null,
                this.self.blockPosition(),
                ModSounds.EMPEROR_SHOOT_EVENT,
                SoundSource.PLAYERS,
                2F,
                (float) (0.98 + (Math.random() * 0.04))
        );

        return true;
    }

    public static final byte
            ANIME = 0,
            MANGA = 1;


    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME,
                MANGA

        );
    }

    @Override
    public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case PowersEmperor.MANGA -> Component.translatable("skins.roundabout.emperor.manga");

            default -> Component.translatable("skins.roundabout.emperor.anime");
        };
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,1, "ability.roundabout.emperor_shoot",
                "instruction.roundabout.press_attack", StandIcons.EMPEROR_SHOOT,0,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,1, "ability.roundabout.emperor_heat",
                "instruction.roundabout.passive", StandIcons.EMPEROR_HEAT,0,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,2, "ability.roundabout.emperor_squint",
                "instruction.roundabout.hold_block", StandIcons.EMPEROR_SQUINT,0,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,4, "ability.roundabout.emperor_single",
                "instruction.roundabout.press_skill", StandIcons.EMPEROR_SINGLE,1,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99, 4, "ability.roundabout.emperor_triple",
                "instruction.roundabout.press_skill_crouch", StandIcons.EMPEROR_TRIPLE,1,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,1, "ability.roundabout.emperor_control_mode_on",
                "instruction.roundabout.press_skill", StandIcons.EMPEROR_CONTROL_MODE_ON,2,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80,5, "ability.roundabout.emperor_auto_mode_on",
                "instruction.roundabout.press_skill_crouch", StandIcons.EMPEROR_AUTO_MODE_ON,2,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99,1, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,3, "ability.roundabout.emperor_speed_up",
                "instruction.roundabout.press_skill", StandIcons.EMPEROR_SPEED_UP,4,level,bypass));

        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,2, "ability.roundabout.emperor_speed_down",
                "instruction.roundabout.press_skill_crouch", StandIcons.EMPEROR_SPEED_DOWN,4,level,bypass));
        return $$1;
    }
}