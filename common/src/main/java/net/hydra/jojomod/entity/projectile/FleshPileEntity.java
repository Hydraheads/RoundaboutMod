package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.FleshBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.nio.ReadOnlyBufferException;
import java.util.Arrays;


public class FleshPileEntity extends ThrowableItemProjectile {
    public FleshPileEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected static final EntityDataAccessor<Integer> FLESH_COUNT = SynchedEntityData.defineId(FleshPileEntity.class,
            EntityDataSerializers.INT);
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(FLESH_COUNT)) {
            super.defineSynchedData();
            this.entityData.define(FLESH_COUNT, 0);
        }
    }
    public void setFleshCount(int amount) {this.entityData.set(FLESH_COUNT,amount);}
    public int getFleshCount() {return this.entityData.get(FLESH_COUNT);}


    public FleshPileEntity(LivingEntity living, Level $$1,int amount) {
        super(ModEntities.FLESH_PILE, living, $$1);
        setFleshCount(amount);
    }

    public FleshPileEntity(Level level, double d0, double d1, double d2, int amount) {
        super(ModEntities.FLESH_PILE, d0, d1,d2,level);
        setFleshCount(amount);
    }


    @Override
    protected Item getDefaultItem() {
        return ModBlocks.FLESH_BLOCK.asItem();
    }


    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!this.level().isClientSide) {

            ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.FLESH_BLOCK.defaultBlockState()), this.getOnPos().getX() + 0.5, this.getOnPos().getY() + 0.5, this.getOnPos().getZ() + 0.5,
                    15, 0.4, 0.4, 0.25, 0.4);
            this.playSound(SoundEvents.GENERIC_SPLASH, 1F, 1.5F);

            placeFlesh($$0.getBlockPos(),(getFleshCount() != 0 ? getFleshCount() : 4));


            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        Entity entity = $$0.getEntity();
        if (entity instanceof LivingEntity LE) {
            LE.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,this.getFleshCount() > 10 ? 70 : 50,1));
        }
    }

    public boolean[] isValidLocation(BlockPos pos, int offsetX, int offsetY, int offsetZ, int level){
        BlockPos blk =  new BlockPos(pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);

        BlockState check = this.level().getBlockState(blk);

        boolean[] info = {false,check.is(ModBlocks.FLESH_BLOCK) &&
                check.getValue(FleshBlock.LAYERS)+level <= 4 };

        if (this.level().isEmptyBlock(blk)) {
            BlockPos $$8 = blk.below();
            BlockState b = this.level().getBlockState($$8);
            info[0] = b.isFaceSturdy(this.level(), $$8, Direction.UP)
                    || (b.is(ModBlocks.FLESH_BLOCK) && b.getValue(FleshBlock.LAYERS) == 4);
        }
        return info;
    }

    public boolean[] checkHeights(BlockPos pos, int offsetX, int offsetZ, int level) {
        boolean[] info = {false,false};
        for (int i=-1;i<3;i++) {
            boolean[] result = isValidLocation(pos,offsetX,i,offsetZ,level);
            if (result[0] || result[1] ) {
                info = result;
            }
        }
        return info;
    }

    public void setGoo(BlockPos pos, int offsetX, int offsetZ, int level){
        BlockPos blockPos = null;
        boolean replace = false;
        for (int i=-1;i<3;i++) {
            boolean[] result = isValidLocation(pos,offsetX,i,offsetZ,level);
            if (result[0] || result[1] ) {
                replace = result[1];
                blockPos = new BlockPos(
                        pos.getX()+offsetX,
                        pos.getY()+i,
                        pos.getZ()+offsetZ
                );
            }
        }
        if (blockPos != null) {
            BlockState n = ModBlocks.FLESH_BLOCK.defaultBlockState().setValue(ModBlocks.FLESH_LAYER, level);
            if (replace) {
                n = ModBlocks.FLESH_BLOCK.defaultBlockState().setValue(ModBlocks.FLESH_LAYER, level + this.level().getBlockState(blockPos).getValue(FleshBlock.LAYERS));
            }
            this.level().setBlockAndUpdate(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()),n);
        }
    }



    public void placeFlesh(BlockPos pos, int amount) {
        // reduces the value preemptively
        amount--;
        int[][] array = {
                {0,0,0},
                {0,1,0},
                {0,0,0}
        };
        if (amount > 10) {
            array = new int[][]{
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 1, 0, 0},
                    {0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0}
            };
        }

        // sets the middle value to -1 if there's something in the way
        if (!level().getBlockState(pos.above()).is(Blocks.AIR)) {
            array[array.length/2][array.length/2] = -1;
            amount++;
        }


        // sets some spaces to -1 to prevent placing stuff there
        for (int x=0;x<array.length;x++) {
            for (int y=0;y<array[0].length;y++) {
                boolean[] result = checkHeights(pos,x-1,y-1,array[x][y]);
                if (!(result[0] || result[1])) {
                    array[x][y] = -1;
                }
             }
        }


        // adds values to locations
        for (int i=0;i<amount;i++) {
            int x = (int) (Math.random()*array.length);
            int y = (int) (Math.random()*array.length);
            int n = 0;
            while(array[x][y] == -1 && n <= 10 ) {
                x = (int) (Math.random()*array.length);
                y = (int) (Math.random()*array.length);
                n++;
            }
            if (array[x][y] != -1) {
                array[x][y] += 1;
            }
        }

        // if everywhere is invalid then drop an item
        boolean zeroSpots = true;
        for(int x=0;x<array.length;x++) {
            for(int y=0;y<array.length;y++) {
                if (array[x][y] != -1) {
                    zeroSpots = false;
                    break;
                }
            }
            if (!zeroSpots) {break;}
        }
        if (zeroSpots) {
            spawnAtLocation(new ItemStack(ModBlocks.FLESH_BLOCK,getFleshCount()));
            return;
        }


        // places the stuff
        for (int x=0;x<array.length;x++) {
            for (int y=0;y<array[0].length;y++) {
                if (array[x][y] > 0) {
                    setGoo(pos, x-(array.length/2), y-(array.length/2), Mth.clamp(0,array[x][y],4));
                }
            }
        }
    }




    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection != Direction.DOWN) {
            Vec2 vecMagic = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
            $$1 = vecMagic.y; $$2 = vecMagic.x;
        }
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }
}
