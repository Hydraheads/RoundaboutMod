package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.ThrownWaterBottleEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
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

    public boolean interceptAttack(){
        return angerSelectionMode();
    }

    public boolean angerSelectionMode(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    public boolean canSummonStandAsEntity(){
        return false;
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

        if (angerSelectionMode())
            setSkillIcon(context, x, y, 4, StandIcons.CUPID_ON, PowerIndex.SKILL_4);
        else
            setSkillIcon(context, x, y, 4, StandIcons.RAGE_SELECTION, PowerIndex.SKILL_4);

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
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                throwBottleClient();
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
        SurvivorTarget = null;
        EntityTargetOne = null;
        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
        tryPowerPacket(PowerIndex.POWER_4);
    }

    public void throwBottleClient(){
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            if (canUseWaterBottleThrow()) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                tryPowerPacket(PowerIndex.POWER_1);
            }
        }
    }

    public void throwBottleActually(ItemStack stack){
        ThrownWaterBottleEntity $$4 = new ThrownWaterBottleEntity(this.self.level(), this.self);
        $$4.setItem(stack);
        $$4.shootFromRotation(this.self, this.self.getXRot(), this.self.getYRot(), -0.1F, 1.5F, 0.2F);
        this.self.level().addFreshEntity($$4);
    }

    public boolean throwWaterBottle(){
        int cooldown = 5;
        this.setCooldown(PowerIndex.SKILL_1, cooldown);
        if (!this.self.level().isClientSide() && this.self instanceof Player PL){
            ItemStack stack = this.getSelf().getMainHandItem();
            if ((!stack.isEmpty() && stack.getItem() instanceof PotionItem PI && PotionUtils.getPotion(stack) == Potions.WATER)) {
                throwBottleActually(stack.copy());
                if (!PL.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return true;
            }
            ItemStack stack2 = this.getSelf().getOffhandItem();
            if ((!stack2.isEmpty() && stack2.getItem() instanceof PotionItem PI && PotionUtils.getPotion(stack2) == Potions.WATER)) {
                throwBottleActually(stack2.copy());
                if (!PL.getAbilities().instabuild) {
                    stack2.shrink(1);
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        switch (move)
        {
            case PowerIndex.POWER_4_BONUS -> {
                initializeTargets(blockPos);
            }
        }
        return tryPower(move, forced);
    }

    public void initializeTargets(BlockPos blockPos){
        Entity targ = this.self.level().getEntity(blockPos.getX());
        if (targ instanceof SurvivorEntity SE){
            SurvivorTarget = SE;
        }
        EntityTargetOne = this.self.level().getEntity(blockPos.getY());
        EntityTargetTwo = this.self.level().getEntity(blockPos.getZ());
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return throwWaterBottle();
            }
            case PowerIndex.POWER_2_SNEAK -> {
                return removeAllSurvivors();
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
                    tryBlockPosPowerPacket(PowerIndex.POWER_4_BONUS, new BlockPos(SurvivorTarget.getId(), EntityTargetOne.getId(), TE.getId()));
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
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 50) {
                createSurvivor(this.self.getPosition(1F),true);
                if (tempstand != null) {
                    List<Entity> mobsInRange = MainUtil.getEntitiesInRange(this.self.level(), this.self.blockPosition(), ClientNetworking.getAppropriateConfig().survivorSettings.survivorRange, this.self);
                    LivingEntity firstTarget = null;
                    if (!mobsInRange.isEmpty()) {
                        for (Entity ent : mobsInRange) {
                            if (SurvivorEntity.canZapEntity(ent) && canUseZap(ent) && ent instanceof LivingEntity LE) {
                                tempstand.matchEntities(this.self,LE);
                            }
                        }
                    }
                }
            }
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

    public void createSurvivor(Vec3 pos, boolean activated){

        if (isClient() || (!this.onCooldown(PowerIndex.SKILL_2) || !ClientNetworking.getAppropriateConfig().survivorSettings.SummonSurvivorCooldownCooldownUsesServerLatency)) {
            int cooldown = ClientNetworking.getAppropriateConfig().survivorSettings.SummonSurvivorCooldownV2;
            this.setCooldown(PowerIndex.SKILL_2, cooldown);
            if (!isClient()) {
                blipStand(pos,activated);

            }
        }
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
            if (SurvivorEntity.canZapEntity(TE) && canUseZap(TE) && TE.distanceTo(SurvivorTarget) <= getCupidRange()){
                return TE;
            }
        } else {
            if (SurvivorEntity.canZapEntity(TE) && canUseZap(TE) && TE.distanceTo(SurvivorTarget) <= getCupidRange()
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
            addSurvivorToList(SE);
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
    public int meltDodgeTicks = -1;

    public int getCupidRange(){
        if (getCreative())
            return ClientNetworking.getAppropriateConfig().survivorSettings.survivorCupidCreativeRange;
        return ClientNetworking.getAppropriateConfig().survivorSettings.survivorCupidRange;
    }
    public int getCupidHighlightRange(){
        return ClientNetworking.getAppropriateConfig().survivorSettings.survivorCupidHighlightRange;
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
    public void tickPower() {
        if (this.self.level().isClientSide()){
            unloadTargets();
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
            GHAST=7;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                BASE,
                GREEN,
                RED,
                PURPLE,
                BLUE,
                SILVER,
                GHAST
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


    public boolean canUseWaterBottleThrow(){
        ItemStack stack = this.getSelf().getMainHandItem();
        ItemStack stack2 = this.getSelf().getOffhandItem();
        return ((!stack.isEmpty() && stack.getItem() instanceof PotionItem PI && PotionUtils.getPotion(stack) == Potions.WATER)
                || (!stack2.isEmpty() && stack2.getItem() instanceof PotionItem PI2 && PotionUtils.getPotion(stack2) == Potions.WATER));
    }
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 1 && !canUseWaterBottleThrow()){
            return true;
        }

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
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.target_zap",
                "instruction.roundabout.press_skill", StandIcons.RAGE_SELECTION,4,level,bypass));
        return $$1;
    }

    /**
    public boolean isWip(){
        return true;
    }
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW);
    }
    **/


    public boolean switchAngerSelectionMode(){
        getStandUserSelf().roundabout$setUniqueStandModeToggle(!angerSelectionMode());
        if (!isClient() && this.self instanceof ServerPlayer PE) {
            if (angerSelectionMode()) {
                PE.displayClientMessage(Component.translatable("text.roundabout.survivor.anger_selection").withStyle(ChatFormatting.RED), true);
            } else {
                PE.displayClientMessage(Component.translatable("text.roundabout.survivor.anger_selection_off").withStyle(ChatFormatting.RED), true);
            }
        }
        return true;
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