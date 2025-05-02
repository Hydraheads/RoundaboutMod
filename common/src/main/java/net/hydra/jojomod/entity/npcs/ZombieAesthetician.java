package net.hydra.jojomod.entity.npcs;

import com.mojang.logging.LogUtils;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.UUID;

public class ZombieAesthetician extends Zombie {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<Boolean> DATA_CONVERTING_ID =
            SynchedEntityData.defineId(ZombieAesthetician.class, EntityDataSerializers.BOOLEAN);;
    private static final int VILLAGER_CONVERSION_WAIT_MIN = 3600;
    private static final int VILLAGER_CONVERSION_WAIT_MAX = 6000;
    private static final int MAX_SPECIAL_BLOCKS_COUNT = 14;
    private static final int SPECIAL_BLOCK_RADIUS = 4;
    private int villagerConversionTime;
    @Nullable
    private UUID conversionStarter;
    @Nullable
    private Tag gossips;
    @Nullable
    private CompoundTag tradeOffers;
    private int villagerXp;

    public ZombieAesthetician(EntityType<? extends ZombieAesthetician> entityType, Level level) {
        super(entityType, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(ROUNDABOUT$SKIN_NUMBER)) {
            this.entityData.define(DATA_CONVERTING_ID, false);
            this.entityData.define(ROUNDABOUT$SKIN_NUMBER, 1);
        }
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        if (this.gossips != null) {
            compoundTag.put("Gossips", this.gossips);
        }

        compoundTag.putInt("ConversionTime", this.isConverting() ? this.villagerConversionTime : -1);
        if (this.conversionStarter != null) {
            compoundTag.putUUID("ConversionPlayer", this.conversionStarter);
        }

        compoundTag.putInt("Xp", this.villagerXp);
    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);


        if (compoundTag.contains("Gossips", 9)) {
            this.gossips = compoundTag.getList("Gossips", 10);
        }

        if (compoundTag.contains("ConversionTime", 99) && compoundTag.getInt("ConversionTime") > -1) {
            this.startConverting(compoundTag.hasUUID("ConversionPlayer") ? compoundTag.getUUID("ConversionPlayer") : null, compoundTag.getInt("ConversionTime"));
        }

        if (compoundTag.contains("Xp", 3)) {
            this.villagerXp = compoundTag.getInt("Xp");
        }

    }

    public void tick() {
        if (!this.level().isClientSide && this.isAlive() && this.isConverting()) {
            int i = this.getConversionProgress();
            this.villagerConversionTime -= i;
            if (this.villagerConversionTime <= 0) {
                this.finishConversion((ServerLevel) this.level());
            }
        }

        super.tick();
    }

    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (itemStack.is(Items.GOLDEN_APPLE)) {
            if (this.hasEffect(MobEffects.WEAKNESS)) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                if (!this.level().isClientSide) {
                    this.startConverting(player.getUUID(), this.random.nextInt(2401) + 3600);
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, interactionHand);
        }
    }

    protected boolean convertsInWater() {
        return false;
    }

    public boolean removeWhenFarAway(double d) {
        return !this.isConverting() && this.villagerXp == 0;
    }

    public boolean isConverting() {
        return (Boolean) this.getEntityData().get(DATA_CONVERTING_ID);
    }

    private void startConverting(@Nullable UUID uUID, int i) {
        this.conversionStarter = uUID;
        Roundabout.LOGGER.info("1");
        this.villagerConversionTime = i;
        this.getEntityData().set(DATA_CONVERTING_ID, true);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, i, Math.min(this.level().getDifficulty().getId() - 1, 0)));
        this.level().broadcastEntityEvent(this, (byte) 16);
    }

    public void handleEntityEvent(byte b) {
        if (b == 16) {
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

        } else {
            super.handleEntityEvent(b);
        }
    }

    private void finishConversion(ServerLevel serverLevel) {
        Roundabout.LOGGER.info("4");
        Aesthetician villager = this.convertTo(ModEntities.AESTHETICIAN, false);
        villager.setSkinNumber(getSkinNumber());
        EquipmentSlot[] var3 = EquipmentSlot.values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            EquipmentSlot equipmentSlot = var3[var5];
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                if (EnchantmentHelper.hasBindingCurse(itemStack)) {
                    villager.getSlot(equipmentSlot.getIndex() + 300).set(itemStack);
                } else {
                    double d = (double) this.getEquipmentDropChance(equipmentSlot);
                    if (d > 1.0) {
                        this.spawnAtLocation(itemStack);
                    }
                }
            }
        }

        if (this.gossips != null) {
            villager.setGossips(this.gossips);
        }

        villager.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(villager.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData) null, (CompoundTag) null);
        villager.refreshBrain(serverLevel);
        if (this.conversionStarter != null) {
            Player player = serverLevel.getPlayerByUUID(this.conversionStarter);
            if (player instanceof ServerPlayer) {
                serverLevel.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, player, villager);
            }
        }

        villager.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        if (!this.isSilent()) {
            serverLevel.levelEvent((Player) null, 1027, this.blockPosition(), 0);
        }

    }

    private int getConversionProgress() {
        int i = 1;
        if (this.random.nextFloat() < 0.01F) {
            int j = 0;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for (int k = (int) this.getX() - 4; k < (int) this.getX() + 4 && j < 14; ++k) {
                for (int l = (int) this.getY() - 4; l < (int) this.getY() + 4 && j < 14; ++l) {
                    for (int m = (int) this.getZ() - 4; m < (int) this.getZ() + 4 && j < 14; ++m) {
                        BlockState blockState = this.level().getBlockState(mutableBlockPos.set(k, l, m));
                        if (blockState.is(Blocks.IRON_BARS) || blockState.getBlock() instanceof BedBlock) {
                            if (this.random.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public SoundEvent getAmbientSound() {
        return null;
    }

    public SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    public SoundEvent getDeathSound() {
        return null;
    }

    public SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_VILLAGER_STEP;
    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }
    private static final EntityDataAccessor<Integer> ROUNDABOUT$SKIN_NUMBER = SynchedEntityData.defineId(ZombieAesthetician.class,
            EntityDataSerializers.INT);

    public void setSkinNumber(int Pos){
        this.getEntityData().set(ROUNDABOUT$SKIN_NUMBER, Pos);
    }
    public int getSkinNumber(){
        return this.getEntityData().get(ROUNDABOUT$SKIN_NUMBER);
    }
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {

        RandomSource $$5 = serverLevelAccessor.getRandom();
        if ($$5.nextFloat() < 0.2F) {
            setSkinNumber(2);
        } else if ($$5.nextFloat() < 0.4F) {
            setSkinNumber(3);
        } else if ($$5.nextFloat() < 0.6F) {
            setSkinNumber(4);
        } else if ($$5.nextFloat() < 0.8F) {
            setSkinNumber(5);
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

}
