package com.example.jjf.stand.powers;

import com.example.jjf.entity.projectile.JJFProjectileEntity;
import com.example.jjf.client.JJFHudSettings;
import com.example.jjf.launcher.JJFLauncherAccess;
import com.example.jjf.launcher.JJFLauncherState;
import com.example.jjf.registry.ModEntities;
import com.example.jjf.registry.ModSounds;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravitySource;
import com.example.jjf.zero_gravity.ZeroGravityState;
import com.example.jjf.zero_gravity.ZeroGravitySystem;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import com.example.jjf.entity.projectile.JJFSpitEntity;
import net.minecraft.world.entity.Entity;
import com.example.jjf.launcher.JJFLauncherAmmoItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.hydra.jojomod.event.AbilityIconInstance;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;

public class PowersJumpinJackFlash extends NewPunchingStand {

    public static final byte POWER_SELF_ZERO_GRAVITY = PowerIndex.POWER_1;
    public static final byte POWER_LOAD_ITEM = PowerIndex.POWER_4;
    public static final byte POWER_UNLOAD_ITEM = PowerIndex.POWER_4_BLOCK;
    public static final byte POWER_FIRE_ITEM = PowerIndex.POWER_4_EXTRA;

    public static final byte POWER_FIRE_BURST_3 = 74;
    public static final byte POWER_FIRE_BURST_4 = 75;
    public static final byte POWER_FIRE_BURST_5 = 76;
    public static final byte POWER_FIRE_BURST_6 = 77;
    public static final byte POWER_FIRE_CONE = 78;

    private static final int MAX_LOADED_AMMO = 64;
    private static final int LOAD_REPEAT_TICKS = 4;

    private static final int BURST_READY_TICKS = 8;
    private static final int BURST_4_TICKS = 20;
    private static final int BURST_5_TICKS = 32;
    private static final int BURST_6_TICKS = 44;
    private static final int CONE_READY_TICKS = 50;

    private static final int SINGLE_SHOT_COOLDOWN_TICKS = 8;
    private static final int MULTI_SHOT_COOLDOWN_TICKS = 2;

    private static final float PROJECTILE_SPEED = 3.0F;
    private static final float BURST_SPREAD = 0.35F;
    private static final float CONE_SPREAD = 5.0F;

    private static final double LAUNCHER_RECOIL_MAX_HORIZONTAL = 1.2D;
    private static final double LAUNCHER_RECOIL_MAX_VERTICAL = 0.8D;
    private static final double BURST_RECOIL_GROWTH = 0.30D;
    private static final double CONE_RECOIL_MULTIPLIER = 1.15D;
    private static final double GROUND_RECOIL_MULTIPLIER = 0.55D;

    private static final int SPIN_ATTACK_MIN_TICKS = 1;
    private static final int SPIN_ATTACK_FULL_TICKS = 35;

    private static final float SPIN_ATTACK_MIN_DAMAGE = 2.5F;
    private static final float SPIN_ATTACK_MAX_DAMAGE = 5.0F;

    private static final double SPIN_ATTACK_MIN_KNOCKBACK = 0.70D;
    private static final double SPIN_ATTACK_MAX_KNOCKBACK = 3.25D;
    private static final double SPIN_ATTACK_ZERO_G_MULTIPLIER = 1.80D;

    public static final byte POWER_SPIT =
            PowerIndex.POWER_2;

    public static final byte POWER_RELEASE_SPREAD = 79;
    public static final byte POWER_SPIN_RELEASE = 80;

    private static final int SPIT_COOLDOWN_TICKS = 80;
    private static final float SPIT_SPEED = 1.6F;

    private boolean jjf$spinAttackHeld = false;
    private int jjf$spinAttackTicks = 0;

private static final ResourceLocation NORMAL_GRAVITY_ICON =
            new ResourceLocation(
                    "roundabout",
                    "textures/mob_effect/gravity_flip.png"
            );

    private static final ResourceLocation ZERO_GRAVITY_ON =
            new ResourceLocation(
                    "jjf",
                    "textures/gui/jjf/zero_gravity_on.png"
            );

    private static final ResourceLocation LOADING_ICON =
            new ResourceLocation(
                    "jjf",
                    "textures/gui/jjf/load.png"
            );

    private static final ResourceLocation SHOOTING_ICON =
            new ResourceLocation(
                    "jjf",
                    "textures/gui/jjf/shoot.png"
            );

    private static final ResourceLocation UNLOADING_ICON =
            new ResourceLocation(
                    "jjf",
                    "textures/gui/jjf/unload.png"
            );

    private static final ResourceLocation SPIT_ICON =
            new ResourceLocation(
                    "jjf",
                    "textures/gui/jjf/spit.png"
            );

    private static final ResourceLocation PUNCH_ICON =
            new ResourceLocation(
                    "jjf",
                    "textures/gui/jjf/punch.png"
            );

    private static final ResourceLocation GUARD_ICON =
            new ResourceLocation(
                    "jjf",
                    "textures/gui/jjf/guard.png"
            );
    private boolean jjf$skill4WasDown = false;
    private int jjf$loadHoldTicks = 0;

    private boolean jjf$useWasDown = false;
    private int jjf$useHoldTicks = 0;

    private boolean jjf$fireRightSide = true;

    public PowersJumpinJackFlash(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersJumpinJackFlash(entity);
    }

    @Override
    public boolean canSummonStand() {
        return true;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.JUMPIN_JACK_FLASH.create(
                this.getSelf().level()
        );
    }

