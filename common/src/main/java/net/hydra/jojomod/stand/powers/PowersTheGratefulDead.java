package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;

import java.util.Arrays;

public class PowersTheGratefulDead extends NewPunchingStand {
    public PowersTheGratefulDead(LivingEntity self){
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersTheGratefulDead(entity);
    }
    @Override
    public boolean canSummonStandAsEntity(){
        return true;
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.THE_GRATEFUL_DEAD.create(this.getSelf().level());
    }


    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal("K4traik").withStyle(ChatFormatting.DARK_PURPLE);
    }

    public static final byte
            ANIME_THE_GRATEFUL_DEAD = 1;

    @Override
    public List<Byte> getSkinList(){
        return Arrays.asList(
                ANIME_THE_GRATEFUL_DEAD
        );
    }
}
