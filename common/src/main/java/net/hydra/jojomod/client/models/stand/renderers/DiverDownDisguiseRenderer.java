package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;

public final class DiverDownDisguiseRenderer extends AbstractDisguiseRenderer {

    public static DiverDownDisguiseRenderer INSTANCE;

    public DiverDownDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected boolean shouldShowArmor(LivingEntity entity) {
        if (entity instanceof StandUser su) {
            StandPowers sp = su.roundabout$getStandPowers();
            if (sp instanceof PowersDiverDown dd) {
                return dd.shouldShowDisguiseArmor();
            }
            if (su.roundabout$getDiverUser() != null) {
                return su.roundabout$getDiverUser().shouldShowDisguiseArmor();
            }
        }
        if (Minecraft.getInstance().player instanceof StandUser su) {
            StandPowers sp = su.roundabout$getStandPowers();
            if (sp instanceof PowersDiverDown dd) {
                return dd.shouldShowDisguiseArmor();
            }
        }
        return true;
    }
}