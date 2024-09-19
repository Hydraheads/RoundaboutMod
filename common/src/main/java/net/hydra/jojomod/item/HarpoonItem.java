package net.hydra.jojomod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.hydra.jojomod.entity.projectile.HarpoonEntity;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HarpoonItem extends Item implements Vanishable {
    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float SHOOT_POWER = 2.5F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public HarpoonItem(Item.Properties $$0) {
        super($$0);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> $$1 = ImmutableMultimap.builder();
        $$1.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 6.0, AttributeModifier.Operation.ADDITION));
        $$1.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.7F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$1.build();
    }

    @Override
    public boolean canAttackBlock(BlockState $$0, Level $$1, BlockPos $$2, Player $$3) {
        return !$$3.isCreative();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }


    @Override
    public boolean isValidRepairItem(ItemStack $$0, ItemStack $$1) {
        return $$1.is(Items.IRON_INGOT);
    }

    @Override
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if ($$2 instanceof Player $$4) {
            int $$5 = this.getUseDuration($$0) - $$3;
            if ($$5 >= 10) {
                    if (!$$1.isClientSide) {
                        $$0.hurtAndBreak(1, $$4, $$1x -> $$1x.broadcastBreakEvent($$2.getUsedItemHand()));
                            HarpoonEntity $$7 = new HarpoonEntity($$1, $$4, $$0);
                            $$7.shootFromRotation($$4, $$4.getXRot(), $$4.getYRot(), 0.0F, 2.5F, 1.0F);
                            if ($$4.getAbilities().instabuild) {
                                $$7.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            $$1.addFreshEntity($$7);
                            $$1.playSound(null, $$7, ModSounds.HARPOON_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!$$4.getAbilities().instabuild) {
                                $$4.getInventory().removeItem($$0);
                            }
                    }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        if ($$3.getDamageValue() >= $$3.getMaxDamage() - 1) {
            return InteractionResultHolder.fail($$3);
        } else if (EnchantmentHelper.getRiptide($$3) > 0 && !$$1.isInWaterOrRain()) {
            return InteractionResultHolder.fail($$3);
        } else {
            $$1.startUsingItem($$2);
            return InteractionResultHolder.consume($$3);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack $$0, LivingEntity $$1, LivingEntity $$2) {
        $$0.hurtAndBreak(1, $$2, $$0x -> $$0x.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack $$0, Level $$1, BlockState $$2, BlockPos $$3, LivingEntity $$4) {
        if ((double)$$2.getDestroySpeed($$1, $$3) != 0.0) {
            $$0.hurtAndBreak(2, $$4, $$0x -> $$0x.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }

        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot $$0) {
        return $$0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers($$0);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
