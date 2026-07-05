package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StepRuleEntity;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.DietSavedSecond;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.*;

public class PowersCalifornia extends NewDashPreset {

    public PowersCalifornia(LivingEntity self) {
        super(self);
    }
    public final Map<Entity, Integer> hurtEntities = new HashMap<>();
    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().cinderellaSettings.enableCinderella;
    }

    public DietSavedSecond rewindSnap = null;
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


    public void playUnfairSound(){
        if (self.level() instanceof ServerLevel sl){
            this.self.level().playSound(null, this.self.blockPosition(),
                    ModSounds.CKB_NO_EVENT, SoundSource.PLAYERS, 1F,
                    (float) (0.99f + Math.random() * 0.02f));
        }
    }
    public void playGotchaSound(){
        if (self.level() instanceof ServerLevel sl){
            this.self.level().playSound(null, this.self.blockPosition(),
                    ModSounds.CKB_YES_EVENT, SoundSource.PLAYERS, 1F,
                    (float) (0.99f + Math.random() * 0.02f));
        }
    }

    /**When you deal damage, intercept or run code based off of it, or potentially cancel it*/
    public boolean interceptDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if (!$$0.is(DamageTypes.THORNS)){
            if (isDoNotHurt()){
                if (hurtEntities.containsKey(target)){
                    removeFromList(target);
                    playUnfairSound();
                }
            }
        }
        return false;
    }


    public void removeFromList(Entity entity){
        hurtEntities.remove(entity);
        if (self instanceof ServerPlayer sp) {
            S2CPacketUtil.sendGenericIntToClientPacket(
                    sp,
                    PacketDataIndex.S2C_INT_CKB_REMOVE,
                    entity.getId()
            );
        }
    }
    public void addToList(Entity entity){
        if (entity.isAlive()) {
            hurtEntities.put(entity, entity.tickCount + 200);
            if (self instanceof ServerPlayer sp) {
                S2CPacketUtil.sendGenericIntToClientPacket(
                        sp,
                        PacketDataIndex.S2C_INT_CKB_ADD,
                        entity.getId()
                );
            }
        }
    }

    private final Set<Integer> clientEntityIds = new HashSet<>();
    public boolean isCapturedEntity(Entity entity) {
        return clientEntityIds.contains(entity.getId());
    }

    public Set<Integer> getCapturedEntityIds() {
        return clientEntityIds;
    }
    public void addToClientList(int entityId) {
        clientEntityIds.add(entityId);
    }

    public void removeFromClientList(int entityId) {
        clientEntityIds.remove(entityId);
    }
    public void clearClientList() {
        clientEntityIds.clear();
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
            case SKILL_1_NORMAL,SKILL_1_CROUCH -> {
                tryCatchEnemies();
            }
            case SKILL_2_NORMAL,SKILL_2_CROUCH -> {
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
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){

        if (slot == 1){
            if (clientEntityIds.isEmpty()){
                return true;
            }
        }

        return super.isAttackIneptVisually(activeP,slot);
    }
    public void tryCatchEnemies(){
        if (!clientEntityIds.isEmpty()) {
            if (!onCooldown(PowerIndex.SKILL_1)) {
                clearClientList();
                tryPowerPacket(PowerIndex.POWER_1);
            }
        }
    }
    @SuppressWarnings("deprecation")
    public void tryStrategyClient(){
        if (isDoNotHurt()) {
            if (!onCooldown(PowerIndex.SKILL_2)) {
                tryPowerPacket(PowerIndex.POWER_2);
            }
        } else if (isDoNotStep()){
            if (!onCooldown(PowerIndex.SKILL_EXTRA)) {
                BlockHitResult result = getRayBlockHit(self,5);
                BlockState state = self.level().getBlockState(result.getBlockPos());
                if (!state.isAir() &&
                        (state.isSolid()
                        || !state.getFluidState().isEmpty())){
                    tryBlockPosPowerPacket(PowerIndex.SKILL_EXTRA,result.getBlockPos());
                }
            }
        }
    }
    public void ruleSwitchClient(){
        if (!onCooldown(PowerIndex.SKILL_4)){
            this.self.playSound(ModSounds.MAGIC_DING_EVENT, 1F, 1.0F);
            clearClientList();
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



    /**If the standard left click input should be canceled while your stand is active*/
    public boolean interceptAttack(){
        return inCowerStance();
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == CaliforniaKingBedEntity.PART_8_SKIN) {
            return Component.translatable("skins.roundabout.california_king_bed.base");
        } else if (skinId == CaliforniaKingBedEntity.SUNSHINE) {
            return Component.translatable("skins.roundabout.california_king_bed.sunshine");
        }
        return Component.translatable("skins.roundabout.california_king_bed.base");
    }




    public void onActuallyHurt(DamageSource source, float $$1){
        if (source.getEntity() != null && !source.is(DamageTypes.THORNS)) {
            if (inCowerStance()){
                if (attackTimeDuring >= 5){
                    rewindSnap = DietSavedSecond.saveEntitySecond(self);
                    setCowerLeaveStance();
                    playGotchaSound();
                    addToList(source.getEntity());
                } else {
                    setNoStance();
                }
            }
        }
    }

    @Override
    public ResourceLocation getIconYes(int slot){
        if (slot == 1 && !getCapturedEntityIds().isEmpty())
            return StandIcons.SQUARE_PINK;
        return super.getIconYes(slot);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 1, StandIcons.STEAL_MEMORIES, PowerIndex.SKILL_1);

        if (isDoNotHurt()){
            setSkillIcon(context, x, y, 2, StandIcons.HURT_RULE, PowerIndex.SKILL_2);
        } else if (isDoNotLeave()){
            setSkillIcon(context, x, y, 2, StandIcons.LEAVE_RULE, PowerIndex.SKILL_EXTRA_2);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.FORBID_RULE, PowerIndex.SKILL_EXTRA);
        }

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


    public boolean canUseStepRule(){
        BlockHitResult result = getRayBlockHit(self,5);
        return !self.level().getBlockState(result.getBlockPos()).isAir();
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos pos) {
        spawnPos = pos;
        return super.tryBlockPosPower(move, forced,pos);
    }
    public BlockPos spawnPos = BlockPos.ZERO;

    public void doTheStepRule(){
        if (!this.self.level().isClientSide()){
            if (!onCooldown(PowerIndex.SKILL_EXTRA)) {
                setCooldown(PowerIndex.SKILL_EXTRA, 15);

                Vector3f newVec = new Vector3f((float) (spawnPos.getX() + 0.5),
                        (float) (spawnPos.getY() + 1),
                        (float) (spawnPos.getZ() + 0.5));

                StepRuleEntity step =
                        // slightly off to not z-fight
                        new StepRuleEntity(
                                self.level(),
                                newVec.x,
                                newVec.y,
                                newVec.z
                        );
                this.self.level().playSound(null, this.self.blockPosition(),
                        ModSounds.CKB_TILE_EVENT, SoundSource.PLAYERS, 1F,
                        (float) (1.00f + Math.random() * 0.01f));
                step.userEntity = self;
                step.timing = 200;
                self.level().addFreshEntity(step);
            }
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

    public void clearListServer(){
        if (!hurtEntities.isEmpty() && self instanceof ServerPlayer sp) {
            Iterator<Map.Entry<Entity, Integer>> it = hurtEntities.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<Entity, Integer> entry = it.next();

                Entity entity = entry.getKey();
                S2CPacketUtil.sendGenericIntToClientPacket(
                        sp,
                        PacketDataIndex.S2C_INT_CKB_REMOVE,
                        entity.getId()
                );

                it.remove();
            }
        }
    }

    public int timeSinceSwitch = 0;
    public void tickPower() {
        super.tickPower();
        if (!self.level().isClientSide()) {
            if (!hurtEntities.isEmpty() && self instanceof ServerPlayer sp) {
                Iterator<Map.Entry<Entity, Integer>> it = hurtEntities.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry<Entity, Integer> entry = it.next();

                    Entity entity = entry.getKey();

                    if (!entity.isAlive() || entry.getValue() <= entity.tickCount) {
                        S2CPacketUtil.sendGenericIntToClientPacket(
                                sp,
                                PacketDataIndex.S2C_INT_CKB_REMOVE,
                                entity.getId()
                        );

                        it.remove();
                    }
                }
            }
            if (getActivePower() == PowerIndex.POWER_2) {
                if (inCowerStance()) {
                    if (attackTimeDuring >= 30) {
                        xTryPower(PowerIndex.NONE, true);
                        timeSinceSwitch = 12;
                        setCowerLeaveStance();
                    } else if (attackTimeDuring == 5) {
                        Vec3 posPo = self.getEyePosition().add(self.getLookAngle().scale(1.5f));
                    }
                }
            } else {
                if (inCowerStance()) {
                    setCowerLeaveStance();
                }
            }

            if (inCowerLeaveStance()){
                timeSinceSwitch--;
                if (timeSinceSwitch <= 0){
                    setNoStance();
                }
            }


            if (hurtEntities.isEmpty() && rewindSnap != null){
                rewindSnap = null;
            }
        }
    }

    @Override
    public float inputSpeedModifiers(float basis){
        if (inCowerStance()) {
            basis*=0.0f;
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public boolean cancelJump(){
        if (inCowerStance()) {
            return true;
        }
        return super.cancelJump();
    }

    @Override
    public boolean cancelSprintParticles(){
        if (inCowerStance()) {
            return true;
        }
        return super.cancelSprintParticles();
    }

    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        if (!getCapturedEntityIds().isEmpty() && isCapturedEntity(ent)){
            return true;
        }
        return false;
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        return 16254719;
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
        } else if (move == PowerIndex.POWER_1){
            punishServer();
        } else if (move == PowerIndex.SKILL_EXTRA){
            doTheStepRule();
        }
        return super.setPowerOther(move,lastMove);
    }

    public void punishServer(){
        if (!hurtEntities.isEmpty() && self instanceof ServerPlayer sp) {
            if (rewindSnap != null){
                rewindSnap.loadTime(self);
                setCooldown(PowerIndex.SKILL_2,200);
                ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.PINK_SMOKE,
                        this.getSelf().getX(), this.getSelf().getY() + 1, this.getSelf().getZ(),
                        12, 2, 0.5,2, 0.015);
            }
            Iterator<Map.Entry<Entity, Integer>> it = hurtEntities.entrySet().iterator();
            this.self.level().playSound(null, this.self.blockPosition(),
                    ModSounds.CKB_STEAL_EVENT, SoundSource.PLAYERS, 1F,
                    (float) (1.00f + Math.random() * 0.01f));


            while (it.hasNext()) {
                Map.Entry<Entity, Integer> entry = it.next();

                Entity entity = entry.getKey();

                if (entity.isAlive()) {
                    entity.setDeltaMovement(0,0.2,0);
                }
            }
            hurtEntities.clear();
        }
    }

    public boolean inCowerStance(){
        return self instanceof Player pl && ((IPlayerEntity)pl).roundabout$GetPoseEmote() == 35;
    }
    public boolean inCowerLeaveStance(){
        return self instanceof Player pl && ((IPlayerEntity)pl).roundabout$GetPoseEmote() == 36;
    }

    public void setCowerLeaveStance(){
        if (self instanceof Player pl ){
            ((IPlayerEntity)pl).roundabout$SetPoseEmote((byte) 36);
        }
    }
    public void setNoStance(){
        if (self instanceof Player pl ){
            ((IPlayerEntity)pl).roundabout$SetPoseEmote((byte) 0);
        }
    }


    @Override
    public void onPoseEmoteSwitch(byte from, byte to){
        if (!self.level().isClientSide()){
            if (from == 35 && !(to == 35)){
                setCooldown(PowerIndex.SKILL_2,160);
            }
        }
    }

    public void cowerServer(){
        if (!onCooldown(PowerIndex.SKILL_2)){
            if (self instanceof ServerPlayer pl){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEEL_RAISE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.00f + Math.random() * 0.03f));
                setActivePower(PowerIndex.POWER_2);
                this.setAttackTimeDuring(0);
                ((IPlayerEntity)pl).roundabout$SetPoseEmote((byte) 35);
            }
        }
    }
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_2 ||
                num == PowerIndex.SKILL_EXTRA ||
                num == PowerIndex.SKILL_EXTRA_2) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    public void switchRules(){
        setCooldown(PowerIndex.SKILL_4,6);
        rewindSnap = null;
        hurtEntities.clear();
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
