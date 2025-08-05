package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.ICreeper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Creeper.class)
public abstract class AccessCreeper extends Monster implements ICreeper {

    /**There is no reason for these to be private or protected, we should be able to tap into them.*/

    @Override
    public int roundabout$getSwell(){
        return swell;
    }
    @Override

    public void roundabout$setSwell(int swell){
        this.swell = swell;
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    private int swell;

    protected AccessCreeper(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

}
