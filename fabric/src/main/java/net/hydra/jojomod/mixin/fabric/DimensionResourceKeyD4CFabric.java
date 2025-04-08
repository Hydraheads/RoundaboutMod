package net.hydra.jojomod.mixin.fabric;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
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
import net.minecraft.resources.ResourceKey;
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
public abstract class DimensionResourceKeyD4CFabric {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Nullable private ChunkPos lastPos;

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


            IntegratedServer $$0 = this.minecraft.getSingleplayerServer();
            Connection $$1 = this.minecraft.getConnection().getConnection();
            float $$2 = $$1.getAverageSentPackets();
            float $$3 = $$1.getAverageReceivedPackets();
            String $$5;
            if ($$0 != null) {
                $$5 = String.format(Locale.ROOT, "Integrated server @ %.0f ms ticks, %.0f tx, %.0f rx", $$0.getAverageTickTime(), $$2, $$3);
            } else {
                $$5 = String.format(Locale.ROOT, "\"%s\" server, %.0f tx, %.0f rx", this.minecraft.player.getServerBrand(), $$2, $$3);
            }

            BlockPos $$6 = this.minecraft.getCameraEntity().blockPosition();
            String[] var10000;
            String var10003;
            if (this.minecraft.showOnlyReducedInfo()) {
                var10000 = new String[9];
                var10003 = SharedConstants.getCurrentVersion().getName();
                var10000[0] = "Minecraft " + var10003 + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ")";
                var10000[1] = this.minecraft.fpsString;
                var10000[2] = $$5;
                var10000[3] = this.minecraft.levelRenderer.getChunkStatistics();
                var10000[4] = this.minecraft.levelRenderer.getEntityStatistics();
                var10003 = this.minecraft.particleEngine.countParticles();
                var10000[5] = "P: " + var10003 + ". T: " + this.minecraft.level.getEntityCount();
                var10000[6] = this.minecraft.level.gatherChunkSourceStats();
                var10000[7] = "";
                var10000[8] = String.format(Locale.ROOT, "Chunk-relative: %d %d %d", $$6.getX() & 15, $$6.getY() & 15, $$6.getZ() & 15);

                List<String> slist = Lists.newArrayList(var10000);
                if (RendererAccess.INSTANCE.hasRenderer()) {
                    slist.add("[Fabric] Active renderer: " + RendererAccess.INSTANCE.getRenderer().getClass().getSimpleName());
                } else {
                    slist.add("[Fabric] Active renderer: none (vanilla)");
                }
                cir.setReturnValue(slist);
            } else {
                Entity $$7 = this.minecraft.getCameraEntity();
                Direction $$8 = $$7.getDirection();
                String $$13;
                switch ($$8) {
                    case NORTH:
                        $$13 = "Towards negative Z";
                        break;
                    case SOUTH:
                        $$13 = "Towards positive Z";
                        break;
                    case WEST:
                        $$13 = "Towards negative X";
                        break;
                    case EAST:
                        $$13 = "Towards positive X";
                        break;
                    default:
                        $$13 = "Invalid";
                }

                ChunkPos $$14 = new ChunkPos($$6);
                if (!Objects.equals(this.lastPos, $$14)) {
                    this.lastPos = $$14;
                    this.clearChunkCache();
                }

                Level $$15 = this.getLevel();
                LongSet $$16 = $$15 instanceof ServerLevel ? ((ServerLevel) $$15).getForcedChunks() : LongSets.EMPTY_SET;
                var10000 = new String[7];
                var10003 = SharedConstants.getCurrentVersion().getName();
                var10000[0] = "Minecraft " + var10003 + " (" + this.minecraft.getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType()) + ")";
                var10000[1] = this.minecraft.fpsString;
                var10000[2] = $$5;
                var10000[3] = this.minecraft.levelRenderer.getChunkStatistics();
                var10000[4] = this.minecraft.levelRenderer.getEntityStatistics();
                var10003 = this.minecraft.particleEngine.countParticles();
                var10000[5] = "P: " + var10003 + ". T: " + this.minecraft.level.getEntityCount();
                var10000[6] = this.minecraft.level.gatherChunkSourceStats();
                List<String> $$17 = Lists.newArrayList(var10000);
                String $$18 = this.getServerChunkStats();
                if ($$18 != null) {
                    $$17.add($$18);
                }

