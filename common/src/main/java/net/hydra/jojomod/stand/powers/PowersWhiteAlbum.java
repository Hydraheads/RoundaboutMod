package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.ThrownWaterBottleEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PowersWhiteAlbum extends NewDashPreset {
    public PowersWhiteAlbum(LivingEntity self) {
        super(self);
    }
    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().survivorSettings.enableSurvivor;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersWhiteAlbum(entity);
    }

    public boolean interceptAttack(){
        return angerSelectionMode();
    }

    public boolean angerSelectionMode(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }

    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public boolean rendersPlayer(){
        return true;
    }

    public boolean skatesActive = false;
    public boolean hasSkatesActivated(){
        return skatesActive && PowerTypes.hasStandActive(self);
    }

    @Override
    public boolean cancelSprintJump(){
        if (hasSkatesActivated()){
            return true;
        }
        return super.cancelSprintJump();
    }

    public int acceleration = 0;
    public float inputSpeedModifiers(float basis){
        if (hasSkatesActivated()){
            basis *= 1.3f+(acceleration*0.015F);
        }
        return super.inputSpeedModifiers(basis);
    }

    int lastAcceleration = 0;
    double lastY = 0;
    @Override
    public void tickPower() {
        if (isPacketPlayer()){
            lastAcceleration = acceleration;
            if (hasSkatesActivated()){
                if (self.isInWater() || self.hurtTime > 10 || self.isUsingItem()) {
                    acceleration = 0;
                } else if (!self.onGround()) {
                    if (lastY < self.getY()){
                        acceleration = 0;
                    } else {
                        acceleration = Math.min(100,acceleration+5);
                    }
                } else {
                    if (self.isSprinting() && !self.horizontalCollision){
                        if (lastY < self.getY()){
                            acceleration = Math.max(0,acceleration-25);
                        } else {
                            acceleration = Math.min(100,acceleration+1);
                        }
                    } else {
                        acceleration = Math.max(0,acceleration-5);
                    }
                }

            } else {
                acceleration = 0;
            }
            if (self.onGround()){
                lastY = self.getY();
            }
            if (acceleration != lastAcceleration){
                C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_WHITE_ALBUM_ACCELERATION,acceleration);
            }
        }
        super.tickPower();
    }

    @Override
    public boolean interceptDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if (self instanceof Player pl) {
            if (!self.level().isClientSide) {
                S2CPacketUtil.sendGenericIntToClientPacket(pl,
                        PacketDataIndex.INT_WHITE_ALBUM_ACCELERATION, 0);
            }
            acceleration = 0;
        }

        return false;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons

        if (!isHoldingSneak()){
            if (hasSkatesActivated()){
                setSkillIcon(context, x, y, 1, StandIcons.SKATE_ACTIVE, PowerIndex.SKILL_1);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.SKATE_INACTIVE, PowerIndex.SKILL_1);
            }
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.FREEZE_CANCEL, PowerIndex.SKILL_1);
        }

        if (!isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.TWISTER, PowerIndex.NO_CD);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.GENTLY_WEEPS, PowerIndex.NO_CD);
        }


        if (!isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.ICE_WALL, PowerIndex.GLOBAL_DASH);
        }

        if (!isHoldingSneak()){
            setSkillIcon(context, x, y, 4, StandIcons.SUIT_COMBAT, PowerIndex.NO_CD);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.FREEZE_BLOCKS, PowerIndex.NO_CD);
        }

        super.renderIcons(context, x, y);
    }

    public boolean renderHelmet(){
        return PowerTypes.hasStandActive(self);
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
        //Roundabout.LOGGER.info("skates: "+skatesActive);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putBoolean("skatesActive",skatesActive);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        if ($$0.contains("skatesActive")) {
            skatesActive = $$0.getBoolean("skatesActive");
        }
    }


    public void toggleSkatesClient(){
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
           ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
           tryPowerPacket(PowerIndex.POWER_1);
        }
    }
    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL-> {
                toggleSkatesClient();
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
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                switchModeClient();
            }
        }
    }

    public void switchModeClient(){
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            SurvivorTarget = null;
            EntityTargetOne = null;
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }


    public void throwBottleActually(ItemStack stack){

        this.self.level().playSound(
                null,
                this.self.getX(),
                this.self.getY(),
                this.self.getZ(),
                SoundEvents.SPLASH_POTION_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (this.self.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        ThrownWaterBottleEntity $$4 = new ThrownWaterBottleEntity(this.self.level(), this.self);
        $$4.setItem(stack);
        $$4.shootFromRotation(this.self, this.self.getXRot(), this.self.getYRot(), -0.1F, 1.5F, 0.2F);
        this.self.level().addFreshEntity($$4);
    }

    public boolean toggleSkates(){
        int cooldown = 5;
        this.setCooldown(PowerIndex.SKILL_1, cooldown);
        if (!this.self.level().isClientSide() && this.self instanceof Player PL){
            skatesActive = !skatesActive;
            if (skatesActive){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_EQUIP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
            } else {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_RETRACT_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
            }
            saveDiscAndSync();
        }
        return true;
    }

    public static float getWhiteAlbumAmt(Entity entity,float partialTicks){
        float heyFull = 0;
        if (entity instanceof LivingEntity LE) {
            StandUser user = ((StandUser) LE);
            boolean hasWhiteAlbumOut = user.roundabout$getStandPowers() instanceof PowersWhiteAlbum pw && pw.renderHelmet();
            int whiteAlbumTicks = user.roundabout$getWhiteAlbumVanishTicks();
            if (hasWhiteAlbumOut || whiteAlbumTicks > 0) {
                byte skin = user.roundabout$getStandSkin();
                if (user.roundabout$getLastStandSkin() != skin) {
                    user.roundabout$setLastStandSkin(skin);
                    whiteAlbumTicks = 0;
                    user.roundabout$setWhiteAlbumVanishTicks(0);
                }

                float partialTicks2 = partialTicks % 1;
                if (hasWhiteAlbumOut) {
                    heyFull = whiteAlbumTicks + partialTicks2;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    heyFull = whiteAlbumTicks - partialTicks2;
                    heyFull = Math.max(heyFull / 10, 0);
                }
            }
        }
        return heyFull;
    }

    @Override
    public boolean tryTripleIntPower(int move, boolean forced, int chargeTime, int move2, int move3){
        switch (move)
        {
            case PowerIndex.POWER_4_BONUS -> {
                initializeTargets(chargeTime,move2, move3);
            }
        }
        return tryPower(move, forced);
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
                return toggleSkates();
            }
            case PowerIndex.POWER_4 -> {
                return switchAngerSelectionMode();
            }
            case PowerIndex.POWER_4_BONUS -> {
                return selectTarget();
            }
        }
        return super.setPowerOther(move,lastMove);
    }

    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        if (ent != null) {
            if (angerSelectionMode()) {
                if (
                        (SurvivorTarget != null  && ent.is(SurvivorTarget)) ||
                                (EntityTargetOne != null && ent.is(EntityTargetOne))
                ) {
                    return true;
                }

                Entity highlights = getHighlighter();
                if (highlights != null && highlights.is(ent)){
                    return true;
                }
            }
        }
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

    public SurvivorEntity SurvivorTarget = null;
    public Entity EntityTargetOne = null;
    public Entity EntityTargetTwo = null;
    public boolean selectTarget(){
        setRageCupidCooldown();
        unloadTargets();
        SurvivorEntity surv = SurvivorTarget;
        if (surv != null && EntityTargetOne instanceof LivingEntity LE && EntityTargetTwo instanceof LivingEntity LE2){
            surv.matchEntities(LE,LE2);
        }
        return true;
    }
    public void selectTargetClient(){
        Entity TE = MainUtil.getTargetEntity(this.self, getCupidHighlightRange(), 15);
        if (SurvivorTarget == null){
            if (TE instanceof SurvivorEntity SE && (SE.getActivated() || getCreative())){
                SurvivorTarget = SE;
                this.self.playSound(ModSounds.SURVIVOR_PLACE_EVENT, 1F, 1.5F);
            }
        } else if (EntityTargetOne == null){
            if (SurvivorEntity.canZapEntity(TE) && canUseZap(TE) && TE.distanceTo(SurvivorTarget) <= getCupidRange()){
                EntityTargetOne = TE;
                this.self.playSound(ModSounds.SURVIVOR_PLACE_EVENT, 1F, 1.5F);
            }
        } else {
            if (SurvivorEntity.canZapEntity(TE) && canUseZap(TE) && TE.distanceTo(SurvivorTarget) <= getCupidRange() && !EntityTargetOne.is(TE)){
                /**Passing 3 integers is something a block pos can do, so why not just use that packet*/

                if (!this.onCooldown(PowerIndex.SKILL_4)) {
                    setRageCupidCooldown();
                    tryTripleIntPacket(PowerIndex.POWER_4_BONUS, SurvivorTarget.getId(), EntityTargetOne.getId(), TE.getId());

                    SurvivorTarget = null;
                    EntityTargetOne = null;
                }
            }
        }
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


    @Override
    public void tickMobAI(LivingEntity attackTarget){
    }

    public void despawnSurvivorClient(){
        tryPowerPacket(PowerIndex.POWER_2_SNEAK);
    }
    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        return tryPower(move, forced);
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect) {
        if (!this.getSelf().level().isClientSide()) {
        }
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


    public void setRageCupidCooldown(){
        int cooldown = ClientNetworking.getAppropriateConfig().survivorSettings.rageCupidCooldown;
        this.setCooldown(PowerIndex.SKILL_4, cooldown);
    }

    public Entity getHighlighter(){
        Entity TE = MainUtil.getTargetEntity(this.self, getCupidHighlightRange(), 15);
        if (SurvivorTarget == null){
            if (TE instanceof SurvivorEntity SE && (SE.getActivated() || getCreative())){
                return SE;
            }
        } else if (EntityTargetOne == null){
            if (SurvivorEntity.canZapEntity(TE) && canUseZap(TE) && !TE.isInvisible() && TE.distanceTo(SurvivorTarget) <= getCupidRange()){
                return TE;
            }
        } else {
            if (SurvivorEntity.canZapEntity(TE) && canUseZap(TE) && !TE.isInvisible() && TE.distanceTo(SurvivorTarget) <= getCupidRange()
                    && !EntityTargetOne.is(TE)){
                return TE;
            }
        }
        return null;
    }

    SurvivorEntity tempstand = null;
    public void blipStand(Vec3 pos, boolean activated){
        StandEntity stand = ModEntities.SURVIVOR.create(this.getSelf().level());
        if (stand instanceof SurvivorEntity SE) {
            StandUser user = getStandUserSelf();
            stand.absMoveTo(pos.x(), pos.y(), pos.z());
            stand.setSkin(user.roundabout$getStandSkin());
            stand.setIdleAnimation(user.roundabout$getIdlePos());
            stand.setMaster(this.self);
            SE.setRandomSize((float) (Math.random()*0.4F));
            SE.setYRot(this.self.getYHeadRot() % 360);
            if (activated){
                SE.setActivated(true);
            }
            tempstand = SE;
            this.self.level().addFreshEntity(stand);
            playStandUserOnlySoundsIfNearby(PLACE, 100, false, false);
        }

    }

    @Override
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_2 && ClientNetworking.getAppropriateConfig().survivorSettings.SummonSurvivorCooldownCooldownUsesServerLatency) {
            return true;
        }
        if (num == PowerIndex.SKILL_4 && ClientNetworking.getAppropriateConfig().survivorSettings.rageCupidCooldownCooldownUsesServerLatency) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
    }


    /** if = -1, not melt dodging */
    public int meltDodgeTicks = -1;

    public int getCupidRange(){
        if (getCreative())
            return ClientNetworking.getAppropriateConfig().survivorSettings.survivorCupidCreativeRange;
        return ClientNetworking.getAppropriateConfig().survivorSettings.survivorCupidRange;
    }
    public int getCupidHighlightRange(){
        return 100;
    }

    public void unloadTargets(){
        if (SurvivorTarget != null){
            if ((!SurvivorTarget.getActivated() && !getCreative()) || SurvivorTarget.isRemoved() || !SurvivorTarget.isAlive()){
                SurvivorTarget = null;
            }
        }
        if (EntityTargetOne != null){
            if (SurvivorTarget == null ||
                    EntityTargetOne.isRemoved() || !EntityTargetOne.isAlive() ||
                    (EntityTargetOne.distanceTo(SurvivorTarget) > getCupidRange())){
                SurvivorTarget = null;
            }
        }
        if (EntityTargetTwo != null){
            if (SurvivorTarget == null || EntityTargetOne == null ||
                    EntityTargetTwo.isRemoved() || !EntityTargetTwo.isAlive() ||
                    (EntityTargetTwo.distanceTo(SurvivorTarget) > getCupidRange())
                    || (EntityTargetOne != null && EntityTargetOne.is(EntityTargetTwo))){
                EntityTargetTwo = null;
            }
        }
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
            BETA =2,
            FIERCE =3,
            BLACK =4;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                BASE,
                FIERCE,
                BLACK,
                BETA
        );
    }

    @Override
    public int getDisplayPowerInventoryScale(){
        return 60;
    }
    @Override
    public int getDisplayPowerInventoryYOffset(){
        return 7;
    }
    @Override public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.white_album."+getSkinString(skinId));
    }
    public static Component getSkinNameStatic(byte skinId) {
        return Component.translatable("skins.roundabout.white_album."+getSkinString(skinId));
    }
    public static String getSkinString(byte skinId) {
        return switch (skinId)
        {
            case BETA -> "beta";
            case BLACK -> "black";
            case FIERCE -> "dramatic";
            default -> "base";
        };
    }

    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        switch (soundChoice)
        {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.WHITE_ALBUM_SUMMON_EVENT;
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


    public boolean canUseWaterBottleThrow(){
        ItemStack stack = this.getSelf().getMainHandItem();
        ItemStack stack2 = this.getSelf().getOffhandItem();
        return ((!stack.isEmpty() && stack.getItem() instanceof PotionItem PI &&
                PotionUtils.getPotion(stack) == Potions.WATER && !(PI instanceof SplashPotionItem))
                || ((!stack2.isEmpty() && stack2.getItem() instanceof PotionItem PI2 && PotionUtils.getPotion(stack2) == Potions.WATER)
                && !(PI2 instanceof SplashPotionItem)));
    }
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        return super.isAttackIneptVisually(activeP,slot);
    }

    public static final byte
            PLACE = 61,
            RETRACT = 62,
            SHOCK = 63;
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



    public boolean switchAngerSelectionMode(){
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            getStandUserSelf().roundabout$setUniqueStandModeToggle(!angerSelectionMode());
            if (!isClient() && this.self instanceof ServerPlayer PE) {
                if (angerSelectionMode()) {
                    PE.displayClientMessage(Component.translatable("text.roundabout.survivor.anger_selection").withStyle(ChatFormatting.RED), true);
                } else {
                    PE.displayClientMessage(Component.translatable("text.roundabout.survivor.anger_selection_off").withStyle(ChatFormatting.RED), true);
                }
            }
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


    boolean holdAttack = false;
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!holdAttack) {
                holdAttack = true;
                if (angerSelectionMode()) {
                    selectTargetClient();
                }
            }
        } else if (holdAttack){
            holdAttack = false;
        }
    }
}