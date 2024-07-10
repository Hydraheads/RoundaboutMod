package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.GasolineSplatterEntity;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class GasolineBucketItem extends Item {
    public static final float SHOOT_POWER = 0.6F;
    public GasolineBucketItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$1, Player $$2, InteractionHand $$3) {
        ItemStack hand = $$2.getItemInHand($$3);
        if (!$$1.isClientSide) {
            hand.hurtAndBreak(1, $$2, $$1x -> $$1x.broadcastBreakEvent($$2.getUsedItemHand()));
            GasolineSplatterEntity $$7 = new GasolineSplatterEntity($$2, $$1);
            $$7.shootFromRotation($$2, $$2.getXRot(), $$2.getYRot(), -7, SHOOT_POWER, 1.0F);

            $$1.addFreshEntity($$7);
            $$1.playSound(null, $$7, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 0.85F, 1.0F);
        }
        $$2.awardStat(Stats.ITEM_USED.get(this));
        if (!$$2.getAbilities().instabuild) {
            if (!$$1.isClientSide) {
                $$2.setItemInHand($$3, new ItemStack(Items.BUCKET));
            }
        }
        $$2.swing($$3);
        return InteractionResultHolder.consume(hand);
    }
}
