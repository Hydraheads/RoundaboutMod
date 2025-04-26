package net.hydra.jojomod.entity.visages;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class JojoNPCPlayer extends JojoNPC {
    public boolean isSimple(){
        return true;
    }

    public Player faker;
    public JojoNPCPlayer(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }
}
