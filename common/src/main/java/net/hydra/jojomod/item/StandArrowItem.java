package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.StandArrowEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
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
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class StandArrowItem extends RoundaboutArrowItem {

    public StandArrowItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if (!($$3.getDamageValue() >= $$3.getMaxDamage())) {
            CompoundTag tag = $$3.isEmpty() ? null : $$3.getTagElement("StandDisc");
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            if (tag2 != null) {
                if ($$1.isCrouching()) {
                    if ($$1.experienceLevel >= 1 || $$1.isCreative()) {
                        if (!$$1.isCreative()) {
                            $$1.giveExperienceLevels(-1);
                        }
                        rollStand($$0, $$1, $$3);
                        return InteractionResultHolder.consume($$3);
                    } else {
                        $$1.displayClientMessage(Component.translatable("container.enchant.level.requirement", 1).withStyle(ChatFormatting.RED), true);
                        return InteractionResultHolder.fail($$3);
                    }
                } else {
                    $$1.startUsingItem($$2);
                    if ($$0.isClientSide()) {
                        ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_STAND_ARROW_START_SOUND);
                    }
                    return InteractionResultHolder.consume($$3);
                }
            } else {
                if (!$$0.isClientSide) {
                    rollStand($$0, $$1, $$3);
                    return InteractionResultHolder.consume($$3);
                }
                return InteractionResultHolder.fail($$3);
            }
        }
        return InteractionResultHolder.fail($$3);
    }

    public void rollStand(Level $$0, Player $$1, ItemStack $$2) {
        if (!$$0.isClientSide()) {
            $$0.playSound(null, $$1.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.5F, 1.3F);
            ItemStack stack = rerollStand($$2);
            $$1.awardStat(Stats.ITEM_USED.get(this));
            ((ServerLevel) $$0).sendParticles(ParticleTypes.HAPPY_VILLAGER, $$1.getX(),
                $$1.getY() + $$1.getEyeHeight(), $$1.getZ(),
                15, 1, 1, 1, 1);
            if (stack.getItem() instanceof StandDiscItem SD){
                $$1.displayClientMessage(Component.translatable("item.roundabout.stand_arrow.rerollOutcome").withStyle(ChatFormatting.WHITE).append(SD.getDisplayName2()).withStyle(ChatFormatting.AQUA), true);
            }
        }
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
                itemstack = rerollStand($$0);
            }
            if (tag2 != null) {
                itemstack = ItemStack.of(tag2);
            }
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof StandDiscItem de) {
                if (grantStand(itemstack, live)) {
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
                itemstack = rerollStand($$0);
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
                            if (PE.experienceLevel >= 15 || PE.isCreative()) {
                                CompoundTag tag = $$0.isEmpty() ? null : $$0.getTagElement("StandDisc");
                                CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
                                if (tag2 != null) {
                                    ItemStack itemstack = ItemStack.of(tag2);
                                    if (itemstack.getItem() instanceof StandDiscItem de) {
                                        if (grantStand(itemstack, $$2)) {
                                            $$1.playSound(null, $$2.blockPosition(), ModSounds.STAND_ARROW_USE_EVENT, SoundSource.PLAYERS, 1.5F, 1F);
                                            PE.displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireStand").withStyle(ChatFormatting.WHITE), true);
                                            ((ServerLevel) $$1).sendParticles(ParticleTypes.FIREWORK, $$2.getX(),
                                                    $$2.getY() + $$2.getEyeHeight(), $$2.getZ(),
                                                    20, 0, 0, 0, 0.4);
                                            if (!PE.isCreative()) {
                                                PE.giveExperienceLevels(-15);
                                            }
                                            $$0.removeTagKey("StandDisc");
                                            $$0.hurt(1,PE.level().getRandom(),(ServerPlayer) PE);
                                            //$$0.removeTagKey("StandDisc");
                                        }
                                    }
                                }
                            } else {
                                PE.displayClientMessage(Component.translatable("container.enchant.level.requirement", 15).withStyle(ChatFormatting.RED), true);
                            }
                        }
                    }
                }
            }
        } else {
            ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_ITEM_STOP_SOUND);
        }
    }

    public static ItemStack rerollStand(ItemStack $$0){
        CompoundTag tag = $$0.getOrCreateTagElement("StandDisc");
        int index = (int) (Math.floor(Math.random()* ModItems.STAND_ARROW_POOL.size()));
        Item item = ModItems.STAND_ARROW_POOL.get(index);
        if (tag.get("DiscItem") != null) {
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            if (tag2 != null) {
                ItemStack stack2 = ItemStack.of(tag2);
                if (!stack2.isEmpty()) {
                    if (stack2.is(item)){
                        index += 1;
                        if (index >= ModItems.STAND_ARROW_POOL.size()){
                            index=0;
                        }
                        item = ModItems.STAND_ARROW_POOL.get(index);
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
        return UseAnim.BLOCK;
    }


    public static boolean grantStand(ItemStack discStack, LivingEntity target){
        if (discStack.getItem() instanceof StandDiscItem de){
            ((StandUser) target).roundabout$setStand(null);
            ((StandUser) target).roundabout$setActive(false);
            ((StandUser) target).roundabout$setStandDisc(discStack.copy());
            de.generateStandPowers(target);
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
                            Component.translatable("item.roundabout.stand_arrow.reroll").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)
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
