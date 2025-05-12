package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.StoredSoundInstance;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

public class SoftAndWetPlunderBubbleEntity extends SoftAndWetBubbleEntity {
    private static final EntityDataAccessor<Byte> PLUNDER_TYPE = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<BlockPos> BLOCK_POS = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> FINISHED = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ENTITY_STOLEN = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SINGULAR = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BOOLEAN);

    @Unique
    public List<StoredSoundInstance> bubbleSounds = new ArrayList<>();

    @Unique
    public void bubbleSoundsInit(){
        if (bubbleSounds == null) {
            bubbleSounds = new ArrayList<>();
        }
    }
    @Unique
    public void addPlunderBubbleSounds(StoredSoundInstance plunder){
        bubbleSoundsInit();
        bubbleSounds.add(plunder);
    }
    @Unique
    public void addPlunderBubbleSounds(SoundEvent soundEvent, SoundSource soundSource, float pitch, float volum){
        bubbleSoundsInit();
        bubbleSounds.add(new StoredSoundInstance(soundEvent,soundSource,pitch,volum));
    }
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
        if (!getFinished()) {
            if ((this.getPlunderType() == PlunderTypes.FRICTION.id || this.getPlunderType() == PlunderTypes.SOUND.id) && !this.getActivated()) {
                this.setBlockPos($$0.getBlockPos().above());
                this.setBlockPos($$0.getBlockPos());
                setFloating();
            } else {
                super.onHitBlock($$0);
            }
        }
    }

    public void setFloating(){
        this.setActivated(true);
        this.setDeltaMovement(0,0.01,0);
    }

    public void popSounds(){
        bubbleSoundsInit();

        List<StoredSoundInstance> bs = new ArrayList<>(bubbleSounds) {};
        if (bs != null && !bs.isEmpty()){
                for (StoredSoundInstance value : bs) {

                if (this.level().isClientSide()){
                    this.level().playLocalSound(this.blockPosition().getX(), this.blockPosition().getY(),
                            this.blockPosition().getZ(), value.soundEvent, value.soundSource,
                            value.pitch, value.volume, false);
                } else {
                    this.level().playSound(null, this.blockPosition(), value.soundEvent,
                            value.soundSource, value.pitch, value.volume);
                }
            }
        } else {

        }
    }
    @Override
    public void popBubble(){
        this.setFinished(true);

        if (!this.level().isClientSide()) {
            ServerLevel serverWorld = ((ServerLevel) this.level());
            Vec3 userLocation = new Vec3(this.getX(),  this.getY(), this.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    ModPacketHandler.PACKET_ACCESS.sendIntPacket(serverPlayerEntity, PacketDataIndex.S2C_INT_BUBBLE_FINISH,this.getId());
                }
            }

            this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                    SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            popSounds();
        }


        this.discard();
    }
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!getActivated() && !getFinished() && !($$0.getEntity() instanceof SoftAndWetBubbleEntity) && !($$0.getEntity().getId() == getUserID())) {
            if (this.getPlunderType() == PlunderTypes.SOUND.id){
                this.setEntityStolen($$0.getEntity().getId());
                setFloating();
            } else {
                super.onHitEntity($$0);
            }
        }
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
                if (getEntityStolen() <= 0) {
                    ((ILevelAccess) this.level()).roundabout$addPlunderBubble(this);
                } else {
                    ((ILevelAccess) this.level()).roundabout$addPlunderBubbleEntity(this);
                }
            }
        }

            Entity owner = this.getOwner();
            if (getSingular() && this.getOwner() != null && !this.getActivated()) {
                this.shootFromRotationDeltaAgnostic2(owner, owner.getXRot(), owner.getYRot(), 1.0F, getSped());
            }


        super.tick();

        if (this.getFinished()){
            this.discard();
        }
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
    public boolean getFinished() {
        return this.getEntityData().get(FINISHED);
    }
    public void setFinished(boolean activ) {
        this.getEntityData().set(FINISHED, activ);
    }
    public boolean getSingular() {
        return this.getEntityData().get(SINGULAR);
    }
    public void setSingular(boolean single) {
        this.getEntityData().set(SINGULAR, single);
    }
    public int getEntityStolen() {
        return this.getEntityData().get(ENTITY_STOLEN);
    }
    public void setEntityStolen(int entid) {
        this.getEntityData().set(ENTITY_STOLEN, entid);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(PLUNDER_TYPE)){
            this.entityData.define(PLUNDER_TYPE, (byte)0);
            this.entityData.define(BLOCK_POS, BlockPos.ZERO);
            this.entityData.define(FINISHED, false);
            this.entityData.define(SINGULAR, false);
            this.entityData.define(ENTITY_STOLEN, -1);
        }
    }

}
