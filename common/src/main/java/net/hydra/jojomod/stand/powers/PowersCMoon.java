package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.minecraft.world.entity.LivingEntity;
import net.hydra.jojomod.access.IGravityEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;


public class PowersCMoon extends NewPunchingStand {
    @Override public boolean isWip(){return true;}
    @Override public Component ifWipListDevStatus(){ return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.GREEN);}
    @Override public Component ifWipListDev(){ return Component.literal("Scary Chat").withStyle(ChatFormatting.GREEN);}
    public PowersCMoon(LivingEntity self) {super(self); }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersCMoon(entity); }

    // Scary, these are the all the methods you need to make your stand show up in game (i hope)
    // good luck figuring them out -88superguy
    @Override
    public Component getSkinName(byte skinId) {
        switch (skinId) {
            // look at other Powers for examples
        }
    }

    @Override
    public StandEntity getNewStandEntity() {
        // look at other Powers for examples
    }

}
