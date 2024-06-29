package net.hydra.jojomod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class KnifeItem extends Item implements Vanishable {


    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 3.0F;
    public static final float SHOOT_POWER = 2.5F;

    public KnifeItem(Item.Properties $$0) {
        super($$0);
    }

    @Override
    public boolean canAttackBlock(BlockState $$0, Level $$1, BlockPos $$2, Player $$3) {
        return !$$3.isCreative();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.SPEAR;
    }

    /**Default 72000*/
    @Override
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if ($$2 instanceof Player $$4) {
            int $$5 = this.getUseDuration($$0) - $$3;
            int itemTime = 10;
            if ($$0.is(ModItems.KNIFE)){itemTime=5;}
            if ($$5 >= itemTime) {
                int $$6 = EnchantmentHelper.getRiptide($$0);
                if ($$6 <= 0 || $$4.isInWaterOrRain()) {
                    if (!$$1.isClientSide) {
                        $$0.hurtAndBreak(1, $$4, $$1x -> $$1x.broadcastBreakEvent($$2.getUsedItemHand()));
                        if ($$6 == 0) {
                            KnifeEntity $$7 = new KnifeEntity($$1, $$4, $$0);
                            $$7.shootFromRotation($$4, $$4.getXRot(), $$4.getYRot(), 0.0F, 2.5F + (float)$$6 * 0.5F, 1.0F);
                            if ($$4.getAbilities().instabuild) {
                                $$7.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            $$1.addFreshEntity($$7);
                            $$1.playSound(null, $$7, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!$$4.getAbilities().instabuild) {
                                $$4.getInventory().removeItem($$0);
                            }
                        }
                    }

                    $$4.awardStat(Stats.ITEM_USED.get(this));
                    if ($$6 > 0) {
                        float $$8 = $$4.getYRot();
                        float $$9 = $$4.getXRot();
                        float $$10 = -Mth.sin($$8 * (float) (Math.PI / 180.0)) * Mth.cos($$9 * (float) (Math.PI / 180.0));
                        float $$11 = -Mth.sin($$9 * (float) (Math.PI / 180.0));
                        float $$12 = Mth.cos($$8 * (float) (Math.PI / 180.0)) * Mth.cos($$9 * (float) (Math.PI / 180.0));
                        float $$13 = Mth.sqrt($$10 * $$10 + $$11 * $$11 + $$12 * $$12);
                        float $$14 = 3.0F * ((1.0F + (float)$$6) / 4.0F);
                        $$10 *= $$14 / $$13;
                        $$11 *= $$14 / $$13;
                        $$12 *= $$14 / $$13;
                        $$4.push((double)$$10, (double)$$11, (double)$$12);
                        $$4.startAutoSpinAttack(20);
                        if ($$4.onGround()) {
                            float $$15 = 1.1999999F;
                            $$4.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                        }

                        SoundEvent $$16;
                        if ($$6 >= 3) {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if ($$6 == 2) {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_1;
                        }

                        $$1.playSound(null, $$4, $$16, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.consume($$3);
    }

    @Override
    public int getEnchantmentValue() {
        return
                1;
    }
}
