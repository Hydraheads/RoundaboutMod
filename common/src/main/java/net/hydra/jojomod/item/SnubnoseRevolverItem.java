package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.BladedBowlerHatEntity;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class SnubnoseRevolverItem extends FirearmItem implements Vanishable {

    public SnubnoseRevolverItem(Properties $$0) {
        super($$0);
    }

    private static final String AMMO_COUNT_TAG = "AmmoCount";

    private int getAmmo(ItemStack stack) {
        return stack.getOrCreateTag().getInt(AMMO_COUNT_TAG);
    }

    private void setAmmo(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt(AMMO_COUNT_TAG, count);
    }

    int maxAmmo = 6;

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {
        ItemStack itemStack = $$1.getMainHandItem();
        if (!(itemStack.getItem() instanceof SnubnoseRevolverItem)) {
            return InteractionResultHolder.fail(itemStack);
        }
        LivingEntity livingEntity = $$1;
        int ammo = getAmmo(itemStack);
        if (ammo > 0) {
            if ($$1 instanceof LivingEntity) {
                RoundaboutBulletEntity $$7 = new RoundaboutBulletEntity($$0, livingEntity);
                $$7.shootFromRotation($$1, $$1.getXRot(), $$1.getYRot(), 0.0F, 1.5F, 1.0F);
                $$0.addFreshEntity($$7);
                if (livingEntity != null && ((StandUser) livingEntity).roundabout$isBubbleEncased()) {
                    StandUser SE = ((StandUser) livingEntity);
                    if (!$$0.isClientSide()) {
                        SE.roundabout$setBubbleEncased((byte) 0);
                        $$0.playSound(null, livingEntity.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                                SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                        ((ServerLevel) $$0).sendParticles(ModParticles.BUBBLE_POP,
                                livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * 0.5, livingEntity.getZ(),
                                5, 0.25, 0.25, 0.25, 0.025);
                    }
                }
            }
        } else if ($$1.isCrouching()) {
            setAmmo(itemStack, maxAmmo);
            Roundabout.LOGGER.info("Reloaded");
        }
        super.use($$0, $$1, $$2);

        return InteractionResultHolder.consume(itemStack);
    }
}

