package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.FireProjectile;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.StoredSoundInstance;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PlunderTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SoftAndWetPlunderBubbleEntity extends SoftAndWetBubbleEntity {
    private static final EntityDataAccessor<Byte> PLUNDER_TYPE = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<BlockPos> BLOCK_POS = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> FINISHED = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ENTITY_STOLEN = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LIQUID_STOLEN = SynchedEntityData.defineId(SoftAndWetPlunderBubbleEntity.class, EntityDataSerializers.INT);
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
        if (isPopPlunderBubbble()) {
            return false;
        }


        if (this.getPlunderType() == PlunderTypes.POTION_EFFECTS.id && this.getActivated()){
            return true;
        }

        if (this.level().isClientSide() && ClientUtil.getPlayer() != null && ClientUtil.getPlayer().getId() == getUserID()){
            return false;
        }
        return true;
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

    public boolean stolenPhysicalLiquid = false;
    public boolean finishedUsingLiquid = false;
    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        if (!this.level().isClientSide()) {
            if (!getFinished() && !getReturning()) {
                if ((this.getPlunderType() == PlunderTypes.FRICTION.id || this.getPlunderType() == PlunderTypes.SOUND.id) && !this.getActivated()) {
                    this.setBlockPos($$0.getBlockPos().above());
                    this.setBlockPos($$0.getBlockPos());
                    if ($$0.getDirection() == Direction.DOWN) {
                        setFloatingUpsideDown();
                    } else {
                        setFloating();
                    }
                } else if (this.getPlunderType() == PlunderTypes.MOISTURE.id) {
                    if (!this.getActivated()) {
                        if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof GasolineBlock) {
                            if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                    ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                                this.level().setBlock($$0.getBlockPos(), Blocks.AIR.defaultBlockState(), 11);
                                stolenPhysicalLiquid = true;
                            }
                            this.setLiquidStolen(1);

                            if ($$0.getDirection() == Direction.DOWN) {
                                setFloatingUpsideDown();
                                stolenPhysicalLiquid = true;
                            } else {
                                setFloating();
                            }
                        } else if (this.level().getBlockState($$0.getBlockPos().above()).getBlock() instanceof GasolineBlock) {
                            if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                    ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                                this.level().setBlock($$0.getBlockPos().above(), Blocks.AIR.defaultBlockState(), 11);
                            }
                            this.setLiquidStolen(1);
                            setFloating();
                        } else if (this.level().getBlockState($$0.getBlockPos()).hasProperty(BlockStateProperties.WATERLOGGED) &&
                                this.level().getBlockState($$0.getBlockPos()).getValue(BlockStateProperties.WATERLOGGED)) {
                            if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                    ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                                this.level().setBlockAndUpdate($$0.getBlockPos(), this.level().getBlockState($$0.getBlockPos()).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)));

                                if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                        ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                                    this.level().setBlock(this.blockPosition(), Blocks.AIR.defaultBlockState(), 11);
                                    stolenPhysicalLiquid = true;
                                }
                                this.setLiquidStolen(2);
                                setFloating();
                            }
                        } else if (this.level().getBlockState($$0.getBlockPos().relative($$0.getDirection())).hasProperty(BlockStateProperties.WATERLOGGED) &&
                                this.level().getBlockState($$0.getBlockPos().relative($$0.getDirection())).getValue(BlockStateProperties.WATERLOGGED)) {
                            if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                    ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                                this.level().setBlockAndUpdate($$0.getBlockPos().relative($$0.getDirection()), this.level().getBlockState($$0.getBlockPos().relative($$0.getDirection())).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)));

                                if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                        ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                                    this.level().setBlock(this.blockPosition(), Blocks.AIR.defaultBlockState(), 11);
                                    stolenPhysicalLiquid = true;
                                }

                                this.setLiquidStolen(2);
                                setFloating();
                            }
                        } else {
                            super.onHitBlock($$0);
                        }
                    } else {
                        if (stolenPhysicalLiquid){
                            if (getLiquidStolen() == 1){
                                if (stolenPhysicalLiquid) {
                                    BlockPos bpos = $$0.getBlockPos().relative($$0.getDirection());
                                    Block bk = this.level().getBlockState(bpos).getBlock();
                                    if (bk instanceof FireBlock || bk instanceof StandFireBlock || bk instanceof CampfireBlock){
                                        gasExplode();
                                        popBubble();
                                    } else {
                                        if (MainUtil.tryPlaceBlock(this.standUser, bpos)) {
                                            this.level().setBlockAndUpdate(bpos, ModBlocks.GASOLINE_SPLATTER.defaultBlockState().setValue(ModBlocks.GAS_CAN_LEVEL, Integer.valueOf(2)));
                                            finishedUsingLiquid = true;
                                        } else {
                                            spawnGasSplatter();
                                            finishedUsingLiquid = true;
                                        }
                                    }
                                }
                            } else if (getLiquidStolen() == 2) {
                                BlockPos bpos1 = $$0.getBlockPos();
                                if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser)) {
                                    if (this.level().getBlockState(bpos1).hasProperty(BlockStateProperties.WATERLOGGED) &&
                                    !this.level().getBlockState(bpos1).getValue(BlockStateProperties.WATERLOGGED)){
                                        this.level().setBlockAndUpdate(bpos1, this.level().getBlockState(bpos1).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)));
                                        finishedUsingLiquid = true;
                                    }
                                }

                                BlockPos bpos = $$0.getBlockPos().relative($$0.getDirection());
                                if (!finishedUsingLiquid) {
                                    if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser)) {
                                        if (this.level().getBlockState(bpos).hasProperty(BlockStateProperties.WATERLOGGED) &&
                                                !this.level().getBlockState(bpos).getValue(BlockStateProperties.WATERLOGGED)) {
                                            this.level().setBlockAndUpdate(bpos, this.level().getBlockState(bpos).setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)));
                                            finishedUsingLiquid = true;
                                        }
                                    }
                                }

                                if (!finishedUsingLiquid) {
                                    if (MainUtil.tryPlaceBlock(this.standUser, bpos, true)) {
                                        this.level().setBlockAndUpdate(bpos, Blocks.WATER.defaultBlockState());
                                    } else {
                                        splashWater();
                                    }
                                    finishedUsingLiquid = true;
                                }
                            } else if (getLiquidStolen() == 3) {
                                BlockPos bpos = $$0.getBlockPos().relative($$0.getDirection());
                                if (MainUtil.tryPlaceBlock(this.standUser, bpos, true)) {
                                    this.level().setBlockAndUpdate(bpos, Blocks.LAVA.defaultBlockState());
                                } else {
                                    splashLava();
                                }
                                finishedUsingLiquid = true;
                            }
                        }
                        super.onHitBlock($$0);
                    }
                } else if (this.getPlunderType() == PlunderTypes.OXYGEN.id) {
                    if (getLaunched() && getActivated()){
                        BlockPos bpos = $$0.getBlockPos().relative($$0.getDirection());
                        BlockPos bpos2 = $$0.getBlockPos();
                        if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof GasolineBlock) {
                            gasExplode();
                        } else if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof CampfireBlock && MainUtil.getIsGamemodeApproriateForGrief(standUser)
                                && this.level().getBlockState($$0.getBlockPos()).hasProperty(BlockStateProperties.LIT) && !this.level().getBlockState($$0.getBlockPos()).getValue(BlockStateProperties.LIT)) {
                            this.level().setBlockAndUpdate(bpos2, this.level().getBlockState($$0.getBlockPos()).setValue(BlockStateProperties.LIT, Boolean.valueOf(true)));
                        } else if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof CandleBlock && MainUtil.getIsGamemodeApproriateForGrief(standUser)
                                && this.level().getBlockState($$0.getBlockPos()).hasProperty(BlockStateProperties.LIT) && !this.level().getBlockState($$0.getBlockPos()).getValue(BlockStateProperties.LIT)) {
                            this.level().setBlockAndUpdate(bpos2, this.level().getBlockState($$0.getBlockPos()).setValue(BlockStateProperties.LIT, Boolean.valueOf(true)));
                        } else {
                            if (MainUtil.tryPlaceBlock(this.standUser, bpos) && MainUtil.getIsGamemodeApproriateForGrief(standUser)) {
                                if (this.level().getBlockState(bpos.below()).isAir()) {
                                    if ($$0.getDirection().equals(Direction.NORTH)) {
                                        this.level().setBlockAndUpdate(bpos, Blocks.FIRE.defaultBlockState().setValue(PipeBlock.SOUTH, true));
                                    } else if ($$0.getDirection().equals(Direction.SOUTH)) {
                                        this.level().setBlockAndUpdate(bpos, Blocks.FIRE.defaultBlockState().setValue(PipeBlock.NORTH, true));
                                    } else if ($$0.getDirection().equals(Direction.EAST)) {
                                        this.level().setBlockAndUpdate(bpos, Blocks.FIRE.defaultBlockState().setValue(PipeBlock.WEST, true));
                                    } else if ($$0.getDirection().equals(Direction.WEST)) {
                                        this.level().setBlockAndUpdate(bpos, Blocks.FIRE.defaultBlockState().setValue(PipeBlock.EAST, true));
                                    } else {
                                        this.level().setBlockAndUpdate(bpos, Blocks.FIRE.defaultBlockState());
                                    }
                                } else {
                                    this.level().setBlockAndUpdate(bpos, Blocks.FIRE.defaultBlockState());
                                }
                            }
                        }
                        super.onHitBlock($$0);
                    } else {
                        if (this.standUser != null) {
                            if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof MagmaBlock) {
                                airSupply = this.standUser.getMaxAirSupply();
                                startReturning();
                            } else if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof CampfireBlock && MainUtil.getIsGamemodeApproriateForGrief(standUser)
                            && this.level().getBlockState($$0.getBlockPos()).hasProperty(BlockStateProperties.LIT) && this.level().getBlockState($$0.getBlockPos()).getValue(BlockStateProperties.LIT)) {
                                fireTicks = 100;
                                this.level().setBlockAndUpdate($$0.getBlockPos(), this.level().getBlockState($$0.getBlockPos()).setValue(BlockStateProperties.LIT, Boolean.valueOf(false)));
                                setFloating();
                            } else if (this.level().getBlockState($$0.getBlockPos()).getBlock() instanceof CandleBlock && MainUtil.getIsGamemodeApproriateForGrief(standUser)
                            && this.level().getBlockState($$0.getBlockPos()).hasProperty(BlockStateProperties.LIT) && this.level().getBlockState($$0.getBlockPos()).getValue(BlockStateProperties.LIT)){
                                fireTicks = 100;
                                this.level().setBlockAndUpdate($$0.getBlockPos(),  this.level().getBlockState($$0.getBlockPos()).setValue(BlockStateProperties.LIT,Boolean.valueOf(false)));
                                setFloating();
                            } else if (this.level().getBlockState($$0.getBlockPos().relative($$0.getDirection())).getBlock() instanceof BaseFireBlock && MainUtil.getIsGamemodeApproriateForGrief(standUser)){
                                fireTicks = 100;
                                this.level().setBlockAndUpdate($$0.getBlockPos().above(), Blocks.AIR.defaultBlockState());
                                setFloating();
                            } else {
                                super.onHitBlock($$0);
                            }
                        } else {
                            super.onHitBlock($$0);
                        }
                    }
                } else {
                    super.onHitBlock($$0);
                }
            }
        }
    }

    public void rangedSplash(){
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.GASOLINE_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);
            SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
            this.playSound($$6, 1F, 1.5F);
            List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox(this.level(), this.getX(), this.getY(),
                    this.getZ(), 2, 2, 2));
            if (!entities.isEmpty()) {
                for (Entity value : entities) {
                    if (value instanceof LivingEntity) {
                        ((StandUser) value).roundabout$setGasolineTime(((StandUser) value).roundabout$getMaxBucketGasolineTime());
                    }
                }
            }
        }
    }

    public void setFloating2(){
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
        if (this.getPlunderType() == PlunderTypes.SIGHT.id) {
            if (this.getEntityStolen() > 0 && this.level().getEntity(this.getEntityStolen()) instanceof Player PL){
                lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.sightStealingDurationOnPlayersInTicks;
            } else {
                lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.sightStealingDurationOnMobsInTicks;
            }
            theatricPop();
        } else if (this.getPlunderType() == PlunderTypes.FRICTION.id) {
            lifeSpan = ClientNetworking.getAppropriateConfig().softAndWetSettings.frictionStealingDurationInTicks;
            theatricPop();
        }
    }
    public void setFloating(){
        setLaunched(false);
        setFloating2();
        this.setDeltaMovement(0, 0.01, 0);
    }

    public void setFloatingUpsideDown(){
        setFloating2();
        this.setDeltaMovement(0, -0.01, 0);
    }

    public void gasExplode(){
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.FLAME, this.getX(), this.getY() + this.getEyeHeight(), this.getZ(),
                    40, 0.0, 0.2, 0.0, 0.2);
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY() + this.getEyeHeight(), this.getZ(),
                    1, 0.5, 0.5, 0.5, 0.2);
            MainUtil.gasExplode(null, (ServerLevel) this.level(), this.getOnPos(), 0, 2, 4, MainUtil.gasDamageMultiplier() * 16);
        }
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


    public void theatricPop(){

        ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                this.getX(), this.getY() + this.getBbHeight() * 0.6, this.getZ(),
                1, 0, 0, 0, 0.015);
        this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
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
            if (!isPopPlunderBubbble()) {
                theatricPop();
                popSounds();
            }


            if (this.getPlunderType() == PlunderTypes.MOISTURE.id){
                if (!finishedUsingLiquid){
                    finishedUsingLiquid = true;
                    if (stolenPhysicalLiquid){
                        if (getLiquidStolen() == 1) {
                           spawnGasSplatter();
                        } else if (getLiquidStolen() == 2) {

                            if (MainUtil.tryPlaceBlock(this.standUser, this.blockPosition(),true)) {
                                this.level().setBlockAndUpdate(this.blockPosition(), Blocks.WATER.defaultBlockState());
                            } else {
                                splashWater();
                            }
                        } else if (getLiquidStolen() == 3) {
                            if (MainUtil.tryPlaceBlock(this.standUser, this.blockPosition(),true)) {
                                this.level().setBlockAndUpdate(this.blockPosition(), Blocks.LAVA.defaultBlockState());
                            } else {
                                splashLava();
                            }
                        }
                    } else {
                        if (getLiquidStolen() == 1) {
                            rangedSplash();
                        } else if (getLiquidStolen() == 2) {
                            splashWater();
                        } else if (getLiquidStolen() == 3) {
                            splashLava();
                        }
                    }
                }
            }
            this.discard();
        }


    }

    public void splashWater(){

        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WATER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);
            SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
            this.playSound($$6, 1F, 1.5F);
            List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox(this.level(), this.getX(), this.getY(),
                    this.getZ(), 3, 3, 3));
            if (!entities.isEmpty()) {
                for (Entity value : entities) {
                    if (value instanceof LivingEntity) {
                        if (((StandUser) value).roundabout$isOnStandFire()){
                            ((StandUser) value).roundabout$setRemainingStandFireTicks(0);
                        }
                    }

                    if (value.isOnFire()){
                        value.clearFire();
                    }
                }
            }
        }
    }

    public void splashLava(){
        if (!this.level().isClientSide()) {
            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAVA.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);
            SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
            this.playSound($$6, 1F, 1.5F);
            List<Entity> entities = MainUtil.hitbox(MainUtil.genHitbox(this.level(), this.getX(), this.getY(),
                    this.getZ(), 3, 3, 3));
            if (!entities.isEmpty()) {
                for (Entity value : entities) {
                        value.setRemainingFireTicks(300+value.getRemainingFireTicks());
                }
            }
        }
    }

    public void spawnGasSplatter(){
        if (this.standUser != null) {
            GasolineSplatterEntity splat = new GasolineSplatterEntity(this.standUser, this.standUser.level());
            splat.absMoveTo(this.getX(), this.getY(), this.getZ());
            standUser.level().addFreshEntity(splat);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("roundabout.HeldItem",this.getHeldItem().save(compoundtag));
        $$0.putBoolean("roundabout.ditchedItem",hasDitchedItem);
        $$0.putFloat("roundabout.speed",getSped());
        $$0.putBoolean("roundabout.stolenLiquid",stolenPhysicalLiquid);
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        CompoundTag compoundtag = $$0.getCompound("roundabout.HeldItem");
        ItemStack itemstack = ItemStack.of(compoundtag);
        hasDitchedItem = $$0.getBoolean("roundabout.ditchedItem");
        setSped($$0.getFloat("roundabout.speed"));
        stolenPhysicalLiquid = $$0.getBoolean("roundabout.stolenPhysicalLiquid");
        this.setHeldItem(itemstack);
        super.readAdditionalSaveData($$0);
    }

    public boolean isPopPlunderBubbble(){
        return (getPlunderType() == PlunderTypes.FRICTION.id || getPlunderType() == PlunderTypes.SIGHT.id) && getActivated();
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.5F;
    }
    public int airSupply = 0;
    Collection<MobEffectInstance> mobEffects;
    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!this.level().isClientSide()) {
            if (!($$0.getEntity() instanceof SoftAndWetBubbleEntity)) {
                if (!getActivated() && !getFinished() && !($$0.getEntity().getId() == getUserID())
                        && !getReturning()) {
                    if (this.getPlunderType() == PlunderTypes.SOUND.id) {
                        if (!((ILevelAccess) this.level()).roundabout$isSoundPlunderedEntity($$0.getEntity())) {
                            this.setEntityStolen($$0.getEntity().getId());
                            setFloating();
                        } else {
                            super.onHitEntity($$0);
                        }
                    } else if (this.getPlunderType() == PlunderTypes.MOBS.id) {
                        if ($$0.getEntity() instanceof LivingEntity LE &&
                                MainUtil.canBeScoopedUpInBubble(LE)) {
                            this.setEntityStolen($$0.getEntity().getId());
                            $$0.getEntity().ejectPassengers();
                            if ($$0.getEntity().getVehicle() != null){
                                $$0.getEntity().getVehicle().ejectPassengers();
                            }
                            $$0.getEntity().startRiding(this);

                            if ($$0.getEntity().getVehicle() != null && $$0.getEntity().getVehicle().is(this)){
                                setFloating();
                            } else {
                                super.onHitEntity($$0);
                            }
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
                                    if (!MainUtil.isSpecialEffect(value) && !value.isInfiniteDuration()) {
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
                        if (getLaunched() && getActivated()) {
                            $$0.getEntity().setRemainingFireTicks($$0.getEntity().getRemainingFireTicks()+fireTicks);
                            super.onHitEntity($$0);
                        } else if ($$0.getEntity().isOnFire()) {
                            fireTicks = $$0.getEntity().getRemainingFireTicks();
                            $$0.getEntity().clearFire();
                            setFloating();
                        } else if ($$0.getEntity() instanceof LivingEntity LE && !LE.canBreatheUnderwater()) {
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
                } else {
                    if (getActivated() && getLaunched() && !getFinished()) {
                        if (this.getPlunderType() == PlunderTypes.POTION_EFFECTS.id) {
                            if ($$0.getEntity() instanceof LivingEntity LE) {
                                if (mobEffects != null && !mobEffects.isEmpty()) {
                                    Collection<MobEffectInstance> mobEffects2 = new ArrayList<>(mobEffects);
                                    for (MobEffectInstance value : mobEffects2) {
                                        LE.addEffect(value);
                                    }
                                }
                                super.onHitEntity($$0);
                            }
                        } else if (this.getPlunderType() == PlunderTypes.OXYGEN.id && !($$0.getEntity().getId() == getUserID())) {
                            $$0.getEntity().setRemainingFireTicks($$0.getEntity().getRemainingFireTicks()+fireTicks);
                            super.onHitEntity($$0);
                        } else if (this.getPlunderType() == PlunderTypes.MOISTURE.id && !($$0.getEntity().getId() == getUserID())) {
                            if (getLiquidStolen() == 1) {
                                splashGas($$0.getEntity());
                                finishedUsingLiquid = true;
                            }
                            super.onHitEntity($$0);
                        }
                    }
                }
            }
        }
    }

    public int fireTicks;

    public void splashGas(Entity ent){
        ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.GASOLINE_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                15, 0.4, 0.4, 0.25, 0.4);
        SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
        this.playSound($$6, 1F, 1.5F);
        if (ent instanceof LivingEntity) {
            ((StandUser) ent).roundabout$setGasolineTime(((StandUser) ent).roundabout$getMaxBucketGasolineTime());
        }
    }

    protected float getEyeHeight(Pose $$0, EntityDimensions $$1) {
        return $$1.height * 0.1F;
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

    public void forceDropItem(){
        if (!hasDitchedItem) {
            ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                    this.getY() + this.getEyeHeight(), this.getZ(),
                    getHeldItem());
            $$4.setPickUpDelay(40);
            $$4.setThrower(this.getUUID());
            this.level().addFreshEntity($$4);
            hasDitchedItem = true;
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
    public LivingEntity getStandUser(){
        if (standUser != null){
            return standUser;
        } else if (standUserUUID != null && !this.level().isClientSide()){
            Entity ett = ((ServerLevel)this.level()).getEntity(standUserUUID);
            if (ett instanceof LivingEntity lett){
                standUser = lett;
                this.setUserID(lett.getId());
            }
        } else if (this.level().getEntity(this.getUserID()) instanceof LivingEntity LE){
            standUser = LE;
        }
        return standUser;
    }

    public void popIfSuffocating(){
        if (!this.level().isClientSide()) {
            if (this.getPlunderType() == PlunderTypes.MOBS.id) {
                if (getActivated()) {
                    Entity ent = this.level().getEntity(getEntityStolen());
                    if (ent != null) {
                        if (ent.isInWall() || !(ent.getVehicle() instanceof SoftAndWetPlunderBubbleEntity sbe && sbe.is(this))) {
                            this.ejectPassengers();
                            popBubble();
                        }
                    } else {
                        this.ejectPassengers();
                        popBubble();
                    }
                }
            }
        }
    }


    @Override
    public void tick() {

        if (!this.level().isClientSide()){
            lifeSpan--;
            if (lifeSpan <= 0 || (this.standUser == null || !(((StandUser)this.standUser).roundabout$getStandPowers() instanceof PowersSoftAndWet))){
                popBubble();
                return;
            }
        }


        LivingEntity usr = this.getStandUser();
        if (usr != null && ((StandUser)usr).roundabout$getStandPowers() instanceof PowersSoftAndWet PW){
            if (PW.bubbleList != null && !PW.bubbleList.contains(this)){
                PW.bubbleList.add(this);
            }
        } else {
            if (!this.level().isClientSide()) {
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
            if (getSingular()) {
                if (owner != null && !this.getActivated()) {
                    this.shootFromRotationDeltaAgnostic2(owner, owner.getXRot(), owner.getYRot(), 1.0F, getSped());
                }
            } else {
                if (this.tickCount < 20 && !this.getLaunched()) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.86));
                }
            }
        }


        /**Because the bubble straight up ignores  collision half the time, I need an additional chedk to attempt to collide*/
        Vec3 currentPos = this.position();
        Vec3 nextPos = currentPos.add(this.getDeltaMovement());

// Make the projectile's path a swept AABB with thickness
        // Enlarge sweep area to cover fast motion & small entities
        AABB sweptBox = this.getBoundingBox()
                .expandTowards(this.getDeltaMovement())
                .inflate(this.getBbWidth() * 1 + 0.1); // Adjust as needed

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                this.level(), this, currentPos, nextPos, sweptBox,
                this::canHitEntity
        );

        if (entityHitResult != null) {
            this.onHitEntity(entityHitResult);
        }

        AABB box = this.getBoundingBox().inflate(0.2); // Slight growth
        for (Entity e : level().getEntities(this, box, this::canHitEntity)) {
            this.onHitEntity(new EntityHitResult(e));
        }
        stealLiquidsAndFlames();

        if (getActivated() && !getReturning() && !getFinished() && !getLaunched()){
            this.setDeltaMovement(0, 0.01, 0);
        }

        AABB BB1 = this.getBoundingBox();
        popIfSuffocating();
        super.tick();
        popIfSuffocating();
        if (this.getPlunderType() == PlunderTypes.ITEM.id && !this.getReturning() && !this.getFinished() && !this.isRemoved()){
            AABB BB2 = this.getBoundingBox();
            tryPhaseItemGrab(BB1, BB2);
        }

        stealLiquidsAndFlames();

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
            if (!this.level().isClientSide()) {
                this.discard();
            }
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
                    popBubble();
                }
            }
        }
    }
    @Override
    public boolean fireImmune() {
        return true;
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

    public void stealLiquidsAndFlames(){
        if (!this.level().isClientSide() && !isRemoved()) {
            if (this.getPlunderType() == PlunderTypes.MOISTURE.id) {
                BlockState bs = this.level().getBlockState(this.blockPosition());
                if (!getActivated()){
                    if (bs.is(Blocks.WATER) && bs.getValue(BlockStateProperties.LEVEL) == 0){
                        if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                            this.level().setBlock(this.blockPosition(), Blocks.AIR.defaultBlockState(), 11);
                            stolenPhysicalLiquid = true;
                        }
                        this.setLiquidStolen(2);
                        setFloating();
                    } else if (bs.is(Blocks.LAVA) && bs.getValue(BlockStateProperties.LEVEL) == 0){
                        if (MainUtil.getIsGamemodeApproriateForGrief(this.standUser) &&
                                ClientNetworking.getAppropriateConfig().softAndWetSettings.moistureWithStandGriefingTakesLiquidBlocks) {
                            this.level().setBlock(this.blockPosition(), Blocks.AIR.defaultBlockState(), 11);
                            stolenPhysicalLiquid = true;
                        }
                        this.setLiquidStolen(3);
                        setFloating();
                    }
                } else {
                    /**Also explode gasoline bubbles touching fire*/
                    if (getLiquidStolen() == 1){
                        if (bs.getBlock() instanceof FireBlock || bs.getBlock() instanceof StandFireBlock){
                            gasExplode();
                            popBubble();
                        }
                    }
                }
            } else if (this.getPlunderType() == PlunderTypes.OXYGEN.id) {
                BlockState bs = this.level().getBlockState(this.blockPosition());
                if (!getActivated()) {
                    if (bs.getBlock() instanceof BaseFireBlock && MainUtil.getIsGamemodeApproriateForGrief(standUser)) {
                        fireTicks = 100;
                            this.level().setBlockAndUpdate(this.blockPosition(), Blocks.AIR.defaultBlockState());
                        setFloating();
                    }
                }
            }
        }
    }





    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (isPopPlunderBubbble()){
            return false;
        }
        if (!this.level().isClientSide()) {
            if (this.getPlunderType() == PlunderTypes.ITEM.id) {
                addItemNotLight($$0.getEntity());
                hasDitchedItem = true;
            } else if (this.getPlunderType() == PlunderTypes.MOISTURE.id){
                if (getLiquidStolen() == 1) {
                    if (stolenPhysicalLiquid) {
                        if ($$0.getDirectEntity() != null && $$0.getEntity() != null) {
                            if ($$0.getDirectEntity().isOnFire() || $$0.getDirectEntity() instanceof FireProjectile) {
                                gasExplode();
                                finishedUsingLiquid = true;
                            } else {
                                ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.GASOLINE_SPLATTER.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                                        15, 0.4, 0.4, 0.25, 0.4);
                                SoundEvent $$6 = SoundEvents.GENERIC_SPLASH;
                                this.playSound($$6, 1F, 1.5F);
                                if ($$0.getEntity() instanceof LivingEntity && $$0.getDirectEntity().is($$0.getEntity())) {
                                    ((StandUser) $$0.getEntity()).roundabout$setGasolineTime(((StandUser) $$0.getEntity()).roundabout$getMaxBucketGasolineTime());
                                }
                            }
                        }
                    }
                }
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
    public int getLiquidStolen() {
        return this.getEntityData().get(LIQUID_STOLEN);
    }
    public void setLiquidStolen(int entid) {
        this.getEntityData().set(LIQUID_STOLEN, entid);
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
            this.entityData.define(LIQUID_STOLEN, -1);
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
