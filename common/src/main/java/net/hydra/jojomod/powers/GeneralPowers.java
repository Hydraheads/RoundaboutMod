package net.hydra.jojomod.powers;

import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class GeneralPowers extends AbilityScapeBasis {
    public GeneralPowers(LivingEntity self) {
        super(self);
    }
    public GeneralPowers() {
        super(null);
    }


    @Override
    public void syncActivePower(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer SP){
            S2CPacketUtil.sendActivePowerPowersPacket(SP,activePower);
        }
    }
}
