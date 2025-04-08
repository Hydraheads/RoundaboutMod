package net.hydra.jojomod.mixin.forge;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.SharedConstants;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(DebugScreenOverlay.class)
public abstract class DimensionResourceKeyD4CForge {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow @Nullable
    private ChunkPos lastPos;

    @Shadow public abstract void clearChunkCache();

    @Shadow protected abstract Level getLevel();

    @Shadow @Nullable protected abstract String getServerChunkStats();

    @Shadow protected abstract LevelChunk getClientChunk();

    @Shadow @Nullable protected abstract LevelChunk getServerChunk();

    @Shadow @Final private static Map<Heightmap.Types, String> HEIGHTMAP_NAMES;

    @Shadow
    private static String printBiome(Holder<Biome> $$0) {
        return null;
    }

    @Shadow @Nullable protected abstract ServerLevel getServerLevel();
    @Inject(method = "getGameInformation", at= @At(value = "HEAD"), cancellable = true)
    private void roundabout$spoofDimensionName(CallbackInfoReturnable<List<String>> cir)
    {
        if (this.minecraft.level != null && this.minecraft.level.dimension().location().toString().startsWith("roundabout:d4c-")) {
            IntegratedServer integratedserver = this.minecraft.getSingleplayerServer();
            Connection connection = this.minecraft.getConnection().getConnection();
            float f = connection.getAverageSentPackets();
            float f1 = connection.getAverageReceivedPackets();
            String s;
            if (integratedserver != null) {
                s = String.format(Locale.ROOT, "Integrated server @ %.0f ms ticks, %.0f tx, %.0f rx", integratedserver.getAverageTickTime(), f, f1);
            } else {
                s = String.format(Locale.ROOT, "\"%s\" server, %.0f tx, %.0f rx", this.minecraft.player.getServerBrand(), f, f1);
            }

            BlockPos blockpos = this.minecraft.getCameraEntity().blockPosition();
            if (this.minecraft.showOnlyReducedInfo()) {
                cir.setReturnValue(Lists.newArrayList("Minecraft " + SharedConstants.getCurrentVersion().getName() + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.minecraft.fpsString, s, this.minecraft.levelRenderer.getChunkStatistics(), this.minecraft.levelRenderer.getEntityStatistics(), "P: " + this.minecraft.particleEngine.countParticles() + ". T: " + this.minecraft.level.getEntityCount(), this.minecraft.level.gatherChunkSourceStats(), "", String.format(Locale.ROOT, "Chunk-relative: %d %d %d", blockpos.getX() & 15, blockpos.getY() & 15, blockpos.getZ() & 15)));
            } else {
                Entity entity = this.minecraft.getCameraEntity();
                Direction direction = entity.getDirection();
                String s1;
                switch (direction) {
                    case NORTH:
                        s1 = "Towards negative Z";
                        break;
                    case SOUTH:
                        s1 = "Towards positive Z";
                        break;
                    case WEST:
                        s1 = "Towards negative X";
                        break;
                    case EAST:
                        s1 = "Towards positive X";
                        break;
                    default:
                        s1 = "Invalid";
                }

                ChunkPos chunkpos = new ChunkPos(blockpos);
                if (!Objects.equals(this.lastPos, chunkpos)) {
                    this.lastPos = chunkpos;
                    this.clearChunkCache();
                }

                Level level = this.getLevel();
                LongSet longset = (LongSet) (level instanceof ServerLevel ? ((ServerLevel) level).getForcedChunks() : LongSets.EMPTY_SET);
                List<String> list = Lists.newArrayList("Minecraft " + SharedConstants.getCurrentVersion().getName() + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType()) + ")", this.minecraft.fpsString, s, this.minecraft.levelRenderer.getChunkStatistics(), this.minecraft.levelRenderer.getEntityStatistics(), "P: " + this.minecraft.particleEngine.countParticles() + ". T: " + this.minecraft.level.getEntityCount(), this.minecraft.level.gatherChunkSourceStats());
                String s2 = this.getServerChunkStats();
                if (s2 != null) {
                    list.add(s2);
                }

                list.add(new ResourceLocation("overworld") + " FC: " + longset.size());
                list.add("");
                list.add(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", this.minecraft.getCameraEntity().getX(), this.minecraft.getCameraEntity().getY(), this.minecraft.getCameraEntity().getZ()));
                list.add(String.format(Locale.ROOT, "Block: %d %d %d [%d %d %d]", blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() & 15, blockpos.getY() & 15, blockpos.getZ() & 15));
                list.add(String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", chunkpos.x, SectionPos.blockToSectionCoord(blockpos.getY()), chunkpos.z, chunkpos.getRegionLocalX(), chunkpos.getRegionLocalZ(), chunkpos.getRegionX(), chunkpos.getRegionZ()));
                list.add(String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", direction, s1, Mth.wrapDegrees(entity.getYRot()), Mth.wrapDegrees(entity.getXRot())));
                LevelChunk levelchunk = this.getClientChunk();
                if (levelchunk.isEmpty()) {
                    list.add("Waiting for chunk...");
                } else {
                    int i = this.minecraft.level.getChunkSource().getLightEngine().getRawBrightness(blockpos, 0);
                    int j = this.minecraft.level.getBrightness(LightLayer.SKY, blockpos);
                    int k = this.minecraft.level.getBrightness(LightLayer.BLOCK, blockpos);
                    list.add("Client Light: " + i + " (" + j + " sky, " + k + " block)");
                    LevelChunk levelchunk1 = this.getServerChunk();
                    StringBuilder stringbuilder = new StringBuilder("CH");

                    for (Heightmap.Types heightmap$types : Heightmap.Types.values()) {
                        if (heightmap$types.sendToClient()) {
                            stringbuilder.append(" ").append(HEIGHTMAP_NAMES.get(heightmap$types)).append(": ").append(levelchunk.getHeight(heightmap$types, blockpos.getX(), blockpos.getZ()));
                        }
                    }

                    list.add(stringbuilder.toString());
                    stringbuilder.setLength(0);
                    stringbuilder.append("SH");

                    for (Heightmap.Types heightmap$types1 : Heightmap.Types.values()) {
                        if (heightmap$types1.keepAfterWorldgen()) {
                            stringbuilder.append(" ").append(HEIGHTMAP_NAMES.get(heightmap$types1)).append(": ");
                            if (levelchunk1 != null) {
                                stringbuilder.append(levelchunk1.getHeight(heightmap$types1, blockpos.getX(), blockpos.getZ()));
                            } else {
                                stringbuilder.append("??");
                            }
                        }
                    }

                    list.add(stringbuilder.toString());
                    if (blockpos.getY() >= this.minecraft.level.getMinBuildHeight() && blockpos.getY() < this.minecraft.level.getMaxBuildHeight()) {
                        list.add("Biome: " + printBiome(this.minecraft.level.getBiome(blockpos)));
                        long l = 0L;
                        float f2 = 0.0F;
                        if (levelchunk1 != null) {
                            f2 = level.getMoonBrightness();
                            l = levelchunk1.getInhabitedTime();
                        }

                        DifficultyInstance difficultyinstance = new DifficultyInstance(level.getDifficulty(), level.getDayTime(), l, f2);
                        list.add(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f (Day %d)", difficultyinstance.getEffectiveDifficulty(), difficultyinstance.getSpecialMultiplier(), this.minecraft.level.getDayTime() / 24000L));
                    }

                    if (levelchunk1 != null && levelchunk1.isOldNoiseGeneration()) {
                        list.add("Blending: Old");
                    }
                }

                ServerLevel serverlevel = this.getServerLevel();
                if (serverlevel != null) {
                    ServerChunkCache serverchunkcache = serverlevel.getChunkSource();
                    ChunkGenerator chunkgenerator = serverchunkcache.getGenerator();
                    RandomState randomstate = serverchunkcache.randomState();
                    chunkgenerator.addDebugScreenInfo(list, randomstate, blockpos);
                    Climate.Sampler climate$sampler = randomstate.sampler();
                    BiomeSource biomesource = chunkgenerator.getBiomeSource();
                    biomesource.addDebugInfo(list, blockpos, climate$sampler);
                    NaturalSpawner.SpawnState naturalspawner$spawnstate = serverchunkcache.getLastSpawnState();
                    if (naturalspawner$spawnstate != null) {
                        Object2IntMap<MobCategory> object2intmap = naturalspawner$spawnstate.getMobCategoryCounts();
                        int i1 = naturalspawner$spawnstate.getSpawnableChunkCount();
                        list.add("SC: " + i1 + ", " + (String) Stream.of(MobCategory.values()).map((p_94068_) -> {
                            return Character.toUpperCase(p_94068_.getName().charAt(0)) + ": " + object2intmap.getInt(p_94068_);
                        }).collect(Collectors.joining(", ")));
                    } else {
                        list.add("SC: N/A");
                    }
                }

                PostChain postchain = this.minecraft.gameRenderer.currentEffect();
                if (postchain != null) {
                    list.add("Shader: " + postchain.getName());
                }

                list.add(this.minecraft.getSoundManager().getDebugString() + String.format(Locale.ROOT, " (Mood %d%%)", Math.round(this.minecraft.player.getCurrentMood() * 100.0F)));
                cir.setReturnValue(list);
            }
        }
    }
}
