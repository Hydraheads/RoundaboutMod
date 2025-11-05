package net.hydra.jojomod.item;


import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnubisItem extends Item {


    public AnubisItem(Properties $$0) {
        super($$0);
    }


    @Override
    public boolean canAttackBlock(BlockState $$0, Level $$1, BlockPos $$2, Player $$3) {
        return !$$3.isCreative();
    }


    @Override
    public ItemStack finishUsingItem(ItemStack $$0, Level $$1, LivingEntity $$2) {
        if (!$$1.isClientSide) {
            if ($$2 instanceof Player P) {
                ((StandUser) P).roundabout$setPossessionTime(100);
                ((Player) $$2).getCooldowns().addCooldown($$0.getItem(),10/*2400*/);
                //TODO GET A PROPER SOUND
                P.level().playSound(null,P.blockPosition(), ModSounds.ANUBIS_POSSESSION_EVENT,SoundSource.PLAYERS,1.0F,1.0F);
            }
        }
        return $$0;
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        $$2.add(Component.translatable("leveling.roundabout.disc_wip").withStyle(ChatFormatting.RED));
        $$2.add(Component.translatable("leveling.roundabout.disc_wip_2").withStyle(ChatFormatting.RED));
        $$2.add(Component.translatable("roundabout.dev_status.dev_status").withStyle(ChatFormatting.WHITE)
                .append(" ")
                .append(Component.translatable("roundabout.dev_status.active")).withStyle(ChatFormatting.YELLOW));
        $$2.add(Component.translatable("roundabout.dev_status.dev_name").withStyle(ChatFormatting.WHITE)
                .append(" ")
                .append("Prisma").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public void onUseTick(Level $$0, LivingEntity $$1, ItemStack $$2, int $$3) {
        if (!$$0.isClientSide()) {
            if ($$1.isUsingItem()) {
                if ($$3 % 8 == 0) {
                    ((ServerLevel) $$1.level()).sendParticles(ModParticles.MENACING,
                            $$1.getX(), $$1.getY() + 0.3, $$1.getZ(),
                            1, 0.2, 0.2, 0.2, 0.05);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.fail($$3);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 60;
    }


}
