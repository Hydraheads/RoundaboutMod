package net.hydra.jojomod.entity;

import com.mojang.logging.LogUtils;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BlockD4CEntity extends Entity {


    private static final Logger LOGGER = LogUtils.getLogger();
    private BlockState blockState;
    public int time;
    public boolean dropItem;
    public boolean tsmove;
    public boolean canGrief = false;
    private boolean cancelDrop;
    private boolean hurtEntities;
    private int fallDamageMax;
    private float fallDamagePerDistance;
    public int timing = -1;
    @Nullable
    public CompoundTag blockData;
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;
    protected static final EntityDataAccessor<Vector3f> DATA_FINAL_POS;

    public BlockD4CEntity(EntityType<? extends BlockD4CEntity> $$0, Level $$1) {
        super($$0, $$1);
        this.blockState = Blocks.SAND.defaultBlockState();
        this.dropItem = true;
        this.fallDamageMax = 40;
        this.blocksBuilding = true;
    }

    public static final float dimensions = 1F;

    public BlockD4CEntity(Level $$0, double $$1, double $$2, double $$3, BlockState $$4) {
        this(ModEntities.D4C_BLOCK, $$0);
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
    public void push(Entity $$0) {
    }

    public boolean isAttackable() {
        return false;
    }

    public void setStartPos(BlockPos $$0) {
        this.entityData.set(DATA_START_POS, $$0);
    }
    public void setDataFinalPos(Vector3f $$0) {
        this.entityData.set(DATA_FINAL_POS, $$0);
    }

    public BlockPos getStartPos() {
        return (BlockPos)this.entityData.get(DATA_START_POS);
    }
    public Vector3f getFinalPos() {
        return (Vector3f) this.entityData.get(DATA_FINAL_POS);
    }

    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
        this.entityData.define(DATA_FINAL_POS, new Vector3f(0,0,0));
    }

    public boolean isPickable() {
        return false;
    }


    public void breakAndDiscard(){
        level().levelEvent(
                2001,
                getOnPos().above(),
                Block.getId(blockState)
        );
        discard();
    }

    public boolean isWhiteAlbumWall = false;

    @Override
    public boolean hurt(DamageSource $$0, float damage) {

        return super.hurt($$0,damage);
    }

    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    public boolean updated = false;
    @Override
    public void lerpTo(double $$0, double $$1, double $$2, float $$3, float $$4, int $$5, boolean $$6) {
        this.lerpX = $$0;
        this.lerpY = $$1;
        this.lerpZ = $$2;
        this.setRot($$3, $$4);
        this.lerpSteps = $$5;
        updated = true;
    }
    Vec3 finPos = Vec3.ZERO;
    public int existTime = 0;
    @Override
    public void tick() {
        existTime++;

        if (!level().isClientSide()) {
            if (level().getBlockState(getStartPos()).is(getBlockState().getBlock())){
                if (existTime > 5) {
                    Vec3 start = this.position();
                    Vec3 target = getStartPos().getCenter().subtract(0, 0.5, 0);

                    Vec3 direction = target.subtract(start).normalize();

                    double speed = 0.7D;

                    MainUtil.sendParticlesIfPossible(
                            this,
                            level(),
                            ModParticles.MENGER,
                            this.position().x,
                            this.position().y + 0.5,
                            this.position().z,
                            0,
                            (float) (direction.x * speed),
                            (float) (direction.y * speed),
                            (float) (direction.z * speed),
                            speed
                    );
                }
                if (existTime > 15) {

                    Vec3 currentPos = getPosition(1);
                    Vec3 finPos = getStartPos().getCenter().subtract(0,0.5,0);

                    Vec3 difference = finPos.subtract(currentPos);
                    double distance = difference.length();

                    // Speed per tick
                    double spd = 1D;

                    if (distance <= spd) {
                        // We reached the target
                        setPos(finPos.x, finPos.y, finPos.z);

                        // Explode
                        level().explode(
                                this,
                                getX(),
                                getY(),
                                getZ(),
                                2.0F,
                                Level.ExplosionInteraction.NONE
                        );

                        if (canGrief){
                            level().removeBlock(getStartPos(),true);
                        }

                        discard();
                    } else {
                        // Move toward target at a constant speed
                        Vec3 movement = difference.normalize().scale(spd);

                        setPos(
                                getX() + movement.x,
                                getY() + movement.y,
                                getZ() + movement.z
                        );
                    }
                } else {

                }
            } else {
                discard();
            }
        } else {
            if (this.lerpSteps > 0) {
                double $$0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
                double $$1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
                double $$2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
                --this.lerpSteps;
                this.setPos($$0, $$1, $$2);
            } else {
                if (updated) {
                    this.setPos(lerpX, lerpY, lerpZ);
                }
            }
        }

        super.tick();
    }




    protected void addAdditionalSaveData(CompoundTag $$0) {
        $$0.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        $$0.putInt("Time", this.time);
        $$0.putInt("DeathTimer", this.timing);
        $$0.putBoolean("WhiteAlbum", this.isWhiteAlbumWall);
        $$0.putBoolean("DropItem", this.dropItem);
        $$0.putBoolean("HurtEntities", this.hurtEntities);
        $$0.putBoolean("CanGrief", this.canGrief);
        $$0.putFloat("FallHurtAmount", this.fallDamagePerDistance);
        $$0.putInt("FallHurtMax", this.fallDamageMax);
        $$0.putFloat("FinalPosX", getFinalPos().x());
        $$0.putFloat("FinalPosY", getFinalPos().y());
        $$0.putFloat("FinalPosZ", getFinalPos().z());
        if (this.blockData != null) {
            $$0.put("TileEntityData", this.blockData);
        }

        $$0.putBoolean("CancelDrop", this.cancelDrop);
    }

    protected void readAdditionalSaveData(CompoundTag $$0) {
        this.blockState = NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), $$0.getCompound("BlockState"));
        this.time = $$0.getInt("Time");
        this.timing = $$0.getInt("DeathTimer");
        this.isWhiteAlbumWall = $$0.getBoolean("WhiteAlbum");
        this.canGrief = $$0.getBoolean("CanGrief");
        if ($$0.contains("HurtEntities", 99)) {
            this.hurtEntities = $$0.getBoolean("HurtEntities");
            this.fallDamagePerDistance = $$0.getFloat("FallHurtAmount");
            this.fallDamageMax = $$0.getInt("FallHurtMax");
        } else if (this.blockState.is(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }


        if ($$0.contains("FinalPosX")) {
            setDataFinalPos(new Vector3f($$0.getFloat("FinalPosX"),
                    $$0.getFloat("FinalPosY"),$$0.getFloat("FinalPosZ")) );

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
        DATA_START_POS = SynchedEntityData.defineId(BlockD4CEntity.class, EntityDataSerializers.BLOCK_POS);
        DATA_FINAL_POS = SynchedEntityData.defineId(BlockD4CEntity.class, EntityDataSerializers.VECTOR3);
    }

    public boolean fireImmune() {
        return true;
    }
    public boolean isOnFire() {
        return false;
    }

}
