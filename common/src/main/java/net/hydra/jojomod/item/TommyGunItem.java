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

public class TommyGunItem extends FirearmItem implements Vanishable {

    public TommyGunItem(Properties properties) {
        super(properties.durability(1500));
    }

    @Override
    public int getMaxAmmo() { return 30; }

    @Override
    public Item getAmmoItem() { return ModItems.TOMMY_AMMO; }

    @Override
    public int getReloadCooldown() { return 70; }

    @Override
    public byte getReloadSound() { return SoundIndex.TOMMY_RELOAD; }

    @Override
    public int getFireCooldown() { return 2; }

    @Override
    public byte getBulletType() { return RoundaboutBulletEntity.TOMMY_GUN; }

    @Override
    public SoundEvent getFireSound() { return ModSounds.TOMMY_FIRE_EVENT; }

    @Override
    public float getFireSoundVolume() { return 7.0F; }

    @Override
    public float getFireSoundPitch() {
        Random random = new Random();
        return random.nextFloat(1.7F - 1.5F) + 1.5F;
    }

    @Override
    public void applyRecoil(Player player) {
        S2CPacketUtil.gunRecoil(player, "tommy");
    }

    @Override
    public boolean canWhitesnakeUse() {
        return false;
    }

    @Override
    public void configureBulletTrajectory(RoundaboutBulletEntity bullet, LivingEntity shooter) {
        bullet.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 3.0F, 5.0F);
    }

    @Override
    public Vec3 getMuzzleOffset() {
        return new Vec3(1.5, 0.10, -0.15);
    }
}