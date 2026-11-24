package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;

public class KingNothingEntity extends FollowingStandEntity {

    /**
     * Initialize Stands
     *
     * @param entityType
     * @param world
     */
    public KingNothingEntity(EntityType<? extends Mob> entityType,
                                Level world) {
        super(entityType, world);
    }

    public static final byte
            MANGA = 1,
            SPINE = 2,
            NOTE = 3,
            BETA = 4,
            WARDEN = 5,
            SNIFFER = 6,
            SOMETHING = 7,
            RAM = 8,
            CHRISTMAS = 9;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            byte animation = this.getAnimation();
            if (getSkin() == RAM) {
                this.hideHorns.stop();
            }
            else {
                this.hideHorns.startIfStopped(this.tickCount);
            }
            if (animation == REACH) {
                this.reach.startIfStopped(this.tickCount);
            }
            else {
                this.reach.stop();
            }

            if (animation == DISINTEGRATE) {
                this.disintegrate.startIfStopped(this.tickCount);
                switch (getIdleAnimation()) {
                    case 0 -> this.idleAnimationState.startIfStopped(this.tickCount);
                    case 1 -> this.idleAnimationState2.startIfStopped(this.tickCount);
                    case 2 -> this.idleAnimationState3.startIfStopped(this.tickCount);
                    case 3 -> this.idleAnimationState4.startIfStopped(this.tickCount);
                    case 4 -> this.idleAnimationStarPlat1.startIfStopped(this.tickCount);
                    case 5 -> this.idleAnimationStarPlat2.startIfStopped(this.tickCount);
                    case 6 -> this.idleAnimationMagRed1.startIfStopped(this.tickCount);
                    case 7 -> this.idleAnimationMagRed2.startIfStopped(this.tickCount);
                    case 8 -> this.idleAnimationTheWorld1.startIfStopped(this.tickCount);
                    case 9 -> this.idleAnimationTheWorld2.startIfStopped(this.tickCount);
                    case 10 -> this.idleAnimationKillerQueen1.startIfStopped(this.tickCount);
                    case 11 -> this.idleAnimationKillerQueen2.startIfStopped(this.tickCount);
                    case 12 -> this.idleAnimationPurpleHaze1.startIfStopped(this.tickCount);
                    case 13 -> this.idleAnimationPurpleHaze2.startIfStopped(this.tickCount);
                    case 14 -> this.idleAnimationKingCrimson1.startIfStopped(this.tickCount);
                    case 15 -> this.idleAnimationKingCrimson2.startIfStopped(this.tickCount);
                    case 16 -> this.idleAnimationWhiteSnake1.startIfStopped(this.tickCount);
                    case 17 -> this.idleAnimationWhiteSnake2.startIfStopped(this.tickCount);
                    case 18 -> this.idleAnimationDiverDown1.startIfStopped(this.tickCount);
                    case 19 -> this.idleAnimationDiverDown2.startIfStopped(this.tickCount);
                    case 20 -> this.idleAnimationD4C1.startIfStopped(this.tickCount);
                    case 21 -> this.idleAnimationD4C2.startIfStopped(this.tickCount);
                    case 22 -> this.idleAnimationSoftAndWet1.startIfStopped(this.tickCount);
                    case 23 -> this.idleAnimationSoftAndWet2.startIfStopped(this.tickCount);
                    case 24 -> this.idleAnimationKneel.startIfStopped(this.tickCount);
                    case 25 -> this.idleAnimationBrave.startIfStopped(this.tickCount);
                    case 26 -> this.idleAnimationReborn.startIfStopped(this.tickCount);
                    case 27 -> this.idleAnimationPatriot.startIfStopped(this.tickCount);
                    case 28 -> this.idleAnimationRelaxed.startIfStopped(this.tickCount);
                    case 29 -> this.idleAnimationCrazyDiamond1.startIfStopped(this.tickCount);
                    case 30 -> this.idleAnimationCrazyDiamond2.startIfStopped(this.tickCount);
                    case 31 -> this.idleAnimationGoldenExperience1.startIfStopped(this.tickCount);
                    case 32 -> this.idleAnimationGoldenExperience2.startIfStopped(this.tickCount);
                    case 33 -> this.idleAnimationStickyFingers1.startIfStopped(this.tickCount);
                    case 34 -> this.idleAnimationStickyFingers2.startIfStopped(this.tickCount);
                }
            }
            else {
                this.disintegrate.stop();
            }

