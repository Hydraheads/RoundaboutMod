package com.example.jjf.zero_gravity;

import net.minecraft.core.Direction;

import java.util.UUID;

public class ZeroGravityState {

    private ZeroGravitySource source = ZeroGravitySource.NONE;
    private UUID ownerUUID;
    private int zeroGravityTicks = 0;
    private int airPressureTicks = 0;
    private int outsideOwnerRangeTicks = 0;
    private float radius = 0.0F;
    private int restorationTicks = 0;
    private int swingImpulseCooldown = 0;
    private int zeroGravityToggleCooldown = 0;
    private int suctionLaunchSoundCooldown = 0;
    private boolean suctioned = false;
    private boolean suctionLaunchSoundEnabled = false;
    private Direction suctionSurface;

    public boolean isZeroGravity() {
        return source != ZeroGravitySource.NONE;
    }

    public boolean isSpread() {
        return source == ZeroGravitySource.SPREAD;
    }

    public boolean isSafeUserZeroGravity() {
        return source == ZeroGravitySource.USER_TOGGLE;
    }

    public ZeroGravitySource getSource() {
        return source;
    }

    public void setSource(ZeroGravitySource source) {
        this.source = source;
        if (source != ZeroGravitySource.SPREAD) {
            resetAirPressureTicks();
            resetOutsideOwnerRangeTicks();
        }
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public int getZeroGravityTicks() {
        return zeroGravityTicks;
    }

    public void incrementZeroGravityTicks() {
        zeroGravityTicks++;
    }

    public void resetZeroGravityTicks() {
        zeroGravityTicks = 0;
    }

    public int getAirPressureTicks() {
        return airPressureTicks;
    }

    public void incrementAirPressureTicks() {
        airPressureTicks++;
    }

    public void setAirPressureTicks(int ticks) {
        airPressureTicks = Math.max(0, ticks);
    }

    public void resetAirPressureTicks() {
        airPressureTicks = 0;
    }

    public int getOutsideOwnerRangeTicks() {
        return outsideOwnerRangeTicks;
    }

    public void incrementOutsideOwnerRangeTicks() {
        outsideOwnerRangeTicks++;
    }

    public void resetOutsideOwnerRangeTicks() {
        outsideOwnerRangeTicks = 0;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getRestorationTicks() {
        return restorationTicks;
    }

    public void setRestorationTicks(int restorationTicks) {
        this.restorationTicks = restorationTicks;
    }

    public boolean isRestoringWeight() {
        return restorationTicks > 0;
    }

    public void tickRestoration() {
        if (restorationTicks > 0) {
            restorationTicks--;
        }
    }

    public int getSwingImpulseCooldown() {
        return swingImpulseCooldown;
    }

    public void setSwingImpulseCooldown(int ticks) {
        swingImpulseCooldown = ticks;
    }

    public void tickSwingImpulseCooldown() {
        if (swingImpulseCooldown > 0) {
            swingImpulseCooldown--;
        }
    }

    public boolean isSuctioned() {
        return suctioned;
    }

    public void setSuctioned(boolean suctioned) {
        this.suctioned = suctioned;
    }

    public Direction getSuctionSurface() {
        return suctionSurface;
    }

    public void setSuctionSurface(Direction suctionSurface) {
        this.suctionSurface = suctionSurface;
    }

    public int getZeroGravityToggleCooldown() {
        return zeroGravityToggleCooldown;
    }

    public void setZeroGravityToggleCooldown(int ticks) {
        zeroGravityToggleCooldown = ticks;
    }

    public void tickZeroGravityToggleCooldown() {
        if (zeroGravityToggleCooldown > 0) {
            zeroGravityToggleCooldown--;
        }
    }

    public int getSuctionLaunchSoundCooldown() {
        return suctionLaunchSoundCooldown;
    }

    public void setSuctionLaunchSoundCooldown(int ticks) {
        suctionLaunchSoundCooldown = ticks;
    }

    public void tickSuctionLaunchSoundCooldown() {
        if (suctionLaunchSoundCooldown > 0) {
            suctionLaunchSoundCooldown--;
        }
    }

    public boolean isSuctionLaunchSoundEnabled() {
        return suctionLaunchSoundEnabled;
    }

    public void setSuctionLaunchSoundEnabled(boolean enabled) {
        suctionLaunchSoundEnabled = enabled;
    }

    public void toggleSuctionLaunchSound() {
        suctionLaunchSoundEnabled = !suctionLaunchSoundEnabled;
    }

    public void clearZeroGravity() {
        source = ZeroGravitySource.NONE;
        ownerUUID = null;
        zeroGravityTicks = 0;
        airPressureTicks = 0;
        outsideOwnerRangeTicks = 0;
        radius = 0.0F;
        suctioned = false;
        suctionSurface = null;
        swingImpulseCooldown = 0;
    }
}
