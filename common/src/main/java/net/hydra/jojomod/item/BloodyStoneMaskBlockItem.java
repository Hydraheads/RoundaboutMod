package net.hydra.jojomod.item;

import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class BloodyStoneMaskBlockItem extends BlockItem {
    public BloodyStoneMaskBlockItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int $$3, boolean $$4) {
        if (!level.isClientSide()){
            if (entity.isInWater() && entity instanceof LivingEntity LE){
                ItemStack stack2 = ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem().getDefaultInstance();
                stack2.setTag(stack.getTag());
                LE.getSlot($$3).set(stack2);
            }
        }
    }
}