    @Override
    public void playSummonSound() {
        if (this.getSelf().isCrouching()) return;

        this.getSelf().level().playSound(
                null,
                this.getSelf().blockPosition(),
                ModSounds.JUMPIN_JACK_FLASH_SUMMON,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }

    @Override
    public void punchImpact(Entity entity) {
    }

    @Override
    public void standPunch() {
    }

    @Override
    public boolean interceptAttack() {
        if (shouldUseZeroGravityBlockPush()) {
            return false;
        }

        if (shouldUseZeroGravityItemSwing()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canUseMiningStand() {
        if (this.getSelf() instanceof ZeroGravityAccess access) {
            ZeroGravityState state =
                    access.jjf$getZeroGravityState();

            if (state.isZeroGravity()) {
                return false;
            }
        }

        return super.canUseMiningStand();
    }

    private boolean shouldUseZeroGravityBlockPush() {
        if (!(this.getSelf() instanceof Player player)) {
            return false;
        }

        if (!(player instanceof ZeroGravityAccess access)) {
            return false;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) return false;
        if (player.onGround()) return false;

        HitResult hit =
                player.pick(
                        4.5D,
                        0.0F,
                        false
                );

        if (hit.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockHitResult blockHit =
                (BlockHitResult) hit;

        BlockState blockState =
                player.level().getBlockState(
                        blockHit.getBlockPos()
                );

        return blockState.isCollisionShapeFullBlock(
                player.level(),
                blockHit.getBlockPos()
        );
    }

    private boolean shouldUseZeroGravityItemSwing() {
        if (!(this.getSelf() instanceof Player player)) {
            return false;
        }

        if (!(player instanceof ZeroGravityAccess access)) {
            return false;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) return false;
        if (player.onGround()) return false;
        if (player.getMainHandItem().isEmpty()) return false;
        if (shouldUseZeroGravityBlockPush()) return false;

        return getTargetEntity(
                player,
                -1,
                getPunchAngle()
        ) == null;
    }

    @Override
    public boolean setPowerAttack() {
        if (shouldUseZeroGravityBlockPush()) {
            return false;
        }

        if (shouldUseZeroGravityItemSwing()) {
            return false;
        }

        beginSpinAttack();
        return true;
    }

    @Override
    public void updateAttack() {
        beginSpinAttack();
    }

    private void beginSpinAttack() {
        if (jjf$spinAttackHeld) {
            return;
        }

        jjf$useWasDown = false;
        jjf$useHoldTicks = 0;
        jjf$spinAttackHeld = true;
        jjf$spinAttackTicks = 0;
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        this.setAttackTimeMax(SPIN_ATTACK_FULL_TICKS);
        this.setActivePower(PowerIndex.BARRAGE);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        this.poseStand(net.hydra.jojomod.event.index.OffsetIndex.ATTACK);
        animateStand(StandEntity.BARRAGE_CHARGE);
    }


    @Override
    public void tickPower() {
        super.tickPower();

        if (!jjf$spinAttackHeld) {
            return;
        }

        this.poseStand(net.hydra.jojomod.event.index.OffsetIndex.ATTACK);
        animateStand(StandEntity.BARRAGE_CHARGE);

        if (!(this.getSelf() instanceof Player)) {
            return;
        }

        if (!this.getSelf().level().isClientSide()) {
            return;
        }

        if (!isPacketPlayer()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null
                || minecraft.player.getId() != this.getSelf().getId()) {
            return;
        }

        if (!minecraft.options.keyAttack.isDown()) {
            releaseJJFSpinAttackClient();
        }
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        float modified = super.inputSpeedModifiers(basis);

        if (!jjf$spinAttackHeld) {
            return modified;
        }

        return Math.min(modified, basis * 0.30F);
    }

    @Override
    public boolean cancelSprintJump() {
        return jjf$spinAttackHeld || super.cancelSprintJump();
    }

    @Override
    public boolean cancelSprint() {
        return jjf$spinAttackHeld || super.cancelSprint();
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_GUARD -> {
                if (isHoldingSneak()) {
                    if (this.getSelf().level().isClientSide()) {
                        releaseSelfZeroGravity();
                    }
                    tryPowerPacket(POWER_RELEASE_SPREAD);
                    return;
                }

                if (onCooldown(PowerIndex.SKILL_1)) {
                    return;
                }

                if (this.getSelf().level().isClientSide()) {
                    toggleSelfZeroGravity();
                }

                tryPowerPacket(POWER_SELF_ZERO_GRAVITY);
            }

            case SKILL_1_CROUCH, SKILL_1_CROUCH_GUARD -> {
                if (this.getSelf().level().isClientSide()) {
                    releaseSelfZeroGravity();
                }
                tryPowerPacket(POWER_RELEASE_SPREAD);
            }

            case SKILL_2_NORMAL -> {
                if (onCooldown(
                        PowerIndex.SKILL_2
                )) {
                    return;
                }

                setCooldown(
                        PowerIndex.SKILL_2,
                        SPIT_COOLDOWN_TICKS
                );

                tryPowerPacket(
                        POWER_SPIT
                );
            }

            case SKILL_3_NORMAL ->
                    dash();

            case SKILL_4_NORMAL ->
                    tryPowerPacket(
                            POWER_LOAD_ITEM
                    );

            case SKILL_4_GUARD,
                 SKILL_4_CROUCH_GUARD ->
                    tryPowerPacket(
                            POWER_UNLOAD_ITEM
                    );


            default -> {
            }
        }
    }

    @Override
    public void buttonInput4(
            boolean keyIsDown,
            Options options
    ) {
        if (!keyIsDown) {
            jjf$skill4WasDown = false;
            jjf$loadHoldTicks = 0;
            return;
        }

        cancelPlayerPose();

        if (isHoldingSneak() || isGuarding()) {
            if (!jjf$skill4WasDown) {
                if (isHoldingSneak() && isGuarding()) {
                    powerActivate(
                            PowerContext.SKILL_4_CROUCH_GUARD
                    );
                } else if (isHoldingSneak()) {
                    powerActivate(
                            PowerContext.SKILL_4_CROUCH
                    );
                } else {
                    powerActivate(
                            PowerContext.SKILL_4_GUARD
                    );
                }
            }

            jjf$skill4WasDown = true;
            return;
        }

        if (!jjf$skill4WasDown) {
            powerActivate(
                    PowerContext.SKILL_4_NORMAL
            );

            jjf$skill4WasDown = true;
            jjf$loadHoldTicks = 0;
            return;
        }

        jjf$loadHoldTicks++;

        if (jjf$loadHoldTicks
                % LOAD_REPEAT_TICKS == 0) {
            powerActivate(
                    PowerContext.SKILL_4_NORMAL
            );
        }
    }

    @Override
    public void buttonInputUse(
            boolean keyIsDown,
            Options options
    ) {
        if (jjf$spinAttackHeld) {
            jjf$useWasDown = false;
            jjf$useHoldTicks = 0;
            return;
        }
        if (!(getSelf() instanceof Player player)) {
            return;
        }

        if (!keyIsDown) {
            return;
        }

        Minecraft minecraft =
                Minecraft.getInstance();

        if (!jjf$useWasDown && minecraft.hitResult != null
                && minecraft.hitResult.getType()
                == HitResult.Type.BLOCK) {

            jjf$useWasDown = false;
            jjf$useHoldTicks = 0;
            return;
        }

        if (player.isUsingItem()) {
            jjf$useWasDown = false;
            jjf$useHoldTicks = 0;
            return;
        }

        ItemStack mainHand =
                player.getMainHandItem();

        ItemStack offHand =
                player.getOffhandItem();

        if ((!mainHand.isEmpty()
                && mainHand.getUseAnimation() != UseAnim.NONE)
                || (!offHand.isEmpty()
                && offHand.getUseAnimation() != UseAnim.NONE)) {

            jjf$useWasDown = false;
            jjf$useHoldTicks = 0;
            return;
        }

        if (!jjf$useWasDown) {
            jjf$useWasDown = true;
            jjf$useHoldTicks = 0;
            return;
        }

        if (jjf$useHoldTicks < CONE_READY_TICKS) {
            jjf$useHoldTicks++;
        }
    }

    @Override
    public boolean clickRelease() {
        return jjf$useWasDown && !jjf$spinAttackHeld;
    }

    @Override
    public boolean onClickRelease() {
        if (jjf$spinAttackHeld) {
            jjf$useWasDown = false;
            jjf$useHoldTicks = 0;
            return false;
        }
        if (!jjf$useWasDown) {
            return false;
        }

        int charge =
                jjf$useHoldTicks;

        if (charge >= CONE_READY_TICKS) {
            tryPowerPacket(
                    POWER_FIRE_CONE
            );
        } else if (charge >= BURST_6_TICKS) {
            tryPowerPacket(
                    POWER_FIRE_BURST_6
            );
        } else if (charge >= BURST_5_TICKS) {
            tryPowerPacket(
                    POWER_FIRE_BURST_5
            );
        } else if (charge >= BURST_4_TICKS) {
            tryPowerPacket(
                    POWER_FIRE_BURST_4
            );
        } else if (charge >= BURST_READY_TICKS) {
            tryPowerPacket(
                    POWER_FIRE_BURST_3
            );
        } else {
            tryPowerPacket(
                    POWER_FIRE_ITEM
            );
        }

        jjf$useWasDown = false;
        jjf$useHoldTicks = 0;

        return false;
    }

    @Override
    public boolean setPowerOther(
            int move,
            int lastMove
    ) {
        if (move == POWER_SELF_ZERO_GRAVITY) {
            if (onCooldown(PowerIndex.SKILL_1)) {
                return false;
            }

            if (!this.getSelf()
                    .level()
                    .isClientSide()) {
                toggleSelfZeroGravity();
            }

            return true;
        }

        if (move == POWER_RELEASE_SPREAD) {
            if (!this.getSelf().level().isClientSide()) {
                releaseOwnSpread();
                releaseSelfZeroGravity();
            }

            return true;
        }

        if (move == POWER_SPIT) {
            if (onCooldown(
                    PowerIndex.SKILL_2
            )) {
                return false;
            }

            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                fireSpit(
                        player
                );
            }

            setCooldown(
                    PowerIndex.SKILL_2,
                    SPIT_COOLDOWN_TICKS
            );

            return true;
        }

        if (move == POWER_LOAD_ITEM) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                loadHeldItem(player);
            }

            return true;
        }

        if (move == POWER_UNLOAD_ITEM) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                unloadLoadedItem(player);
            }

