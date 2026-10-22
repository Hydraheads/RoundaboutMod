package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.phys.Vec3;
import java.util.Random;

public class JackalRifleItem extends FirearmItem implements Vanishable {

    public JackalRifleItem(Properties properties) {
        super(properties.durability(200));
    }

    @Override
    public int getMaxAmmo() { return 1; }

    @Override
    public Item getAmmoItem() { return ModItems.SNIPER_AMMO; }

    @Override
    public int getReloadCooldown() { return 20; }

    @Override
    public byte getReloadSound() { return SoundIndex.SNIPER_RELOAD; }

    @Override
    public int getFireCooldown() { return 15; }

    @Override
    public byte getBulletType() { return RoundaboutBulletEntity.SNIPER; }

    @Override
    public SoundEvent getFireSound() { return ModSounds.JACKAL_FIRE_EVENT; }

    @Override
    public float getFireSoundVolume() { return 100.0F; }

    @Override
    public float getFireSoundPitch() { return 1.0F; }

    @Override
    public void applyRecoil(Player player) {
        S2CPacketUtil.gunRecoil(player, "sniper");
    }

    @Override
    public boolean canWhitesnakeUse() {
        return false;
    }

    @Override
    public void configureBulletTrajectory(RoundaboutBulletEntity bullet, LivingEntity shooter) {
        bullet.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 6.0F, 0.0F);
    }

    @Override
    public Vec3 getMuzzleOffset() {
        return new Vec3(1.0, 0.24, -0.15);
    }
}