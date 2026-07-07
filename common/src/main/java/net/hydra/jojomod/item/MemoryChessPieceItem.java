package net.hydra.jojomod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MemoryChessPieceItem extends Item implements Vanishable {
    public MemoryChessPieceItem(Properties $$0) {
        super($$0.defaultDurability(6));
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);

        return InteractionResultHolder.sidedSuccess($$3, $$0.isClientSide());
    }

    public static ItemStack initializePiece(ItemStack stack, Entity victim, int stealType){
        if (victim != null){
            stack.getOrCreateTag().putUUID("victim",victim.getUUID());
            stack.getOrCreateTag().putInt("stealType",stealType);
            stack.getOrCreateTag().putString("vicName", victim.getName().getString());
        }
        return stack;
    }
    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        String comp = $$0.getOrCreateTag().getString("vicName");
        if (comp != null){
             $$2.add(Component.literal(comp).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }
}
