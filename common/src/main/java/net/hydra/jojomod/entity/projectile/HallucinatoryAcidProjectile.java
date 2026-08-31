package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.HallucinatoryAcidBlock;
import net.hydra.jojomod.block.HallucinatoryAcidBlockEntity;
import net.hydra.jojomod.block.HallucinatoryAcidWallBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import java.util.UUID;

public final class HallucinatoryAcidProjectile extends ThrowableItemProjectile {
    public static final String SKIN_TAG = "WhitesnakeAcidSkin";
    private int puddleAmount = 8;

    public HallucinatoryAcidProjectile(EntityType<? extends HallucinatoryAcidProjectile> type, Level level) {
        super(type, level);
    }

    public HallucinatoryAcidProjectile(LivingEntity owner, Level level) {
        super(ModEntities.HALLUCINATORY_ACID_PROJECTILE, owner, level);
        setItem(createSkinItem(ownerSkin(owner)));
    }

    public HallucinatoryAcidProjectile(LivingEntity owner, Level level, int puddleAmount) {
        this(owner, level);
        this.puddleAmount = Mth.clamp(puddleAmount, 1, 8);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.HALLUCINATORY_ACID_GLOB;
    }

    public static int itemSkin(ItemStack stack) {
        return stack.hasTag() ? Mth.clamp(stack.getTag().getInt(SKIN_TAG),
                WhitesnakeEntity.ANIME_SKIN, WhitesnakeEntity.SANDSNAKE_SKIN) : WhitesnakeEntity.ANIME_SKIN;
    }

