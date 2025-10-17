package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class MaskItemEntity extends Entity {


    /**This mixin makes stone maskss convert to regular masks in water if they are thrown
     * like an entity*/
    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        if (!this.level().isClientSide()) {
            ItemStack stack = getItem();
            if (stack != null && !stack.isEmpty() && stack.is(ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem())) {
                if (this.isInWater()) {
                    rdbt$transformMask();
                } else {
                    AABB box = this.getBoundingBox();
                    BlockPos.betweenClosedStream(
                            Mth.floor(box.minX), Mth.floor(box.minY),
                            Mth.floor(box.minZ), Mth.floor(box.maxX),
                            Mth.floor(box.maxY), Mth.floor(box.maxZ)
                    ).forEach(blockPos -> {
                        BlockState state = level().getBlockState(blockPos);
                        if (rdbt$isWaterCauldron(state) && rdbt$isInsideCauldron(blockPos)) {
                            rdbt$transformMask();
                        }
                    });
                }
            }
        }
    }

    public void rdbt$transformMask(){
        ItemStack stack = getItem();
        ItemStack stack2 = ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem().getDefaultInstance();
        stack2.setTag(stack.getTag());
        ItemEntity $$4 = new ItemEntity(this.level(), this.getX(),
                this.getY(), this.getZ(),
                stack2);
        $$4.setPickUpDelay(this.pickupDelay);
        $$4.setThrower(this.thrower);
        $$4.setXRot(this.getXRot());
        $$4.setYRot(this.getYRot());
        $$4.setDeltaMovement(this.getDeltaMovement());
        this.discard();
        this.level().addFreshEntity($$4);
    }


    private boolean rdbt$isWaterCauldron(BlockState state) {
        return state.is(Blocks.WATER_CAULDRON) && state.getValue(LayeredCauldronBlock.LEVEL) > 0;
    }

    private boolean rdbt$isInsideCauldron(BlockPos blockPos) {
        double y = getY();
        double x = getX() - blockPos.getX();
        double z = getZ() - blockPos.getZ();

        boolean insideXZ = x > 0.0625 && x < 0.9375 && z > 0.0625 && z < 0.9375;
        boolean insideY = y > blockPos.getY() + 0.25 && y < blockPos.getY() + 0.9375;
        return insideXZ && insideY;
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public MaskItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }


    @Shadow public abstract ItemStack getItem();

    @Shadow @Nullable private UUID thrower;

    @Shadow private int pickupDelay;
}
