package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

public class BowlerHatItem extends TieredItem implements Vanishable {

    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public BowlerHatItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(tier, properties);
        properties.defaultDurability(238);
        this.attackDamage = 0;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.defaultModifiers = builder.build();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean canAttackBlock(BlockState $$0, Level $$1, BlockPos $$2, Player $$3) {
        return !$$3.isCreative();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot $$0) {
        return $$0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers($$0);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level dimension, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (player.getAttackStrengthScale(1) >= 1F) {
            player.startUsingItem(interactionHand);
            if (player.getUseItem() == itemStack && player.getUseItemRemainingTicks() == player.getUseItem().getUseDuration()) {
                dimension.playSound(null, player, ModSounds.BOWLER_HAT_AIM_SOUND_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public boolean mineBlock(ItemStack $$0, Level $$1, BlockState $$2, BlockPos $$3, LivingEntity $$4) {
        if (!$$1.isClientSide && !$$2.is(BlockTags.FIRE)) {
            $$0.hurtAndBreak(1, $$4, $$0x -> $$0x.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return super.mineBlock($$0, $$1, $$2, $$3, $$4);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level dimension, LivingEntity livingEntity, int timeLeft) {
        if (!dimension.isClientSide && livingEntity instanceof Player player) {
            int chargeTime = this.getUseDuration(stack) - timeLeft;

            if (chargeTime >= 10) {
                player.getCooldowns().addCooldown(stack.getItem(), 60);
                BladedBowlerHatEntity $$7 = new BladedBowlerHatEntity(dimension, livingEntity, stack.copy());
                $$7.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                dimension.addFreshEntity($$7);
                stack.shrink(1);
                if (livingEntity != null && ((StandUser)livingEntity).roundabout$isBubbleEncased()){
                    StandUser SE = ((StandUser)livingEntity);
                    if (!dimension.isClientSide()){
                        SE.roundabout$setBubbleEncased((byte) 0);
                        dimension.playSound(null, livingEntity.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                                SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                        ((ServerLevel) dimension).sendParticles(ModParticles.BUBBLE_POP,
                                livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * 0.5, livingEntity.getZ(),
                                5, 0.25, 0.25, 0.25, 0.025);
                    }
                }
            }
        }
    }
}