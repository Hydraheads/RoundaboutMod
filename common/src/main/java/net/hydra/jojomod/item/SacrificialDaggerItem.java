package net.hydra.jojomod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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

public class SacrificialDaggerItem extends TieredItem implements Vanishable {
    /**It is basically a hybrid speedy weapon + shears that also inflicts bleed on strike*/

    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SacrificialDaggerItem(Tier $$0, float $$1, float $$2, Properties $$3) {
        super($$0, $$3);
        $$3.defaultDurability(40);
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
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if (!$$1.isClientSide) {
            if (!($$0.getDamageValue() >= $$0.getMaxDamage())) {
                int $$5 = this.getUseDuration($$0) - $$3;
                int itemTime = 5;
                if ($$5 >= itemTime) {
                    if ($$2 instanceof ServerPlayer SP){
                        if (!SP.getAbilities().instabuild) {
                            $$0.hurt(1, $$2.level().getRandom(), SP);
                        }
                    } else {
                        $$0.hurt(1,$$2.level().getRandom(),null);
                    }
                    MainUtil.makeMobBleed($$2);
                    MainUtil.makeBleed($$2,0,400,$$2);
                    $$1.playSound(null, $$2, ModSounds.KNIFE_IMPACT_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    $$2.hurt(ModDamageTypes.of($$1, ModDamageTypes.DAGGER), 2.01F);
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
        return UseAnim.BOW;
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
        } else {
            return !$$1.is(Blocks.VINE) && !$$1.is(Blocks.GLOW_LICHEN) ? super.getDestroySpeed($$0, $$1) : 2.0F;
        }
    }
}

