package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
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


    public void convertStuff(ItemStack stack, Player player){
        if (stack.getItem() instanceof StandDiscItem SI) {
            ((StandUser) player).roundabout$setStand(null);
            ((StandUser) player).roundabout$setActive(false);
            ((StandUser) player).roundabout$setStandDisc(stack.copy());
            ((StandUser) player).roundabout$getStandPowers().onStandSwitchInto();
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3;
        if ($$2.equals(InteractionHand.MAIN_HAND)){
            $$3 = $$1.getItemBySlot(EquipmentSlot.MAINHAND);
        } else {
            $$3 = $$1.getItemBySlot(EquipmentSlot.OFFHAND);
        }
        if (!$$0.isClientSide) {
            if (!$$3.isEmpty() && $$3.getItem() instanceof StandDiscItem) {
                ItemStack currentDisc = ((StandUser) $$1).roundabout$getStandDisc().copy();
                if (!currentDisc.isEmpty()) {
                    ItemStack convDisc = MainUtil.saveToDiscData($$1, currentDisc.copy());
                    ((StandUser) $$1).roundabout$getStandPowers().onStandSwitch();
                    convertStuff($$3, $$1);
                    $$1.getCooldowns().addCooldown(this, 22);
                    $$1.getCooldowns().addCooldown(convDisc.getItem(), 22);
                    if ($$2.equals(InteractionHand.MAIN_HAND)){
                        $$1.setItemSlot(
                                EquipmentSlot.MAINHAND,
                                convDisc
                        );
                    } else {
                        $$1.setItemSlot(
                                EquipmentSlot.OFFHAND,
                                convDisc
                        );
                    }

                    if (!$$1.isCreative()) {
                        ((StandUser)$$1).roundabout$setSealedTicks(
                                ClientNetworking.getAppropriateConfig().itemSettings.switchStandDiscLength);
                    }
                } else {
                    convertStuff($$3, $$1);
                    $$3.shrink(1);
                }
            }
        } else {
            if ($$1 !=  null){
                ((StandUser)$$1).roundabout$setInteractedWithDisc(true);
            }
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
                if (ClientNetworking.getAppropriateConfig().standLevelingSettings.enableStandLeveling) {
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
            $$2.add(Component.translatable("leveling.roundabout.disc_wip_2").withStyle(ChatFormatting.RED));
            $$2.add(Component.translatable("leveling.roundabout.disc_wip_3").withStyle(ChatFormatting.RED));
            $$2.add(Component.translatable("roundabout.dev_status.dev_status").withStyle(ChatFormatting.WHITE)
                    .append(" ")
                    .append(this.standPowers.ifWipListDevStatus()));
            $$2.add(Component.translatable("roundabout.dev_status.dev_name").withStyle(ChatFormatting.WHITE)
                    .append(" ")
                    .append(this.standPowers.ifWipListDev()));
        }
    }

    public MutableComponent getDisplayName2() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    public static MutableComponent getDisplayName2(StandDiscItem sd) {
        return Component.translatable(sd.getDescriptionId() + ".desc");
    }

}
