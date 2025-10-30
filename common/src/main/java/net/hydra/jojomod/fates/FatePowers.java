package net.hydra.jojomod.fates;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class FatePowers extends AbilityScapeBasis {

    public FatePowers(LivingEntity self) {
        super(self);
    }
    public FatePowers() {
        super(null);
    }
    public void tick(){
    }


    /**This is imporant, on every fate class, override this and do something like
     *     return new VampireFate(entity); */
    public FatePowers generateFatePowers(LivingEntity entity){
        return new FatePowers(entity);
    }




    @Override

    public void xTryPower(byte index, boolean forced){
        tryPower(index, forced);
        tryPowerStuff();
    }

    @Override
    public void syncActivePower(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer SP){
            S2CPacketUtil.sendActivePowerFatePacket(SP,activePower);
        }
    }
}