                ResourceLocation var10001 = new ResourceLocation("overworld");
                $$17.add("" + var10001 + " FC: " + ((LongSet) $$16).size());
                $$17.add("");
                $$17.add(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", this.minecraft.getCameraEntity().getX(), this.minecraft.getCameraEntity().getY(), this.minecraft.getCameraEntity().getZ()));
                $$17.add(String.format(Locale.ROOT, "Block: %d %d %d [%d %d %d]", $$6.getX(), $$6.getY(), $$6.getZ(), $$6.getX() & 15, $$6.getY() & 15, $$6.getZ() & 15));
                $$17.add(String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", $$14.x, SectionPos.blockToSectionCoord($$6.getY()), $$14.z, $$14.getRegionLocalX(), $$14.getRegionLocalZ(), $$14.getRegionX(), $$14.getRegionZ()));
                $$17.add(String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", $$8, $$13, Mth.wrapDegrees($$7.getYRot()), Mth.wrapDegrees($$7.getXRot())));
                LevelChunk $$19 = this.getClientChunk();
                if ($$19.isEmpty()) {
                    $$17.add("Waiting for chunk...");
                } else {
                    int $$20 = this.minecraft.level.getChunkSource().getLightEngine().getRawBrightness($$6, 0);
                    int $$21 = this.minecraft.level.getBrightness(LightLayer.SKY, $$6);
                    int $$22 = this.minecraft.level.getBrightness(LightLayer.BLOCK, $$6);
                    $$17.add("Client Light: " + $$20 + " (" + $$21 + " sky, " + $$22 + " block)");
                    LevelChunk $$23 = this.getServerChunk();
                    StringBuilder $$24 = new StringBuilder("CH");
                    Heightmap.Types[] var21 = Heightmap.Types.values();
                    int var22 = var21.length;

                    int var23;
                    Heightmap.Types $$26;
                    for (var23 = 0; var23 < var22; ++var23) {
                        $$26 = var21[var23];
                        if ($$26.sendToClient()) {
                            $$24.append(" ").append((String) HEIGHTMAP_NAMES.get($$26)).append(": ").append($$19.getHeight($$26, $$6.getX(), $$6.getZ()));
                        }
                    }

                    $$17.add($$24.toString());
                    $$24.setLength(0);
                    $$24.append("SH");
                    var21 = Heightmap.Types.values();
                    var22 = var21.length;

                    for (var23 = 0; var23 < var22; ++var23) {
                        $$26 = var21[var23];
                        if ($$26.keepAfterWorldgen()) {
                            $$24.append(" ").append((String) HEIGHTMAP_NAMES.get($$26)).append(": ");
                            if ($$23 != null) {
                                $$24.append($$23.getHeight($$26, $$6.getX(), $$6.getZ()));
                            } else {
                                $$24.append("??");
                            }
                        }
                    }

                    $$17.add($$24.toString());
                    if ($$6.getY() >= this.minecraft.level.getMinBuildHeight() && $$6.getY() < this.minecraft.level.getMaxBuildHeight()) {
                        Holder var27 = this.minecraft.level.getBiome($$6);
                        $$17.add("Biome: " + printBiome(var27));
                        long $$27 = 0L;
                        float $$28 = 0.0F;
                        if ($$23 != null) {
                            $$28 = $$15.getMoonBrightness();
                            $$27 = $$23.getInhabitedTime();
                        }

                        DifficultyInstance $$29 = new DifficultyInstance($$15.getDifficulty(), $$15.getDayTime(), $$27, $$28);
                        $$17.add(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f (Day %d)", $$29.getEffectiveDifficulty(), $$29.getSpecialMultiplier(), this.minecraft.level.getDayTime() / 24000L));
                    }

                    if ($$23 != null && $$23.isOldNoiseGeneration()) {
                        $$17.add("Blending: Old");
                    }
                }

                ServerLevel $$30 = this.getServerLevel();
                if ($$30 != null) {
                    ServerChunkCache $$31 = $$30.getChunkSource();
                    ChunkGenerator $$32 = $$31.getGenerator();
                    RandomState $$33 = $$31.randomState();
                    $$32.addDebugScreenInfo($$17, $$33, $$6);
                    Climate.Sampler $$34 = $$33.sampler();
                    BiomeSource $$35 = $$32.getBiomeSource();
                    $$35.addDebugInfo($$17, $$6, $$34);
                    NaturalSpawner.SpawnState $$36 = $$31.getLastSpawnState();
                    if ($$36 != null) {
                        Object2IntMap<MobCategory> $$37 = $$36.getMobCategoryCounts();
                        int $$38 = $$36.getSpawnableChunkCount();
                        $$17.add("SC: " + $$38 + ", " + (String) Stream.of(MobCategory.values()).map(($$1x) -> {
                            char var10008 = Character.toUpperCase($$1x.getName().charAt(0));
                            return "" + var10008 + ": " + $$37.getInt($$1x);
                        }).collect(Collectors.joining(", ")));
                    } else {
                        $$17.add("SC: N/A");
                    }
                }

                PostChain $$39 = this.minecraft.gameRenderer.currentEffect();
                if ($$39 != null) {
                    $$17.add("Shader: " + $$39.getName());
                }

                String var29 = this.minecraft.getSoundManager().getDebugString();
                $$17.add(var29 + String.format(Locale.ROOT, " (Mood %d%%)", Math.round(this.minecraft.player.getCurrentMood() * 100.0F)));

                if (RendererAccess.INSTANCE.hasRenderer()) {
                    $$17.add("[Fabric] Active renderer: " + RendererAccess.INSTANCE.getRenderer().getClass().getSimpleName());
                } else {
                    $$17.add("[Fabric] Active renderer: none (vanilla)");
                }


                cir.setReturnValue($$17);
            }
        }
    }
}
