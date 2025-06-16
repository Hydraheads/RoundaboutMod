package net.hydra.jojomod.mixin;

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
public class ZStructureTemplatePool {

    @Shadow @Final private ObjectArrayList<StructurePoolElement> templates;

    @Shadow @Final private Holder<StructureTemplatePool> fallback;

    @Shadow @Final private List<Pair<StructurePoolElement, Integer>> rawTemplates;

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

/**
    @Inject(method = "<init>(Lnet/minecraft/core/Holder;Ljava/util/List;Lnet/minecraft/world/level/levelgen/structure/pools/StructureTemplatePool$Projection;)V", at = @At(value = "RETURN"))
    protected void roundabout$init2(Holder $$0, List $$1, StructureTemplatePool.Projection $$2, CallbackInfo ci) {
        ObjectArrayList<StructurePoolElement> templates2 = new ObjectArrayList<>();
        Roundabout.LOGGER.info("X2:");

        this.templates.clear();
        this.rawTemplates.clear();

        for (Object object : $$1) {
            Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer> $$3 = (Pair) object;
            StructurePoolElement $$4 = (StructurePoolElement) ((Function) $$3.getFirst()).apply($$2);
            int second = $$3.getSecond();
            Roundabout.LOGGER.info("nm: "+$$4.getProjection().getName());
            Roundabout.LOGGER.info("ns: "+$$4.getProjection().getSerializedName());

            this.rawTemplates.add(Pair.of($$4, (Integer) $$3.getSecond()));

            for (int $$5 = 0; $$5 < (Integer) $$3.getSecond(); ++$$5) {
                this.templates.add($$4);
            }
        }

    }
    **/
}
