package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GasolineCanItem extends Item implements Vanishable {


    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 2.0F;
    public static final float SHOOT_POWER = 0.48F;

    public GasolineCanItem(Properties $$0) {
        super($$0);
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
    public InteractionResultHolder<ItemStack> use(Level $$1, Player $$2, InteractionHand $$3) {
        ItemStack hand = $$2.getItemInHand($$3);
                if (!$$1.isClientSide) {
                    hand.hurtAndBreak(1, $$2, $$1x -> $$1x.broadcastBreakEvent($$2.getUsedItemHand()));
                    GasolineCanEntity $$7 = new GasolineCanEntity($$2, $$1);
                    $$7.shootFromRotation($$2, $$2.getXRot(), $$2.getYRot(), -25F, SHOOT_POWER, 1.0F);

                    $$1.addFreshEntity($$7);
                    $$1.playSound(null, $$7, SoundEvents.EGG_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!$$2.getAbilities().instabuild) {
                        hand.shrink(1);
                    }
                }
        $$2.getCooldowns().addCooldown(this, 20);
                $$2.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(hand);
    }

    @Override
    public int getEnchantmentValue() {
        return
                1;
    }



}

