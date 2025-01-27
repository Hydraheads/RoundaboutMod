package net.hydra.jojomod.entity.corpses;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelPart;

public class FallenSpiderModel <T extends FallenMob> extends SpiderModel<T> {
    protected FallenSpiderModel(ModelPart $$0) {
        super($$0);
    }

    public void setupAnim(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        if (!$$0.isActivated){
            super.setupAnim($$0,0,0,0,0,0);
        } else {
            super.setupAnim($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

    public boolean isAggressive(T $$0) {
        return $$0.isAggressive();
    }
}
