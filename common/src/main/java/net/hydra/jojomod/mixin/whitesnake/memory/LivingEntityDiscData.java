package net.hydra.jojomod.mixin.whitesnake.memory;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.event.powers.whitesnake.disc.DreamingMemoryController;
import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryPersonality;
import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryAiController;
import net.hydra.jojomod.event.powers.whitesnake.disc.CommandDiscController;
import net.hydra.jojomod.event.powers.whitesnake.disc.MusicDiscController;
import net.hydra.jojomod.event.powers.whitesnake.disc.WhitesnakeDiscUtil;
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
    @Unique private static final EntityDataAccessor<Boolean> SBA$HAS_MEMORY =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique private static final EntityDataAccessor<Byte> SBA$MEMORY_PERSONALITY =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);
    @Unique private boolean sba$hasSightDisc = true;
    @Unique private boolean sba$hasHearingDisc = true;
    @Unique private String sba$sightDiscOwnerId = "";
    @Unique private String sba$sightDiscOwnerName = "";
    @Unique private String sba$memoryDiscOwnerId = "";
    @Unique private String sba$memoryDiscOwnerName = "";
    @Unique private String sba$memoryTameOwnerId = "";
    @Unique private String sba$memoryTameOwnerName = "";
    @Unique private String sba$hearingDiscOwnerId = "";
    @Unique private String sba$hearingDiscOwnerName = "";
    @Unique private int sba$sightSealTicks;
    @Unique private int sba$sightSealMaxTicks;
    @Unique private int sba$memorySealTicks;
    @Unique private int sba$memorySealMaxTicks;
    @Unique private int sba$hearingSealTicks;
    @Unique private int sba$hearingSealMaxTicks;
    @Unique private ItemStack sba$musicDisc = ItemStack.EMPTY;
    @Unique private boolean sba$bodyDiscStateDirty = true;
    @Unique private boolean sba$foreignDiscsDropped;
    @Unique private boolean sba$memoryDevelopmentLimited;
    @Unique private int sba$previousLevelDecreaseTicks;
    @Unique private int sba$memoryDevelopmentLimitedTicks;
    @Unique private CompoundTag sba$memoryReading = new CompoundTag();
    @Unique private ItemStack sba$temporaryMemoryDisc = ItemStack.EMPTY;
    @Unique private CompoundTag sba$memoryBeforeDreaming = new CompoundTag();

    protected LivingEntityDiscData(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void sba$defineDiscData(CallbackInfo ci) {
        if (!this.entityData.hasItem(SBA$HAS_MEMORY)) {
            this.entityData.define(SBA$HAS_MEMORY, true);
            this.entityData.define(SBA$MEMORY_PERSONALITY,
                    MemoryPersonality.classify((LivingEntity) (Object) this));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void sba$saveDiscData(CompoundTag tag, CallbackInfo ci) {
        CommandDiscController.save((LivingEntity) (Object) this, tag);
        if (WhitesnakeDiscUtil.isDiscBlacklisted((LivingEntity) (Object) this)) return;
        CompoundTag discs = new CompoundTag();
        discs.putBoolean("HasMemory", sba$ownsMemoryDisc());
        discs.putInt("MemorySealTicks", sba$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY));
        discs.putInt("MemorySealMaxTicks", sba$getDiscSealMaxTicks(WhitesnakeDiscUtil.MEMORY));
        discs.putString("MemoryOwnerId", sba$getMemoryDiscOwnerId());
        discs.putString("MemoryOwnerName", sba$getMemoryDiscOwnerName());
        discs.putString("MemoryTameOwnerId", sba$getMemoryTameOwnerId());
        discs.putString("MemoryTameOwnerName", sba$getMemoryTameOwnerName());
        if (WhitesnakeDiscUtil.isSightDiscEnabled()) {
            discs.putBoolean("HasSight", sba$hasSightDisc);
            discs.putInt("SightSealTicks", sba$sightSealTicks);
            discs.putInt("SightSealMaxTicks", sba$sightSealMaxTicks);
            discs.putString("SightOwnerId", sba$sightDiscOwnerId);
            discs.putString("SightOwnerName", sba$sightDiscOwnerName);
        }
        if (WhitesnakeDiscUtil.isHearingDiscEnabled()) {
            discs.putBoolean("HasHearing", sba$hasHearingDisc);
            discs.putInt("HearingSealTicks", sba$hearingSealTicks);
            discs.putInt("HearingSealMaxTicks", sba$hearingSealMaxTicks);
            discs.putString("HearingOwnerId", sba$hearingDiscOwnerId);
            discs.putString("HearingOwnerName", sba$hearingDiscOwnerName);
        }
        discs.putByte("MemoryPersonality", sba$getMemoryPersonality());
        discs.putBoolean("MemoryDevelopmentLimited", sba$memoryDevelopmentLimited);
        discs.putInt("PreviousLevelDecreaseTicks", sba$previousLevelDecreaseTicks);
        discs.putInt("MemoryDevelopmentLimitedTicks", sba$memoryDevelopmentLimitedTicks);
        if (!sba$memoryReading.isEmpty()) {
            discs.put("MemoryReading", sba$memoryReading.copy());
        }
        if (!sba$getMusicDisc().isEmpty()) {
            discs.put("MusicDisc", sba$getMusicDisc().save(new CompoundTag()));
        }
        if (sba$hasTemporaryMemoryDisc()) {
            discs.put("DreamingMemoryDisc", sba$temporaryMemoryDisc.save(new CompoundTag()));
            discs.put("MemoryBeforeDreaming", sba$memoryBeforeDreaming.copy());
        }
        tag.put("sba.WhitesnakeDiscs", discs);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void sba$loadDiscData(CompoundTag tag, CallbackInfo ci) {
        CommandDiscController.load((LivingEntity) (Object) this, tag);
        if (WhitesnakeDiscUtil.isDiscBlacklisted((LivingEntity) (Object) this)) return;
        if (!tag.contains("sba.WhitesnakeDiscs", 10)) {
            return;
        }
        CompoundTag discs = tag.getCompound("sba.WhitesnakeDiscs");
        sba$setMemoryDiscOwnerId(discs.getString("MemoryOwnerId"));
        sba$setMemoryDiscOwnerName(discs.getString("MemoryOwnerName"));
        sba$setMemoryTameOwnerId(discs.getString("MemoryTameOwnerId"));
        sba$setMemoryTameOwnerName(discs.getString("MemoryTameOwnerName"));
        if (WhitesnakeDiscUtil.isSightDiscEnabled()) {
            sba$hasSightDisc = WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this)
                    && (!discs.contains("HasSight") || discs.getBoolean("HasSight"));
            sba$sightDiscOwnerId = discs.getString("SightOwnerId");
            sba$sightDiscOwnerName = discs.getString("SightOwnerName");
            sba$sightSealTicks = discs.getInt("SightSealTicks");
            sba$sightSealMaxTicks = discs.getInt("SightSealMaxTicks");
        } else {
            sba$resetSightDiscState();
        }
        if (WhitesnakeDiscUtil.isHearingDiscEnabled()) {
            sba$hasHearingDisc = !discs.contains("HasHearing") || discs.getBoolean("HasHearing");
            sba$hearingDiscOwnerId = discs.getString("HearingOwnerId");
            sba$hearingDiscOwnerName = discs.getString("HearingOwnerName");
            sba$hearingSealTicks = discs.getInt("HearingSealTicks");
            sba$hearingSealMaxTicks = discs.getInt("HearingSealMaxTicks");
        } else {
            sba$resetHearingDiscState();
        }
        sba$bodyDiscStateDirty = true;
        if (discs.contains("MemoryPersonality")) {
            sba$setMemoryPersonality(discs.getByte("MemoryPersonality"));
        }
        sba$memoryDevelopmentLimited = discs.getBoolean("MemoryDevelopmentLimited");
        sba$previousLevelDecreaseTicks = discs.getInt("PreviousLevelDecreaseTicks");
        sba$memoryDevelopmentLimitedTicks = discs.getInt("MemoryDevelopmentLimitedTicks");
        sba$memoryReading = discs.contains("MemoryReading", 10)
                ? discs.getCompound("MemoryReading").copy() : new CompoundTag();
        sba$setMusicDisc(discs.contains("MusicDisc", 10)
                ? ItemStack.of(discs.getCompound("MusicDisc")) : ItemStack.EMPTY);
        sba$setTemporaryMemoryDisc(discs.contains("DreamingMemoryDisc", 10)
                ? ItemStack.of(discs.getCompound("DreamingMemoryDisc")) : ItemStack.EMPTY);
        sba$setMemoryBeforeDreaming(discs.contains("MemoryBeforeDreaming", 10)
                ? discs.getCompound("MemoryBeforeDreaming") : new CompoundTag());
        entityData.set(SBA$HAS_MEMORY,
                !discs.contains("HasMemory") || discs.getBoolean("HasMemory"));
        sba$bodyDiscStateDirty = true;
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.discSealing) {
            sba$setDiscSeal(WhitesnakeDiscUtil.MEMORY, discs.getInt("MemorySealTicks"),
                    discs.getInt("MemorySealMaxTicks"));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void sba$tickDiscEffects(CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (WhitesnakeDiscUtil.isDiscBlacklisted(living)) {
            CommandDiscController.ejectCommandDisc(living);
            return;
        }
        if (!level().isClientSide() && living instanceof ServerPlayer player) {
            WhitesnakeDiscUtil.ejectMobMemoryFromPlayer(player);
        }
        DreamingMemoryController.tick(living);
        if (WhitesnakeDiscUtil.canCarrySightDisc(living) && !sba$hasSightDisc() && !level().isClientSide()
                && (!living.hasEffect(MobEffects.BLINDNESS)
                || living.getEffect(MobEffects.BLINDNESS).getDuration() < 80)) {
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false, false));
        }
        boolean lobotomized = DiscItemData.isLobotomized(living);
        if (lobotomized) {
            living.xxa = 0;
            living.zza = 0;
            living.setXRot(90.0F);
            living.setYHeadRot(living.getYRot());
            if (living instanceof Mob mob) {
                if (mob.isNoAi()) mob.setNoAi(false);
                mob.setTarget(null);
                mob.getNavigation().stop();
                sba$groundLobotomizedFlyer(mob);
            }
        } else if (DiscItemData.isBlankMemoryMob(living)) {
            Mob mob = (Mob) living;
            if (mob.isNoAi()) mob.setNoAi(false);
            mob.setTarget(null);
        } else {
            MemoryAiController.tick(living);
        }
        CommandDiscController.tickExplosion(living);
        CommandDiscController.tick(living);
        MusicDiscController.tick(living);
        if (living instanceof ServerPlayer player) {
            sba$updateMemoryDevelopment(player);
        }
        if (!level().isClientSide()) {
            sba$tickDiscSeals(living);
            if (living instanceof ServerPlayer player) sba$syncBodyDiscState(player);
        }
    }

    @Unique
    private static void sba$groundLobotomizedFlyer(Mob mob) {
        if (!(mob instanceof FlyingMob) && !(mob instanceof Allay)) return;
        mob.setNoGravity(false);
        if (mob.onGround() || mob.isInWater() || mob.isInLava()) return;
        Vec3 movement = mob.getDeltaMovement();
        double fallSpeed = Math.max(-3.92D, Math.min(-0.08D, movement.y - 0.08D));
        mob.setDeltaMovement(0.0D, fallSpeed, 0.0D);
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void sba$ejectMusicDiscWhenDamaged(DamageSource source, float amount,
                                                       CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) MusicDiscController.ejectOnDamage((LivingEntity) (Object) this);
    }

    @Inject(method = "die", at = @At("TAIL"))
    private void sba$dropForeignDiscs(DamageSource source, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        CommandDiscController.ejectCommandDisc(living);
        if (sba$foreignDiscsDropped || living.level().isClientSide()
                || WhitesnakeDiscUtil.isDiscBlacklisted(living)) return;
        sba$foreignDiscsDropped = true;
        String entityId = living.getUUID().toString();

        if (WhitesnakeDiscUtil.isSightDiscEnabled() && sba$ownsSightDisc()
                && sba$isForeign(sba$getSightDiscOwnerId(), entityId)) {
            ItemStack stack = new ItemStack(ModItems.SIGHT_DISC);
            DiscItemData.setOwner(stack, sba$getSightDiscOwnerId(), sba$getSightDiscOwnerName());
            sba$setHasSightDisc(false);
            living.spawnAtLocation(stack, 0.35F);
        }
        if (sba$ownsMemoryDisc() && sba$isForeign(sba$getMemoryDiscOwnerId(), entityId)) {
            ItemStack stack = new ItemStack(ModItems.MEMORY_DISC);
            DiscItemData.setOwner(stack, sba$getMemoryDiscOwnerId(), sba$getMemoryDiscOwnerName());
            DiscItemData.setPersonality(stack, sba$getMemoryPersonality());
            DiscItemData.setTameOwner(stack, sba$getMemoryTameOwnerId(),
                    sba$getMemoryTameOwnerName());
            DiscItemData.setMemoryReading(stack, sba$getMemoryReading());
            sba$setHasMemoryDisc(false);
            sba$setMemoryReading(new CompoundTag());
            living.spawnAtLocation(stack, 0.35F);
        }
        if (WhitesnakeDiscUtil.isHearingDiscEnabled() && sba$ownsHearingDisc()
                && sba$isForeign(sba$getHearingDiscOwnerId(), entityId)) {
            ItemStack stack = new ItemStack(ModItems.HEARING_DISC);
            DiscItemData.setOwner(stack, sba$getHearingDiscOwnerId(), sba$getHearingDiscOwnerName());
            sba$setHasHearingDisc(false);
            living.spawnAtLocation(stack, 0.35F);
        }

        StandUser standUser = (StandUser) living;
        ItemStack standDisc = standUser.sba$getStandDisc();
        if (!(living instanceof KingCrimsonCloneEntity) && !standDisc.isEmpty()
                && sba$isForeign(DiscItemData.getOwnerId(standDisc), entityId)) {
            ItemStack stack = MainUtil.saveToDiscData(living, standDisc.copy());
            standUser.sba$getStandPowers().onStandSwitch();
            standUser.sba$setStand(null);
            standUser.sba$setActive(false);
            standUser.sba$setStandDisc(ItemStack.EMPTY);
            standUser.sba$setStandPowers(null);
            living.spawnAtLocation(stack, 0.35F);
        }
    }

    @Unique
    private static boolean sba$isForeign(String ownerId, String entityId) {
        return ownerId != null && !ownerId.isEmpty() && !ownerId.equals(entityId);
    }

    @Override
    public boolean sba$hasSightDisc() {
        if (!WhitesnakeDiscUtil.isSightDiscEnabled()) return true;
        return sba$ownsSightDisc() && sba$getDiscSealTicks(WhitesnakeDiscUtil.SIGHT) <= 0;
    }

    @Override
    public boolean sba$ownsSightDisc() {
        return !WhitesnakeDiscUtil.isSightDiscEnabled()
                || WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this) && sba$hasSightDisc;
    }

    @Override
    public void sba$setHasSightDisc(boolean value) {
        sba$hasSightDisc = !WhitesnakeDiscUtil.isSightDiscEnabled()
                || value && WhitesnakeDiscUtil.canCarrySightDisc((LivingEntity) (Object) this);
        sba$setDiscSeal(WhitesnakeDiscUtil.SIGHT, 0, 0);
        sba$bodyDiscStateDirty = true;
    }
    @Override
    public boolean sba$hasMemoryDisc() {
        return sba$ownsMemoryDisc() && sba$getDiscSealTicks(WhitesnakeDiscUtil.MEMORY) <= 0;
    }

    @Override
    public boolean sba$ownsMemoryDisc() {
        return entityData.get(SBA$HAS_MEMORY);
    }

    @Override
    public void sba$setHasMemoryDisc(boolean value) {
        entityData.set(SBA$HAS_MEMORY, value);
        sba$setDiscSeal(WhitesnakeDiscUtil.MEMORY, 0, 0);
        sba$bodyDiscStateDirty = true;
    }
    @Override
    public boolean sba$hasHearingDisc() {
        if (!WhitesnakeDiscUtil.isHearingDiscEnabled()) return true;
        return sba$ownsHearingDisc() && sba$getDiscSealTicks(WhitesnakeDiscUtil.HEARING) <= 0;
    }

    @Override
    public boolean sba$ownsHearingDisc() {
        return !WhitesnakeDiscUtil.isHearingDiscEnabled() || sba$hasHearingDisc;
    }

    @Override
    public void sba$setHasHearingDisc(boolean value) {
        sba$hasHearingDisc = !WhitesnakeDiscUtil.isHearingDiscEnabled() || value;
        sba$setDiscSeal(WhitesnakeDiscUtil.HEARING, 0, 0);
        sba$bodyDiscStateDirty = true;
    }
    @Override
    public int sba$getDiscSealTicks(byte type) {
        return switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> WhitesnakeDiscUtil.isSightDiscEnabled()
                    ? sba$sightSealTicks : 0;
            case WhitesnakeDiscUtil.MEMORY -> sba$memorySealTicks;
            case WhitesnakeDiscUtil.HEARING -> WhitesnakeDiscUtil.isHearingDiscEnabled()
                    ? sba$hearingSealTicks : 0;
            default -> 0;
        };
    }
    @Override
    public int sba$getDiscSealMaxTicks(byte type) {
        return switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> WhitesnakeDiscUtil.isSightDiscEnabled()
                    ? sba$sightSealMaxTicks : 0;
            case WhitesnakeDiscUtil.MEMORY -> sba$memorySealMaxTicks;
            case WhitesnakeDiscUtil.HEARING -> WhitesnakeDiscUtil.isHearingDiscEnabled()
                    ? sba$hearingSealMaxTicks : 0;
            default -> 0;
        };
    }
    @Override
    public void sba$setDiscSeal(byte type, int ticks, int maxTicks) {
        int remaining = Math.max(0, ticks);
        int maximum = Math.max(remaining, maxTicks);
        switch (type) {
            case WhitesnakeDiscUtil.SIGHT -> {
                if (!WhitesnakeDiscUtil.isSightDiscEnabled()) {
                    sba$resetSightDiscState();
                    return;
                }
                sba$sightSealTicks = remaining;
                sba$sightSealMaxTicks = maximum;
                sba$bodyDiscStateDirty = true;
            }
            case WhitesnakeDiscUtil.MEMORY -> {
                sba$memorySealTicks = remaining;
                sba$memorySealMaxTicks = maximum;
                sba$bodyDiscStateDirty = true;
            }
            case WhitesnakeDiscUtil.HEARING -> {
                if (!WhitesnakeDiscUtil.isHearingDiscEnabled()) {
                    sba$resetHearingDiscState();
                    return;
                }
                sba$hearingSealTicks = remaining;
                sba$hearingSealMaxTicks = maximum;
                sba$bodyDiscStateDirty = true;
            }
        }
    }
    @Override
    public String sba$getSightDiscOwnerId() {
        return WhitesnakeDiscUtil.isSightDiscEnabled() ? sba$sightDiscOwnerId : "";
    }

    @Override
    public void sba$setSightDiscOwnerId(String value) {
        sba$sightDiscOwnerId = WhitesnakeDiscUtil.isSightDiscEnabled() && value != null ? value : "";
    }

    @Override
    public String sba$getSightDiscOwnerName() {
        return WhitesnakeDiscUtil.isSightDiscEnabled() ? sba$sightDiscOwnerName : "";
    }

    @Override
    public void sba$setSightDiscOwnerName(String value) {
        sba$sightDiscOwnerName = WhitesnakeDiscUtil.isSightDiscEnabled() && value != null ? value : "";
    }

    @Override
    public String sba$getMemoryDiscOwnerId() {
        return sba$memoryDiscOwnerId;
    }

    @Override
    public void sba$setMemoryDiscOwnerId(String value) {
        sba$memoryDiscOwnerId = value == null ? "" : value;
    }

    @Override
    public String sba$getMemoryDiscOwnerName() {
        return sba$memoryDiscOwnerName;
    }

    @Override
    public void sba$setMemoryDiscOwnerName(String value) {
        sba$memoryDiscOwnerName = value == null ? "" : value;
    }

    @Override
    public String sba$getMemoryTameOwnerId() {
        return sba$memoryTameOwnerId;
    }

    @Override
    public void sba$setMemoryTameOwnerId(String value) {
        sba$memoryTameOwnerId = value == null ? "" : value;
    }

    @Override
    public String sba$getMemoryTameOwnerName() {
        return sba$memoryTameOwnerName;
    }

    @Override
    public void sba$setMemoryTameOwnerName(String value) {
        sba$memoryTameOwnerName = value == null ? "" : value;
    }

    @Override
    public String sba$getHearingDiscOwnerId() {
        return WhitesnakeDiscUtil.isHearingDiscEnabled() ? sba$hearingDiscOwnerId : "";
    }

    @Override
    public void sba$setHearingDiscOwnerId(String value) {
        sba$hearingDiscOwnerId = WhitesnakeDiscUtil.isHearingDiscEnabled() && value != null ? value : "";
    }

    @Override
    public String sba$getHearingDiscOwnerName() {
        return WhitesnakeDiscUtil.isHearingDiscEnabled() ? sba$hearingDiscOwnerName : "";
    }

    @Override
    public void sba$setHearingDiscOwnerName(String value) {
        sba$hearingDiscOwnerName = WhitesnakeDiscUtil.isHearingDiscEnabled() && value != null ? value : "";
    }

    @Override
    public byte sba$getMemoryPersonality() {
        return entityData.get(SBA$MEMORY_PERSONALITY);
    }

    @Override
    public void sba$setMemoryPersonality(byte value) {
        entityData.set(SBA$MEMORY_PERSONALITY, value);
    }
    @Override
    public CompoundTag sba$getMemoryReading() {
        return sba$memoryReading.copy();
    }

    @Override
    public void sba$setMemoryReading(CompoundTag value) {
        sba$memoryReading = value == null ? new CompoundTag() : value.copy();
    }

    @Override
    public boolean sba$hasTemporaryMemoryDisc() {
        return !sba$temporaryMemoryDisc.isEmpty();
    }

    @Override
    public ItemStack sba$getTemporaryMemoryDisc() {
        return sba$temporaryMemoryDisc.copy();
    }

    @Override
    public void sba$setTemporaryMemoryDisc(ItemStack value) {
        sba$temporaryMemoryDisc = value == null ? ItemStack.EMPTY : value.copy();
    }

    @Override
    public CompoundTag sba$getMemoryBeforeDreaming() {
        return sba$memoryBeforeDreaming.copy();
    }

    @Override
    public void sba$setMemoryBeforeDreaming(CompoundTag value) {
        sba$memoryBeforeDreaming = value == null ? new CompoundTag() : value.copy();
    }

    @Override
    public ItemStack sba$getMusicDisc() {
        return sba$musicDisc;
    }

    @Override
    public void sba$setMusicDisc(ItemStack value) {
        sba$musicDisc = value == null ? ItemStack.EMPTY : value.copy();
    }

    @Unique
    private void sba$resetSightDiscState() {
        sba$hasSightDisc = true;
        sba$sightDiscOwnerId = "";
        sba$sightDiscOwnerName = "";
        sba$sightSealTicks = 0;
        sba$sightSealMaxTicks = 0;
    }

    @Unique
    private void sba$resetHearingDiscState() {
        sba$hasHearingDisc = true;
        sba$hearingDiscOwnerId = "";
        sba$hearingDiscOwnerName = "";
        sba$hearingSealTicks = 0;
        sba$hearingSealMaxTicks = 0;
    }

    @Unique
    private void sba$syncBodyDiscState(ServerPlayer player) {
        if (!sba$bodyDiscStateDirty || player.connection == null) return;
        S2CPacketUtil.syncWhitesnakeDiscState(player, WhitesnakeDiscUtil.MEMORY,
                entityData.get(SBA$HAS_MEMORY), sba$memorySealTicks, sba$memorySealMaxTicks);
        if (WhitesnakeDiscUtil.isSightDiscEnabled()) {
            S2CPacketUtil.syncWhitesnakeDiscState(player, WhitesnakeDiscUtil.SIGHT,
                    sba$hasSightDisc, sba$sightSealTicks, sba$sightSealMaxTicks);
        }
        if (WhitesnakeDiscUtil.isHearingDiscEnabled()) {
            S2CPacketUtil.syncWhitesnakeDiscState(player, WhitesnakeDiscUtil.HEARING,
                    sba$hasHearingDisc, sba$hearingSealTicks, sba$hearingSealMaxTicks);
        }
        sba$bodyDiscStateDirty = false;
    }

    @Unique
    private void sba$tickDiscSeals(LivingEntity living) {
        for (byte type = WhitesnakeDiscUtil.SIGHT; type <= WhitesnakeDiscUtil.HEARING; type++) {
            if (!WhitesnakeDiscUtil.isBodyDiscEnabled(type)) continue;
            int remaining = sba$getDiscSealTicks(type);
            if (remaining <= 0) continue;
            int next = ClientNetworking.getAppropriateConfig().whitesnakeSettings.discSealing ? remaining - 1 : 0;
            sba$setDiscSeal(type, next, sba$getDiscSealMaxTicks(type));
            if (next > 0) continue;
            if (type == WhitesnakeDiscUtil.MEMORY) {
                if (living instanceof Mob mob && mob.isNoAi()) mob.setNoAi(false);
            }
        }
    }

    @Unique
    private void sba$updateMemoryDevelopment(ServerPlayer player) {
        IPlayerEntity playerData = (IPlayerEntity) player;
        boolean limited = DiscItemData.isMemoryDevelopmentLimited(player);
        if (limited) {
            if (!sba$memoryDevelopmentLimited) {
                sba$memoryDevelopmentLimited = true;
                sba$previousLevelDecreaseTicks = playerData.rdbt$getLevelDecreaseTicks();
                sba$memoryDevelopmentLimitedTicks = 0;
            }
            sba$memoryDevelopmentLimitedTicks++;
            int currentLevelDecreaseTicks = playerData.rdbt$getLevelDecreaseTicks();
            if (currentLevelDecreaseTicks < 1_000_000) {
                sba$previousLevelDecreaseTicks = Math.max(sba$previousLevelDecreaseTicks,
                        currentLevelDecreaseTicks + sba$memoryDevelopmentLimitedTicks);
                playerData.rdbt$setLevelDecreaseTicks(Integer.MAX_VALUE / 2);
            }
        } else if (sba$memoryDevelopmentLimited) {
            int remaining = Math.max(0,
                    sba$previousLevelDecreaseTicks - sba$memoryDevelopmentLimitedTicks);
            sba$memoryDevelopmentLimited = false;
            sba$previousLevelDecreaseTicks = 0;
            sba$memoryDevelopmentLimitedTicks = 0;
            playerData.rdbt$setLevelDecreaseTicks(remaining);
        }
    }
}
