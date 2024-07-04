package net.hydra.jojomod.item;

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
    public static final float SHOOT_POWER = 0.7F;

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
    public void releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3) {
        if ($$2 instanceof Player $$4) {
            int $$5 = this.getUseDuration($$0) - $$3;
            int itemTime = 10;
            if ($$0.is(ModItems.MATCH)){itemTime=5;}
            if ($$5 >= itemTime) {
                if (!$$1.isClientSide) {
                    $$0.hurtAndBreak(1, $$4, $$1x -> $$1x.broadcastBreakEvent($$2.getUsedItemHand()));
                    int knifeCount = 1;
                    MatchEntity $$7 = new MatchEntity($$4, $$1);
                    $$7.shootFromRotation($$4, $$4.getXRot(), $$4.getYRot(), -3F, SHOOT_POWER, 1.0F);

                    $$1.addFreshEntity($$7);
                    $$1.playSound(null, $$7, SoundEvents.EGG_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
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

