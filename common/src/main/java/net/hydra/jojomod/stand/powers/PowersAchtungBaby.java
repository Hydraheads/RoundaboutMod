package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.JusticeEntity;
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
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersAchtungBaby extends NewDashPreset {
    public PowersAchtungBaby(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersAchtungBaby(entity);
    }


    public boolean InvisibleVisionOn(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons
        if (InvisibleVisionOn())
            setSkillIcon(context, x, y, 1, StandIcons.BABY_VISION_ON, PowerIndex.SKILL_1);
        else
            setSkillIcon(context, x, y, 1, StandIcons.BABY_VISION_OFF, PowerIndex.SKILL_1);


        if (isHoldingSneak())
            setSkillIcon(context, x, y, 2, StandIcons.SELF_INVIS, PowerIndex.NO_CD);
        else
            setSkillIcon(context, x, y, 2, StandIcons.BURST_INVIS, PowerIndex.SKILL_2);

        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        super.renderIcons(context, x, y);
    }


    public List<SurvivorEntity> survivorsSpawned = new ArrayList<>();

    public void listInit(){
        if (survivorsSpawned == null) {
            survivorsSpawned = new ArrayList<>();
        }
    }

    public Component getPosName(byte posID){
        return Component.empty();
    }
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        return $$1;
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

    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                switchModeClient();
            }
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

    public void switchModeClient(){
            SurvivorTarget = null;
            EntityTargetOne = null;
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
            tryPowerPacket(PowerIndex.POWER_1);
    }



    public void initializeTargets(int x, int y, int z){


        Entity targ = this.self.level().getEntity(x);
        if (targ instanceof SurvivorEntity SE){
            SurvivorTarget = SE;
        }
        EntityTargetOne = this.self.level().getEntity(y);
        EntityTargetTwo = this.self.level().getEntity(z);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return invisibleVisionSwitch();
            }
            case PowerIndex.POWER_2 -> {
                return invisibleBurst();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    public boolean invisibleBurst(){
        return true;
    }

    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        return false;
    }
    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if (
                (SurvivorTarget != null && ent != null && ent.is(SurvivorTarget)) ||
                        (EntityTargetOne != null && ent != null && ent.is(EntityTargetOne))
        ){
            return 4971295;
        }
        return 11283968;
    }

    public StandEntity displayStand = null;
    @Override
    public boolean returnFakeStandForHud(){
        return true;
    }
    public SurvivorEntity SurvivorTarget = null;
    public Entity EntityTargetOne = null;
    public Entity EntityTargetTwo = null;
    public boolean selectTarget(){
        setRageCupidCooldown();
        SurvivorEntity surv = SurvivorTarget;
        if (surv != null && EntityTargetOne instanceof LivingEntity LE && EntityTargetTwo instanceof LivingEntity LE2){
            surv.matchEntities(LE,LE2);
        }
        return true;
    }

    public void selectTargetClient(){
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


    public int lastPlacementTime = -1;
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        lastPlacementTime--;
        if (lastPlacementTime <= -1){
            lastPlacementTime = 600;
            createSurvivor(this.self.getPosition(1),true);
        }
    }

    public void despawnSurvivorClient(){
        tryPowerPacket(PowerIndex.POWER_2_SNEAK);
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        if (move == PowerIndex.POWER_2) {
            createSurvivor(pos,false);
            return true;
        }
        return tryPower(move, forced);
    }

    boolean thisistheend=false;
    @Override
    public void tickStandRejection(MobEffectInstance effect) {
    }

    public boolean canUseZap(Entity ent) {
        if (ent instanceof LivingEntity LE &&
                (
                        MainUtil.isBossMob(LE) &&
                                !ClientNetworking.getAppropriateConfig().survivorSettings.canUseSurvivorOnBossesInSurvival &&
                                !(this.getCreative())
                )
        ) {
            return false;
        }
        return true;
    }

    public void createSurvivor(Vec3 pos, boolean activated){
    }

    public void setRageCupidCooldown(){
        int cooldown = ClientNetworking.getAppropriateConfig().survivorSettings.rageCupidCooldown;
        this.setCooldown(PowerIndex.SKILL_4, cooldown);
    }


    @Override
    public boolean isServerControlledCooldown(CooldownInstance ci, byte num){
        if (num == PowerIndex.SKILL_2 && ClientNetworking.getAppropriateConfig().survivorSettings.SummonSurvivorCooldownCooldownUsesServerLatency) {
            return true;
        }
        if (num == PowerIndex.SKILL_4 && ClientNetworking.getAppropriateConfig().survivorSettings.rageCupidCooldownCooldownUsesServerLatency) {
            return true;
        }
        return super.isServerControlledCooldown(ci, num);
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }


    /** if = -1, not melt dodging */



    @Override
    public void tickPower() {
        if (this.self.level().isClientSide()){
        }
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
            BASE = 1,
            GREEN =2,
            RED =3,
            PURPLE=4,
            BLUE=5,
            SILVER=6,
            GHAST=7,
            ENDER=8,
            CONDUIT=9;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                BASE,
                GREEN,
                RED,
                PURPLE,
                BLUE,
                SILVER,
                GHAST,
                ENDER,
                CONDUIT
        );
    }

    @Override
    public int getDisplayPowerInventoryScale(){
        return 60;
    }
    @Override
    public int getDisplayPowerInventoryYOffset(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == JusticeEntity.DARK_MIRAGE){
            return super.getDisplayPowerInventoryYOffset();
        }
        return 7;
    }
    @Override public Component getSkinName(byte skinId) {
        return switch (skinId)
        {
            case GREEN -> Component.translatable("skins.roundabout.survivor.green");
            case RED -> Component.translatable("skins.roundabout.survivor.red");
            case PURPLE -> Component.translatable("skins.roundabout.survivor.purple");
            case BLUE -> Component.translatable("skins.roundabout.survivor.blue");
            case SILVER -> Component.translatable("skins.roundabout.survivor.silver");
            case GHAST -> Component.translatable("skins.roundabout.survivor.ghast");
            case ENDER -> Component.translatable("skins.roundabout.survivor.ender");
            case CONDUIT -> Component.translatable("skins.roundabout.survivor.conduit");
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
                return ModSounds.SUMMON_ACHTUNG_EVENT;
            }
            case BURST -> {
                return ModSounds.ACHTUNG_BURST_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }


    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP,slot);
    }

    public static final byte
            BURST = 61;
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.throw_bottle",
                "instruction.roundabout.press_skill", StandIcons.BOTTLE, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.summon_survivor",
                "instruction.roundabout.press_skill", StandIcons.SPAWN,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.desummon_survivor",
                "instruction.roundabout.press_skill_crouch", StandIcons.DESPAWN,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.target_zap",
                    "instruction.roundabout.press_skill", StandIcons.RAGE_SELECTION, 4, level, bypass));
        }
        return $$1;
    }

    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }


    public boolean invisibleVisionSwitch(){
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            getStandUserSelf().roundabout$setUniqueStandModeToggle(!InvisibleVisionOn());
            if (!isClient() && this.self instanceof ServerPlayer PE) {
                if (InvisibleVisionOn()) {
                    PE.displayClientMessage(Component.translatable("text.roundabout.achtung.vision_on").withStyle(ChatFormatting.AQUA), true);
                } else {
                    PE.displayClientMessage(Component.translatable("text.roundabout.achtung.vision_off").withStyle(ChatFormatting.AQUA), true);
                }
            }
        }
        return true;
    }


    boolean holdAttack = false;
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!holdAttack) {
                holdAttack = true;
                if (InvisibleVisionOn()) {
                    selectTargetClient();
                }
            }
        } else if (holdAttack){
            holdAttack = false;
        }
    }
}