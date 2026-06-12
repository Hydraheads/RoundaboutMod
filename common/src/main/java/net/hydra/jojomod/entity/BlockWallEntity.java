package net.hydra.jojomod.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BlockWallEntity extends Entity {


    private static final Logger LOGGER = LogUtils.getLogger();
    private BlockState blockState;
    public int time;
    public boolean dropItem;
    private boolean cancelDrop;
    private boolean hurtEntities;
    private int fallDamageMax;
    private float fallDamagePerDistance;
    @Nullable
    public CompoundTag blockData;
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;

    public BlockWallEntity(EntityType<? extends BlockWallEntity> $$0, Level $$1) {
        super($$0, $$1);
        this.blockState = Blocks.SAND.defaultBlockState();
        this.dropItem = true;
        this.fallDamageMax = 40;
    }

    public static final float dimensions = 1F;

    public BlockWallEntity(Level $$0, double $$1, double $$2, double $$3, BlockState $$4) {
        this(ModEntities.BLOCK_WALL, $$0);
        this.blockState = $$4;
        this.blocksBuilding = true;
        this.setPos($$1, $$2, $$3);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = $$1;
        this.yo = $$2;
        this.zo = $$3;
        this.setStartPos(this.blockPosition());
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    @Override
    public void push(Entity $$0) {
    }
    public static BlockWallEntity fall(Level $$0, BlockPos $$1, BlockState $$2) {
        BlockWallEntity $$3 = new BlockWallEntity($$0, (double)$$1.getX() + (double)0.5F, (double)$$1.getY(), (double)$$1.getZ() + (double)0.5F, $$2.hasProperty(BlockStateProperties.WATERLOGGED) ? (BlockState)$$2.setValue(BlockStateProperties.WATERLOGGED, false) : $$2);
        $$0.setBlock($$1, $$2.getFluidState().createLegacyBlock(), 3);
        $$0.addFreshEntity($$3);
        return $$3;
    }

    public boolean isAttackable() {
        return false;
    }

    public void setStartPos(BlockPos $$0) {
        this.entityData.set(DATA_START_POS, $$0);
    }

    public BlockPos getStartPos() {
        return (BlockPos)this.entityData.get(DATA_START_POS);
    }

    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    public void tick() {
        super.tick();
    }

    public void callOnBrokenAfterFall(Block $$0, BlockPos $$1) {
    }

    public boolean causeFallDamage(float $$0, float $$1, DamageSource $$2) {
        if (!this.hurtEntities) {
            return false;
        } else {
            int $$3 = Mth.ceil($$0 - 1.0F);
            if ($$3 < 0) {
                return false;
            } else {
                Predicate<Entity> $$4 = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);
                Block var8 = this.blockState.getBlock();
                DamageSource var10000;
                if (var8 instanceof Fallable) {
                    Fallable $$5 = (Fallable)var8;
                    var10000 = $$5.getFallDamageSource(this);
                } else {
                    var10000 = this.damageSources().fallingBlock(this);
                }

                DamageSource $$6 = var10000;
                float $$7 = (float)Math.min(Mth.floor((float)$$3 * this.fallDamagePerDistance), this.fallDamageMax);
                this.level().getEntities(this, this.getBoundingBox(), $$4).forEach(($$2x) -> $$2x.hurt($$6, $$7));
                boolean $$8 = this.blockState.is(BlockTags.ANVIL);
                if ($$8 && $$7 > 0.0F && this.random.nextFloat() < 0.05F + (float)$$3 * 0.05F) {
                    BlockState $$9 = AnvilBlock.damage(this.blockState);
                    if ($$9 == null) {
                        this.cancelDrop = true;
                    } else {
                        this.blockState = $$9;
                    }
                }

                return false;
            }
        }
    }

    protected void addAdditionalSaveData(CompoundTag $$0) {
        $$0.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        $$0.putInt("Time", this.time);
        $$0.putBoolean("DropItem", this.dropItem);
        $$0.putBoolean("HurtEntities", this.hurtEntities);
        $$0.putFloat("FallHurtAmount", this.fallDamagePerDistance);
        $$0.putInt("FallHurtMax", this.fallDamageMax);
        if (this.blockData != null) {
            $$0.put("TileEntityData", this.blockData);
        }

        $$0.putBoolean("CancelDrop", this.cancelDrop);
    }

    protected void readAdditionalSaveData(CompoundTag $$0) {
        this.blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), $$0.getCompound("BlockState"));
        this.time = $$0.getInt("Time");
        if ($$0.contains("HurtEntities", 99)) {
            this.hurtEntities = $$0.getBoolean("HurtEntities");
            this.fallDamagePerDistance = $$0.getFloat("FallHurtAmount");
            this.fallDamageMax = $$0.getInt("FallHurtMax");
        } else if (this.blockState.is(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }

        if ($$0.contains("DropItem", 99)) {
            this.dropItem = $$0.getBoolean("DropItem");
        }

        if ($$0.contains("TileEntityData", 10)) {
            this.blockData = $$0.getCompound("TileEntityData");
        }

        this.cancelDrop = $$0.getBoolean("CancelDrop");
        if (this.blockState.isAir()) {
            this.blockState = Blocks.SAND.defaultBlockState();
        }

    }

    public void setHurtsEntities(float $$0, int $$1) {
        this.hurtEntities = true;
        this.fallDamagePerDistance = $$0;
        this.fallDamageMax = $$1;
    }

    public void disableDrop() {
        this.cancelDrop = true;
    }

    public boolean displayFireAnimation() {
        return false;
    }

    public void fillCrashReportCategory(CrashReportCategory $$0) {
        super.fillCrashReportCategory($$0);
        $$0.setDetail("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    protected Component getTypeName() {
        return Component.translatable("entity.minecraft.falling_block_type", new Object[]{this.blockState.getBlock().getName()});
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(this.getBlockState()));
    }

    public void recreateFromPacket(ClientboundAddEntityPacket $$0) {
        super.recreateFromPacket($$0);
        this.blockState = Block.stateById($$0.getData());
        this.blocksBuilding = true;
        double $$1 = $$0.getX();
        double $$2 = $$0.getY();
        double $$3 = $$0.getZ();
        this.setPos($$1, $$2, $$3);
        this.setStartPos(this.blockPosition());
    }

    static {
        DATA_START_POS = SynchedEntityData.defineId(BlockWallEntity.class, EntityDataSerializers.BLOCK_POS);
    }
}
