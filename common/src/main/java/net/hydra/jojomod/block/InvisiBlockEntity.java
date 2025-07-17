package net.hydra.jojomod.block;

import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InvisiBlockEntity extends BlockEntity {

    public int tickCount = 0;
    public LivingEntity standuser = null;

    public List<Vec3> bubbleList = new ArrayList<>();
    public int getTickCount(){
        return tickCount;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public InvisiBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.INVISIBLE_BLOCK_ENTITY, $$0, $$1);
    }
    public static void tickBlockEnt(Level lvl, BlockPos bp, BlockState bs, InvisiBlockEntity invisiBlockEntity) {
    }
}
