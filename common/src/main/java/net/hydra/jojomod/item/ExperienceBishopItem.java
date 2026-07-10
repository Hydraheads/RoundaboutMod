package net.hydra.jojomod.item;

import net.hydra.jojomod.event.IVillagerAccess;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ExperienceBishopItem extends Item implements Vanishable {
    public ExperienceBishopItem(Properties $$0) {
        super($$0.defaultDurability(3));
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.consume($$3);
    }
    /**Default 72000*/
    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return;
        }

        int usedTime = this.getUseDuration(stack) - timeLeft;
        int itemTime = 7;

        if (usedTime < itemTime) {
            return;
        }

        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return;
        }



        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSounds.CINDERELLA_SPARKLE_EVENT,
                SoundSource.NEUTRAL,
                1F,
                1F
        );

        player.displayClientMessage(
                Component.translatable(
                        "text.roundabout.memory.15"
                ).withStyle(ChatFormatting.AQUA),
                true
        );

    }
    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {

    }
}
