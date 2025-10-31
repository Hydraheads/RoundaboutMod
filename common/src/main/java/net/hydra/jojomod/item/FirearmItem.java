package net.hydra.jojomod.item;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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

public class FirearmItem extends Item {

    public FirearmItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack itemStack = $$1.getItemInHand($$2);
        if ($$1.getUseItem() == itemStack) {

        }
        return InteractionResultHolder.success(itemStack);
    }

    public void fireBullet(Level level, Player player) {
        LivingEntity livingEntity = player;
        RoundaboutBulletEntity $$7 = new RoundaboutBulletEntity(level, livingEntity);
        $$7.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        level.addFreshEntity($$7);
        if (livingEntity != null && ((StandUser) livingEntity).roundabout$isBubbleEncased()) {
            StandUser SE = ((StandUser) livingEntity);
            if (!level.isClientSide()) {
                SE.roundabout$setBubbleEncased((byte) 0);
                level.playSound(null, livingEntity.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                        SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                ((ServerLevel) level).sendParticles(ModParticles.BUBBLE_POP,
                        livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * 0.5, livingEntity.getZ(),
                        5, 0.25, 0.25, 0.25, 0.025);
            }
        }
    }
}