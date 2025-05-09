package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SoftAndWetPlunderBubbleEntity extends SoftAndWetBubbleEntity {
    private static final EntityDataAccessor<Byte> PLUNDER_TYPE = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BYTE);

    public SoftAndWetPlunderBubbleEntity(LivingEntity $$1, Level $$2) {
        super(ModEntities.PLUNDER_BUBBLE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public SoftAndWetPlunderBubbleEntity(EntityType<SoftAndWetPlunderBubbleEntity> softAndWetPlunderBubbleEntityEntityType, Level level) {
        super(ModEntities.PLUNDER_BUBBLE, level);
    }



    @Override
    public void tick(){
        super.tick();
    }

    public int getPlunderType() {
        return this.getEntityData().get(PLUNDER_TYPE);
    }
    public void setPlunderType(byte bt) {
        this.getEntityData().set(PLUNDER_TYPE, bt);
    }
    public PlunderTypes getPlunderTypes(PlunderTypes bt) {
        return PlunderTypes.getPlunderTypeDromByte(this.getEntityData().get(PLUNDER_TYPE));
    }
    public void setPlunderType(PlunderTypes bt) {
        this.getEntityData().set(PLUNDER_TYPE, bt.id);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(PLUNDER_TYPE)){
            this.entityData.define(PLUNDER_TYPE, (byte)0);
        }
    }

}
