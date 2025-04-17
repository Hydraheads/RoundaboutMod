package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
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
        ((StandUser)entity).roundabout$setStandPowers(standPowers.generateStandPowersPre(entity));
    }

    public void generateStandPowerRejection(LivingEntity entity){
        ((StandUser)entity).roundabout$setRejectionStandPowers(standPowers.generateStandPowers(entity));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if (!$$0.isClientSide) {
            ItemStack currentDisc = ((StandUser) $$1).roundabout$getStandDisc();
            if (!currentDisc.isEmpty()) {
                addItem($$1, MainUtil.saveToDiscData($$1,currentDisc.copy()));
                ((StandUser) $$1).roundabout$getStandPowers().onStandSwitch();
                if (!$$1.isCreative()){
                    ModPacketHandler.PACKET_ACCESS.sendSimpleByte(
                            ((ServerPlayer)$$1), PacketDataIndex.S2C_SIMPLE_FREEZE_STAND);
                }
            }
            if ($$3.getItem() instanceof StandDiscItem SI) {
                ((StandUser) $$1).roundabout$setStand(null);
                ((StandUser) $$1).roundabout$setActive(false);
                ((StandUser) $$1).roundabout$setStandDisc($$3.copy());
                SI.generateStandPowers($$1);
            }
            $$3.shrink(1);
        }
        return InteractionResultHolder.consume($$3);
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        $$2.add(this.getDisplayName2().withStyle(ChatFormatting.AQUA));
        CompoundTag $$4 = $$0.getTagElement("Memory");
        // && $$1.getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_LEVELING)
        if ($$4 != null && $$1 != null) {
            if (!standPowers.isSecondaryStand() && $$4.contains("Level")) {
                if (ClientNetworking.getAppropriateConfig().enableStandLeveling) {
                    byte lvl = (byte) ($$4.getByte("Level") + 1);
                    if (lvl < standPowers.getMaxLevel()) {
                        $$2.add(Component.translatable("leveling.roundabout.disc_development_potential_level", lvl).withStyle(ChatFormatting.GRAY));
                    } else {
                        $$2.add(Component.translatable("leveling.roundabout.disc_maxed").withStyle(ChatFormatting.GRAY));
                    }
                }
            } if ($$4.contains("Skin")) {
                byte skin = ($$4.getByte("Skin"));
                $$2.add(Component.literal(standPowers.getSkinName(skin).getString()).withStyle(ChatFormatting.BLUE));
            }
        } else {
            if (!standPowers.isSecondaryStand()) {
                $$2.add(Component.translatable("leveling.roundabout.disc_development_potential_level", 1).withStyle(ChatFormatting.GRAY));
            }
        }

        if(this.standPowers.isWip()){
            $$2.add(Component.translatable("leveling.roundabout.disc_wip").withStyle(ChatFormatting.RED));
        }
    }

    public MutableComponent getDisplayName2() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    public static MutableComponent getDisplayName2(StandDiscItem sd) {
        return Component.translatable(sd.getDescriptionId() + ".desc");
    }


    public void addItem(Player player, ItemStack stack){
            ItemEntity $$4 = new ItemEntity(player.level(), player.getX(),
                    player.getY() + player.getEyeHeight(), player.getZ(),
                    stack);
            $$4.setPickUpDelay(0);
            $$4.setThrower(player.getUUID());
            player.level().addFreshEntity($$4);
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
