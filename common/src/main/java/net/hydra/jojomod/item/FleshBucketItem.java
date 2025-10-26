package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.FleshPileEntity;
import net.hydra.jojomod.entity.projectile.GasolineSplatterEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.nbt.CompoundTag;
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

public class FleshBucketItem extends Item {
    public static final float SHOOT_POWER = 0.6F;


    public FleshBucketItem(Properties $$0) {
        super($$0);
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level $$1, Player $$2, InteractionHand $$3) {
        ItemStack hand = $$2.getItemInHand($$3);
        if (!$$1.isClientSide) {

            FleshPileEntity $$7 = new FleshPileEntity($$2, $$1,hand.getMaxDamage()-hand.getDamageValue());
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

        int cooldown = 40;
        if (!( ((StandUser)$$2).roundabout$getStandPowers() instanceof PowersRatt)) {cooldown = 20;}
        $$2.getCooldowns().addCooldown(Items.BUCKET,cooldown);
        $$2.getCooldowns().addCooldown(ModItems.FLESH_BUCKET,cooldown);

        $$2.swing($$3);
        return InteractionResultHolder.consume(hand);
    }
}
