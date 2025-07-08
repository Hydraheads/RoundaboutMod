package net.hydra.jojomod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ScissorItem extends TieredItem implements Vanishable {
    /**It is basically a hybrid speedy weapon + shears that also inflicts bleed on strike*/

    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ScissorItem(Tier $$0, float $$1, float $$2, Item.Properties $$3) {
        super($$0, $$3);
        $$3.defaultDurability(238);
        this.attackDamage = $$1 + $$0.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> $$4 = ImmutableMultimap.builder();
        $$4.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION)
        );
        $$4.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)$$2, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$4.build();
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
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

    @Override
    public boolean mineBlock(ItemStack $$0, Level $$1, BlockState $$2, BlockPos $$3, LivingEntity $$4) {
        if (!$$1.isClientSide && !$$2.is(BlockTags.FIRE)) {
            $$0.hurtAndBreak(1, $$4, $$0x -> $$0x.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        return !$$2.is(BlockTags.LEAVES)
                && !$$2.is(Blocks.COBWEB)
                && !$$2.is(Blocks.GRASS)
                && !$$2.is(Blocks.FERN)
                && !$$2.is(Blocks.DEAD_BUSH)
                && !$$2.is(Blocks.HANGING_ROOTS)
                && !$$2.is(Blocks.VINE)
                && !$$2.is(Blocks.TRIPWIRE)
                && !$$2.is(BlockTags.WOOL)
                ? super.mineBlock($$0, $$1, $$2, $$3, $$4)
                : true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState $$0) {
        return $$0.is(Blocks.COBWEB) || $$0.is(Blocks.REDSTONE_WIRE) || $$0.is(Blocks.TRIPWIRE);
    }

    @Override
    public float getDestroySpeed(ItemStack $$0, BlockState $$1) {
        if ($$1.is(BlockTags.LEAVES)) {
            return 3.0F;
        } else if ($$1.is(Blocks.COBWEB)) {
            return 12.0F;
        } else if ($$1.is(BlockTags.WOOL)) {
            return 4.0F;
        } else {
            return !$$1.is(Blocks.VINE) && !$$1.is(Blocks.GLOW_LICHEN) ? super.getDestroySpeed($$0, $$1) : 2.0F;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext $$0) {
        Level $$1 = $$0.getLevel();
        BlockPos $$2 = $$0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if ($$3.getBlock() instanceof GrowingPlantHeadBlock $$5 && !$$5.isMaxAge($$3)) {
            Player $$6 = $$0.getPlayer();
            ItemStack $$7 = $$0.getItemInHand();
            if ($$6 instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)$$6, $$2, $$7);
            }

            $$1.playSound($$6, $$2, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
            BlockState $$8 = $$5.getMaxAgeState($$3);
            $$1.setBlockAndUpdate($$2, $$8);
            $$1.gameEvent(GameEvent.BLOCK_CHANGE, $$2, GameEvent.Context.of($$0.getPlayer(), $$8));
            if ($$6 != null) {
                $$7.hurtAndBreak(1, $$6, $$1x -> $$1x.broadcastBreakEvent($$0.getHand()));
            }

            return InteractionResult.sidedSuccess($$1.isClientSide);
        }

        return super.useOn($$0);
    }
}

