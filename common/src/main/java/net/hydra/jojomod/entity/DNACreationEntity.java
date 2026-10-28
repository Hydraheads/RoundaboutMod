package net.hydra.jojomod.entity;

import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;

public class DNACreationEntity extends LivingEntity {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(DNACreationEntity.class, EntityDataSerializers.ITEM_STACK);

    public DNACreationEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 15.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!this.getDNAItem().isEmpty()) {
            tag.put("DNAItem", this.getDNAItem().save(new CompoundTag()));
        }
        tag.putInt("TransformTicks", this.tickCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DNAItem")) {
            this.setDNAItem(ItemStack.of(tag.getCompound("DNAItem")));
        }
        if (tag.contains("TransformTicks")) {
            this.tickCount = tag.getInt("TransformTicks");
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
    }

    public void setDNAItem(ItemStack stack) {
        this.entityData.set(DATA_ITEM, stack.copy());
    }

    public ItemStack getDNAItem() {
        return this.entityData.get(DATA_ITEM);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            ItemStack dnaStack = this.getDNAItem();
            if (dnaStack.isEmpty()) {
                this.discard();
                return;
            }

            if (this.tickCount % 7 == 0) {
                MainUtil.sendParticlesIfPossible(
                        this,
                        this.level(),
                        ParticleTypes.HAPPY_VILLAGER,
                        this.getX(),
                        this.getY() + 0.5,
                        this.getZ(),
                        1,
                        0.2, 0.2, 0.2,
                        0.05
                );
            }

            Item dnaItem = dnaStack.getItem();

            if (MainUtil.GOLD_EXPERIENCE_TRANSFORM_RATE.containsKey(dnaItem)) {
                float requiredSeconds = MainUtil.GOLD_EXPERIENCE_TRANSFORM_RATE.get(dnaItem);
                float requiredTicks = requiredSeconds * 20.0f;

                if (this.tickCount >= requiredTicks) {
                    spawnFinalMob(dnaItem);
                    this.discard();
                }
            } else {
                this.discard();
            }
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    private void spawnFinalMob(Item dnaItem) {
        EntityType<?> targetType = MainUtil.GOLD_EXPERIENCE_DNA_DROPS.get(dnaItem);
        if (targetType != null) {
            net.minecraft.world.entity.Entity entity = targetType.create(this.level());

            if (entity instanceof Mob mob) {
                mob.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());

                // Check if it should be a baby
                boolean shouldBeBaby = MainUtil.GOLD_EXPERIENCE_IS_BABY.getOrDefault(dnaItem, false);
                if (shouldBeBaby && mob instanceof Animal animal) {
                    animal.setBaby(true);
                }

                this.level().addFreshEntity(mob);

                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.TOTEM_OF_UNDYING,
                            this.getX(), this.getY() + 0.5, this.getZ(),
                            35,
                            0.3, 0.3, 0.3,
                            0.25
                    );
                    serverLevel.sendParticles(
                            ParticleTypes.HAPPY_VILLAGER,
                            this.getX(), this.getY() + 0.5, this.getZ(),
                            20,
                            0.4, 0.4, 0.4,
                            0.05
                    );
                    this.level().playSound(
                            null,
                            this.blockPosition(),
                            ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.1F
                    );
                }
            }
        }
    }
}