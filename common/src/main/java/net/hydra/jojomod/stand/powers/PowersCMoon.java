package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.world.entity.LivingEntity;
import net.hydra.jojomod.access.IGravityEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;



public class PowersCMoon extends NewPunchingStand {
    @Override public boolean isWip(){return true;}
    @Override public Component ifWipListDevStatus(){ return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.GREEN);}
    @Override public Component ifWipListDev(){ return Component.literal("Scary Chat").withStyle(ChatFormatting.GREEN);}
    public PowersCMoon(LivingEntity self) {super(self); }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersCMoon(entity); }

}
