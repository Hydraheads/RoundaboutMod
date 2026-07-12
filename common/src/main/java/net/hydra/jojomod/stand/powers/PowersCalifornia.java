package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.KingBedBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StepRuleEntity;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.mobs.AnubisGuardian;
import net.hydra.jojomod.entity.npcs.Aesthetician;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.DietSavedSecond;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MemoryChessPieceItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
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
        return ClientNetworking.getAppropriateConfig().californiaKingBedSettings.enableCKB;
    }

    public DietSavedSecond rewindSnap = null;
    public Entity snapEntity = null;
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
        super.addAdditionalSaveData($$0);
        $$0.putByte("currentRule",currentRule);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("currentRule")) {
            currentRule = $$0.getByte("currentRule");
        }
    }


    //Hording player pieces would be toxic
    @Override
    public boolean onKilledEntity(ServerLevel $$0, LivingEntity victim){
        if (self instanceof Player player) {
            UUID victimId = victim.getUUID();

            Inventory inv = player.getInventory();

            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack invStack = inv.getItem(i);

                if (!(invStack.getItem() instanceof MemoryChessPieceItem)) {
                    continue;
                }

                CompoundTag tag = invStack.getTag();
                if (tag != null &&
                        tag.getBoolean("activated") &&
                        tag.hasUUID("victim") &&
                        victimId.equals(tag.getUUID("victim"))) {

                        tag.putBoolean("activated",false);

                        tag.remove("victim");
                        invStack.setDamageValue(0);
                        inv.setItem(i,invStack);
                }
            }
        }


        return super.onKilledEntity($$0,victim);
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

    @Override
    public void onStandSummon(boolean desummon) {
        releaseStandFromBed();
        super.onStandSummon(desummon);
    }

    /**When you deal damage, intercept or run code based off of it, or potentially cancel it*/
    public boolean interceptDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if (!$$0.is(DamageTypes.THORNS)){
            if (isDoNotHurt() && !hurtEntities.isEmpty()){
                clearListServer();
                playUnfairSound();
            }
        }
        return false;
    }



    public int leadedInt = 0;
    public void setLeadTarget(Entity leaded2){
        if (leaded2 instanceof LivingEntity LE){
            leaded = LE;
        } else {
            leaded = null;
        }
        int sendint = 0;
        if (leaded != null){
            sendint= leaded.getId();
        }
        if (self instanceof ServerPlayer sp) {
            S2CPacketUtil.sendGenericIntToClientPacket(
                    sp,
                    PacketDataIndex.S2C_INT_LEADED,
                    sendint
            );
        }
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

    public static boolean canSteal(Entity entity){
        if (entity instanceof LivingEntity LE){
            if (MainUtil.isBossMob(LE) || LE instanceof FallenMob ||
            LE instanceof StandEntity || !entity.isAlive() ||
                    (entity instanceof Player pl && pl.isCreative())){
                return false;
            }
            return true;
        }
        return false;
    }
    public void addToList(Entity entity){
        if (entity.isAlive() && !entity.is(self)) {
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

    public Entity getCaliforniaTargetEntity(){
        return getTargetEntity(self,5);
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
            case SKILL_1_NORMAL -> {
                tryCatchEnemies();
            }
            case SKILL_1_CROUCH -> {
                tryCatchEnemiesEXP();
            }
            case SKILL_2_NORMAL -> {
                tryStrategyClient();
            }
            case SKILL_2_CROUCH -> {
                trySaveLocationClient();
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
            case SKILL_4_CROUCH -> {
                placeBedClient();
            }
        }
    }

    public void placeBedClient(){
        if (!onCooldown(PowerIndex.SKILL_4_SNEAK)) {
            if (canPlaceBed()) {
                tryPowerPacket(PowerIndex.POWER_4_SNEAK);
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
        if (slot == 4 && isHoldingSneak()){
            if (!canPlaceBed()){
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
    public void tryCatchEnemiesEXP(){
        if (!clientEntityIds.isEmpty()) {
            if (!onCooldown(PowerIndex.SKILL_1)) {
                clearClientList();
                tryPowerPacket(PowerIndex.POWER_1_SNEAK);
            }
        }
    }

    public void trySaveLocationClient(){
        if (!onCooldown(PowerIndex.SKILL_2_SNEAK)) {
            tryPowerPacket(PowerIndex.POWER_2_SNEAK);
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
        } else if (isDoNotLeave()){
            if (!onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                if (targEnt != null && !self.isPassenger()){
                    tryIntPowerPacket(PowerIndex.SKILL_EXTRA_2,targEnt.getId());
                } else {
                    tryIntPowerPacket(PowerIndex.SKILL_EXTRA_2,-1);
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
            tryMakeExpBishop();
        }
    }

    public void tryMakeExpBishop(){
        if (!onCooldown(PowerIndex.SKILL_3)) {
            tryPowerPacket(PowerIndex.POWER_3);
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
        } else if (skinId == CaliforniaKingBedEntity.EGYPT) {
            return Component.translatable("skins.roundabout.california_king_bed.egypt");
        } else if (skinId == CaliforniaKingBedEntity.SPOOKY) {
            return Component.translatable("skins.roundabout.california_king_bed.spooky");
        }
        return Component.translatable("skins.roundabout.california_king_bed.base");
    }




    public void onActuallyHurt(DamageSource source, float $$1){
        if (source.getEntity() != null && !source.is(DamageTypes.THORNS)
                && !source.getEntity().getUUID().equals(self.getUUID())
                && !source.is(ModDamageTypes.GO_BEYOND)
                && !source.is(ModDamageTypes.STAND_FIRE)
                && !(source.getEntity() instanceof Pufferfish)
                && !(source.getEntity() instanceof Axolotl)
                && !(source.getEntity() instanceof FallenMob)) {
            if (inCowerStance()){
                if (attackTimeDuring >= 5 && PowersCalifornia.canSteal(source.getEntity())){
                    rewindSnap = DietSavedSecond.saveEntitySecond(self);
                    snapEntity = source.getEntity();
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

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.STEAL_MEMORIES_2, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.STEAL_MEMORIES, PowerIndex.SKILL_1);
        }

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.SAVE_LOCATION, PowerIndex.SKILL_2_SNEAK);
        } else {
            if (isDoNotHurt()) {
                setSkillIcon(context, x, y, 2, StandIcons.HURT, PowerIndex.SKILL_2);
            } else if (isDoNotLeave()) {
                setSkillIcon(context, x, y, 2, StandIcons.LEAVE, PowerIndex.SKILL_EXTRA_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.FORBID, PowerIndex.SKILL_EXTRA);
            }
        }

        if (this.getSelf().fallDistance > 3) {
            setSkillIcon(context, x, y, 3, StandIcons.CALIFORNIA_FALL_CATCH, PowerIndex.SKILL_EXTRA);
        } else {
            if (isHoldingSneak()){
                setSkillIcon(context, x, y, 3, StandIcons.EXPERIENCE_BISHOP, PowerIndex.SKILL_3);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }

        if (isHoldingSneak()){
            setSkillIcon(context, x, y, 4, StandIcons.SLEEP, PowerIndex.SKILL_4_SNEAK);
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
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        storedInt = chargeTime;
        return super.tryIntPower(move, forced, chargeTime);
    }
    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos pos) {
        spawnPos = pos;
        return super.tryBlockPosPower(move, forced,pos);
    }

    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight, int x,
                                  boolean removeNum){
        StandHudRender.renderCKBDistance(context,cameraPlayer,screenWidth,screenHeight,x,leadedInt);
    }
    public BlockPos spawnPos = BlockPos.ZERO;

    public boolean replaceHudActively(){
        return leadedInt > 0;
    }
    public void doTheLeaveRule() {
        if (!this.self.level().isClientSide()) {
            boolean dupe = false;
            if (leaded != null &&
                    leaded.getId() == storedInt){
                dupe = true;
            }
            if (!onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                setCooldown(PowerIndex.SKILL_EXTRA_2, 15);
                if (storedInt > -1 && !dupe){
                    Entity zent = self.level().getEntity(storedInt);
                    if (zent instanceof LivingEntity LV){
                        clearLeaded();
                        ((StandUser)LV).roundabout$setBoundTo(self);
                        setLeadTarget(LV);
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEART_SPARKLE_EVENT,
                                SoundSource.PLAYERS, 1F, (float) (0.99f + Math.random() * 0.03f));
                        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MAGIC_HEART,
                                LV.getEyePosition().x, LV.getEyePosition().y, LV.getEyePosition().z,
                                0, 0, 1,0, 0.15);
                    } else {
                        clearLeaded();
                    }
                } else {
                    clearLeaded();
                }
            }
        }
    }


    public void clearLeadAndPunish(){
        if (!self.level().isClientSide() && leaded != null){
            playUnfairSound();
            clearLeaded();
        }
    }

    public void onEnderPearlThrow(){
        clearLeadAndPunish();
    }

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
                addSpawnedEntity(step);
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


    public static int getCKBrange(){
        return 20;
    }

    public int timeSinceSwitch = 0;
    public void tickPower() {
        super.tickPower();
        if (!self.level().isClientSide()) {

            StandEntity stando = getStandEntity(self);
            if (stando instanceof CaliforniaKingBedEntity cbe && cbe.bedUUID != null){
                if (cbe.distanceTo(self) > 33){
                    releaseStandFromBed();
                }
            }
            tickSpawnedEntities();
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

            if (leaded != null) {
                if (leaded.isAlive()) {
                    if (leaded instanceof Mob mb) {
                        if (leaded instanceof AbstractVillager || leaded instanceof Animal ||
                                leaded instanceof Aesthetician ||
                                leaded instanceof WaterAnimal) {
                            ((IMob) mb).roundabout$setHypnotizedBy(self);
                        }
                    }

                    if (leaded.distanceTo(self) > getCKBrange() && PowersCalifornia.canSteal(leaded)){
                        addToList(leaded);
                        playGotchaSound();
                        clearLeaded();
                    }
                } else {
                    clearLeaded();
                }
            }

            if (self.isFallFlying() || self.isPassenger() || self.isAutoSpinAttack()){
                clearLeadAndPunish();
            }
        } else {
            if (isDoNotLeave()){
                Entity targent = getCaliforniaTargetEntity();
                if (PowersCalifornia.canSteal(targent)){
                    targEnt = targent;
                } else {
                    targEnt = null;
                }
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

    Entity targEnt = null;

    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        if (!getCapturedEntityIds().isEmpty() && isCapturedEntity(ent)){
            return true;
        }
        if (isDoNotLeave() && targEnt != null && ent != null && ent.getId() == targEnt.getId()){
            if (hasStandActive(self)) {
                if (ent.getId() != leadedInt) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onPowerSwitch(){
        clearLeaded();
        super.onPowerSwitch();
    }
    @Override
    public void onStandSwitch(){
        clearLeaded();
        super.onStandSwitch();
    }
    public LivingEntity leaded = null;
    public void clearLeaded(){
        if (leaded != null){
            ((StandUser)leaded).roundabout$dropString();
            setLeadTarget(null);
        }
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if (isDoNotLeave() && targEnt != null && ent != null && ent.getId() == targEnt.getId()){
            return 16635903;
        }
        return 16254719;
    }

    @Override
    public void updateUniqueMoves() {
        super.updateUniqueMoves();
    }
    public void releaseStandFromBed(){
        if (!self.level().isClientSide()){
            StandEntity stando = getStandEntity(self);
            if (stando instanceof CaliforniaKingBedEntity cbe && cbe.bedUUID != null){
                cbe.bedUUID = null;
                cbe.bedBlockBind = null;
                animateStand(StandEntity.IDLE);
                cbe.setOffsetType(OffsetIndex.FOLLOW);
            }
        }
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        releaseStandFromBed();
        if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.FALL_BRACE_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.POWER_4){
            switchRules();
        } else if (move == PowerIndex.POWER_2){
            cowerServer();
        } else if (move == PowerIndex.POWER_2_SNEAK){
            saveLocation();
        } else if (move == PowerIndex.POWER_1){
            punishServer(false);
        } else if (move == PowerIndex.POWER_1_SNEAK){
            punishServer2();
        } else if (move == PowerIndex.SKILL_EXTRA){
            doTheStepRule();
        } else if (move == PowerIndex.SKILL_EXTRA_2){
            doTheLeaveRule();
        } else if (move == PowerIndex.POWER_3){
            saveBishop();
        } else if (move == PowerIndex.POWER_4_SNEAK){
            placeBedServer();
        }
        return super.setPowerOther(move,lastMove);
    }


    public void punishServer2(){
        punishServer(true);
    }

    public void punishServer(boolean exp){
        if (!hurtEntities.isEmpty() && self instanceof ServerPlayer sp) {
            if (rewindSnap != null && snapEntity != null && snapEntity.isAlive()
            && hurtEntities != null && hurtEntities.containsKey(snapEntity)){
                rewindSnap.loadTime(self);
                setCooldown(PowerIndex.SKILL_2,ClientNetworking.getAppropriateConfig().
                        californiaKingBedSettings.doNotHurtMeRewindCD);
                ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.PINK_SMOKE,
                        this.getSelf().getX(), this.getSelf().getY() + 1, this.getSelf().getZ(),
                        12, 2, 0.5,2, 0.015);
            }
            //Uncomment below to test player removal stuff on self
            //hurtEntities.put(self, self.tickCount+200);
            Iterator<Map.Entry<Entity, Integer>> it = hurtEntities.entrySet().iterator();
            this.self.level().playSound(null, this.self.blockPosition(),
                    ModSounds.CKB_STEAL_EVENT, SoundSource.PLAYERS, 1F,
                    (float) (1.00f + Math.random() * 0.01f));


            while (it.hasNext()) {
                Map.Entry<Entity, Integer> entry = it.next();

                Entity entity = entry.getKey();

                if (entity.isAlive()) {
                    entity.setDeltaMovement(0,0.15,0);
                    ItemStack piece = getPieceType(entity, exp, true, -1);
                    MainUtil.addItem(sp,piece);
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.QUESTION,
                            entity.getEyePosition().x, entity.getEyePosition().y+0.5F, entity.getEyePosition().z,
                            0, 0, 0.5,0, 0.15);
                }
            }
            hurtEntities.clear();
            clearAllSpawnedEntities();
            clearLeaded();
            snapEntity = null;
            int length = ClientNetworking.getAppropriateConfig().californiaKingBedSettings.postPunishDelay;
            setCooldown(PowerIndex.SKILL_1, length);
            setCooldown(PowerIndex.SKILL_2, length);
            setCooldown(PowerIndex.SKILL_EXTRA_2, length);
            setCooldown(PowerIndex.SKILL_EXTRA, length);
        }
    }

    public ItemStack getPieceType(Entity victim,boolean exp, boolean style, int force){
        boolean isWhite = !style;
        double rand = Math.random();
        ItemStack stack;
        Item result;
        if (rand < 0.1){
            if (isWhite){
                result= ModItems.MEMORY_KING_WHITE;
            } else {
                result= ModItems.MEMORY_KING;
            }
        } else if (rand < 0.2){
            if (isWhite){
                result= ModItems.MEMORY_QUEEN_WHITE;
            } else {
                result= ModItems.MEMORY_QUEEN;
            }
        } else if (rand < 0.35){
            if (isWhite){
                result= ModItems.MEMORY_BISHOP_WHITE;
            } else {
                result= ModItems.MEMORY_BISHOP;
            }
        } else if (rand < 0.5){
            if (isWhite){
                result= ModItems.MEMORY_KNIGHT_WHITE;
            } else {
                result= ModItems.MEMORY_KNIGHT;
            }
        } else if (rand < 0.65){
            if (isWhite){
                result= ModItems.MEMORY_ROOK_WHITE;
            } else {
                result= ModItems.MEMORY_ROOK;
            }
        } else {
            if (isWhite){
                result= ModItems.MEMORY_PAWN_WHITE;
            } else {
                result= ModItems.MEMORY_PAWN;
            }
        }
        stack = new ItemStack(result);
        int num = force;
        if (force < 0){
            num = getStealType(victim,exp);
        }
        return MemoryChessPieceItem.initializePiece(stack,victim,num);
    }

    public int getStealType(Entity victim, boolean exp){
        boolean isMemortaken = false;
        Entity lastTarget = null;
        if (victim instanceof LivingEntity LE){
            if (exp){
                if (LE instanceof Villager vg){
                    Brain<Villager> brain = vg.getBrain();
                    if (brain.hasMemoryValue(MemoryModuleType.JOB_SITE)) {
                        Optional<GlobalPos> jobSite = brain.getMemory(MemoryModuleType.JOB_SITE);
                        if (jobSite.isPresent()) {
                            return 12;
                        }
                    }
                }
                if (LE instanceof Player pl){
                    return 13;
                }
                if (!((StandUser)LE).rdbt$getExperienceTaken() && LE.getExperienceReward() > 0) {
                    ((StandUser)LE).rdbt$setExperienceTaken(true);
                    return 10;
                }
            }

            if (victim instanceof Mob mb){
                lastTarget = mb.getTarget();
            }
            ((StandUser)LE).roundabout$deeplyRemoveAttackTarget();
            if (victim instanceof Mob mb){
                isMemortaken = ((IMob)victim).rdbt$getStolen();
                ((IMob)victim).rdbt$setStolen(true);
                ((IMob)mb).roundabout$setConfusionTicks(30);
            }
        }
        if (!isMemortaken && (victim instanceof Skeleton || victim instanceof Stray)) {
            return 7;
        } else if (victim instanceof Player pl && PowerTypes.hasStandActive(pl)
        && !((StandUser)pl).roundabout$getStandPowers().isSecondaryStand()
                && ((IPlayerEntity)pl).rdbt$getLevelDecreaseTicks() <= 0
        ) {
            ((IPlayerEntity)pl).rdbt$setLevelDecreaseTicks(400);
            return 8;
        } else if (victim instanceof Player pl && pl.isUsingItem()) {
            ItemStack stack = pl.getUseItem();
            if (!stack.isEmpty()) {
                if (!pl.getCooldowns().isOnCooldown(stack.getItem())) {
                    pl.getCooldowns().addCooldown(stack.getItem(), 70);
                }
                pl.stopUsingItem();
            }
            return 9;
        } else if (!isMemortaken && victim instanceof Witch wt) {
            return 6;
        } else if (!isMemortaken && victim instanceof AbstractIllager al && !(al instanceof AnubisGuardian)) {
            return 5;
        } else if (victim instanceof Villager vg) {
            return 11;
        } else if (victim instanceof FlyingMob ph) {
            return 4;
        } else if (!isMemortaken && victim instanceof IronGolem ig) {
            if (ig.isPlayerCreated()){
                ig.setPlayerCreated(false);
                return 3;
            } else {
                ig.setPlayerCreated(true);
                return 2;
            }
        } if (!(victim instanceof NeutralMob nm && lastTarget ==null) &&
                (victim instanceof Monster || (victim instanceof Mob mb && lastTarget != null))){
            return 1;
        }
        return 0;
    }

    public boolean isRestoreType(int restype){

        return (restype == 3 || restype == 2 || restype == 4 || restype == 5 || restype == 6
                || restype == 7);
    }

    public boolean onCollide(Entity entity){
        if (!self.level().isClientSide()) {
            if (entity instanceof LivingEntity lv && self instanceof Player player) {
                if (self.level().getMaxLocalRawBrightness(BlockPos.containing(self.getEyePosition())) < 2) {
                    return false;
                }
                boolean release = false;
                UUID victimId = lv.getUUID();

                Inventory inv = player.getInventory();

                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack invStack = inv.getItem(i);

                    if (!(invStack.getItem() instanceof MemoryChessPieceItem)) {
                        continue;
                    }

                    CompoundTag tag = invStack.getTag();
                    if (tag != null &&
                            tag.hasUUID("victim") &&
                            victimId.equals(tag.getUUID("victim"))) {
                        int getKey = tag.getInt("stealType");

                        if (getKey != 14 && getKey != 15) {
                            if (getKey == 10 && entity instanceof LivingEntity mb) {
                                ((StandUser) mb).rdbt$setExperienceTaken(false);
                            } else if (getKey == 3 && entity instanceof IronGolem ig) {
                                ig.setPlayerCreated(true);
                            } else if (getKey == 2 && entity instanceof IronGolem ig) {
                                ig.setPlayerCreated(false);
                            }
                            if (entity instanceof Mob mb && isRestoreType(getKey)) {
                                ((IMob) mb).rdbt$setStolen(false);
                            }
                            if (entity instanceof Player pl && getKey == 8) {
                                ((IPlayerEntity) pl).rdbt$setLevelDecreaseTicks(0);
                            }
                            if (getKey == 11 && entity instanceof Villager vg &&
                                    tag.contains("StoredGossip")) {

                                ListTag gossipTag = tag.getList("StoredGossip", 10);
                                vg.getGossips().update(new Dynamic(NbtOps.INSTANCE, gossipTag));
                            }
                            release = true;
                            inv.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }

                if (release){
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.CHESS_PIECE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.00f + Math.random() * 0.03f));

                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.PINK_SMOKE,
                            this.getSelf().getX(), this.getSelf().getY() + 1, this.getSelf().getZ(),
                            5, 0.5, 0.5,0.5, 0.015);
                }
            }
        }


        return false;
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
    public void playFallBraceImpactParticles(){
        ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.WHITE_WOOL.defaultBlockState()),
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                50, 1.1, 0.05, 1.1, 0.4);
        ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.WHITE_WOOL.defaultBlockState()),
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                30, 1, 0.05, 1, 0.4);
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 80) {
                ((StandUser)self).roundabout$deeplyRemoveAttackTarget();
                if (self instanceof Mob mb){
                    ((IMob)mb).roundabout$setConfusionTicks(50);
                    ((ServerLevel) self.level()).sendParticles(ModParticles.QUESTION,
                            self.getEyePosition().x, self.getEyePosition().y+0.5F, self.getEyePosition().z,
                            0, 0, 0.5,0, 0.15);
                }
            }
        }
    }


    @Override
    public void onPoseEmoteSwitch(byte from, byte to){
        if (!self.level().isClientSide()){
            if (from == 35 && !(to == 35)){
                setCooldown(PowerIndex.SKILL_2,
                        ClientNetworking.getAppropriateConfig().californiaKingBedSettings.doNotHurtMeBaseCD);
            }
        }
    }

    void sendParticles(ServerPlayer sp){

        Vec3 lvec = self.getLookAngle();
        Position pn = self.getEyePosition().add(lvec.scale(3));
        Vec3 rev = lvec.reverse();
        ((ServerLevel) this.self.level()).sendParticles(ModParticles.MAGIC_HEART, pn.x(),
                pn.y(), pn.z(),
                0,
                rev.x, rev.y, rev.z,
                0.08);
    }
    public void saveBishop(){
        if (!onCooldown(PowerIndex.SKILL_3)){
            if (self instanceof ServerPlayer pl){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEART_SPARKLE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.20f + Math.random() * 0.03f));

                sendParticles(pl);
                ItemStack piece = ModItems.EXP_BISHOP.getDefaultInstance().copy();
                MainUtil.addItem(pl,piece);
                setCooldown(PowerIndex.SKILL_3,200);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void placeBedServer(){
        if (!onCooldown(PowerIndex.SKILL_4_SNEAK)){
            if (self instanceof ServerPlayer player){
                HitResult hit = player.pick(5.0D, 0.0F, false);

                if (hit instanceof BlockHitResult blockHit) {
                    Entity stand = getStandEntity(self);
                    if (stand instanceof CaliforniaKingBedEntity cbe) {
                        BlockState state = self.level().getBlockState(blockHit.getBlockPos());
                        if (state.isSolid() && !state.is(ModBlocks.KING_BED_BLOCK)){
                            BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());
                            Direction facing = player.getDirection();
                            UUID standUUID = cbe.getUUID();

                            if (placeKingBed(player, pos, facing,standUUID,cbe)) {
                                setCooldown(PowerIndex.SKILL_4_SNEAK, 40);
                                animateStand(CaliforniaKingBedEntity.SLEEP);

                                this.self.level().playSound(null, pos, ModSounds.CKB_PLACE_EVENT, SoundSource.PLAYERS, 1F, 1f);
                                cbe.bedBlockBind = pos;
                                cbe.setPos(pos.getCenter().subtract(0,0.5,0));
                                this.poseStand(OffsetIndex.LOOSE);
                            }
                        }
                    }
                }
            }
        }
    }
    public static boolean placeKingBed(ServerPlayer player, BlockPos pos, Direction facing, UUID standUUID, CaliforniaKingBedEntity ckb) {
        ServerLevel level = player.serverLevel();

        BlockState footState = ModBlocks.KING_BED_BLOCK
                .defaultBlockState()
                .setValue(BedBlock.FACING, facing)
                .setValue(BedBlock.PART, BedPart.FOOT)
                .setValue(BedBlock.OCCUPIED, false);

        BlockPos headPos = pos.relative(facing);

        BlockState headState = footState
                .setValue(BedBlock.PART, BedPart.HEAD);

        // Height limit
        if (!level.isInWorldBounds(pos) || !level.isInWorldBounds(headPos))
            return false;

        // Can the player build here?
        if (!player.mayUseItemAt(pos, facing, ItemStack.EMPTY))
            return false;

        if (!player.mayUseItemAt(headPos, facing, ItemStack.EMPTY))
            return false;

        // Both positions must be replaceable
        if (!level.getBlockState(pos).isAir())
            return false;

        if (!level.getBlockState(headPos).isAir())
            return false;

        // Can both halves survive?
        if (!footState.canSurvive(level, pos))
            return false;

        if (!headState.canSurvive(level, headPos))
            return false;

        if (!player.level().isUnobstructed(headState, headPos, CollisionContext.empty())){
            return false;
        }
        if (!player.level().isUnobstructed(footState, pos, CollisionContext.empty())){
            return false;
        }

        // Place the blocks
        level.setBlock(pos, footState, Block.UPDATE_ALL);
        level.setBlock(headPos, headState, Block.UPDATE_ALL);

        UUID bedUUID = UUID.randomUUID();
        ckb.bedUUID = bedUUID;

        if (level.getBlockEntity(pos) instanceof KingBedBlockEntity bed) {
            bed.setStandUUID(standUUID);
            bed.setBedUUID(bedUUID);
        }

        if (level.getBlockEntity(headPos) instanceof KingBedBlockEntity bed) {
            bed.setStandUUID(standUUID);
            bed.setBedUUID(bedUUID);
        }

        float yaw = footState.getValue(BedBlock.FACING).getOpposite().toYRot();

        ckb.setYRot(yaw);
        ckb.setYHeadRot(yaw);
        ckb.setYBodyRot(yaw);
        ckb.yRotO = yaw;
        ckb.yHeadRotO = yaw;
        ckb.yBodyRotO = yaw;
        ckb.setXRot(0.01F); // if desired

        ckb.yaw = yaw;
        ckb.setYawForce(yaw);

        // Vanilla callback
        footState.getBlock().setPlacedBy(level, pos, footState, player, ItemStack.EMPTY);

        level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);

        return true;
    }


    public boolean canPlaceBed(){
        return self.level().dimensionType().bedWorks();
    }
    public void saveLocation(){
        if (!onCooldown(PowerIndex.SKILL_2_SNEAK)){
            if (self instanceof ServerPlayer pl){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEART_SPARKLE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.20f + Math.random() * 0.03f));

                sendParticles(pl);
                ItemStack piece = getPieceType(self, false, false,14);
                MainUtil.addItem(pl,piece);
                setCooldown(PowerIndex.SKILL_2_SNEAK,400);
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
                num == PowerIndex.SKILL_1 ||
                num == PowerIndex.SKILL_3 ||
                num == PowerIndex.SKILL_EXTRA ||
                num == PowerIndex.SKILL_2_SNEAK ||
                num == PowerIndex.SKILL_4_SNEAK ||
                num == PowerIndex.SKILL_EXTRA_2) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    public void switchRules(){
        setCooldown(PowerIndex.SKILL_4,6);
        rewindSnap = null;
        hurtEntities.clear();
        clearAllSpawnedEntities();
        clearLeaded();
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
    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(CaliforniaKingBedEntity.PART_8_SKIN);
        $$1.add(CaliforniaKingBedEntity.SUNSHINE);
        $$1.add(CaliforniaKingBedEntity.EGYPT);
        $$1.add(CaliforniaKingBedEntity.SPOOKY);
        return $$1;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {

    }


    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.steal_memories",
                "instruction.roundabout.press_skill", StandIcons.STEAL_MEMORIES,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.steal_memories_2",
                "instruction.roundabout.press_skill_crouch", StandIcons.STEAL_MEMORIES_2,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.piece_attack",
                "instruction.roundabout.press_attack", StandIcons.PIECE_ATTACK,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.forbid_rule",
                "instruction.roundabout.press_skill", StandIcons.FORBID,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.hurt_rule",
                "instruction.roundabout.press_skill", StandIcons.HURT,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.leave_rule",
                "instruction.roundabout.press_skill", StandIcons.LEAVE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, 0, "ability.roundabout.save_location",
                "instruction.roundabout.press_skill_crouch", StandIcons.SAVE_LOCATION,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118, 0, "ability.roundabout.fall_brace",
                "instruction.roundabout.press_skill_falling", StandIcons.CALIFORNIA_FALL_CATCH,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.exp_bishop",
                "instruction.roundabout.press_skill_crouch", StandIcons.EXPERIENCE_BISHOP,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.rule_change",
                "instruction.roundabout.press_skill", StandIcons.LEAVE_RULE,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,0, "ability.roundabout.sleep",
                "instruction.roundabout.press_skill_crouch", StandIcons.SLEEP,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,0, "ability.roundabout.step_shadow",
                "instruction.roundabout.passive", StandIcons.STEP_SHADOW,0,level,bypas));
        return $$1;
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


    public List<Entity> spawnedEntities = new ArrayList<>();

    public void addSpawnedEntity(Entity che){
        spawnedEntityInit();
        spawnedEntities.add(che);
    }
    public void spawnedEntityInit(){
        if (spawnedEntities == null) {
            spawnedEntities = new ArrayList<>();
        }
    }
    public void clearAllSpawnedEntities(){
        spawnedEntityInit();

        List<Entity> hurricaneSpecial2 = new ArrayList<>(spawnedEntities) {
        };
        if (!spawnedEntities.isEmpty()) {
            for (Entity value : hurricaneSpecial2) {
                spawnedEntities.remove(value);
                if (value instanceof BlockWallEntity bwe){
                    bwe.breakAndDiscard();
                } else {
                    value.discard();
                }
            }
        }
    }

    public void tickSpawnedEntities(){
        spawnedEntityInit();

        List<Entity> hurricaneSpecial2 = new ArrayList<>(spawnedEntities) {
        };
        if (!spawnedEntities.isEmpty()) {
            for (Entity value : hurricaneSpecial2) {
                if (value.isRemoved() || !(value.isAlive())){
                    spawnedEntities.remove(value);
                }
            }
        }
    }
}
