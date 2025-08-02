package net.hydra.jojomod.mixin.data_and_structures;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.util.config.Config;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StructureTemplatePool.class)
public class DataStructureTemplatePool {
    /**Reconstructs the structure data pool with custom structure jsons so players can modify their
     * rarities in the config*/

    @Inject(method = "<init>(Lnet/minecraft/core/Holder;Ljava/util/List;)V", at = @At(value = "RETURN"))
    protected void roundabout$init(Holder $$0, List $$1, CallbackInfo ci) {
        Config conf = ClientNetworking.getAppropriateConfig();
        if (conf != null && conf.worldgenSettings != null && conf.worldgenSettings.modifyStructureWeights) {

            this.templates.clear();
            for (Object o : $$1) {
                Pair<StructurePoolElement, Integer> pair = (Pair) o;
                StructurePoolElement structurePoolElement = (StructurePoolElement) pair.getFirst();

                int second = pair.getSecond();
                String str = structurePoolElement.toString();
                if (str != null && str.contains("roundabout:meteorite_site")) {
                    second = conf.worldgenSettings.meteoriteWeight;
                } else if (str != null && str.contains("roundabout:cinderella")) {
                    second = conf.worldgenSettings.cinderellaWeight;
                }

                if (second > 0) {
                    for (int i = 0; i < (Integer) second; ++i) {
                        this.templates.add(structurePoolElement);
                    }
                }
            }
        }

    }
    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow @Final private ObjectArrayList<StructurePoolElement> templates;
}
