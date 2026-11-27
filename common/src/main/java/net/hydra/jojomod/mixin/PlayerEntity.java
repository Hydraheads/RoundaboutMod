package net.hydra.jojomod.mixin;

import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StoneMaskBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.GERNetherIllusionHelper;
import net.hydra.jojomod.entity.SilverChariotMountEntity;
import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.ZombieFate;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Mixin(Player.class)
public abstract class PlayerEntity extends LivingEntity implements IPlayerEntity{

    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract float getDestroySpeed(BlockState $$0);

    @Unique
    private static final EntityDataAccessor<Byte> SBA$POS = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> SBA$POS_2 = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);


    @Unique
    private static final EntityDataAccessor<Boolean> SBA$COMBAT_MODE = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<Byte> SBA$POSE_EMOTE = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Byte> SBA$DATA_KNIFE_COUNT_ID = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> SBA$TEAM_COLOR = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> SBA$FATE = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> SBA$POWERS = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Integer> SBA$DODGE_TIME = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> SBA$CAMERA_HITS = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<ItemStack> SBA$MASK_SLOT = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.ITEM_STACK);
    @Unique
    private static final EntityDataAccessor<ItemStack> SBA$MASK_VOICE_SLOT = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.ITEM_STACK);
    @Unique
    private static final EntityDataAccessor<Byte> SBA$STAND_LEVEL = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> SBA$SHAPE_SHIFT = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> SBA$SHAPE_SHIFT_EXTRA = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    //This is essential for clone "girth" that you semi-permanently acquire from jumping bodies
    @Unique
    private static final EntityDataAccessor<Byte> SBA$WIDTH_FACTOR = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Integer> SBA$STAND_EXP = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> SBA$IS_CONTROLLING = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> SBA$IS_BLINDED = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Rotations> SBA$HAIR_COLOR = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.ROTATIONS);

    // In your Player mixin
    @Unique
    private int sba$gerDeathLoops = 0;

    @Unique
    private ListTag sba$gerSavedInventory = null;

    @Override
    public int rdbt$getGerDeathLoops() {
        return this.sba$gerDeathLoops;
    }

    @Override
    public void rdbt$setGerDeathLoops(int loops) {
        this.sba$gerDeathLoops = loops;
    }

    @Override
    public ListTag rdbt$getGerSavedInventory() {
        return this.sba$gerSavedInventory;
    }

    @Override
    public void rdbt$setGerSavedInventory(ListTag inv) {
        this.sba$gerSavedInventory = inv;
    }

    @Unique
    public Deque<Vec3> rdbt$movementHistory = new ArrayDeque<>();

    @Unique
    @Override
    public Deque<Vec3> rdbt$getMovementHistory(){
        return rdbt$movementHistory;
    }

    @Shadow
    @Final
    private Inventory inventory;

    /**Used by Harpoon calculations*/
    @Unique
    private int sba$airTime = 0;
    @Unique
    private int sba$clientDodgeTime = 0;
    @Unique
    private int sba$anchorPlace = 55;
    @Unique
    private int sba$anchorPlaceAttack = 55;
    @Unique
    private float sba$distanceOut = 1.07F;
    @Unique
    private float sba$sizePercent = 1F;
    @Unique
    private float sba$idleRotation = 0;
    @Unique
    private float sba$idleYOffset = 0.1F;

    @Unique
    @Override
    public void sba$setCombatMode(boolean only) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(SBA$COMBAT_MODE, only);
        }
    }
    @Unique
    @Override
    public boolean sba$getCombatMode() {
        if (PowerTypes.isBrawling(this)){
            return true;
        }
        if (PowerTypes.hasStandActive(this) && ((StandUser)this).sba$getStandPowers().hasPassiveCombatMode()){
            return true;
        }
        if (getEntityData().hasItem(SBA$COMBAT_MODE)) {
            return this.getEntityData().get(SBA$COMBAT_MODE);
        } else {
            return false;
        }
    }
    /// Zombie Fate's zombie silverfish count
    @Unique
    private int rdbt$zombieFish = 0;
    @Unique
    @Override
    public void rdbt$setZombieFish(int fish){
        rdbt$zombieFish = fish;
        if (((IFatePlayer)this).rdbt$getFatePowers() instanceof ZombieFate zf){
            zf.zombieFishCount = fish;
        }
    }
    @Unique
    @Override
    public int rdbt$getZombieFish(){
        return rdbt$zombieFish;
    }

    @Inject(
            method = "isPushedByFluid",
            at = @At("HEAD"),
            cancellable = true, require = 0
    )
    private void sba$isPushedByFluid(CallbackInfoReturnable<Boolean> cir) {
        if (((StandUser)this).sba$getStandPowers() instanceof PowersWalkingHeart PW
                && PW.hasExtendedHeelsForWalking()){
            cir.setReturnValue(false);
        }
        if (((StandUser)this).sba$getStandPowers() instanceof PowersDiverDown PDD && PDD.inZipMode()){
            cir.setReturnValue(false);
        }
    }
    @Inject(
            method = "touch",
            at = @At("HEAD"),
            cancellable = true, require = 0
    )
    public void rdbt$touch(Entity $$0, CallbackInfo ci){
        if (PowerTypes.isInADifferentExistence(this,$$0)){
            ci.cancel();
        }

    }
    @Inject(
            method = "startAutoSpinAttack(I)V",
            at = @At("HEAD"),
            cancellable = true, require = 0
    )
    public void rdbt$startAutoSpinAttackP(int $$0, CallbackInfo ci){
        ((StandUser)this).sba$getStandPowers().onSpinAttackStart();
    }


    //0.00392156862
    @Unique
    private static final float rdbt$hairColorX =245f/255f;
    @Unique
    private static final float rdbt$hairColorY =225f/255f;
    @Unique
    private static final float rdbt$hairColorZ =97f/255f;
    @Unique
    @Override
    public void rdbt$setHairColorX(float color){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$HAIR_COLOR)) {
            Rotations rot = ((Player) (Object) this).getEntityData().get(SBA$HAIR_COLOR);
            ((Player) (Object) this).getEntityData().set(SBA$HAIR_COLOR,
                    new Rotations(color,rot.getY(),rot.getZ()));
        }
    }
    @Unique
    @Override
    public void rdbt$setHairColorY(float color){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$HAIR_COLOR)) {
            Rotations rot = ((Player) (Object) this).getEntityData().get(SBA$HAIR_COLOR);
            ((Player) (Object) this).getEntityData().set(SBA$HAIR_COLOR,
                    new Rotations(rot.getX(),color,rot.getZ()));
        }
    }
    @Unique
    @Override
    public void rdbt$setHairColorZ(float color){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$HAIR_COLOR)) {
            Rotations rot = ((Player) (Object) this).getEntityData().get(SBA$HAIR_COLOR);
            ((Player) (Object) this).getEntityData().set(SBA$HAIR_COLOR,
                    new Rotations(rot.getX(),rot.getY(),color));
        }
    }
    @Unique
    @Override
    public float rdbt$getHairColorX(){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$HAIR_COLOR)) {
            return ((Player) (Object) this).getEntityData().get(SBA$HAIR_COLOR).getX();
        }
        return rdbt$hairColorX;
    }
    @Unique
    @Override
    public float rdbt$getHairColorY(){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$HAIR_COLOR)) {
            return ((Player) (Object) this).getEntityData().get(SBA$HAIR_COLOR).getY();
        }
        return rdbt$hairColorY;
    }
    @Unique
    @Override
    public float rdbt$getHairColorZ(){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$HAIR_COLOR)) {
            return ((Player) (Object) this).getEntityData().get(SBA$HAIR_COLOR).getZ();
        }
        return rdbt$hairColorZ;
    }


    @Unique
    protected boolean rdbt$cooldownQuery = false;
    @Unique
    protected boolean rdbt$attemptedQuery = false;
    @Unique
    protected BlackSabbathPlayerInventory rdbt$blckSabbathInventory = new BlackSabbathPlayerInventory((Player) (Object) this);
    @Unique
    @Override
    public BlackSabbathPlayerInventory sba$getBlckSabbathPlayerInventory() {
        return this.rdbt$blckSabbathInventory;
    }
    @Override
    @Unique
    public void sba$setBlckSabbathPlayerInventory(BlackSabbathPlayerInventory bsi) {
        this.rdbt$blckSabbathInventory.replaceWith(bsi);;
    }

    @Unique
    private byte rdbt$respawnStrategy = 0;
    @Unique
    @Override
    public void rdbt$setRespawnStrategy(byte strat){
        rdbt$respawnStrategy = strat;
    }
    @Unique
    @Override
    public byte rdbt$getRespawnStrategy(){
        return rdbt$respawnStrategy;
    }


    @Unique
    @Override
    public void rdbt$queryServerForCooldowns(){

    }
    @Unique
    @Override
    public boolean rdbt$getCooldownQuery(){
        return rdbt$cooldownQuery;
    }
    @Unique
    @Override
    public void rdbt$setCooldownQuery(boolean query){
        rdbt$cooldownQuery = query;
    }
    @Unique
    @Override
    public void rdbt$setCooldownQuery2(){
        rdbt$cooldownQuery = false;
        rdbt$attemptedQuery = false;
    }

    @Unique
    @Override
    public void sba$setTeamColor(byte color){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$TEAM_COLOR)) {
            ((Player) (Object) this).getEntityData().set(SBA$TEAM_COLOR, color);
        }
    }
    @Unique
    @Override
    public byte sba$getTeamColor(){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$TEAM_COLOR)) {
            return this.entityData.get(SBA$TEAM_COLOR);
        }
        return 0;
    }
    @Unique
    @Override
    public byte sba$getWatchStyle(){
        if (((StandUser)this).sba$getStandPowers() instanceof PowersMandom PM) {
            return PM.getWatchStyle();
        }
        return 0;
    }
    @Unique
    @Override
    public void sba$setFate(byte style){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$FATE)) {
            if (sba$getFate() != style) {
                sba$SetPos2(PlayerPosIndex.NONE);
                this.getEntityData().set(SBA$FATE, style);
            }
        }
    }
    @Unique
    @Override
    public byte sba$getFate(){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$FATE)) {
            return this.entityData.get(SBA$FATE);
        }
        return 0;
    }
    @Unique
    @Override
    public void sba$setPower(byte style){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$POWERS)) {
            sba$SetPos2(PlayerPosIndex.NONE);
            this.getEntityData().set(SBA$POWERS, style);
        }
    }
    @Unique
    @Override
    public void sba$setPowerWithPenalty(byte style){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$POWERS)) {
            sba$SetPos2(PlayerPosIndex.NONE);
            if (style != this.getEntityData().get(SBA$POWERS) && this.getEntityData().get(SBA$POWERS) != PowerTypes.NONE.ordinal()){
                if (ClientNetworking.getAppropriateConfig().powersSettings.powerSwitchingPenalty) {
                    ((StandUser) this).sba$getStandPowers().onStandSwitch();
                    ((StandUser) this).sba$getStandPowers().onPowerSwitch();
                    if (!this.isCreative()) {
                        addEffect(new MobEffectInstance(ModEffects.SWITCH, 240, 0));
                    }
                }
            }
            this.getEntityData().set(SBA$POWERS, style);
        }
    }

    @Unique
    public boolean rdbt$isExhaustingMovement = false;
    @Inject(method = "checkMovementStatistics",at=@At(value = "HEAD"),require = 0)
    public void rdbt$checkMovementStatistics(CallbackInfo ci) {
        rdbt$isExhaustingMovement = true;
    }
    @Inject(method = "checkMovementStatistics",at=@At(value = "TAIL"),require = 0)
    public void rdbt$checkMovementStatistics2(CallbackInfo ci) {
        rdbt$isExhaustingMovement = false;
    }
    @Inject(method = "causeFoodExhaustion",at=@At(value = "HEAD"),require = 0,cancellable = true)
    public void rdbt$causeFoodExhaustion(float exhaustion,CallbackInfo ci) {
        //White Album does not lose nearly as much hunger from skating
        if (rdbt$isExhaustingMovement){
            if (((StandUser)this).sba$getStandPowers() instanceof PowersWhiteAlbum PW &&
            PW.hasSkatesActivated() && !this.isInWater()){
                if (!this.getAbilities().invulnerable) {
                    if (!this.level().isClientSide) {
                        this.getFoodData().addExhaustion(exhaustion*0.3F);
                        ci.cancel();
                    }
                }
            }
        }
    }
    @Unique
    @Override
    public byte sba$getPower(){
        if (((Player)(Object)this).getEntityData().hasItem(SBA$POWERS)) {
            byte pows = this.entityData.get(SBA$POWERS);
            if (!level().isClientSide()) {
                if (pows == PowerTypes.NONE.ordinal() && ((StandUser) this).sba$hasAStand()) {
                    this.entityData.set(SBA$POWERS, (byte)PowerTypes.STAND.ordinal());
                    return this.entityData.get(SBA$POWERS);
                }
            }
            return pows;
        }
        return 0;
    }


    ///  hacky solution for really odd anubis bug, check this if something breaks
    @Inject(method = "jumpFromGround",at=@At(value = "HEAD"))
    public void huhh(CallbackInfo ci) {
        Player player = (Player)(Object)(this);
        if ( ((StandUser)player).sba$getStandPowers() instanceof PowersAnubis PA && ((StandUser)player).sba$getUniqueStandModeToggle()) {
            float bigJump = ((StandUser) player).sba$getBonusJumpHeight();
            float totalHeight = bigJump + 1;
            boolean canJump = bigJump > 0;
            boolean isJumping = ((StandUser) player).sba$getBigJump();
            float getCurrentJump = ((StandUser) player).sba$getBigJumpCurrentProgress();

            if (canJump) {
                if (player.getAbilities().flying) {
                    if (isJumping) {
                        this.sba$SetBonusJump(false, totalHeight, getCurrentJump);
                    }
                } else {
                    if (isJumping && player.onGround()) {
                        this.sba$SetBonusJump(false, totalHeight, getCurrentJump);
                    }
                    if (isJumping && player.onGround() && getCurrentJump > 0) {
                    } else {
                        if (player.onGround() || isJumping) {
                            this.sba$SetBonusJump(true, totalHeight, getCurrentJump);
                        }
                    }
                }
            }
        }
    }

    @Unique
    public void sba$SetBonusJump(boolean bigJump, float jumpHeight, float current){
        ((StandUser)this).sba$setBigJump(bigJump);
        if (bigJump){
            C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_BIG_JUMP,current);
        } else {
            C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_BIG_JUMP_CANCEL,current);
        }
    }

    @Unique
    @Override
    public void sba$qmessage(int messageID){
        rdbt$qmessage = messageID;
        rdbt$qmessageTime = 60;
    }
    public int rdbt$qmessage = 0;
    public int rdbt$qmessageTime = 0;
    @Unique
    public void sba$qmessageTick(){
        if (rdbt$qmessageTime > 0){
            rdbt$qmessageTime--;

            if (rdbt$qmessageTime == 0){
                switch (rdbt$qmessage){
                    case 1 -> displayClientMessage(Component.translatable("item.sba.stand_arrow.acquireStand_2").withStyle(ChatFormatting.WHITE), true);
                    case 2 -> rdbt$vamp1();
                    case 3 -> rdbt$vamp2();
                    case 4 -> rdbt$vamp3();
                    default -> displayClientMessage(Component.translatable("item.sba.stand_arrow.acquireStand").withStyle(ChatFormatting.WHITE), true);
                }
            }
        }
    }

    @Unique
    public void rdbt$vamp1(){
        displayClientMessage(Component.translatable("item.sba.stand_arrow.acquireVampire2").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.BOLD), true);
        sba$qmessage(3);
    }
    @Unique
    public void rdbt$vamp2(){
        displayClientMessage(Component.translatable("item.sba.stand_arrow.acquireVampire3").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.BOLD), true);
        sba$qmessage(4);
    }
    @Unique
    public void rdbt$vamp3(){
        displayClientMessage(Component.translatable("item.sba.stand_arrow.acquireVampire4").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD), true);
        S2CPacketUtil.vampireMessage(((Player) (Object)this));
    }


    @Unique
    private PlayerMaskSlots sba$maskInventory = new PlayerMaskSlots(((Player)(Object)this));

    protected PlayerEntity(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Unique
    public Inventory sba$GetInventory(){
        return inventory;
    }


    @Unique
    public int rdbt$levelDecreaseTicks = 0;
    @Unique
    @Override
    public void rdbt$setLevelDecreaseTicks(int decreaseTicks){
        rdbt$levelDecreaseTicks = decreaseTicks;
        if (!level().isClientSide()){
            S2CPacketUtil.sendGenericIntToClientPacket(
                    ((Player) (Object)this),
                    PacketDataIndex.S2C_INT_LVL_DECREASE,
                    decreaseTicks
            );
        }
    }
    @Unique
    @Override
    public int rdbt$getLevelDecreaseTicks(){
        return rdbt$levelDecreaseTicks;
    }

    /**Keep track of unique player animations like floating passive anims like dodging or posing*/
    public void sba$SetPos(byte Pos){
        ((Player) (Object) this).getEntityData().set(SBA$POS, Pos);
    }
    public byte sba$GetPos(){
        return ((Player) (Object) this).getEntityData().get(SBA$POS);
    }
    public void sba$SetPos2(byte Pos){
        ((Player) (Object) this).getEntityData().set(SBA$POS_2, Pos);
    }
    public byte sba$GetPos2(){
        return ((Player) (Object) this).getEntityData().get(SBA$POS_2);
    }
    public void sba$SetPoseEmote(byte Pos){
                byte posEmote = ((Player) (Object) this).getEntityData().get(SBA$POSE_EMOTE);
        ((Player) (Object) this).getEntityData().set(SBA$POSE_EMOTE, Pos);
        ((StandUser)this).sba$getStandPowers().onPoseEmoteSwitch(posEmote,Pos);
    }
    public byte sba$GetPoseEmote(){
        return ((Player) (Object) this).getEntityData().get(SBA$POSE_EMOTE);
    }
    public float sba$getSizePercent(){
        return sba$sizePercent;
    }
    public float sba$getIdleRotation(){
        return sba$idleRotation;
    }
    public float sba$getIdleYOffset(){
        return sba$idleYOffset;
    }
    @Unique
    @Override
    public void sba$setAnchorPlace(int anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,360);
        this.sba$anchorPlace = anchorPlace;
        StandEntity ent = ((StandUser)this).sba$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setAnchorPlace(anchorPlace);
        }
    }
    @Unique
    @Override
    public void sba$setAnchorPlaceAttack(int anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,360);
        this.sba$anchorPlaceAttack = anchorPlace;
        StandEntity ent = ((StandUser)this).sba$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setAnchorPlaceAttack(anchorPlace);
        }
    }
    @Unique
    @Override
    public void sba$setSizePercent(float anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,3);
        this.sba$sizePercent = anchorPlace;
        StandEntity ent = ((StandUser)this).sba$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setSizePercent(anchorPlace);
        }
    }
    @Unique
    @Override
    public void sba$setIdleRotation(float anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,360);
        this.sba$idleRotation = anchorPlace;
        StandEntity ent = ((StandUser)this).sba$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setIdleRotation(anchorPlace);
        }
    }
    @Unique
    @Override
    public void sba$setIdleYOffset(float anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,2);
        this.sba$idleYOffset = anchorPlace;
        StandEntity ent = ((StandUser)this).sba$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setIdleYOffset(anchorPlace);
        }
    }
    @Unique
    @Override
    public int sba$getAnchorPlace(){
        return this.sba$anchorPlace;
    }
    @Unique
    @Override
    public int sba$getAnchorPlaceAttack(){
        return this.sba$anchorPlaceAttack;
    }
    @Unique
    @Override
    public void sba$setDistanceOut(float distanceOut){
        distanceOut = Mth.clamp(distanceOut,0,2F);
        this.sba$distanceOut = distanceOut;
        StandEntity ent = ((StandUser)this).sba$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setDistanceOut(distanceOut);
        }
    }
    @Unique
    @Override
    public float sba$getDistanceOut(){
        return this.sba$distanceOut;
    }
    @Unique
    @Override
    public void sba$setClientDodgeTime(int dodgeTime){
        sba$clientDodgeTime = dodgeTime;
    }
    @Unique
    @Override
    public void sba$setDodgeTime(int dodgeTime){
        ((Player) (Object) this).getEntityData().set(SBA$DODGE_TIME, dodgeTime);
    }
    @Unique
    @Override
    public int sba$getClientDodgeTime(){
        return sba$clientDodgeTime;
    }
    @Unique
    @Override
    public int sba$getDodgeTime(){
        return ((Player) (Object) this).getEntityData().get(SBA$DODGE_TIME);
    }
    @Unique
    @Override
    public int sba$getCameraHits(){
        return ((Player) (Object) this).getEntityData().get(SBA$CAMERA_HITS);
    }
    @Unique
    @Override
    public void sba$setCameraHits(int ticks){
        ((Player) (Object) this).getEntityData().set(SBA$CAMERA_HITS, ticks);
    }

    @Unique
    @Override
    public int sba$getAirTime(){
        return this.sba$airTime;
    }

    @Shadow
    protected boolean wantsToStopRiding() {
        return false;
    }

    @Unique
    public boolean sba$heldDownSwitchExp = false;
    @Unique
    public boolean sba$displayExp = false;

    @Unique
    public boolean sba$displayNamePossible = true;


    /**Crit / Critical Hits!*/
    @ModifyVariable(method = "attack",
            at = @At(value = "STORE"), ordinal = 2)
    public boolean sba$attackThis(boolean value) {
        StandUser SU = ((StandUser) this );
        if (SU.sba$getStandPowers().forceCrit()){
            return true;
        }
        if (SU.sba$getPossessor() instanceof AnubisPossessorEntity) {
            return true;
        }
        return value;
    }


    @Override
    @Unique
    public void sba$setShowName(boolean boo){
        sba$displayNamePossible = boo;
    }
    @Inject(method = "shouldShowName",
            at = @At(value = "HEAD"), cancellable = true)
    public void sba$shouldShowName(CallbackInfoReturnable<Boolean> cir) {
        if (!sba$displayNamePossible){
            cir.setReturnValue(false);
        }
    }
    @Override
    @Unique
    public void sba$showExp(boolean keyIsDown){
        if (!sba$heldDownSwitchExp){
            if (keyIsDown){
                sba$heldDownSwitchExp = true;
                if (sba$displayExp){
                    sba$displayExp=false;
                } else {
                    sba$displayExp=true;
                }
            }
        } else {
            if (!keyIsDown){
                sba$heldDownSwitchExp = false;
            }
        }
    }

    @Override
    @Unique
    public boolean sba$getDisplayExp(){
        return sba$displayExp;
    }

    @Override
    @Unique
    public void sba$setStandLevel(byte level){
        ((Player) (Object) this).getEntityData().set(SBA$STAND_LEVEL, level);
    }
    @Override
    @Unique
    public byte sba$getStandLevel(){
        byte levl = ((Player) (Object) this).getEntityData().get(SBA$STAND_LEVEL);
        levl = (byte) Math.max(levl,1);
        return levl;
    }
    @Override
    @Unique
    public void sba$setShapeShift(byte level){
        if (((Player) (Object) this).getEntityData().get(SBA$SHAPE_SHIFT) != level){
            if (level != ShapeShifts.VILLAGER.ordinal()) {
                ((Player) (Object) this).getEntityData().set(SBA$SHAPE_SHIFT_EXTRA, (byte) 0);
            }
            ((Player) (Object) this).getEntityData().set(SBA$SHAPE_SHIFT, level);
        }
    }
    @Override
    @Unique
    public void sba$shapeShift(){
        if (!this.level().isClientSide()){
            for (int i = 0; i < 30; i++){
                this.level().playSound(null, this, ModSounds.FOG_MORPH_EVENT, SoundSource.PLAYERS, 0.36F, 1.0F);
                ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, this.getX(),
                        this.getY()+(this.getBbWidth()*0.6), this.getZ(),
                        14, 0.4, 0.2, 0.4, 0.35);
            }
        }
    }
    @Override
    @Unique
    public void sba$shapeShiftSilent(){
        if (!this.level().isClientSide()){
            for (int i = 0; i < 30; i++){
                ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, this.getX(),
                        this.getY()+(this.getBbWidth()*0.6), this.getZ(),
                        14, 0.4, 0.2, 0.4, 0.35);
            }
        }
    }
    @Override
    @Unique
    public byte sba$getShapeShift(){
        return ((Player) (Object) this).getEntityData().get(SBA$SHAPE_SHIFT);
    }
    @Override
    @Unique
    public void sba$setShapeShiftExtraData(byte level){
        ((Player) (Object) this).getEntityData().set(SBA$SHAPE_SHIFT_EXTRA, level);
    }
    @Override
    @Unique
    public byte sba$getShapeShiftExtraData(){
        return ((Player) (Object) this).getEntityData().get(SBA$SHAPE_SHIFT_EXTRA);
    }
    @Override
    @Unique
    public void sba$setWidthFactor(byte level){
        ((Player) (Object) this).getEntityData().set(SBA$WIDTH_FACTOR, level);
    }
    @Override
    @Unique
    public byte sba$getWidthFactor(){
        return ((Player) (Object) this).getEntityData().get(SBA$WIDTH_FACTOR);
    }
    @Override
    @Unique
    public void sba$setStandExp(int level){
        ((Player) (Object) this).getEntityData().set(SBA$STAND_EXP, level);
    }
    @Override
    @Unique
    public int sba$getControlling(){
        return ((Player) (Object) this).getEntityData().get(SBA$IS_CONTROLLING);
    }
    @Override
    @Unique
    public int sba$getAttackStrengthTicker(){
        return attackStrengthTicker;
    }
    @Override
    @Unique
    public void sba$setIsControlling(int pilot){
        ((Player) (Object) this).getEntityData().set(SBA$IS_CONTROLLING, pilot);
    }
    @Override
    @Unique
    public boolean sba$getBlinded(){
        return ((Player) (Object) this).getEntityData().get(SBA$IS_BLINDED);
    }
    @Override
    @Unique
    public void sba$setBlinded(boolean blinded){
        ((Player) (Object) this).getEntityData().set(SBA$IS_BLINDED, blinded);
    }


    @Override
    @Unique
    public void sba$addStandExp(int amt){
        if (ClientNetworking.getAppropriateConfig().standLevelingSettings.enableStandLeveling) {
            int currentExp = sba$getStandExp();
            currentExp += amt;
            byte level = this.sba$getStandLevel();
            StandPowers powers = ((StandUser) this).sba$getStandPowers();
            int maxLevel = powers.getMaxLevel();
            if (currentExp >= powers.getExpForLevelUp(level) && level < maxLevel) {
                ((Player) (Object) this).getEntityData().set(SBA$STAND_EXP, 0);
                ((Player) (Object) this).getEntityData().set(SBA$STAND_LEVEL, (byte) (level + 1));
                powers.levelUp();
            } else {
                ((Player) (Object) this).getEntityData().set(SBA$STAND_EXP, currentExp);
            }
        }
    }
    @Override
    @Unique
    public int sba$getStandExp(){
        return ((Player) (Object) this).getEntityData().get(SBA$STAND_EXP);
    }

    @Unique
    public boolean sba$unlockedBonusSkin = false;
    @Override
    @Unique
    public void sba$setUnlockedBonusSkin(boolean bl){
        sba$unlockedBonusSkin = bl;
    }
    @Override
    @Unique
    public boolean sba$getUnlockedBonusSkin(){
        return sba$unlockedBonusSkin;
    }
    @Inject(method = "canSprint", at = @At(value = "HEAD"), cancellable = true)
    public void sba$canSprintPlayer(CallbackInfoReturnable<Boolean> cir) {
        if ( ((StandUser)this).sba$getStandPowers().cancelSprint()
                || ((IFatePlayer)this).rdbt$getFatePowers().cancelSprint()){
            cir.setReturnValue(false);
        }
    }
    /**Attack Speed Decreases when your hand is stone*/
    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At(value = "HEAD"), cancellable = true)
    public void sba$getCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        byte curse = ((StandUser)this).sba$getLocacacaCurse();
        float modifier = 1;
        if (curse > -1) {
            if ((curse == LocacacaCurseIndex.RIGHT_HAND && this.getMainArm() == HumanoidArm.RIGHT)
            || (curse == LocacacaCurseIndex.LEFT_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                modifier = 0.6F;
            }
        }
        if (HeatUtil.isArmsFrozen(this)){
            modifier*=0.6f;
        }
        boolean active = ((StandUser) this).sba$getActive();
        if (active && PowerTypes.hasStandActivelyEquipped(this)){
            modifier*= ((StandUser)this).sba$getStandPowers().getBonusAttackSpeed();
        } else if (active){
            modifier*= ((IPowersPlayer)this).rdbt$getPowers().getBonusAttackSpeed();
        }
        float bpow = ((IFatePlayer)this).rdbt$getFatePowers().getBonusAttackSpeed();
        if (bpow != 1){
            modifier*= bpow;
        }
        if (((StandUser) this).sba$hasDiverArms()) {
            modifier *= 1F + (ClientNetworking.getAppropriateConfig().diverDownSettings.armAttackBoost * 0.01F);
        }
        if (modifier != 1){
            cir.setReturnValue((float)(1.0D / (this.getAttributeValue(Attributes.ATTACK_SPEED)*modifier) * 20.0D));
        }
    }

    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "TAIL"))
    public void sba$actuallyHurt2(DamageSource $$0, float $$1, CallbackInfo ci) {
        if (!this.isInvulnerableTo($$0)) {
            if (!this.isDeadOrDying()) {
                if (sba$getVoiceData() != null){
                    sba$getVoiceData().playIfHurt($$0);
                }
            }
//            if (this.getUseItem() != null && this.getUseItem().is(ModItems.NEW_LOCACACA)){
//                if ($$0.getEntity() != null){
//                    stopUsingItem();
//                }
//            }
        }
    }

    /**Block Breaking Speed Decreases when your hand is stone*/
    @Unique
    private boolean sba$destroySpeedRecursion = false;
    @Inject(method = "getDestroySpeed", at = @At(value = "HEAD"), cancellable = true)
    public void sba$getDestroySpeed(BlockState $$0, CallbackInfoReturnable<Float> cir) {
        byte curse = ((StandUser)this).sba$getLocacacaCurse();
        if (!sba$destroySpeedRecursion) {
            sba$destroySpeedRecursion = true;
            float dSpeed = this.getDestroySpeed($$0);
            sba$destroySpeedRecursion = false;
            if (curse > -1 && (curse == LocacacaCurseIndex.RIGHT_HAND && this.getMainArm() == HumanoidArm.RIGHT)
                    || (curse == LocacacaCurseIndex.LEFT_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                dSpeed *= 0.6F;
            }


            if (HeatUtil.isArmsFrozen(this)){
                dSpeed*=0.25f;
            }

            boolean active = ((StandUser) this).sba$getActive();
            if (active && PowerTypes.hasStandActivelyEquipped(this)){
                dSpeed*= ((StandUser)this).sba$getStandPowers().getBonusPassiveMiningSpeed();
            } else if (active){
                dSpeed*= ((IPowersPlayer)this).rdbt$getPowers().getBonusPassiveMiningSpeed();
            }

            if (!(PowerTypes.hasStandActive(this) && ((StandUser) this).sba$getStandPowers().canUseMiningStand()
            )) {
                float bpow = ((IFatePlayer) this).rdbt$getFatePowers().getBonusPassiveMiningSpeed();
                if (bpow != 1){
                    dSpeed*= bpow;
                }
            }
            if (((StandUser) this).sba$hasDiverArms()) {
                dSpeed *= 1F + (ClientNetworking.getAppropriateConfig().diverDownSettings.armMineBoost * 0.01F);
            }
            cir.setReturnValue(dSpeed);
        }
    }

    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "HEAD"), cancellable = true)
    public void sba$actuallyHurt(DamageSource $$0, float $$1, CallbackInfo ci) {
        StandUser userthis = ((StandUser)this);
        if (!this.isInvulnerableTo($$0) && $$1 > 0) {


            if ($$0.getEntity() instanceof Player pe && !$$0.isIndirect()
                    && !$$0.is(DamageTypes.THORNS)){
                if (((StandUser)pe).sba$getStandPowers().interceptSuccessfulDamageDealtEvent($$0,$$1, ((LivingEntity)(Object)this))){
                    ci.cancel();
                    return;
                }
            }

            if ($$0.getEntity() instanceof LivingEntity livent){
                ShapeShifts shift = ShapeShifts.getShiftFromByte(this.sba$getShapeShift());
                if (shift != ShapeShifts.PLAYER) {
                    if (ShapeShifts.isVillager(shift)) {
                        AABB aab = this.getBoundingBox().inflate(10.0, 8.0, 10.0);
                        List<? extends LivingEntity> le = this.level().getNearbyEntities(IronGolem.class, sba$attackTargeting, ((LivingEntity)(Object)this), aab);
                        Iterator var4 = le.iterator();
                        while(var4.hasNext()) {
                            LivingEntity nle = (LivingEntity)var4.next();
                            IronGolem golem = (IronGolem) nle;
                            golem.setTarget(livent);
                            golem.setLastHurtMob(livent);
                            golem.setLastHurtByMob(livent);
                        }
                    }
                }
            }


            Entity bound = userthis.sba$getBoundTo();
            if (bound != null && !$$0.isIndirect() && !$$0.is(ModDamageTypes.STAND_FIRE)){
                if (userthis.rdbt$getBoundType(bound) == 2) {
                    userthis.sba$dropString();
                }
            }
        }

        if (userthis.sba$mutualActuallyHurt($$0,$$1)){
            ci.cancel();
        }
    }

    /// bypasses the combat mode overrides
    @Unique
    public ItemStack sba$getForRealMainHand() {
        return this.inventory.getSelected();
    }

    @Unique
    public ItemStack sba$getForRealOffHand() {
        return this.inventory.offhand.get(0);
    }

    private final TargetingConditions sba$attackTargeting = TargetingConditions.forCombat().range(64.0);
    @Unique
    public Poses sba$standPos = null;
    @Unique
    public final AnimationState sba$BubbleAim = new AnimationState();
    @Unique
    @Override
    public AnimationState sba$getBubbleAim(){
        return sba$BubbleAim;
    }
    @Unique
    public final AnimationState sba$OffsetCorrect = new AnimationState();
    @Unique
    @Override
    public AnimationState sba$getOffsetCorrect(){
        return sba$OffsetCorrect;
    }
    @Unique
    public final AnimationState sba$barrageArms = new AnimationState();
    @Unique
    @Override
    public AnimationState sba$getBarrageArms(){
        return sba$barrageArms;
    }

    @Unique
    public final AnimationState sba$itemAnimation = new AnimationState();
    @Unique
    @Override
    public AnimationState sba$getItemAnimation(){
        return sba$itemAnimation;
    }
    @Unique
    public final AnimationState sba$itemAnimationActive = new AnimationState();
    @Unique
    @Override
    public AnimationState sba$getItemAnimationActive(){
        return sba$itemAnimationActive;
    }




    @Unique
    public final AnimationState sba$BubbleShotAim = new AnimationState();
    @Unique
    @Override
    public AnimationState sba$getBubbleShotAim(){
        return sba$BubbleShotAim;
    }
    @Unique
    public int sba$BubbleShotAimPoints = 0;
    @Unique
    @Override
    public int sba$getBubbleShotAimPoints(){
        return sba$BubbleShotAimPoints;
    }
    @Unique
    @Override
    public void sba$setBubbleShotAimPoints(int shotPoints){
        sba$BubbleShotAimPoints = shotPoints;
    }

    @Unique
    public final AnimationState sba$styleAnimation = new AnimationState();
    @Unique
    @Override
    public AnimationState getStyleAnimation() {
        return sba$styleAnimation;
    }

    @Unique
    private byte rdbt$lastAnimation = 0;
    @Unique
    public void sba$setupAnimationStates() {
        if (sba$GetPos2() == PlayerPosIndex.BARRAGE) {
            this.sba$barrageArms.startIfStopped(this.tickCount);
        } else {
            this.sba$barrageArms.stop();
        }


        byte posEmote = this.sba$GetPoseEmote();
        if (posEmote != Poses.NONE.id) {
            this.getStyleAnimation().startIfStopped(this.tickCount);
        } else {
            this.getStyleAnimation().stop();
        }
        rdbt$lastAnimation = this.sba$GetPoseEmote();
    }

    /**Break free from stand grab*/
    @Inject(method = "wantsToStopRiding", at = @At(value = "HEAD"), cancellable = true)
    public void sba$wantsToStopRiding(CallbackInfoReturnable<Boolean> cir) {
        if (this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()){
            int rticks = ((StandUser)this).sba$getRestrainedTicks();
            if (rticks < 50){
                cir.setReturnValue(false);
            }
        }
        if (this.getVehicle() != null && this.getVehicle() instanceof SilverChariotMountEntity scme) {
            // cir.setReturnValue(false);
        }
    }
    @Inject(method = "rideTick", at = @At(value = "HEAD"), cancellable = true)
    public void sba$rideTick(CallbackInfo ci) {
        if (this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()){
            int rticks = ((StandUser)this).sba$getRestrainedTicks();
            if (this.level().isClientSide){
                if (this.isCrouching()) {
                    rticks++;
                    if (rticks >= 30) {
                        rticks = 30;
                    }
                    C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_RIDE_TICKS,rticks);
                    ((StandUser) this).sba$setRestrainedTicks(rticks);
                } else {
                    rticks--;
                    if (rticks <= -1) {
                        rticks = -1;
                    }
                    C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_RIDE_TICKS,rticks);
                    ((StandUser) this).sba$setRestrainedTicks(rticks);
                }
            } else {
                if (rticks >= 30){
                    this.stopRiding();
                    this.setShiftKeyDown(false);
                    ci.cancel();
                }
            }
        } else if (this.getVehicle() != null && this.getVehicle() instanceof SilverChariotMountEntity scme) {
            // this.setShiftKeyDown(false);
            // ci.cancel();
        }
    }
    @Inject(method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "HEAD"),
            cancellable = true)
    public void sba$getProjectileHead(ItemStack $$0, CallbackInfoReturnable<ItemStack> cir) {
        if ($$0.getItem() instanceof ProjectileWeaponItem pi) {
            if ($$0.is(ModItems.IRON_BALL_CROSSBOW)){
                for (int $$3 = 0; $$3 < this.inventory.getContainerSize(); $$3++) {
                    ItemStack $$4 = this.inventory.getItem($$3);
                    if ($$4.is(Items.IRON_INGOT)) {
                        cir.setReturnValue($$4);
                        return;
                    }
                }
                cir.setReturnValue(this.getAbilities().instabuild ? new ItemStack(Items.IRON_INGOT) : ItemStack.EMPTY);
                return;
            }

            //GE tree imbued arrows get prioritized for crossbows and bows
            Predicate<ItemStack> heldPredicate = pi.getSupportedHeldProjectiles();
            for (ItemStack stack : this.inventory.offhand) {
                if (heldPredicate.test(stack) && PowersGoldExperience.isTreeImbuedProjectileItem(stack)) {
                    cir.setReturnValue(stack);
                    return;
                }
            }
            Predicate<ItemStack> allPredicate = pi.getAllSupportedProjectiles();
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack stack = this.inventory.getItem(i);
                if (allPredicate.test(stack) && PowersGoldExperience.isTreeImbuedProjectileItem(stack)) {
                    cir.setReturnValue(stack);
                    return;
                }
            }
        }
    }
    @Inject(method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE",
                    target="Lnet/minecraft/world/item/ProjectileWeaponItem;getAllSupportedProjectiles()Ljava/util/function/Predicate;",
            shift= At.Shift.AFTER),
            cancellable = true)
    public void sba$getProjectile(ItemStack $$0, CallbackInfoReturnable<ItemStack> cir) {
        Predicate<ItemStack> $$1 = ((ProjectileWeaponItem)$$0.getItem()).getAllSupportedProjectiles();
        for (int $$3 = 0; $$3 < this.inventory.getContainerSize(); $$3++) {
            ItemStack $$4 = this.inventory.getItem($$3);
            if ($$1.test($$4) ||
                    ($$4.getItem() instanceof StandArrowItem && !($$4.getDamageValue() >= $$4.getMaxDamage()))
                    || $$4.getItem() instanceof WorthyArrowItem) {
                cir.setReturnValue($$4);
            }
        }
    }

    @Inject(method = "getSpeed", at = @At(value = "HEAD"), cancellable = true)
    public void sba$getSpeed(CallbackInfoReturnable<Float> cir) {
        float basis = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
        basis = ((StandUser)this).sba$mutualGetSpeed(basis);

        cir.setReturnValue(basis);
    }

    /**if your stand guard is broken, disable shields. Also, does not run takeshieldhit code if stand guarding.*/
    @Inject(method = "blockUsingShield", at = @At(value = "HEAD"), cancellable = true)
    protected void sbaTakeShieldHit(LivingEntity $$0, CallbackInfo ci) {
        if (((StandUser) this).sba$isGuarding() || ((StandUser) this).sba$getStandPowers().isSpecialGuarding()) {
            if (((StandUser) this).sba$getGuardBroken()){

                ItemStack itemStack = ((LivingEntity) (Object) this).getUseItem();
                Item item = itemStack.getItem();
                if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                    ((LivingEntity) (Object) this).releaseUsingItem();
                    ((Player) (Object) this).stopUsingItem();
                }
                ((Player) (Object) this).getCooldowns().addCooldown(Items.SHIELD, 100);
                ((Player) (Object) this).level().broadcastEntityEvent(((Player) (Object) this), EntityEvent.SHIELD_DISABLED);
            }
            ci.cancel();
        } else if (((StandUser) $$0).sba$getMainhandOverride() &&
                ((StandUser) $$0).sba$getStandPowers().interceptAttack()){
            ci.cancel();
        }
    }
    private long purpleHazePodResetDay = -1;
    private byte purpleHazePods = 6;

    @Override
    public long sba$getPurpleHazePodResetDay() {
        return purpleHazePodResetDay;
    }
    @Override
    public void sba$setPurpleHazePodResetDay(long day) {
        purpleHazePodResetDay = day;
    }
    @Override
    public byte sba$getPurpleHazePods() {
        return purpleHazePods;
    }
    @Override
    public void sba$setPurpleHazePods(byte pods) {
        purpleHazePods = pods;
        if (pods == 6 && ((StandUser) this).sba$getStandPowers() instanceof PowersPurpleHaze PH) {
            PH.podRegenClient = -1;
        }

    }
    @Unique
    public VampireData rdbt$vampireData = new VampireData(level());

    @ModifyVariable(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "TAIL"),
            ordinal = 0, argsOnly = true)
    public CompoundTag sba$addAdditionalSaveData(CompoundTag $$0){
        $$0.putByte("sba.LocacacaCurse", ((StandUser)this).sba$getLocacacaCurse());
        ItemStack m1 = this.sba$maskInventory.getItem(0);
        ItemStack m2 = this.sba$maskInventory.getItem(1);
        if (!m1.isEmpty() || $$0.contains("sba.Mask", 10)) {
            CompoundTag compoundtag = new CompoundTag();
            $$0.put("sba.Mask",m1.save(compoundtag));
        }
        if (!m2.isEmpty() || $$0.contains("sba.VoiceMask", 10)) {
            CompoundTag compoundtag = new CompoundTag();
            $$0.put("sba.VoiceMask",m2.save(compoundtag));
        }
        $$0.put("BlackSabbathPlayerItems", this.rdbt$blckSabbathInventory.createTag());
        CompoundTag compoundtag = $$0.getCompound("sba");
        compoundtag.putInt("anchorPlace",sba$anchorPlace);
        compoundtag.putInt("anchorPlaceAttack",sba$anchorPlaceAttack);
        compoundtag.putInt("forgotStand",rdbt$getLevelDecreaseTicks());
        compoundtag.putFloat("distanceOut",sba$distanceOut);
        compoundtag.putFloat("idleRotation",sba$idleRotation);
        compoundtag.putFloat("idleYOffset",sba$idleYOffset);
        compoundtag.putFloat("sizePercent",sba$sizePercent);
        compoundtag.putByte("teamColor",sba$getTeamColor());
        compoundtag.putByte("widthFactor",sba$getWidthFactor());
        compoundtag.putByte("fate",sba$getFate());
        compoundtag.putByte("power",sba$getPower());
        compoundtag.putFloat("hairColorX",rdbt$getHairColorX());
        compoundtag.putFloat("hairColorY",rdbt$getHairColorY());
        compoundtag.putFloat("hairColorZ",rdbt$getHairColorZ());
        compoundtag.putByte("shapeShift",sba$getShapeShift());
        compoundtag.putByte("shapeShiftExtra",sba$getShapeShiftExtraData());
        compoundtag.putInt("zombieFish",rdbt$getZombieFish());
        compoundtag.putByte("purpleHazePods", purpleHazePods);
        compoundtag.putLong("purpleHazePodResetDay", purpleHazePodResetDay);

        compoundtag.putFloat("guard",((StandUser)this).sba$getGuardPoints());
        compoundtag.putBoolean("guard_break",((StandUser)this).sba$getGuardBroken());


        $$0.put("sba",compoundtag);
        if (ClientNetworking.getAppropriateConfig().vampireSettings.vampireLeveling) {
            CompoundTag vampire = $$0.getCompound("sbaVampire");
            vampire.putInt("vampireLevel", rdbt$vampireData.vampireLevel);
            vampire.putInt("bloodExp", rdbt$vampireData.bloodExp);
            vampire.putInt("animalExp", rdbt$vampireData.animalExp);
            vampire.putInt("monsterEXP", rdbt$vampireData.monsterEXP);
            vampire.putInt("npcExp", rdbt$vampireData.npcExp);
            vampire.putInt("timeSinceAnimal", rdbt$vampireData.timeSinceAnimal);
            vampire.putInt("timeSinceNpc", rdbt$vampireData.timeSinceNpc);
            vampire.putInt("timeSinceMonster", rdbt$vampireData.timeSinceMonster);


            vampire.putByte("strengthLevel", rdbt$vampireData.strengthLevel);
            vampire.putByte("dexterityLevel", rdbt$vampireData.dexterityLevel);
            vampire.putByte("resilienceLevel", rdbt$vampireData.resilienceLevel);
            vampire.putByte("hypnotismLevel", rdbt$vampireData.hypnotismLevel);
            vampire.putByte("superHearingLevel", rdbt$vampireData.superHearingLevel);

            vampire.putByte("bloodSpeedLevel", rdbt$vampireData.bloodSpeedLevel);
            vampire.putByte("graftingLevel", rdbt$vampireData.graftingLevel);
            vampire.putByte("fleshBudLevel", rdbt$vampireData.fleshBudLevel);
            vampire.putByte("daggerSplatterLevel", rdbt$vampireData.daggerSplatterLevel);
            vampire.putByte("jumpLevel", rdbt$vampireData.jumpLevel);

            vampire.putByte("ripperEyesLevel", rdbt$vampireData.ripperEyesLevel);
            vampire.putByte("freezeLevel", rdbt$vampireData.freezeLevel);
            $$0.put("sbaVampire", vampire);
        }

        return $$0;
    }

    @Override
    public VampireData rdbt$getVampireData(){
        return rdbt$vampireData;
    }
    @Override
    public void rdbt$setVampireData(VampireData vdata){
        rdbt$vampireData = vdata;
    }
    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "TAIL"))
    public void sba$readAdditionalSaveData(CompoundTag $$0, CallbackInfo ci){

        ((StandUser)this).sba$setLocacacaCurse($$0.getByte("sba.LocacacaCurse"));
        if ($$0.contains("sba.Mask", 10)) {
            CompoundTag compoundtag = $$0.getCompound("sba.Mask");
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof MaskItem SD){
                this.sba$maskInventory.setItem(0,itemstack);
            }
        }if ($$0.contains("sba.VoiceMask", 10)) {
            CompoundTag compoundtag = $$0.getCompound("sba.VoiceMask");
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof MaskItem SD){
                this.sba$maskInventory.setItem(1,itemstack);
            }
        }
        if ($$0.contains("BlackSabbathPlayerItems", 9)) {
            this.rdbt$blckSabbathInventory.fromTag($$0.getList("BlackSabbathPlayerItems", 10));
        }
        CompoundTag compoundtag2 = $$0.getCompound("sba");
        if (compoundtag2.contains("anchorPlace")) {
            sba$anchorPlace = compoundtag2.getInt("anchorPlace");
        }
        if (compoundtag2.contains("anchorPlaceAttack")) {
            sba$anchorPlaceAttack = compoundtag2.getInt("anchorPlaceAttack");
        }
        if (compoundtag2.contains("forgotStand")) {
            rdbt$levelDecreaseTicks = compoundtag2.getInt("forgotStand");
        }
        if (compoundtag2.contains("distanceOut")) {
            sba$distanceOut = compoundtag2.getFloat("distanceOut");
        }
        if (compoundtag2.contains("idleRotation")) {
            sba$idleRotation = compoundtag2.getFloat("idleRotation");
        }
        if (compoundtag2.contains("idleYOffset")) {
            sba$idleYOffset = compoundtag2.getFloat("idleYOffset");
        }
        if (compoundtag2.contains("sizePercent")) {
            sba$sizePercent = compoundtag2.getFloat("sizePercent");
        }
        if (compoundtag2.contains("teamColor")) {
            sba$setTeamColor(compoundtag2.getByte("teamColor"));
        }
        if (compoundtag2.contains("widthFactor")) {
            sba$setWidthFactor(compoundtag2.getByte("widthFactor"));
        }
        if (compoundtag2.contains("fate")) {
            sba$setFate(compoundtag2.getByte("fate"));
        }
        if (compoundtag2.contains("power")) {
            sba$setPower(compoundtag2.getByte("power"));
        }
        if (compoundtag2.contains("hairColorX")) {
            rdbt$setHairColorX(compoundtag2.getFloat("hairColorX"));
        }
        if (compoundtag2.contains("hairColorY")) {
            rdbt$setHairColorY(compoundtag2.getFloat("hairColorY"));
        }
        if (compoundtag2.contains("hairColorZ")) {
            rdbt$setHairColorZ(compoundtag2.getFloat("hairColorZ"));
        }


        if (compoundtag2.contains("shapeShift")) {
            sba$setShapeShift(compoundtag2.getByte("shapeShift"));
        }
        if (compoundtag2.contains("shapeShiftExtra")) {
            sba$setShapeShiftExtraData(compoundtag2.getByte("shapeShiftExtra"));
        }
        if (compoundtag2.contains("zombieFish")) {
            rdbt$setZombieFish(compoundtag2.getInt("zombieFish"));
        }
        if (compoundtag2.contains("purpleHazePods")) {
            purpleHazePods = compoundtag2.getByte("purpleHazePods");
        }
        if (compoundtag2.contains("purpleHazePodResetDay")) {
            purpleHazePodResetDay = compoundtag2.getLong("purpleHazePodResetDay");
        }

        if (ClientNetworking.getAppropriateConfig().vampireSettings.vampireLeveling) {
            CompoundTag vampire = $$0.getCompound("sbaVampire");
            if (vampire.contains("vampireLevel")) {
                rdbt$vampireData.vampireLevel = vampire.getInt("vampireLevel");
            }
            if (vampire.contains("bloodExp")) {
                rdbt$vampireData.bloodExp = vampire.getInt("bloodExp");
            }
            if (vampire.contains("animalExp")) {
                rdbt$vampireData.animalExp = vampire.getInt("animalExp");
            }
            if (vampire.contains("monsterEXP")) {
                rdbt$vampireData.monsterEXP = vampire.getInt("monsterEXP");
            }
            if (vampire.contains("npcExp")) {
                rdbt$vampireData.npcExp = vampire.getInt("npcExp");
            }
            if (vampire.contains("timeSinceAnimal")) {
                rdbt$vampireData.timeSinceAnimal = vampire.getInt("timeSinceAnimal");
            }
            if (vampire.contains("timeSinceNpc")) {
                rdbt$vampireData.timeSinceNpc = vampire.getInt("timeSinceNpc");
            }
            if (vampire.contains("timeSinceMonster")) {
                rdbt$vampireData.timeSinceMonster = vampire.getInt("timeSinceMonster");
            }

            if (vampire.contains("strengthLevel")) {
                rdbt$vampireData.strengthLevel = vampire.getByte("strengthLevel");
            }
            if (vampire.contains("dexterityLevel")) {
                rdbt$vampireData.dexterityLevel = vampire.getByte("dexterityLevel");
            }
            if (vampire.contains("resilienceLevel")) {
                rdbt$vampireData.resilienceLevel = vampire.getByte("resilienceLevel");
            }
            if (vampire.contains("hypnotismLevel")) {
                rdbt$vampireData.hypnotismLevel = vampire.getByte("hypnotismLevel");
            }
            if (vampire.contains("superHearingLevel")) {
                rdbt$vampireData.superHearingLevel = vampire.getByte("superHearingLevel");
            }
            if (vampire.contains("bloodSpeedLevel")) {
                rdbt$vampireData.bloodSpeedLevel = vampire.getByte("bloodSpeedLevel");
            }
            if (vampire.contains("graftingLevel")) {
                rdbt$vampireData.graftingLevel = vampire.getByte("graftingLevel");
            }
            if (vampire.contains("fleshBudLevel")) {
                rdbt$vampireData.fleshBudLevel = vampire.getByte("fleshBudLevel");
            }
            if (vampire.contains("daggerSplatterLevel")) {
                rdbt$vampireData.daggerSplatterLevel = vampire.getByte("daggerSplatterLevel");
            }
            if (vampire.contains("jumpLevel")) {
                rdbt$vampireData.jumpLevel = vampire.getByte("jumpLevel");
            }
            if (vampire.contains("ripperEyesLevel")) {
                rdbt$vampireData.ripperEyesLevel = vampire.getByte("ripperEyesLevel");
            }
            if (vampire.contains("freezeLevel")) {
                rdbt$vampireData.freezeLevel = vampire.getByte("freezeLevel");
            }
        }

        PowerTypes.initializeStandPower(this);

        if (compoundtag2.contains("guard")){
            ((StandUser)this).sba$setGuardPointsLoad(compoundtag2.getFloat("guard"));
            if (compoundtag2.contains("guard_break")) {
                ((StandUser) this).sba$setGuardBroken(compoundtag2.getBoolean("guard_break"));
            }
        }
        //sba$maskInventory.addItem()
    }

    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "hurtCurrentlyUsedShield", at = @At(value = "HEAD"), cancellable = true)
    protected void sbaDamageShield(float amount, CallbackInfo ci) {
        StandUser user = ((StandUser) this);
        if (user.sba$getLogSource() != null){
        if (PowerTypes.hasPowerActive(this)){
            IPowersPlayer ipp = ((IPowersPlayer) this);
            ipp.rdbt$getPowers().onHitGuard(amount,user.sba$getLogSource());
        }
        if (PowerTypes.hasStandActive(this)){
            user.sba$getStandPowers().onHitGuard(amount,user.sba$getLogSource());
        }
        }
        if (user.sba$isGuarding() || user.sba$getStandPowers().isSpecialGuarding()) {
            if (user.sba$getLogSource() != null && !user.sba$getLogSource().is(DamageTypeTags.BYPASSES_COOLDOWN) && user.sba$getGuardCooldown() > 0) {
                return;
            }


            user.sba$damageGuard(amount);
            ci.cancel();
        }
    }
    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$Jump(CallbackInfo ci) {
        if (((StandUser) this).sba$isClashing() || ((StandUser) this).sba$getStandPowers().cancelJump()
                || ((IFatePlayer)this).rdbt$getFatePowers().cancelJump() || ((StandUser)this).sba$getGrabberSabbath() != 0
        || FateTypes.isTransforming(this) ||
        FateTypes.takesSunlightDamage(this) && FateTypes.isInSunlight(this)
                && !FateTypes.canCurrentlyAvoidSunlight(this)&& !FateTypes.isHidden(this)) {
            if (!FateTypes.isHidden(this)) {
                ci.cancel();
            }
        }
    }

    /**stand mining intercepts tools for drop so that it is hand level*/
    @Inject(method = "hasCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$hasCorrectTool(BlockState $$0, CallbackInfoReturnable<Boolean> cir) {
        if (PowerTypes.hasPowerActive(this) && ((IPowersPlayer) this).rdbt$getPowers().isMining()
        ) {

            int MiningTier = ((IPowersPlayer) this).rdbt$getPowers().getMiningLevel();
            if (MiningTier >= 4){
                cir.setReturnValue(Items.DIAMOND_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else if (MiningTier == 3){
                cir.setReturnValue(Items.IRON_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else if (MiningTier == 2){
                cir.setReturnValue(Items.STONE_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else if (MiningTier == 1){
                cir.setReturnValue(Items.WOODEN_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else {
                cir.setReturnValue(!$$0.requiresCorrectToolForDrops());
            }
            return;
        }
        StandPowers powers = ((StandUser) this).sba$getStandPowers();
        if (PowerTypes.hasStandActive(this) &&
                (powers.canUseMiningStand() || powers.isMiningRegardless())

        ) {
            int MiningTier = ((StandUser) this).sba$getStandPowers().getMiningLevel();
            if (MiningTier >= 4){
                cir.setReturnValue(Items.DIAMOND_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else if (MiningTier == 3){
                cir.setReturnValue(Items.IRON_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else if (MiningTier == 2){
                cir.setReturnValue(Items.STONE_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else if (MiningTier == 1){
                cir.setReturnValue(Items.WOODEN_PICKAXE.isCorrectToolForDrops($$0) || !$$0.requiresCorrectToolForDrops());
            } else {
                cir.setReturnValue(!$$0.requiresCorrectToolForDrops());
            }
            return;
        }
        if (this.inventory.getSelected().getItem() instanceof WarhammerItem wi) {
            if ($$0.getBlock() instanceof StoneMaskBlock){
                cir.setReturnValue(false);
                return;
            }
            if ($$0.requiresCorrectToolForDrops() && MainUtil.confirmIsOre($$0) &&
            wi.isCorrectToolForDrops($$0)){
                cir.setReturnValue(true);
                return;
            } else {
                cir.setReturnValue(!$$0.requiresCorrectToolForDrops() && !($$0.getBlock() instanceof StoneMaskBlock));
            }
            return;
        }
    }

    /**stand mining intercepts mining speed as well*/
    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$getDestroySpeed2(BlockState $$0, CallbackInfoReturnable<Float> cir) {
        StandPowers powers = ((StandUser) this).sba$getStandPowers();
        GeneralPowers gp = ((IPowersPlayer)this).rdbt$getPowers();
        if (PowerTypes.hasStandActive(this) &&
                (powers.canUseMiningStand()) || powers.isMiningRegardless()|| PowerTypes.isBrawling(this)) {
            cir.setReturnValue(rdbt$mutualMiningSpeedFunction($$0,powers));
            return;
        }
        if (PowerTypes.isUsingPower(this) &&
                (((IPowersPlayer)this).rdbt$getPowers().isMining()
                        || PowerTypes.isBrawling(this))){
            cir.setReturnValue(rdbt$mutualMiningSpeedFunction2($$0,gp));
            return;
        }
    }


    @Override
    @Unique
    public float rdbt$mutualMiningSpeedFunction2(BlockState $$0, GeneralPowers powers){
        float mspeed;
        if (!$$0.is(BlockTags.MINEABLE_WITH_PICKAXE)){
            if ($$0.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                mspeed = powers.getShovelMiningSpeed() / 2;
            } else if ($$0.is(BlockTags.MINEABLE_WITH_AXE)){
                mspeed = powers.getAxeMiningSpeed() / 2;
            } else {
                mspeed= powers.getSwordMiningSpeed()/4;
            }
        } else {
            mspeed= powers.getPickMiningSpeed()*3;
        }


        if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            mspeed /= 5.0F;
        }

        if (!this.onGround()) {
            mspeed /= 5.0F;
        }

        if (this.isCrouching() && MainUtil.confirmIsOre($$0) && ClientNetworking.getAppropriateConfig().generalStandSettings.crouchingStopsStandsFromMiningOres) {
            mspeed = 0.0F;
        }

        if ($$0.is(Blocks.COBWEB)){
            mspeed *= 5.0F;
        }

        if (MobEffectUtil.hasDigSpeed(this)) {
            mspeed *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1;
            switch (this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            mspeed *= f1;
        }

        mspeed *= powers.getMiningMultiplier();
        return  mspeed;
    }

    @Override
    @Unique
    public float rdbt$mutualMiningSpeedFunction(BlockState $$0,StandPowers powers){
        float mspeed;
        if (!$$0.is(BlockTags.MINEABLE_WITH_PICKAXE)){
            if ($$0.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                mspeed = powers.getShovelMiningSpeed() / 2;
            } else if ($$0.is(BlockTags.MINEABLE_WITH_AXE)){
                mspeed = powers.getAxeMiningSpeed() / 2;
            } else {
                mspeed= powers.getSwordMiningSpeed()/4;
            }
        } else {
            mspeed= powers.getPickMiningSpeed()*3;
        }

        if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            mspeed /= 5.0F;
        }

        if (!this.onGround()) {
            mspeed /= 5.0F;
        }

        if (this.isCrouching() && MainUtil.confirmIsOre($$0) && ClientNetworking.getAppropriateConfig().generalStandSettings.crouchingStopsStandsFromMiningOres) {
            mspeed = 0.0F;
        }

        if ($$0.is(Blocks.COBWEB)){
            mspeed *= 5.0F;
        }

        if (MobEffectUtil.hasDigSpeed(this)) {
            mspeed *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1;
            switch (this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            mspeed *= f1;
        }

        mspeed *= powers.getMiningMultiplier();
        return  mspeed;
    }

    /**If you are in a barrage, does not play the hurt sound*/
    @Inject(method = "getHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$GetHurtSound(DamageSource $$0, CallbackInfoReturnable<SoundEvent> ci) {
        if (((StandUser) this).sba$isDazed() || $$0.is(ModDamageTypes.STAND_RUSH)) {
            ci.setReturnValue(null);
        } else {
            ShapeShifts shift = ShapeShifts.getShiftFromByte(sba$getShapeShift());
            if (shift != ShapeShifts.PLAYER) {
                if (shift == ShapeShifts.ZOMBIE) {
                    ci.setReturnValue(SoundEvents.ZOMBIE_HURT);
                } else if (shift == ShapeShifts.SKELETON) {
                    ci.setReturnValue(SoundEvents.SKELETON_HURT);
                } else if (shift == ShapeShifts.STRAY) {
                    ci.setReturnValue(SoundEvents.STRAY_HURT);
                } else if (shift == ShapeShifts.WITHER_SKELETON) {
                    ci.setReturnValue(SoundEvents.WITHER_SKELETON_HURT);
                } else if (shift == ShapeShifts.VILLAGER) {
                    ci.setReturnValue(SoundEvents.VILLAGER_HURT);
                }
            }
        }
    }
    @Inject(method = "playStepSound", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$playStepSound(BlockPos $$0, BlockState $$1, CallbackInfo ci) {
        ShapeShifts shift = ShapeShifts.getShiftFromByte(sba$getShapeShift());
        if (shift != ShapeShifts.PLAYER) {
            if (shift == ShapeShifts.ZOMBIE) {
                this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
                ci.cancel();
            } else if (shift == ShapeShifts.SKELETON) {
                this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
                ci.cancel();
            } else if (shift == ShapeShifts.STRAY) {
                this.playSound(SoundEvents.STRAY_STEP, 0.15F, 1.0F);
            } else if (shift == ShapeShifts.WITHER_SKELETON) {
                this.playSound(SoundEvents.WITHER_SKELETON_STEP, 0.15F, 1.0F);
                ci.cancel();
            } else if (shift == ShapeShifts.VILLAGER) {
                super.playStepSound($$0,$$1);
                ci.cancel();
            }
        }
    }

    /**Combat mode goes down when you don't have a stand or power active NO MATTER WHAT*/
    @Unique
    public void sba$tickStandOrStandless(){
        if (!((StandUser)this).sba$getStandPowers().hasStandActive(((LivingEntity) (Object)this))){
            sba$setCombatMode(false);
        }
    }

    @Unique
    public int rdbt$flagTicks = 0;
    @Unique
    @Override
    public int rdbt$getFlagTicks(){
        return rdbt$flagTicks;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$Tick(CallbackInfo ci) {
        if (rdbt$levelDecreaseTicks > 0){
            rdbt$levelDecreaseTicks--;
        }
        rdbt$movementHistory.addLast(this.position());
        if (rdbt$movementHistory.size() > 10) {
            rdbt$movementHistory.removeFirst();
        }

        sba$tickStandOrStandless();
        if (this.level().isClientSide()) {
            if (ClientUtil.isPlayer(this)) {
                GERNetherIllusionHelper.update((Player) (Object) this);
            }
            if (MainUtil.isHoldingBanner(this)){
                if (rdbt$flagTicks < 5){
                    rdbt$flagTicks++;
                }
            } else {
                rdbt$flagTicks = 0;
            }
            if (FateTypes.isVampire(this) && ClientUtil.isPlayer(this)){
                if (rdbt$getVampireData().vampireLevel == -1){
                    rdbt$getVampireData().vampireLevel = 0;
                    C2SPacketUtil.trySingleBytePacket(PacketDataIndex.QUERY_VAMPIRE_UPDATE);
                }
            }

            sba$setupAnimationStates();


            if (!rdbt$getCooldownQuery() && ClientUtil.isPlayer(this)){
                if (!rdbt$attemptedQuery){
                    rdbt$attemptedQuery = true;
                    C2SPacketUtil.handShakeCooldownPacket();
                }

            }
        } else {
            FishingHook hook = fishing;

            if (hook != null) {
                if (PowerTypes.isBrawling(this) || ((StandUser)this).sba$getEffectiveCombatMode()){
                    hook.discard();
                    fishing = null;
                }
            }

            PowerTypes.fixPowers(this);
            sba$qmessageTick();
            byte pos = sba$GetPos2();
            if (pos > 0) {
                if (!PowerTypes.canKeepGuardPos(this) && pos == PlayerPosIndex.GUARD) {
                    sba$SetPos2(PlayerPosIndex.NONE);
                } else if (!PowerTypes.canKeepBarrageChargePos(this) && pos == PlayerPosIndex.BARRAGE_CHARGE) {
                    sba$SetPos2(PlayerPosIndex.NONE);
                } else if (!PowerTypes.canKeepBarragePos(this) && pos == PlayerPosIndex.BARRAGE) {
                    sba$SetPos2(PlayerPosIndex.NONE);
                } else if (!PowerTypes.canChargeShotPos(this) && pos == PlayerPosIndex.CHARGE_SHOT) {
                    sba$SetPos2(PlayerPosIndex.NONE);
                }
            }
        }
        if (!(this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted())) {
            ((StandUser) this).sba$setRestrainedTicks(-1);
        }

        if (sba$getCameraHits() > -1){
            sba$setCameraHits(sba$getCameraHits()-1);
        }

        boolean notSkybound = (this.onGround() || this.isSwimming()|| this.onClimbable() || this.isPassenger()
                || this.hasEffect(MobEffects.LEVITATION));

        if (!((TimeStop) ((Player)(Object) this).level()).getTimeStoppingEntities().isEmpty()) {
            if (((TimeStop) ((Player) (Object) this).level()).CanTimeStopEntity(((Player) (Object) this))) {
                ci.cancel();
                return;
            } else if ((((TimeStop) ((Player) (Object) this).level()).isTimeStoppingEntity(((Player) (Object) this)))) {
                ((StandUser) this).sba$setIdleTime(-1);
                sba$airTime = 0;
            } else {
                if (notSkybound || this.isInWater()){
                    sba$airTime = 0;
                } else {
                    sba$airTime += 1;
                }
            }
        } else {
            if (notSkybound || this.isInWater()){
                sba$airTime = 0;
            } else {
                sba$airTime += 1;
            }
        }

        if (!this.isDeadOrDying()) {
            if (sba$getVoiceData() != null){
                sba$getVoiceData().playOnTick();
            }
        }

    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void sba$Tick2(CallbackInfo ci) {
        if (this.isSleeping()){
            if (this.hasEffect(ModEffects.CAPTURING_LOVE)){
                StandUser user = ((StandUser)this);
                user.sba$setSafeToRemoveLove(true);
                this.removeEffect(ModEffects.CAPTURING_LOVE);
            }
        }

        if (((StandUser)this).sba$getAttackTimeDuring() > -1 || this.isUsingItem()) {
            ((StandUser) this).sba$setIdleTime(-1);
        } else if (!new Vec3(this.getX(), this.getY(), this.getZ()).equals(new Vec3(this.xOld, this.yOld, this.zOld))) {
            ((StandUser) this).sba$setIdleTime(-1);
        } else {
            ((StandUser) this).sba$setIdleTime(((StandUser) this).sba$getIdleTime() + 1);
        }
        ((StandUser) this).sba$getStandPowers().tickPowerEnd();
    }

    @Unique
    public VoiceData sba$voiceData;
    @Override
    @Unique
    public VoiceData sba$getVoiceData() {
        return sba$voiceData;
    }
    @Override
    @Unique
    public void sba$setVoiceData(VoiceData vd) {
        sba$voiceData = vd;
    }
    @Override
    @Unique
    public void rdbt$onSyncedDataUpdated(EntityDataAccessor<?> $$0){
        if ($$0.equals(SBA$POSE_EMOTE)){
            byte posEmote = this.sba$GetPoseEmote();
            if (posEmote != Poses.NONE.id) {
                this.getStyleAnimation().start(this.tickCount);
            } else {
                this.getStyleAnimation().stop();
            }
        }
        ((StandUser)this).rdbt$synchedData($$0);
    }
    @Override
    @Unique
    public void sba$addKnife() {
        byte knifeCount = this.entityData.get(SBA$DATA_KNIFE_COUNT_ID);

        knifeCount++;
        if (knifeCount <= ClientNetworking.getAppropriateConfig().itemSettings.maxKnivesInOneHit){
            ((LivingEntity) (Object) this).getEntityData().set(SBA$DATA_KNIFE_COUNT_ID, knifeCount);
        }
    }
    @Override
    @Unique
    public void sba$setKnife(byte knives) {
        ((LivingEntity) (Object) this).getEntityData().set(SBA$DATA_KNIFE_COUNT_ID, knives);
    }

    @Override
    @Unique
    public PlayerMaskSlots sba$getMaskInventory() {
        return sba$maskInventory;
    }
    @Override
    @Unique
    public void sba$setMaskInventory(PlayerMaskSlots pm) {
        this.sba$maskInventory.replaceWith(pm);;
    }
    @Override
    @Unique
    public final ItemStack sba$getMaskSlot() {
        return this.entityData.get(SBA$MASK_SLOT);
    }
    @Override
    @Unique
    public final ItemStack sba$getMaskVoiceSlot() {
        return this.entityData.get(SBA$MASK_VOICE_SLOT);
    }

    @Override
    @Unique
    public void sba$setMaskSlot(ItemStack stack) {
        ((LivingEntity) (Object) this).getEntityData().set(SBA$MASK_SLOT, stack);
    }

    @Override
    @Unique
    public final int sba$getKnifeCount() {
        return this.entityData.get(SBA$DATA_KNIFE_COUNT_ID);
    }
    @Override
    @Unique
    public void sba$setMaskVoiceSlot(ItemStack stack) {
        if (!this.level().isClientSide()) {
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof MaskItem mi) {
                ItemStack stack2 = sba$getMaskVoiceSlot();
                if (!(stack2.equals(stack))) {
                    sba$setVoiceData(mi.visageData.voiceData(((Player) (Object) this)));
                }
            } else {
                sba$setVoiceData(null);
            }
        }
        ((LivingEntity) (Object) this).getEntityData().set(SBA$MASK_VOICE_SLOT, stack);
    }


    @Unique
    @Override
    public boolean sba$getEffectiveCombatMode() {

        if (PowerTypes.isBrawling(this)){
            return true;
        }
        StandUser user = ((StandUser) this);
        if (PowerTypes.hasStandActive(this) && user.sba$getStandPowers().hasPassiveCombatMode()){
            return true;
        }
        if (getEntityData().hasItem(SBA$COMBAT_MODE)) {
            return this.getEntityData().get(SBA$COMBAT_MODE) && (((StandUser)this).sba$hasAStand() &&
                    user.sba$getStandPowers().hasStandActive(((LivingEntity) (Object)this)));
        } else {
            return false;
        }
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void initDataTrackerSBA(CallbackInfo ci) {
        if (!((LivingEntity)(Object)this).getEntityData().hasItem(SBA$POS)) {
            ((LivingEntity) (Object) this).getEntityData().define(SBA$POS, PlayerPosIndex.NONE);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$POS_2, PlayerPosIndex.NONE);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$POSE_EMOTE, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$DODGE_TIME, -1);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$CAMERA_HITS, -1);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$COMBAT_MODE, false);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$DATA_KNIFE_COUNT_ID, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$MASK_SLOT, ItemStack.EMPTY);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$MASK_VOICE_SLOT, ItemStack.EMPTY);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$STAND_LEVEL, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$SHAPE_SHIFT, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$SHAPE_SHIFT_EXTRA, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$WIDTH_FACTOR, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$STAND_EXP, 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$IS_CONTROLLING, 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$TEAM_COLOR, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$FATE, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$POWERS, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$IS_BLINDED, false);
            ((LivingEntity) (Object) this).getEntityData().define(SBA$HAIR_COLOR, new Rotations(rdbt$hairColorX,rdbt$hairColorY,rdbt$hairColorZ));
        }
    }

    @Shadow
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Shadow
    public ItemStack getItemBySlot(EquipmentSlot var1) {
        return null;
    }

    @Shadow
    public void setItemSlot(EquipmentSlot var1, ItemStack var2) {

    }

    @Inject(method = "interactOn(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at=@At("HEAD"), cancellable = true)
    private void sba$interactOn(Entity $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir)
    {
        if (!$$0.level().isClientSide()) {
            if (!this.isSpectator()) {
                ItemStack $$2 = this.getItemInHand($$1);
                if ($$0 instanceof LivingEntity LE && ((StandUser) LE).sba$isBubbleEncased()) {
                    if (!$$2.isEmpty() && ($$2.getItem() instanceof ShearsItem || $$2.getItem() instanceof ScissorItem)) {
                        ((StandUser) LE).sba$setBubbleEncased((byte) 0);
                        this.level().playSound(null, $$0.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                                SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                        ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                                this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                                5, 0.25, 0.25, 0.25, 0.025);
                        cir.setReturnValue(InteractionResult.PASS);
                    }
                }
            }
        }
    }
    @Inject(method = "killedEntity", at=@At("HEAD"))
    private void sba$killedEntity(ServerLevel $$0, LivingEntity $$1, CallbackInfoReturnable<Boolean> cir)
    {
        if (sba$getVoiceData() != null){
            sba$getVoiceData().playIfKilled($$1);
        }
    }
    @Inject(method = "attack", at=@At("HEAD"), cancellable = true)
    private void sba$attack(Entity $$0, CallbackInfo ci)
    {
        if (sba$getVoiceData() != null){
            if ($$0.isAttackable()) {
                if (!$$0.skipAttackInteraction(this)) {
                    sba$getVoiceData().playIfAttacking($$0);
                }
            }
        }
    }

    @Inject(
            method = "getMainArm",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sba$forceMainArm(CallbackInfoReturnable<HumanoidArm> cir) {
        // If Silver Chariot is using a part 5 skin, then force the main hand to be left
        if (sba$SilverChariotForceLeftArm()) {
            cir.setReturnValue(HumanoidArm.LEFT);
        }
    }

    @Unique
    private boolean sba$SilverChariotForceLeftArm() {
        StandUser SU = (StandUser) this;
        if (SU.sba$getStandPowers() instanceof PowersSilverChariot PSC && PowerTypes.hasStandActive(this) && PSC.hasHandsOut() && SilverChariotEntity.isPart5Skin(SU.sba$getStandSkin())) {
            return true;
        }
        return false;
    }

    @Shadow
    public HumanoidArm getMainArm() {
        return null;
    }

    @Shadow public abstract void displayClientMessage(Component component, boolean bl);

    @Shadow
    public abstract boolean isCreative();


    @Shadow
    public abstract Abilities getAbilities();

    @Shadow
    public abstract FoodData getFoodData();

    @Shadow
    public abstract void resetAttackStrengthTicker();

    @Shadow
    @Nullable
    public FishingHook fishing;

    @Inject(method = "killedEntity", at = @At(value = "HEAD"), cancellable = true)
    public void sba$hasLineOfSight(ServerLevel $$0, LivingEntity $$1, CallbackInfoReturnable<Boolean> cir) {
        if (((StandUser)this).sba$getStandPowers().onKilledEntity($$0,$$1)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "die",at = @At(value = "HEAD"))
    public void sba$onDeath(DamageSource $$0, CallbackInfo ci) {
        StandUser SU = (StandUser) this;
        if (SU.sba$getStandPowers() instanceof PowersRatt PR) {
            if (PR.active) {
                PR.active = false;
            }
        } else if (SU.sba$getStandPowers() instanceof PowersBlackSabbath PB){
            if(PB.active){
                PB.active = false;
            }
            if(PB.selecting){
                PB.selecting = false;
            }
                PB.setNull();
        }
    }

 /*   @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",at = @At(value = "HEAD"), cancellable = true)
    public void sba$drop(ItemStack $$0, boolean $$1, boolean $$2, CallbackInfoReturnable<ItemEntity> cir) {
        Player This = (Player)(Object)this;
        StandUser SU = ((StandUser)This);
        if (SU.sba$isPossessed()) {
            This.addItem($$0);
            cir.cancel();
            return;
        }

    } */

    @Inject(method = "canBeSeenAsEnemy",at = @At("HEAD"),cancellable = true)
    private void sba$canBeSeenAsEnemy(CallbackInfoReturnable<Boolean> cir) {
        StandUser user = (StandUser) this;
        if (user.sba$getStandPowers() instanceof PowersTusk PT) {
            if (PT.getActivePower() == PowersTusk.FLATTEN) {
                cir.setReturnValue(false);
            }
        }
    }
}
