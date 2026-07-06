package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class FancyLighterItem extends BlockItem {

    public FancyLighterItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }


    private static final String OWNER_IS_WHO = "WhoIsOwner";
    private static final String IS_LIT_TAG = "IsLighterLit";

    private boolean getIsLit(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(IS_LIT_TAG);
    }

    private void setIsNotLit(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean(IS_LIT_TAG, value);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);

        if(entity.isInWaterOrRain()){
            if(!getIsLit(stack)) {
                setIsNotLit(stack, true);
                checkIsLitOrNot(stack);
            }
        }
    }

    public int getLighterOwner(ItemStack stack){return stack.getOrCreateTag().getInt(OWNER_IS_WHO);}
    public void setLighterOwner(ItemStack stack, int value){
        stack.getOrCreateTag().putInt(OWNER_IS_WHO, value);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if(!$$1.isInWaterOrRain()) {
            if (!getIsLit($$3)) {
                setIsNotLit($$3, true);
                checkIsLitOrNot($$3);
            } else {
                setIsNotLit($$3, false);
                checkIsLitOrNot($$3);
            }
            if ($$0.isClientSide) {
                System.out.println(getLighterOwner($$3));
            }
        }
        return InteractionResultHolder.fail($$3);
    }

    public void checkIsLitOrNot(ItemStack stack){
        System.out.println(stack.getOrCreateTag().getBoolean(IS_LIT_TAG));
    }

    public void unlit(ItemStack stack){
        this.setIsNotLit(stack, true);
    }

    public float getCurrentPredicateValue(Level level, ItemStack stack) {

        if (level != null) {

            if (getIsLit(stack)) {
                return 0.2f;
            }
        }
        return 0.0f;
    }

}
