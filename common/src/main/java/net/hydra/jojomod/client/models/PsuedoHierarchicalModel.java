package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.function.Function;

public abstract class PsuedoHierarchicalModel extends Model {

    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    protected PsuedoHierarchicalModel() {
        this(RenderType::entityCutoutNoCull);
    }

    protected PsuedoHierarchicalModel(Function<ResourceLocation, RenderType> $$0) {
        super($$0);
    }

    @Override
    public void renderToBuffer(PoseStack $$0, VertexConsumer $$1, int $$2, int $$3, float $$4, float $$5, float $$6, float $$7) {
        this.root().render($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    public abstract ModelPart root();

    public Optional<ModelPart> getAnyDescendantWithName(String $$0) {
        return $$0.equals("root")
                ? Optional.of(this.root())
                : this.root().getAllParts().filter($$1 -> $$1.hasChild($$0)).findFirst().map($$1 -> $$1.getChild($$0));
    }

    protected void animate(AnimationState $$0, AnimationDefinition $$1, float $$2) {
        this.animate($$0, $$1, $$2, 1.0F);
    }

    public abstract void setupAnim(Entity var1, float ageInTicks);
    protected void animateWalk(AnimationDefinition $$0, float $$1, float $$2, float $$3, float $$4) {
        long $$5 = (long)($$1 * 50.0F * $$3);
        float $$6 = Math.min($$2 * $$4, 1.0F);
        PsuedoKeyframeAnimations.animate(this, $$0, $$5, $$6, ANIMATION_VECTOR_CACHE);
    }

    protected void animate(AnimationState $$0, AnimationDefinition $$1, float $$2, float $$3) {
        $$0.updateTime($$2, $$3);
        $$0.ifStarted($$1x -> PsuedoKeyframeAnimations.animate(this, $$1, $$1x.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE));
    }

    protected void applyStatic(AnimationDefinition $$0) {
        PsuedoKeyframeAnimations.animate(this, $$0, 0L, 1.0F, ANIMATION_VECTOR_CACHE);
    }
}
