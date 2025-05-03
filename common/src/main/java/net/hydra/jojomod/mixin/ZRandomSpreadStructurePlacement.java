package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.util.Config;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RandomSpreadStructurePlacement.class)
public abstract class ZRandomSpreadStructurePlacement extends StructurePlacement {
    /**Literally intrecept structures using their salt to change their rarity*/
    @Shadow @Final private RandomSpreadType spreadType;

    protected ZRandomSpreadStructurePlacement(Vec3i $$0, FrequencyReductionMethod $$1, float $$2, int $$3, Optional<ExclusionZone> $$4) {
        super($$0, $$1, $$2, $$3, $$4);
    }

    @Inject(method = "spacing", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$spacing(CallbackInfoReturnable<Integer> cir) {
        //Cinderella spacing interception
        if (this.salt() == 99271345){
            Config conf = ClientNetworking.getAppropriateConfig();
            if (conf != null && conf.worldgenSettings != null){
                if (conf.worldgenSettings.cinderellaSpacing > conf.worldgenSettings.cinderellaSeparationMakeSmallerThanSpacing){
                    cir.setReturnValue(conf.worldgenSettings.cinderellaSpacing);
                }
            }
            return;
        }
        //Meteorite spacing interception
        if (this.salt() == 99271346){

            return;
        }
    }

    @Inject(method = "separation", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$separation(CallbackInfoReturnable<Integer> cir) {
        //Cinderella spacing interception
        if (this.salt() == 99271345){
            Config conf = ClientNetworking.getAppropriateConfig();
            if (conf != null && conf.worldgenSettings != null){
                if (conf.worldgenSettings.cinderellaSpacing > conf.worldgenSettings.cinderellaSeparationMakeSmallerThanSpacing){
                    cir.setReturnValue(conf.worldgenSettings.cinderellaSeparationMakeSmallerThanSpacing);
                }
            }
            return;
        }
        //Meteorite spacing interception
        if (this.salt() == 99271346){

            return;
        }
    }

    @Inject(method = "getPotentialStructureChunk", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getPotentialStructureChunk(long loong, int i1, int i2,CallbackInfoReturnable<ChunkPos> cir) {
        //Cinderella spacing interception
        if (this.salt() == 99271345){
            Config conf = ClientNetworking.getAppropriateConfig();
            if (conf != null && conf.worldgenSettings != null){
                if (conf.worldgenSettings.cinderellaSpacing > conf.worldgenSettings.cinderellaSeparationMakeSmallerThanSpacing){
                    cir.setReturnValue(roundabout$getPotentialStructureChunk2(loong, i1, i2, conf.worldgenSettings.cinderellaSpacing, conf.worldgenSettings.cinderellaSeparationMakeSmallerThanSpacing));
                    //Roundabout.LOGGER.info("yes this works");
                }
            }
            return;
        }
        //Meteorite spacing interception
        if (this.salt() == 99271346){

            return;
        }
    }


    @Unique
    public ChunkPos roundabout$getPotentialStructureChunk2(long loong, int i1, int i2, int space, int separa) {
        int i = Math.floorDiv(i1, space);
        int j = Math.floorDiv(i2, space);
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureWithSalt(loong, i, j, this.salt());
        int k = space - separa;
        int l = this.spreadType.evaluate(worldgenrandom, k);
        int ix = this.spreadType.evaluate(worldgenrandom, k);
        return new ChunkPos(i * space + l, j * space + ix);
    }
}
