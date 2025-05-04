package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = StructureSet.StructureSelectionEntry.class)
public abstract class ZStructureWeight {

    /**
    @Unique
    private static final ResourceLocation roundabout$rl1 = new ResourceLocation(Roundabout.MOD_ID,"meteorite_site");
    @Shadow public abstract Holder<Structure> structure();

    @Inject(method = "weight", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$weight(CallbackInfoReturnable<Integer> cir) {
        Roundabout.LOGGER.info(""+this.structure());
        if (this.structure().is(roundabout$rl1)){
            Roundabout.LOGGER.info("fullpower");

        }
    }**/
}
