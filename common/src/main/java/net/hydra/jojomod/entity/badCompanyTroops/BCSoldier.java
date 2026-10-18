package net.hydra.jojomod.entity.badCompanyTroops;

import net.hydra.jojomod.entity.FakeItemEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class BCSoldier extends BCTroop{
    public BCSoldier(EntityType<? extends BCTroop> entityType, Level world) {
        super(entityType, world);
    }

    private static final EntityDataAccessor<Boolean> BUNDLE = SynchedEntityData.defineId(BCSoldier.class, EntityDataSerializers.BOOLEAN);

    public boolean isDisplay = false;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BUNDLE, false);
    }

    public void setBundle(boolean value) {
        this.entityData.set(BUNDLE, value);
    }
    public boolean getBundle() {
        return this.entityData.get(BUNDLE);
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.4F).add(Attributes.MAX_HEALTH, 4.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    public static float size = 0.15f;
}
