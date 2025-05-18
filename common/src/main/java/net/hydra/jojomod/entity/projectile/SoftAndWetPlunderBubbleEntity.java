package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.StoredSoundInstance;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SoftAndWetPlunderBubbleEntity extends SoftAndWetBubbleEntity {
    private static final EntityDataAccessor<Byte> PLUNDER_TYPE = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<BlockPos> BLOCK_POS = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> FINISHED = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ENTITY_STOLEN = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SINGULAR = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BOOLEAN);

    @Unique
    public List<StoredSoundInstance> bubbleSounds = new ArrayList<>();

    @Unique
    public void bubbleSoundsInit(){
        if (bubbleSounds == null) {
            bubbleSounds = new ArrayList<>();
        }
    }

    @Override
    public boolean isPickable() {
        if (this.getPlunderType() == PlunderTypes.POTION_EFFECTS.id && this.getActivated()){
            return true;
        }
        return super.isPickable();
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
        if (!getFinished() && !getReturning()) {
            if ((this.getPlunderType() == PlunderTypes.FRICTION.id || this.getPlunderType() == PlunderTypes.SOUND.id) && !this.getActivated()) {
                this.setBlockPos($$0.getBlockPos().above());
                this.setBlockPos($$0.getBlockPos());
                setFloating();
            } else if (this.getPlunderType() == PlunderTypes.OXYGEN.id){
                if (this.standUser != null) {
                    if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof MagmaBlock) {
                        airSupply = this.standUser.getMaxAirSupply();
                        startReturning();
                    } else {
                        Roundabout.LOGGER.info("3");
                        super.onHitBlock($$0);
                    }
                } else {
                    Roundabout.LOGGER.info("4");
                    super.onHitBlock($$0);
                }
            } else {
                Roundabout.LOGGER.info("4");
                super.onHitBlock($$0);
            }
        }
    }

    public void setFloating(){
        if (this.getPlunderType() != PlunderTypes.SOUND.id) {
            this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_PLUNDER_EVENT,
                    SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
        }

        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(ModParticles.PLUNDER,
                    this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                    10, 0.2, 0.2, 0.2, 0.015);
        }
        this.setActivated(true);
        this.setDeltaMovement(0, 0.01, 0);
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
                ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                        this.getX(), this.getY() + this.getBbHeight()*0.6, this.getZ(),
                        1, 0, 0,0, 0.015);
            this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                    SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
            popSounds();
        }


        this.discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("roundabout.HeldItem",this.getHeldItem().save(compoundtag));
        $$0.putBoolean("roundabout.ditchedItem",hasDitchedItem);
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("roundabout.HeldItem");
        ItemStack itemstack = ItemStack.of(compoundtag);
        hasDitchedItem = $$0.getBoolean("roundabout.ditchedItem");
        this.setHeldItem(itemstack);
        super.readAdditionalSaveData($$0);
    }

    public int airSupply = 0;
    Collection<MobEffectInstance> mobEffects;
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide()) {
            if (!getActivated() && !getFinished() && !($$0.getEntity() instanceof SoftAndWetBubbleEntity) && !($$0.getEntity().getId() == getUserID())
                    && !getReturning()) {
                if (this.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (!((ILevelAccess) this.level()).roundabout$isSoundPlunderedEntity($$0.getEntity())) {
                        this.setEntityStolen($$0.getEntity().getId());
                        setFloating();
                    } else {
                        super.onHitEntity($$0);
                    }
                } else if (this.getPlunderType() == PlunderTypes.FRICTION.id) {
                    if ($$0.getEntity() instanceof LivingEntity LE &&
                            MainUtil.canHaveFrictionTaken(LE)) {
                        if (!((ILevelAccess) this.level()).roundabout$isFrictionPlunderedEntity($$0.getEntity())) {
                            this.setEntityStolen($$0.getEntity().getId());
                            setFloating();
                        }
                    } else {
                        super.onHitEntity($$0);
                    }
                } else if (this.getPlunderType() == PlunderTypes.POTION_EFFECTS.id) {
                    if ($$0.getEntity() instanceof LivingEntity LE && !LE.getActiveEffects().isEmpty()) {
                        Collection<MobEffectInstance> effects = new ArrayList<>(LE.getActiveEffects());
                        if (!effects.isEmpty()) {
                            Collection<MobEffectInstance> effects2 = new ArrayList<>();
                            for (MobEffectInstance value : effects) {
                                if (!MainUtil.isSpecialEffect(value)) {
                                    effects2.add(new MobEffectInstance(value));
                                    LE.getActiveEffects().remove(value);
                                }
                            }
                            if (!effects2.isEmpty()) {
                                mobEffects = effects2;
                                setFloating();
                            } else {
                                super.onHitEntity($$0);
                            }
                        } else {
                            super.onHitEntity($$0);
                        }
                    } else {
                        super.onHitEntity($$0);
                    }
                } else if (this.getPlunderType() == PlunderTypes.SIGHT.id) {
                    if ($$0.getEntity() instanceof LivingEntity LE && ((StandUser) LE).roundabout$getEyeSightTaken() == null &&
                            MainUtil.canHaveSightTaken(LE)) {
                        this.setEntityStolen($$0.getEntity().getId());
                        if (!this.level().isClientSide()) {
                            ((StandUser) LE).roundabout$deeplyRemoveAttackTarget();
                            ((StandUser) LE).roundabout$setEyeSightTaken(this);
                        }
                        setFloating();
                    } else {
                        super.onHitEntity($$0);
                    }
                } else if (this.getPlunderType() == PlunderTypes.OXYGEN.id) {
                    if ($$0.getEntity() instanceof LivingEntity LE && !LE.canBreatheUnderwater()) {
                        int supply = $$0.getEntity().getAirSupply();
                        if (supply > 0) {
                            airSupply = supply;
                            $$0.getEntity().setAirSupply(0);
                            startReturning();
                        }
                    } else {
                        super.onHitEntity($$0);
                    }
                } else {
                    super.onHitEntity($$0);
                }
            }
        }
    }

    public boolean hasDitchedItem = false;
    public void addItemLight(){
        if (!hasDitchedItem) {
            if (standUser instanceof Player PE) {
                if (canAddItem(getHeldItem(), PE.getInventory()) && standUser.isAlive()) {
                    PE.addItem(getHeldItem());
                } else {
                    ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                            this.getY() + this.getEyeHeight(), this.getZ(),
                            getHeldItem());
                    $$4.setPickUpDelay(40);
                    $$4.setThrower(this.standUser.getUUID());
                    standUser.level().addFreshEntity($$4);
                }
            } else {
                ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                        this.getY() + this.getEyeHeight(), this.getZ(),
                        getHeldItem());
                $$4.setPickUpDelay(40);
                this.level().addFreshEntity($$4);
            }
        }
    }

    public void addItemNotLight(Entity ent){
        if (!hasDitchedItem) {
            if (ent instanceof Player PE) {
                if (canAddItem(getHeldItem(), PE.getInventory()) && PE.isAlive()) {
                    PE.addItem(getHeldItem());
                } else {
                    ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                            this.getY() + this.getEyeHeight(), this.getZ(),
                            getHeldItem());
                    $$4.setPickUpDelay(40);
                    $$4.setThrower(PE.getUUID());
                    PE.level().addFreshEntity($$4);
                }
            } else {
                ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                        this.getY() + this.getEyeHeight(), this.getZ(),
                        getHeldItem());
                $$4.setPickUpDelay(40);
                this.level().addFreshEntity($$4);
            }
        }
    }

    public boolean canAddItem(ItemStack itemStack, Inventory inventory) {
        boolean bl = false;
        for (ItemStack itemStack2 : inventory.items) {
            if (!itemStack2.isEmpty() && (!ItemStack.isSameItemSameTags(itemStack2, itemStack) || itemStack2.getCount() >= itemStack2.getMaxStackSize())) continue;
            bl = true;
            break;
        }
        return bl;
    }
    public void returnToUser(){
        if (this.standUser != null) {
            this.setDeltaMovement(this.getPosition(0).subtract(this.standUser.position()).reverse().normalize().scale(0.4));
        }
    }


    public void startReturning(){
        if (this.getPlunderType() != PlunderTypes.SOUND.id) {
            this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_PLUNDER_EVENT,
                    SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
        }

        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(ModParticles.PLUNDER,
                    this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                    10, 0.2, 0.2, 0.2, 0.015);
        }
        setReturning(true);
        returnToUser();
    }

    public boolean isArrayAdded = false;
    @Override
    public void tick() {

        if (!this.level().isClientSide()){
            lifeSpan--;
            if (lifeSpan <= 0){
                Roundabout.LOGGER.info("1");
                popBubble();
                return;
            }
        }

        if (this.getActivated()){
            if (this.level().isClientSide() || !isArrayAdded) {
                if (this.getPlunderType() == PlunderTypes.FRICTION.id || this.getPlunderType() == PlunderTypes.SOUND.id) {
                    if (getEntityStolen() <= 0) {
                        ((ILevelAccess) this.level()).roundabout$addPlunderBubble(this);
                        isArrayAdded = true;
                    } else {
                        ((ILevelAccess) this.level()).roundabout$addPlunderBubbleEntity(this);
                        isArrayAdded = true;
                    }
                }
            }
        } else if (this.getReturning()){
            returnToUser();
        } else {
            Entity owner = this.getOwner();
            if (getSingular() && this.getOwner() != null && !this.getActivated()) {
                this.shootFromRotationDeltaAgnostic2(owner, owner.getXRot(), owner.getYRot(), 1.0F, getSped());
            }
        }


        AABB BB1 = this.getBoundingBox();

        super.tick();
        if (this.getPlunderType() == PlunderTypes.ITEM.id && !this.getReturning() && !this.getFinished() && !this.isRemoved()){
            AABB BB2 = this.getBoundingBox();
            tryPhaseItemGrab(BB1, BB2);
        }

        if (!this.getReturning() && !this.level().isClientSide()){
            if (this.getPlunderType() == PlunderTypes.OXYGEN.id){
                if (this.standUser != null) {
                    if (this.standUser.isUnderWater() && this.level().getBlockState(this.blockPosition()).isAir()){
                        airSupply = this.standUser.getMaxAirSupply();
                        startReturning();
                    }
                }
            }
        }

        if (this.getFinished()){
            this.discard();
        } else if (this.getReturning() && !this.level().isClientSide()){
            if (this.standUser != null) {
                if (this.distanceTo(standUser) < 1){
                    int maxSupply = this.standUser.getMaxAirSupply();
                    int supply = this.standUser.getAirSupply();
                    if (supply < maxSupply){
                        supply+= airSupply;
                        if (supply > maxSupply){
                            supply = maxSupply;
                        }
                        this.standUser.setAirSupply(supply);

                        this.level().playSound(null, this.blockPosition(), ModSounds.AIR_BUBBLE_EVENT,
                                SoundSource.PLAYERS, 2F, (float) (1.1 + (Math.random() * 0.04)));
                    }
                    Roundabout.LOGGER.info("2");
                    popBubble();
                }
            }
        }
    }

    public void tryPhaseItemGrab(AABB bb1, AABB bb2){
        if (!this.level().isClientSide) {
            bb1 = bb1.inflate(1.6F);
            bb2 = bb2.inflate(1.6F);

            AABB $$2 = bb1.minmax(bb2);
            List<Entity> $$3 = this.level().getEntities(this, $$2);
            if (!$$3.isEmpty()) {
                for (int $$4 = 0; $$4 < $$3.size(); $$4++) {
                    Entity $$5 = $$3.get($$4);
                    if ($$5 instanceof ItemEntity IE) {
                        if (!(IE.getItem().getItem() instanceof BlockItem BI && BI.getBlock() instanceof ShulkerBoxBlock)) {
                            this.setHeldItem(IE.getItem().copyWithCount(1));
                            startReturning();

                            IE.getItem().shrink(1);
                            itemNearby(IE.getId());
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (!this.level().isClientSide()) {
            if (this.getPlunderType() == PlunderTypes.ITEM.id) {
                addItemNotLight($$0.getEntity());
                hasDitchedItem = true;
            } else if (this.getPlunderType() == PlunderTypes.POTION_EFFECTS.id) {
                if ($$0.getEntity() instanceof LivingEntity LE) {
                    if (mobEffects !=null && !mobEffects.isEmpty()) {
                        Collection<MobEffectInstance> mobEffects2 = new ArrayList<>(mobEffects);
                        for (MobEffectInstance value : mobEffects2) {
                            LE.addEffect(value);
                        }
                    }
                }
            }
            return super.hurt($$0, $$1);
        }
        return true;
    }


    public final ItemStack getHeldItem() {
        return this.entityData.get(HELD_ITEM);
    }


    protected static final EntityDataAccessor<ItemStack> HELD_ITEM = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class,
            EntityDataSerializers.ITEM_STACK);
    public final void setHeldItem(ItemStack stack) {
        this.entityData.set(HELD_ITEM, stack);
    }
    public final void itemNearby(int id) {
        if (!this.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.level());
            Vec3 userLocation = new Vec3(this.getX(),  this.getY(), this.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    ModPacketHandler.PACKET_ACCESS.sendIntPacket(serverPlayerEntity, PacketDataIndex.S2C_INT_GRAB_ITEM,id);
                }
            }
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
    public boolean getReturning() {
        return this.getEntityData().get(RETURNING);
    }
    public void setReturning(boolean single) {
        this.getEntityData().set(RETURNING, single);
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
            this.entityData.define(RETURNING, false);
            this.entityData.define(HELD_ITEM, ItemStack.EMPTY);
            this.entityData.define(ENTITY_STOLEN, -1);
        }
    }

    @Override
    public void remove(Entity.RemovalReason $$0) {
        if (!this.getHeldItem().isEmpty() && !this.level().isClientSide()) {
            addItemLight();
        }
        super.remove($$0);
    }
}
