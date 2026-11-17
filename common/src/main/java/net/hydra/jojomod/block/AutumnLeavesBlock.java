
package net.hydra.jojomod.block;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.WeakHashMap;

public class AutumnLeavesBlock extends BushBlock {

    public static final DirectionProperty FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE =
            box(0, 0, 0, 16, 0.5, 16);

    private static final int TRAVEL_DISTANCE = 12;

    public static final BooleanProperty COOLDOWN =
            BooleanProperty.create("cooldown");

    private static final int COOLDOWN_TICKS = 60;
    private static final int SOUND_COOLDOWN_TICKS = 10;

    private static final Map<Entity, Long> LAST_MOVEMENT =
            new WeakHashMap<>();

    private static final int COMBO_RESET_TICKS = 10;

    private static int getMaxCombo() {
        return ClientNetworking.getAppropriateConfig()
                .AutumnLeavesSettings.MaxCombo;
    }

    private static int getCooldownAfterCombo() {
        return ClientNetworking.getAppropriateConfig()
                .AutumnLeavesSettings.CooldownAfterCombo;
    }

    private static final Map<Entity, Integer> COMBO_COUNT =
            new WeakHashMap<>();

    private static final Map<Entity, Long> LAST_COMBO_TICK =
            new WeakHashMap<>();

    private static final Map<Entity, Long> COMBO_BLOCKED_UNTIL =
            new WeakHashMap<>();

