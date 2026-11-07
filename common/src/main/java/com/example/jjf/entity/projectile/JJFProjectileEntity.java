package com.example.jjf.entity.projectile;

import com.example.jjf.entity.JJFGunpowderCloudEntity;
import com.example.jjf.registry.ModEntities;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravitySource;
import com.example.jjf.zero_gravity.ZeroGravityState;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class JJFProjectileEntity extends ThrowableItemProjectile {

    private static final DustParticleOptions BLUE_TRAIL =
            new DustParticleOptions(
                    new Vector3f(0.10F, 0.45F, 1.0F),
                    0.75F
            );

    private static final java.util.Map<LivingEntity, Long> JJF_GRIT_COOLDOWNS = new java.util.WeakHashMap<>();

    private static final double MAX_RANGE = 48.0D;
    private static final int MAX_LIFE_TICKS = 100;

    private static final double ZERO_G_MAX_HORIZONTAL = 1.2D;
    private static final double ZERO_G_MAX_VERTICAL = 0.8D;
    private static final double ZERO_G_HORIZONTAL_BALANCE = 1.55D;

    private static final float IRON_DAMAGE = 2.0F;
    private static final float SLIME_DAMAGE = 1.0F;
    private static final float ARROW_DAMAGE = 3.0F;
    private static final float GLASS_DAMAGE = 2.5F;
    private static final float AMETHYST_DAMAGE = 3.0F;
    private static final float CONCRETE_DAMAGE = 1.0F;
    private static final float SNOWBALL_DAMAGE = 0.5F;
    private static final float FIRE_CHARGE_DAMAGE = 1.5F;
    private static final float TRIDENT_DAMAGE = 4.0F;
    private static final float TNT_EXPLOSION_POWER = 2.0F;

    private static final int EGG_BLINDNESS_TICKS = 80;
    private static final int SPECTRAL_GLOW_TICKS = 200;
    private static final int GLASS_BLEED_TICKS = 100;
    private static final int AMETHYST_BLEED_TICKS = 140;

    private static final double SLIME_BLOCK_BOUNCE = 0.82D;
    private static final double SLIME_ENTITY_BOUNCE = 0.72D;
    private static final int MAX_SLIME_BOUNCES = 8;

    private Vec3 startPos;
    private double projectileSpeed;
    private boolean initialized;
    private int bounceCount;

    public JJFProjectileEntity(
            EntityType<? extends JJFProjectileEntity> type,
            Level level
    ) {
        super(type, level);
        setNoGravity(true);
    }

    public JJFProjectileEntity(
            Level level,
            LivingEntity owner
    ) {
        super(ModEntities.JJF_PROJECTILE, owner, level);
        setNoGravity(true);
    }

    @Override
    public boolean ignoreExplosion() {
        if (isTnt()) {
            return true;
        }
        return super.ignoreExplosion();
    }

    @Override
    protected Item getDefaultItem() {
        return Items.IRON_NUGGET;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity == getOwner() && tickCount < 10) {
            return false;
        }
        return super.canHitEntity(entity);
    }

    @Override
    public void tick() {
        setNoGravity(true);

        if (!initialized) {
            startPos = position();
            projectileSpeed = getDeltaMovement().length();
            initialized = true;
        }

        super.tick();

        spawnBlueTrail();
        spawnSpecialTrail();

        if (tickCount >= MAX_LIFE_TICKS) {
            discard();
            return;
        }

        if (startPos != null && position().distanceTo(startPos) >= MAX_RANGE) {
            discard();
            return;
        }

        Vec3 motion = getDeltaMovement();

        if (motion.lengthSqr() > 0.0001D && projectileSpeed > 0.0D) {
            setDeltaMovement(motion.normalize().scale(projectileSpeed));
        }
    }

    private void spawnBlueTrail() {
        if (!level().isClientSide()) {
            return;
        }

        Vec3 motion = getDeltaMovement();

        if (motion.lengthSqr() < 0.0001D) {
            return;
        }

        for (int i = 1; i <= 4; i++) {
            double t = i / 4.0D;

            level().addParticle(
                    BLUE_TRAIL,
                    getX() - motion.x * t,
                    getY() - motion.y * t,
                    getZ() - motion.z * t,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }

    private void spawnSpecialTrail() {
        if (!level().isClientSide()) {
            return;
        }

        ParticleOptions particle = null;

        if (isWater()) particle = ParticleTypes.SPLASH;
        else if (isLava() || isFireCharge() || isBlazePowder() || isMagmaCream()) particle = ParticleTypes.FLAME;
        else if (isEnderPearl() || isChorusFruit()) particle = ParticleTypes.PORTAL;
        else if (isGlowInk()) particle = ParticleTypes.GLOW;
        else if (isEchoShard()) particle = ParticleTypes.SCULK_SOUL;

        if (particle != null) {
            level().addParticle(
                    particle,
                    getX(), getY(), getZ(),
                    0.0D, 0.0D, 0.0D
            );
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level().isClientSide()) {
            return;
        }

        Entity target = result.getEntity();

        if (target instanceof JJFGunpowderCloudEntity cloud) {
            cloud.hurt(
                    damageSources().thrown(this, getOwner()),
                    1.0F
            );
            discard();
            return;
        }

        if (target instanceof LivingEntity living) {
            applyZeroGravityImpact(living);
        }

        if (jjf$specialEntityHit(target)) {
            discard();
            return;
        }

        if (isSpawnEgg()) {
            spawnEgg(target.blockPosition());
            discard();
            return;
        }

        if (isTnt()) {
            explodeTnt();
            return;
        }

        if (isGunpowder()) {
            spawnGunpowderCloud();
            discard();
            return;
        }

        if (isWater()) {
            target.clearFire();
            pushTarget(target, 0.35D);
            spawnWaterParticles();
            dropItem(new ItemStack(Items.BUCKET), 0.10D);
            discard();
            return;
        }

        if (isLava()) {
            hurtTarget(target, 1.5F);
            target.setSecondsOnFire(7);
            spawnLavaParticles();
            dropItem(new ItemStack(Items.BUCKET), 0.10D);
            discard();
            return;
        }

        if (isMilk()) {
            if (target instanceof LivingEntity living) {
                living.removeAllEffects();
            }
            spawnWhiteParticles();
            dropItem(new ItemStack(Items.BUCKET), 0.10D);
            discard();
            return;
        }

        if (isTrident()) {
            hurtTarget(target, TRIDENT_DAMAGE);
            dropTrident();
            discard();
            return;
        }

        if (isEnderPearl()) {
            if (target instanceof LivingEntity living) {
                teleportAlongMotion(living, 6.0D);
            }
            spawnPortalParticles();
            discard();
            return;
        }

        if (isChorusFruit()) {
            if (target instanceof LivingEntity living) {
                randomTeleport(living, 8.0D);
            }
            spawnPortalParticles();
            discard();
            return;
        }

        if (isCobweb()) {
            applyEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 60, 3);
            dampenTarget(target, 0.20D);
            spawnItemParticles(Items.COBWEB, 8);
            discard();
            return;
        }

        if (isNormalPotion()) {
            if (target instanceof LivingEntity living) {
                applyDirectPotion(living, 1.0D);
            }
            spawnPotionParticles();
            discard();
            return;
        }

        if (isSplashPotion()) {
            splashPotion();
            discard();
            return;
        }

        if (isLingeringPotion()) {
            spawnLingeringPotion();
            discard();
            return;
        }

        if (isTippedArrow()) {
            hurtTarget(target, ARROW_DAMAGE);
            if (target instanceof LivingEntity living) {
                applyDirectPotion(living, 0.50D);
            }
            discard();
            return;
        }

        if (isEgg()) {
            splatterEgg();
            applyEffect(target, MobEffects.BLINDNESS, EGG_BLINDNESS_TICKS, 0);
            discard();
            return;
        }

        if (isInk()) {
            applyEffect(target, MobEffects.BLINDNESS, 70, 0);
            spawnInkCloud();
            discard();
            return;
        }

        if (isGlowInk()) {
            applyEffect(target, MobEffects.GLOWING, 200, 0);
            spawnItemParticles(Items.GLOW_INK_SAC, 12);
            discard();
            return;
        }

        if (isCoal()) {
            spawnSmokeCloud();
            discard();
            return;
        }

        if (isBlazePowder()) {
            hurtTarget(target, 1.0F);
            target.setSecondsOnFire(5);
            igniteNearby(2.5D, 3);
            spawnFlameParticles();
            discard();
            return;
        }

        if (isMagmaCream()) {
            hurtTarget(target, 1.0F);
            target.setSecondsOnFire(4);
            spawnFlameParticles();
            bounceFromEntity();
            return;
        }

        if (isHoneyBottle()) {
            applyStickyEffect(target, 0.30D, 100);
            dropItem(new ItemStack(Items.GLASS_BOTTLE), 0.08D);
            discard();
            return;
        }

        if (isHoneycomb()) {
            applyStickyEffect(target, 0.18D, 80);
            discard();
            return;
        }

        if (isPufferfish()) {
            hurtTarget(target, 1.0F);
            applyEffect(target, MobEffects.POISON, 100, 0);
            discard();
            return;
        }

        if (isSpiderEye()) {
            hurtTarget(target, 0.5F);
            applyEffect(target, MobEffects.POISON, 80, 0);
            discard();
            return;
        }

        if (isFermentedSpiderEye()) {
            hurtTarget(target, 0.5F);
            applyEffect(target, MobEffects.WEAKNESS, 120, 0);
            discard();
            return;
        }

        if (isRottenFlesh()) {
            hurtTarget(target, 0.5F);
            applyEffect(target, MobEffects.HUNGER, 160, 0);
            discard();
            return;
        }

        if (isGlowstone()) {
            flashNearby();
            discard();
            return;
        }

        if (isRedstone()) {
            spawnRedstoneParticles();
            discard();
            return;
        }

        if (isFirework()) {
            hurtTarget(target, 1.0F);
            radialPush(3.0D, 0.55D);
            spawnFirework();
            discard();
            return;
        }

        if (isDragonBreath()) {
            spawnDragonBreathCloud();
            dropItem(new ItemStack(Items.GLASS_BOTTLE), 0.08D);
            discard();
            return;
        }

        if (isPointedDripstone()) {
            hurtTarget(target, 3.0F);
            applyBleed(target, 80, 0);
            spawnItemParticles(Items.POINTED_DRIPSTONE, 10);
            discard();
            return;
        }

        if (isObsidian()) {
            hurtTarget(target, 4.0F);
            pushTarget(target, 0.75D);
            spawnItemParticles(Items.OBSIDIAN, 12);
            discard();
            return;
        }

        if (isAnvil()) {
            hurtTarget(target, 5.0F);
            pushTarget(target, 0.95D);
            level().playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ANVIL_LAND,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
            discard();
            return;
        }

        if (isFeather()) {
            hurtTarget(target, 0.5F);
            pushTarget(target, 0.18D);
            spawnItemParticles(Items.FEATHER, 8);
            discard();
            return;
        }

        if (isPrismarineShard()) {
            float damage = target.isInWaterOrBubble() ? 3.0F : 2.5F;
            hurtTarget(target, damage);
            applyBleed(target, 60, 0);
            spawnNautilusParticles();
            discard();
            return;
        }

        if (isNautilusShell()) {
            hurtTarget(target, 2.0F);
            pushTarget(target, 0.65D);
            spawnNautilusParticles();
            discard();
            return;
        }

        if (isEchoShard()) {
            spawnSonicImpact();
            radialPush(4.0D, 0.80D);
            discard();
            return;
        }

        if (isSand()) {
            spawnDustCloud(getDustState(), 2.6F, 65, 45, false);
            discard();
            return;
        }

        if (isConcretePowder()) {
            hurtTarget(target, CONCRETE_DAMAGE);
            spawnDustCloud(getDustState(), 1.8F, 50, 25, true);
            discard();
            return;
        }

        if (isFireCharge()) {
            hurtTarget(target, FIRE_CHARGE_DAMAGE);
            target.setSecondsOnFire(6);
            spawnFlameParticles();
            discard();
            return;
        }

        if (isSnowball()) {
            hurtTarget(target, SNOWBALL_DAMAGE);
            applySnowballKnockback(target);
            spawnItemParticles(Items.SNOWBALL, 8);
            discard();
            return;
        }

        if (isGlass()) {
            hurtTarget(target, GLASS_DAMAGE);
            applyBleed(target, GLASS_BLEED_TICKS, 0);
            spawnGlassParticles();
            discard();
            return;
        }

        if (isAmethyst()) {
            hurtTarget(target, AMETHYST_DAMAGE);
            applyBleed(target, AMETHYST_BLEED_TICKS, 1);
            spawnItemParticles(Items.AMETHYST_SHARD, 12);
            discard();
            return;
        }

        if (isJJFExclusiveItem()) {
            hurtTarget(target, 2.0F);
            spawnJJFParticles();
            discard();
            return;
        }

        float damage = isArrow()
                ? ARROW_DAMAGE
                : isSlime()
                ? SLIME_DAMAGE
                : IRON_DAMAGE;

        hurtTarget(target, damage);

        if (isSpectralArrow()) {
            applyEffect(target, MobEffects.GLOWING, SPECTRAL_GLOW_TICKS, 0);
        }

        if (isSlime()) {
            bounceFromEntity();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (level().isClientSide()) {
            return;
        }

        if (jjf$specialBlockHit(result)) {
            discard();
            return;
        }

        if (isSpawnEgg()) {
            spawnEgg(result.getBlockPos().relative(result.getDirection()));
            discard();
            return;
        }

        if (isTnt()) {
            explodeTnt();
            return;
        }

        if (isGunpowder()) {
            spawnGunpowderCloud();
            discard();
            return;
        }

        if (isWater()) {
            placeWater(result);
            spawnWaterParticles();
            dropItem(new ItemStack(Items.BUCKET), 0.10D);
            discard();
            return;
        }

        if (isLava()) {
            placeLava(result);
            spawnLavaParticles();
            dropItem(new ItemStack(Items.BUCKET), 0.10D);
            discard();
            return;
        }

        if (isMilk()) {
            cleanseNearby(3.0D);
            spawnWhiteParticles();
            dropItem(new ItemStack(Items.BUCKET), 0.10D);
            discard();
            return;
        }

        if (isTrident()) {
            dropTrident();
            discard();
            return;
        }

        if (isEnderPearl()) {
            teleportOwnerToImpact(result);
            spawnPortalParticles();
            discard();
            return;
        }

        if (isChorusFruit()) {
            if (getOwner() instanceof LivingEntity living) {
                randomTeleport(living, 8.0D);
            }
            spawnPortalParticles();
            discard();
            return;
        }

        if (isCobweb()) {
            placeCobweb(result);
            discard();
            return;
        }

        if (isNormalPotion()) {
            spawnPotionParticles();
            discard();
            return;
        }

        if (isSplashPotion()) {
            splashPotion();
            discard();
            return;
        }

        if (isLingeringPotion()) {
            spawnLingeringPotion();
            discard();
            return;
        }

        if (isInk()) {
            spawnInkCloud();
            discard();
            return;
        }

        if (isGlowInk()) {
            spawnItemParticles(Items.GLOW_INK_SAC, 12);
            discard();
            return;
        }

        if (isCoal()) {
            spawnSmokeCloud();
            discard();
            return;
        }

        if (isBlazePowder()) {
            igniteNearby(2.5D, 3);
            igniteBlock(result);
            spawnFlameParticles();
            discard();
            return;
        }

        if (isMagmaCream()) {
            igniteBlock(result);
            spawnFlameParticles();
            bounceFromBlock(result);
            return;
        }

        if (isHoneyBottle() || isHoneycomb()) {
            spawnHoneyCloud();
            if (isHoneyBottle()) {
                dropItem(new ItemStack(Items.GLASS_BOTTLE), 0.08D);
            }
            discard();
            return;
        }

        if (isGlowstone()) {
            flashNearby();
            discard();
            return;
        }

        if (isRedstone()) {
            pulseRedstone(result);
            discard();
            return;
        }

        if (isFirework()) {
            radialPush(3.0D, 0.55D);
            spawnFirework();
            discard();
            return;
        }

        if (isDragonBreath()) {
            spawnDragonBreathCloud();
            dropItem(new ItemStack(Items.GLASS_BOTTLE), 0.08D);
            discard();
            return;
        }

        if (isAnvil()) {
            level().playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ANVIL_LAND,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
            discard();
            return;
        }

        if (isEchoShard()) {
            spawnSonicImpact();
            radialPush(4.0D, 0.80D);
            discard();
            return;
        }

        if (isPrismarineShard() || isNautilusShell()) {
            spawnNautilusParticles();
            discard();
            return;
        }

        if (isEgg()) {
            splatterEgg();
            discard();
            return;
        }

        if (isSand()) {
            spawnDustCloud(getDustState(), 2.6F, 65, 45, false);
            discard();
            return;
        }

        if (isConcretePowder()) {
            spawnDustCloud(getDustState(), 1.8F, 50, 25, true);
            discard();
            return;
        }

        if (isFireCharge()) {
            igniteBlock(result);
            spawnFlameParticles();
            discard();
            return;
        }

        if (isSnowball()) {
            spawnItemParticles(Items.SNOWBALL, 8);
            discard();
            return;
        }

        if (isGlass()) {
            spawnGlassParticles();
            discard();
            return;
        }

        if (isAmethyst()) {
            spawnItemParticles(Items.AMETHYST_SHARD, 12);
            discard();
            return;
        }

        if (isJJFExclusiveItem()) {
            spawnJJFParticles();
            discard();
            return;
        }

        if (isSlime()) {
            bounceFromBlock(result);
            return;
        }

        super.onHitBlock(result);
    }

    private boolean jjf$specialEntityHit(Entity target) {
        ItemStack ammo = getItem();
        if (ammo.is(Items.DIRT) || ammo.is(Items.COARSE_DIRT)) {
            applyEffect(target, MobEffects.BLINDNESS, ammo.is(Items.DIRT) ? 40 : 20, 0);
            if (target instanceof LivingEntity living) {
                long now = living.level().getGameTime();
                if (now - JJF_GRIT_COOLDOWNS.getOrDefault(living, -100L) >= 20L) {
                    JJF_GRIT_COOLDOWNS.put(living, now);
                    for (net.minecraft.world.entity.EquipmentSlot slot : new net.minecraft.world.entity.EquipmentSlot[]{
                            net.minecraft.world.entity.EquipmentSlot.HEAD,
                            net.minecraft.world.entity.EquipmentSlot.CHEST,
                            net.minecraft.world.entity.EquipmentSlot.LEGS,
                            net.minecraft.world.entity.EquipmentSlot.FEET}) {
                        ItemStack armor = living.getItemBySlot(slot);
                        if (armor.isDamageableItem()) armor.hurtAndBreak(
                                ammo.is(Items.DIRT) ? 1 : 2, living,
                                entity -> entity.broadcastBreakEvent(slot));
                    }
                }
            }
        } else if (ammo.is(Items.GRAVEL)) {
            hurtTarget(target, 1.5F);
            pushTarget(target, 0.40D);
        } else if (ammo.is(Items.CLAY_BALL)) {
            applyEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 50, 1);
        } else if (ammo.is(Items.BRICK) || ammo.is(Items.NETHER_BRICK)) {
            hurtTarget(target, 2.5F);
            pushTarget(target, 0.55D);
            if (ammo.is(Items.NETHER_BRICK)) target.setSecondsOnFire(2);
        } else if (ammo.is(Items.BONE)) {
            hurtTarget(target, target.getType().getCategory() == net.minecraft.world.entity.MobCategory.MONSTER ? 2.5F : 1.0F);
            pushTarget(target, 0.40D);
        } else if (ammo.is(Items.BONE_MEAL)) {
            applyEffect(target, MobEffects.CONFUSION, 40, 0);
        } else if (ammo.is(Items.FLINT)) {
            hurtTarget(target, 1.0F);
            applyBleed(target, 55, 0);
        } else if (ammo.is(Items.CACTUS)) {
            hurtTarget(target, 1.0F);
            applyEffect(target, MobEffects.POISON, 50, 0);
        } else if (ammo.is(Items.SWEET_BERRIES)) {
            hurtTarget(target, 0.5F);
            applyEffect(target, MobEffects.MOVEMENT_SLOWDOWN, 25, 0);
        } else if (ammo.is(Items.GLOW_BERRIES)) {
            hurtTarget(target, 0.5F);
            applyEffect(target, MobEffects.GLOWING, 80, 0);
        } else if (ammo.is(Items.SLIME_BLOCK)) {
            pushTarget(target, 1.0D);
        } else if (ammo.is(Items.SPONGE)) {
            pushTarget(target, 0.30D);
        } else if (ammo.is(Items.WET_SPONGE)) {
            target.clearFire();
            pushTarget(target, 0.45D);
        } else if (ammo.is(Items.SCUTE)) {
            hurtTarget(target, 1.5F);
            pushTarget(target, 0.35D);
        } else if (ammo.is(Items.RABBIT_FOOT)) {
            if (getOwner() instanceof LivingEntity living)
                living.addEffect(new MobEffectInstance(MobEffects.JUMP, 90, 0));
        } else if (ammo.is(Items.PHANTOM_MEMBRANE)) {
            applyEffect(target, MobEffects.SLOW_FALLING, 90, 0);
        } else if (ammo.is(Items.SHULKER_SHELL)) {
            applyEffect(target, MobEffects.LEVITATION, 30, 0);
        } else if (ammo.is(Items.END_ROD)) {
            applyEffect(target, MobEffects.GLOWING, 100, 0);
        } else if (ammo.is(Items.BASALT)) {
            hurtTarget(target, 2.5F);
            pushTarget(target, 0.90D);
        } else if (ammo.is(Items.LIGHTNING_ROD)) {
            if (level().isThundering() && level().canSeeSky(target.blockPosition())
                    && level() instanceof ServerLevel server) {
                net.minecraft.world.entity.LightningBolt bolt = net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(server);
                if (bolt != null) {
                    bolt.moveTo(target.position());
                    server.addFreshEntity(bolt);
                }
            } else {
                applyEffect(target, MobEffects.GLOWING, 45, 0);
            }
        } else if (ammo.is(Items.COPPER_INGOT)) {
            hurtTarget(target, target instanceof LivingEntity living && living.getArmorValue() > 0 ? 3.0F : 1.5F);
        } else if (ammo.is(Items.CHAIN) || ammo.is(Items.FISHING_ROD)) {
            if (getOwner() instanceof LivingEntity owner && target != owner) {
                Vec3 pull = owner.position().subtract(target.position());
                if (pull.lengthSqr() > 0.001D) {
                    target.push(pull.normalize().x * 0.75D, 0.12D, pull.normalize().z * 0.75D);
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean jjf$specialBlockHit(BlockHitResult hit) {
        ItemStack ammo = getItem();
        if (ammo.is(Items.END_ROD)) {
            if (level() instanceof ServerLevel server)
                server.sendParticles(ParticleTypes.END_ROD, getX(), getY(), getZ(), 12, 0.2D, 0.2D, 0.2D, 0.02D);
            return true;
        }
        if (ammo.is(Items.WET_SPONGE)) {
            spawnWaterParticles();
            return true;
        }
        if (ammo.is(Items.LIGHTNING_ROD) && level().isThundering()
                && level().canSeeSky(hit.getBlockPos().above())
                && level() instanceof ServerLevel server) {
            net.minecraft.world.entity.LightningBolt bolt = net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(server);
            if (bolt != null) {
                bolt.moveTo(Vec3.atCenterOf(hit.getBlockPos().above()));
                server.addFreshEntity(bolt);
            }
            return true;
        }
        return false;
    }

    private void hurtTarget(Entity target, float damage) {
        target.hurt(
                damageSources().thrown(this, getOwner()),
                damage
        );
    }

    private void applyZeroGravityImpact(LivingEntity target) {

        if (!(target instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();


        if (state.getSource()
                != ZeroGravitySource.SPREAD) {
            return;
        }


        Vec3 motion =
                getDeltaMovement();

        if (motion.lengthSqr() < 0.0001D) {
            return;
        }

        double strength =
                getZeroGravityImpactStrength();

        Vec3 direction =
                motion.normalize();

        Vec3 current =
                target.getDeltaMovement();

        Vec3 result =
                current.add(
                        direction.x
                                * strength
                                * ZERO_G_HORIZONTAL_BALANCE,

                        direction.y
                                * strength,

                        direction.z
                                * strength
                                * ZERO_G_HORIZONTAL_BALANCE
                );


        double x = clamp(
                result.x,
                -ZERO_G_MAX_HORIZONTAL,
                ZERO_G_MAX_HORIZONTAL
        );

        double y = clamp(
                result.y,
                -ZERO_G_MAX_VERTICAL,
                ZERO_G_MAX_VERTICAL
        );

        double z = clamp(
                result.z,
                -ZERO_G_MAX_HORIZONTAL,
                ZERO_G_MAX_HORIZONTAL
        );

        target.setDeltaMovement(
                x,
                y,
                z
        );

        target.hasImpulse = true;
        target.hurtMarked = true;

        target.fallDistance = 0.0F;
    }

    private double getZeroGravityImpactStrength() {
        if (isEchoShard()) return 0.36D;
        if (isObsidian() || isAnvil()) return 0.35D;
        if (isSnowball() || isNautilusShell()) return 0.30D;
        if (isFeather()) return 0.28D;
        if (isTrident()) return 0.24D;
        if (isMagmaCream()) return 0.23D;
        if (isSlime()) return 0.22D;
        if (isWater() || isPrismarineShard()) return 0.20D;
        if (isPointedDripstone()) return 0.19D;
        if (isAmethyst() || isConcretePowder()) return 0.18D;
        if (isArrow()) return 0.17D;
        if (isGlass()) return 0.16D;
        if (isFireCharge() || isBlazePowder() || isLava()) return 0.14D;
        if (isIron()) return 0.14D;
        if (isEgg()) return 0.10D;
        if (isTnt()) return 0.05D;
        return 0.08D;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private void applyEffect(
            Entity target,
            MobEffect effect,
            int duration,
            int amplifier
    ) {
        if (!(target instanceof LivingEntity living)) {
            return;
        }

        living.addEffect(
                new MobEffectInstance(effect, duration, amplifier),
                getOwner()
        );
    }

    private void applyBleed(Entity target, int duration, int amplifier) {
        if (!(target instanceof LivingEntity living)) {
            return;
        }

        living.addEffect(
                new MobEffectInstance(
                        ModEffects.BLEED,
                        duration,
                        amplifier
                ),
                getOwner()
        );
    }

    private void applyStickyEffect(Entity target, double motionScale, int duration) {
        dampenTarget(target, motionScale);
        applyEffect(target, MobEffects.MOVEMENT_SLOWDOWN, duration, 2);
    }

    private void dampenTarget(Entity target, double scale) {
        target.setDeltaMovement(target.getDeltaMovement().scale(scale));
        target.hasImpulse = true;
    }

    private void pushTarget(Entity target, double strength) {
        Vec3 motion = getDeltaMovement();

        if (motion.lengthSqr() < 0.0001D) {
            return;
        }

        Vec3 push = motion.normalize().scale(strength);

        target.push(
                push.x,
                Math.max(0.06D, push.y * 0.40D),
                push.z
        );
    }

    private void radialPush(double radius, double strength) {
        AABB area = getBoundingBox().inflate(radius);

        for (Entity entity : level().getEntities(
                this,
                area,
                entity -> entity != getOwner()
        )) {
            Vec3 direction = entity.position().subtract(position());
            double distance = direction.length();

            if (distance <= 0.001D || distance > radius) {
                continue;
            }

            double scale = 1.0D - distance / radius;
            Vec3 push = direction.normalize().scale(strength * scale);

            entity.push(
                    push.x,
                    Math.max(0.05D, push.y),
                    push.z
            );
        }
    }

    private void applySnowballKnockback(Entity target) {
        Vec3 motion = getDeltaMovement();

        if (motion.lengthSqr() < 0.0001D) {
            return;
        }

        Vec3 push = motion.normalize().scale(0.75D);

        target.push(
                push.x,
                Math.max(0.12D, push.y * 0.35D),
                push.z
        );
    }

    private void igniteNearby(double radius, int seconds) {
        AABB area = getBoundingBox().inflate(radius);

        for (LivingEntity living : level().getEntitiesOfClass(
                LivingEntity.class,
                area
        )) {
            living.setSecondsOnFire(seconds);
        }
    }

    private void applyDirectPotion(LivingEntity target, double strength) {
        for (MobEffectInstance effect : PotionUtils.getMobEffects(getItem())) {
            if (effect.getEffect().isInstantenous()) {
                effect.getEffect().applyInstantenousEffect(
                        getOwner(),
                        getOwner(),
                        target,
                        effect.getAmplifier(),
                        strength
                );
                continue;
            }

            int duration = Math.max(
                    1,
                    (int) (effect.getDuration() * strength)
            );

            target.addEffect(
                    new MobEffectInstance(
                            effect.getEffect(),
                            duration,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    ),
                    getOwner()
            );
        }
    }

    private void splashPotion() {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        double radius = 4.0D;
        AABB area = getBoundingBox().inflate(radius);

        for (LivingEntity living : level().getEntitiesOfClass(
                LivingEntity.class,
                area
        )) {
            double distance = Math.sqrt(distanceToSqr(living));

            if (distance > radius) {
                continue;
            }

            double strength = 1.0D - distance / radius;

            for (MobEffectInstance effect : PotionUtils.getMobEffects(getItem())) {
                if (effect.getEffect().isInstantenous()) {
                    effect.getEffect().applyInstantenousEffect(
                            getOwner(),
                            getOwner(),
                            living,
                            effect.getAmplifier(),
                            strength
                    );
                    continue;
                }

                int duration = (int) (
                        effect.getDuration()
                                * 0.75D
                                * strength
                );

                if (duration < 20) {
                    continue;
                }

                living.addEffect(
                        new MobEffectInstance(
                                effect.getEffect(),
                                duration,
                                effect.getAmplifier(),
                                effect.isAmbient(),
                                effect.isVisible(),
                                effect.showIcon()
                        ),
                        getOwner()
                );
            }
        }

        server.sendParticles(
                ParticleTypes.EFFECT,
                getX(), getY(), getZ(),
                24,
                0.55D, 0.35D, 0.55D,
                0.08D
        );
    }

    private void spawnLingeringPotion() {
        AreaEffectCloud cloud = new AreaEffectCloud(
                level(),
                getX(),
                getY(),
                getZ()
        );

        if (getOwner() instanceof LivingEntity owner) {
            cloud.setOwner(owner);
        }

        cloud.setRadius(3.0F);
        cloud.setDuration(160);
        cloud.setWaitTime(0);
        cloud.setRadiusPerTick(-3.0F / 160.0F);
        cloud.setParticle(ParticleTypes.ENTITY_EFFECT);

        for (MobEffectInstance effect : PotionUtils.getMobEffects(getItem())) {
            int duration = Math.max(20, effect.getDuration() / 4);

            cloud.addEffect(
                    new MobEffectInstance(
                            effect.getEffect(),
                            duration,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    )
            );
        }

        level().addFreshEntity(cloud);
    }

    private void spawnEffectCloud(
            ParticleOptions particle,
            float radius,
            int duration,
            MobEffectInstance effect
    ) {
        AreaEffectCloud cloud = new AreaEffectCloud(
                level(),
                getX(),
                getY(),
                getZ()
        );

        if (getOwner() instanceof LivingEntity owner) {
            cloud.setOwner(owner);
        }

        cloud.setRadius(radius);
        cloud.setDuration(duration);
        cloud.setWaitTime(0);
        cloud.setRadiusPerTick(
                -radius / Math.max(1.0F, duration)
        );
        cloud.setParticle(particle);

        if (effect != null) {
            cloud.addEffect(effect);
        }

        level().addFreshEntity(cloud);
    }

    private void spawnInkCloud() {
        spawnEffectCloud(
                ParticleTypes.SQUID_INK,
                2.5F,
                70,
                new MobEffectInstance(
                        MobEffects.BLINDNESS,
                        60,
                        0
                )
        );
    }

    private void spawnSmokeCloud() {
        spawnEffectCloud(
                ParticleTypes.LARGE_SMOKE,
                3.0F,
                80,
                new MobEffectInstance(
                        MobEffects.BLINDNESS,
                        45,
                        0
                )
        );
    }

    private void spawnHoneyCloud() {
        spawnEffectCloud(
                ParticleTypes.FALLING_HONEY,
                2.0F,
                70,
                new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        60,
                        2
                )
        );
    }

    private void spawnDragonBreathCloud() {
        spawnEffectCloud(
                ParticleTypes.DRAGON_BREATH,
                3.0F,
                100,
                new MobEffectInstance(
                        MobEffects.HARM,
                        1,
                        0
                )
        );
    }

    private void flashNearby() {
        AABB area = getBoundingBox().inflate(3.5D);

        for (LivingEntity living : level().getEntitiesOfClass(
                LivingEntity.class,
                area
        )) {
            living.addEffect(
                    new MobEffectInstance(
                            MobEffects.BLINDNESS,
                            25,
                            0
                    ),
                    getOwner()
            );

            living.addEffect(
                    new MobEffectInstance(
                            MobEffects.GLOWING,
                            120,
                            0
                    ),
                    getOwner()
            );
        }

        sendParticles(
                ParticleTypes.END_ROD,
                30,
                0.45D,
                0.45D,
                0.45D,
                0.15D
        );
    }

    private void cleanseNearby(double radius) {
        AABB area = getBoundingBox().inflate(radius);

        for (LivingEntity living : level().getEntitiesOfClass(
                LivingEntity.class,
                area
        )) {
            living.removeAllEffects();
        }
    }

    private void spawnEgg(BlockPos pos) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        ItemStack stack = getItem();

        if (!(stack.getItem() instanceof SpawnEggItem egg)) {
            return;
        }

        EntityType<?> type = egg.getType(stack.getTag());
        Player player = getOwner() instanceof Player p ? p : null;

        type.spawn(
                server,
                stack,
                player,
                pos,
                MobSpawnType.DISPENSER,
                true,
                false
        );
    }

    private void teleportAlongMotion(LivingEntity target, double maxDistance) {
        Vec3 motion = getDeltaMovement();

        if (motion.lengthSqr() < 0.0001D) {
            return;
        }

        Vec3 direction = motion.normalize();

        for (double distance = maxDistance;
             distance >= 2.0D;
             distance -= 1.0D) {

            Vec3 destination = target.position().add(
                    direction.scale(distance)
            );

            if (target.randomTeleport(
                    destination.x,
                    destination.y,
                    destination.z,
                    true
            )) {
                return;
            }
        }
    }

    private void randomTeleport(LivingEntity target, double radius) {
        for (int i = 0; i < 12; i++) {
            double x = target.getX()
                    + (random.nextDouble() - 0.5D)
                    * radius
                    * 2.0D;

            double y = target.getY()
                    + random.nextInt(7)
                    - 3;

            double z = target.getZ()
                    + (random.nextDouble() - 0.5D)
                    * radius
                    * 2.0D;

            if (target.randomTeleport(x, y, z, true)) {
                return;
            }
        }
    }

    private void teleportOwnerToImpact(BlockHitResult result) {
        if (!(getOwner() instanceof LivingEntity owner)) {
            return;
        }

        Direction face = result.getDirection();
        Vec3 destination = result.getLocation().add(
                face.getStepX() * 0.75D,
                face.getStepY() * 0.75D,
                face.getStepZ() * 0.75D
        );

        owner.randomTeleport(
                destination.x,
                destination.y,
                destination.z,
                true
        );
    }

    private void placeCobweb(BlockHitResult result) {
        BlockPos pos = result.getBlockPos().relative(result.getDirection());

        if (!level().isEmptyBlock(pos)) {
            return;
        }

        level().setBlockAndUpdate(
                pos,
                Blocks.COBWEB.defaultBlockState()
        );
    }

    private void pulseRedstone(BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        BlockState state = level().getBlockState(pos);

        if (getOwner() instanceof Player player
                && (state.getBlock() instanceof ButtonBlock
                || state.getBlock() instanceof LeverBlock)) {
            state.use(
                    level(),
                    player,
                    InteractionHand.MAIN_HAND,
                    result
            );
        }

        level().updateNeighborsAt(
                pos,
                state.getBlock()
        );

        spawnRedstoneParticles();
    }

    private void spawnFirework() {
        ItemStack stack = getItem().copy();
        stack.setCount(1);

        FireworkRocketEntity rocket =
                new FireworkRocketEntity(
                        level(),
                        stack,
                        getX(),
                        getY(),
                        getZ(),
                        true
                );

        Vec3 motion = getDeltaMovement();

        if (motion.lengthSqr() > 0.0001D) {
            rocket.setDeltaMovement(
                    motion.normalize().scale(0.20D)
            );
        }

        level().addFreshEntity(rocket);
    }

    private void bounceFromBlock(BlockHitResult result) {
        Vec3 motion = getDeltaMovement();
        Direction direction = result.getDirection();
        Vec3 bounced;

        switch (direction.getAxis()) {
            case X -> bounced = new Vec3(
                    -motion.x,
                    motion.y,
                    motion.z
            );
            case Y -> bounced = new Vec3(
                    motion.x,
                    -motion.y,
                    motion.z
            );
            case Z -> bounced = new Vec3(
                    motion.x,
                    motion.y,
                    -motion.z
            );
            default -> bounced = motion.scale(-1.0D);
        }

        bounced = bounced.scale(SLIME_BLOCK_BOUNCE);
        applySlimeBounce(bounced);

        if (isRemoved()) {
            return;
        }

        Vec3 normal = new Vec3(
                direction.getStepX(),
                direction.getStepY(),
                direction.getStepZ()
        );

        setPos(
                getX() + normal.x * 0.06D,
                getY() + normal.y * 0.06D,
                getZ() + normal.z * 0.06D
        );
    }

    private void bounceFromEntity() {
        Vec3 motion = getDeltaMovement();
        Vec3 bounced = motion.scale(-SLIME_ENTITY_BOUNCE);

        bounced = bounced.add(
                (random.nextDouble() - 0.5D) * 0.18D,
                (random.nextDouble() - 0.5D) * 0.18D,
                (random.nextDouble() - 0.5D) * 0.18D
        );

        applySlimeBounce(bounced);

        if (isRemoved() || bounced.lengthSqr() < 0.0001D) {
            return;
        }

        Vec3 away = bounced.normalize().scale(0.20D);

        setPos(
                getX() + away.x,
                getY() + away.y,
                getZ() + away.z
        );
    }

    private void applySlimeBounce(Vec3 bounced) {
        bounceCount++;

        if (bounceCount > MAX_SLIME_BOUNCES
                || bounced.lengthSqr() < 0.04D) {
            discard();
            return;
        }

        projectileSpeed = bounced.length();
        setDeltaMovement(bounced);
        hasImpulse = true;
    }

    private void splatterEgg() {
        sendParticles(
                ParticleTypes.EGG_CRACK,
                12,
                0.20D,
                0.20D,
                0.20D,
                0.08D
        );
    }

    private void spawnFlameParticles() {
        sendParticles(
                ParticleTypes.FLAME,
                12,
                0.20D,
                0.20D,
                0.20D,
                0.04D
        );
    }

    private void spawnWaterParticles() {
        sendParticles(
                ParticleTypes.SPLASH,
                24,
                0.35D,
                0.25D,
                0.35D,
                0.12D
        );

        sendParticles(
                ParticleTypes.BUBBLE,
                12,
                0.25D,
                0.20D,
                0.25D,
                0.06D
        );
    }

    private void spawnLavaParticles() {
        sendParticles(
                ParticleTypes.LAVA,
                12,
                0.25D,
                0.20D,
                0.25D,
                0.04D
        );

        sendParticles(
                ParticleTypes.FLAME,
                10,
                0.20D,
                0.20D,
                0.20D,
                0.04D
        );
    }

    private void spawnSteamParticles() {
        sendParticles(
                ParticleTypes.LARGE_SMOKE,
                18,
                0.35D,
                0.35D,
                0.35D,
                0.04D
        );
    }

    private void spawnGlassParticles() {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        server.sendParticles(
                new BlockParticleOption(
                        ParticleTypes.BLOCK,
                        Blocks.GLASS.defaultBlockState()
                ),
                getX(), getY(), getZ(),
                16,
                0.25D, 0.25D, 0.25D,
                0.12D
        );
    }

    private void spawnItemParticles(Item item, int count) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        server.sendParticles(
                new ItemParticleOption(
                        ParticleTypes.ITEM,
                        new ItemStack(item)
                ),
                getX(), getY(), getZ(),
                count,
                0.22D, 0.22D, 0.22D,
                0.08D
        );
    }

    private void spawnPotionParticles() {
        sendParticles(
                ParticleTypes.EFFECT,
                20,
                0.30D,
                0.30D,
                0.30D,
                0.10D
        );
    }

    private void spawnWhiteParticles() {
        sendParticles(
                ParticleTypes.CLOUD,
                18,
                0.30D,
                0.20D,
                0.30D,
                0.08D
        );
    }

    private void spawnPortalParticles() {
        sendParticles(
                ParticleTypes.PORTAL,
                24,
                0.30D,
                0.30D,
                0.30D,
                0.20D
        );
    }

    private void spawnRedstoneParticles() {
        sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                16,
                0.25D,
                0.25D,
                0.25D,
                0.08D
        );
    }

    private void spawnNautilusParticles() {
        sendParticles(
                ParticleTypes.NAUTILUS,
                16,
                0.25D,
                0.25D,
                0.25D,
                0.08D
        );
    }

    private void spawnJJFParticles() {
        sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                16,
                0.22D,
                0.22D,
                0.22D,
                0.10D
        );
    }

    private void spawnSonicImpact() {
        sendParticles(
                ParticleTypes.SONIC_BOOM,
                1,
                0.0D,
                0.0D,
                0.0D,
                0.0D
        );

        level().playSound(
                null,
                blockPosition(),
                SoundEvents.WARDEN_SONIC_BOOM,
                SoundSource.PLAYERS,
                0.8F,
                1.4F
        );
    }

    private void sendParticles(
            ParticleOptions particle,
            int count,
            double xSpread,
            double ySpread,
            double zSpread,
            double speed
    ) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        server.sendParticles(
                particle,
                getX(), getY(), getZ(),
                count,
                xSpread,
                ySpread,
                zSpread,
                speed
        );
    }

    private void spawnDustCloud(
            BlockState particleState,
            float radius,
            int duration,
            int blindnessTicks,
            boolean slows
    ) {
        if (level().isClientSide()) {
            return;
        }

        AreaEffectCloud cloud = new AreaEffectCloud(
                level(),
                getX(),
                getY(),
                getZ()
        );

        cloud.setRadius(radius);
        cloud.setDuration(duration);
        cloud.setWaitTime(0);
        cloud.setRadiusPerTick(
                -radius / Math.max(1.0F, duration)
        );
        cloud.setParticle(
                new BlockParticleOption(
                        ParticleTypes.FALLING_DUST,
                        particleState
                )
        );
        cloud.addEffect(
                new MobEffectInstance(
                        MobEffects.BLINDNESS,
                        blindnessTicks,
                        0
                )
        );

        if (slows) {
            cloud.addEffect(
                    new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN,
                            40,
                            0
                    )
            );
        }

        level().addFreshEntity(cloud);
    }

    private void spawnGunpowderCloud() {
        if (level().isClientSide()) {
            return;
        }

        JJFGunpowderCloudEntity cloud =
                new JJFGunpowderCloudEntity(
                        ModEntities.JJF_GUNPOWDER_CLOUD,
                        level()
                );

        cloud.setPos(getX(), getY(), getZ());
        level().addFreshEntity(cloud);
    }

    private void igniteBlock(BlockHitResult result) {
        if (level().isClientSide()) {
            return;
        }

        BlockPos firePos = result.getBlockPos().relative(
                result.getDirection()
        );

        if (!level().isEmptyBlock(firePos)) {
            return;
        }

        level().setBlockAndUpdate(
                firePos,
                Blocks.FIRE.defaultBlockState()
        );
    }

    private void placeWater(BlockHitResult result) {
        if (level().isClientSide()) {
            return;
        }

        if (level().dimensionType().ultraWarm()) {
            spawnSteamParticles();
            return;
        }

        placeFluid(
                result,
                Blocks.WATER.defaultBlockState()
        );
    }

    private void placeLava(BlockHitResult result) {
        if (level().isClientSide()) {
            return;
        }

        placeFluid(
                result,
                Blocks.LAVA.defaultBlockState()
        );
    }

    private void placeFluid(BlockHitResult result, BlockState fluid) {
        BlockPos pos = result.getBlockPos().relative(
                result.getDirection()
        );

        if (!level().isEmptyBlock(pos)) {
            return;
        }

        level().setBlockAndUpdate(pos, fluid);
    }

    private void dropItem(ItemStack stack, double motionScale) {
        if (level().isClientSide()) {
            return;
        }

        ItemEntity dropped = new ItemEntity(
                level(),
                getX(),
                getY(),
                getZ(),
                stack
        );

        dropped.setDeltaMovement(
                getDeltaMovement().scale(motionScale)
        );

        level().addFreshEntity(dropped);
    }

    private void dropTrident() {
        if (level().isClientSide()) {
            return;
        }

        ItemStack trident = getItem().copy();
        trident.setCount(1);

        if (trident.isDamageableItem()) {
            int damage = trident.getDamageValue() + 1;

            if (damage >= trident.getMaxDamage()) {
                spawnItemParticles(Items.TRIDENT, 10);
                return;
            }

            trident.setDamageValue(damage);
        }

        dropItem(trident, 0.12D);
    }

    private void explodeTnt() {
        if (level().isClientSide()) {
            return;
        }

        level().explode(
                this,
                getX(),
                getY(),
                getZ(),
                TNT_EXPLOSION_POWER,
                false,
                Level.ExplosionInteraction.TNT
        );

        discard();
    }

    private BlockState getDustState() {
        ItemStack stack = getItem();

        if (stack.is(Items.RED_SAND)) return Blocks.RED_SAND.defaultBlockState();
        if (stack.is(Items.WHITE_CONCRETE_POWDER)) return Blocks.WHITE_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.ORANGE_CONCRETE_POWDER)) return Blocks.ORANGE_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.MAGENTA_CONCRETE_POWDER)) return Blocks.MAGENTA_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.LIGHT_BLUE_CONCRETE_POWDER)) return Blocks.LIGHT_BLUE_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.YELLOW_CONCRETE_POWDER)) return Blocks.YELLOW_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.LIME_CONCRETE_POWDER)) return Blocks.LIME_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.PINK_CONCRETE_POWDER)) return Blocks.PINK_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.GRAY_CONCRETE_POWDER)) return Blocks.GRAY_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.LIGHT_GRAY_CONCRETE_POWDER)) return Blocks.LIGHT_GRAY_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.CYAN_CONCRETE_POWDER)) return Blocks.CYAN_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.PURPLE_CONCRETE_POWDER)) return Blocks.PURPLE_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.BLUE_CONCRETE_POWDER)) return Blocks.BLUE_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.BROWN_CONCRETE_POWDER)) return Blocks.BROWN_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.GREEN_CONCRETE_POWDER)) return Blocks.GREEN_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.RED_CONCRETE_POWDER)) return Blocks.RED_CONCRETE_POWDER.defaultBlockState();
        if (stack.is(Items.BLACK_CONCRETE_POWDER)) return Blocks.BLACK_CONCRETE_POWDER.defaultBlockState();

        return Blocks.SAND.defaultBlockState();
    }

    private boolean isJJFExclusiveItem() {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(
                getItem().getItem()
        );

        return id != null && "jjf".equals(id.getNamespace());
    }

    private boolean isIron() { return getItem().is(Items.IRON_NUGGET); }
    private boolean isSlime() { return getItem().is(Items.SLIME_BALL); }
    private boolean isEgg() { return getItem().is(Items.EGG); }
    private boolean isTnt() { return getItem().is(Items.TNT); }

    private boolean isArrow() {
        return getItem().is(Items.ARROW)
                || getItem().is(Items.SPECTRAL_ARROW)
                || getItem().is(Items.TIPPED_ARROW);
    }

    private boolean isSpectralArrow() { return getItem().is(Items.SPECTRAL_ARROW); }
    private boolean isTippedArrow() { return getItem().is(Items.TIPPED_ARROW); }
    private boolean isSnowball() { return getItem().is(Items.SNOWBALL); }
    private boolean isFireCharge() { return getItem().is(Items.FIRE_CHARGE); }
    private boolean isWater() { return getItem().is(Items.WATER_BUCKET); }
    private boolean isLava() { return getItem().is(Items.LAVA_BUCKET); }
    private boolean isMilk() { return getItem().is(Items.MILK_BUCKET); }
    private boolean isTrident() { return getItem().is(Items.TRIDENT); }
    private boolean isNormalPotion() { return getItem().is(Items.POTION); }
    private boolean isSplashPotion() { return getItem().is(Items.SPLASH_POTION); }
    private boolean isLingeringPotion() { return getItem().is(Items.LINGERING_POTION); }
    private boolean isEnderPearl() { return getItem().is(Items.ENDER_PEARL); }
    private boolean isChorusFruit() { return getItem().is(Items.CHORUS_FRUIT); }
    private boolean isCobweb() { return getItem().is(Items.COBWEB); }
    private boolean isInk() { return getItem().is(Items.INK_SAC); }
    private boolean isGlowInk() { return getItem().is(Items.GLOW_INK_SAC); }

    private boolean isCoal() {
        return getItem().is(Items.COAL)
                || getItem().is(Items.CHARCOAL);
    }

    private boolean isBlazePowder() { return getItem().is(Items.BLAZE_POWDER); }
    private boolean isMagmaCream() { return getItem().is(Items.MAGMA_CREAM); }
    private boolean isHoneyBottle() { return getItem().is(Items.HONEY_BOTTLE); }
    private boolean isHoneycomb() { return getItem().is(Items.HONEYCOMB); }
    private boolean isPufferfish() { return getItem().is(Items.PUFFERFISH); }
    private boolean isSpiderEye() { return getItem().is(Items.SPIDER_EYE); }
    private boolean isFermentedSpiderEye() { return getItem().is(Items.FERMENTED_SPIDER_EYE); }
    private boolean isRottenFlesh() { return getItem().is(Items.ROTTEN_FLESH); }
    private boolean isGlowstone() { return getItem().is(Items.GLOWSTONE_DUST); }
    private boolean isRedstone() { return getItem().is(Items.REDSTONE); }
    private boolean isFirework() { return getItem().is(Items.FIREWORK_ROCKET); }
    private boolean isDragonBreath() { return getItem().is(Items.DRAGON_BREATH); }
    private boolean isPointedDripstone() { return getItem().is(Items.POINTED_DRIPSTONE); }
    private boolean isObsidian() { return getItem().is(Items.OBSIDIAN); }
    private boolean isAnvil() { return getItem().is(Items.ANVIL); }
    private boolean isFeather() { return getItem().is(Items.FEATHER); }
    private boolean isPrismarineShard() { return getItem().is(Items.PRISMARINE_SHARD); }
    private boolean isNautilusShell() { return getItem().is(Items.NAUTILUS_SHELL); }
    private boolean isEchoShard() { return getItem().is(Items.ECHO_SHARD); }
    private boolean isSand() { return getItem().is(Items.SAND) || getItem().is(Items.RED_SAND); }
    private boolean isGlass() { return getItem().is(Items.GLASS) || getItem().is(Items.GLASS_PANE); }
    private boolean isAmethyst() { return getItem().is(Items.AMETHYST_SHARD); }
    private boolean isGunpowder() { return getItem().is(Items.GUNPOWDER); }
    private boolean isSpawnEgg() { return getItem().getItem() instanceof SpawnEggItem; }

    private boolean isConcretePowder() {
        ItemStack stack = getItem();

        return stack.is(Items.WHITE_CONCRETE_POWDER)
                || stack.is(Items.ORANGE_CONCRETE_POWDER)
                || stack.is(Items.MAGENTA_CONCRETE_POWDER)
                || stack.is(Items.LIGHT_BLUE_CONCRETE_POWDER)
                || stack.is(Items.YELLOW_CONCRETE_POWDER)
                || stack.is(Items.LIME_CONCRETE_POWDER)
                || stack.is(Items.PINK_CONCRETE_POWDER)
                || stack.is(Items.GRAY_CONCRETE_POWDER)
                || stack.is(Items.LIGHT_GRAY_CONCRETE_POWDER)
                || stack.is(Items.CYAN_CONCRETE_POWDER)
                || stack.is(Items.PURPLE_CONCRETE_POWDER)
                || stack.is(Items.BLUE_CONCRETE_POWDER)
                || stack.is(Items.BROWN_CONCRETE_POWDER)
                || stack.is(Items.GREEN_CONCRETE_POWDER)
                || stack.is(Items.RED_CONCRETE_POWDER)
                || stack.is(Items.BLACK_CONCRETE_POWDER);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (level().isClientSide()) {
            return;
        }

        if (isSlime() || isMagmaCream()) {
            return;
        }

        if (!isRemoved()) {
            discard();
        }
    }
}
