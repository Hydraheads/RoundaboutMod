package net.hydra.jojomod.advancement.criteria;

import net.hydra.jojomod.Roundabout;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

import java.lang.reflect.Method;

public class ModCriteria {
    public static DimensionHopCriterion DIMENSION_HOP_TRIGGER;

    @SuppressWarnings("unchecked")
    public static <T extends CriterionTrigger<?>> T register(T trigger)
    {
        try
        {
            Method m = CriteriaTriggers.class.getDeclaredMethod("register", CriterionTrigger.class);
            m.setAccessible(true);
            return (T)m.invoke(CriteriaTriggers.class, trigger);
        }
        catch (Exception e)
        {
            Roundabout.LOGGER.error("Exception caught while registering advancement criteria!");
            return null;
        }
    }

    public static void bootstrap()
    {
        DIMENSION_HOP_TRIGGER = register(new DimensionHopCriterion());
    }
}