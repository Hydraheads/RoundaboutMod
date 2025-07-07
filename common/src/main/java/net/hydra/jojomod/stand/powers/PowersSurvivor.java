package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersSurvivor extends NewDashPreset {
    public PowersSurvivor(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersSurvivor(entity);
    }


    public boolean dangerYappingOn(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
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
        // code for advanced icons
         setSkillIcon(context, x, y, 1, StandIcons.BOTTLE, PowerIndex.SKILL_1);

        if (isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.DESPAWN, PowerIndex.NO_CD);
        else
            setSkillIcon(context, x, y, 2, StandIcons.SPAWN, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        setSkillIcon(context, x, y, 4, StandIcons.RAGE_SELECTION, PowerIndex.SKILL_4);

        super.renderIcons(context, x, y);
    }


    public List<SurvivorEntity> survivorsSpawned = new ArrayList<>();

    public void listInit(){
        if (survivorsSpawned == null) {
            survivorsSpawned = new ArrayList<>();
        }
    }

    @Override

    public void tickPowerEnd() {
        if (survivorsSpawned != null && !survivorsSpawned.isEmpty()) {
            offloadSurvivors();
        }
    }
    public void addSurvivorToList(SurvivorEntity che){
        listInit();
        survivorsSpawned.add(che);
        List<SurvivorEntity> survivorsList2 = new ArrayList<>(survivorsSpawned) {
        };
        int scount = ClientNetworking.getAppropriateConfig().survivorSettings.maxSurvivorsCount;
        if (!survivorsList2.isEmpty() && survivorsList2.size() > scount) {
            survivorsList2.get(0).forceDespawn(true);
            survivorsSpawned.remove(0);
        }
    }

    public void offloadSurvivors(){
        listInit();
        List<SurvivorEntity> survivorsList2 = new ArrayList<>(survivorsSpawned) {
        };
        if (!survivorsList2.isEmpty()) {
            for (SurvivorEntity value : survivorsList2) {
                if (value.isRemoved() || !value.isAlive() || (this.self.level().isClientSide() && this.self.level().getEntity(value.getId()) == null)) {
                    survivorsSpawned.remove(value);
                }
            }
        }
    }
    public boolean removeAllSurvivors(){
        listInit();

        boolean success = false;
        List<SurvivorEntity> survivorsList2 = new ArrayList<>(survivorsSpawned) {
        };
        if (!survivorsList2.isEmpty()) {
            for (SurvivorEntity value : survivorsList2) {
                    value.forceDespawn(true);
                    success = true;
                    survivorsSpawned.remove(value);
            }
        }

        if (success)
            playStandUserOnlySoundsIfNearby(RETRACT, 100, false, false);

        return true;
    }

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_2_NORMAL-> {
                summonSurvivorClient();
            }
            case SKILL_2_CROUCH-> {
                despawnSurvivorClient();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dash();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_2_SNEAK -> {
                return removeAllSurvivors();
            }
        }
        return super.setPowerOther(move,lastMove);
    }
    public boolean canUseStillStandingRecharge(byte bt){
        if (bt == PowerIndex.SKILL_2)
            return false;
        return super.canUseStillStandingRecharge(bt);
    }

    public void summonSurvivorClient(){
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            Vec3 pos = MainUtil.getRaytracePointOnMobOrBlockIfNotUp(this.self, 30,0.3f);
            if (pos != null) {
                tryPosPower(PowerIndex.POWER_2, true, pos);
                tryPosPowerPacket(PowerIndex.POWER_2, pos);
            }
        }
    }
    public void despawnSurvivorClient(){
        tryPowerPacket(PowerIndex.POWER_2_SNEAK);
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        if (move == PowerIndex.POWER_2) {
            createSurvivor(move, pos);
            return true;
        }
        return tryPower(move, forced);
    }

    public void createSurvivor(int move, Vec3 pos){

        if (isClient() || (!this.onCooldown(PowerIndex.SKILL_2) || !ClientNetworking.getAppropriateConfig().survivorSettings.SummonSurvivorCooldownCooldownUsesServerLatency)) {
            int cooldown = ClientNetworking.getAppropriateConfig().survivorSettings.SummonSurvivorCooldownV2;
            this.setCooldown(PowerIndex.SKILL_2, cooldown);
            if (!isClient()) {
                blipStand(pos);

            }
        }
    }


    public void blipStand(Vec3 pos){
        StandEntity stand = ModEntities.SURVIVOR.create(this.getSelf().level());
        if (stand instanceof SurvivorEntity SE) {
            StandUser user = getStandUserSelf();
            stand.absMoveTo(pos.x(), pos.y(), pos.z());
            stand.setSkin(user.roundabout$getStandSkin());
            stand.setIdleAnimation(user.roundabout$getIdlePos());
            stand.setMaster(this.self);
            addSurvivorToList(SE);
            SE.setRandomSize((float) (Math.random()*0.4F));
            SE.setYRot(this.self.getYHeadRot() % 360);
            this.self.level().addFreshEntity(stand);
            playStandUserOnlySoundsIfNearby(PLACE, 100, false, false);
        }

    }

    @Override
    public boolean isServerControlledCooldown(CooldownInstance ci, byte num){
        if (num == PowerIndex.SKILL_2 && ClientNetworking.getAppropriateConfig().survivorSettings.SummonSurvivorCooldownCooldownUsesServerLatency) {
            return true;
        }
        return super.isServerControlledCooldown(ci, num);
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP, slot);
    }

    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;

    @Override
    public void tickPower() {
        /**Yap animation based on using power*/
        super.tickPower();
    }


    @Override
    public void updateIntMove(int in) {

        super.updateIntMove(in);
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }

    public static final byte
            BASE = 1;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                BASE
        );
    }

    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            default -> Component.translatable("skins.roundabout.survivor.base");
        };
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SURVIVOR_SUMMON_EVENT;
            }
            case PLACE -> {
                return ModSounds.SURVIVOR_PLACE_EVENT;
            }
            case RETRACT -> {
                return ModSounds.SURVIVOR_REMOVE_EVENT;
            }
            case SHOCK -> {
                return ModSounds.SURVIVOR_SHOCK_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    public static final byte
            PLACE = 61,
            RETRACT = 62,
            SHOCK = 63;
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        return $$1;
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
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }
}