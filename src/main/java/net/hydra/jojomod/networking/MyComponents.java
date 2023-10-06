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

/** This is the main file which registers components for the Cardinal Components library.
 * Cardinal components is essentially the Capabilities system that is built into
 * Forge, but for fabric.
 * We are using Cardinal components because syncing data with custom packets is fairly difficult and
 * at times clunky, especially when the data that needs to be synced cannot be put in a data tracker.
 * This will only exist for the Fabric release of the mod, and the Forge version will use
 * Forge's native Capabilities system instead.
 * */

public class MyComponents implements EntityComponentInitializer {

    public static final ComponentKey<StandUserComponent> STAND_USER =
            ComponentRegistry.getOrCreate(new Identifier(RoundaboutMod.MOD_ID, "stand_user"), StandUserComponent.class);
    public static final ComponentKey<StandComponent> STAND =
            ComponentRegistry.getOrCreate(new Identifier(RoundaboutMod.MOD_ID, "stand"), StandComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(StandEntity.class, STAND).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).impl(StandData.class).end(StandData::new);
        registry.beginRegistration(LivingEntity.class, STAND_USER).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).impl(StandUserData.class).end(StandUserData::new);
        registry.registerForPlayers(STAND_USER, StandUserData::new, RespawnCopyStrategy.ALWAYS_COPY);
        //registry.registerFor(LivingEntity.class, STAND_USER, StandUserData::new);
    }
}
