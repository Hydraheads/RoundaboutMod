package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
        if (!this.level().isClientSide() && this.isInWater()){
            ItemStack stack = getItem();
            if (stack != null && !stack.isEmpty() && stack.is(ModBlocks.BLOODY_STONE_MASK_BLOCK.asItem())){
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
        }
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
