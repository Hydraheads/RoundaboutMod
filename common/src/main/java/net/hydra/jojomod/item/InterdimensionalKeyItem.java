package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InterdimensionalKeyItem extends Item {
    public static final String LINKED_DIM_KEY = "LinkedDimension";
    private static final ResourceLocation EMPTY = new ResourceLocation("roundabout", "empty");

    public static ResourceLocation getLinkedDimension(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(LINKED_DIM_KEY)) {
            Roundabout.LOGGER.info(tag.getString(LINKED_DIM_KEY));
            return ResourceLocation.tryParse(tag.getString(LINKED_DIM_KEY));
        }
        return EMPTY;
    }

    public static void setLinkedDimension(ItemStack stack, ResourceLocation dimension) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(LINKED_DIM_KEY, dimension.toString());
    }

    public static boolean isLinked(ItemStack stack) {
        return !getLinkedDimension(stack).equals(EMPTY);
    }

    public InterdimensionalKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide)
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        if (isLinked(player.getItemInHand(hand)))
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        else
        {
            if (((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C d4c)
            {
                if (!level.dimension().location().getNamespace().equals("roundabout"))
                    return InteractionResultHolder.pass(player.getItemInHand(hand));

                setLinkedDimension(player.getItemInHand(hand), level.dimension().location());

                player.displayClientMessage(Component.translatable("item.roundabout.interdimensional_key.success"), true);

                return InteractionResultHolder.consume(player.getItemInHand(hand));
            }
            else
            {
                player.displayClientMessage(Component.translatable("item.roundabout.interdimensional_key.error"), true);
                return InteractionResultHolder.pass(player.getItemInHand(hand));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> hoverText, TooltipFlag tooltipFlag) {
        hoverText.add(
                isLinked(stack)
                        ? Component.translatable("item.roundabout.interdimensional_key.linked")
                        : Component.translatable("item.roundabout.interdimensional_key.unlinked")
        );

        super.appendHoverText(stack, level, hoverText, tooltipFlag);
    }
}
