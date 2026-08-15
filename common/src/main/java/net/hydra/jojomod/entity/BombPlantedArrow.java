package net.hydra.jojomod.entity;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BombPlantedArrow extends Arrow {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(BombPlantedItemEntity.class, EntityDataSerializers.ITEM_STACK);
    public LivingEntity host = null;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
    }

    public void setItem(ItemStack $$0) { this.getEntityData().set(DATA_ITEM, $$0); }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public BombPlantedArrow(EntityType<? extends Arrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    public BombPlantedArrow(Level $$0, double $$1, double $$2, double $$3) {
        super($$0, $$1, $$2, $$3);
    }

    public BombPlantedArrow(Level $$0, LivingEntity $$1) {
        super($$0, $$1);
    }

    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (host == null || !(host.isAlive() && ((StandUser)host).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ
                && PKQ.bombEntity == this)) {
                defuse();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
    }

    public void setEffectsFromItem(ItemStack itemStack) {
        super.setEffectsFromItem(itemStack);
        setItem(itemStack);
    }

    public void defuse() {
        ArrowItem $$10 = (ArrowItem)(getItem().getItem() instanceof ArrowItem ? getItem().getItem() : Items.ARROW);
        AbstractArrow arrow = $$10.createArrow(level(), getItem(), host);
        arrow.setCritArrow(isCritArrow());
        arrow.setPos(getPosition(1));
        arrow.setYRot(getYRot());
        arrow.setXRot(getXRot());
        arrow.setPierceLevel(getPierceLevel());
        arrow.setDeltaMovement(getDeltaMovement());
        //arrow.pickup = pickup;

        level().addFreshEntity(arrow);
        discard();
    }
}
