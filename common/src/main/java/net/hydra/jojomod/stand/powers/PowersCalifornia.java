package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PowersCalifornia extends NewDashPreset {

    public PowersCalifornia(LivingEntity self) {
        super(self);
    }

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().cinderellaSettings.enableCinderella;
    }

    public static final byte DO_NOT_STEP_HERE = 0;
    public static final byte DO_NOT_HURT_ME = 1;
    public static final byte DO_NOT_LEAVE_ME = 2;
    public byte currentRule = DO_NOT_STEP_HERE;
    public byte getCurrentRule(){
        return currentRule;
    }
    public void setCurrentRule(byte rule){
        currentRule = rule;
        if (!self.level().isClientSide()){
            saveDiscAndSync();
        }
    }
    public void nextRule(){
        currentRule++;
        if (currentRule > DO_NOT_LEAVE_ME){
            currentRule = DO_NOT_STEP_HERE;
        }
        if (!self.level().isClientSide()){
            saveDiscAndSync();
        }
    }
    public boolean isDoNotStep(){
        return currentRule == DO_NOT_STEP_HERE;
    }
    public boolean isDoNotLeave(){
        return currentRule == DO_NOT_LEAVE_ME;
    }
    public boolean isDoNotHurt(){
        return currentRule == DO_NOT_HURT_ME;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putByte("currentRule",currentRule);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        if ($$0.contains("currentRule")) {
            currentRule = $$0.getByte("currentRule");
        }
    }

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.CALIFORNIA_KING_BED.create(this.getSelf().level());
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersCalifornia(entity);
    }

    @Override
    public boolean isSecondaryStand(){
        return true;
    }
    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        return $$1;
    }

    @Override
    public Component getPosName(byte posID){
        if (posID == 1){
            return Component.translatable(  "idle.roundabout.ckb_2");
        } else {
            return Component.translatable(  "idle.roundabout.ckb_1");
        }
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_2_NORMAL -> {
                tryStrategyClient();
            }
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                tryToDash2Client();
            }
            case SKILL_4_NORMAL -> {
                ruleSwitchClient();
            }
        }
    }

    public void tryStrategyClient(){
        if (isDoNotHurt()) {
            if (!onCooldown(PowerIndex.SKILL_2)) {
                tryPowerPacket(PowerIndex.POWER_2);
            }
        }
    }
    public void ruleSwitchClient(){
        if (!onCooldown(PowerIndex.SKILL_4)){
            this.self.playSound(ModSounds.MAGIC_DING_EVENT, 1F, 1.0F);
            setCooldown(PowerIndex.SKILL_4,7);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }

    public void tryToDashClient(){
        if (canFallBrace()) {
            doFallBraceClient();
        } else {
            dash();
        }
    }
    public void tryToDash2Client(){
        if (canFallBrace()) {
            doFallBraceClient();
        } else {
            dash();
        }
    }

    @Override
    public void tickPowerEnd() {
        super.tickPowerEnd();
    }


    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == CaliforniaKingBedEntity.PART_8_SKIN) {
            return Component.translatable("skins.roundabout.california_king_bed.base");
        } else if (skinId == CaliforniaKingBedEntity.SUNSHINE) {
            return Component.translatable("skins.roundabout.california_king_bed.sunshine");
        }
        return Component.translatable("skins.roundabout.california_king_bed.base");
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (this.getSelf().fallDistance > 3) {
            setSkillIcon(context, x, y, 3, StandIcons.CALIFORNIA_FALL_CATCH, PowerIndex.SKILL_EXTRA);
        } else {
            if (isHoldingSneak()){
                setSkillIcon(context, x, y, 3, StandIcons.EXPERIENCE_BISHOP, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 4, StandIcons.GO_BEYOND, PowerIndex.SKILL_4_SNEAK);
        } else {
            ResourceLocation icon;
            if (isDoNotHurt()){
                icon = StandIcons.HURT_RULE;
            } else if (isDoNotLeave()){
                icon = StandIcons.LEAVE_RULE;
            } else {
                icon = StandIcons.FORBID_RULE;
            }
            setSkillIcon(context, x, y, 4, icon, PowerIndex.SKILL_4);
        }
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_CALIFORNIA_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    @Override
    /**The stand is named on the disc so we just use that*/
    public Component getStandName(){
        ItemStack disc = ((StandUser)this.getSelf()).roundabout$getStandDisc();
        if (!disc.isEmpty() && disc.getItem() instanceof StandDiscItem SDI){
            return Component.translatable(SDI.getDescriptionId() + ".desc.short");
        }
        return Component.empty();
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move,forced);
    }

    public void tickPower() {
        super.tickPower();
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.FALL_BRACE_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.POWER_4){
            switchRules();
        } else if (move == PowerIndex.POWER_2){
            cowerServer();
        }
        return super.setPowerOther(move,lastMove);
    }

    public boolean inCowerStance(){
        return self instanceof Player pl && ((IPlayerEntity)pl).roundabout$GetPoseEmote() == 35;
    }


    @Override
    public void onPoseEmoteSwitch(byte from, byte to){
        if (from == 35 && !(to == 35 || to == 36)){
            setCooldown(PowerIndex.SKILL_4,200);
        }
    }

    public void cowerServer(){
        if (!onCooldown(PowerIndex.SKILL_2)){
            if (self instanceof ServerPlayer pl){
                setActivePower(PowerIndex.POWER_2);
                ((IPlayerEntity)pl).roundabout$SetPoseEmote((byte) 35);
            }
        }
    }
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_2) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    public void switchRules(){
        setCooldown(PowerIndex.SKILL_4,6);
        nextRule();
        if (self instanceof ServerPlayer pl){
            pl.displayClientMessage(Component.translatable("text.roundabout.ckb_rule_"+currentRule).withStyle(ChatFormatting.LIGHT_PURPLE), true);
        }
    }

    @Override
    public void playFallBraceInitSound(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.FLUFF_BRACE_INIT_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
    }
    @Override
    public void playFallBraceImpactSounds(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.FLUFF_FALL_BRACE_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget){
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
       return $$1;
    }
    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(CaliforniaKingBedEntity.PART_8_SKIN);
        $$1.add(CaliforniaKingBedEntity.SUNSHINE);
        return $$1;
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
    }


    @Override
    public boolean fallBraceInit() {
        this.getSelf().fallDistance -= 20;
        if (this.getSelf().fallDistance < 0){
            this.getSelf().fallDistance = 0;
        }
        impactBrace = true;
        impactAirTime = 15;

        animateStand(StandEntity.BLOCK);
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.EXTRA);
        this.poseStand(OffsetIndex.BENEATH_2);
        animateStand(CaliforniaKingBedEntity.FALL_BRACE);
        if (!this.getSelf().level().isClientSide()) {
            playFallBraceInitSound();
        }
        return true;
    }


    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("Hydra");
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active");
    }
}
