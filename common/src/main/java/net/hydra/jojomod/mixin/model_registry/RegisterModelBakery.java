package net.hydra.jojomod.mixin.model_registry;

import net.hydra.jojomod.client.ModItemModels;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class RegisterModelBakery {

    /**This is the means of registering item models to the game besides the default ones that apply normally, for instance
     * making a book have a new texture if it has a certain name
     * */
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void roundabout$addITEMNAME(BlockColors $$0, ProfilerFiller $$1, Map $$2, Map $$3, CallbackInfo ci) {
        this.loadTopLevel(ModItemModels.HARPOON_IN_HAND);
        this.loadTopLevel(ModItemModels.SNUBNOSE_REVOLVER_IN_HAND);
        this.loadTopLevel(ModItemModels.TOMMY_GUN_IN_HAND);
        this.loadTopLevel(ModItemModels.ANUBIS_IN_HAND);
        this.loadTopLevel(ModItemModels.STAND_BOW);
        this.loadTopLevel(ModItemModels.STAND_BEETLE_BOW);
        this.loadTopLevel(ModItemModels.WORTHY_BOW);
        this.loadTopLevel(ModItemModels.STAND_WORTHY_CROSSBOW);
        this.loadTopLevel(ModItemModels.STAND_ARROW_CROSSBOW);
        this.loadTopLevel(ModItemModels.STAND_BEETLE_CROSSBOW);
        this.loadTopLevel(ModItemModels.DREAD_BOOK);
        this.loadTopLevel(ModItemModels.FOG_BLOCK_ICON);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DIO_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DIO_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DIO_HELD);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DIO_HELD_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DIO_HELD_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_RIGHT_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_RIGHT_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_RIGHT_HELD);
        this.loadTopLevel(ModItemModels.STREET_SIGN_RIGHT_HELD_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_RIGHT_HELD_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_STOP_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_STOP_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_STOP_HELD);
        this.loadTopLevel(ModItemModels.STREET_SIGN_STOP_HELD_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_STOP_HELD_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_YIELD_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_YIELD_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_YIELD_HELD);
        this.loadTopLevel(ModItemModels.STREET_SIGN_YIELD_HELD_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_YIELD_HELD_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DANGER_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DANGER_D2);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DANGER_HELD);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DANGER_HELD_D);
        this.loadTopLevel(ModItemModels.STREET_SIGN_DANGER_HELD_D2);
        this.topLevelModels.get(ModItemModels.HARPOON_IN_HAND).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.ANUBIS_IN_HAND).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STAND_BOW).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STAND_BEETLE_BOW).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.WORTHY_BOW).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STAND_WORTHY_CROSSBOW).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STAND_ARROW_CROSSBOW).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STAND_BEETLE_CROSSBOW).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.DREAD_BOOK).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.FOG_BLOCK_ICON).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DIO_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DIO_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DIO_HELD).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DIO_HELD_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DIO_HELD_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_RIGHT_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_RIGHT_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_RIGHT_HELD).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_RIGHT_HELD_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_RIGHT_HELD_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_STOP_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_STOP_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_STOP_HELD).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_STOP_HELD_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_STOP_HELD_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_YIELD_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_YIELD_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_YIELD_HELD).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_YIELD_HELD_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_YIELD_HELD_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DANGER_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DANGER_D2).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DANGER_HELD).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DANGER_HELD_D).resolveParents(this::getModel);
        this.topLevelModels.get(ModItemModels.STREET_SIGN_DANGER_HELD_D2).resolveParents(this::getModel);
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    protected abstract void loadTopLevel(ModelResourceLocation modelId);

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> topLevelModels;

    @Shadow
    public UnbakedModel getModel(ResourceLocation $$0) {
        return null;
    }


    /**
    @ModifyVariable(method="getModel", at=@At("HEAD"),ordinal = 0)
    private ResourceLocation roundabout$fogModelResourceKey(ResourceLocation key)
    {
        if (key.getPath().startsWith("fog_") && key.getNamespace().equals(Roundabout.MOD_ID))
            return Roundabout.location("block/fog_block");
        return key;
    }
    **/
}
