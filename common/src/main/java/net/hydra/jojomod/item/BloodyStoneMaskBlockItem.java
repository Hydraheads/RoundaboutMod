package net.hydra.jojomod.item;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.List;

public class BloodyStoneMaskBlockItem extends BlockItem {
    public BloodyStoneMaskBlockItem(Block $$0, Properties $$1) {
        super($$0, $$1);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int $$3, boolean $$4) {
        if (!level.isClientSide()){
            if (entity.isInWater() && entity instanceof LivingEntity LE &&
                    !(FateTypes.isTransforming(LE))){
                ItemStack stack2 = ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem().getDefaultInstance();
                stack2.setTag(stack.getTag());
                LE.getSlot($$3).set(stack2);
            }
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
