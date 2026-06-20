package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.block.WhiteAlbumIceBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.joml.Vector3f;

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

    public static final byte ICE_CHARGE = 80;

    /**returns if you are using stand guard*/
    public boolean isGuardInput(){
        return this.activePower == PowerIndex.EXTRA;
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
    public boolean isBrawling(){
        return fistsOut;
    }
    @Override
    public boolean interceptAttack(){
        return fistsOut;
    }

    @Override
    /**If the standard right click input should usually be canceled while your stand is active*/
    public boolean interceptGuard(){
        return fistsOut;
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
        if (hasSkatesActivated() && acceleration > 0 && !self.isSwimming() &&
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
                                    !(blockState3.getBlock() instanceof LiquidBlockContainer)&&
                                    !(blockState3.getBlock() instanceof FireBlock)&&
                                    !(blockState3.getBlock() instanceof StandFireBlock)
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
        //Roundabout.LOGGER.info("2");
        if (!self.level().isClientSide()) {
            //Roundabout.LOGGER.info("3");
            if (hasSkatesActivated()) {
                //Roundabout.LOGGER.info("4");
                if (acceleration >= getMaxAccelerationTicks()) {
                    //Roundabout.LOGGER.info("5");
                    setPlayerPos(PlayerPosIndex.SKATE_TWIRL);
                    twirlTicks = 20;
                } else {
                    //Roundabout.LOGGER.info("6");
                    if (getPlayerPos() != PlayerPosIndex.SKATE_TWIRL) {
                        //Roundabout.LOGGER.info("7");
                        setPlayerPos(PlayerPosIndex.SKATE_JUMP);
                    }
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

    public boolean canGuard(){
        return !this.isBarraging() && isBrawling();
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && canGuard()) {
            ((StandUser)this.getSelf()).roundabout$tryPowerP(PowerIndex.EXTRA,true);
            tryPowerPacket(PowerIndex.EXTRA);
            return true;
        }
        return false;
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
        return !isBlockingTraditionally() && hasStandActive(self) && !isBarraging();
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
            if (hasSkatesActivated() && self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vf &&
                    vf.isPlantedInWall()){
                toggleSkates();
            }

            ticklIceEntities();
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

    public void setAcceleration(int num){
        byte pos = getPlayerPos();
        acceleration = num;
        if (num > 0){
            if (pos != PlayerPosIndex.SKATE_JUMP &&
                    pos != PlayerPosIndex.SKATE_TWIRL){
                setPlayerPos(PlayerPosIndex.SKATE_GENERAL);
            }
        } else {
            if (pos == PlayerPosIndex.SKATE_GENERAL){
                setPlayerPos(PlayerPosIndex.NONE);
            }
        }
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
            setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.FREEZE_CANCEL, PowerIndex.SKILL_1_SNEAK);
        }

        if (!isHoldingSneak()){
            setSkillIcon(context, x, y, 2, StandIcons.TWISTER, PowerIndex.NO_CD);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.GENTLY_WEEPS, PowerIndex.NO_CD);
        }


        if (!isHoldingSneak()){
            if (hasSkatesActivated()){
                setSkillIcon(context, x, y, 3, StandIcons.ICE_WALL_BEHIND, PowerIndex.SKILL_3);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
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
            setSkillIcon(context, x, y, 4, StandIcons.FREEZE_BLOCKS, PowerIndex.SKILL_4_SNEAK);
        }

        super.renderIcons(context, x, y);
    }


    public void toggleBlockFreezeClient(){
        if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK) && !self.isInWater()) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_4_SNEAK);
        }
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
    public boolean fistsOut = false;
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putBoolean("skatesActive",skatesActive);
        $$0.putBoolean("cracked",cracked);
        $$0.putBoolean("fistsOut",fistsOut);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        if ($$0.contains("skatesActive")) {
            skatesActive = $$0.getBoolean("skatesActive");
        } if ($$0.contains("cracked")) {
            cracked = $$0.getBoolean("cracked");
        } if ($$0.contains("fistsOut")) {
            fistsOut = $$0.getBoolean("fistsOut");
        }
    }


    public void iceWallClient(){
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            if (canUseIceWall()) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
                tryPowerPacket(PowerIndex.POWER_3);
            }
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
                toggleFistsClient();
            }
            case SKILL_1_CROUCH-> {
                iceCancelClient();
            }
            case SKILL_3_NORMAL -> {
                dashOrWall();
            }
            case SKILL_3_CROUCH -> {
                iceWallClient();
            }
            case SKILL_4_NORMAL -> {
                toggleSkatesClient();
            }
            case SKILL_4_CROUCH -> {
                toggleBlockFreezeClient();
            }
        }
    }

    public void dashOrWall(){
        if (hasSkatesActivated()){
            if (!this.onCooldown(PowerIndex.SKILL_3)) {
                if (canUseIceWall()) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
                    tryPowerPacket(PowerIndex.POWER_3_BLOCK);
                }
            }
        } else {
            dash();
        }
    }

    public void toggleFistsClient(){
        if (!onCooldown(PowerIndex.SKILL_4)){
            this.setCooldown(PowerIndex.SKILL_4, 9);
            tryPowerPacket(PowerIndex.POWER_1_BONUS);
        }
    }

    public void iceCancelClient(){
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)){
            this.setCooldown(PowerIndex.SKILL_1, 40);
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, 40);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
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
            clearAllIceEntities();
            iceCenter = self.blockPosition();
            startIceSnapRing = 1;

            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SNAP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
        }
    }



    public HitResult pick(double $$0, float $$1, boolean $$2) {
        Vec3 $$3 = this.self.getEyePosition($$1);
        Vec3 $$4 = this.self.getViewVector($$1);
        Vec3 $$5 = $$3.add($$4.x * $$0, $$4.y * $$0, $$4.z * $$0);
        return this.self.level().clip(new ClipContext($$3, $$5, ClipContext.Block.COLLIDER, $$2 ? net.minecraft.world.level.ClipContext.Fluid.ANY : net.minecraft.world.level.ClipContext.Fluid.NONE, self));
    }

    public boolean canUseIceWall(){

        HitResult hit = pick(2, 0.0F, false);

        BlockPos centerPos;

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;

            // Spawn on top of the block you are looking at
            centerPos = blockHit.getBlockPos().relative(blockHit.getDirection());
        } else {
            // Fallback: 2 blocks in front of player
            centerPos = self.blockPosition().relative(self.getDirection(), 2);
        }

        for (var i = 0; i < 5; i++) {
            if (self.level().getBlockState(centerPos.below(i)).isSolid()){
                return true;
            }
        }
        return false;
    }


    public int placePhase = 0;
    BlockPos storeCenter = BlockPos.ZERO;
    Direction sideX = Direction.UP;
    public void iceWallServer(boolean special){
        int cooldown = 110;
        this.setCooldown(PowerIndex.SKILL_3, cooldown);
        if (!this.self.level().isClientSide()){

            BlockPos centerPos;
            Direction facing = self.getDirection();

            if (special){
                centerPos = self.getOnPos().relative(facing.getOpposite()).relative(facing.getOpposite());
            } else {

                HitResult hit = pick(2, 0.0F, false);
                if (hit.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHit = (BlockHitResult) hit;

                    // Spawn on top of the block you are looking at
                    centerPos = blockHit.getBlockPos().relative(blockHit.getDirection());
                } else {
                    // Fallback: 2 blocks in front of player
                    centerPos = self.blockPosition().relative(self.getDirection(), 2);
                }
            }

            boolean isSafe = false;
            for (var i = 0; i < 5; i++) {
                if (self.level().getBlockState(centerPos.below(i)).isSolid()){
                    centerPos = centerPos.below(i);
                    isSafe = true;
                    i = 10;
                }
            }

            if (isSafe) {
                centerPos = new BlockPos(centerPos.getX(), Math.min(centerPos.getY(), self.blockPosition().getY()),
                        centerPos.getZ());

// Left/right axis
                Direction side = facing.getClockWise();
                sideX = side;
                storeCenter = centerPos;
                for (int width = -1; width <= 1; width++) {
                    for (int height = 0; height < 2; height++) {

                        BlockPos spawnPos = centerPos
                                .relative(side, width)
                                .above(height);

                        Vector3f newVec = new Vector3f((float) (spawnPos.getX()+0.5),
                                (float) (spawnPos.getY()),
                                (float) (spawnPos.getZ() + 0.5)).add(0, -1, 0);

                        BlockWallEntity wall =
                                // slightly off to not z-fight
                                new BlockWallEntity(
                                        self.level(),
                                        newVec.x,
                                        newVec.y,
                                        newVec.z,
                                        Blocks.PACKED_ICE.defaultBlockState()
                                );
                        wall.setDataFinalPos(newVec.add(0, 2, 0));
                        wall.timing = 200;
                        wall.canGrief = MainUtil.getIsGamemodeApproriateForGrief(self);
                        addIceEntity(wall);
                        self.level().addFreshEntity(wall);
                    }
                }
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.ICE_RISES_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
            }
        }
    }

    public void toggleFists(){
        int cooldown = 9;
        this.setCooldown(PowerIndex.SKILL_4, cooldown);
        if (!this.self.level().isClientSide()){
            fistsOut = !fistsOut;
            if (fistsOut){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEEL_RAISE_EVENT, SoundSource.PLAYERS, 0.9F, (float) (1.02 + (Math.random() * 0.06)));
            } else {
                //this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEEL_RAISE_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
            }
            saveDiscAndSync();
        }
    }

    public boolean toggleSkates(){
        int cooldown = 5;
        this.setCooldown(PowerIndex.SKILL_1, cooldown);
        if (!this.self.level().isClientSide() && this.self instanceof Player PL){
            if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vf &&
                    vf.isPlantedInWall()) {
                if (skatesActive) {
                    skatesActive = false;
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_EQUIP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
                    saveDiscAndSync();
                }
            } else {
                skatesActive = !skatesActive;
                if (skatesActive) {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_EQUIP_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
                } else {
                    this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SKATE_RETRACT_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
                }
                saveDiscAndSync();
            }
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

        return tryPower(move, forced);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move)
        {
            case PowerIndex.POWER_1 -> {
                return toggleSkates();
            }
            case PowerIndex.POWER_1_BONUS -> {
                toggleFists();
            }
            case PowerIndex.POWER_3 -> {
                iceWallServer(false);
            }
            case PowerIndex.EXTRA -> {
                setPowerExtraGuard();
            }
            case PowerIndex.POWER_3_BLOCK -> {
                iceWallServer(true);
            }
            case PowerIndex.POWER_1_SNEAK -> {
                iceCancelServer();
            }
            case PowerIndex.POWER_4_SNEAK -> {
                freezeBlocksServer();
            }
        }
        return super.setPowerOther(move,lastMove);
    }


    public boolean setPowerExtraGuard() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.EXTRA);
        if (!self.level().isClientSide()) {
            if (getPlayerPos2() != PlayerPosIndex.CHARGE_SHOT) {
                setPlayerPos2(PlayerPosIndex.CHARGE_SHOT);
            }
        }
        return true;
    }


    public void freezeBlocksServer(){
        int cooldown = 240;
        this.setCooldown(PowerIndex.SKILL_4_SNEAK, cooldown);
        if (!this.self.level().isClientSide() && this.self instanceof Player PL && !PL.isInWater()){
            if (MainUtil.getIsGamemodeApproriateForGrief(PL)){
                int radius = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.blockFreezeRadius;
                BlockPos center = self.blockPosition();
                boolean canFreezeWater = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.freezesSurfaceWater;

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {

                            // Compare squared distances (faster than sqrt)
                            if (x * x + y * y + z * z > radius * radius) {
                                continue;
                            }

                            BlockPos pos = center.offset(x, y, z);

                            BlockState state = self.level().getBlockState(pos);

                            if (canFreezeWater && state.is(Blocks.WATER) && self.level().getBlockState(pos.above()).isAir()){
                                if (self.level().isUnobstructed(Blocks.ICE.defaultBlockState(), pos, CollisionContext.empty())) {
                                    if (self.level().isUnobstructed(Blocks.ICE.defaultBlockState(),
                                            pos.above(), CollisionContext.empty())) {
                                            if (!(self instanceof Player pl && !MainUtil.canPlaceOnClaim(pl, pos))) {
                                                self.level().setBlock(
                                                        pos,
                                                        Blocks.ICE.defaultBlockState(),
                                                        Block.UPDATE_ALL
                                                );
                                            }

                                    }
                                }
                            } else {
                                Block replacement = MainUtil.FREEZABLE_BLOCKS.get(state.getBlock());

                                if (replacement != null) {
                                    if (!(self instanceof Player pl && !MainUtil.canPlaceOnClaim(pl, pos))) {
                                        self.level().setBlock(
                                                pos,
                                                replacement.defaultBlockState(),
                                                Block.UPDATE_ALL
                                        );
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                ItemStack stack = self.getItemBySlot(EquipmentSlot.MAINHAND);

                if (stack.getItem() instanceof BlockItem blockItem
                        && MainUtil.FREEZABLE_BLOCK_ITEMS.containsKey(blockItem.getBlock())) {

                    Block frozen = MainUtil.FREEZABLE_BLOCK_ITEMS.get(blockItem.getBlock());

                    self.setItemSlot(
                            EquipmentSlot.MAINHAND,
                            new ItemStack(frozen)
                    );
                }
            }

            if (self.level() instanceof ServerLevel sl) {
                sl.sendParticles(ModParticles.COLD_CRACKLE,
                        self.getEyePosition().x,
                        self.getEyePosition().y,
                        self.getEyePosition().z,
                        0, 0, 0, 0, 0);

            }
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.BLOCK_FREEZE_EVENT,
                    SoundSource.PLAYERS, 1F, 1F);
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


    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move, forced);
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
        }
        return super.getSoundFromByte(soundChoice);
    }


    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (slot == 4 && isHoldingSneak() && self.isInWater()){
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
        if (getCreative() || !ClientNetworking.getAppropriateConfig().survivorSettings.canonSurvivorHasNoRageCupid) {
            $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.target_zap",
                    "instruction.roundabout.press_skill", StandIcons.RAGE_SELECTION, 4, level, bypass));
        }
        return $$1;
    }



    public List<Entity> iceEntities = new ArrayList<>();

    public void addIceEntity(Entity che){
        iceEntityInit();
        iceEntities.add(che);
    }
    public void iceEntityInit(){
        if (iceEntities == null) {
            iceEntities = new ArrayList<>();
        }
    }
    public void clearAllIceEntities(){
        iceEntityInit();

        List<Entity> hurricaneSpecial2 = new ArrayList<>(iceEntities) {
        };
        if (!iceEntities.isEmpty()) {
            for (Entity value : hurricaneSpecial2) {
                iceEntities.remove(value);
                if (value instanceof BlockWallEntity bwe){
                    bwe.breakAndDiscard();
                } else {
                    value.discard();
                }
            }
        }
    }

    public void ticklIceEntities(){
        iceEntityInit();

        List<Entity> hurricaneSpecial2 = new ArrayList<>(iceEntities) {
        };
        if (!iceEntities.isEmpty()) {
            for (Entity value : hurricaneSpecial2) {
                if (value.isRemoved() || !(value.isAlive())){
                    iceEntities.remove(value);
                }
            }
        }
    }

    @Override
    public boolean setPowerAttack(){
        setAttack();
        return false;
    }
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (activePowerPhase == 0){
                if (isBrawling()) {
                    if (getActivePower() != PowerIndex.EXTRA &&
                            !isBarraging()) {
                        this.tryPower(PowerIndex.ATTACK);
                    }
                }
            }
        }
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (activePowerPhase == 0){
                if (isBrawling()) {

                }
            }
        }
    }

    public int getWhiteAlbumChargeLength(){
        return 35;
    }


    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean powerOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = getAttackTimeDuring();
        if (powerOn && isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (getActivePower() == PowerIndex.EXTRA) {
            int ClashTime =  Math.round(attackTimeDuring / Math.max(getWhiteAlbumChargeLength(),attackTimeDuring) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 104, ClashTime, 6);
        } else if (powerOn && isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            int barTexture = 0;
            Entity TE = getTargetEntity(playerEntity, 3, getBrawlPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {

                    if (getActivePowerPhase() == getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else if (TE != null && isBrawling()) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);

                }
            }
            if (powerOn && isBrawling()) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }

    @Override
    public float getBrawlPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 0.7F;
        } else {
            return 2.2F;
        }
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
    public void addToCombo(Entity targ){
        if (targ instanceof Player PL){
            HeatUtil.addHeat(PL,-2);
        } else if (targ instanceof LivingEntity LE){
            HeatUtil.addHeat(LE,-14);
        }
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active");
    }


}