            if (animation == RETURN) {
                this.returning.startIfStopped(this.tickCount);
                switch (getIdleAnimation()) {
                    case 0 -> this.idleAnimationState.startIfStopped(this.tickCount);
                    case 1 -> this.idleAnimationState2.startIfStopped(this.tickCount);
                    case 2 -> this.idleAnimationState3.startIfStopped(this.tickCount);
                    case 3 -> this.idleAnimationState4.startIfStopped(this.tickCount);
                    case 4 -> this.idleAnimationStarPlat1.startIfStopped(this.tickCount);
                    case 5 -> this.idleAnimationStarPlat2.startIfStopped(this.tickCount);
                    case 6 -> this.idleAnimationMagRed1.startIfStopped(this.tickCount);
                    case 7 -> this.idleAnimationMagRed2.startIfStopped(this.tickCount);
                    case 8 -> this.idleAnimationTheWorld1.startIfStopped(this.tickCount);
                    case 9 -> this.idleAnimationTheWorld2.startIfStopped(this.tickCount);
                    case 10 -> this.idleAnimationKillerQueen1.startIfStopped(this.tickCount);
                    case 11 -> this.idleAnimationKillerQueen2.startIfStopped(this.tickCount);
                    case 12 -> this.idleAnimationPurpleHaze1.startIfStopped(this.tickCount);
                    case 13 -> this.idleAnimationPurpleHaze2.startIfStopped(this.tickCount);
                    case 14 -> this.idleAnimationKingCrimson1.startIfStopped(this.tickCount);
                    case 15 -> this.idleAnimationKingCrimson2.startIfStopped(this.tickCount);
                    case 16 -> this.idleAnimationWhiteSnake1.startIfStopped(this.tickCount);
                    case 17 -> this.idleAnimationWhiteSnake2.startIfStopped(this.tickCount);
                    case 18 -> this.idleAnimationDiverDown1.startIfStopped(this.tickCount);
                    case 19 -> this.idleAnimationDiverDown2.startIfStopped(this.tickCount);
                    case 20 -> this.idleAnimationD4C1.startIfStopped(this.tickCount);
                    case 21 -> this.idleAnimationD4C2.startIfStopped(this.tickCount);
                    case 22 -> this.idleAnimationSoftAndWet1.startIfStopped(this.tickCount);
                    case 23 -> this.idleAnimationSoftAndWet2.startIfStopped(this.tickCount);
                    case 24 -> this.idleAnimationKneel.startIfStopped(this.tickCount);
                    case 25 -> this.idleAnimationBrave.startIfStopped(this.tickCount);
                    case 26 -> this.idleAnimationReborn.startIfStopped(this.tickCount);
                    case 27 -> this.idleAnimationPatriot.startIfStopped(this.tickCount);
                    case 28 -> this.idleAnimationRelaxed.startIfStopped(this.tickCount);
                    case 29 -> this.idleAnimationCrazyDiamond1.startIfStopped(this.tickCount);
                    case 30 -> this.idleAnimationCrazyDiamond2.startIfStopped(this.tickCount);
                    case 31 -> this.idleAnimationGoldenExperience1.startIfStopped(this.tickCount);
                    case 32 -> this.idleAnimationGoldenExperience2.startIfStopped(this.tickCount);
                    case 33 -> this.idleAnimationStickyFingers1.startIfStopped(this.tickCount);
                    case 34 -> this.idleAnimationStickyFingers2.startIfStopped(this.tickCount);
                }
            }
            else {
                this.returning.stop();
            }

