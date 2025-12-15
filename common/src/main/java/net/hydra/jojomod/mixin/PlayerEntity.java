package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.hydra.jojomod.event.powers.visagedata.voicedata.VoiceData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ScissorItem;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.item.WorthyArrowItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.PlayerMaskSlots;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
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

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Mixin(Player.class)
public abstract class PlayerEntity extends LivingEntity implements IPlayerEntity{

    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract float getDestroySpeed(BlockState $$0);

    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$POS = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$POS_2 = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$POSE_EMOTE = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$DATA_KNIFE_COUNT_ID = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$TEAM_COLOR = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$WATCH_STYLE = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$FATE = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);

    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$DODGE_TIME = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$CAMERA_HITS = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<ItemStack> ROUNDABOUT$MASK_SLOT = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.ITEM_STACK);
    @Unique
    private static final EntityDataAccessor<ItemStack> ROUNDABOUT$MASK_VOICE_SLOT = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.ITEM_STACK);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$STAND_LEVEL = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$SHAPE_SHIFT = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$SHAPE_SHIFT_EXTRA = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$STAND_EXP = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$IS_CONTROLLING = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$IS_BLINDED = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Rotations> ROUNDABOUT$HAIR_COLOR = SynchedEntityData.defineId(Player.class,
            EntityDataSerializers.ROTATIONS);

    @Shadow
    @Final
    private Inventory inventory;

    /**Used by Harpoon calculations*/
    @Unique
    private int roundabout$airTime = 0;
    @Unique
    private int roundabout$clientDodgeTime = 0;
    @Unique
    private int roundabout$anchorPlace = 55;
    @Unique
    private int roundabout$anchorPlaceAttack = 55;
    @Unique
    private float roundabout$distanceOut = 1.07F;
    @Unique
    private float roundabout$sizePercent = 1F;
    @Unique
    private float roundabout$idleRotation = 0;
    @Unique
    private float roundabout$idleYOffset = 0.1F;

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
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$HAIR_COLOR)) {
            Rotations rot = ((Player) (Object) this).getEntityData().get(ROUNDABOUT$HAIR_COLOR);
            ((Player) (Object) this).getEntityData().set(ROUNDABOUT$HAIR_COLOR,
                    new Rotations(color,rot.getY(),rot.getZ()));
        }
    }
    @Unique
    @Override
    public void rdbt$setHairColorY(float color){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$HAIR_COLOR)) {
            Rotations rot = ((Player) (Object) this).getEntityData().get(ROUNDABOUT$HAIR_COLOR);
            ((Player) (Object) this).getEntityData().set(ROUNDABOUT$HAIR_COLOR,
                    new Rotations(rot.getX(),color,rot.getZ()));
        }
    }
    @Unique
    @Override
    public void rdbt$setHairColorZ(float color){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$HAIR_COLOR)) {
            Rotations rot = ((Player) (Object) this).getEntityData().get(ROUNDABOUT$HAIR_COLOR);
            ((Player) (Object) this).getEntityData().set(ROUNDABOUT$HAIR_COLOR,
                    new Rotations(rot.getX(),rot.getY(),color));
        }
    }
    @Unique
    @Override
    public float rdbt$getHairColorX(){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$HAIR_COLOR)) {
            return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$HAIR_COLOR).getX();
        }
        return rdbt$hairColorX;
    }
    @Unique
    @Override
    public float rdbt$getHairColorY(){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$HAIR_COLOR)) {
            return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$HAIR_COLOR).getY();
        }
        return rdbt$hairColorY;
    }
    @Unique
    @Override
    public float rdbt$getHairColorZ(){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$HAIR_COLOR)) {
            return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$HAIR_COLOR).getZ();
        }
        return rdbt$hairColorZ;
    }


    @Unique
    protected boolean rdbt$cooldownQuery = false;
    @Unique
    protected boolean rdbt$attemptedQuery = false;

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
    public void roundabout$setTeamColor(byte color){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$TEAM_COLOR)) {
            ((Player) (Object) this).getEntityData().set(ROUNDABOUT$TEAM_COLOR, color);
        }
    }
    @Unique
    @Override
    public byte roundabout$getTeamColor(){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$TEAM_COLOR)) {
            return this.entityData.get(ROUNDABOUT$TEAM_COLOR);
        }
        return 0;
    }
    @Unique
    @Override
    public void roundabout$setWatchStyle(byte style){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$WATCH_STYLE)) {
            ((Player) (Object) this).getEntityData().set(ROUNDABOUT$WATCH_STYLE, style);
        }
    }
    @Unique
    @Override
    public byte roundabout$getWatchStyle(){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$WATCH_STYLE)) {
            return this.entityData.get(ROUNDABOUT$WATCH_STYLE);
        }
        return 0;
    }
    @Unique
    @Override
    public void roundabout$setFate(byte style){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$FATE)) {
            this.getEntityData().set(ROUNDABOUT$FATE, style);
        }
    }
    @Unique
    @Override
    public byte roundabout$getFate(){
        if (((Player)(Object)this).getEntityData().hasItem(ROUNDABOUT$FATE)) {
            return this.entityData.get(ROUNDABOUT$FATE);
        }
        return 0;
    }


    ///  hacky solution for really odd anubis bug, check this if something breaks
    @Inject(method = "jumpFromGround",at=@At(value = "HEAD"))
    public void huhh(CallbackInfo ci) {
        Player player = (Player)(Object)(this);
        if ( ((StandUser)player).roundabout$getStandPowers() instanceof PowersAnubis PA && ((StandUser)player).roundabout$getUniqueStandModeToggle()) {
            float bigJump = ((StandUser) player).roundabout$getBonusJumpHeight();
            float totalHeight = bigJump + 1;
            boolean canJump = bigJump > 0;
            boolean isJumping = ((StandUser) player).roundabout$getBigJump();
            float getCurrentJump = ((StandUser) player).roundabout$getBigJumpCurrentProgress();

            if (canJump) {
                if (player.getAbilities().flying) {
                    if (isJumping) {
                        this.roundabout$SetBonusJump(false, totalHeight, getCurrentJump);
                    }
                } else {
                    if (isJumping && player.onGround()) {
                        this.roundabout$SetBonusJump(false, totalHeight, getCurrentJump);
                    }
                    if (isJumping && player.onGround() && getCurrentJump > 0) {
                    } else {
                        if (player.onGround() || isJumping) {
                            this.roundabout$SetBonusJump(true, totalHeight, getCurrentJump);
                        }
                    }
                }
            }
        }
    }

    @Unique
    public void roundabout$SetBonusJump(boolean bigJump, float jumpHeight, float current){
        ((StandUser)this).roundabout$setBigJump(bigJump);
        if (bigJump){
            C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_BIG_JUMP,current);
        } else {
            C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_BIG_JUMP_CANCEL,current);
        }
    }

    @Unique
    @Override
    public void roundabout$qmessage(int messageID){
        rdbt$qmessage = messageID;
        rdbt$qmessageTime = 60;
    }
    public int rdbt$qmessage = 0;
    public int rdbt$qmessageTime = 0;
    @Unique
    public void roundabout$qmessageTick(){
        if (rdbt$qmessageTime > 0){
            rdbt$qmessageTime--;

            if (rdbt$qmessageTime == 0){
                switch (rdbt$qmessage){
                    case 1 -> displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireStand_2").withStyle(ChatFormatting.WHITE), true);
                    case 2 -> rdbt$vamp1();
                    case 3 -> rdbt$vamp2();
                    case 4 -> rdbt$vamp3();
                    default -> displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireStand").withStyle(ChatFormatting.WHITE), true);
                }
            }
        }
    }

    @Unique
    public void rdbt$vamp1(){
        displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireVampire2").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.BOLD), true);
        roundabout$qmessage(3);
    }
    @Unique
    public void rdbt$vamp2(){
        displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireVampire3").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.BOLD), true);
        roundabout$qmessage(4);
    }
    @Unique
    public void rdbt$vamp3(){
        displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireVampire4").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD), true);
        S2CPacketUtil.vampireMessage(((Player) (Object)this));
    }


    @Unique
    private PlayerMaskSlots roundabout$maskInventory = new PlayerMaskSlots(((Player)(Object)this));

    protected PlayerEntity(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Unique
    public Inventory roundabout$GetInventory(){
        return inventory;
    }

    /**Keep track of unique player animations like floating passive anims like dodging or posing*/
    public void roundabout$SetPos(byte Pos){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$POS, Pos);
    }
    public byte roundabout$GetPos(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$POS);
    }
    public void roundabout$SetPos2(byte Pos){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$POS_2, Pos);
    }
    public byte roundabout$GetPos2(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$POS_2);
    }
    public void roundabout$SetPoseEmote(byte Pos){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$POSE_EMOTE, Pos);
    }
    public byte roundabout$GetPoseEmote(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$POSE_EMOTE);
    }
    public float roundabout$getSizePercent(){
        return roundabout$sizePercent;
    }
    public float roundabout$getIdleRotation(){
        return roundabout$idleRotation;
    }
    public float roundabout$getIdleYOffset(){
        return roundabout$idleYOffset;
    }
    @Unique
    @Override
    public void roundabout$setAnchorPlace(int anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,360);
        this.roundabout$anchorPlace = anchorPlace;
        StandEntity ent = ((StandUser)this).roundabout$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setAnchorPlace(anchorPlace);
        }
    }
    @Unique
    @Override
    public void roundabout$setAnchorPlaceAttack(int anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,360);
        this.roundabout$anchorPlaceAttack = anchorPlace;
        StandEntity ent = ((StandUser)this).roundabout$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setAnchorPlaceAttack(anchorPlace);
        }
    }
    @Unique
    @Override
    public void roundabout$setSizePercent(float anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,3);
        this.roundabout$sizePercent = anchorPlace;
        StandEntity ent = ((StandUser)this).roundabout$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setSizePercent(anchorPlace);
        }
    }
    @Unique
    @Override
    public void roundabout$setIdleRotation(float anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,360);
        this.roundabout$idleRotation = anchorPlace;
        StandEntity ent = ((StandUser)this).roundabout$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setIdleRotation(anchorPlace);
        }
    }
    @Unique
    @Override
    public void roundabout$setIdleYOffset(float anchorPlace){
        anchorPlace = Mth.clamp(anchorPlace,0,2);
        this.roundabout$idleYOffset = anchorPlace;
        StandEntity ent = ((StandUser)this).roundabout$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setIdleYOffset(anchorPlace);
        }
    }
    @Unique
    @Override
    public int roundabout$getAnchorPlace(){
        return this.roundabout$anchorPlace;
    }
    @Unique
    @Override
    public int roundabout$getAnchorPlaceAttack(){
        return this.roundabout$anchorPlaceAttack;
    }
    @Unique
    @Override
    public void roundabout$setDistanceOut(float distanceOut){
        distanceOut = Mth.clamp(distanceOut,0,2F);
        this.roundabout$distanceOut = distanceOut;
        StandEntity ent = ((StandUser)this).roundabout$getStand();
        if (ent instanceof FollowingStandEntity FE){
            FE.setDistanceOut(distanceOut);
        }
    }
    @Unique
    @Override
    public float roundabout$getDistanceOut(){
        return this.roundabout$distanceOut;
    }
    @Unique
    @Override
    public void roundabout$setClientDodgeTime(int dodgeTime){
        roundabout$clientDodgeTime = dodgeTime;
    }
    @Unique
    @Override
    public void roundabout$setDodgeTime(int dodgeTime){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$DODGE_TIME, dodgeTime);
    }
    @Unique
    @Override
    public int roundabout$getClientDodgeTime(){
        return roundabout$clientDodgeTime;
    }
    @Unique
    @Override
    public int roundabout$getDodgeTime(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$DODGE_TIME);
    }
    @Unique
    @Override
    public int roundabout$getCameraHits(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$CAMERA_HITS);
    }
    @Unique
    @Override
    public void roundabout$setCameraHits(int ticks){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$CAMERA_HITS, ticks);
    }

    @Unique
    @Override
    public int roundabout$getAirTime(){
        return this.roundabout$airTime;
    }

    @Shadow
    protected boolean wantsToStopRiding() {
        return false;
    }

    @Unique
    public boolean roundabout$heldDownSwitchExp = false;
    @Unique
    public boolean roundabout$displayExp = false;

    @Unique
    public boolean roundabout$displayNamePossible = true;


    /**Crit / Critical Hits!*/
    @ModifyVariable(method = "attack",
            at = @At(value = "STORE"), ordinal = 2)
    public boolean roundabout$attackThis(boolean value) {
        StandUser SU = ((StandUser) this );
        if (SU.roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.hasExtendedHeelsForWalking()){
            return true;
        }
        if ( SU.roundabout$isPossessed() ) {
            return true;
        }
        return value;
    }


    @Override
    @Unique
    public void roundabout$setShowName(boolean boo){
        roundabout$displayNamePossible = boo;
    }
    @Inject(method = "shouldShowName",
            at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$shouldShowName(CallbackInfoReturnable<Boolean> cir) {
        if (!roundabout$displayNamePossible){
            cir.setReturnValue(false);
        }
    }
    @Override
    @Unique
    public void roundabout$showExp(boolean keyIsDown){
        if (!roundabout$heldDownSwitchExp){
            if (keyIsDown){
                roundabout$heldDownSwitchExp = true;
                if (roundabout$displayExp){
                    roundabout$displayExp=false;
                } else {
                    roundabout$displayExp=true;
                }
            }
        } else {
            if (!keyIsDown){
                roundabout$heldDownSwitchExp = false;
            }
        }
    }

    @Override
    @Unique
    public boolean roundabout$getDisplayExp(){
        return roundabout$displayExp;
    }

    @Override
    @Unique
    public void roundabout$setStandLevel(byte level){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$STAND_LEVEL, level);
    }
    @Override
    @Unique
    public byte roundabout$getStandLevel(){
        byte levl = ((Player) (Object) this).getEntityData().get(ROUNDABOUT$STAND_LEVEL);
        levl = (byte) Math.max(levl,1);
        return levl;
    }
    @Override
    @Unique
    public void roundabout$setShapeShift(byte level){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$SHAPE_SHIFT, level);
    }
    @Override
    @Unique
    public void roundabout$shapeShift(){
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
    public void roundabout$shapeShiftSilent(){
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
    public void roundabout$setShapeShiftExtraData(byte level){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$SHAPE_SHIFT_EXTRA, level);
    }
    @Override
    @Unique
    public byte roundabout$getShapeShift(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$SHAPE_SHIFT);
    }
    @Override
    @Unique
    public byte roundabout$getShapeShiftExtraData(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$SHAPE_SHIFT_EXTRA);
    }
    @Override
    @Unique
    public void roundabout$setStandExp(int level){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$STAND_EXP, level);
    }
    @Override
    @Unique
    public int roundabout$getControlling(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$IS_CONTROLLING);
    }
    @Override
    @Unique
    public void roundabout$setIsControlling(int pilot){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$IS_CONTROLLING, pilot);
    }
    @Override
    @Unique
    public boolean roundabout$getBlinded(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$IS_BLINDED);
    }
    @Override
    @Unique
    public void roundabout$setBlinded(boolean blinded){
        ((Player) (Object) this).getEntityData().set(ROUNDABOUT$IS_BLINDED, blinded);
    }


    @Override
    @Unique
    public void roundabout$addStandExp(int amt){
        if (ClientNetworking.getAppropriateConfig().standLevelingSettings.enableStandLeveling) {
            int currentExp = roundabout$getStandExp();
            currentExp += amt;
            byte level = this.roundabout$getStandLevel();
            StandPowers powers = ((StandUser) this).roundabout$getStandPowers();
            int maxLevel = powers.getMaxLevel();
            if (currentExp >= powers.getExpForLevelUp(level) && level < maxLevel) {
                ((Player) (Object) this).getEntityData().set(ROUNDABOUT$STAND_EXP, 0);
                ((Player) (Object) this).getEntityData().set(ROUNDABOUT$STAND_LEVEL, (byte) (level + 1));
                powers.levelUp();
            } else {
                ((Player) (Object) this).getEntityData().set(ROUNDABOUT$STAND_EXP, currentExp);
            }
        }
    }
    @Override
    @Unique
    public int roundabout$getStandExp(){
        return ((Player) (Object) this).getEntityData().get(ROUNDABOUT$STAND_EXP);
    }

    @Unique
    public boolean roundabout$unlockedBonusSkin = false;
    @Override
    @Unique
    public void roundabout$setUnlockedBonusSkin(boolean bl){
        roundabout$unlockedBonusSkin = bl;
    }
    @Override
    @Unique
    public boolean roundabout$getUnlockedBonusSkin(){
        return roundabout$unlockedBonusSkin;
    }
    @Inject(method = "canSprint", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$canSprintPlayer(CallbackInfoReturnable<Boolean> cir) {
        if ( ((StandUser)this).roundabout$getStandPowers().cancelSprint()
                || ((IFatePlayer)this).rdbt$getFatePowers().cancelSprint()){
            cir.setReturnValue(false);
        }
    }
    /**Attack Speed Decreases when your hand is stone*/
    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getCurrentItemAttackStrengthDelay(CallbackInfoReturnable<Float> cir) {
        byte curse = ((StandUser)this).roundabout$getLocacacaCurse();
        float modifier = 1;
        if (curse > -1) {
            if ((curse == LocacacaCurseIndex.RIGHT_HAND && this.getMainArm() == HumanoidArm.RIGHT)
            || (curse == LocacacaCurseIndex.LEFT_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                modifier = 0.6F;
            }
        }
        boolean standActive = ((StandUser) this).roundabout$getActive();
        if (standActive){
            modifier*= ((StandUser)this).roundabout$getStandPowers().getBonusAttackSpeed();
        }
        if (modifier != 1){
            cir.setReturnValue((float)(1.0D / (this.getAttributeValue(Attributes.ATTACK_SPEED)*modifier) * 20.0D));
        }
    }

    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "TAIL"))
    public void roundabout$actuallyHurt2(DamageSource $$0, float $$1, CallbackInfo ci) {
        if (!this.isInvulnerableTo($$0)) {
            if (!this.isDeadOrDying()) {
                if (roundabout$getVoiceData() != null){
                    roundabout$getVoiceData().playIfHurt($$0);
                }
            }
        }
    }

    /**Block Breaking Speed Decreases when your hand is stone*/
    @Unique
    private boolean roundabout$destroySpeedRecursion = false;
    @Inject(method = "getDestroySpeed", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getDestroySpeed(BlockState $$0, CallbackInfoReturnable<Float> cir) {
        byte curse = ((StandUser)this).roundabout$getLocacacaCurse();
        if (!roundabout$destroySpeedRecursion) {
            roundabout$destroySpeedRecursion = true;
            float dSpeed = this.getDestroySpeed($$0);
            roundabout$destroySpeedRecursion = false;
            if (curse > -1 && (curse == LocacacaCurseIndex.RIGHT_HAND && this.getMainArm() == HumanoidArm.RIGHT)
                    || (curse == LocacacaCurseIndex.LEFT_HAND && this.getMainArm() == HumanoidArm.LEFT)) {
                dSpeed *= 0.6F;
            }

            boolean standActive = ((StandUser) this).roundabout$getActive();
            if (standActive){
                dSpeed*= ((StandUser)this).roundabout$getStandPowers().getBonusPassiveMiningSpeed();
            }
            cir.setReturnValue(dSpeed);
        }
    }

    @Inject(method = "actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$actuallyHurt(DamageSource $$0, float $$1, CallbackInfo ci) {
        if (!this.isInvulnerableTo($$0) && $$1 > 0) {


            if ($$0.getEntity() instanceof Player pe && !$$0.isIndirect()
                    && !$$0.is(DamageTypes.THORNS)){
                if (((StandUser)pe).roundabout$getStandPowers().interceptSuccessfulDamageDealtEvent($$0,$$1, ((LivingEntity)(Object)this))){
                    ci.cancel();
                    return;
                }
            }

            if ($$0.getEntity() instanceof LivingEntity livent){
                ShapeShifts shift = ShapeShifts.getShiftFromByte(this.roundabout$getShapeShift());
                if (shift != ShapeShifts.PLAYER) {
                    if (ShapeShifts.isVillager(shift)) {
                        AABB aab = this.getBoundingBox().inflate(10.0, 8.0, 10.0);
                        List<? extends LivingEntity> le = this.level().getNearbyEntities(IronGolem.class, roundabout$attackTargeting, ((LivingEntity)(Object)this), aab);
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


            Entity bound = ((StandUser)this).roundabout$getBoundTo();
            if (bound != null && !$$0.isIndirect() && !$$0.is(ModDamageTypes.STAND_FIRE)){
                ((StandUser)this).roundabout$dropString();
            }
        }

        if (((StandUser)this).roundabout$mutualActuallyHurt($$0,$$1)){
            ci.cancel();
        }
    }

    private final TargetingConditions roundabout$attackTargeting = TargetingConditions.forCombat().range(64.0);
    @Unique
    public Poses roundabout$standPos = null;
    @Unique
    public final AnimationState roundabout$WRYYY = new AnimationState();
    @Unique
    @Override
    public AnimationState getWry(){
        return roundabout$WRYYY;
    }
    @Unique
    public final AnimationState roundabout$BubbleAim = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getBubbleAim(){
        return roundabout$BubbleAim;
    }
    @Unique
    public final AnimationState roundabout$OffsetCorrect = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getOffsetCorrect(){
        return roundabout$OffsetCorrect;
    }

    @Unique
    public final AnimationState roundabout$anubishUnsheath = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getAnubisUnsheath(){
        return roundabout$anubishUnsheath;
    }
    @Unique
    public final AnimationState roundabout$thirdPersonAnubishUnsheath = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getThirdPersonAnubisUnsheath(){
        return roundabout$thirdPersonAnubishUnsheath;
    }

    @Unique
    public final AnimationState roundabout$snubnoseAim = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseAim(){
        return roundabout$snubnoseAim;
    }
    @Unique
    public final AnimationState roundabout$snubnoseRecoil = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseRecoil(){
        return roundabout$snubnoseRecoil;
    }
    @Unique
    public final AnimationState roundabout$snubnoseModelAim = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseModelAim(){
        return roundabout$snubnoseModelAim;
    }
    @Unique
    public final AnimationState roundabout$snubnoseModelRecoil = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseModelRecoil(){
        return roundabout$snubnoseModelRecoil;
    }
    @Unique
    public final AnimationState roundabout$snubnoseAimLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseAimLeft(){
        return roundabout$snubnoseAimLeft;
    }
    @Unique
    public final AnimationState roundabout$snubnoseRecoilLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseRecoilLeft(){
        return roundabout$snubnoseRecoilLeft;
    }
    @Unique
    public final AnimationState roundabout$snubnoseModelAimLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseModelAimLeft(){
        return roundabout$snubnoseModelAimLeft;
    }
    @Unique
    public final AnimationState roundabout$snubnoseModelRecoilLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getSnubnoseModelRecoilLeft(){
        return roundabout$snubnoseModelRecoilLeft;
    }

    @Unique
    public final AnimationState roundabout$tommyAim = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyAim(){
        return roundabout$tommyAim;
    }
    @Unique
    public final AnimationState roundabout$tommyModelAim = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyModelAim(){
        return roundabout$tommyModelAim;
    }
    @Unique
    public final AnimationState roundabout$tommyAimLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyAimLeft(){
        return roundabout$tommyAimLeft;
    }
    @Unique
    public final AnimationState roundabout$tommyModelAimLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyModelAimLeft(){
        return roundabout$tommyModelAimLeft;
    }
    @Unique
    public final AnimationState roundabout$tommyRecoil = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyRecoil(){
        return roundabout$tommyRecoil;
    }
    @Unique
    public final AnimationState roundabout$tommyModelRecoil = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyModelRecoil(){
        return roundabout$tommyModelRecoil;
    }
    @Unique
    public final AnimationState roundabout$tommyRecoilLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyRecoilLeft(){
        return roundabout$tommyRecoilLeft;
    }
    @Unique
    public final AnimationState roundabout$tommyModelRecoilLeft = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getTommyModelRecoilLeft(){
        return roundabout$tommyModelRecoilLeft;
    }


    @Unique
    public final AnimationState roundabout$BubbleShotAim = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getBubbleShotAim(){
        return roundabout$BubbleShotAim;
    }
    @Unique
    public int roundabout$BubbleShotAimPoints = 0;
    @Unique
    @Override
    public int roundabout$getBubbleShotAimPoints(){
        return roundabout$BubbleShotAimPoints;
    }
    @Unique
    @Override
    public void roundabout$setBubbleShotAimPoints(int shotPoints){
        roundabout$BubbleShotAimPoints = shotPoints;
    }
    @Unique
    public final AnimationState roundabout$GIORNO = new AnimationState();

    @Unique
    @Override
    public AnimationState getGiorno(){
        return roundabout$GIORNO;
    }
    @Unique
    public final AnimationState roundabout$JOSEPH = new AnimationState();
    @Unique
    @Override
    public AnimationState getJoseph(){
        return roundabout$JOSEPH;
    }
    @Unique
    public final AnimationState roundabout$KOICHI = new AnimationState();
    @Unique
    @Override
    public AnimationState getKoichi(){
        return roundabout$KOICHI;
    }
    @Unique
    public final AnimationState roundabout$OH_NO = new AnimationState();
    @Unique
    @Override
    public AnimationState getOhNo(){return roundabout$OH_NO;}
    @Unique
    public final AnimationState roundabout$SITTING = new AnimationState();
    @Unique
    public final AnimationState roundabout$VAMPIRE = new AnimationState();
    @Unique
    @Override
    public AnimationState getSitting(){return roundabout$SITTING;}
    @Unique
    @Override
    public AnimationState getVampire(){return roundabout$VAMPIRE;}
    @Unique
    public final AnimationState roundabout$TORTURE_DANCE = new AnimationState();
    @Unique
    @Override
    public AnimationState getTortureDance(){
        return roundabout$TORTURE_DANCE;
    }
    @Unique
    @Override
    public AnimationState getWamuu(){
        return roundabout$WAMUU;
    }
    @Unique
    @Override
    public AnimationState getWatch(){
        return roundabout$WATCH;
    }
    @Unique
    @Override
    public AnimationState getJotaro(){
        return roundabout$JOTARO;
    }
    @Unique
    @Override
    public AnimationState getJonathan(){
        return roundabout$JONATHAN;
    }
    @Unique
    public final AnimationState roundabout$WAMUU = new AnimationState();
    @Unique
    public final AnimationState roundabout$WATCH = new AnimationState();

    @Unique
    public final AnimationState roundabout$JOTARO = new AnimationState();
    @Unique
    public final AnimationState roundabout$JONATHAN = new AnimationState();


    @Unique
    public void roundabout$setupAnimationStates() {
        if (roundabout$GetPoseEmote() == Poses.JONATHAN.id) {
            this.roundabout$JONATHAN.startIfStopped(this.tickCount);
        } else {
            this.roundabout$JONATHAN.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.JOTARO.id) {
            this.roundabout$JOTARO.startIfStopped(this.tickCount);
        } else {
            this.roundabout$JOTARO.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.WAMUU.id) {
            this.roundabout$WAMUU.startIfStopped(this.tickCount);
        } else {
            this.roundabout$WAMUU.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.TORTURE_DANCE.id) {
            this.roundabout$TORTURE_DANCE.startIfStopped(this.tickCount);
        } else {
            this.roundabout$TORTURE_DANCE.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.OH_NO.id) {
            this.roundabout$OH_NO.startIfStopped(this.tickCount);
        } else {
            this.roundabout$OH_NO.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.WRY.id) {
            this.roundabout$WRYYY.startIfStopped(this.tickCount);
        } else {
            this.roundabout$WRYYY.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.GIORNO.id) {
            this.roundabout$GIORNO.startIfStopped(this.tickCount);
        } else {
            this.roundabout$GIORNO.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.KOICHI.id) {
            this.roundabout$KOICHI.startIfStopped(this.tickCount);
        } else {
            this.roundabout$KOICHI.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.JOSEPH.id) {
            this.roundabout$JOSEPH.startIfStopped(this.tickCount);
        } else {
            this.roundabout$JOSEPH.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.WATCH.id) {
            this.roundabout$WATCH.startIfStopped(this.tickCount);
        } else {
            this.roundabout$WATCH.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.SITTING.id) {
            this.roundabout$SITTING.startIfStopped(this.tickCount);
        } else {
            this.roundabout$SITTING.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.VAMPIRE_TRANSFORMATION.id) {
            this.roundabout$VAMPIRE.startIfStopped(this.tickCount);
        } else {
            this.roundabout$VAMPIRE.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.VAMPIRE_TRANSFORMATION.id) {
            this.roundabout$VAMPIRE.startIfStopped(this.tickCount);
        } else {
            this.roundabout$VAMPIRE.stop();
        }
        if (roundabout$GetPoseEmote() == Poses.VAMPIRE_TRANSFORMATION.id) {
            this.roundabout$VAMPIRE.startIfStopped(this.tickCount);
        } else {
            this.roundabout$VAMPIRE.stop();
        }
    }

    /**Break free from stand grab*/
    @Inject(method = "wantsToStopRiding", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$wantsToStopRiding(CallbackInfoReturnable<Boolean> cir) {
        if (this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()){
            int rticks = ((StandUser)this).roundabout$getRestrainedTicks();
            if (rticks < 50){
                cir.setReturnValue(false);
            }
        }
    }
    @Inject(method = "rideTick", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$rideTick(CallbackInfo ci) {
        if (this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()){
            int rticks = ((StandUser)this).roundabout$getRestrainedTicks();
            if (this.level().isClientSide){
                if (this.isCrouching()) {
                    rticks++;
                    if (rticks >= 30) {
                        rticks = 30;
                    }
                    C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_RIDE_TICKS,rticks);
                    ((StandUser) this).roundabout$setRestrainedTicks(rticks);
                } else {
                    rticks--;
                    if (rticks <= -1) {
                        rticks = -1;
                    }
                    C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_RIDE_TICKS,rticks);
                    ((StandUser) this).roundabout$setRestrainedTicks(rticks);
                }
            } else {
                if (rticks >= 30){
                    this.stopRiding();
                    this.setShiftKeyDown(false);
                    ci.cancel();
                }
            }
        }
    }
    @Inject(method = "getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE",
                    target="Lnet/minecraft/world/item/ProjectileWeaponItem;getAllSupportedProjectiles()Ljava/util/function/Predicate;",
            shift= At.Shift.AFTER),
            cancellable = true)
    public void roundabout$getProjectile(ItemStack $$0, CallbackInfoReturnable<ItemStack> cir) {
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
    public void roundabout$getSpeed(CallbackInfoReturnable<Float> cir) {
        float basis = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
        if (!((StandUser)this).roundabout$getStandDisc().isEmpty()){
            basis = ((StandUser)this).roundabout$getStandPowers().inputSpeedModifiers(basis);
        }
        basis = ((IFatePlayer)this).rdbt$getFatePowers().inputSpeedModifiers(basis);
        basis = ((StandUser)this).roundabout$mutualGetSpeed(basis);

        cir.setReturnValue(basis);
    }

    /**if your stand guard is broken, disable shields. Also, does not run takeshieldhit code if stand guarding.*/
    @Inject(method = "blockUsingShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutTakeShieldHit(LivingEntity $$0, CallbackInfo ci) {
        if (((StandUser) this).roundabout$isGuarding()) {
            if (((StandUser) this).roundabout$getGuardBroken()){

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
        } else if (((StandUser) $$0).roundabout$getMainhandOverride() &&
                ((StandUser) $$0).roundabout$getStandPowers().interceptAttack()){
            ci.cancel();
        }
    }

    @ModifyVariable(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "TAIL"),
            ordinal = 0, argsOnly = true)
    public CompoundTag roundabout$addAdditionalSaveData(CompoundTag $$0){
        $$0.putByte("roundabout.LocacacaCurse", ((StandUser)this).roundabout$getLocacacaCurse());
        ItemStack m1 = this.roundabout$maskInventory.getItem(0);
        ItemStack m2 = this.roundabout$maskInventory.getItem(1);
        if (!m1.isEmpty() || $$0.contains("roundabout.Mask", 10)) {
            CompoundTag compoundtag = new CompoundTag();
            $$0.put("roundabout.Mask",m1.save(compoundtag));
        }
        if (!m2.isEmpty() || $$0.contains("roundabout.VoiceMask", 10)) {
            CompoundTag compoundtag = new CompoundTag();
            $$0.put("roundabout.VoiceMask",m2.save(compoundtag));
        }
        CompoundTag compoundtag = $$0.getCompound("roundabout");
        compoundtag.putInt("anchorPlace",roundabout$anchorPlace);
        compoundtag.putInt("anchorPlaceAttack",roundabout$anchorPlaceAttack);
        compoundtag.putFloat("distanceOut",roundabout$distanceOut);
        compoundtag.putFloat("idleRotation",roundabout$idleRotation);
        compoundtag.putFloat("idleYOffset",roundabout$idleYOffset);
        compoundtag.putFloat("sizePercent",roundabout$sizePercent);
        compoundtag.putByte("teamColor",roundabout$getTeamColor());
        compoundtag.putByte("watchStyle",roundabout$getWatchStyle());
        compoundtag.putByte("fate",roundabout$getFate());
        compoundtag.putFloat("hairColorX",rdbt$getHairColorX());
        compoundtag.putFloat("hairColorY",rdbt$getHairColorY());
        compoundtag.putFloat("hairColorZ",rdbt$getHairColorZ());
        $$0.put("roundabout",compoundtag);

        return $$0;
    }
    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "TAIL"))
    public void roundabout$readAdditionalSaveData(CompoundTag $$0, CallbackInfo ci){

        ((StandUser)this).roundabout$setLocacacaCurse($$0.getByte("roundabout.LocacacaCurse"));
        if ($$0.contains("roundabout.Mask", 10)) {
            CompoundTag compoundtag = $$0.getCompound("roundabout.Mask");
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof MaskItem SD){
                this.roundabout$maskInventory.setItem(0,itemstack);
            }
        }if ($$0.contains("roundabout.VoiceMask", 10)) {
            CompoundTag compoundtag = $$0.getCompound("roundabout.VoiceMask");
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof MaskItem SD){
                this.roundabout$maskInventory.setItem(1,itemstack);
            }
        }
        CompoundTag compoundtag2 = $$0.getCompound("roundabout");
        if (compoundtag2.contains("anchorPlace")) {
            roundabout$anchorPlace = compoundtag2.getInt("anchorPlace");
        }
        if (compoundtag2.contains("anchorPlaceAttack")) {
            roundabout$anchorPlaceAttack = compoundtag2.getInt("anchorPlaceAttack");
        }
        if (compoundtag2.contains("distanceOut")) {
            roundabout$distanceOut = compoundtag2.getFloat("distanceOut");
        }
        if (compoundtag2.contains("idleRotation")) {
            roundabout$idleRotation = compoundtag2.getFloat("idleRotation");
        }
        if (compoundtag2.contains("idleYOffset")) {
            roundabout$idleYOffset = compoundtag2.getFloat("idleYOffset");
        }
        if (compoundtag2.contains("sizePercent")) {
            roundabout$sizePercent = compoundtag2.getFloat("sizePercent");
        }
        if (compoundtag2.contains("teamColor")) {
            roundabout$setTeamColor(compoundtag2.getByte("teamColor"));
        }
        if (compoundtag2.contains("watchStyle")) {
            roundabout$setWatchStyle(compoundtag2.getByte("watchStyle"));
        }
        if (compoundtag2.contains("fate")) {
            roundabout$setFate(compoundtag2.getByte("fate"));
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

        //roundabout$maskInventory.addItem()
    }

    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "hurtCurrentlyUsedShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutDamageShield(float amount, CallbackInfo ci) {
        StandUser user = ((StandUser) this);
        if (user.roundabout$isGuarding()) {
            if (user.roundabout$getLogSource() != null && !user.roundabout$getLogSource().is(DamageTypeTags.BYPASSES_COOLDOWN) && user.roundabout$getGuardCooldown() > 0) {
                return;
            }

            user.roundabout$damageGuard(amount);
            ci.cancel();
        }
    }
    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Jump(CallbackInfo ci) {
        if (((StandUser) this).roundabout$isClashing() || ((StandUser) this).roundabout$getStandPowers().cancelJump()
                || ((IFatePlayer)this).rdbt$getFatePowers().cancelJump()
        || FateTypes.isTransforming(this) ||
        FateTypes.takesSunlightDamage(this) && FateTypes.isInSunlight(this)) {
            ci.cancel();
        }
    }

    /**stand mining intercepts tools for drop so that it is hand level*/
    @Inject(method = "hasCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$hasCorrectTool(BlockState $$0, CallbackInfoReturnable<Boolean> cir) {
        if (((StandUser) this).roundabout$getActive() && ((StandUser) this).roundabout$getStandPowers().canUseMiningStand()
        ) {
            int MiningTier = ((StandUser) this).roundabout$getStandPowers().getMiningLevel();
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
        }
    }

    /**stand mining intercepts mining speed as well*/
    @Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getDestroySpeed2(BlockState $$0, CallbackInfoReturnable<Float> cir) {
        StandPowers powers = ((StandUser) this).roundabout$getStandPowers();
        if (((StandUser) this).roundabout$getActive() && ((StandUser) this).roundabout$getStandPowers().canUseMiningStand()) {

            cir.setReturnValue(rdbt$mutualMiningSpeedFunction($$0,powers));
        }
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

        if (this.isCrouching() && $$0.getBlock() instanceof DropExperienceBlock && ClientNetworking.getAppropriateConfig().generalStandSettings.crouchingStopsStandsFromMiningOres) {
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
    protected void roundabout$GetHurtSound(DamageSource $$0, CallbackInfoReturnable<SoundEvent> ci) {
        if (((StandUser) this).roundabout$isDazed() || $$0.is(ModDamageTypes.STAND_RUSH)) {
            ci.setReturnValue(null);
        } else {
            ShapeShifts shift = ShapeShifts.getShiftFromByte(roundabout$getShapeShift());
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
    protected void roundabout$playStepSound(BlockPos $$0, BlockState $$1, CallbackInfo ci) {
        ShapeShifts shift = ShapeShifts.getShiftFromByte(roundabout$getShapeShift());
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
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$Tick(CallbackInfo ci) {

        if (this.level().isClientSide()) {
            roundabout$setupAnimationStates();
            if (!rdbt$getCooldownQuery()){
                if (!rdbt$attemptedQuery){
                    rdbt$attemptedQuery = true;
                    C2SPacketUtil.handShakeCooldownPacket();
                }

            }
        } else {
            roundabout$qmessageTick();
        }
        if (!(this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted())) {
            ((StandUser) this).roundabout$setRestrainedTicks(-1);
        }

        if (roundabout$getCameraHits() > -1){
            roundabout$setCameraHits(roundabout$getCameraHits()-1);
        }

        boolean notSkybound = (this.onGround() || this.isSwimming()|| this.onClimbable() || this.isPassenger()
                || this.hasEffect(MobEffects.LEVITATION));

        if (!((TimeStop) ((Player)(Object) this).level()).getTimeStoppingEntities().isEmpty()) {
            if (((TimeStop) ((Player) (Object) this).level()).CanTimeStopEntity(((Player) (Object) this))) {
                ci.cancel();
                return;
            } else if ((((TimeStop) ((Player) (Object) this).level()).isTimeStoppingEntity(((Player) (Object) this)))) {
                ((StandUser) this).roundabout$setIdleTime(-1);
                roundabout$airTime = 0;
            } else {
                if (notSkybound || this.isInWater()){
                    roundabout$airTime = 0;
                } else {
                    roundabout$airTime += 1;
                }
            }
        }

        if (!this.isDeadOrDying()) {
            if (roundabout$getVoiceData() != null){
                roundabout$getVoiceData().playOnTick();
            }
        }

    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void roundabout$Tick2(CallbackInfo ci) {
        if (this.isSleeping()){
            if (this.hasEffect(ModEffects.CAPTURING_LOVE)){
                StandUser user = ((StandUser)this);
                user.roundabout$setSafeToRemoveLove(true);
                this.removeEffect(ModEffects.CAPTURING_LOVE);
            }
        }

        if (((StandUser)this).roundabout$getAttackTimeDuring() > -1 || this.isUsingItem()) {
            ((StandUser) this).roundabout$setIdleTime(-1);
        } else if (!new Vec3(this.getX(), this.getY(), this.getZ()).equals(new Vec3(this.xOld, this.yOld, this.zOld))) {
            ((StandUser) this).roundabout$setIdleTime(-1);
        } else {
            ((StandUser) this).roundabout$setIdleTime(((StandUser) this).roundabout$getIdleTime() + 1);
        }
        ((StandUser) this).roundabout$getStandPowers().tickPowerEnd();
    }

    @Unique
    public VoiceData roundabout$voiceData;
    @Override
    @Unique
    public VoiceData roundabout$getVoiceData() {
        return roundabout$voiceData;
    }
    @Override
    @Unique
    public void roundabout$setVoiceData(VoiceData vd) {
        roundabout$voiceData = vd;
    }
    @Override
    @Unique
    public void roundabout$addKnife() {
        byte knifeCount = this.entityData.get(ROUNDABOUT$DATA_KNIFE_COUNT_ID);

        knifeCount++;
        if (knifeCount <= ClientNetworking.getAppropriateConfig().itemSettings.maxKnivesInOneHit){
            ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$DATA_KNIFE_COUNT_ID, knifeCount);
        }
    }
    @Override
    @Unique
    public void roundabout$setKnife(byte knives) {
        ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$DATA_KNIFE_COUNT_ID, knives);
    }

    @Override
    @Unique
    public PlayerMaskSlots roundabout$getMaskInventory() {
        return roundabout$maskInventory;
    }
    @Override
    @Unique
    public void roundabout$setMaskInventory(PlayerMaskSlots pm) {
        this.roundabout$maskInventory.replaceWith(pm);;
    }
    @Override
    @Unique
    public final ItemStack roundabout$getMaskSlot() {
        return this.entityData.get(ROUNDABOUT$MASK_SLOT);
    }
    @Override
    @Unique
    public final ItemStack roundabout$getMaskVoiceSlot() {
        return this.entityData.get(ROUNDABOUT$MASK_VOICE_SLOT);
    }

    @Override
    @Unique
    public void roundabout$setMaskSlot(ItemStack stack) {
        ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$MASK_SLOT, stack);
    }

    @Override
    @Unique
    public final int roundabout$getKnifeCount() {
        return this.entityData.get(ROUNDABOUT$DATA_KNIFE_COUNT_ID);
    }
    @Override
    @Unique
    public void roundabout$setMaskVoiceSlot(ItemStack stack) {
        if (!this.level().isClientSide()) {
            if (stack != null && !stack.isEmpty() && stack.getItem() instanceof MaskItem mi) {
                ItemStack stack2 = roundabout$getMaskVoiceSlot();
                if (!(stack2.equals(stack))) {
                    roundabout$setVoiceData(mi.visageData.voiceData(((Player) (Object) this)));
                }
            } else {
                roundabout$setVoiceData(null);
            }
        }
        ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$MASK_VOICE_SLOT, stack);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void initDataTrackerRoundabout(CallbackInfo ci) {
        if (!((LivingEntity)(Object)this).getEntityData().hasItem(ROUNDABOUT$POS)) {
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$POS, PlayerPosIndex.NONE);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$POS_2, PlayerPosIndex.NONE);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$POSE_EMOTE, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$DODGE_TIME, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$CAMERA_HITS, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$DATA_KNIFE_COUNT_ID, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$MASK_SLOT, ItemStack.EMPTY);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$MASK_VOICE_SLOT, ItemStack.EMPTY);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_LEVEL, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$SHAPE_SHIFT, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$SHAPE_SHIFT_EXTRA, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_EXP, 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IS_CONTROLLING, 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$TEAM_COLOR, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$WATCH_STYLE, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$FATE, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IS_BLINDED, false);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$HAIR_COLOR, new Rotations(rdbt$hairColorX,rdbt$hairColorY,rdbt$hairColorZ));
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
    private void roundabout$interactOn(Entity $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir)
    {
        if (((StandUser)(Player)(Object)this).roundabout$isParallelRunning())
        {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }

        if (!$$0.level().isClientSide()) {
            if (!this.isSpectator()) {
                ItemStack $$2 = this.getItemInHand($$1);
                if ($$0 instanceof LivingEntity LE && ((StandUser) LE).roundabout$isBubbleEncased()) {
                    if (!$$2.isEmpty() && ($$2.getItem() instanceof ShearsItem || $$2.getItem() instanceof ScissorItem)) {
                        ((StandUser) LE).roundabout$setBubbleEncased((byte) 0);
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
    private void roundabout$killedEntity(ServerLevel $$0, LivingEntity $$1, CallbackInfoReturnable<Boolean> cir)
    {
        if (roundabout$getVoiceData() != null){
            roundabout$getVoiceData().playIfKilled($$1);
        }
    }
    @Inject(method = "attack", at=@At("HEAD"), cancellable = true)
    private void roundabout$attack(Entity $$0, CallbackInfo ci)
    {
        if (roundabout$getVoiceData() != null){
            if ($$0.isAttackable()) {
                if (!$$0.skipAttackInteraction(this)) {
                    roundabout$getVoiceData().playIfAttacking($$0);
                }
            }
        }
    }
    @Shadow
    public HumanoidArm getMainArm() {
        return null;
    }

    @Shadow public abstract void displayClientMessage(Component component, boolean bl);

    @Inject(method = "canHarmPlayer", at=@At("HEAD"), cancellable = true)
    private void roundabout$canHarmPlayer(Player player, CallbackInfoReturnable<Boolean> cir)
    {
        if (player.level().isClientSide)
            return;

        if (((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C powers)
        {
            if (powers.meltDodgeTicks != -1 || ((StandUser)player).roundabout$isParallelRunning())
            {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "blockActionRestricted", at = @At("HEAD"), cancellable = true)
    private void roundabout$disableBlockBreaking(Level level, BlockPos pos, GameType gameType, CallbackInfoReturnable<Boolean> cir)
    {
        if (((StandUser)(Player)(Object)this).roundabout$isParallelRunning())
        {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }


    @Inject(method = "killedEntity", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$hasLineOfSight(ServerLevel $$0, LivingEntity $$1, CallbackInfoReturnable<Boolean> cir) {
        if (((StandUser)this).roundabout$getStandPowers().onKilledEntity($$0,$$1)){
            cir.setReturnValue(false);
        }
    }

 /*   @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$drop(ItemStack $$0, boolean $$1, boolean $$2, CallbackInfoReturnable<ItemEntity> cir) {
        Player This = (Player)(Object)this;
        StandUser SU = ((StandUser)This);
        if (SU.roundabout$isPossessed()) {
            This.addItem($$0);
            cir.cancel();
            return;
        }

    } */
}
