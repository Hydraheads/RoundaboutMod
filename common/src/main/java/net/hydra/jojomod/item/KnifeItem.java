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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.phys.EntityHitResult;
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
                if (!$$1.isClientSide) {
                    $$0.hurtAndBreak(1, $$4, $$1x -> $$1x.broadcastBreakEvent($$2.getUsedItemHand()));
                    int knifeCount = 1;
                    boolean bundle = $$0.is(ModItems.KNIFE_BUNDLE);
                    if (bundle){knifeCount=4;}
                    for (int i = 0; i< knifeCount; i++) {

                        KnifeEntity $$7 = new KnifeEntity($$1, $$4, $$0);
                        if (bundle){
                            $$7.shootFromRotationWithVariance($$4, $$4.getXRot(), $$4.getYRot(), 0.0F, 1.5F, 1.0F);
                        } else {
                            $$7.shootFromRotation($$4, $$4.getXRot(), $$4.getYRot(), 0.0F, 1.5F, 1.0F);
                        }
                        if ($$4.getAbilities().instabuild) {
                            $$7.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        $$1.addFreshEntity($$7);
                        if (i == 0){
                            $$1.playSound(null, $$7, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                    if (!$$4.getAbilities().instabuild) {
                        $$0.shrink(1);
                    }
                }

                $$4.awardStat(Stats.ITEM_USED.get(this));
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