    private static ItemStack createSkinItem(int skin) {
        ItemStack stack = new ItemStack(ModItems.HALLUCINATORY_ACID_GLOB);
        stack.getOrCreateTag().putInt(SKIN_TAG, Mth.clamp(skin,
                WhitesnakeEntity.ANIME_SKIN, WhitesnakeEntity.SANDSNAKE_SKIN));
        return stack;
    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);
        if (level().isClientSide()) return;
        ServerLevel server = (ServerLevel) level();
        server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                        ModBlocks.HALLUCINATORY_ACID.defaultBlockState()
                                .setValue(HallucinatoryAcidBlock.SKIN, ownerSkin(getOwner()))),
                getX(), getY(), getZ(), 18, 0.4, 0.25, 0.4, 0.25);
        playSound(random.nextBoolean() ? ModSounds.WHITESNAKE_GOO_DRIP_1_EVENT
                : ModSounds.WHITESNAKE_GOO_DRIP_2_EVENT, 1.0F, 1.0F);
        if (hit.getDirection().getAxis().isHorizontal()) placeWallAcid(hit, puddleAmount);
        else placePuddle(hit.getBlockPos(), puddleAmount);
        discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("PuddleAmount", puddleAmount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("PuddleAmount")) puddleAmount = Mth.clamp(tag.getInt("PuddleAmount"), 1, 8);
    }

    private void placePuddle(BlockPos impact, int amount) {
        int[][] layers = new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        if (!level().getBlockState(impact.above()).isAir()) layers[1][1] = -1;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (findPlacement(impact, x - 1, z - 1, 1) == null) layers[x][z] = -1;
            }
        }
        for (int i = 1; i < amount; i++) {
            for (int tries = 0; tries < 12; tries++) {
                int x = random.nextInt(3);
                int z = random.nextInt(3);
                if (layers[x][z] >= 0 && layers[x][z] < 4) {
                    layers[x][z]++;
                    break;
                }
            }
        }
        UUID ownerId = getOwner() == null ? null : getOwner().getUUID();
        int skin = ownerSkin(getOwner());
        long expiry = level().getGameTime() + ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidDespawnTime;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (layers[x][z] > 0) setAcid(impact, x - 1, z - 1, layers[x][z], ownerId, expiry, skin);
            }
        }
    }

    private void placeWallAcid(BlockHitResult hit, int amount) {
        Direction facing = hit.getDirection();
        Direction lateral = facing.getAxis() == Direction.Axis.Z ? Direction.EAST : Direction.SOUTH;
        BlockPos base = hit.getBlockPos().relative(facing);
        BlockPos[] positions = new BlockPos[]{base, base.above(), base.relative(lateral),
                base.relative(lateral.getOpposite()), base.above().relative(lateral),
                base.above().relative(lateral.getOpposite()), base.below()};
        UUID ownerId = getOwner() == null ? null : getOwner().getUUID();
        int skin = ownerSkin(getOwner());
        long expiry = level().getGameTime() + ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidDespawnTime;
        int remaining = Mth.clamp((amount + 1) / 2, 1, positions.length);
        for (BlockPos pos : positions) {
            if (remaining <= 0) break;
            BlockState old = level().getBlockState(pos);
            if (old.is(ModBlocks.HALLUCINATORY_ACID_WALL)) {
                if (old.getValue(HallucinatoryAcidWallBlock.FACING) != facing) continue;
            } else {
                BlockPos supportPos = pos.relative(facing.getOpposite());
                if (!canReplaceWithAcid(old) || !level().getBlockState(supportPos)
                        .isFaceSturdy(level(), supportPos, facing)) continue;
                level().setBlockAndUpdate(pos, ModBlocks.HALLUCINATORY_ACID_WALL.defaultBlockState()
                        .setValue(HallucinatoryAcidWallBlock.FACING, facing)
                        .setValue(HallucinatoryAcidWallBlock.SKIN, skin)
                        .setValue(HallucinatoryAcidWallBlock.WATERLOGGED,
                                old.getFluidState().is(Fluids.WATER)));
            }
            if (level().getBlockEntity(pos) instanceof HallucinatoryAcidBlockEntity acid) {
                acid.initialize(ownerId, expiry);
            }
            remaining--;
        }
    }

    private BlockPos findPlacement(BlockPos origin, int offsetX, int offsetZ, int addedLayers) {
        BlockPos selected = null;
        for (int y = -1; y < 3; y++) {
            BlockPos pos = origin.offset(offsetX, y, offsetZ);
            BlockState state = level().getBlockState(pos);
            if (state.is(ModBlocks.HALLUCINATORY_ACID)
                    && state.getValue(HallucinatoryAcidBlock.LAYERS) + addedLayers <= 4) selected = pos;
            else if (canReplaceWithAcid(state)) {
                BlockPos below = pos.below();
                BlockState support = level().getBlockState(below);
                if (support.isFaceSturdy(level(), below, Direction.UP)
                        || support.is(ModBlocks.HALLUCINATORY_ACID)
                        && support.getValue(HallucinatoryAcidBlock.LAYERS) == 4) selected = pos;
            }
        }
        return selected;
    }

    private void setAcid(BlockPos origin, int offsetX, int offsetZ, int amount, UUID ownerId, long expiry,
                         int skin) {
        BlockPos pos = findPlacement(origin, offsetX, offsetZ, amount);
        if (pos == null) return;
        BlockState old = level().getBlockState(pos);
        int total = Mth.clamp(amount + (old.is(ModBlocks.HALLUCINATORY_ACID)
                ? old.getValue(HallucinatoryAcidBlock.LAYERS) : 0), 1, 4);
        level().setBlockAndUpdate(pos, ModBlocks.HALLUCINATORY_ACID.defaultBlockState()
                .setValue(HallucinatoryAcidBlock.LAYERS, total)
                .setValue(HallucinatoryAcidBlock.SKIN, skin)
                .setValue(HallucinatoryAcidBlock.WATERLOGGED,
                        old.getFluidState().is(Fluids.WATER)));
        if (level().getBlockEntity(pos) instanceof HallucinatoryAcidBlockEntity acid) {
            acid.initialize(ownerId, expiry);
        }
    }

    public static boolean placeTrailStageOne(Level level, BlockPos origin, LivingEntity owner) {
        if (level.isClientSide()) return false;
        BlockPos selected = null;
        for (int y = -1; y < 2; y++) {
            BlockPos pos = origin.offset(0, y, 0);
            BlockState state = level.getBlockState(pos);
            if (state.is(ModBlocks.HALLUCINATORY_ACID)) {
                selected = pos;
                break;
            }
            if (canReplaceWithAcid(state)) {
                BlockPos below = pos.below();
                BlockState support = level.getBlockState(below);
                if (support.isFaceSturdy(level, below, Direction.UP)
                        || support.is(ModBlocks.HALLUCINATORY_ACID)
                        && support.getValue(HallucinatoryAcidBlock.LAYERS) == 4) {
                    selected = pos;
                    break;
                }
            }
        }
        if (selected == null) return false;

        BlockState state = level.getBlockState(selected);
        if (!state.is(ModBlocks.HALLUCINATORY_ACID)) {
            level.setBlockAndUpdate(selected, ModBlocks.HALLUCINATORY_ACID.defaultBlockState()
                    .setValue(HallucinatoryAcidBlock.LAYERS, 1)
                    .setValue(HallucinatoryAcidBlock.SKIN, ownerSkin(owner))
                    .setValue(HallucinatoryAcidBlock.WATERLOGGED,
                            state.getFluidState().is(Fluids.WATER)));
        }
        if (level.getBlockEntity(selected) instanceof HallucinatoryAcidBlockEntity acid) {
            UUID ownerId = owner == null ? null : owner.getUUID();
            acid.initialize(ownerId, level.getGameTime()
                    + ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidDespawnTime);
        }
        return true;
    }

    public static boolean placeTrailWall(Level level, BlockPos origin, LivingEntity owner,
                                         Direction gravity) {
        if (level.isClientSide() || !gravity.getAxis().isHorizontal()) return false;
        Direction facing = gravity.getOpposite();
        BlockState old = level.getBlockState(origin);
        if (old.is(ModBlocks.HALLUCINATORY_ACID_WALL)) {
            if (old.getValue(HallucinatoryAcidWallBlock.FACING) != facing) return false;
        } else {
            if (!old.canBeReplaced() || !canReplaceWithAcid(old)) return false;
            BlockPos supportPos = origin.relative(gravity);
            if (!level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing)) return false;
            level.setBlockAndUpdate(origin, ModBlocks.HALLUCINATORY_ACID_WALL.defaultBlockState()
                    .setValue(HallucinatoryAcidWallBlock.FACING, facing)
                    .setValue(HallucinatoryAcidWallBlock.SKIN, ownerSkin(owner))
                    .setValue(HallucinatoryAcidWallBlock.WATERLOGGED,
                            old.getFluidState().is(Fluids.WATER)));
        }
        if (level.getBlockEntity(origin) instanceof HallucinatoryAcidBlockEntity acid) {
            UUID ownerId = owner == null ? null : owner.getUUID();
            acid.initialize(ownerId, level.getGameTime()
                    + ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidDespawnTime);
        }
        return true;
    }

    private static boolean canReplaceWithAcid(BlockState state) {
        if (state.isAir()) return true;
        return state.getFluidState().is(Fluids.WATER)
                && !ClientNetworking.getAppropriateConfig().whitesnakeSettings.waterWashesAwayAcid;
    }

    private static int ownerSkin(Entity owner) {
        if (owner instanceof StandUser standUser) {
            return Mth.clamp(standUser.roundabout$getStandSkin(),
                    WhitesnakeEntity.ANIME_SKIN, WhitesnakeEntity.SANDSNAKE_SKIN);
        }
        return WhitesnakeEntity.ANIME_SKIN;
    }
}
