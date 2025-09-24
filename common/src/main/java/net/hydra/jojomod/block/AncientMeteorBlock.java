package net.hydra.jojomod.block;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class AncientMeteorBlock extends Block {
    public AncientMeteorBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void animateTick(BlockState $$0, Level $$1, BlockPos $$2, RandomSource $$3) {
        if ($$1.isClientSide() && ConfigManager.getClientConfig() != null &&
                ConfigManager.getClientConfig().particleSettings != null &&
                ConfigManager.getClientConfig().particleSettings.meteorsEmitParticles
        ){
            double $$4 = (double)$$2.getX();
            double $$5 = (double)$$2.getY();
            double $$6 = (double)$$2.getZ();
            if (
                    $$1.getBlockState($$2.north()).getBlock().equals(ModBlocks.IMPACT_MOUND) ||
                            $$1.getBlockState($$2.south()).getBlock().equals(ModBlocks.IMPACT_MOUND) ||
                            $$1.getBlockState($$2.east()).getBlock().equals(ModBlocks.IMPACT_MOUND) ||
                            $$1.getBlockState($$2.west()).getBlock().equals(ModBlocks.IMPACT_MOUND) ||
                            $$1.getBlockState($$2.above()).getBlock().equals(ModBlocks.IMPACT_MOUND) ||
                            $$1.getBlockState($$2.below()).getBlock().equals(ModBlocks.IMPACT_MOUND)
            ){

                for (var i = 0; i < 2; i++) {
                    double rand1 = Math.random() * 1 - 0.5;
                    double rand2 = Math.random() * 1 - 0.5;
                    double rand3 = Math.random() * 1 - 0.5;

                    double rand4 = Math.random() * 3 - 1.5;
                    double rand5 = Math.random() * 1.5;
                    double rand6 = Math.random() * 3 - 1.5;
                    $$1.addParticle(METEOR, $$4 + 0.5 + rand4, $$5 + 0.5 +rand5, $$6 + 0.5 + rand6, rand1, rand2, rand3);
                }
            }
        }
    }
    public static final Vector3f METEOR_PARTICLE_COLOR = Vec3.fromRGB24(7369075).toVector3f();
    public static final DustParticleOptions METEOR = new DustParticleOptions(METEOR_PARTICLE_COLOR, 1.0F);
}
