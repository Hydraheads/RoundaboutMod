package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.projectile.EmperorBulletEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetBubbleEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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


    public boolean holdDownClick = false;
    public boolean consumeClickInput = false;
    public float speedMultiplier = 1.0f;

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


    @Override
    public void tickPower() {
        super.tickPower();

        if (shootTicks > 0) {
            shootTicks -= getLowerTicks();

            shootTicks = Math.max(0, shootTicks);
        }
    }



    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1) {
            tripleShot = !tripleShot;
            System.out.println("TOGGLED SERVER: " + tripleShot);
            return true;
        } else if (move == PowerIndex.POWER_2) {
            return this.controlModeInactive();
        } else if (move == PowerIndex.POWER_2_SNEAK) {
            return this.autoModeActive();
        } else if (move == PowerIndex.POWER_4_EXTRA) {
            return this.shootEmperorBullet();
        }
        return super.setPowerOther(move,lastMove);
    }

    public static final byte
            SHOOT = PowerIndex.POWER_4_EXTRA;


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        PowersEmperor PA = new PowersEmperor(entity);
        ((StandUser)entity).roundabout$setStandSkin((byte) 1);
        return PA;}

    private boolean autoMode;

    public boolean autoModeActive(){
        return autoMode;
    }

    private boolean tripleShot;

    private void tripleShotToggle() {
        if (getSelf() instanceof StandUser su) {
            su.roundabout$setUniqueStandModeToggle(!su.roundabout$getUniqueStandModeToggle());
        }
    }

    private boolean controlMode;

    public boolean controlModeInactive(){
        return controlMode;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (tripleShotActive()) {
            setSkillIcon(context, x, y, 1, StandIcons.EMPEROR_TRIPLE, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.EMPEROR_SINGLE, PowerIndex.SKILL_1);
        }

        if (isHoldingSneak()) {
            if (autoModeActive()) {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_AUTO_MODE_ON, PowerIndex.SKILL_2_SNEAK);
            } else if (!autoModeActive()) {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_AUTO_MODE_OFF, PowerIndex.SKILL_2_SNEAK);
            }
        } else {
            if (controlModeInactive()) {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_CONTROL_MODE_OFF, PowerIndex.SKILL_2);
            } else if (!controlModeInactive()) {
                setSkillIcon(context, x, y, 2, StandIcons.EMPEROR_CONTROL_MODE_ON, PowerIndex.SKILL_2);
            }
        }

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 4, StandIcons.EMPEROR_SPEED_DOWN, PowerIndex.SKILL_4_SNEAK);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.EMPEROR_SPEED_UP, PowerIndex.SKILL_4);
        }
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
                controlModeToggle();
            }
            case SKILL_2_CROUCH -> {
                autoModeToggle();
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
        if (!self.level().isClientSide) {
        }
    }

    private void bulletSpeedUp() {
        speedMultiplier = Mth.clamp(speedMultiplier + 0.15f, 0.2f, 3.0f);
    }

    private void bulletSpeedDown() {
        speedMultiplier = Mth.clamp(speedMultiplier - 0.15f, 0.2f, 3.0f);
    }

    private void autoModeToggle() {
        autoMode = !autoMode;
        if (!self.level().isClientSide) {
        }
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

    public float getBulletStrength(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return levelupDamageMod(multiplyPowerByStandConfigShooting(multiplyPowerByStandConfigPlayers(1.35F)));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigShooting(multiplyPowerByStandConfigMobs(3F)));
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


    public boolean confirmShot(int useTicks){
        if (canShootBullet(useTicks)){
            //int pauseGrowthTicks = pauseTicks();
            setShootTicks((shootTicks+useTicks));
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

    public int shootTicks = 1000;
    public int getShootTicks(){return shootTicks;}
    public void setShootTicks(int shootTicks){this.shootTicks = Mth.clamp(shootTicks,0,getMaxShootTicks());}
    public int getMaxShootTicks(){return 1000;}
    public int getLowerTicks(){return ClientNetworking.getAppropriateConfig().emperorSettings.heatTickDownRate;}

    public float getBulletSpeed(){
        return (float) (0.8F*(ClientNetworking.getAppropriateConfig().
                emperorSettings.bulletShootSpeedMultiplier*0.01));
    }

    public boolean shootEmperorBullet() {

        if (self.level().isClientSide) {
            return true;
        }

        this.setCooldown(PowerIndex.SKILL_4, 3);

        this.poseStand(OffsetIndex.FOLLOW);
        this.setAttackTimeDuring(-10);
        this.setActivePower(PowerIndex.POWER_4_EXTRA);

        float speed = getBulletSpeed() * speedMultiplier;

        this.bulletListInit();

        if (tripleShotActive()) {

            for (int i = -1; i <= 1; i++) {

                EmperorBulletEntity bullet = getEmperorBullet();

                float spread = i * 6.0f;

                bullet.shootFromRotation(
                        (Player) this.self,
                        this.self.getXRot(),
                        this.self.getYRot() + spread,
                        0.0F,
                        speed,
                        0.0F
                );

                bullet.setNoGravity(true);
                bullet.setBaseDamage(getEmperorBulletStrength(this.self) * speedMultiplier);

                this.bulletList.add(bullet);
                this.getSelf().level().addFreshEntity(bullet);
            }

        } else {

            EmperorBulletEntity bullet = getEmperorBullet();

            bullet.shootFromRotation(
                    (Player) this.self,
                    this.self.getXRot(),
                    this.self.getYRot(),
                    0.0F,
                    speed,
                    0.0F
            );

            bullet.setNoGravity(true);
            bullet.setBaseDamage(getEmperorBulletStrength(this.self) * speedMultiplier);

            this.bulletList.add(bullet);
            this.getSelf().level().addFreshEntity(bullet);
        }

        this.self.level().playSound(
                null,
                this.self.blockPosition(),
                ModSounds.EXPLOSIVE_BUBBLE_SHOT_EVENT,
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