package net.hydra.jojomod.client.gui;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse;


public class BlackSabbathPlayerInventoryScreen
        extends EffectRenderingInventoryScreen<BlackSabbathPlayerInventoryMenu> {
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation(Roundabout.MOD_ID, "textures/gui/polpo_stand_screen.png");
    private final Player player;
    private float xMouse;
    private float yMouse;
    protected int leftPos;
    protected int topPos;

    public BlackSabbathPlayerInventoryScreen(BlackSabbathPlayerInventoryMenu $$0, Inventory $$1, Player $$2) {
        super($$0, $$1, Component.literal("Black Sabbath"));
        this.player = $$2;
    }

    @Override
    protected void renderBg(GuiGraphics $$0, float $$1, int $$2, int $$3) {
        int $$4 = (this.width - this.imageWidth) / 2;
        int $$5 = (this.height - this.imageHeight) / 2;
        $$0.blit(HORSE_INVENTORY_LOCATION, $$4, $$5, 0, 0, this.imageWidth, this.imageHeight);

        if(((StandUser)player).roundabout$getStandPowers() instanceof PowersBlackSabbath) {
        if(((StandUser)player).roundabout$getStand() instanceof BlackSabbathEntity bs) {
            renderEntityInInventoryFollowsMouse(
                    $$0, $$4 + 34, $$5 + 67, 25, (float)($$4 + 51) - this.xMouse, (float)($$5 + 75 - 50) - this.yMouse, bs
            );
        }
        }
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
