package net.hydra.jojomod.networking;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.networking.component.StandComponent;
import net.hydra.jojomod.networking.component.StandData;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.networking.component.StandUserData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class MyComponents implements EntityComponentInitializer {

    public static final ComponentKey<StandUserComponent> STAND_USER =
            ComponentRegistry.getOrCreate(new Identifier(RoundaboutMod.MOD_ID, "stand_user"), StandUserComponent.class);
    public static final ComponentKey<StandComponent> STAND =
            ComponentRegistry.getOrCreate(new Identifier(RoundaboutMod.MOD_ID, "stand"), StandComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(StandEntity.class, STAND, StandData::new);
        registry.registerFor(LivingEntity.class, STAND_USER, StandUserData::new);
    }
}
