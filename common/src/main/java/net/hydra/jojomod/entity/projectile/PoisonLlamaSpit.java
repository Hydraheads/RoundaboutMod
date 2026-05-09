package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class PoisonLlamaSpit extends LlamaSpit {
    public PoisonLlamaSpit(EntityType<? extends LlamaSpit> $$0, Level $$1) {
        super($$0, $$1);
    }

    public PoisonLlamaSpit(Level $$0, LivingEntity $$1) {
        this(ModEntities.POISON_LLAMA_SPIT, $$0);
        this.setOwner($$1);
        this.setPos($$1.getX() - (double)($$1.getBbWidth() + 1.0F) * (double)0.5F * (double) Mth.sin($$1.yBodyRot * ((float)Math.PI / 180F)), $$1.getEyeY() - (double)0.1F, $$1.getZ() + (double)($$1.getBbWidth() + 1.0F) * (double)0.5F * (double)Mth.cos($$1.yBodyRot * ((float)Math.PI / 180F)));
    }

    protected void onHitEntity(EntityHitResult $$0) {

    }
    protected void onHitBlock(BlockHitResult $$0) {

        setGoo($$0.getBlockPos(), 0, 0);
        super.onHitBlock($$0);
    }
    public void setGoo(BlockPos pos, int offsetX, int offsetZ){
        BlockPos blockPos = null;
        if (canPlaceGoo(pos, offsetX, +1, offsetZ)) {
            blockPos = new BlockPos(pos.getX() + offsetX, pos.getY() + 1, pos.getZ() + offsetZ);
        } else if (canPlaceGoo(pos, offsetX, +2, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY() + 2,pos.getZ()+offsetZ);
        } else if (canPlaceGoo(pos, offsetX, 0, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY(),pos.getZ()+offsetZ);
        } else if (canPlaceGoo(pos, offsetX, -1, offsetZ)){
            blockPos = new BlockPos(pos.getX()+offsetX,pos.getY()-1,pos.getZ()+offsetZ);
        }
        //if (this.level().getBlockState(pos).getBlock())
        if (blockPos != null) {
            this.level().setBlockAndUpdate(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()), ModBlocks.ACID_PUDDLE.defaultBlockState());
        }
    }
    public boolean canPlaceGoo(BlockPos pos, int offsetX, int offsetY, int offsetZ){
        BlockPos blk =  new BlockPos(pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);

        if (this.level().isEmptyBlock(blk)) {
            BlockPos $$8 = blk.below();
            if (this.level().getBlockState($$8).isFaceSturdy(this.level(), $$8, Direction.UP)) {
                return true;
            }
        }

        return false;
    }
}