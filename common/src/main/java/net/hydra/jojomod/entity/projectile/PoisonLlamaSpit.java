package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class PoisonLlamaSpit extends LlamaSpit {
    public PoisonLlamaSpit(EntityType<? extends LlamaSpit> $$0, Level $$1) {
        super($$0, $$1);
    }

    public PoisonLlamaSpit(Level $$0, LivingEntity $$1) {
        this(ModEntities.POISON_LLAMA_SPIT, $$0);
        this.setOwner($$1);
        this.setPos($$1.getX() - (double)($$1.getBbWidth() + 1.0F) * (double)0.5F * (double) Mth.sin($$1.yBodyRot * ((float)Math.PI / 180F)), $$1.getEyeY() - (double)0.1F, $$1.getZ() + (double)($$1.getBbWidth() + 1.0F) * (double)0.5F * (double)Mth.cos($$1.yBodyRot * ((float)Math.PI / 180F)));
    }

    protected void onHitEntity(EntityHitResult $$0) {

    }

    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
    }
}