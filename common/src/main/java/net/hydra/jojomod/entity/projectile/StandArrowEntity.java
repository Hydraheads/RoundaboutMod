package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StandArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> STAND_ARROW = SynchedEntityData.defineId(StandArrowEntity.class,
            EntityDataSerializers.ITEM_STACK);

    public StandArrowEntity(EntityType<? extends StandArrowEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    public StandArrowEntity(Level $$0, double $$1, double $$2, double $$3) {
        super(ModEntities.STAND_ARROW, $$1, $$2, $$3, $$0);
    }

    @Override
    public void tick() {
        Roundabout.LOGGER.info(""+this.getArrow());
        super.tick();
    }

    public StandArrowEntity(Level $$0, LivingEntity $$1, ItemStack stack) {
        super(ModEntities.STAND_ARROW, $$1, $$0);
        setArrow(stack.copy());
        Roundabout.LOGGER.info("Creation"+stack);
    }
    public ItemStack getArrow() {
        return this.entityData.get(STAND_ARROW);
    }
    public void setArrow(ItemStack arrow) {
        this.entityData.set(STAND_ARROW,arrow);
    }
    @Override
    protected ItemStack getPickupItem() {
        return getArrow();
    }


    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("standArrow",this.getPickupItem().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("standArrow");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setArrow(itemstack);
        super.readAdditionalSaveData($$0);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STAND_ARROW, ItemStack.EMPTY);
    }
}
