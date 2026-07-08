package net.hydra.jojomod.item;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

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
        if (comp != null && !comp.isBlank()){
             $$2.add(Component.literal(comp).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    public static void attackThePerson(Player player, ItemStack stack) {
        if (stack != null && !(stack.getItem() instanceof MemoryChessPieceItem)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }
        player.swing(InteractionHand.MAIN_HAND,true);

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.hasUUID("victim")) {
            return;
        }

        UUID uuid = tag.getUUID("victim");

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        Entity entity = serverLevel.getEntity(uuid);

        if (entity instanceof LivingEntity living && living.hurtTime <= 0) {
            float dmg = 3;
            if (living instanceof Player pl){
                dmg = 2;
            }
            living.hurt(ModDamageTypes.of(living.level(), ModDamageTypes.CHESS_STRIKE, player), dmg);

            player.getMainHandItem().hurtAndBreak(2, player, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }
}