    public AutumnLeavesBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(COOLDOWN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COOLDOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(COOLDOWN, true);
    }
    @Override
    public void onPlace(BlockState state, Level level,
                        BlockPos pos, BlockState oldState,
                        boolean isMoving) {

        super.onPlace(state, level, pos, oldState, isMoving);

        if (!level.isClientSide
                && !oldState.is(this)
                && state.getValue(COOLDOWN)) {

            level.scheduleTick(pos, this, COOLDOWN_TICKS);
        }
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level,
                                 BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        if (level.isClientSide || state.getValue(COOLDOWN)) {
            return;
        }

        long currentTick = level.getGameTime();

        // lets block that specific entitiy from using al
        long blockedUntil = COMBO_BLOCKED_UNTIL.getOrDefault(entity, 0L);

        if (currentTick < blockedUntil) {
            return;
        }

        long lastComboTick = LAST_COMBO_TICK.getOrDefault(entity, -1000L);

        if (currentTick - lastComboTick >= COMBO_RESET_TICKS) {
            COMBO_COUNT.remove(entity);
        }

        int combo = COMBO_COUNT.getOrDefault(entity, 0);

        if (combo >= getMaxCombo()) {
            COMBO_BLOCKED_UNTIL.put(
                    entity,
                    currentTick + getCooldownAfterCombo()
            );

            COMBO_COUNT.remove(entity);
            LAST_COMBO_TICK.remove(entity);
            return;
        }

        level.setBlock(pos, state.setValue(COOLDOWN, true), 3);
        level.scheduleTick(pos, this, COOLDOWN_TICKS);

        Direction direction = state.getValue(FACING);

        double startX = entity.getX();
        double startY = entity.getY();
        double startZ = entity.getZ();
        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        double currentY = pos.getY();

        double lastSafeX = startX;
        double lastSafeY = startY;
        double lastSafeZ = startZ;

        boolean shouldFall = false;

        if (entity instanceof ItemEntity itemEntity) {
            itemEntity.setNoPickUpDelay();
        }

        for (int distance = 1; distance <= TRAVEL_DISTANCE; distance++) {

            double nextX = centerX + direction.getStepX() * distance;
            double nextZ = centerZ + direction.getStepZ() * distance;

            boolean foundValidPosition = false;
            double nextY = currentY;

            double[] heightOffsets = {0.0, 1.0, -1.0};

            for (double offset : heightOffsets) {

                double candidateY = currentY + offset;

                AABB candidateBox = entity.getBoundingBox().move(
                        nextX - startX,
                        candidateY - startY,
                        nextZ - startZ
                );

                if (!level.noCollision(entity, candidateBox)) {
                    continue;
                }

                AABB floorBox = candidateBox.move(0, -0.1, 0);

                boolean hasGround = !level.noCollision(entity, floorBox);

                BlockPos feetPos = BlockPos.containing(
                        nextX,
                        candidateY + 0.01,
                        nextZ
                );

                // Ignore the Autumn Leaves block that launched the entity.
                boolean isLeaves = !feetPos.equals(pos)
                        && level.getBlockState(feetPos)
                        .getBlock() instanceof AutumnLeavesBlock;

                if (hasGround || isLeaves) {
                    nextY = candidateY;
                    foundValidPosition = true;
                    break;
                }
            }

            if (!foundValidPosition) {

                AABB forwardBox = entity.getBoundingBox().move(
                        nextX - startX,
                        currentY - startY,
                        nextZ - startZ
                );

                if (level.noCollision(entity, forwardBox)) {
                    lastSafeX = nextX;
                    lastSafeY = currentY;
                    lastSafeZ = nextZ;
                    shouldFall = true;
                }

                break;
            }

            currentY = nextY;

            // Allow players to collect items along the path.
            if (entity instanceof ItemEntity itemEntity) {

                AABB pickupBox = entity.getBoundingBox().move(
                        nextX - startX,
                        currentY - startY,
                        nextZ - startZ
                ).inflate(0.5, 0.25, 0.5);

                for (Player player : level.getEntitiesOfClass(
                        Player.class, pickupBox)) {

                    if (player.isSpectator() || !player.isAlive()) {
                        continue;
                    }

                    itemEntity.playerTouch(player);

                    if (itemEntity.isRemoved()) {
                        return;
                    }
                }
            }

            BlockPos leavesPos = BlockPos.containing(
                    nextX,
                    currentY + 0.01,
                    nextZ
            );

            BlockState leavesState = level.getBlockState(leavesPos);

            // Only stop at a different, charged Autumn Leaves block.
            // This allows combos without stopping at the original block.
            if (!leavesPos.equals(pos)
                    && leavesState.getBlock() instanceof AutumnLeavesBlock
                    && !leavesState.getValue(COOLDOWN)) {

                lastSafeX = leavesPos.getX() + 0.5;
                lastSafeY = currentY;
                lastSafeZ = leavesPos.getZ() + 0.5;

                break;
            }

            lastSafeX = nextX;
            lastSafeY = currentY;
            lastSafeZ = nextZ;
        }

        if (lastSafeX != startX
                || lastSafeY != startY
                || lastSafeZ != startZ) {

            COMBO_COUNT.put(entity, combo + 1);
            LAST_COMBO_TICK.put(entity, currentTick);

            playMovementSound(level, pos, entity);
            playMovementParticles(level, pos, direction);

            entity.teleportTo(
                    lastSafeX,
                    lastSafeY,
                    lastSafeZ
            );

            entity.setDeltaMovement(
                    entity.getDeltaMovement().x,
                    shouldFall ? -0.1 : 0.0,
                    entity.getDeltaMovement().z
            );

            entity.fallDistance = 0.0F;
            entity.setOnGround(!shouldFall);
            entity.hurtMarked = true;
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level,
                     BlockPos pos, RandomSource random) {

        if (state.getValue(COOLDOWN)) {

            level.setBlock(
                    pos,
                    state.setValue(COOLDOWN, false),
                    3
            );

            level.playSound(
                    null,
                    pos,
                    ModSounds.AL_CHARGE_EVENT,
                    SoundSource.BLOCKS,
                    0.7F,
                    1.2F
            );

            for (int i = 0; i < 12; i++) {

                double x = pos.getX() + random.nextDouble();
                double y = pos.getY() + 0.1;
                double z = pos.getZ() + random.nextDouble();

                level.sendParticles(
                        ModParticles.AUTUMN_LEAVES,
                        x, y, z,
                        0,
                        0.0, 0.025, 0.0,
                        1.0
                );
            }
        }
    }
    private void playMovementSound(Level level, BlockPos pos, Entity entity) {

        long currentTick = level.getGameTime();

        Long lastMovement = LAST_MOVEMENT.get(entity);

        if (lastMovement == null
                || currentTick - lastMovement >= SOUND_COOLDOWN_TICKS) {

            level.playSound(
                    null,
                    entity,
                    ModSounds.AL_MOVE_EVENT,
                    SoundSource.PLAYERS,
                    0.7F,
                    1.0F
            );
        }

        LAST_MOVEMENT.put(entity, currentTick);
    }
    private void playMovementParticles(Level level, BlockPos pos,
                                       Direction direction) {

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        RandomSource random = level.getRandom();

        for (int i = 0; i < 20; i++) {

            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 0.1;
            double z = pos.getZ() + random.nextDouble();

            double speed = 0.25 + random.nextDouble() * 0.10;

            double vx = direction.getStepX() * speed
                    + (random.nextDouble() - 0.5) * 0.02;

            double vy = 0.0;

            double vz = direction.getStepZ() * speed
                    + (random.nextDouble() - 0.5) * 0.02;

            serverLevel.sendParticles(
                    ModParticles.AUTUMN_LEAVES,
                    x, y, z,
                    0,
                    vx, vy, vz,
                    1.0
            );
        }
    }
}
