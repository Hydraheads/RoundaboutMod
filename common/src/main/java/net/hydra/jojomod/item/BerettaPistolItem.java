package net.hydra.jojomod.item;

import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.phys.Vec3;

public class BerettaPistolItem extends FirearmItem implements Vanishable {

    public BerettaPistolItem(Properties properties) {
        super(properties.durability(300));
    }

    @Override
    public int getMaxAmmo() { return 7; }

    @Override
    public Item getAmmoItem() { return ModItems.PISTOL_AMMO; }

    @Override
    public int getReloadCooldown() { return 70; }

    @Override
    public byte getReloadSound() { return SoundIndex.REVOLVER_RELOAD; }

    @Override
    public int getFireCooldown() { return 3; }

    @Override
    public byte getBulletType() { return RoundaboutBulletEntity.BERETTA; }

    @Override
    public SoundEvent getFireSound() { return ModSounds.SNUBNOSE_FIRE_EVENT; }

    @Override
    public float getFireSoundVolume() { return 7.0F; }

    @Override
    public float getFireSoundPitch() { return 1.0F; }

    @Override
    public void applyRecoil(Player player) {
        // no recoil
    }

    @Override
    public boolean canWhitesnakeUse() {
        return true;
    }

    @Override
    public void configureBulletTrajectory(RoundaboutBulletEntity bullet, LivingEntity shooter) {
        bullet.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 3.7F, 1.8F);
    }

    @Override
    public Vec3 getMuzzleOffset() {
        return new Vec3(1.0, 0.24, -0.15);
    }
}