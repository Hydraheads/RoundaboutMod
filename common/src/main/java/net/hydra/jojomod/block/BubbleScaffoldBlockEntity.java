package net.hydra.jojomod.block;

import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BubbleScaffoldBlockEntity extends BlockEntity {

    public int tickCount = 0;
    public LivingEntity standuser = null;
    public int bubbleNo = 0;

    public List<Vec3> bubbleList = new ArrayList<>();
    public int getTickCount(){
        return tickCount;
    }

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    public BubbleScaffoldBlockEntity(BlockPos $$0, BlockState $$1) {
        super(ModBlocks.BUBBLE_SCAFFOLD_BLOCK_ENTITY, $$0, $$1);
    }
    public static void tickBubbleScaffold(Level lvl, BlockPos bp, BlockState bs, BubbleScaffoldBlockEntity bubble) {
        bubble.tickCount++;
            if (!lvl.isClientSide()) {
                if (bubble.standuser != null && ((StandUser)bubble.standuser).roundabout$getStandPowers() instanceof PowersSoftAndWet PW){
                    if (bs.getBlock() instanceof BubbleScaffoldBlock fb) {
                        if (bubble.tickCount > 200 || PW.bubbleNumber != bubble.bubbleNo) {
                            lvl.removeBlock(bp, false);
                            ((ServerLevel) lvl).sendParticles(ModParticles.BUBBLE_POP,
                                    bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5,
                                    7, 0.25, 0.25, 0.25, 0.2);
                            lvl.playSound(null, bp, ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
                            lvl.playSound(null, bp, ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
                            lvl.playSound(null, bp, ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
                            lvl.playSound(null, bp, ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
                            lvl.playSound(null, bp, ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
                            lvl.playSound(null, bp, ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
                            lvl.playSound(null, bp, ModSounds.BUBBLE_POP_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.9 + (Math.random() * 0.2)));
                        }
                    }
                } else {
                    if (bs.getBlock() instanceof BubbleScaffoldBlock fb) {
                        lvl.removeBlock(bp, false);
                    }
                }
            }
    }
}
