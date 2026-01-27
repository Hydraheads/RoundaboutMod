package net.hydra.jojomod.item;


import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AnubisItem extends Item {


    public AnubisItem(Properties $$0) {
        super($$0);
    }




    @Override
    public ItemStack finishUsingItem(ItemStack $$0, Level $$1, LivingEntity $$2) {
        if ($$2 instanceof Player P) {
            StandUser SU = (StandUser)P;
            if (!$$1.isClientSide) {
                P.getCooldowns().addCooldown($$0.getItem(),10/*2400*/);
                P.level().playSound(null,P.blockPosition(), ModSounds.ANUBIS_POSSESSION_EVENT,SoundSource.PLAYERS,1.0F,1.3F);


                List<LivingEntity> targets = new ArrayList<>();
                for (LivingEntity target : $$2.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(),$$2,$$2.getBoundingBox().inflate(20))) {
                    if (!target.equals($$2) && target.isAlive() && target.attackable()) {
                        targets.add(target);
                    }
                }
                AnubisPossessorEntity p = new AnubisPossessorEntity($$2.level(), $$2, targets );
                p.setPos($$2.getPosition(1));
                $$1.addFreshEntity(p);
                SU.roundabout$setPossessor(p);

                if (SU.roundabout$getStandPowers() != null) {
                    if (!PowerTypes.hasStandActive($$2)) {
                        SU.roundabout$summonStand($$2.level(), false, true);
                    }
                    if (SU.roundabout$getStandPowers() instanceof PowersWalkingHeart PWH) {
                        PWH.extendHeels();
                    }
                }

                AnubisItem.aggroOnto($$2);
            }
        }
        return $$0;
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        $$2.add(Component.translatable("leveling.roundabout.disc_wip").withStyle(ChatFormatting.RED));
        $$2.add(Component.translatable("leveling.roundabout.disc_wip_2").withStyle(ChatFormatting.RED));
        $$2.add(Component.translatable("roundabout.dev_status.dev_status").withStyle(ChatFormatting.WHITE)
                .append(" ")
                .append(Component.translatable("roundabout.dev_status.active")).withStyle(ChatFormatting.YELLOW));
        $$2.add(Component.translatable("roundabout.dev_status.dev_name").withStyle(ChatFormatting.WHITE)
                .append(" ")
                .append("Prisma").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public void onUseTick(Level $$0, LivingEntity $$1, ItemStack $$2, int $$3) {
        if (!$$0.isClientSide()) {
            if ($$1.isUsingItem()) {
                if ($$3 % 8 == 0) {
                    ((ServerLevel) $$1.level()).sendParticles(ModParticles.MENACING,
                            $$1.getX(), $$1.getY() + 0.3, $$1.getZ(),
                            1, 0.2, 0.2, 0.2, 0.05);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level $$0, Player $$1, InteractionHand $$2) {

        ((IPlayerEntity)$$1).roundabout$getThirdPersonAnubisUnsheath().startIfStopped($$1.tickCount);

        ItemStack $$3 = $$1.getItemInHand($$2);
        $$1.startUsingItem($$2);
        return InteractionResultHolder.fail($$3);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack $$0) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack $$0) {
        return 50;
    }


    public static int aggroOnto(LivingEntity LE) {
        int radius = 13;
        AABB box = LE.getBoundingBox().inflate(radius,2,radius);
        List<Mob> entities = LE.level().getNearbyEntities(Mob.class, TargetingConditions.DEFAULT,LE,box);
        entities.removeIf(entity -> entity instanceof Villager);
        entities.removeIf(entity -> entity instanceof NeutralMob && entity.getTarget() == null);
        entities.removeIf(entity -> entity instanceof Piglin && entity.getTarget() == null);
        for (Mob M : entities) {
            if (M instanceof Wolf W && W.getOwner().equals(LE) ) {
                W.setTarget(null);
            }
        }
        entities.removeIf(entity ->  (entity instanceof TamableAnimal TA && TA.isTame()) );

        for (Mob M : entities) {
            M.setTarget(LE);
            M.setLastHurtByMob(LE);
        }

        Vec3 pos = LE.getPosition(1);

        ((ServerLevel) LE.level()).sendParticles(ModParticles.RAGING_LIGHT,
                pos.x,
                pos.y + LE.getEyeHeight(),
                pos.z,
                30, 0, 0, 0, 0.4);
        return entities.size();
    }
}
