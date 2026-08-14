package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.disc.DiscInventoryLimit;
import net.hydra.jojomod.event.powers.disc.DreamingMemoryController;
import net.hydra.jojomod.event.powers.disc.MemoryPersonality;
import net.hydra.jojomod.event.powers.disc.MemoryAiController;
import net.hydra.jojomod.event.powers.disc.CommandDiscController;
import net.hydra.jojomod.event.powers.disc.MusicDiscController;
import net.hydra.jojomod.event.powers.disc.WhitesnakeDiscUtil;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDiscData extends Entity implements DiscBearer {
    @Unique private static final EntityDataAccessor<Boolean> ROUNDABOUT$HAS_SIGHT =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<Boolean> ROUNDABOUT$HAS_MEMORY =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<Boolean> ROUNDABOUT$HAS_HEARING =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<Boolean> ROUNDABOUT$HAS_DREAMING_MEMORY =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$SIGHT_OWNER_ID =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$SIGHT_OWNER_NAME =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$MEMORY_OWNER_ID =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$MEMORY_OWNER_NAME =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$MEMORY_TAME_OWNER_ID =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$MEMORY_TAME_OWNER_NAME =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$HEARING_OWNER_ID =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> ROUNDABOUT$HEARING_OWNER_NAME =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<Byte> ROUNDABOUT$MEMORY_PERSONALITY =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);
    @Unique private static final EntityDataAccessor<ItemStack> ROUNDABOUT$MUSIC_DISC =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.ITEM_STACK);
    @Unique private static final EntityDataAccessor<Integer> ROUNDABOUT$SIGHT_SEAL_TICKS =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> ROUNDABOUT$SIGHT_SEAL_MAX_TICKS =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> ROUNDABOUT$MEMORY_SEAL_TICKS =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> ROUNDABOUT$MEMORY_SEAL_MAX_TICKS =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> ROUNDABOUT$HEARING_SEAL_TICKS =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique private static final EntityDataAccessor<Integer> ROUNDABOUT$HEARING_SEAL_MAX_TICKS =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique private boolean roundabout$foreignDiscsDropped;
    @Unique private boolean roundabout$memoryDevelopmentLimited;
    @Unique private int roundabout$previousLevelDecreaseTicks;
    @Unique private int roundabout$memoryDevelopmentLimitedTicks;
    @Unique private CompoundTag roundabout$memoryReading = new CompoundTag();
    @Unique private ItemStack roundabout$temporaryMemoryDisc = ItemStack.EMPTY;
    @Unique private CompoundTag roundabout$memoryBeforeDreaming = new CompoundTag();

    protected LivingEntityDiscData(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void roundabout$defineDiscData(CallbackInfo ci) {
        if (!this.entityData.hasItem(ROUNDABOUT$HAS_SIGHT)) {
            this.entityData.define(ROUNDABOUT$HAS_SIGHT,
                    WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this));
            this.entityData.define(ROUNDABOUT$HAS_MEMORY, true);
            this.entityData.define(ROUNDABOUT$HAS_HEARING, true);
            this.entityData.define(ROUNDABOUT$HAS_DREAMING_MEMORY, false);
            this.entityData.define(ROUNDABOUT$SIGHT_OWNER_ID, "");
            this.entityData.define(ROUNDABOUT$SIGHT_OWNER_NAME, "");
            this.entityData.define(ROUNDABOUT$MEMORY_OWNER_ID, "");
            this.entityData.define(ROUNDABOUT$MEMORY_OWNER_NAME, "");
            this.entityData.define(ROUNDABOUT$MEMORY_TAME_OWNER_ID, "");
            this.entityData.define(ROUNDABOUT$MEMORY_TAME_OWNER_NAME, "");
            this.entityData.define(ROUNDABOUT$HEARING_OWNER_ID, "");
            this.entityData.define(ROUNDABOUT$HEARING_OWNER_NAME, "");
            this.entityData.define(ROUNDABOUT$MEMORY_PERSONALITY,
                    MemoryPersonality.classify((LivingEntity) (Object) this));
            this.entityData.define(ROUNDABOUT$MUSIC_DISC, ItemStack.EMPTY);
            this.entityData.define(ROUNDABOUT$SIGHT_SEAL_TICKS, 0);
            this.entityData.define(ROUNDABOUT$SIGHT_SEAL_MAX_TICKS, 0);
            this.entityData.define(ROUNDABOUT$MEMORY_SEAL_TICKS, 0);
            this.entityData.define(ROUNDABOUT$MEMORY_SEAL_MAX_TICKS, 0);
            this.entityData.define(ROUNDABOUT$HEARING_SEAL_TICKS, 0);
            this.entityData.define(ROUNDABOUT$HEARING_SEAL_MAX_TICKS, 0);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void roundabout$saveDiscData(CompoundTag tag, CallbackInfo ci) {
        CompoundTag discs = new CompoundTag();
        discs.putBoolean("HasSight", roundabout$ownsSightDisc());
        discs.putBoolean("HasMemory", roundabout$ownsMemoryDisc());
        discs.putBoolean("HasHearing", roundabout$ownsHearingDisc());
        discs.putInt("SightSealTicks", roundabout$getDiscSealTicks(WhitesnakeDiscUtil.SIGHT));
        discs.putInt("SightSealMaxTicks", roundabout$getDiscSealMaxTicks(WhitesnakeDiscUtil.SIGHT));
        discs.putInt("MemorySealTicks", roundabout$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY));
        discs.putInt("MemorySealMaxTicks", roundabout$getDiscSealMaxTicks(WhitesnakeDiscUtil.MEMORY));
        discs.putInt("HearingSealTicks", roundabout$getDiscSealTicks(WhitesnakeDiscUtil.HEARING));
        discs.putInt("HearingSealMaxTicks", roundabout$getDiscSealMaxTicks(WhitesnakeDiscUtil.HEARING));
        discs.putString("SightOwnerId", roundabout$getSightDiscOwnerId());
        discs.putString("SightOwnerName", roundabout$getSightDiscOwnerName());
        discs.putString("MemoryOwnerId", roundabout$getMemoryDiscOwnerId());
        discs.putString("MemoryOwnerName", roundabout$getMemoryDiscOwnerName());
        discs.putString("MemoryTameOwnerId", roundabout$getMemoryTameOwnerId());
        discs.putString("MemoryTameOwnerName", roundabout$getMemoryTameOwnerName());
        discs.putString("HearingOwnerId", roundabout$getHearingDiscOwnerId());
        discs.putString("HearingOwnerName", roundabout$getHearingDiscOwnerName());
        discs.putByte("MemoryPersonality", roundabout$getMemoryPersonality());
        discs.putBoolean("MemoryDevelopmentLimited", roundabout$memoryDevelopmentLimited);
        discs.putInt("PreviousLevelDecreaseTicks", roundabout$previousLevelDecreaseTicks);
        discs.putInt("MemoryDevelopmentLimitedTicks", roundabout$memoryDevelopmentLimitedTicks);
        if (!roundabout$memoryReading.isEmpty()) {
            discs.put("MemoryReading", roundabout$memoryReading.copy());
        }
        if (!roundabout$getMusicDisc().isEmpty()) {
            discs.put("MusicDisc", roundabout$getMusicDisc().save(new CompoundTag()));
        }
        if (roundabout$hasTemporaryMemoryDisc()) {
            discs.put("DreamingMemoryDisc", roundabout$temporaryMemoryDisc.save(new CompoundTag()));
            discs.put("MemoryBeforeDreaming", roundabout$memoryBeforeDreaming.copy());
        }
        tag.put("roundabout.WhitesnakeDiscs", discs);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void roundabout$loadDiscData(CompoundTag tag, CallbackInfo ci) {
        if (!tag.contains("roundabout.WhitesnakeDiscs", 10)) {
            return;
        }
        CompoundTag discs = tag.getCompound("roundabout.WhitesnakeDiscs");
        roundabout$setSightDiscOwnerId(discs.getString("SightOwnerId"));
        roundabout$setSightDiscOwnerName(discs.getString("SightOwnerName"));
        roundabout$setMemoryDiscOwnerId(discs.getString("MemoryOwnerId"));
        roundabout$setMemoryDiscOwnerName(discs.getString("MemoryOwnerName"));
        roundabout$setMemoryTameOwnerId(discs.getString("MemoryTameOwnerId"));
        roundabout$setMemoryTameOwnerName(discs.getString("MemoryTameOwnerName"));
        roundabout$setHearingDiscOwnerId(discs.getString("HearingOwnerId"));
        roundabout$setHearingDiscOwnerName(discs.getString("HearingOwnerName"));
        if (discs.contains("MemoryPersonality")) {
            roundabout$setMemoryPersonality(discs.getByte("MemoryPersonality"));
        }
        roundabout$memoryDevelopmentLimited = discs.getBoolean("MemoryDevelopmentLimited");
        roundabout$previousLevelDecreaseTicks = discs.getInt("PreviousLevelDecreaseTicks");
        roundabout$memoryDevelopmentLimitedTicks = discs.getInt("MemoryDevelopmentLimitedTicks");
        roundabout$memoryReading = discs.contains("MemoryReading", 10)
                ? discs.getCompound("MemoryReading").copy() : new CompoundTag();
        roundabout$setMusicDisc(discs.contains("MusicDisc", 10)
                ? ItemStack.of(discs.getCompound("MusicDisc")) : ItemStack.EMPTY);
        roundabout$setTemporaryMemoryDisc(discs.contains("DreamingMemoryDisc", 10)
                ? ItemStack.of(discs.getCompound("DreamingMemoryDisc")) : ItemStack.EMPTY);
        roundabout$setMemoryBeforeDreaming(discs.contains("MemoryBeforeDreaming", 10)
                ? discs.getCompound("MemoryBeforeDreaming") : new CompoundTag());
        entityData.set(ROUNDABOUT$HAS_SIGHT,
                WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this)
                        && (!discs.contains("HasSight") || discs.getBoolean("HasSight")));
        entityData.set(ROUNDABOUT$HAS_MEMORY,
                !discs.contains("HasMemory") || discs.getBoolean("HasMemory"));
        entityData.set(ROUNDABOUT$HAS_HEARING,
                !discs.contains("HasHearing") || discs.getBoolean("HasHearing"));
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.discSealing) {
            roundabout$setDiscSeal(WhitesnakeDiscUtil.SIGHT, discs.getInt("SightSealTicks"),
                    discs.getInt("SightSealMaxTicks"));
            roundabout$setDiscSeal(WhitesnakeDiscUtil.MEMORY, discs.getInt("MemorySealTicks"),
                    discs.getInt("MemorySealMaxTicks"));
            roundabout$setDiscSeal(WhitesnakeDiscUtil.HEARING, discs.getInt("HearingSealTicks"),
                    discs.getInt("HearingSealMaxTicks"));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundabout$tickDiscEffects(CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        DreamingMemoryController.tick(living);
        if (WhitesnakeDiscUtil.canCarrySightDisc(living) && !roundabout$hasSightDisc() && !level().isClientSide()
                && (!living.hasEffect(MobEffects.BLINDNESS)
                || living.getEffect(MobEffects.BLINDNESS).getDuration() < 80)) {
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false, false));
        }
        boolean lobotomized = DiscItemData.isLobotomized(living)
                || living instanceof Mob mob && DreamingMemoryController.isDreamingWithoutMemory(mob);
        if (lobotomized) {
            living.xxa = 0;
            living.zza = 0;
            living.setXRot(90.0F);
            living.setYHeadRot(living.getYRot());
            if (living instanceof Mob mob) {
                if (mob.isNoAi()) mob.setNoAi(false);
                mob.setTarget(null);
                mob.getNavigation().stop();
                roundabout$groundLobotomizedFlyer(mob);
            }
        } else if (DiscItemData.isBlankMemoryMob(living)) {
            Mob mob = (Mob) living;
            if (mob.isNoAi()) mob.setNoAi(false);
            mob.setTarget(null);
        } else {
            MemoryAiController.tick(living);
        }
        CommandDiscController.tickExplosion(living);
        if (roundabout$hasMemoryDisc()) CommandDiscController.tick(living);
        MusicDiscController.tick(living);
        if (living instanceof ServerPlayer player) {
            DiscInventoryLimit.enforce(player);
            roundabout$updateMemoryDevelopment(player);
        }
        if (!level().isClientSide()) roundabout$tickDiscSeals(living);
    }

    @Unique
    private static void roundabout$groundLobotomizedFlyer(Mob mob) {
        if (!(mob instanceof FlyingMob) && !(mob instanceof Allay)) return;
        mob.setNoGravity(false);
        if (mob.onGround() || mob.isInWater() || mob.isInLava()) return;
        Vec3 movement = mob.getDeltaMovement();
        double fallSpeed = Math.max(-3.92D, Math.min(-0.08D, movement.y - 0.08D));
        mob.setDeltaMovement(0.0D, fallSpeed, 0.0D);
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void roundabout$ejectMusicDiscWhenDamaged(DamageSource source, float amount,
                                                       CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) MusicDiscController.ejectOnDamage((LivingEntity) (Object) this);
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void roundabout$dropForeignDiscs(DamageSource source, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (roundabout$foreignDiscsDropped || living.level().isClientSide()) return;
        roundabout$foreignDiscsDropped = true;
        String entityId = living.getUUID().toString();

        if (roundabout$ownsSightDisc() && roundabout$isForeign(roundabout$getSightDiscOwnerId(), entityId)) {
            ItemStack stack = new ItemStack(ModItems.SIGHT_DISC);
            DiscItemData.setOwner(stack, roundabout$getSightDiscOwnerId(), roundabout$getSightDiscOwnerName());
            roundabout$setHasSightDisc(false);
            living.spawnAtLocation(stack, 0.35F);
        }
        if (roundabout$ownsMemoryDisc() && roundabout$isForeign(roundabout$getMemoryDiscOwnerId(), entityId)) {
            ItemStack stack = new ItemStack(ModItems.MEMORY_DISC);
            DiscItemData.setOwner(stack, roundabout$getMemoryDiscOwnerId(), roundabout$getMemoryDiscOwnerName());
            DiscItemData.setPersonality(stack, roundabout$getMemoryPersonality());
            DiscItemData.setTameOwner(stack, roundabout$getMemoryTameOwnerId(),
                    roundabout$getMemoryTameOwnerName());
            DiscItemData.setMemoryReading(stack, roundabout$getMemoryReading());
            roundabout$setHasMemoryDisc(false);
            roundabout$setMemoryReading(new CompoundTag());
            living.spawnAtLocation(stack, 0.35F);
        }
        if (roundabout$ownsHearingDisc() && roundabout$isForeign(roundabout$getHearingDiscOwnerId(), entityId)) {
            ItemStack stack = new ItemStack(ModItems.HEARING_DISC);
            DiscItemData.setOwner(stack, roundabout$getHearingDiscOwnerId(), roundabout$getHearingDiscOwnerName());
            roundabout$setHasHearingDisc(false);
            living.spawnAtLocation(stack, 0.35F);
        }

        StandUser standUser = (StandUser) living;
        ItemStack standDisc = standUser.roundabout$getStandDisc();
        if (!(living instanceof KingCrimsonCloneEntity) && !standDisc.isEmpty()
                && roundabout$isForeign(DiscItemData.getOwnerId(standDisc), entityId)) {
            ItemStack stack = MainUtil.saveToDiscData(living, standDisc.copy());
            standUser.roundabout$getStandPowers().onStandSwitch();
            standUser.roundabout$setStand(null);
            standUser.roundabout$setActive(false);
            standUser.roundabout$setStandDisc(ItemStack.EMPTY);
            standUser.roundabout$setStandPowers(null);
            living.spawnAtLocation(stack, 0.35F);
        }
    }

    @Unique
    private static boolean roundabout$isForeign(String ownerId, String entityId) {
        return ownerId != null && !ownerId.isEmpty() && !ownerId.equals(entityId);
    }

    @Override
    public boolean roundabout$hasSightDisc() {
        return roundabout$ownsSightDisc() && roundabout$getDiscSealTicks(WhitesnakeDiscUtil.SIGHT) <= 0;
    }

    @Override
    public boolean roundabout$ownsSightDisc() {
        return entityData.get(ROUNDABOUT$HAS_SIGHT);
    }

    @Override
    public void roundabout$setHasSightDisc(boolean value) {
        entityData.set(ROUNDABOUT$HAS_SIGHT, value);
        roundabout$setDiscSeal(WhitesnakeDiscUtil.SIGHT, 0, 0);
    }
    @Override
    public boolean roundabout$hasMemoryDisc() {
        return roundabout$ownsMemoryDisc() && roundabout$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY) <= 0;
    }

    @Override
    public boolean roundabout$ownsMemoryDisc() {
        return entityData.get(ROUNDABOUT$HAS_MEMORY);
    }

    @Override
    public void roundabout$setHasMemoryDisc(boolean value) {
        boolean changed = entityData.get(ROUNDABOUT$HAS_MEMORY) != value;
        entityData.set(ROUNDABOUT$HAS_MEMORY, value);
        roundabout$setDiscSeal(WhitesnakeDiscUtil.MEMORY, 0, 0);
        if (changed && (Object) this instanceof ServerPlayer player) MemoryAiController.clearPlayerState(player);
    }
    @Override
    public boolean roundabout$hasHearingDisc() {
        return roundabout$ownsHearingDisc() && roundabout$getDiscSealTicks(WhitesnakeDiscUtil.HEARING) <= 0;
    }

    @Override
    public boolean roundabout$ownsHearingDisc() {
        return entityData.get(ROUNDABOUT$HAS_HEARING);
    }

    @Override
    public void roundabout$setHasHearingDisc(boolean value) {
        entityData.set(ROUNDABOUT$HAS_HEARING, value);
        roundabout$setDiscSeal(WhitesnakeDiscUtil.HEARING, 0, 0);
    }
    @Override
    public int roundabout$getDiscSealTicks(byte type) {
        return entityData.get(switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> ROUNDABOUT$SIGHT_SEAL_TICKS;
            case WhitesnakeDiscUtil.MEMORY -> ROUNDABOUT$MEMORY_SEAL_TICKS;
            default -> ROUNDABOUT$HEARING_SEAL_TICKS;
        });
    }
    @Override
    public int roundabout$getDiscSealMaxTicks(byte type) {
        return entityData.get(switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> ROUNDABOUT$SIGHT_SEAL_MAX_TICKS;
            case WhitesnakeDiscUtil.MEMORY -> ROUNDABOUT$MEMORY_SEAL_MAX_TICKS;
            default -> ROUNDABOUT$HEARING_SEAL_MAX_TICKS;
        });
    }
    @Override
    public void roundabout$setDiscSeal(byte type, int ticks, int maxTicks) {
        int remaining = Math.max(0, ticks);
        int maximum = Math.max(remaining, maxTicks);
        switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> {
                entityData.set(ROUNDABOUT$SIGHT_SEAL_TICKS, remaining);
                entityData.set(ROUNDABOUT$SIGHT_SEAL_MAX_TICKS, maximum);
            }
            case WhitesnakeDiscUtil.MEMORY -> {
                entityData.set(ROUNDABOUT$MEMORY_SEAL_TICKS, remaining);
                entityData.set(ROUNDABOUT$MEMORY_SEAL_MAX_TICKS, maximum);
            }
            case WhitesnakeDiscUtil.HEARING -> {
                entityData.set(ROUNDABOUT$HEARING_SEAL_TICKS, remaining);
                entityData.set(ROUNDABOUT$HEARING_SEAL_MAX_TICKS, maximum);
            }
        }
    }
    @Override
    public String roundabout$getSightDiscOwnerId() {
        return entityData.get(ROUNDABOUT$SIGHT_OWNER_ID);
    }

    @Override
    public void roundabout$setSightDiscOwnerId(String value) {
        entityData.set(ROUNDABOUT$SIGHT_OWNER_ID, value == null ? "" : value);
    }

    @Override
    public String roundabout$getSightDiscOwnerName() {
        return entityData.get(ROUNDABOUT$SIGHT_OWNER_NAME);
    }

    @Override
    public void roundabout$setSightDiscOwnerName(String value) {
        entityData.set(ROUNDABOUT$SIGHT_OWNER_NAME, value == null ? "" : value);
    }

    @Override
    public String roundabout$getMemoryDiscOwnerId() {
        return entityData.get(ROUNDABOUT$MEMORY_OWNER_ID);
    }

    @Override
    public void roundabout$setMemoryDiscOwnerId(String value) {
        entityData.set(ROUNDABOUT$MEMORY_OWNER_ID, value == null ? "" : value);
    }

    @Override
    public String roundabout$getMemoryDiscOwnerName() {
        return entityData.get(ROUNDABOUT$MEMORY_OWNER_NAME);
    }

    @Override
    public void roundabout$setMemoryDiscOwnerName(String value) {
        entityData.set(ROUNDABOUT$MEMORY_OWNER_NAME, value == null ? "" : value);
    }

    @Override
    public String roundabout$getMemoryTameOwnerId() {
        return entityData.get(ROUNDABOUT$MEMORY_TAME_OWNER_ID);
    }

    @Override
    public void roundabout$setMemoryTameOwnerId(String value) {
        entityData.set(ROUNDABOUT$MEMORY_TAME_OWNER_ID, value == null ? "" : value);
    }

    @Override
    public String roundabout$getMemoryTameOwnerName() {
        return entityData.get(ROUNDABOUT$MEMORY_TAME_OWNER_NAME);
    }

    @Override
    public void roundabout$setMemoryTameOwnerName(String value) {
        entityData.set(ROUNDABOUT$MEMORY_TAME_OWNER_NAME, value == null ? "" : value);
    }

    @Override
    public String roundabout$getHearingDiscOwnerId() {
        return entityData.get(ROUNDABOUT$HEARING_OWNER_ID);
    }

    @Override
    public void roundabout$setHearingDiscOwnerId(String value) {
        entityData.set(ROUNDABOUT$HEARING_OWNER_ID, value == null ? "" : value);
    }

    @Override
    public String roundabout$getHearingDiscOwnerName() {
        return entityData.get(ROUNDABOUT$HEARING_OWNER_NAME);
    }

    @Override
    public void roundabout$setHearingDiscOwnerName(String value) {
        entityData.set(ROUNDABOUT$HEARING_OWNER_NAME, value == null ? "" : value);
    }

    @Override
    public byte roundabout$getMemoryPersonality() {
        return entityData.get(ROUNDABOUT$MEMORY_PERSONALITY);
    }

    @Override
    public void roundabout$setMemoryPersonality(byte value) {
        boolean changed = entityData.get(ROUNDABOUT$MEMORY_PERSONALITY) != value;
        entityData.set(ROUNDABOUT$MEMORY_PERSONALITY, value);
        if (changed && (Object) this instanceof ServerPlayer player) MemoryAiController.clearPlayerState(player);
    }
    @Override
    public CompoundTag roundabout$getMemoryReading() {
        return roundabout$memoryReading.copy();
    }

    @Override
    public void roundabout$setMemoryReading(CompoundTag value) {
        roundabout$memoryReading = value == null ? new CompoundTag() : value.copy();
    }

    @Override
    public boolean roundabout$hasTemporaryMemoryDisc() {
        return entityData.get(ROUNDABOUT$HAS_DREAMING_MEMORY);
    }

    @Override
    public ItemStack roundabout$getTemporaryMemoryDisc() {
        return roundabout$temporaryMemoryDisc.copy();
    }

    @Override
    public void roundabout$setTemporaryMemoryDisc(ItemStack value) {
        roundabout$temporaryMemoryDisc = value == null ? ItemStack.EMPTY : value.copy();
        entityData.set(ROUNDABOUT$HAS_DREAMING_MEMORY, !roundabout$temporaryMemoryDisc.isEmpty());
    }

    @Override
    public CompoundTag roundabout$getMemoryBeforeDreaming() {
        return roundabout$memoryBeforeDreaming.copy();
    }

    @Override
    public void roundabout$setMemoryBeforeDreaming(CompoundTag value) {
        roundabout$memoryBeforeDreaming = value == null ? new CompoundTag() : value.copy();
    }

    @Override
    public ItemStack roundabout$getMusicDisc() {
        return entityData.get(ROUNDABOUT$MUSIC_DISC);
    }

    @Override
    public void roundabout$setMusicDisc(ItemStack value) {
        entityData.set(ROUNDABOUT$MUSIC_DISC, value == null ? ItemStack.EMPTY : value.copy());
    }

    @Unique
    private void roundabout$tickDiscSeals(LivingEntity living) {
        for (byte type = WhitesnakeDiscUtil.SIGHT; type <= WhitesnakeDiscUtil.HEARING; type++) {
            int remaining = roundabout$getDiscSealTicks(type);
            if (remaining <= 0) continue;
            int next = ClientNetworking.getAppropriateConfig().whitesnakeSettings.discSealing ? remaining - 1 : 0;
            roundabout$setDiscSeal(type, next, roundabout$getDiscSealMaxTicks(type));
            if (next > 0) continue;
            if (type == WhitesnakeDiscUtil.MEMORY) {
                if (living instanceof Mob mob && mob.isNoAi()) mob.setNoAi(false);
                if (living instanceof ServerPlayer player) MemoryAiController.clearPlayerState(player);
            }
        }
    }

    @Unique
    private void roundabout$updateMemoryDevelopment(ServerPlayer player) {
        IPlayerEntity playerData = (IPlayerEntity) player;
        boolean limited = DiscItemData.isMemoryDevelopmentLimited(player);
        if (limited) {
            if (!roundabout$memoryDevelopmentLimited) {
                roundabout$memoryDevelopmentLimited = true;
                roundabout$previousLevelDecreaseTicks = playerData.rdbt$getLevelDecreaseTicks();
                roundabout$memoryDevelopmentLimitedTicks = 0;
            }
            roundabout$memoryDevelopmentLimitedTicks++;
            int currentLevelDecreaseTicks = playerData.rdbt$getLevelDecreaseTicks();
            if (currentLevelDecreaseTicks < 1_000_000) {
                roundabout$previousLevelDecreaseTicks = Math.max(roundabout$previousLevelDecreaseTicks,
                        currentLevelDecreaseTicks + roundabout$memoryDevelopmentLimitedTicks);
                playerData.rdbt$setLevelDecreaseTicks(Integer.MAX_VALUE / 2);
            }
        } else if (roundabout$memoryDevelopmentLimited) {
            int remaining = Math.max(0,
                    roundabout$previousLevelDecreaseTicks - roundabout$memoryDevelopmentLimitedTicks);
            roundabout$memoryDevelopmentLimited = false;
            roundabout$previousLevelDecreaseTicks = 0;
            roundabout$memoryDevelopmentLimitedTicks = 0;
            playerData.rdbt$setLevelDecreaseTicks(remaining);
        }
    }
}
