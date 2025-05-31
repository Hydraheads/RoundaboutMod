package net.hydra.jojomod.advancement.criteria;

import com.google.gson.JsonObject;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class DimensionHopCriterion extends SimpleCriterionTrigger<DimensionHopCriterion.Instance> {
    private static final ResourceLocation ID = Roundabout.location("dimension_hop");

    @Override
    protected Instance createInstance(JsonObject jsonObject, ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
        return new Instance(contextAwarePredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer sp)
    {
        if (DynamicWorld.isWorldDynamic(sp.level()) && sp.level().dimension().location().getPath().startsWith("d4c-"))
        {
            this.trigger(sp, instance -> true);
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate predicate) {
            super(ID, predicate);
        }

        public static Instance any() {
            return new Instance(ContextAwarePredicate.ANY);
        }
    }
}
