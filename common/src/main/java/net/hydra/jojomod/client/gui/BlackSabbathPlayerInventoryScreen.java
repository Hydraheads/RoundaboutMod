package net.hydra.jojomod.client.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClientPlayer;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


public class BlackSabbathPlayerInventoryScreen
        extends EffectRenderingInventoryScreen<BlackSabbathPlayerInventoryMenu> {
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation("textures/gui/container/horse.png");
    private final Player player;
    private float xMouse;
    private float yMouse;

    public BlackSabbathPlayerInventoryScreen(BlackSabbathPlayerInventoryMenu $$0, Inventory $$1, Player $$2) {
        super($$0, $$1, $$2.getDisplayName());
        this.player = $$2;
    }

    @Override
    protected void renderBg(GuiGraphics $$0, float $$1, int $$2, int $$3) {
        LivingEntity se = null;
        int $$4 = (this.width - this.imageWidth) / 2;
        int $$5 = (this.height - this.imageHeight) / 2;
        if(((StandUser)player).roundabout$getStandPowers() instanceof PowersBlackSabbath PS){
            se = ModEntities.BLACK_SABBATH.create(player.level());
        }
        $$0.blit(HORSE_INVENTORY_LOCATION, $$4, $$5, 0, 0, this.imageWidth, this.imageHeight);
            $$0.blit(HORSE_INVENTORY_LOCATION, $$4 + 79, $$5 + 17, 0, this.imageHeight, 9 * 18, 54);

            $$0.blit(HORSE_INVENTORY_LOCATION, $$4 + 7, $$5 + 35 - 18, 18, this.imageHeight + 54, 18, 18);


        InventoryScreen.renderEntityInInventoryFollowsMouse(
                $$0, $$4 + 51, $$5 + 60, 17, (float)($$4 + 51) - this.xMouse, (float)($$5 + 75 - 50) - this.yMouse, se
        );
    }

    @Override
    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        this.renderBackground($$0);
        this.xMouse = (float)$$1;
        this.yMouse = (float)$$2;
        super.render($$0, $$1, $$2, $$3);
        this.renderTooltip($$0, $$1, $$2);
    }
}