            return true;
        }

        if (move == POWER_FIRE_ITEM) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                fireSingleShot(player);
            }

            return true;
        }

        if (move == POWER_FIRE_BURST_3) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                fireBurst(player, 3);
            }

            return true;
        }

        if (move == POWER_FIRE_BURST_4) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                fireBurst(player, 4);
            }

            return true;
        }

        if (move == POWER_FIRE_BURST_5) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                fireBurst(player, 5);
            }

            return true;
        }


        if (move == POWER_FIRE_BURST_6) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                fireBurst(player, 6);
            }

            return true;
        }

        if (move == POWER_FIRE_CONE) {
            if (!this.getSelf()
                    .level()
                    .isClientSide()
                    && this.getSelf()
                    instanceof Player player) {
                fireCone(player);
            }

            return true;
        }

        return super.setPowerOther(
                move,
                lastMove
        );
    }

    private void fireSpit(
            Player player
    ) {
        JJFSpitEntity spit =
                new JJFSpitEntity(
                        player.level(),
                        player
                );

        Vec3 look =
                player.getLookAngle();

        Vec3 spawn =
                new Vec3(
                        player.getX(),
                        player.getEyeY()
                                - 0.12D,
                        player.getZ()
                ).add(
                        look.scale(
                                0.35D
                        )
                );

        spit.setPos(
                spawn.x,
                spawn.y,
                spawn.z
        );

        spit.shootFromRotation(
                player,
                player.getXRot(),
                player.getYRot(),
                0.0F,
                SPIT_SPEED,
                0.6F
        );

        player.level()
                .addFreshEntity(
                        spit
                );
    }

    private boolean isSupportedAmmo(
            ItemStack stack
    ) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.getItem()
                instanceof JJFLauncherAmmoItem) {
            return true;
        }

        if (stack.getItem()
                instanceof SpawnEggItem) {
            return true;
        }

        return stack.is(Items.IRON_NUGGET)
                || stack.is(Items.SLIME_BALL)
                || stack.is(Items.EGG)
                || stack.is(Items.TNT)

                || stack.is(Items.ARROW)
                || stack.is(Items.SPECTRAL_ARROW)
                || stack.is(Items.TIPPED_ARROW)

                || stack.is(Items.SNOWBALL)

                || stack.is(Items.FIRE_CHARGE)
                || stack.is(Items.BLAZE_POWDER)
                || stack.is(Items.MAGMA_CREAM)

                || stack.is(Items.WATER_BUCKET)
                || stack.is(Items.LAVA_BUCKET)
                || stack.is(Items.MILK_BUCKET)

                || stack.is(Items.TRIDENT)

                || stack.is(Items.POTION)
                || stack.is(Items.SPLASH_POTION)
                || stack.is(Items.LINGERING_POTION)

                || stack.is(Items.SAND)
                || stack.is(Items.RED_SAND)

                || stack.is(Items.GLASS)
                || stack.is(Items.GLASS_PANE)
                || stack.is(Items.AMETHYST_SHARD)
                || stack.is(Items.POINTED_DRIPSTONE)
                || stack.is(Items.PRISMARINE_SHARD)

                || stack.is(Items.GUNPOWDER)

                || stack.is(Items.ENDER_PEARL)
                || stack.is(Items.CHORUS_FRUIT)

                || stack.is(Items.COBWEB)

                || stack.is(Items.INK_SAC)
                || stack.is(Items.GLOW_INK_SAC)

                || stack.is(Items.COAL)
                || stack.is(Items.CHARCOAL)

                || stack.is(Items.HONEY_BOTTLE)
                || stack.is(Items.HONEYCOMB)

                || stack.is(Items.PUFFERFISH)
                || stack.is(Items.SPIDER_EYE)
                || stack.is(Items.FERMENTED_SPIDER_EYE)
                || stack.is(Items.ROTTEN_FLESH)

                || stack.is(Items.GLOWSTONE_DUST)
                || stack.is(Items.REDSTONE)

                || stack.is(Items.FIREWORK_ROCKET)
                || stack.is(Items.DRAGON_BREATH)

                || stack.is(Items.OBSIDIAN)
                || stack.is(Items.ANVIL)
                || stack.is(Items.FEATHER)

                || stack.is(Items.NAUTILUS_SHELL)
                || stack.is(Items.ECHO_SHARD)

                || isConcretePowder(stack)
                || stack.is(Items.DIRT)
                || stack.is(Items.COARSE_DIRT)
                || stack.is(Items.GRAVEL)
                || stack.is(Items.CLAY_BALL)
                || stack.is(Items.BRICK)
                || stack.is(Items.NETHER_BRICK)
                || stack.is(Items.BONE)
                || stack.is(Items.BONE_MEAL)
                || stack.is(Items.FLINT)
                || stack.is(Items.CACTUS)
                || stack.is(Items.SWEET_BERRIES)
                || stack.is(Items.GLOW_BERRIES)
                || stack.is(Items.SLIME_BLOCK)
                || stack.is(Items.SPONGE)
                || stack.is(Items.WET_SPONGE)
                || stack.is(Items.SCUTE)
                || stack.is(Items.RABBIT_FOOT)
                || stack.is(Items.PHANTOM_MEMBRANE)
                || stack.is(Items.SHULKER_SHELL)
                || stack.is(Items.END_ROD)
                || stack.is(Items.BASALT)
                || stack.is(Items.LIGHTNING_ROD)
                || stack.is(Items.COPPER_INGOT)
                || stack.is(Items.CHAIN)
                || stack.is(Items.FISHING_ROD);
    }

    private boolean isConcretePowder(
            ItemStack stack
    ) {
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



    private float getSpreadMultiplier(
            ItemStack stack
    ) {
        if (stack.is(Items.ARROW)
                || stack.is(Items.SPECTRAL_ARROW)
                || stack.is(Items.TIPPED_ARROW)) {
            return 0.70F;
        }

        if (stack.is(Items.TRIDENT)) {
            return 0.75F;
        }

        if (stack.is(Items.FIREWORK_ROCKET)) {
            return 0.80F;
        }

        if (stack.is(Items.IRON_NUGGET)) {
            return 0.80F;
        }

        if (stack.is(Items.AMETHYST_SHARD)) {
            return 0.85F;
        }

        if (stack.is(Items.GLASS)
                || stack.is(Items.GLASS_PANE)) {
            return 0.90F;
        }

        if (stack.is(Items.SNOWBALL)
                || stack.is(Items.EGG)
                || stack.is(Items.ENDER_PEARL)) {
            return 0.95F;
        }

        if (stack.is(Items.SPLASH_POTION)
                || stack.is(Items.LINGERING_POTION)) {
            return 1.10F;
        }

        if (stack.is(Items.SLIME_BALL)) {
            return 1.20F;
        }

        if (stack.is(Items.TNT)) {
            return 1.25F;
        }

        if (stack.is(Items.GUNPOWDER)) {
            return 1.30F;
        }

        if (stack.is(Items.SAND)
                || stack.is(Items.RED_SAND)) {
            return 1.35F;
        }

        if (isConcretePowder(stack)) {
            return 1.20F;
        }

        return 1.0F;
    }

    private int jjf$getRequiredAmmoLevel(ItemStack item) {
        if (item.is(Items.TNT) || item.is(Items.ANVIL)
                || item.is(Items.OBSIDIAN) || item.is(Items.TRIDENT)
                || item.is(Items.SHULKER_SHELL) || item.is(Items.LIGHTNING_ROD)
                || item.is(Items.ECHO_SHARD) || item.is(Items.DRAGON_BREATH)
                || item.is(Items.NETHERITE_SCRAP)) return 7;
        if (item.is(Items.FIRE_CHARGE) || item.is(Items.LAVA_BUCKET)
                || item.is(Items.FIREWORK_ROCKET) || item.is(Items.BASALT)
                || item.is(Items.ENDER_PEARL) || item.is(Items.CHAIN)
                || item.is(Items.FISHING_ROD) || item.is(Items.SLIME_BLOCK)
                || item.is(Items.POINTED_DRIPSTONE) || item.is(Items.AMETHYST_SHARD)) return 5;
        if (item.is(Items.GRAVEL) || item.is(Items.BRICK)
                || item.is(Items.NETHER_BRICK) || item.is(Items.FLINT)
                || item.is(Items.CACTUS) || item.is(Items.SCUTE)
                || item.is(Items.COPPER_INGOT) || item.is(Items.END_ROD)
                || item.is(Items.MAGMA_CREAM)) return 3;
        return 1;
    }

    private boolean jjf$canLoadAmmo(Player player, int requiredLevel) {
        if (player.isCreative()) return true;
        if (player instanceof StandUser user) {
            ItemStack disc = user.roundabout$getStandDisc();
            if (!disc.isEmpty() && disc.getItem() instanceof MaxStandDiscItem) return true;
        }
        return player instanceof IPlayerEntity access
                && access.roundabout$getStandLevel() >= requiredLevel;
    }

    private void loadHeldItem(
            Player player
    ) {
        if (!(player
                instanceof JJFLauncherAccess access)) {
            return;
        }

        JJFLauncherState launcher =
                access.jjf$getLauncherState();

        ItemStack held =
                player.getMainHandItem();

        if (!isSupportedAmmo(held)) {
            return;
        }

        int requiredLevel = jjf$getRequiredAmmoLevel(held);
        if (!jjf$canLoadAmmo(player, requiredLevel)) {
            player.displayClientMessage(
                    Component.literal("You need JJF development level " + requiredLevel + " to load this."),
                    true
            );
            return;
        }

        int capacity =
                getAmmoCapacity(held);

        if (launcher.hasLoadedItem()) {
            ItemStack loaded =
                    launcher.getLoadedItem();

            if (!ItemStack.isSameItemSameTags(
                    loaded,
                    held
            )) {
                return;
            }

            if (loaded.getCount()
                    >= capacity) {
                return;
            }

            loaded.grow(1);

            launcher.setLoadedItem(
                    loaded
            );
        } else {
            ItemStack loaded =
                    held.copy();

            loaded.setCount(1);

            launcher.setLoadedItem(
                    loaded
            );
        }

        if (!player.getAbilities()
                .instabuild) {
            held.shrink(1);
        }

        access.jjf$setLoadedAmmoCount(
                launcher.getLoadedCount()
        );

        player.getInventory()
                .setChanged();
    }

    private void unloadLoadedItem(
            Player player
    ) {
        if (!(player
                instanceof JJFLauncherAccess access)) {
            return;
        }

        JJFLauncherState launcher =
                access.jjf$getLauncherState();

        if (!launcher.hasLoadedItem()) {
            return;
        }

        ItemStack loaded =
                launcher.getLoadedItem();

        launcher.clearLoadedItem();

        access.jjf$setLoadedAmmoCount(0);

        if (player.getAbilities()
                .instabuild) {
            return;
        }

        boolean fullyAdded =
                player.getInventory()
                        .add(loaded);

        if (!fullyAdded
                && !loaded.isEmpty()) {
            player.drop(
                    loaded,
                    false
            );
        }

        player.getInventory()
                .setChanged();
    }

    private void fireSingleShot(
            Player player
    ) {
        if (!(player
                instanceof JJFLauncherAccess access)) {
            return;
        }

        JJFLauncherState launcher =
                access.jjf$getLauncherState();

        if (!launcher.hasLoadedItem()) {
            return;
        }

        long gameTime =
                player.level()
                        .getGameTime();

        if (!launcher.canFire(
                gameTime,
                SINGLE_SHOT_COOLDOWN_TICKS
        )) {
            return;
        }

        ItemStack loaded =
                launcher.getLoadedItem();

        if (!spawnLauncherProjectile(
                player,
                loaded,
                0.0F
        )) {
            return;
        }

        float soundPitch =
                getLauncherSoundPitch(
                        loaded
                );

        float soundVolume =
                getLauncherSoundVolume(
                        loaded
                );

        applyLauncherRecoil(
                player,
                loaded,
                1,
                false
        );

        consumeLoadedAmmo(
                player,
                launcher,
                loaded,
                1
        );

        launcher.markFired(
                gameTime
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                ModSounds.LAUNCHER_SHOOT,
                SoundSource.PLAYERS,
                soundVolume,
                soundPitch
        );
    }

    private int getAmmoCapacity(ItemStack stack) {
        if (stack.getItem()
                instanceof JJFLauncherAmmoItem custom) {
            return custom.jjf$getAmmoCapacity(
                    stack
            );
        }

        if (stack.is(Items.WATER_BUCKET)
                || stack.is(Items.LAVA_BUCKET)
                || stack.is(Items.MILK_BUCKET)
                || stack.is(Items.TRIDENT)
                || stack.is(Items.ANVIL)
                || stack.getItem() instanceof SpawnEggItem) {
            return 1;
        }

        if (stack.is(Items.DRAGON_BREATH)) {
            return 2;
        }

        if (stack.is(Items.TNT)
                || stack.is(Items.ENDER_PEARL)
                || stack.is(Items.CHORUS_FRUIT)
                || stack.is(Items.COBWEB)
                || stack.is(Items.OBSIDIAN)
                || stack.is(Items.FIREWORK_ROCKET)) {
            return 4;
        }

        if (stack.is(Items.POTION)
                || stack.is(Items.SPLASH_POTION)
                || stack.is(Items.LINGERING_POTION)
                || stack.is(Items.GUNPOWDER)) {
            return 8;
        }

        if (stack.is(Items.EGG)
                || stack.is(Items.FIRE_CHARGE)
                || stack.is(Items.BLAZE_POWDER)
                || stack.is(Items.MAGMA_CREAM)
                || stack.is(Items.GLASS)
                || stack.is(Items.GLASS_PANE)
                || stack.is(Items.HONEY_BOTTLE)
                || stack.is(Items.PUFFERFISH)) {
            return 16;
        }

        if (stack.is(Items.TIPPED_ARROW)
                || stack.is(Items.AMETHYST_SHARD)
                || stack.is(Items.POINTED_DRIPSTONE)
                || stack.is(Items.PRISMARINE_SHARD)
                || stack.is(Items.NAUTILUS_SHELL)
                || stack.is(Items.ECHO_SHARD)) {
            return 32;
        }

        return MAX_LOADED_AMMO;
    }

    private float getProjectileSpeed(ItemStack stack) {
        if (stack.is(Items.ANVIL)) {
            return 1.5F;
        }

        if (stack.is(Items.OBSIDIAN)) {
            return 2.0F;
        }

        if (stack.is(Items.WATER_BUCKET)
                || stack.is(Items.LAVA_BUCKET)
                || stack.is(Items.MILK_BUCKET)) {
            return 2.3F;
        }

        if (stack.getItem() instanceof SpawnEggItem) {
            return 2.4F;
        }

        if (stack.is(Items.POTION)
                || stack.is(Items.SPLASH_POTION)
                || stack.is(Items.LINGERING_POTION)) {
            return 2.5F;
        }

        if (stack.is(Items.TRIDENT)) {
            return 2.8F;
        }

        return PROJECTILE_SPEED;
    }

    private void fireBurst(
            Player player,
            int requestedShots
    ) {
        if (!(player
                instanceof JJFLauncherAccess access)) {
            return;
        }

        JJFLauncherState launcher =
                access.jjf$getLauncherState();

        if (!launcher.hasLoadedItem()) {
            return;
        }

        long gameTime =
                player.level()
                        .getGameTime();

        if (!launcher.canFire(
                gameTime,
                MULTI_SHOT_COOLDOWN_TICKS
        )) {
            return;
        }

        ItemStack loaded =
                launcher.getLoadedItem();

        int shots =
                Math.min(
                        requestedShots,
                        loaded.getCount()
                );

        if (shots <= 0) {
            return;
        }

        int actuallyFired = 0;

        for (int i = 0; i < shots; i++) {
            if (spawnLauncherProjectile(
                    player,
                    loaded,
                    BURST_SPREAD
            )) {
                actuallyFired++;
            }
        }

        if (actuallyFired <= 0) {
            return;
        }

        float soundPitch =
                getLauncherSoundPitch(
                        loaded
                );

        float soundVolume =
                getLauncherSoundVolume(
                        loaded
                );

        applyLauncherRecoil(
                player,
                loaded,
                actuallyFired,
                false
        );

        consumeLoadedAmmo(
                player,
                launcher,
                loaded,
                actuallyFired
        );

        launcher.markFired(
                gameTime
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                ModSounds.LAUNCHER_BURST,
                SoundSource.PLAYERS,
                soundVolume,
                soundPitch
        );
    }

    private void fireCone(
            Player player
    ) {
        if (!(player
                instanceof JJFLauncherAccess access)) {
            return;
        }

        JJFLauncherState launcher =
                access.jjf$getLauncherState();

        if (!launcher.hasLoadedItem()) {
            return;
        }

        long gameTime =
                player.level()
                        .getGameTime();

        if (!launcher.canFire(
                gameTime,
                MULTI_SHOT_COOLDOWN_TICKS
        )) {
            return;
        }

        ItemStack loaded =
                launcher.getLoadedItem();

        int shots =
                loaded.getCount();

        if (shots <= 0) {
            return;
        }

        int actuallyFired = 0;

        for (int i = 0; i < shots; i++) {
            if (spawnLauncherProjectile(
                    player,
                    loaded,
                    CONE_SPREAD
            )) {
                actuallyFired++;
            }
        }

        if (actuallyFired <= 0) {
            return;
        }

        float soundPitch =
                Math.max(
                        0.65F,
                        getLauncherSoundPitch(
                                loaded
                        ) - 0.08F
                );

        float soundVolume =
                Math.min(
                        1.25F,
                        getLauncherSoundVolume(
                                loaded
                        ) + 0.10F
                );

        applyLauncherRecoil(
                player,
                loaded,
                actuallyFired,
                true
        );

        consumeLoadedAmmo(
                player,
                launcher,
                loaded,
                actuallyFired
        );

        launcher.markFired(
                gameTime
        );

        player.level().playSound(
                null,
                player.blockPosition(),
                ModSounds.LAUNCHER_BURST,
                SoundSource.PLAYERS,
                soundVolume,
                soundPitch
        );
    }

    private void applyLauncherRecoil(
            Player player,
            ItemStack stack,
            int shots,
            boolean cone
    ) {
        if (!(player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isSafeUserZeroGravity()) {
            return;
        }

        Vec3 look =
                player.getLookAngle();

        Vec3 recoilDirection =
                new Vec3(
                        -look.x,
                        -look.y * 0.35D,
                        -look.z
                );

        if (recoilDirection.lengthSqr()
                < 0.0001D) {
            return;
        }

        double strength =
                getLauncherRecoilStrength(
                        stack
                );

        int extraShots =
                Math.max(
                        0,
                        shots - 1
                );

        double shotMultiplier =
                1.0D
                        + Math.sqrt(
                        extraShots
                ) * BURST_RECOIL_GROWTH;

        shotMultiplier =
                Math.min(
                        2.40D,
                        shotMultiplier
                );

        if (cone) {
            shotMultiplier *=
                    CONE_RECOIL_MULTIPLIER;
        }

        if (ZeroGravitySystem
                .isTouchingGround(
                        player
                )) {
            shotMultiplier *=
                    GROUND_RECOIL_MULTIPLIER;
        }

        Vec3 recoil =
                recoilDirection
                        .normalize()
                        .scale(
                                strength
                                        * shotMultiplier
                        );

        Vec3 motion =
                player.getDeltaMovement()
                        .add(
                                recoil
                        );

        player.setDeltaMovement(
                clampLauncherMotion(
                        motion.x,
                        LAUNCHER_RECOIL_MAX_HORIZONTAL
                ),
                clampLauncherMotion(
                        motion.y,
                        LAUNCHER_RECOIL_MAX_VERTICAL
                ),
                clampLauncherMotion(
                        motion.z,
                        LAUNCHER_RECOIL_MAX_HORIZONTAL
                )
        );

        player.hasImpulse = true;
    }

    private double getLauncherRecoilStrength(
            ItemStack stack
    ) {
        if (stack.is(Items.ANVIL)) {
            return 0.22D;
        }

        if (stack.is(Items.TNT)) {
            return 0.18D;
        }

        if (stack.is(Items.OBSIDIAN)) {
            return 0.17D;
        }

        if (stack.is(Items.FIREWORK_ROCKET)) {
            return 0.16D;
        }

        if (stack.is(Items.TRIDENT)) {
            return 0.13D;
        }

        if (stack.is(Items.WATER_BUCKET)
                || stack.is(Items.LAVA_BUCKET)
                || stack.is(Items.MILK_BUCKET)) {
            return 0.12D;
        }

        if (stack.getItem()
                instanceof SpawnEggItem) {
            return 0.10D;
        }

        if (stack.is(Items.DRAGON_BREATH)
                || stack.is(Items.GUNPOWDER)) {
            return 0.09D;
        }

        if (stack.is(Items.ENDER_PEARL)
                || stack.is(Items.CHORUS_FRUIT)) {
            return 0.08D;
        }

        if (stack.is(Items.FIRE_CHARGE)
                || stack.is(Items.BLAZE_POWDER)
                || stack.is(Items.MAGMA_CREAM)) {
            return 0.075D;
        }

        if (stack.is(Items.POTION)
                || stack.is(Items.SPLASH_POTION)
                || stack.is(Items.LINGERING_POTION)) {
            return 0.065D;
        }

        if (stack.is(Items.SLIME_BALL)) {
            return 0.060D;
        }

        if (stack.is(Items.ARROW)
                || stack.is(Items.SPECTRAL_ARROW)
                || stack.is(Items.TIPPED_ARROW)) {
            return 0.055D;
        }

        if (stack.is(Items.AMETHYST_SHARD)
                || stack.is(Items.POINTED_DRIPSTONE)
                || stack.is(Items.PRISMARINE_SHARD)
                || stack.is(Items.NAUTILUS_SHELL)
                || stack.is(Items.ECHO_SHARD)) {
            return 0.050D;
        }

        if (stack.is(Items.IRON_NUGGET)
                || stack.is(Items.GLASS)
                || stack.is(Items.GLASS_PANE)
                || isConcretePowder(
                stack
        )) {
            return 0.045D;
        }

        if (stack.is(Items.SNOWBALL)
                || stack.is(Items.EGG)) {
            return 0.035D;
        }

        if (stack.is(Items.FEATHER)) {
            return 0.015D;
        }

        return 0.040D;
    }

    private float getLauncherSoundPitch(
            ItemStack stack
    ) {
        if (stack.is(Items.ANVIL)) {
            return 0.72F;
        }

        if (stack.is(Items.TNT)
                || stack.is(Items.OBSIDIAN)) {
            return 0.80F;
        }

        if (stack.is(Items.TRIDENT)
                || stack.is(Items.WATER_BUCKET)
                || stack.is(Items.LAVA_BUCKET)
                || stack.is(Items.MILK_BUCKET)) {
            return 0.90F;
        }

        if (stack.is(Items.FIREWORK_ROCKET)
                || stack.is(Items.FIRE_CHARGE)
                || stack.is(Items.BLAZE_POWDER)) {
            return 0.95F;
        }

        if (stack.is(Items.ARROW)
                || stack.is(Items.SPECTRAL_ARROW)
                || stack.is(Items.TIPPED_ARROW)) {
            return 1.08F;
        }

        if (stack.is(Items.SNOWBALL)
                || stack.is(Items.EGG)
                || stack.is(Items.FEATHER)) {
            return 1.18F;
        }

        return 1.0F;
    }

    private float getLauncherSoundVolume(
            ItemStack stack
    ) {
        if (stack.is(Items.ANVIL)
                || stack.is(Items.TNT)
                || stack.is(Items.OBSIDIAN)) {
            return 1.15F;
        }

        if (stack.is(Items.TRIDENT)
                || stack.is(Items.FIREWORK_ROCKET)) {
            return 1.05F;
        }

        if (stack.is(Items.FEATHER)
                || stack.is(Items.SNOWBALL)) {
            return 0.82F;
        }

        return 0.95F;
    }

    private double clampLauncherMotion(
            double value,
            double limit
    ) {
        return Math.max(
                -limit,
                Math.min(
                        limit,
                        value
                )
        );
    }

    private boolean spawnLauncherProjectile(
            Player player,
            ItemStack loaded,
            float baseSpread
    ) {
        ItemStack firedItem =
                loaded.copy();

        firedItem.setCount(1);

        Vec3 spawnPos =
                getLauncherSpawnPosition(
                        player,
                        jjf$fireRightSide
                );

        float spread =
                baseSpread
                        * getSpreadMultiplier(
                        loaded
                );

        Projectile projectile =
                createNativeLauncherProjectile(
                        player,
                        firedItem,
                        spawnPos
                );

        if (projectile == null) {
            JJFProjectileEntity jjfProjectile =
                    new JJFProjectileEntity(
                            player.level(),
                            player
                    );

            jjfProjectile.setItem(
                    firedItem
            );

            projectile =
                    jjfProjectile;
        }

        projectile.setPos(
                spawnPos.x,
                spawnPos.y,
                spawnPos.z
        );

        projectile.shootFromRotation(
                player,
                player.getXRot(),
                player.getYRot(),
                0.0F,
                getProjectileSpeed(
                        loaded
                ),
                spread
        );

        boolean spawned =
                player.level()
                        .addFreshEntity(
                                projectile
                        );

        if (spawned) {
            jjf$fireRightSide =
                    !jjf$fireRightSide;
        }

        return spawned;
    }

    private Projectile createNativeLauncherProjectile(
            Player player,
            ItemStack firedItem,
            Vec3 spawnPos
    ) {
        if (firedItem.is(Items.ARROW)
                || firedItem.is(Items.SPECTRAL_ARROW)
                || firedItem.is(Items.TIPPED_ARROW)) {
            AbstractArrow arrow =
                    ProjectileUtil.getMobArrow(
                            player,
                            firedItem,
                            1.0F
                    );

            arrow.pickup =
                    player.getAbilities().instabuild
                            ? AbstractArrow.Pickup.CREATIVE_ONLY
                            : AbstractArrow.Pickup.ALLOWED;

            return arrow;
        }

        if (firedItem.is(Items.SNOWBALL)) {
            Snowball snowball =
                    new Snowball(
                            player.level(),
                            player
                    );

            snowball.setItem(
                    firedItem
            );

            return snowball;
        }

        if (firedItem.is(Items.EGG)) {
            ThrownEgg egg =
                    new ThrownEgg(
                            player.level(),
                            player
                    );

            egg.setItem(
                    firedItem
            );

            return egg;
        }

        if (firedItem.is(Items.ENDER_PEARL)) {
            ThrownEnderpearl pearl =
                    new ThrownEnderpearl(
                            player.level(),
                            player
                    );

            pearl.setItem(
                    firedItem
            );

            return pearl;
        }

        if (firedItem.is(Items.SPLASH_POTION)
                || firedItem.is(Items.LINGERING_POTION)) {
            ThrownPotion potion =
                    new ThrownPotion(
                            player.level(),
                            player
                    );

            potion.setItem(
                    firedItem
            );

            return potion;
        }

        if (firedItem.is(Items.TRIDENT)) {
            ThrownTrident trident =
                    new ThrownTrident(
                            player.level(),
                            player,
                            firedItem
                    );

            trident.pickup =
                    player.getAbilities().instabuild
                            ? AbstractArrow.Pickup.CREATIVE_ONLY
                            : AbstractArrow.Pickup.ALLOWED;

            return trident;
        }

        if (firedItem.is(Items.FIREWORK_ROCKET)) {
            return new FireworkRocketEntity(
                    player.level(),
                    firedItem,
                    player,
                    spawnPos.x,
                    spawnPos.y,
                    spawnPos.z,
                    true
            );
        }

        return null;
    }

    private Vec3 getLauncherSpawnPosition(
            Player player,
            boolean rightSide
    ) {
        Vec3 forward =
                player.getLookAngle();

        Vec3 horizontalForward =
                new Vec3(
                        forward.x,
                        0.0D,
                        forward.z
                );

        if (horizontalForward.lengthSqr()
                > 0.0001D) {
            horizontalForward =
                    horizontalForward.normalize();
        }

        Vec3 right =
                new Vec3(
                        -horizontalForward.z,
                        0.0D,
                        horizontalForward.x
                );

        Vec3 base =
                new Vec3(
                        player.getX(),
                        player.getEyeY() - 0.15D,
                        player.getZ()
                );

        StandEntity stand =
                ((StandUser) player)
                        .roundabout$getStand();

        if (stand != null
                && !stand.isRemoved()) {
            base =
                    new Vec3(
                            stand.getX(),
                            stand.getY()
                                    + stand.getBbHeight()
                                    * 0.62D,
                            stand.getZ()
                    );
        }

        double sideOffset =
                rightSide
                        ? 0.30D
                        : -0.30D;

        return base
                .add(
                        right.scale(
                                sideOffset
                        )
                )
                .add(
                        horizontalForward.scale(
                                0.20D
                        )
                )
                .add(
                        0.0D,
                        0.06D,
                        0.0D
                );
    }

    private void consumeLoadedAmmo(
            Player player,
            JJFLauncherState launcher,
            ItemStack loaded,
            int amount
    ) {
        loaded.shrink(
                amount
        );

        if (loaded.isEmpty()) {
            launcher.clearLoadedItem();
        } else {
            launcher.setLoadedItem(
                    loaded
            );
        }

        if (player
                instanceof JJFLauncherAccess access) {
            access.jjf$setLoadedAmmoCount(
                    launcher.getLoadedCount()
            );
        }
    }

    @Override
    public boolean setPowerBarrageCharge() {
        beginSpinAttack();
        return true;
    }

    @Override
    public void setPowerBarrage() {
        beginSpinAttack();
    }

    @Override
    public void updateBarrage() {
        if (!jjf$spinAttackHeld) {
            return;
        }

        jjf$spinAttackTicks =
                Mth.clamp(
                        this.attackTimeDuring,
                        0,
                        SPIN_ATTACK_FULL_TICKS
                );

        if (this.attackTimeDuring > SPIN_ATTACK_FULL_TICKS) {
            this.attackTimeDuring = SPIN_ATTACK_FULL_TICKS;
        }

        this.setAttackTime(
                Math.max(
                        0,
                        SPIN_ATTACK_FULL_TICKS
                                - jjf$spinAttackTicks
                )
        );

        if (!(this.getSelf() instanceof Player)
                && jjf$spinAttackTicks >= SPIN_ATTACK_FULL_TICKS) {
            int chargeTicks = jjf$spinAttackTicks;
            performSpinAttack(chargeTicks);
            finishSpinAttackState();
        }
    }

    @Override
    public void barrageImpact(
            Entity entity,
            int hitNumber
    ) {
    }

    @Override
    public int getBarrageLength() {
        return SPIN_ATTACK_FULL_TICKS;
    }

    private double getSpinRawCharge(int chargeTicks) {
        return Mth.clamp(
                chargeTicks
                        / (double) SPIN_ATTACK_FULL_TICKS,
                0.0D,
                1.0D
        );
    }

    private double getSpinForceCharge(int chargeTicks) {
        double rawCharge =
                getSpinRawCharge(chargeTicks);

        return rawCharge * rawCharge;
    }

    private void releaseJJFSpinAttackClient() {
        if (!jjf$spinAttackHeld) {
            return;
        }

        int chargeTicks =
                Mth.clamp(
                        Math.max(
                                SPIN_ATTACK_MIN_TICKS,
                                jjf$spinAttackTicks
                        ),
                        SPIN_ATTACK_MIN_TICKS,
                        SPIN_ATTACK_FULL_TICKS
                );

        tryIntPowerPacket(
                POWER_SPIN_RELEASE,
                chargeTicks
        );

        finishSpinAttackState();
    }

    @Override
    public boolean tryIntPower(
            int move,
            boolean forced,
            int chargeTime
    ) {
        if (move == POWER_SPIN_RELEASE) {
            if (!this.getSelf().level().isClientSide()) {
                int serverCharge =
                        Mth.clamp(
                                Math.max(
                                        SPIN_ATTACK_MIN_TICKS,
                                        jjf$spinAttackTicks
                                ),
                                SPIN_ATTACK_MIN_TICKS,
                                SPIN_ATTACK_FULL_TICKS
                        );

                int requestedCharge =
                        Mth.clamp(
                                chargeTime,
                                SPIN_ATTACK_MIN_TICKS,
                                SPIN_ATTACK_FULL_TICKS
                        );

                int allowedCharge =
                        Mth.clamp(
                                serverCharge + 2,
                                SPIN_ATTACK_MIN_TICKS,
                                SPIN_ATTACK_FULL_TICKS
                        );

                int finalCharge =
                        Math.min(
                                requestedCharge,
                                allowedCharge
                        );

                boolean validState =
                        jjf$spinAttackHeld
                                || getActivePower() == PowerIndex.BARRAGE
                                || getActivePower() == PowerIndex.BARRAGE_CHARGE;

                if (validState) {
                    performSpinAttack(finalCharge);
                }

                finishSpinAttackState();
            }

            return true;
        }

        return super.tryIntPower(
                move,
                forced,
                chargeTime
        );
    }

    private void finishSpinAttackState() {
        jjf$spinAttackHeld = false;
        jjf$spinAttackTicks = 0;
        animateStand(StandEntity.BARRAGE_FINISHER);
        this.attackTimeDuring = -10;
        this.setAttackTimeMax(10);
    }

    private void performSpinAttack(
            int chargeTicks
    ) {
        Entity target =
                getTargetEntity(
                        this.getSelf(),
                        -1,
                        getPunchAngle()
                );

        if (target == null
                || target.distanceTo(this.getSelf()) > getReach() + 1.0F) {
            performSpinSurfaceLaunch(chargeTicks);
            return;
        }

        double rawCharge =
                getSpinRawCharge(chargeTicks);

        double forceCharge =
                getSpinForceCharge(chargeTicks);

        float damage =
                (float) (
                        SPIN_ATTACK_MIN_DAMAGE
                                + (
                                SPIN_ATTACK_MAX_DAMAGE
                                        - SPIN_ATTACK_MIN_DAMAGE
                        ) * rawCharge
                );

        boolean hit =
                StandDamageEntityAttack(
                        target,
                        damage,
                        0.0F,
                        this.getSelf()
                );

        if (!hit) {
            return;
        }

        double knockback =
                SPIN_ATTACK_MIN_KNOCKBACK
                        + (
                        SPIN_ATTACK_MAX_KNOCKBACK
                                - SPIN_ATTACK_MIN_KNOCKBACK
                ) * forceCharge;

        if (target instanceof LivingEntity living
                && living instanceof ZeroGravityAccess access
                && access.jjf$getZeroGravityState().isZeroGravity()) {
            knockback *= SPIN_ATTACK_ZERO_G_MULTIPLIER;
        }

        Vec3 direction =
                this.getSelf()
                        .getLookAngle();

        if (direction.lengthSqr() < 0.0001D) {
            direction =
                    target.position()
                            .subtract(
                                    this.getSelf().position()
                            );
        }

        if (direction.lengthSqr() < 0.0001D) {
            return;
        }

        direction = direction.normalize();

        double vertical =
                direction.y * knockback
                        + 0.16D
                        + 0.44D * forceCharge;

        target.setDeltaMovement(
                direction.x * knockback,
                Math.max(0.18D, vertical),
                direction.z * knockback
        );

        target.hasImpulse = true;
        target.hurtMarked = true;

        if (target instanceof LivingEntity living) {
            living.fallDistance = 0.0F;
        }

        applySpinRecoil(
                direction,
                forceCharge
        );
    }

    private void performSpinSurfaceLaunch(
            int chargeTicks
    ) {
        if (!(this.getSelf() instanceof Player player)) {
            return;
        }

        HitResult hit =
                player.pick(
                        getReach() + 1.25D,
                        0.0F,
                        false
                );

        if (hit.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockHitResult blockHit =
                (BlockHitResult) hit;

        Direction face =
                blockHit.getDirection();

        Vec3 normal =
                Vec3.atLowerCornerOf(
                        face.getNormal()
                );

        double forceCharge =
                getSpinForceCharge(chargeTicks);

        double strength =
                0.38D
                        + 1.27D * forceCharge;

        if (player instanceof ZeroGravityAccess access
                && access.jjf$getZeroGravityState().isZeroGravity()) {
            strength *= 1.30D;
        }

        Vec3 current =
                player.getDeltaMovement();

        Vec3 launch =
                normal.scale(strength);

        player.setDeltaMovement(
                current.x * 0.25D + launch.x,
                current.y * 0.25D + launch.y,
                current.z * 0.25D + launch.z
        );

        player.hasImpulse = true;
        player.hurtMarked = true;
        player.fallDistance = 0.0F;
    }

    private void applySpinRecoil(
            Vec3 attackDirection,
            double forceCharge
    ) {
        if (!(this.getSelf() instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isZeroGravity()) {
            return;
        }

        if (attackDirection.lengthSqr() < 0.0001D) {
            return;
        }

        double strength =
                0.08D
                        + 0.60D * forceCharge;

        Vec3 recoil =
                attackDirection
                        .normalize()
                        .scale(-strength);

        Vec3 current =
                this.getSelf()
                        .getDeltaMovement();

        this.getSelf()
                .setDeltaMovement(
                        current.add(recoil)
                );

        this.getSelf().hasImpulse = true;
        this.getSelf().hurtMarked = true;
        this.getSelf().fallDistance = 0.0F;
    }

    private void releaseOwnSpread() {
        if (this.getSelf().level().isClientSide()) {
            return;
        }

        UUID ownerUUID =
                this.getSelf().getUUID();

        AABB area =
                this.getSelf()
                        .getBoundingBox()
                        .inflate(64.0D);

        List<LivingEntity> entities =
                this.getSelf()
                        .level()
                        .getEntitiesOfClass(
                                LivingEntity.class,
                                area
                        );

        for (LivingEntity entity : entities) {
            if (!(entity instanceof ZeroGravityAccess access)) {
                continue;
            }

            ZeroGravityState state =
                    access.jjf$getZeroGravityState();

            if (state.getSource() != ZeroGravitySource.SPREAD) {
                continue;
            }

            UUID spreadOwner =
                    state.getOwnerUUID();

            if (spreadOwner == null
                    || !spreadOwner.equals(ownerUUID)) {
                continue;
            }

            state.clearZeroGravity();
            state.setRestorationTicks(20);
            entity.setNoGravity(false);
            entity.fallDistance = 0.0F;
        }
    }

    private void releaseSelfZeroGravity() {
        if (!(this.getSelf() instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();

        if (!state.isSafeUserZeroGravity()
                || !this.getSelf().getUUID().equals(state.getOwnerUUID())) {
            return;
        }

        state.clearZeroGravity();
        state.setRestorationTicks(20);
        this.getSelf().setNoGravity(false);
        this.getSelf().fallDistance = 0.0F;
        this.getSelf().hasImpulse = true;
    }

    private void toggleSelfZeroGravity() {
        if (!(this.getSelf()
                instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (state.getSource()
                == ZeroGravitySource.SPREAD) {
            return;
        }

        if (state.getSource()
                == ZeroGravitySource.USER_TOGGLE) {
            state.clearZeroGravity();
            state.setRestorationTicks(1);

            this.getSelf()
                    .setNoGravity(false);

            setCooldown(
                    PowerIndex.SKILL_1,
                    40
            );

            return;
        }

        state.setSource(
                ZeroGravitySource.USER_TOGGLE
        );

        state.setOwnerUUID(
                this.getSelf().getUUID()
        );

        state.resetZeroGravityTicks();

        this.getSelf()
                .setNoGravity(true);

        setCooldown(
                PowerIndex.SKILL_1,
                40
        );
    }

    @Override
    public void dash() {
        if (!canJJFDash()) {
            return;
        }

        boolean zeroGravityGroundDash =
                !getSelf().onGround()
                        && getSelf()
                        instanceof ZeroGravityAccess access
                        && access.jjf$getZeroGravityState()
                        .isZeroGravity()
                        && ZeroGravitySystem
                        .isTouchingGround(
                                getSelf()
                        );

        if (zeroGravityGroundDash) {
            getSelf()
                    .setOnGround(true);
        }

        super.dash();

        if (zeroGravityGroundDash) {
            Vec3 motion =
                    getSelf()
                            .getDeltaMovement();

            getSelf()
                    .setDeltaMovement(
                            motion.x,
                            0.0D,
                            motion.z
                    );

            getSelf()
                    .setOnGround(false);

            getSelf().hasImpulse = true;
        }
    }

    private boolean canJJFDash() {
        if (getSelf().onGround()) {
            return true;
        }

        if (getSelf()
                instanceof ZeroGravityAccess access) {
            return access.jjf$getZeroGravityState()
                    .isZeroGravity()
                    && ZeroGravitySystem
                    .isTouchingGround(
                            getSelf()
                    );
        }

        return false;
    }

    @Override
    public boolean isAttackIneptVisually(
            byte activeP,
            int slot
    ) {
        if (slot == 3
                && !canJJFDash()) {
            return true;
        }

        return super.isAttackIneptVisually(
                activeP,
                slot
        );
    }

    @Override
    public void renderIcons(
            GuiGraphics context,
            int x,
            int y
    ) {
        ResourceLocation zeroGravityIcon =
                NORMAL_GRAVITY_ICON;

        if (this.getSelf()
                instanceof ZeroGravityAccess access) {
            ZeroGravityState state =
                    access.jjf$getZeroGravityState();

            if (state.isSafeUserZeroGravity()) {
                zeroGravityIcon =
                        ZERO_GRAVITY_ON;
            }
        }

        setSkillIcon(
                context,
                x,
                y,
                1,
                zeroGravityIcon,
                PowerIndex.SKILL_1
        );

        setSkillIcon(
                context,
                x,
                y,
                2,
                SPIT_ICON,
                PowerIndex.SKILL_2
        );

        setSkillIcon(
                context,
                x,
                y,
                3,
                StandIcons.DODGE,
                PowerIndex.GLOBAL_DASH
        );

        ResourceLocation launcherIcon =
                LOADING_ICON;

        if (jjf$useWasDown) {
            launcherIcon =
                    SHOOTING_ICON;
        } else if (isGuarding()) {
            launcherIcon =
                    UNLOADING_ICON;
        }

        setSkillIcon(
                context,
                x,
                y,
                4,
                launcherIcon,
                PowerIndex.SKILL_4
        );
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(
            GuiGraphics context,
            float delta,
            int mouseX,
            int mouseY,
            int leftPos,
            int topPos,
            byte level,
            boolean bypass
    ) {
        List<AbilityIconInstance> icons =
                Lists.newArrayList();

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 20,
                topPos + 80,
                0,
                "ability.jjf.punch",
                "instruction.jjf.punch",
                PUNCH_ICON,
                0,
                level,
                bypass
        ));

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 20,
                topPos + 99,
                0,
                "ability.jjf.guard",
                "instruction.jjf.guard",
                GUARD_ICON,
                0,
                level,
                bypass
        ));

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 39,
                topPos + 80,
                0,
                "ability.jjf.zero_gravity",
                "instruction.jjf.zero_gravity",
                ZERO_GRAVITY_ON,
                1,
                level,
                bypass
        ));

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 39,
                topPos + 99,
                0,
                "ability.jjf.spit",
                "instruction.jjf.spit",
                SPIT_ICON,
                2,
                level,
                bypass
        ));

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 39,
                topPos + 118,
                0,
                "ability.roundabout.dodge",
                "instruction.jjf.dash",
                StandIcons.DODGE,
                3,
                level,
                bypass
        ));

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 58,
                topPos + 80,
                0,
                "ability.jjf.load",
                "instruction.jjf.load",
                LOADING_ICON,
                4,
                level,
                bypass
        ));

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 58,
                topPos + 99,
                0,
                "ability.jjf.unload",
                "instruction.jjf.unload",
                UNLOADING_ICON,
                4,
                level,
                bypass
        ));

        icons.add(drawSingleGUIIcon(
                context,
                18,
                leftPos + 58,
                topPos + 118,
                0,
                "ability.jjf.launcher",
                "instruction.jjf.launcher",
                SHOOTING_ICON,
                4,
                level,
                bypass
        ));

        return icons;
    }

    @Override
    public void renderAttackHud(
            GuiGraphics context,
            Player playerEntity,
            int scaledWidth,
            int scaledHeight,
            int ticks,
            int vehicleHeartCount,
            float flashAlpha,
            float otherFlashAlpha
    ) {
        if (!jjf$useWasDown) {
            super.renderAttackHud(
                    context,
                    playerEntity,
                    scaledWidth,
                    scaledHeight,
                    ticks,
                    vehicleHeartCount,
                    flashAlpha,
                    otherFlashAlpha
            );

            return;
        }

        int barWidth = 72;
        int barHeight = 5;

        int x =
                scaledWidth / 2
                        - barWidth / 2;

        int y =
                scaledHeight - 65;

        int ammoCount =
                playerEntity
                        instanceof JJFLauncherAccess access
                        ? access.jjf$getLoadedAmmoCount()
                        : 0;

        boolean noAmmo =
                ammoCount <= 0;

        int charge =
                Mth.clamp(
                        jjf$useHoldTicks,
                        0,
                        CONE_READY_TICKS
                );

        float progress =
                noAmmo
                        ? 0.0F
                        : (float) charge
                        / (float) CONE_READY_TICKS;

        int fillWidth =
                Mth.clamp(
                        (int) (
                                barWidth
                                        * progress
                        ),
                        0,
                        barWidth
                );

        context.fill(
                x - 1,
                y - 1,
                x + barWidth + 1,
                y + barHeight + 1,
                0xFF000000
        );

        context.fill(
                x,
                y,
                x + barWidth,
                y + barHeight,
                0xFF404040
        );

        if (fillWidth > 0) {
            context.fill(
                    x,
                    y,
                    x + fillWidth,
                    y + barHeight,
                    0xFFFFFFFF
            );
        }

        drawChargeMarker(
                context,
                x,
                y,
                barWidth,
                barHeight,
                BURST_READY_TICKS
        );

        drawChargeMarker(
                context,
                x,
                y,
                barWidth,
                barHeight,
                BURST_4_TICKS
        );

        drawChargeMarker(
                context,
                x,
                y,
                barWidth,
                barHeight,
                BURST_5_TICKS
        );

        drawChargeMarker(
                context,
                x,
                y,
                barWidth,
                barHeight,
                BURST_6_TICKS
        );

        if (JJFHudSettings.showAmmoText()) {
            String ammoText =
                    "AMMO "
                            + ammoCount;

            context.drawString(
                    Minecraft.getInstance()
                            .font,
                    ammoText,
                    x + barWidth + 6,
                    y - 2,
                    0xFFFFFF,
                    true
            );
        }

        if (JJFHudSettings.showLauncherText()) {
            String statusText = null;

            if (noAmmo) {
                statusText =
                        "NO AMMO";
            } else if (charge
                    >= CONE_READY_TICKS) {
                statusText =
                        "CONE";
            } else if (charge
                    >= BURST_6_TICKS) {
                statusText =
                        "BURST 6";
            } else if (charge
                    >= BURST_5_TICKS) {
                statusText =
                        "BURST 5";
            } else if (charge
                    >= BURST_4_TICKS) {
                statusText =
                        "BURST 4";
            } else if (charge
                    >= BURST_READY_TICKS) {
                statusText =
                        "BURST 3";
            }

            if (statusText != null) {
                int textWidth =
                        Minecraft.getInstance()
                                .font
                                .width(
                                        statusText
                                );

                context.drawString(
                        Minecraft.getInstance()
                                .font,
                        statusText,
                        scaledWidth / 2
                                - textWidth / 2,
                        y - 10,
                        0xFFFFFF,
                        true
                );
            }
        }
    }

    private void drawChargeMarker(
            GuiGraphics context,
            int x,
            int y,
            int barWidth,
            int barHeight,
            int chargeTick
    ) {
        int markerX =
                x + Mth.clamp(
                        Math.round(
                                barWidth
                                        * (
                                        chargeTick
                                                / (float) CONE_READY_TICKS
                                )
                        ),
                        0,
                        barWidth
                );

        context.fill(
                markerX,
                y - 1,
                markerX + 1,
                y + barHeight + 1,
                0xFF808080
        );
    }


    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.literal(
                "In Development"
        );
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal(
                "BoxHead"
        );
    }
}
