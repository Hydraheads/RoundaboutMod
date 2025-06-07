package net.hydra.jojomod.client.models.corpses;

import net.hydra.jojomod.entity.corpses.FallenMob;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;

public class FallenVillagerModel <T extends FallenMob> extends VillagerModel<T> {
    public FallenVillagerModel(ModelPart $$0) {
        super($$0);
    }

    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        if (!$$0.getActivated()){
            this.root().resetPose();
            this.root().getChild("head").resetPose();
            this.root().getChild("head").getChild("nose").resetPose();
            this.root().getChild("right_leg").resetPose();
            this.root().getChild("left_leg").resetPose();
        } else {
            super.setupAnim($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

    public boolean isAggressive(T $$0) {
        return $$0.isAggressive();
    }
}

