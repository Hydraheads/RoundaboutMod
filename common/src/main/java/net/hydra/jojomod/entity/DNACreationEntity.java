package net.hydra.jojomod.entity;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
            }
        }
    }
}