package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class PowersWalkingHeart extends NewDashPreset {

    public PowersWalkingHeart(LivingEntity self) {
        super(self);
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.WALKING_HEART.create(this.getSelf().level());
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersWalkingHeart(entity);
    }

    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        $$1.add((byte) 3);
        $$1.add((byte) 4);
        return $$1;
    }

    @Override
    public byte getMaxLevel(){
        return 4;
    }


    @Override
    public boolean interceptSuccessfulDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if ((hasStandActive(this.getSelf()) && $$0.is(DamageTypes.PLAYER_ATTACK)) && hasExtendedHeelsForWalking()){
            addEXP(1);
        }

        return false;
    }

    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1) {
            amt = 200;
        } else if (currentLevel == 2){
            amt = 400;
        } else {
            amt = 800;
        }
        amt= (int) (amt*(getLevelMultiplier()));
        return amt;
    }
    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 4){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.skins").
                        withStyle(ChatFormatting.AQUA), true);
            } else if (level == 2 || level == 3){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.skins").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL, SKILL_1_CROUCH-> {
                spikeAttackModeToggleClient();
            }
            case SKILL_2_NORMAL, SKILL_2_CROUCH-> {
                extendHeels();
            }
            case SKILL_3_NORMAL, SKILL_3_CROUCH -> {
                dashOrWallLatch();
            }
            case SKILL_4_NORMAL, SKILL_4_CROUCH -> {
                unlockSpiderClient();
            }
        }
    }


    public void unlockSpiderClient(){
        if (this.getSelf() instanceof Player PE) {
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (!((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() && !bypass) {
                ItemStack stack = self.getMainHandItem();
                if (!stack.isEmpty() && stack.is(Items.COBWEB) && stack.getCount() >= 64){
                    tryPowerPacket(PowerIndex.POWER_3_BONUS);
                }
            }
        }
    }


    public void extendHeels(){
        if (!this.onCooldown(PowerIndex.SKILL_3) || hasExtendedHeelsForWalking()) {
            if (!inCombatMode()){
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
            tryPowerPacket(PowerIndex.POWER_2);
            }
        }
    }


    public void dashOrWallLatch(){
        if (inCombatMode())
            return;
        if (canLatchOntoWall())
            doWallLatchClient();
        else if (!hasExtendedHeelsForWalking())
            dash();
    }

    public void doWallLatchClient(){
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
            tryPowerPacket(PowerIndex.POWER_3);
        }
    }


    public Direction heelDirection = Direction.DOWN;

    public Direction getHeelDirection(){
        return heelDirection;
    }
    public void setHeelDirection(Direction dir){
        heelDirection = dir;
    }

    public boolean hasExtendedHeelsForWalking(){
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }
    public boolean canLatchOntoWall(){
        if ((this.self.onGround() && !hasExtendedHeelsForWalking()) || (!this.self.onGround() && hasExtendedHeelsForWalking()))
            return false;
        BlockPos pos1 = this.self.getOnPos();
        Direction gravdir = ((IGravityEntity)this.self).roundabout$getGravityDirection();

        if (this.self.level().getBlockState(pos1).isSolid()){
            pos1 = pos1.relative(gravdir.getOpposite());
        }
        Direction rd = RotationUtil.getRealFacingDirection(this.self);
        if (rd == gravdir)
            return false;
        pos1 = pos1.relative(RotationUtil.getRealFacingDirection(this.self));
        if (this.self.level().getBlockState(pos1).isSolid()){
            return true;
        }
        return false;
    }

    public void regularExtendHeels(){
        boolean isAnchored = hasExtendedHeelsForWalking();
            if (isAnchored){
                if (!this.self.level().isClientSide()) {
                    toggleSpikes(false);
                }
            } else {
                if (!inCombatMode()) {
                    if (self.onGround()) {
                        this.setCooldown(PowerIndex.SKILL_3, 8);
                        if (!this.self.level().isClientSide()) {
                            setHeelDirection(Direction.DOWN);
                            toggleSpikes(true);
                        }
                    }
                }
            }
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 80) {
                MainUtil.makeBleed(this.self,0,900,this.self);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 1F, 1);
                if (MainUtil.getMobBleed(this.self)){
                    MainUtil.makeMobBleed(this.self);
                }
            } if (effect.getDuration() == 50) {
                MainUtil.makeBleed(this.self,1,900,this.self);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 1F, 1);
                if (MainUtil.getMobBleed(this.self)){
                    MainUtil.makeMobBleed(this.self);
                }
            } if (effect.getDuration() == 20) {
                MainUtil.makeBleed(this.self,2,900,this.self);
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 1F, 1);
                if (MainUtil.getMobBleed(this.self)){
                    MainUtil.makeMobBleed(this.self);
                }
            }
        }
    }

    public void spikeAttackModeToggleClient(){
        if (!hasExtendedHeelsForWalking()) {
            if (!inCombatMode() && getShootTicks() > 0)
                return;
            this.tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);

            getStandUserSelf().roundabout$getStandPowers().tryPower(PowerIndex.NONE, true);
            tryPowerPacket(PowerIndex.NONE);
            ClientUtil.stopDestroyingBlock();
        }
    }

    public void wallLatch(){
        if (canLatchOntoWall()){
            this.setCooldown(PowerIndex.SKILL_3, 6);
            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.WALL_LATCH_EVENT, SoundSource.PLAYERS, 1F, 1f);
                toggleSpikes(true);
                Direction gd = RotationUtil.getRealFacingDirection(this.self);
                setHeelDirection(gd);
                ((IGravityEntity) this.self).roundabout$setGravityDirection(gd);
                justFlippedTicks = 7;
            }
        }
    }

    public void grantFallImmunity(){
        if (ClientNetworking.getAppropriateConfig().walkingHeartSettings.fallProtectionOnRelease) {
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
        }
    }

    public void toggleSpikes(boolean toggle){
        if (!this.self.level().isClientSide()) {
            boolean getTog = getStandUserSelf().roundabout$getUniqueStandModeToggle();
            if (toggle != getTog) {
                if (toggle) {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXTEND_SPIKES_EVENT, SoundSource.PLAYERS, 1F, 1f);
                } else {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXTEND_SPIKES_EVENT, SoundSource.PLAYERS, 1F, 1.5F);
                }
            }
        }
        getStandUserSelf().roundabout$setUniqueStandModeToggle(toggle);
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.SPIKE_ATTACK_MODE, PowerIndex.SKILL_1);

        if (hasExtendedHeelsForWalking())
            setSkillIcon(context, x, y, 2, StandIcons.GROUND_IMPLANT, PowerIndex.SKILL_2);
        else
            setSkillIcon(context, x, y, 2, StandIcons.GROUND_IMPLANT_OUT, PowerIndex.SKILL_2);
        if (canLatchOntoWall() || hasExtendedHeelsForWalking())
            setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK, PowerIndex.SKILL_3);
        else
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        if (this.getSelf() instanceof Player PE) {
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (!((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() && !bypass) {
                ItemStack stack = self.getMainHandItem();
                if (!stack.isEmpty() && stack.is(Items.COBWEB) && stack.getCount() >= 64){
                    setSkillIcon(context, x, y, 4, StandIcons.SPIDER_SKIN, PowerIndex.NONE);
                }
            }
        }
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 3 && hasExtendedHeelsForWalking() && !canLatchOntoWall())
            return true;
        if (slot == 1 && hasExtendedHeelsForWalking())
            return true;
        if (slot == 1 && isBlockedByStone())
            return true;
        if ((slot == 2 || slot == 3) && inCombatMode())
            return true;
        return super.isAttackIneptVisually(activeP, slot);
    }


    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    public SoundEvent getSoundFromByte(byte soundChoice){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_WALKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public float inputSpeedModifiers(float basis){
        if (inCombatMode()) {
            return 0;
        }
        return super.inputSpeedModifiers(basis);
    }
    public boolean inCombatMode(){
        return getStandUserSelf().roundabout$getCombatMode();
    }

    @Override
    public boolean cancelJump(){
        return inCombatMode();
    }
    @Override
    public boolean cancelSprint(){
        return inCombatMode();
    }

    public void switchModes(){
        if (getStandUserSelf().roundabout$getCombatMode()){
            getStandUserSelf().roundabout$setCombatMode(false);
            if (!this.self.level().isClientSide()){
                this.self.level().playSound(null, self.getX(), self.getY(),
                        self.getZ(), ModSounds.HEEL_STOMP_EVENT, self.getSoundSource(), 1F, 1.0F);
            }
        } else {
            if (hasExtendedHeelsForWalking() || isBlockedByStone())
                return;

            this.self.setSprinting(false);
            getStandUserSelf().roundabout$setCombatMode(true);
            if (!this.self.level().isClientSide()){
                this.self.level().playSound(null, self.getX(), self.getY(),
                        self.getZ(), ModSounds.HEEL_RAISE_EVENT, self.getSoundSource(), 1F, 1.0F);
            }
        }
    }


    public int shootTicks = 0;
    public int getMaxShootTicks(){
        if (self instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 3 || bypass) {
                return 10000;
            }
            if (Level > 2) {
                return 10000-getUseTicks();
            }if (Level > 1) {
                return 10000-getUseTicks()-getUseTicks();
            }
            return 10000-getUseTicks()-getUseTicks()-getUseTicks()-getUseTicks();
        }
        return 10000;
    }
    public int getShootTicks(){
        return shootTicks;
    }
    public void setShootTicks(int shootTicks){
        this.shootTicks = Mth.clamp(shootTicks,0,getMaxShootTicks());

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

    @Override
    public boolean tryPower(int move, boolean forced) {
        switch (move)
        {
            case PowerIndex.POWER_2 -> {
                regularExtendHeels();
            }
            case PowerIndex.POWER_3 -> {
                wallLatch();
            }
            case PowerIndex.POWER_4 -> {
                switchModes();
            }
            case PowerIndex.POWER_4_EXTRA -> {
                useSpikeAttack();
            }
            case PowerIndex.POWER_4_SNEAK_EXTRA -> {
                useSpikeAttack2();
            }
            case PowerIndex.POWER_1_BONUS -> {
                missSound();
            }
            case PowerIndex.POWER_1_SNEAK -> {
                hitSound();
            }

            case PowerIndex.POWER_3_BONUS-> {
                spiderUnlock();
            }

        }
        return super.tryPower(move,forced);
    }

    public void spiderUnlock(){
        if (this.getSelf() instanceof Player PE) {
            Level lv = this.getSelf().level();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            StandUser user = ((StandUser)PE);
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (!((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() && !bypass) {
                ItemStack stack = self.getMainHandItem();
                if (!stack.isEmpty() && stack.is(Items.COBWEB) && stack.getCount() >= 64){
                    if (!lv.isClientSide()) {
                        IPlayerEntity ipe = ((IPlayerEntity) PE);
                        ipe.roundabout$setUnlockedBonusSkin(true);
                        lv.playSound(null, PE.getX(), PE.getY(),
                                PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                        ((ServerLevel) lv).sendParticles(ParticleTypes.END_ROD, PE.getX(),
                                PE.getY()+PE.getEyeHeight(), PE.getZ(),
                                10, 0.5, 0.5, 0.5, 0.2);
                        user.roundabout$setStandSkin(WalkingHeartEntity.SPIDER_SKIN);
                        ((ServerPlayer) ipe).displayClientMessage(
                                Component.translatable("unlock_skin.roundabout.walking_heart.spider"), true);
                        user.roundabout$summonStand(lv, true, false);
                        stack.shrink(64);
                    }
                }
            }
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.POWER_1_BLOCK) {
            Entity ent = self.level().getEntity(chargeTime);
            if (ent != null){

                HeelSpikeDamageEntityAttack(ent,getSpikeDamage(ent),0.5F,ent,false);
                return true;
            }
        } if (move == PowerIndex.POWER_2_BLOCK) {
            Entity ent = self.level().getEntity(chargeTime);
            if (ent != null){

                HeelSpikeDamageEntityAttack(ent,getSpikeDamage(ent),0.7F,ent,true);
                return true;
            }
        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    public float getSpikeDamage(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 2.1F* (ClientNetworking.getAppropriateConfig().
                    walkingHeartSettings.walkingHeartAttackMultOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 3* (ClientNetworking.getAppropriateConfig().
                    walkingHeartSettings.walkingHeartAttackMultOnMobs*0.01)));
        }
    }

    public boolean HeelSpikeDamageEntityAttack(Entity target, float pow, float knockbackStrength, Entity attacker, boolean rightClick){
        if (attacker instanceof TamableAnimal TA){
            if (target instanceof TamableAnimal TT && TT.getOwner() != null
                    && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())){
                return false;
            }
        } else if (attacker instanceof AbstractVillager){
            if (target instanceof AbstractVillager){
                return false;
            }
        }
        if (DamageHandler.HeelSpikeStandDamageEntity(target,pow, attacker)){
            if (attacker instanceof LivingEntity LE){
                LE.setLastHurtMob(target);
            }
            if (target instanceof LivingEntity LE) {
                addEXP(3,LE);
                float mod = -1;
                if (rightClick){
                    mod = 1;
                }
                LE.knockback(knockbackStrength * 0.5f, mod*Mth.sin(this.self.getYRot() * ((float) Math.PI / 180)), mod*-Mth.cos(this.self.getYRot() * ((float) Math.PI / 180)));

                MainUtil.makeBleed(LE,0,300,this.self);
            }
            return true;
        } else {
            if (target instanceof LivingEntity) {
                float mod = -1;
                if (rightClick){
                    mod = 1;
                }
                ((LivingEntity) target).knockback(knockbackStrength * 0.5f, mod*Mth.sin(this.self.getYRot() * ((float) Math.PI / 180)), mod*-Mth.cos(this.self.getYRot() * ((float) Math.PI / 180)));
            }
        }
        return false;
    }

    public final void sendHeelPacket(double range) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, range)) {
                    S2CPacketUtil.heelExtend(serverPlayerEntity,self.getId());
                }
            }
        }
    }

    public void hitSound(){
        sendHeelPacket(90);
        this.self.level().playSound(null, this.self.blockPosition(),
                ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
    }
    public void missSound(){
        sendHeelPacket(90);
        this.self.level().playSound(null, this.self.blockPosition(),
                ModSounds.SPIKE_MISS_EVENT, SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
    }

    public boolean replaceHudActively(){
        return inCombatMode() || getShootTicks() > 0;
    }
    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight, int x){
        StandHudRender.renderWalkingHeartHud(context,cameraPlayer,screenWidth,screenHeight,x);
    }

    public void useSpikeAttackF(boolean rightClick){
        this.setCooldown(PowerIndex.SKILL_4, 3);
        this.setAttackTimeDuring(-10);
        this.setActivePower(PowerIndex.POWER_4_EXTRA);

        if (this.self.level().isClientSide()){
            List<Entity> TE = this.getTargetEntityListThroughWalls(self, 7F,10);
            if (TE == null || TE.isEmpty()){
                tryPowerPacket(PowerIndex.POWER_1_BONUS);
            } else {
                tryPowerPacket(PowerIndex.POWER_1_SNEAK);
                for (Entity value : TE) {
                    if (rightClick){
                        tryIntPowerPacket(PowerIndex.POWER_2_BLOCK, value.getId());
                    } else {
                        tryIntPowerPacket(PowerIndex.POWER_1_BLOCK, value.getId());
                    }
                }
            }
        }
    }

    public void useSpikeAttack2(){
        useSpikeAttackF(true);
    }

    public boolean isBlockedByStone(){
        byte curse = ((StandUser)self).roundabout$getLocacacaCurse();
        if (self instanceof Player PE) {
            if (curse == LocacacaCurseIndex.RIGHT_LEG && PE.getMainArm() == HumanoidArm.RIGHT)
                return true;
            if (curse == LocacacaCurseIndex.LEFT_LEG && PE.getMainArm() == HumanoidArm.LEFT)
                return true;
        }

            return false;
    }

    public void useSpikeAttack(){
        useSpikeAttackF(false);
    }

    public void tickPower() {
        setHeelExtension(getHeelExtension()-1);
        if (this.self.level().isClientSide()) {
            if (!inCombatMode()){
                currentKickTicks = 0;
            } else if (currentKickTicks < chargeKickTicks()){
                currentKickTicks++;
            }

            if (hasExtendedHeelsForWalking() && !getStandUserSelf().rdbt$getJumping()){
                if (!self.onGround()) {
                    if (this.self.getDeltaMovement().y < 0){
                        this.self.setDeltaMovement(this.self.getDeltaMovement().add(0,-0.14,0));
                    }
                }
            }
        } else {

            if (inCombatMode() && isBlockedByStone()){
                switchModes();
            }

            if (hasExtendedHeelsForWalking()){
                if (justFlippedTicks > 0){
                    justFlippedTicks--;
                } else {
                    if (self.onGround()){
                        mercyTicks = 5;
                    } else {
                        Vec3 newVec = new Vec3(0,-0.2,0);
                        Vec3 newVec2 = new Vec3(0,-1.0,0);
                        Vec3 newVec4 = new Vec3(0,-0.5,0);
                        Vec3 newVec5 = new Vec3(0,-1.1,0);
                        newVec = RotationUtil.vecPlayerToWorld(newVec,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos = BlockPos.containing(self.getPosition(1).add(newVec));
                        newVec2 = RotationUtil.vecPlayerToWorld(newVec2,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos2 = BlockPos.containing(self.getPosition(1).add(newVec2));
                        newVec4 = RotationUtil.vecPlayerToWorld(newVec4,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos4 = BlockPos.containing(self.getPosition(1).add(newVec4));
                        newVec5 = RotationUtil.vecPlayerToWorld(newVec5,((IGravityEntity)self).roundabout$getGravityDirection());
                        BlockPos pos5 = BlockPos.containing(self.getPosition(1).add(newVec5));
                        if (
                                (
                                        self.level().getBlockState(pos).isSolid()
                                                || self.level().getBlockState(pos2).isSolid()
                                                || self.level().getBlockState(pos4).isSolid()
                                                || self.level().getBlockState(pos5).isSolid()
                                )){
                            mercyTicks--;
                        } else {
                            mercyTicks = 0;
                        }
                    }
                    if (self.isSleeping() || (!self.onGround() && mercyTicks <= 0) || self.getRootVehicle() != this.self) {
                        heelDirection = Direction.DOWN;
                        if (((IGravityEntity) this.self).roundabout$getGravityDirection() != heelDirection){
                            grantFallImmunity();
                        }
                        toggleSpikes(false);
                        ((IGravityEntity) this.self).roundabout$setGravityDirection(heelDirection);
                        setHeelDirection(heelDirection);
                    }
                }

            } else {
                setHeelDirection(Direction.DOWN);
            }
        }

        if (this.self instanceof Player PE && PE.isCreative()) {
            setShootTicks(0);
        } else {
            if (inCombatMode()){
                if (getShootTicks() < getMaxShootTicks()) {
                    setShootTicks(getShootTicks() + getRaiseTicks());
                }
            } else {
                if (getPauseGrowthTicks() > 0) {
                    pauseGrowthTicks -= 1;
                } else {
                    if (getShootTicks() > 0) {
                        setShootTicks(getShootTicks() - getLowerTicks());
                    }
                }
            }
        }

        super.tickPower();
    }

    public int getRaiseTicks(){
        return 40;
    }
    public int pauseTicks(){
        return 60;
    }

    public int heelExtension = 0;

    public int getHeelExtension(){
        return heelExtension;
    }
    public void setHeelExtension(int extNum){
        heelExtension = Mth.clamp(extNum,0,1000);
    }

    public int getLowerTicks(){
        return 60;
    }

    public boolean canShootSpikes(int useTicks){
        if ((shootTicks+useTicks) <= getMaxShootTicks()){
            return true;
        }
        return false;
    }

    public int getUseTicks(){
        return 1300;
    }

    @Override
    public boolean interceptGuard(){
        return inCombatMode();
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        buttonInputSpike(keyIsDown,options,true);
        return true;
    }


    public boolean holdDownClick = false;
    public boolean consumeClickInput = false;
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        buttonInputSpike(keyIsDown,options,false);
    }
    public void buttonInputSpike(boolean keyIsDown, Options options, boolean rightClick) {
        if (currentKickTicks < chargeKickTicks())
            return;
        if (!consumeClickInput) {
            if (holdDownClick) {
                if (!keyIsDown) {
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    if (inCombatMode()){
                        if (!holdDownClick){
                            if (!this.onCooldown(PowerIndex.SKILL_4) && ((getActivePower() == PowerIndex.NONE)
                                    || getActivePower() == PowerIndex.POWER_4_EXTRA)) {
                                if (confirmShot(getUseTicks())) {
                                    if (this.self instanceof Player PE){
                                        IPlayerEntity ipe = ((IPlayerEntity)PE);
                                        ipe.roundabout$getBubbleShotAim().stop();
                                        ipe.roundabout$setBubbleShotAimPoints(10);
                                    }
                                    if (rightClick){
                                        this.tryPower(PowerIndex.POWER_4_SNEAK_EXTRA, true);
                                    } else {
                                        this.tryPower(PowerIndex.POWER_4_EXTRA, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    public boolean confirmShot(int useTicks){
        if (canShootSpikes(useTicks)){
            pauseGrowthTicks = pauseTicks();
            setShootTicks((shootTicks+useTicks));
            return true;
        }
        return false;
    }

    public int pauseGrowthTicks = 0;
    public int getPauseGrowthTicks(){
        return pauseGrowthTicks;
    }

    public int justFlippedTicks = 0;
    public int mercyTicks = 0;

    @Override
    public void updateUniqueMoves() {

        super.updateUniqueMoves();
    }

    public static final byte VISAGE_NOISE = 104;
    public static final byte IMPALE_NOISE = 105;
    public boolean deface(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_2);
            playStandUserOnlySoundsIfNearby(IMPALE_NOISE, 27, false,false);
            this.animateStand(CinderellaEntity.DEFACE);
            this.poseStand(OffsetIndex.ATTACK);

            return true;
        }
        return false;
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.spike_attack_mode",
                "instruction.roundabout.press_skill", StandIcons.SPIKE_ATTACK_MODE, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.heel_plant",
                "instruction.roundabout.press_skill", StandIcons.GROUND_IMPLANT,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.wall_walk_move",
                "instruction.roundabout.press_skill", StandIcons.WALL_WALK,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.firm_swing",
                "instruction.roundabout.passive", StandIcons.FIRM_SWING,0,level,bypass));
        return $$1;
    }
    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(WalkingHeartEntity.MANGA_SKIN);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass) {
                $$1.add(WalkingHeartEntity.MODEL_SKIN);
                $$1.add(WalkingHeartEntity.PURPLE_SKIN);
            }if (Level > 2 || bypass) {
                $$1.add(WalkingHeartEntity.VALENTINE_SKIN);
                $$1.add(WalkingHeartEntity.VERDANT_SKIN);
            }if (Level > 3 || bypass) {
                $$1.add(WalkingHeartEntity.PALE_SKIN);
                $$1.add(WalkingHeartEntity.GOTHIC_SKIN);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(WalkingHeartEntity.SPIDER_SKIN);
            }
        }
        return $$1;
    }


    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == WalkingHeartEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.base");
        } else if (skinId == WalkingHeartEntity.MODEL_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.model");
        } else if (skinId == WalkingHeartEntity.PURPLE_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.purple");
        } else if (skinId == WalkingHeartEntity.VERDANT_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.verdant");
        } else if (skinId == WalkingHeartEntity.PALE_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.pale");
        } else if (skinId == WalkingHeartEntity.VALENTINE_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.valentine");
        } else if (skinId == WalkingHeartEntity.GOTHIC_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.gothic");
        } else if (skinId == WalkingHeartEntity.SPIDER_SKIN){
            return Component.translatable(  "skins.roundabout.walking_heart.spider");
        }
        return Component.translatable(  "skins.roundabout.walking_heart.base");
    }


    public static int chargeKickTicks(){
        return 8;
    }
    public int currentKickTicks = 0;

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {;
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (inCombatMode()) {
            Entity TE = this.getTargetEntityThroughWalls(playerEntity, 7F,10);
            float finalATime = (float) currentKickTicks / chargeKickTicks();
            int barTexture = 0;
            if (TE != null) {
                barTexture = 18;
            }
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            int finalATimeInt = Math.round(finalATime * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);
            if (TE != null) {
            }
        }
    }

}
