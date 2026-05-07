package net.hydra.jojomod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class StoneMaskBlockItem extends BlockItem {
    public StoneMaskBlockItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_GENERIC;
    }

    public InteractionResultHolder<ItemStack> swapWithEquipmentSlot(Item $$0, Level $$1, Player $$2, InteractionHand $$3) {
        ItemStack $$4 = $$2.getItemInHand($$3);
        EquipmentSlot $$5 = getEquipmentSlot();
        ItemStack $$6 = $$2.getItemBySlot($$5);
        if (!EnchantmentHelper.hasBindingCurse($$6) && !ItemStack.matches($$4, $$6)) {
            if (!$$1.isClientSide()) {
                $$2.awardStat(Stats.ITEM_USED.get($$0));
            }

            ItemStack $$7 = $$6.isEmpty() ? $$4 : $$6.copyAndClear();
            ItemStack $$8 = $$4.copyAndClear();
            $$2.setItemSlot($$5, $$8);
            return InteractionResultHolder.sidedSuccess($$7, $$1.isClientSide());
        } else {
            return InteractionResultHolder.fail($$4);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        InteractionResultHolder<ItemStack> placed = super.use($$0,$$1,$$2);
        if (!$$0.isClientSide() && !placed.getResult().consumesAction()){
            return this.swapWithEquipmentSlot(this, $$0, $$1, $$2);
        }
        return placed;
    }

    @Nullable
    public static Equipable get(ItemStack $$0) {
        Item $$3 = $$0.getItem();
        if ($$3 instanceof Equipable) {
            return (Equipable)$$3;
        } else {
            if ($$0.getItem() instanceof BlockItem $$2) {
                Block var6 = $$2.getBlock();
                if (var6 instanceof Equipable) {
                    return (Equipable)var6;
                }
            }

            return null;
        }
    }


    @Override
    public void appendHoverText(ItemStack $$0, @org.jetbrains.annotations.Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        $$2.add(Component.translatable("leveling.roundabout.disc_wip").withStyle(ChatFormatting.RED));
        $$2.add(Component.translatable("leveling.roundabout.disc_wip_2").withStyle(ChatFormatting.RED));

        $$2.add(Component.translatable("roundabout.dev_status.dev_status").withStyle(ChatFormatting.WHITE)
                .append(" ")
                .append(Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA)));
        $$2.add(Component.translatable("roundabout.dev_status.dev_name").withStyle(ChatFormatting.WHITE)
                .append(" ")
                .append(Component.literal(  "Hydra").withStyle(ChatFormatting.YELLOW))
        );
    }
}
