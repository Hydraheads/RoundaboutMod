package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
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
import net.hydra.jojomod.util.S2CPacketUtil;
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
    @Unique private static final EntityDataAccessor<Boolean> ROUNDABOUT$HAS_MEMORY =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<Byte> ROUNDABOUT$MEMORY_PERSONALITY =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);
    @Unique private boolean roundabout$hasSightDisc = true;
    @Unique private boolean roundabout$hasHearingDisc = true;
    @Unique private String roundabout$sightDiscOwnerId = "";
    @Unique private String roundabout$sightDiscOwnerName = "";
    @Unique private String roundabout$memoryDiscOwnerId = "";
    @Unique private String roundabout$memoryDiscOwnerName = "";
    @Unique private String roundabout$memoryTameOwnerId = "";
    @Unique private String roundabout$memoryTameOwnerName = "";
    @Unique private String roundabout$hearingDiscOwnerId = "";
    @Unique private String roundabout$hearingDiscOwnerName = "";
    @Unique private int roundabout$sightSealTicks;
    @Unique private int roundabout$sightSealMaxTicks;
    @Unique private int roundabout$memorySealTicks;
    @Unique private int roundabout$memorySealMaxTicks;
    @Unique private int roundabout$hearingSealTicks;
    @Unique private int roundabout$hearingSealMaxTicks;
    @Unique private ItemStack roundabout$musicDisc = ItemStack.EMPTY;
    @Unique private boolean roundabout$bodyDiscStateDirty = true;
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
        if (!this.entityData.hasItem(ROUNDABOUT$HAS_MEMORY)) {
            this.entityData.define(ROUNDABOUT$HAS_MEMORY, true);
            this.entityData.define(ROUNDABOUT$MEMORY_PERSONALITY,
                    MemoryPersonality.classify((LivingEntity) (Object) this));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void roundabout$saveDiscData(CompoundTag tag, CallbackInfo ci) {
        if (WhitesnakeDiscUtil.isDiscBlacklisted((LivingEntity) (Object) this)) return;
        CompoundTag discs = new CompoundTag();
        discs.putBoolean("HasMemory", roundabout$ownsMemoryDisc());
        discs.putInt("MemorySealTicks", roundabout$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY));
        discs.putInt("MemorySealMaxTicks", roundabout$getDiscSealMaxTicks(WhitesnakeDiscUtil.MEMORY));
        discs.putString("MemoryOwnerId", roundabout$getMemoryDiscOwnerId());
        discs.putString("MemoryOwnerName", roundabout$getMemoryDiscOwnerName());
        discs.putString("MemoryTameOwnerId", roundabout$getMemoryTameOwnerId());
        discs.putString("MemoryTameOwnerName", roundabout$getMemoryTameOwnerName());
        if (WhitesnakeDiscUtil.isSightDiscEnabled()) {
            discs.putBoolean("HasSight", roundabout$hasSightDisc);
            discs.putInt("SightSealTicks", roundabout$sightSealTicks);
            discs.putInt("SightSealMaxTicks", roundabout$sightSealMaxTicks);
            discs.putString("SightOwnerId", roundabout$sightDiscOwnerId);
            discs.putString("SightOwnerName", roundabout$sightDiscOwnerName);
        }
        if (WhitesnakeDiscUtil.isHearingDiscEnabled()) {
            discs.putBoolean("HasHearing", roundabout$hasHearingDisc);
            discs.putInt("HearingSealTicks", roundabout$hearingSealTicks);
            discs.putInt("HearingSealMaxTicks", roundabout$hearingSealMaxTicks);
            discs.putString("HearingOwnerId", roundabout$hearingDiscOwnerId);
            discs.putString("HearingOwnerName", roundabout$hearingDiscOwnerName);
        }
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
        if (WhitesnakeDiscUtil.isDiscBlacklisted((LivingEntity) (Object) this)) return;
        if (!tag.contains("roundabout.WhitesnakeDiscs", 10)) {
            return;
        }
        CompoundTag discs = tag.getCompound("roundabout.WhitesnakeDiscs");
        roundabout$setMemoryDiscOwnerId(discs.getString("MemoryOwnerId"));
        roundabout$setMemoryDiscOwnerName(discs.getString("MemoryOwnerName"));
        roundabout$setMemoryTameOwnerId(discs.getString("MemoryTameOwnerId"));
        roundabout$setMemoryTameOwnerName(discs.getString("MemoryTameOwnerName"));
        if (WhitesnakeDiscUtil.isSightDiscEnabled()) {
            roundabout$hasSightDisc = WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this)
                    && (!discs.contains("HasSight") || discs.getBoolean("HasSight"));
            roundabout$sightDiscOwnerId = discs.getString("SightOwnerId");
            roundabout$sightDiscOwnerName = discs.getString("SightOwnerName");
            roundabout$sightSealTicks = discs.getInt("SightSealTicks");
            roundabout$sightSealMaxTicks = discs.getInt("SightSealMaxTicks");
        } else {
            roundabout$resetSightDiscState();
        }
        if (WhitesnakeDiscUtil.isHearingDiscEnabled()) {
            roundabout$hasHearingDisc = !discs.contains("HasHearing") || discs.getBoolean("HasHearing");
            roundabout$hearingDiscOwnerId = discs.getString("HearingOwnerId");
            roundabout$hearingDiscOwnerName = discs.getString("HearingOwnerName");
            roundabout$hearingSealTicks = discs.getInt("HearingSealTicks");
            roundabout$hearingSealMaxTicks = discs.getInt("HearingSealMaxTicks");
        } else {
            roundabout$resetHearingDiscState();
        }
        roundabout$bodyDiscStateDirty = true;
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
        entityData.set(ROUNDABOUT$HAS_MEMORY,
                !discs.contains("HasMemory") || discs.getBoolean("HasMemory"));
        roundabout$bodyDiscStateDirty = true;
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.discSealing) {
            roundabout$setDiscSeal(WhitesnakeDiscUtil.MEMORY, discs.getInt("MemorySealTicks"),
                    discs.getInt("MemorySealMaxTicks"));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundabout$tickDiscEffects(CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (WhitesnakeDiscUtil.isDiscBlacklisted(living)) return;
        if (!level().isClientSide() && living instanceof ServerPlayer player) {
            WhitesnakeDiscUtil.ejectMobMemoryFromPlayer(player);
        }
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
            roundabout$updateMemoryDevelopment(player);
        }
        if (!level().isClientSide()) {
            roundabout$tickDiscSeals(living);
            if (living instanceof ServerPlayer player) roundabout$syncBodyDiscState(player);
        }
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
        if (roundabout$foreignDiscsDropped || living.level().isClientSide()
                || WhitesnakeDiscUtil.isDiscBlacklisted(living)) return;
        roundabout$foreignDiscsDropped = true;
        String entityId = living.getUUID().toString();

        if (WhitesnakeDiscUtil.isSightDiscEnabled() && roundabout$ownsSightDisc()
                && roundabout$isForeign(roundabout$getSightDiscOwnerId(), entityId)) {
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
        if (WhitesnakeDiscUtil.isHearingDiscEnabled() && roundabout$ownsHearingDisc()
                && roundabout$isForeign(roundabout$getHearingDiscOwnerId(), entityId)) {
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
        if (!WhitesnakeDiscUtil.isSightDiscEnabled()) return true;
        return roundabout$ownsSightDisc() && roundabout$getDiscSealTicks(WhitesnakeDiscUtil.SIGHT) <= 0;
    }

    @Override
    public boolean roundabout$ownsSightDisc() {
        return !WhitesnakeDiscUtil.isSightDiscEnabled()
                || WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this) && roundabout$hasSightDisc;
    }

    @Override
    public void roundabout$setHasSightDisc(boolean value) {
        roundabout$hasSightDisc = !WhitesnakeDiscUtil.isSightDiscEnabled()
                || value && WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this);
        roundabout$setDiscSeal(WhitesnakeDiscUtil.SIGHT, 0, 0);
        roundabout$bodyDiscStateDirty = true;
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
        entityData.set(ROUNDABOUT$HAS_MEMORY, value);
        roundabout$setDiscSeal(WhitesnakeDiscUtil.MEMORY, 0, 0);
        roundabout$bodyDiscStateDirty = true;
    }
    @Override
    public boolean roundabout$hasHearingDisc() {
        if (!WhitesnakeDiscUtil.isHearingDiscEnabled()) return true;
        return roundabout$ownsHearingDisc() && roundabout$getDiscSealTicks(WhitesnakeDiscUtil.HEARING) <= 0;
    }

    @Override
    public boolean roundabout$ownsHearingDisc() {
        return !WhitesnakeDiscUtil.isHearingDiscEnabled() || roundabout$hasHearingDisc;
    }

    @Override
    public void roundabout$setHasHearingDisc(boolean value) {
        roundabout$hasHearingDisc = !WhitesnakeDiscUtil.isHearingDiscEnabled() || value;
        roundabout$setDiscSeal(WhitesnakeDiscUtil.HEARING, 0, 0);
        roundabout$bodyDiscStateDirty = true;
    }
    @Override
    public int roundabout$getDiscSealTicks(byte type) {
        return switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> WhitesnakeDiscUtil.isSightDiscEnabled()
                    ? roundabout$sightSealTicks : 0;
            case WhitesnakeDiscUtil.MEMORY -> roundabout$memorySealTicks;
            case WhitesnakeDiscUtil.HEARING -> WhitesnakeDiscUtil.isHearingDiscEnabled()
                    ? roundabout$hearingSealTicks : 0;
            default -> 0;
        };
    }
    @Override
    public int roundabout$getDiscSealMaxTicks(byte type) {
        return switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> WhitesnakeDiscUtil.isSightDiscEnabled()
                    ? roundabout$sightSealMaxTicks : 0;
            case WhitesnakeDiscUtil.MEMORY -> roundabout$memorySealMaxTicks;
            case WhitesnakeDiscUtil.HEARING -> WhitesnakeDiscUtil.isHearingDiscEnabled()
                    ? roundabout$hearingSealMaxTicks : 0;
            default -> 0;
        };
    }
    @Override
    public void roundabout$setDiscSeal(byte type, int ticks, int maxTicks) {
        int remaining = Math.max(0, ticks);
        int maximum = Math.max(remaining, maxTicks);
        switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> {
                if (!WhitesnakeDiscUtil.isSightDiscEnabled()) {
                    roundabout$resetSightDiscState();
                    return;
                }
                roundabout$sightSealTicks = remaining;
                roundabout$sightSealMaxTicks = maximum;
                roundabout$bodyDiscStateDirty = true;
            }
            case WhitesnakeDiscUtil.MEMORY -> {
                roundabout$memorySealTicks = remaining;
                roundabout$memorySealMaxTicks = maximum;
                roundabout$bodyDiscStateDirty = true;
            }
            case WhitesnakeDiscUtil.HEARING -> {
                if (!WhitesnakeDiscUtil.isHearingDiscEnabled()) {
                    roundabout$resetHearingDiscState();
                    return;
                }
                roundabout$hearingSealTicks = remaining;
                roundabout$hearingSealMaxTicks = maximum;
                roundabout$bodyDiscStateDirty = true;
            }
        }
    }
    @Override
    public String roundabout$getSightDiscOwnerId() {
        return WhitesnakeDiscUtil.isSightDiscEnabled() ? roundabout$sightDiscOwnerId : "";
    }

    @Override
    public void roundabout$setSightDiscOwnerId(String value) {
        roundabout$sightDiscOwnerId = WhitesnakeDiscUtil.isSightDiscEnabled() && value != null ? value : "";
    }

    @Override
    public String roundabout$getSightDiscOwnerName() {
        return WhitesnakeDiscUtil.isSightDiscEnabled() ? roundabout$sightDiscOwnerName : "";
    }

    @Override
    public void roundabout$setSightDiscOwnerName(String value) {
        roundabout$sightDiscOwnerName = WhitesnakeDiscUtil.isSightDiscEnabled() && value != null ? value : "";
    }

    @Override
    public String roundabout$getMemoryDiscOwnerId() {
        return roundabout$memoryDiscOwnerId;
    }

    @Override
    public void roundabout$setMemoryDiscOwnerId(String value) {
        roundabout$memoryDiscOwnerId = value == null ? "" : value;
    }

    @Override
    public String roundabout$getMemoryDiscOwnerName() {
        return roundabout$memoryDiscOwnerName;
    }

    @Override
    public void roundabout$setMemoryDiscOwnerName(String value) {
        roundabout$memoryDiscOwnerName = value == null ? "" : value;
    }

    @Override
    public String roundabout$getMemoryTameOwnerId() {
        return roundabout$memoryTameOwnerId;
    }

    @Override
    public void roundabout$setMemoryTameOwnerId(String value) {
        roundabout$memoryTameOwnerId = value == null ? "" : value;
    }

    @Override
    public String roundabout$getMemoryTameOwnerName() {
        return roundabout$memoryTameOwnerName;
    }

    @Override
    public void roundabout$setMemoryTameOwnerName(String value) {
        roundabout$memoryTameOwnerName = value == null ? "" : value;
    }

    @Override
    public String roundabout$getHearingDiscOwnerId() {
        return WhitesnakeDiscUtil.isHearingDiscEnabled() ? roundabout$hearingDiscOwnerId : "";
    }

    @Override
    public void roundabout$setHearingDiscOwnerId(String value) {
        roundabout$hearingDiscOwnerId = WhitesnakeDiscUtil.isHearingDiscEnabled() && value != null ? value : "";
    }

    @Override
    public String roundabout$getHearingDiscOwnerName() {
        return WhitesnakeDiscUtil.isHearingDiscEnabled() ? roundabout$hearingDiscOwnerName : "";
    }

    @Override
    public void roundabout$setHearingDiscOwnerName(String value) {
        roundabout$hearingDiscOwnerName = WhitesnakeDiscUtil.isHearingDiscEnabled() && value != null ? value : "";
    }

    @Override
    public byte roundabout$getMemoryPersonality() {
        return entityData.get(ROUNDABOUT$MEMORY_PERSONALITY);
    }

    @Override
    public void roundabout$setMemoryPersonality(byte value) {
        entityData.set(ROUNDABOUT$MEMORY_PERSONALITY, value);
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
        return !roundabout$temporaryMemoryDisc.isEmpty();
    }

    @Override
    public ItemStack roundabout$getTemporaryMemoryDisc() {
        return roundabout$temporaryMemoryDisc.copy();
    }

    @Override
    public void roundabout$setTemporaryMemoryDisc(ItemStack value) {
        roundabout$temporaryMemoryDisc = value == null ? ItemStack.EMPTY : value.copy();
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
        return roundabout$musicDisc;
    }

    @Override
    public void roundabout$setMusicDisc(ItemStack value) {
        roundabout$musicDisc = value == null ? ItemStack.EMPTY : value.copy();
    }

    @Unique
    private void roundabout$resetSightDiscState() {
        roundabout$hasSightDisc = true;
        roundabout$sightDiscOwnerId = "";
        roundabout$sightDiscOwnerName = "";
        roundabout$sightSealTicks = 0;
        roundabout$sightSealMaxTicks = 0;
    }

    @Unique
    private void roundabout$resetHearingDiscState() {
        roundabout$hasHearingDisc = true;
        roundabout$hearingDiscOwnerId = "";
        roundabout$hearingDiscOwnerName = "";
        roundabout$hearingSealTicks = 0;
        roundabout$hearingSealMaxTicks = 0;
    }

    @Unique
    private void roundabout$syncBodyDiscState(ServerPlayer player) {
        if (!roundabout$bodyDiscStateDirty || player.connection == null) return;
        S2CPacketUtil.syncWhitesnakeDiscState(player, WhitesnakeDiscUtil.MEMORY,
                entityData.get(ROUNDABOUT$HAS_MEMORY), roundabout$memorySealTicks, roundabout$memorySealMaxTicks);
        if (WhitesnakeDiscUtil.isSightDiscEnabled()) {
            S2CPacketUtil.syncWhitesnakeDiscState(player, WhitesnakeDiscUtil.SIGHT,
                    roundabout$hasSightDisc, roundabout$sightSealTicks, roundabout$sightSealMaxTicks);
        }
        if (WhitesnakeDiscUtil.isHearingDiscEnabled()) {
            S2CPacketUtil.syncWhitesnakeDiscState(player, WhitesnakeDiscUtil.HEARING,
                    roundabout$hasHearingDisc, roundabout$hearingSealTicks, roundabout$hearingSealMaxTicks);
        }
        roundabout$bodyDiscStateDirty = false;
    }

    @Unique
    private void roundabout$tickDiscSeals(LivingEntity living) {
        for (byte type = WhitesnakeDiscUtil.SIGHT; type <= WhitesnakeDiscUtil.HEARING; type++) {
            if (!WhitesnakeDiscUtil.isBodyDiscEnabled(type)) continue;
            int remaining = roundabout$getDiscSealTicks(type);
            if (remaining <= 0) continue;
            int next = ClientNetworking.getAppropriateConfig().whitesnakeSettings.discSealing ? remaining - 1 : 0;
            roundabout$setDiscSeal(type, next, roundabout$getDiscSealMaxTicks(type));
            if (next > 0) continue;
            if (type == WhitesnakeDiscUtil.MEMORY) {
                if (living instanceof Mob mob && mob.isNoAi()) mob.setNoAi(false);
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
