package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IGossipContainerAccess;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.UUID;

@Mixin(GossipContainer.class)
public class ZGossipContainer implements IGossipContainerAccess {

    @Shadow
    @Final
    private Map<UUID, ?> gossips;

    public void rdbt$clearGossips(){
        this.gossips.clear();
    }

}
