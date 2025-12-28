package net.hydra.jojomod.mixin.powers;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.powers.GeneralPowers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PowersPlayer extends LivingEntity implements IPowersPlayer {


    @Unique
    public GeneralPowers rdbt$powers = null;
    @Unique
    public byte rdbt$lastPower = PowerTypes.NONE.id;
    @Unique
    @Override
    public GeneralPowers rdbt$getPowers(){
        if (rdbt$lastPower != ((IPlayerEntity)this).roundabout$getFate()){
            rdbt$lastPower = ((IPlayerEntity)this).roundabout$getFate();
            rdbt$powers = null;
        }
        if (rdbt$powers == null){
            rdbt$powers = PowerTypes.getPowerFromByte(((IPlayerEntity)this).roundabout$getPower()).generalPowers.generatePowers(this);
        }
        return rdbt$powers;
    }

    protected PowersPlayer(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
}
