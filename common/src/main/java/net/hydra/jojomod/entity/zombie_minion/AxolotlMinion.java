package net.hydra.jojomod.entity.zombie_minion;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LerpingModel;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.Map;

public class AxolotlMinion extends BaseMinion  implements LerpingModel {
    public AxolotlMinion(EntityType<? extends AxolotlMinion> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new FallenZombieAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
    }

    private final Map<String, Vector3f> modelRotationValues = Maps.newHashMap();
    @Override
    public Map<String, Vector3f> getModelRotationValues() {
        return this.modelRotationValues;
    }
}
