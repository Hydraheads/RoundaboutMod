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
}