package net.hydra.jojomod.client.models;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PsuedoKeyframeAnimations {
    public static void animate(PsuedoHierarchicalModel $$0, AnimationDefinition $$1, long $$2, float $$3, Vector3f $$4) {
        float $$5 = getElapsedSeconds($$1, $$2);

        for (Map.Entry<String, List<AnimationChannel>> $$6 : $$1.boneAnimations().entrySet()) {
            Optional<ModelPart> $$7 = $$0.getAnyDescendantWithName($$6.getKey());
            List<AnimationChannel> $$8 = $$6.getValue();
            $$7.ifPresent($$4x -> $$8.forEach($$4xx -> {
                Keyframe[] $$5x = $$4xx.keyframes();
                int $$6x = Math.max(0, Mth.binarySearch(0, $$5x.length, $$2x -> $$5 <= $$5x[$$2x].timestamp()) - 1);
                int $$7x = Math.min($$5x.length - 1, $$6x + 1);
                Keyframe $$8x = $$5x[$$6x];
                Keyframe $$9 = $$5x[$$7x];
                float $$10 = $$5 - $$8x.timestamp();
                float $$11;
                if ($$7x != $$6x) {
                    $$11 = Mth.clamp($$10 / ($$9.timestamp() - $$8x.timestamp()), 0.0F, 1.0F);
                } else {
                    $$11 = 0.0F;
                }

                $$9.interpolation().apply($$4, $$11, $$5x, $$6x, $$7x, $$3);
                $$4xx.target().apply($$4x, $$4);
            }));
        }
    }

    private static float getElapsedSeconds(AnimationDefinition $$0, long $$1) {
        float $$2 = (float)$$1 / 1000.0F;
        return $$0.looping() ? $$2 % $$0.lengthInSeconds() : $$2;
    }

    public static Vector3f posVec(float $$0, float $$1, float $$2) {
        return new Vector3f($$0, -$$1, $$2);
    }

    public static Vector3f degreeVec(float $$0, float $$1, float $$2) {
        return new Vector3f($$0 * (float) (Math.PI / 180.0), $$1 * (float) (Math.PI / 180.0), $$2 * (float) (Math.PI / 180.0));
    }

    public static Vector3f scaleVec(double $$0, double $$1, double $$2) {
        return new Vector3f((float)($$0 - 1.0), (float)($$1 - 1.0), (float)($$2 - 1.0));
    }
}