            if (animation == SUMMON) {
                this.summon.startIfStopped(this.tickCount);
                switch (getIdleAnimation()) {
                    case 0 -> this.idleAnimationState.startIfStopped(this.tickCount);
                    case 1 -> this.idleAnimationState2.startIfStopped(this.tickCount);
                    case 2 -> this.idleAnimationState3.startIfStopped(this.tickCount);
                    case 3 -> this.idleAnimationState4.startIfStopped(this.tickCount);
                    case 4 -> this.idleAnimationStarPlat1.startIfStopped(this.tickCount);
                    case 5 -> this.idleAnimationStarPlat2.startIfStopped(this.tickCount);
                    case 6 -> this.idleAnimationMagRed1.startIfStopped(this.tickCount);
                    case 7 -> this.idleAnimationMagRed2.startIfStopped(this.tickCount);
                    case 8 -> this.idleAnimationTheWorld1.startIfStopped(this.tickCount);
                    case 9 -> this.idleAnimationTheWorld2.startIfStopped(this.tickCount);
                    case 10 -> this.idleAnimationKillerQueen1.startIfStopped(this.tickCount);
                    case 11 -> this.idleAnimationKillerQueen2.startIfStopped(this.tickCount);
                    case 12 -> this.idleAnimationPurpleHaze1.startIfStopped(this.tickCount);
                    case 13 -> this.idleAnimationPurpleHaze2.startIfStopped(this.tickCount);
                    case 14 -> this.idleAnimationKingCrimson1.startIfStopped(this.tickCount);
                    case 15 -> this.idleAnimationKingCrimson2.startIfStopped(this.tickCount);
                    case 16 -> this.idleAnimationWhiteSnake1.startIfStopped(this.tickCount);
                    case 17 -> this.idleAnimationWhiteSnake2.startIfStopped(this.tickCount);
                    case 18 -> this.idleAnimationDiverDown1.startIfStopped(this.tickCount);
                    case 19 -> this.idleAnimationDiverDown2.startIfStopped(this.tickCount);
                    case 20 -> this.idleAnimationD4C1.startIfStopped(this.tickCount);
                    case 21 -> this.idleAnimationD4C2.startIfStopped(this.tickCount);
                    case 22 -> this.idleAnimationSoftAndWet1.startIfStopped(this.tickCount);
                    case 23 -> this.idleAnimationSoftAndWet2.startIfStopped(this.tickCount);
                    case 24 -> this.idleAnimationKneel.startIfStopped(this.tickCount);
                    case 25 -> this.idleAnimationBrave.startIfStopped(this.tickCount);
                    case 26 -> this.idleAnimationReborn.startIfStopped(this.tickCount);
                    case 27 -> this.idleAnimationPatriot.startIfStopped(this.tickCount);
                    case 28 -> this.idleAnimationRelaxed.startIfStopped(this.tickCount);
                    case 29 -> this.idleAnimationCrazyDiamond1.startIfStopped(this.tickCount);
                    case 30 -> this.idleAnimationCrazyDiamond2.startIfStopped(this.tickCount);
                    case 31 -> this.idleAnimationGoldenExperience1.startIfStopped(this.tickCount);
                    case 32 -> this.idleAnimationGoldenExperience2.startIfStopped(this.tickCount);
                    case 33 -> this.idleAnimationStickyFingers1.startIfStopped(this.tickCount);
                    case 34 -> this.idleAnimationStickyFingers2.startIfStopped(this.tickCount);
                }
            }
            else {
                this.summon.stop();
            }
        }
    }

    public static final byte
            REACH = 51,
            DISINTEGRATE = 52,
            RETURN = 53,
            SUMMON = 54;

    public final AnimationState disintegrate = new AnimationState();
    public final AnimationState returning = new AnimationState();
    public final AnimationState summon = new AnimationState();
    public final AnimationState reach = new AnimationState();
    public final AnimationState hideHorns = new AnimationState();

    @Override
    public @NotNull ItemStack getItemBySlot(EquipmentSlot $$0) {
        if ($$0.equals(EquipmentSlot.MAINHAND)) {
            return getHeldItem();
        }
        return super.getItemBySlot($$0);
    }

    @Override
    public @NotNull ItemStack getMainHandItem() {
        return getHeldItem();
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return StandEntity.createStandAttributes().add(Attributes.ATTACK_SPEED);
    }

    @Override
    public boolean isTechnicallyInImpassableWall() {
        float $$0 = this.getDimensions(this.getPose()).width * 0.8F;
        AABB $$1 = AABB.ofSize(this.getBoundingBox().getCenter(), (double)$$0, 1.0E-6, (double)$$0);
        return BlockPos.betweenClosedStream($$1)
                .anyMatch(
                        $$1x -> {
                            BlockState $$2 = this.level().getBlockState($$1x);
                            return !$$2.isAir() && !($$2.getBlock() instanceof IronBarsBlock)
                                    && !($$2.getBlock() instanceof FenceBlock)
                                    && !($$2.getBlock() instanceof FenceGateBlock)
                                    && !($$2.getBlock() instanceof SlabBlock)
                                    && !($$2.getBlock() instanceof AnvilBlock) && !($$2.getBlock() instanceof BellBlock)
                                    && !($$2.getBlock() instanceof RodBlock) && $$2.isSuffocating(this.level(), $$1x)
                                    && Shapes.joinIsNotEmpty(
                                    $$2.getCollisionShape(this.level(), $$1x).move($$1x.getX(),
                                            $$1x.getY(), $$1x.getZ()), Shapes.create($$1),
                                    BooleanOp.AND)
                                    || ($$2.getBlock() instanceof GlassBlock);
                        }
                );
    }

    @Override
    public boolean isTechnicallyInWall() {
        float $$0 = this.getDimensions(this.getPose()).width * 0.8F;
        AABB $$1 = AABB.ofSize(this.getBoundingBox().getCenter(), (double)$$0, 1.0E-6, (double)$$0);
        return BlockPos.betweenClosedStream($$1)
                .anyMatch(
                        $$1x -> {
                            BlockState $$2 = this.level().getBlockState($$1x);
                            return !$$2.isAir()
                                    && $$2.isSuffocating(this.level(), $$1x)
                                    && Shapes.joinIsNotEmpty(
                                    $$2.getCollisionShape(this.level(), $$1x).move((double)$$1x.getX(), (double)$$1x.getY(), (double)$$1x.getZ()),
                                    Shapes.create($$1),
                                    BooleanOp.AND)
                                    || ($$2.getBlock() instanceof GlassBlock);
                        }
                );
    }
}
