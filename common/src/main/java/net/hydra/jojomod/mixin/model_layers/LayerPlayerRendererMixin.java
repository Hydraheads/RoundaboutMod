package net.hydra.jojomod.mixin.model_layers;

import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.models.layers.*;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class LayerPlayerRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    /**All player model related layers, even if it is registered in humanoid it also needs to be registered here,
     * because of the way vanilla has it set up, this is not a humanoid renderer so to speak*/
    @Inject(method="<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At(value = "RETURN"))
    private void roundaboutRenderKnives(EntityRendererProvider.Context $$0, boolean $$1, CallbackInfo ci) {
        this.addLayer(new KnifeLayer<>($$0, this));
        //this.addLayer(new LocacacaBeamLayer<>($$0, this));
        this.addLayer(new StoneLayer<>($$0, this));
        this.addLayer(new FacelessLayer<>($$0, this));
        this.addLayer(new ShootingArmLayer<>($$0, this));
        this.addLayer(new HeyYaLayer<>($$0, this));
        this.addLayer(new MandomLayer<>($$0, this));
        this.addLayer(new RattShoulderLayer<>($$0, this));
        this.addLayer(new VisagePartLayer<>($$0, this));
        this.addLayer(new BowlerHatLayer<>($$0, this));
        this.addLayer(new FirearmLayer<>($$0, this));
        this.addLayer(new RoadRollerLayer<>($$0, this));
        this.addLayer(new WornStoneMaskLayer<>($$0, this));
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    public LayerPlayerRendererMixin(EntityRendererProvider.Context $$0, PlayerModel<AbstractClientPlayer> $$1, float $$2) {
        super($$0, $$1, $$2);
    }
}
