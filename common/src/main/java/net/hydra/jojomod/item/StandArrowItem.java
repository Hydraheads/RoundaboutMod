package net.hydra.jojomod.item;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class StandArrowItem extends Item {

    public StandArrowItem(Properties $$0) {
        super($$0);
    }

    public static void setDuration(ItemStack $$0, byte $$1) {
        Roundabout.LOGGER.info("2");
        $$0.getOrCreateTagElement("StandDisc").put("DiscItem", randomizeStand().getDefaultInstance().save(new CompoundTag()));
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
            CompoundTag tag = $$3.isEmpty() ? null : $$3.getTagElement("StandDisc");
            CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
            if (tag2 != null) {
                $$1.startUsingItem($$2);
                return InteractionResultHolder.consume($$3);
            } else {
                if (!$$0.isClientSide) {
                    setDuration($$3, (byte) 1);
                    $$1.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.consume($$3);
                }
                return InteractionResultHolder.fail($$3);
            }
        //return InteractionResultHolder.fail($$3);
    }


    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }
    @Override
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if (!$$1.isClientSide) {
            int $$5 = this.getUseDuration($$0) - $$3;
            int itemTime = 5;
            if ($$5 >= itemTime) {
                CompoundTag tag = $$0.isEmpty() ? null : $$0.getTagElement("StandDisc");
                CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
                if (tag2 != null) {
                    ItemStack itemstack = ItemStack.of(tag2);
                    if (itemstack.getItem() instanceof StandDiscItem de) {
                        if (grantStand(itemstack, $$2)) {
                            itemstack.removeTagKey("StandDisc");
                        }
                    }
                }
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BLOCK;
    }


    public static boolean grantStand(ItemStack discStack, LivingEntity target){
        if (discStack.getItem() instanceof StandDiscItem de){
            ((StandUser) target).setStand(null);
            ((StandUser) target).setActive(false);
            ((StandUser) target).roundabout$setStandDisc(discStack.copy());
            de.generateStandPowers(target);
            ((StandUser) target).summonStand(target.level(),true,true);
            return true;
        }
        return false;
    }


    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {

        CompoundTag tag = $$0.isEmpty() ? null : $$0.getTagElement("StandDisc");
        CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
        if (tag2 != null) {
            ItemStack itemstack = ItemStack.of(tag2);
            if (itemstack.getItem() instanceof StandDiscItem de) {
                    $$2.add(
                            de.getDisplayName().withStyle(ChatFormatting.AQUA)
                    );
                //Component.translatable("item.roundabout.stand_arrow.reroll", Minecraft.getInstance().options.keyShift.getDefaultKey().getName())
                $$2.add(
                        Component.translatable("item.roundabout.stand_arrow.reroll").withStyle(ChatFormatting.GRAY)
                );
                $$2.add(
                        Component.translatable("item.roundabout.stand_arrow.reroll2").withStyle(ChatFormatting.GRAY)
                );
            }
        } else {
            $$2.add(
                    Component.translatable("item.roundabout.stand_arrow.roll").withStyle(ChatFormatting.GRAY)
            );
            $$2.add(
                    Component.translatable("item.roundabout.stand_arrow.roll2").withStyle(ChatFormatting.GRAY)
            );
        }
    }


    public static StandDiscItem randomizeStand(){
        int index = (int) (Math.floor(Math.random()* ModItems.STAND_ARROW_POOL.size()));
        return ModItems.STAND_ARROW_POOL.get(index);
    }
}
