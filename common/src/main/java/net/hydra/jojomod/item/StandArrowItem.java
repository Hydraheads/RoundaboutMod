package net.hydra.jojomod.item;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class StandArrowItem extends RoundaboutArrowItem {

    public StandArrowItem(Properties $$0) {
        super($$0);
    }

    public static void rerollStand(Player player, boolean offh, ItemStack stack,byte context){
        ItemStack item;
        if (offh){
            item =player.getOffhandItem();
        } else {
            item = player.getInventory().getItem((player.getInventory().findSlotMatchingItem(stack)));
        }

        if (!player.isCreative()) {
            CompoundTag tag = stack.isEmpty() ? null : stack.getTagElement("StandDisc");
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            if (tag2 != null) {
                player.giveExperienceLevels(-ClientNetworking.getAppropriateConfig().itemSettings.levelsToRerollStandWithArrow);
            }
        }
        if (context == PacketDataIndex.ITEM_SWITCH_MAIN){
            rollStand(player.level(), player, item,true);
        } else if (context == PacketDataIndex.ITEM_SWITCH_SECONDARY){
            rollStand(player.level(), player, item,false);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if (!($$3.getDamageValue() >= $$3.getMaxDamage())) {
            CompoundTag tag = $$3.isEmpty() ? null : $$3.getTagElement("StandDisc");
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            if (tag2 != null) {
                if ($$1.isCrouching() && !ConfigManager.getAdvancedConfig().standArrowSecondaryPoolv5.isEmpty()) {
                    int reroll = ClientNetworking.getAppropriateConfig().itemSettings.levelsToRerollStandWithArrow;
                    if ($$1.experienceLevel >= reroll || $$1.isCreative()) {
                        if (ConfigManager.getAdvancedConfig().standArrowSecondaryPoolv5.isEmpty()) {
                             if (!$$1.isCreative()) {
                             $$1.giveExperienceLevels(-reroll);
                             }
                             rollStand($$0, $$1, $$3,true);
                             return InteractionResultHolder.consume($$3);
                        } else {
                            if ($$0.isClientSide()){
                                ClientUtil.openStandSwitchUI($$3);
                            }
                        }
                    } else {
                        $$1.displayClientMessage(Component.translatable("container.enchant.level.requirement", reroll).withStyle(ChatFormatting.RED), true);
                        return InteractionResultHolder.fail($$3);
                    }
                } else {
                    $$1.startUsingItem($$2);
                    if ($$0.isClientSide()) {
                        C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_STAND_ARROW_START_SOUND);
                    }
                    return InteractionResultHolder.consume($$3);
                }
            } else {
                if (ConfigManager.getAdvancedConfig().standArrowSecondaryPoolv5.isEmpty()) {
                    if (!$$0.isClientSide) {
                        rollStand($$0, $$1, $$3, true);
                        return InteractionResultHolder.consume($$3);
                    } else {
                        return InteractionResultHolder.fail($$3);
                    }
                } else {
                    if ($$0.isClientSide) {
                        ClientUtil.openStandSwitchUI($$3);
                    }
                }
            }
        }
        return InteractionResultHolder.fail($$3);
    }

    public static void rollStand(Level level, Player player, ItemStack itemStack, boolean primary) {
        if (!level.isClientSide()) {
            level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.5F, 1.3F);
            ItemStack stack = rerollStand(itemStack,primary);
            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(),
                player.getY() + player.getEyeHeight(), player.getZ(),
                15, 1, 1, 1, 1);

            if (stack == null)
            {
                player.displayClientMessage(Component.translatable("item.roundabout.stand_arrow.noStandsInPool").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.AQUA), true);
                return;
            }

            if (stack.getItem() instanceof StandDiscItem SD){
                player.displayClientMessage(Component.translatable("item.roundabout.stand_arrow.rerollOutcome").withStyle(ChatFormatting.WHITE).append(SD.getDisplayName2()).withStyle(ChatFormatting.AQUA), true);
            }
        }
    }

    public boolean isWorthinessType(ItemStack itemStack, LivingEntity LE){
        CompoundTag tag = itemStack.isEmpty() ? null : itemStack.getTagElement("StandDisc");
        CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
        if (tag2 != null) {
            ItemStack itemstack = ItemStack.of(tag2);
            if (itemstack.getItem() instanceof StandDiscItem de) {
                if (de.standPowers.isWorthinessType(LE)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    public static void grantMobStand(ItemStack $$0, Level $$1, LivingEntity live){
        if (!$$1.isClientSide) {
            CompoundTag tag = $$0.isEmpty() ? null : $$0.getTagElement("StandDisc");
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            ItemStack itemstack = ItemStack.EMPTY;
            if (tag2 == null){
                itemstack = rerollStand($$0,true);
            }
            if (tag2 != null) {
                itemstack = ItemStack.of(tag2);
            }
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof StandDiscItem de) {
                if (grantStand(itemstack, live)) {
                    PowerTypes.forceInitializeStandPower(live);
                    $$1.playSound(null, live.blockPosition(), ModSounds.STAND_ARROW_USE_EVENT, SoundSource.PLAYERS, 1.5F, 1F);
                    ((ServerLevel) $$1).sendParticles(ParticleTypes.FIREWORK, live.getX(),
                            live.getY() + live.getEyeHeight(), live.getZ(),
                            20, 0, 0, 0, 0.4);
                    $$0.removeTagKey("StandDisc");
                    //$$0.removeTagKey("StandDisc");
                }
            }
        }
    }


    public static void grantMobRejection(ItemStack $$0, Level $$1, LivingEntity live){
            CompoundTag tag = $$0.isEmpty() ? null : $$0.getTagElement("StandDisc");
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            ItemStack itemstack = ItemStack.EMPTY;
            if (tag2 == null){
                itemstack = rerollStand($$0,true);
            }
            if (tag2 != null) {
                itemstack = ItemStack.of(tag2);
            }
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof StandDiscItem de) {
                ((StandUser)live).roundabout$setRejectionStandDisc(itemstack.copy());
                de.generateStandPowerRejection(live);
                $$0.removeTagKey("StandDisc");
            }
    }


    @Override
    public boolean isValidRepairItem(ItemStack $$0, ItemStack $$1) {
        return $$1.is(ModItems.METEORITE_INGOT);
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }
    @Override
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if (!$$1.isClientSide) {
            if (!($$0.getDamageValue() >= $$0.getMaxDamage())) {
                int $$5 = this.getUseDuration($$0) - $$3;
                int itemTime = 5;
                if ($$5 >= itemTime) {
                    if ($$2 instanceof Player PE) {
                        if (!((StandUser) $$2).roundabout$getStandDisc().isEmpty()) {
                            PE.displayClientMessage(Component.translatable("item.roundabout.stand_arrow.haveStand").withStyle(ChatFormatting.RED), true);
                        } else {
                            int get = ClientNetworking.getAppropriateConfig().itemSettings.levelsToGetStand;
                            CompoundTag tag = $$0.isEmpty() ? null : $$0.getTagElement("StandDisc");
                            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
                            if (tag2 != null) {
                                ItemStack itemstack = ItemStack.of(tag2);
                                if (itemstack.getItem() instanceof StandDiscItem de) {
                                    if (de.standPowers.isSecondaryStand()){
                                        get = 0;
                                    }
                                    if (de.standPowers.isSecondaryStand() || PE.experienceLevel >= get || PE.isCreative()) {
                                        if (grantStand(itemstack, $$2)) {
                                            $$1.playSound(null, $$2.blockPosition(), ModSounds.STAND_ARROW_USE_EVENT, SoundSource.PLAYERS, 1.5F, 1F);
                                            PE.displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireStand").withStyle(ChatFormatting.WHITE), true);
                                            ((IPlayerEntity)PE).roundabout$qmessage(1);
                                            ((ServerLevel) $$1).sendParticles(ParticleTypes.FIREWORK, $$2.getX(),
                                                    $$2.getY() + $$2.getEyeHeight(), $$2.getZ(),
                                                    20, 0, 0, 0, 0.4);
                                            if (!PE.isCreative() && get > 0) {
                                                PE.giveExperienceLevels(-get);
                                            }
                                            $$0.removeTagKey("StandDisc");
                                            $$0.hurt(1,PE.level().getRandom(),(ServerPlayer) PE);
                                            //$$0.removeTagKey("StandDisc");
                                        }

                                    } else {
                                        PE.displayClientMessage(Component.translatable("container.enchant.level.requirement", get).withStyle(ChatFormatting.RED), true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_ITEM_STOP_SOUND);
        }
    }

    public static @Nullable ItemStack rerollStand(ItemStack $$0,boolean primary){
        if (ModItems.STAND_ARROW_POOL.isEmpty())
            return null;

        CompoundTag tag = $$0.getOrCreateTagElement("StandDisc");
        int index = (int) (Math.floor(Math.random()* ModItems.STAND_ARROW_POOL.size()));
        Item item = ModItems.STAND_ARROW_POOL.get(index);
        if (!primary){
            index = (int) (Math.floor(Math.random()* ModItems.STAND_ARROW_SECONDARY_STAND_POOL.size()));
            item = ModItems.STAND_ARROW_SECONDARY_STAND_POOL.get(index);
        }
        if (tag.get("DiscItem") != null) {
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            if (tag2 != null) {
                ItemStack stack2 = ItemStack.of(tag2);
                if (!stack2.isEmpty()) {
                    if (stack2.is(item)){
                        index += 1;
                        if (primary) {
                            if (index >= ModItems.STAND_ARROW_POOL.size()) {
                                index = 0;
                            }
                            item = ModItems.STAND_ARROW_POOL.get(index);
                        } else {
                            if (index >= ModItems.STAND_ARROW_SECONDARY_STAND_POOL.size()) {
                                index = 0;
                            }
                            item = ModItems.STAND_ARROW_SECONDARY_STAND_POOL.get(index);
                        }
                    }
                }
            }
        }
        ItemStack itemStack = item.getDefaultInstance();

        $$0.getOrCreateTagElement("StandDisc").put("DiscItem", itemStack.save(new CompoundTag()));
        return itemStack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }


    public static boolean grantStand(ItemStack discStack, LivingEntity target){
        if (discStack.getItem() instanceof StandDiscItem de){
            if (!target.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_LEVELING)){
                discStack.getOrCreateTagElement("Memory").putByte("Level",de.standPowers.getMaxLevel());
            }

            ((StandUser) target).roundabout$setStand(null);
            ((StandUser) target).roundabout$setActive(false);
            ((StandUser) target).roundabout$setStandDisc(discStack.copy());
            de.generateStandPowers(target);
            ((StandUser) target).roundabout$getStandPowers().rollSkin();
            ((StandUser) target).roundabout$summonStand(target.level(),true,false);
            return true;
        }
        return false;
    }


    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        if (!($$0.getDamageValue() >= $$0.getMaxDamage())) {
            CompoundTag tag = $$0.isEmpty() ? null : $$0.getTagElement("StandDisc");
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            if (tag2 != null) {
                ItemStack itemstack = ItemStack.of(tag2);
                if (itemstack.isEmpty()) {
                    $$2.add(
                            Component.translatable("item.roundabout.stand_arrow.roll").withStyle(ChatFormatting.GRAY)
                    );
                } else if (itemstack.getItem() instanceof StandDiscItem de) {
                    $$2.add(
                            de.getDisplayName2().withStyle(ChatFormatting.AQUA)
                    );
                    //Component.translatable("item.roundabout.stand_arrow.reroll", Minecraft.getInstance().options.keyShift.getDefaultKey().getName())
                    $$2.add(
                            Component.empty()
                    );
                    $$2.add(
                            Component.translatable("item.roundabout.stand_arrow.reroll",ClientNetworking.getAppropriateConfig().itemSettings.levelsToRerollStandWithArrow).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)
                    );
                    $$2.add(
                            Component.translatable("item.roundabout.stand_arrow.reroll2").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)
                    );
                    $$2.add(
                            Component.translatable("item.roundabout.stand_arrow.magnet").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC)
                    );
                }
            } else {
                $$2.add(
                        Component.translatable("item.roundabout.stand_arrow.roll").withStyle(ChatFormatting.GRAY)
                );
                $$2.add(
                        Component.translatable("item.roundabout.stand_arrow.magnet").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.ITALIC)
                );
            }
        } else {
            $$2.add(
                    Component.translatable("item.roundabout.stand_arrow.usedUp").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)
            );
        }
    }


    public static StandDiscItem randomizeStand(){
        int index = (int) (Math.floor(Math.random()* ModItems.STAND_ARROW_POOL.size()));
        return ModItems.STAND_ARROW_POOL.get(index);
    }
}
