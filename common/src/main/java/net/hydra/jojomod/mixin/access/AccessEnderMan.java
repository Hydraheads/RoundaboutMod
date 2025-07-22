package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IEnderMan;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EnderMan.class)
public abstract class AccessEnderMan extends Monster implements NeutralMob, IEnderMan {
    /**A simple mixin to make endermen teleportation publicly accessible.*/
    @Unique
    public void roundabout$teleport(){
        this.teleport();
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow protected abstract boolean teleport();

    protected AccessEnderMan(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

}
