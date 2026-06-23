package net.hydra.jojomod.block;

import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StickyIceCoatingBlock
        extends WhiteAlbumCoatingBlock {
    public StickyIceCoatingBlock(Properties properties) {
        super(properties);
    }

    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$3 instanceof LivingEntity LE && !LE.isInvulnerable() && !MainUtil.isBossMob($$3)) {
            if (!(((StandUser)LE).roundabout$getStandPowers() instanceof PowersWhiteAlbum PW &&
                    PowerTypes.hasStandActive(LE))) {
                if (FateTypes.isVampire(LE)) {
                    $$3.makeStuckInBlock($$0, new Vec3((double) 0.3F, (double) 0.3F, (double) 0.3F));
                } else {
                    $$3.makeStuckInBlock($$0, new Vec3((double) 0.6F, (double) 0.8F, (double) 0.6F));
                }
            }
            if (!$$1.isClientSide) {
                //HeatUtil.addHeat($$3,-1);
            }
        }
    }

    @Override
    public int range1(){
        return 2;
    }
    @Override
    public int range2(){
        return 4;
    }
}