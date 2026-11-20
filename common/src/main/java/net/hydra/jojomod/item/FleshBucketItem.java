package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.FleshPileEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class FleshBucketItem extends Item {
    public static final float SHOOT_POWER = 0.6F;
    public static final int BASE_CAPACITY = 16;
    public static final String FLESH_MISSING_TAG = "flesh_missing";

    public FleshBucketItem(Properties $$0) {
        super($$0);
    }

    public static int getFleshMissing(ItemStack stack) {
        return stack.getOrCreateTag().getInt(FLESH_MISSING_TAG);
    }

    public static void setFleshMissing(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt(FLESH_MISSING_TAG, count);
    }

    @Override
    public boolean isBarVisible(ItemStack $$0) {
        return getFleshMissing($$0) > 0;
    }

    @Override
    public int getBarColor(ItemStack $$0) {
        float $$1 = Math.max(0.0F, ((float)BASE_CAPACITY -
                (float)getFleshMissing($$0))) / (float)BASE_CAPACITY;
        return Mth.hsvToRgb($$1 / 3f, 1f, 1f);
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag $$0) {
        super.verifyTagAfterLoad($$0);
        if ($$0.contains("Damage") && !$$0.contains(FLESH_MISSING_TAG)) {
            $$0.putInt(FLESH_MISSING_TAG, $$0.getInt("Damage"));
            $$0.remove("Damage");
        }
    }

    @Override
    public int getBarWidth(ItemStack $$0) {
        return Math.round(13.0F - (float)getFleshMissing($$0) * 13.0F / (float)BASE_CAPACITY);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$1, Player $$2, InteractionHand $$3) {
        ItemStack hand = $$2.getItemInHand($$3);
        if (!$$1.isClientSide) {

            FleshPileEntity $$7 = new FleshPileEntity($$2, $$1, BASE_CAPACITY - getFleshMissing(hand));
            if ($$2.isCrouching()) {
                $$7.setFleshCount(Math.min(BASE_CAPACITY - getFleshMissing(hand),8));
            }
            $$7.shootFromRotation($$2, $$2.getXRot(), $$2.getYRot(), -7, SHOOT_POWER, 1.0F);

            $$1.addFreshEntity($$7);
            $$1.playSound(null, $$7, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 0.85F, 1.0F);

        }
        $$2.awardStat(Stats.ITEM_USED.get(this));
        if (!$$2.getAbilities().instabuild) {
            if (!$$1.isClientSide) {
                if(!$$2.isCrouching() || getFleshMissing(hand)+8 >= BASE_CAPACITY) {
                    $$2.setItemInHand($$3, new ItemStack(Items.BUCKET));
                } else {
                    setFleshMissing(hand, Math.max(getFleshMissing(hand)+8,0));
                }
            }
        }

        int cooldown = 40;
        if (( ((StandUser)$$2).roundabout$getStandPowers() instanceof PowersRatt)) {cooldown = 20;}
        $$2.getCooldowns().addCooldown(Items.BUCKET,cooldown);
        $$2.getCooldowns().addCooldown(ModItems.FLESH_BUCKET,cooldown);

        $$2.swing($$3);
        return InteractionResultHolder.consume(hand);
    }
}
