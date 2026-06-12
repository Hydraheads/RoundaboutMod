package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.WhiteAlbumIceBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.ThrownWaterBottleEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
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
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

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
        return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.enableWhiteAlbum;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersWhiteAlbum(entity);
    }

    public boolean freezeImmune(){
        return hasStandActive(self) || super.freezeImmune();
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
    public void onLandingAnimatedJump(){
        if (hasSkatesActivated()){
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATING_LAND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
        }
    }

    @Override
    public float guardSpecialties(DamageSource sauce, float damage){
        if (sauce.is(DamageTypes.PLAYER_ATTACK)){
            damage*=0.44F;
        }
        if (sauce.is(ModDamageTypes.BULLET) ||
                sauce.getEntity() instanceof Blaze){
            damage*=0.5F;
        }
        return super.guardSpecialties(sauce,damage);
    }
    @Override
    public void onChangedBlock(BlockPos blockPos){
        if (hasSkatesActivated() && self.isSprinting() && !self.isSwimming() &&
        !self.isFallFlying()) {
            if (!self.onGround()) {
                return;
            }
            BlockState blockState = ModBlocks.WHITE_ALBUM_ICE_BLOCK.defaultBlockState();
            int j = Math.min(16, 2 + 1);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, -1, -j), blockPos.offset(j, -1, j))) {
                BlockState blockState3;
                if (!blockPos2.closerToCenterThan(self.position(), j)) continue;
                mutableBlockPos.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                BlockState blockState2 = self.level().getBlockState(mutableBlockPos);
                if (!blockState2.isAir() || (blockState3 = self.level().getBlockState(blockPos2)) != FrostedIceBlock.meltsInto()
                        || !blockState.canSurvive(self.level(), blockPos2) ||
                        !self.level().isUnobstructed(blockState, blockPos2, CollisionContext.empty())) continue;
                self.level().setBlockAndUpdate(blockPos2, blockState);
                self.level().scheduleTick(blockPos2, ModBlocks.WHITE_ALBUM_ICE_BLOCK, Mth.nextInt(self.getRandom(), 110, 130));
            }

            j = 2;
            blockState = ModBlocks.WHITE_ALBUM_ICE_SLAB.defaultBlockState();
            for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, 0, -j), blockPos.offset(j, 0, j))) {

                mutableBlockPos.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                BlockState blockState2 = self.level().getBlockState(mutableBlockPos);
                BlockState blockState3 =self.level().getBlockState(mutableBlockPos.below());
                if (!blockState.canSurvive(self.level(), blockPos2) ||
                        !self.level().isUnobstructed(blockState, blockPos2, CollisionContext.empty())) continue;
                if (blockState3.isAir() ||
                        (MainUtil.getIsGamemodeApproriateForGrief(self) &&
                                blockState3.canBeReplaced() &&
                                !(blockState3.getBlock() instanceof LiquidBlockContainer)
                                &&
                        !blockState3.liquid() &&
                        !(blockState3.hasProperty(BlockStateProperties.WATERLOGGED) &&
                                blockState3.getValue(BlockStateProperties.WATERLOGGED)
                                )
                        )
                ) {
                            self.level().setBlockAndUpdate(blockPos2, blockState);
                            self.level().scheduleTick(blockPos2, ModBlocks.WHITE_ALBUM_ICE_SLAB, Mth.nextInt(self.getRandom(), 110, 130));

                }
                }

            }
    }

    @Override
    public boolean surpassesFire(){
        if (((StandUser)self).roundabout$getStandPowers() instanceof
                PowersWhiteAlbum PWA &&
                !(((StandUser)self).roundabout$getGuardBroken())
                && hasStandActive(self)
        ){
            return true;
        }
        return false;
    }

    /**When you take damage, intercept or run code based off of it, or potentially cancel it*/
    public boolean interceptIncomingHarm(DamageSource $$0, float $$1){
        if (!self.level().isClientSide() && hasStandActive(self)) {
            StandUser user = getStandUserSelf();
            if (!user.roundabout$getGuardBroken()) {
                if ($$0.is(DamageTypes.FALL)) {
                    if ($$1 > ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumGuardPoints) {
                        user.roundabout$breakGuard();
                        this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                SoundSource.PLAYERS, 1F, 1.5F);
                        if (self instanceof Player player){
                            player.getCooldowns().addCooldown(Items.SHIELD, 100);
                        }
                    } else {
                        user.roundabout$damageGuard($$1);
                        if (!user.roundabout$getGuardBroken()) {
                            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_GUARD_SOUND_EVENT,
                                    SoundSource.PLAYERS, 0.8F, 1.6F + self.level().random.nextFloat() * 0.3f);
                        } else {
                            this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                    SoundSource.PLAYERS, 1F, 1.5F);
                            if (self instanceof Player player){
                                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                            }
                        }

                        return true;
                    }
                } else {
                    if ($$0.is(DamageTypes.CACTUS) ||
                            $$0.is(DamageTypes.STALAGMITE) ||
                            $$0.is(DamageTypes.SWEET_BERRY_BUSH) ||
                            $$0.is(DamageTypes.LAVA) ||
                            $$0.is(DamageTypes.IN_FIRE)||
                            $$0.is(DamageTypes.ON_FIRE)
                    ){
                        $$1*=0.05F;
                        if ($$1 > ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumGuardPoints) {
                            user.roundabout$breakGuard();
                            this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                    SoundSource.PLAYERS, 1F, 1.5F);
                            if (self instanceof Player player){
                                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                            }
                        } else {
                            user.roundabout$damageGuard($$1);
                            if (user.roundabout$getGuardBroken()) {
                                this.self.level().playSound(null, this.self.blockPosition(), SoundEvents.SHIELD_BREAK,
                                        SoundSource.PLAYERS, 1F, 1.5F);
                                if (self instanceof Player player){
                                    player.getCooldowns().addCooldown(Items.SHIELD, 100);
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void fixThis(){
        if (!self.level().isClientSide()) {
            if (hasSkatesActivated()) {
                if (getPlayerPos2() <= 0) {
                    if (acceleration >= getMaxAccelerationTicks()) {
                        setPlayerPos2(PlayerPosIndex.SKATE_TWIRL);
                    } else {
                        setPlayerPos2(PlayerPosIndex.SKATE_JUMP);
                    }
                    twirlTicks = 20;
                }
            }
        }
    }
    @Override
    public void onJump(){
        fixThis();
    }

    @Override
    public boolean cancelSprintJump(){
        if (hasSkatesActivated() && acceleration < getMaxAccelerationTicks()){
            return true;
        }
        return super.cancelSprintJump();
    }

    public int acceleration = 0;
    public float inputSpeedModifiers(float basis){
        if (hasSkatesActivated()){
            basis *= 1.3f+(acceleration*ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumAccelerationAmount);
        }
        return super.inputSpeedModifiers(basis);
    }

    public boolean isBlockingTraditionally() {
        if (this.self.isUsingItem() && !this.self.getUseItem().isEmpty()) {
            Item $$0 = this.self.getUseItem().getItem();
            if ($$0.getUseAnimation(this.self.getUseItem()) != UseAnim.BLOCK) {
                return false;
            } else {
                return $$0.getUseDuration(this.self.getUseItem()) - this.self.getUseItemRemainingTicks() >= 5;
            }
        } else {
            return false;
        }
    }

    /**for stands that subvert guard mechanics like white album*/
    @Override
    public boolean isSpecialGuarding(){
        return !isBlockingTraditionally() && hasStandActive(self);
    }

    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumGuardPoints;
    }

    @Override
    public float regenBrokenGuard(){
        return getStandUserSelf().roundabout$getMaxGuardPoints() / 200;
    }
    @Override
    public float regenGuard(){
        return getStandUserSelf().roundabout$getMaxGuardPoints() / 440;
    }

    @Override
    public boolean forceCrit(){
        return acceleration >= getMaxAccelerationTicks() || super.forceCrit();
    }

    private static final int MAX_ICE_SNAP_RADIUS = 30;
    int lastAcceleration = 0;
    double lastY = 0;
    @Override
    public void tickPower() {
        if (!self.level().isClientSide()) {
            if (cracked){
                if (!getStandUserSelf().roundabout$getGuardBroken()) {
                    cracked = false;
                    saveDiscAndSync();
                }
            } else {
                if (getStandUserSelf().roundabout$getGuardBroken()) {
                    cracked = true;
                    saveDiscAndSync();
                }
            }
            if (lastY < self.getY() && !self.onGround() && !self.isInWater() && !self.isSwimming()) {
                fixThis();
            }

            if (startIceSnapRing > 0 && self.tickCount % 3 == 0) {
                if (hasSkatesActivated()){
                    startIceSnapRing = 0;
                } else {

                    BlockPos center = iceCenter;
                    int r = startIceSnapRing;

                    // Check the square perimeter at radius r
                    processRing(center, r);

                    startIceSnapRing++;

                    if (startIceSnapRing > MAX_ICE_SNAP_RADIUS) {
                        startIceSnapRing = 0;
                    }
                }
            }
        }
        if (isPacketPlayer()){
            lastAcceleration = acceleration;
            if (hasSkatesActivated()){
                if (self.isInWater() || self.hurtTime > 0 || self.isUsingItem()
                || !self.isSprinting() || self.isSwimming()) {
                    acceleration = 0;
                } else if (!self.onGround()) {
                    if (lastY < self.getY()){
                        acceleration = 0;
                    } else {
                        acceleration = Math.min(getMaxAccelerationTicks(),acceleration+5);
                    }
                } else {
                    if (!self.horizontalCollision){
                        if (lastY < self.getY()){
                            acceleration = Math.max(0,acceleration-25);
                        } else {
                            acceleration = Math.min(getMaxAccelerationTicks(),acceleration+1);
                        }
                    } else {
                        acceleration = Math.max(0,acceleration-15);
                    }
                }

            } else {
                acceleration = 0;
            }
            if (acceleration != lastAcceleration){
                C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_WHITE_ALBUM_ACCELERATION,acceleration);
            }
        }
        if (self.onGround()){
            lastY = self.getY();
        }
        super.tickPower();
    }
    private void processRing(BlockPos center, int r) {

        Level level = self.level();

        for (int y = -4; y <= 4; y++) {

            for (int x = -r; x <= r; x++) {
                removeIce(level, center.offset(x, y, -r));
                removeIce(level, center.offset(x, y, r));
            }

            for (int z = -r + 1; z <= r - 1; z++) {
                removeIce(level, center.offset(-r, y, z));
                removeIce(level, center.offset(r, y, z));
            }
        }
    }
    private void removeIce(Level level, BlockPos pos) {

        BlockState state = level.getBlockState(pos);

        if (state.is(ModBlocks.WHITE_ALBUM_ICE_SLAB)) {

            level.destroyBlock(pos, false);
            // or:
            // level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        } else if (state.is(ModBlocks.WHITE_ALBUM_ICE_BLOCK)) {
            WhiteAlbumIceBlock.melt2(state,level,pos);
            // or:
            // level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    public static int getMaxAccelerationTicks(){
        return ClientNetworking.getAppropriateConfig().whiteAlbumSettings.whiteAlbumMaxAcceleration;
    }
    /**Add Knockback to attacks when appropriate. This replaces the knockback enchant if it is
     * higher than the knockback enchant*/
    public int bufferAttackKnockbackLevel(){
        if (hasSkatesActivated()) {
            if (acceleration >= getMaxAccelerationTicks()) {
                return 3;
            } else if (acceleration > getMaxAccelerationTicks() * 0.75) {
                return 2;
            } else if (acceleration > getMaxAccelerationTicks() * 0.5) {
                return 1;
            }
        }
        return 0;
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

    public ResourceLocation getIconYes(int slot){
        if (slot == 4 && acceleration >= getMaxAccelerationTicks())
            return StandIcons.SQUARE_GOLD;
        return super.getIconYes(slot);
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // code for advanced icons

        if (!isHoldingSneak()){
            setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT, PowerIndex.NO_CD);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.FREEZE_CANCEL, PowerIndex.SKILL_1_SNEAK);
        }

        if (!isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.TWISTER, PowerIndex.NO_CD);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.GENTLY_WEEPS, PowerIndex.NO_CD);
        }


        if (!isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.ICE_WALL, PowerIndex.SKILL_3);
        }

        if (!isHoldingSneak()){
            if (hasSkatesActivated()){
                setSkillIcon(context, x, y, 4, StandIcons.SKATE_ACTIVE, PowerIndex.SKILL_1);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.SKATE_INACTIVE, PowerIndex.SKILL_1);
            }
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


    public boolean cracked = false;
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putBoolean("skatesActive",skatesActive);
        $$0.putBoolean("cracked",cracked);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        if ($$0.contains("skatesActive")) {
            skatesActive = $$0.getBoolean("skatesActive");
        } if ($$0.contains("cracked")) {
            cracked = $$0.getBoolean("cracked");
        }
    }


    public void iceWallClient(){
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
            tryPowerPacket(PowerIndex.POWER_3);
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
            }
            case SKILL_1_CROUCH-> {
                iceCancelClient();
            }
            case SKILL_2_NORMAL-> {
                summonSurvivorClient();
            }
            case SKILL_2_CROUCH-> {
                despawnSurvivorClient();
            }
            case SKILL_3_NORMAL -> {
                dash();
            }
            case SKILL_3_CROUCH -> {
                iceWallClient();
            }
            case SKILL_4_NORMAL -> {
                toggleSkatesClient();
            }
            case SKILL_4_CROUCH -> {
            }
        }
    }

    public void iceCancelClient(){
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)){
            this.setCooldown(PowerIndex.SKILL_1, 40);
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, 40);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
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


    public int startIceSnapRing = 0;
    public BlockPos iceCenter = BlockPos.ZERO;
    public void iceCancelServer(){
        this.setCooldown(PowerIndex.SKILL_1, 40);
        if (!this.self.level().isClientSide()){
            if (hasSkatesActivated()) {
                toggleSkates();
            }
            iceCenter = self.blockPosition();
            startIceSnapRing = 1;

            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SNAP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
        }
    }


    public void iceWallServer(){
        int cooldown = 120;
        this.setCooldown(PowerIndex.SKILL_3, cooldown);
        if (!this.self.level().isClientSide()){
            BlockWallEntity fallingblockentity =
                    new BlockWallEntity(self.level(),
                            self.getX() + 2, self.getY(), self.getZ(),
                            Blocks.FROSTED_ICE.defaultBlockState());
            self.level().addFreshEntity(fallingblockentity);
           this.self.level().playSound(null, this.self.blockPosition(), ModSounds.ICE_RISES_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
        }
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
            case PowerIndex.POWER_3 -> {
                iceWallServer();
            }
            case PowerIndex.POWER_1_SNEAK -> {
                iceCancelServer();
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