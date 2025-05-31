package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class D4CLightBlock extends BaseEntityBlock {
    public D4CLightBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new D4CLightBlockEntity(ModBlocks.D4C_LIGHT_BLOCK_ENTITY, blockPos, blockState);
    }

    private @Nullable D4CLightBlockEntity getEntity(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof D4CLightBlockEntity be ? be : null;
    }

    private @Nullable Player getOwner(Level level, BlockPos pos) {
        D4CLightBlockEntity entity = getEntity(level, pos);
        if (entity == null) return null;

        UUID ownerUUID = entity.getOwnerUUID();
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getServer().getPlayerList().getPlayer(ownerUUID);
        } else if (level.isClientSide) {
            Player clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer != null && clientPlayer.getUUID().equals(ownerUUID)) {
                return clientPlayer;
            }
        }

        return null;
    }

    @Override
    @SuppressWarnings("deprecated")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Player owner = getOwner(level, pos);
        if (owner == null) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return;
        }

        if (level.getBlockState(pos.above()).is(ModBlocks.D4C_LIGHT_BLOCK)) {
            if (((StandUser) owner).roundabout$getStandPowers() instanceof PowersD4C d4c) {
                if (!d4c.isBetweenTwoThings(pos)) {
                    d4c.ejectParallelRunning();

                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        if (!(level instanceof Level world)) return;

        Player owner = getOwner(world, pos);
        if (owner == null) return;

        if (((StandUser) owner).roundabout$getStandPowers() instanceof PowersD4C d4c) {
            d4c.ejectParallelRunning();
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Player owner = getOwner(level, pos);
        if (owner == null) return;

        if (((StandUser) owner).roundabout$getStandPowers() instanceof PowersD4C d4c) {
            if (entity instanceof AbstractArrow) {
                d4c.ejectParallelRunning();
            }
        }
    }
}