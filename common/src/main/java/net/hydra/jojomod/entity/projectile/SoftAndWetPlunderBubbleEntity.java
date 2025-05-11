package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class SoftAndWetPlunderBubbleEntity extends SoftAndWetBubbleEntity {
    private static final EntityDataAccessor<Byte> PLUNDER_TYPE = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> ACTIVATED = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> BLOCK_POS = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BLOCK_POS);


    public int lifeSpan = 0;

    public SoftAndWetPlunderBubbleEntity(LivingEntity $$1, Level $$2) {
        super(ModEntities.PLUNDER_BUBBLE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }

    public SoftAndWetPlunderBubbleEntity(EntityType<SoftAndWetPlunderBubbleEntity> softAndWetPlunderBubbleEntityEntityType, Level level) {
        super(ModEntities.PLUNDER_BUBBLE, level);
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if ((this.getPlunderType() == PlunderTypes.FRICTION.id || this.getPlunderType() == PlunderTypes.SOUND.id) && !this.getActivated()){
            this.setBlockPos($$0.getBlockPos().above());
            this.setBlockPos($$0.getBlockPos());
            this.setActivated(true);
            this.setDeltaMovement(0,0.01,0);
        } else {
            super.onHitBlock($$0);
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        super.onHitEntity($$0);
    }
    @Override
    public void tick() {

        if (!this.level().isClientSide()){
            lifeSpan--;
            if (lifeSpan <= 0){
                popBubble();
                return;
            }
        }

        if (this.getActivated()){
            if (this.getPlunderType() == PlunderTypes.FRICTION.id || this.getPlunderType() == PlunderTypes.SOUND.id){
                ((ILevelAccess)this.level()).roundabout$addPlunderBubble(this);
            }
        }


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

    public BlockPos getBlockPos() {
        return this.getEntityData().get(BLOCK_POS);
    }
    public void setBlockPos(BlockPos bpos) {
        this.getEntityData().set(BLOCK_POS, bpos);
    }
    public boolean getActivated() {
        return this.getEntityData().get(ACTIVATED);
    }
    public void setActivated(boolean activ) {
        this.getEntityData().set(ACTIVATED, activ);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(PLUNDER_TYPE)){
            this.entityData.define(PLUNDER_TYPE, (byte)0);
            this.entityData.define(BLOCK_POS, BlockPos.ZERO);
            this.entityData.define(ACTIVATED, false);
        }
    }

}
