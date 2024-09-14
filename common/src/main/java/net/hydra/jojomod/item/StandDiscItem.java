package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class StandDiscItem extends Item {

    public final StandPowers standPowers;
    public StandDiscItem(Properties $$0, StandPowers standPowers) {
        super($$0);
        this.standPowers = standPowers;
    }

    public void generateStandPowers(LivingEntity entity){
        ((StandUser)entity).setStandPowers(standPowers.generateStandPowers(entity));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if (!$$0.isClientSide) {
            ItemStack currentDisc = ((StandUser) $$1).roundabout$getStandDisc();
            if (!currentDisc.isEmpty()) {
                addItem($$1, currentDisc.copy());
            }
            if ($$3.getItem() instanceof StandDiscItem SI) {
                ((StandUser) $$1).setStand(null);
                ((StandUser) $$1).setActive(false);
                ((StandUser) $$1).roundabout$setStandDisc($$3.copy());
                SI.generateStandPowers($$1);
            }
            $$3.shrink(1);
        }
        return InteractionResultHolder.consume($$3);
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        $$2.add(this.getDisplayName().withStyle(ChatFormatting.AQUA));
    }

    public MutableComponent getDisplayName() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }


    public void addItem(Player player, ItemStack stack){
        if (canAddItem(stack, player.getInventory())) {
            player.addItem(stack);
        } else {
            ItemEntity $$4 = new ItemEntity(player.level(), player.getX(),
                    player.getY() + player.getEyeHeight(), player.getZ(),
                    stack);
            $$4.setPickUpDelay(40);
            $$4.setThrower(player.getUUID());
            player.level().addFreshEntity($$4);
        }
    }
    public boolean canAddItem(ItemStack itemStack, Inventory inventory) {
        boolean bl = false;
        for (ItemStack itemStack2 : inventory.items) {
            if (!itemStack2.isEmpty() && (!ItemStack.isSameItemSameTags(itemStack2, itemStack) || itemStack2.getCount() >= itemStack2.getMaxStackSize())) continue;
            bl = true;
            break;
        }
        return bl;
    }
}
