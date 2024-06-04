package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

public class PowersTheWorld extends StandPowers {

    public PowersTheWorld(LivingEntity self) {
        super(self);
    }

    @Override
    public void buttonInputSpecial() {
        if (this.getSelf().level().isClientSide()) {
            if (KeyInputs.roundaboutClickCount == 0) {
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SPECIAL);
                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL, true);
            }
            KeyInputs.roundaboutClickCount = 2;
        }
    }

    /***/
    @Override
    public void setPowerSpecial(int lastMove) {
        if (!this.getSelf().level().isClientSide()) {
            if (!this.isStoppingTime()) {
                /*Time Stop*/
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(),
                        ModSounds.TIME_STOP_THE_WORLD_EVENT, SoundSource.PLAYERS, 0.95F,
                        1F);
                this.timeStoppingTicks = 180;
                ((TimeStop) this.getSelf().level()).addTimeStoppingEntity(this.getSelf());
            } else {
                /*Time Resume*/
                ((TimeStop) this.getSelf().level()).removeTimeStoppingEntity(this.getSelf());
                this.timeStoppingTicks = 0;
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(),
                        ModSounds.TIME_RESUME_EVENT, SoundSource.PLAYERS, 0.95F,
                        1F);
            }
        }
    }

    /**20 ticks in a second*/
    private int timeStoppingTicks = 0;
    @Override
    public boolean isStoppingTime(){
        return timeStoppingTicks > 0;
    }
}
