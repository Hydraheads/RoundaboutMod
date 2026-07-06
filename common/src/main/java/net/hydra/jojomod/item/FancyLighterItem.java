package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.paintings.BirthOfVenusPainting;
import net.hydra.jojomod.entity.paintings.RoundaboutPainting;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

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
        InteractionResultHolder<ItemStack> placed = super.use($$0,$$1,$$2);
        if (!$$0.isClientSide()){
            this.lirOrUnlit($$0, $$1, $$3);
        }
        return placed;
    }

    public void checkIsLitOrNot(ItemStack stack){
        System.out.println(stack.getOrCreateTag().getBoolean(IS_LIT_TAG));
    }

    public void unlit(ItemStack stack){
        this.setIsNotLit(stack, true);
    }

    public void lirOrUnlit(Level $$0, Player $$1, ItemStack $$2){
        if(!$$1.isInWaterOrRain()) {
            if (!getIsLit($$2)) {
                setIsNotLit($$2, true);
                checkIsLitOrNot($$2);
            } else {
                setIsNotLit($$2, false);
                checkIsLitOrNot($$2);
            }
        }
    }

    public float getCurrentPredicateValue(Level level, ItemStack stack) {

        if (level != null) {
            return getIsLit(stack) ? 0.2f : 0.0f;
        }
        return 0.0f;
    }

}
