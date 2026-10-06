package net.hydra.jojomod.chocolatedisco.client.screens;

import net.hydra.jojomod.chocolatedisco.ChocolateDiscoGrid;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSelectionState;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoGridTransform;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSounds;
import net.hydra.jojomod.chocolatedisco.network.ChocolateDiscoNetworking;
import net.zetalasis.networking.message.api.ModMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

public class ChocolateDiscoSelectionScreen extends Screen {

    private static final int SMALL_CELL_SIZE = 24;
    private static final int LARGE_CELL_SIZE = 13;
    private static final int CELL_GAP = 1;

    private static final float CELL_TEXT_SCALE = 0.65F;

    /*
     * Colors
     */
    private static final int GOLD = 0xFFD4A72C;
    private static final int DARK_OUTLINE = 0xFF140D05;

    private static final int CELL_BACKGROUND = 0xCC18130A;
    private static final int CELL_HOVER = 0xFFD4A72C;
    private static final int CELL_HOVER_TEXT = 0xFF140D05;

    private static final int TEXT_COLOR = 0xFFD4A72C;

    /*
     * Currently hovered cell.
     */
    private int hoveredColumn = -1;
    private int hoveredRow = -1;

    /*
     * Building Mode drag state.
     */
    private boolean dragging = false;

    private final Player player;

    public ChocolateDiscoSelectionScreen(
            Player player
    ) {

        super(
                Component.literal(
                        "Chocolate Disco"
                )
        );

        this.player = player;
    }

    /*
     * ================================================================
     * GRID SIZE
     * ================================================================
     */

    private int getGridSize() {

        return ChocolateDiscoGridTransform.isLocked(player)
                ? ChocolateDiscoGrid.LARGE_GRID_SIZE
                : ChocolateDiscoGrid.GRID_SIZE;
    }

    private int getCellSize() {

        return getGridSize()
                == ChocolateDiscoGrid.LARGE_GRID_SIZE

                ? LARGE_CELL_SIZE

                : SMALL_CELL_SIZE;
    }

    private int getGridPixelSize() {

        int gridSize =
                getGridSize();

        int cellSize =
                getCellSize();

        return gridSize * cellSize
                + (gridSize - 1) * CELL_GAP;
    }

    /*
     * ================================================================
     * SHIFT STATE
     * ================================================================
     */

    private boolean isShiftDown() {

        long window =
                Minecraft.getInstance()
                        .getWindow()
                        .getWindow();

        return GLFW.glfwGetKey(
                window,
                GLFW.GLFW_KEY_LEFT_SHIFT
        ) == GLFW.GLFW_PRESS

                || GLFW.glfwGetKey(
                window,
                GLFW.GLFW_KEY_RIGHT_SHIFT
        ) == GLFW.GLFW_PRESS;
    }

    @Override
    protected void init() {

        super.init();

        /*
         * Nothing else is needed here.
         *
         * The grid is drawn manually so we can have
         * complete control over the appearance.
         */
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {

        /*
         * Update hovered cell.
         */
        updateHoveredCell(
                mouseX,
                mouseY
        );

        /*
         * Darken the Minecraft world behind the HUD.
         */
        renderBackground(
                guiGraphics
        );

        /*
         * Draw instruction.
         */
        String instruction;

        if (
                ChocolateDiscoSelectionState.isBuildingMode()
        ) {

            instruction =
                    "Click and drag to select tiles";

        } else {

            instruction =
                    "Select a tile";
        }

        int instructionWidth =
                this.font.width(instruction);

        guiGraphics.drawString(
                this.font,
                instruction,
                (this.width - instructionWidth) / 2,
                getGridTop() - 30,
                0xFFFFFFFF
        );

        /*
         * Draw column numbers.
         */
        drawColumnNumbers(
                guiGraphics
        );

        /*
         * Draw row letters.
         */
        drawRowLetters(
                guiGraphics
        );

        /*
         * Draw the current grid.
         */
        drawGrid(
                guiGraphics
        );

        /*
         * Draw currently hovered cell information.
         */
        drawHoveredCell(
                guiGraphics
        );

        super.render(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );
    }

    private void drawGrid(
            GuiGraphics guiGraphics
    ) {

        int left =
                getGridLeft();

        int top =
                getGridTop();

        int gridSize =
                getGridSize();

        int cellSize =
                getCellSize();

        boolean shiftDown =
                isShiftDown();

        for (
                int row = 0;
                row < gridSize;
                row++
        ) {

            for (
                    int column = 0;
                    column < gridSize;
                    column++
            ) {

                int x =
                        left
                                + column
                                * (cellSize + CELL_GAP);

                int y =
                        top
                                + row
                                * (cellSize + CELL_GAP);

                boolean hovered =
                        column == hoveredColumn
                                && row == hoveredRow;

                /*
                 * Normal single-tile selection.
                 */
                boolean selected =
                        !ChocolateDiscoSelectionState.isBuildingMode()
                                && column
                                == ChocolateDiscoSelectionState
                                .getSelectedColumn()
                                && row
                                == ChocolateDiscoSelectionState
                                .getSelectedRow();

                /*
                 * Building Mode multi-selection.
                 */
                boolean buildingSelected =
                        ChocolateDiscoSelectionState
                                .isBuildingMode()

                                && ChocolateDiscoSelectionState
                                .isCellSelected(
                                        column,
                                        row
                                );

                /*
                 * Shift temporarily disables the
                 * hover highlight while dragging.
                 *
                 * Already-selected cells remain highlighted.
                 */
                boolean highlighted =
                        (!shiftDown && hovered)
                                || selected
                                || buildingSelected;

                /*
                 * Dark outline.
                 */
                guiGraphics.fill(
                        x - 1,
                        y - 1,
                        x + cellSize + 1,
                        y + cellSize + 1,
                        DARK_OUTLINE
                );

                /*
                 * Cell background / hover color.
                 */
                guiGraphics.fill(
                        x,
                        y,
                        x + cellSize,
                        y + cellSize,
                        highlighted
                                ? CELL_HOVER
                                : CELL_BACKGROUND
                );

                /*
                 * Cell name.
                 */
                String cellName =
                        ChocolateDiscoGrid.getCellName(
                                column,
                                row
                        );

                /*
                 * Scale only the cell text.
                 */
                guiGraphics.pose().pushPose();

                float textWidth =
                        this.font.width(cellName)
                                * CELL_TEXT_SCALE;

                float textHeight =
                        this.font.lineHeight
                                * CELL_TEXT_SCALE;

                float textX =
                        x
                                + (cellSize - textWidth)
                                / 2.0F;

                float textY =
                        y
                                + (cellSize - textHeight)
                                / 2.0F;

                guiGraphics.pose().translate(
                        textX,
                        textY,
                        0
                );

                guiGraphics.pose().scale(
                        CELL_TEXT_SCALE,
                        CELL_TEXT_SCALE,
                        1.0F
                );

                guiGraphics.drawString(
                        this.font,
                        cellName,
                        0,
                        0,
                        highlighted
                                ? CELL_HOVER_TEXT
                                : TEXT_COLOR
                );

                guiGraphics.pose().popPose();
            }
        }
    }

    private void drawColumnNumbers(
            GuiGraphics guiGraphics
    ) {

        int left =
                getGridLeft();

        int top =
                getGridTop();

        int gridSize =
                getGridSize();

        int cellSize =
                getCellSize();

        for (
                int column = 0;
                column < gridSize;
                column++
        ) {

            String number =
                    String.valueOf(
                            column + 1
                    );

            int textWidth =
                    this.font.width(number);

            int x =
                    left
                            + column
                            * (cellSize + CELL_GAP)
                            + (cellSize - textWidth)
                            / 2;

            guiGraphics.drawString(
                    this.font,
                    number,
                    x,
                    top - 15,
                    GOLD
            );
        }
    }

    private void drawRowLetters(
            GuiGraphics guiGraphics
    ) {

        int left =
                getGridLeft();

        int top =
                getGridTop();

        int gridSize =
                getGridSize();

        int cellSize =
                getCellSize();

        for (
                int row = 0;
                row < gridSize;
                row++
        ) {

            String letter =
                    String.valueOf(
                            (char) ('A' + row)
                    );

            int textWidth =
                    this.font.width(letter);

            int x =
                    left
                            - 15
                            - textWidth;

            int y =
                    top
                            + row
                            * (cellSize + CELL_GAP)
                            + (cellSize - this.font.lineHeight)
                            / 2;

            guiGraphics.drawString(
                    this.font,
                    letter,
                    x,
                    y,
                    GOLD
            );
        }
    }

    private void drawHoveredCell(
            GuiGraphics guiGraphics
    ) {

        String text;

        if (
                ChocolateDiscoSelectionState.isBuildingMode()
        ) {

            /*
             * Building Mode uses multiple selected cells,
             * so do not display the normal single-tile indicator.
             */
            text =
                    "Selected tiles: "
                            + ChocolateDiscoSelectionState
                            .getSelectedCellCount();

        } else {

            /*
             * Normal Mode uses one selected destination.
             */
            if (
                    hoveredColumn < 0
                            || hoveredRow < 0
            ) {

                return;
            }

            String cellName =
                    ChocolateDiscoGrid.getCellName(
                            hoveredColumn,
                            hoveredRow
                    );

            text =
                    "Selected: "
                            + cellName;
        }

        int width =
                this.font.width(text);

        guiGraphics.drawString(
                this.font,
                text,
                (this.width - width) / 2,
                getGridTop()
                        + getGridPixelSize()
                        + 20,
                GOLD
        );
    }

    private void updateHoveredCell(
            double mouseX,
            double mouseY
    ) {

        int left =
                getGridLeft();

        int top =
                getGridTop();

        int gridSize =
                getGridSize();

        int cellSize =
                getCellSize();

        hoveredColumn = -1;
        hoveredRow = -1;

        for (
                int row = 0;
                row < gridSize;
                row++
        ) {

            for (
                    int column = 0;
                    column < gridSize;
                    column++
            ) {

                int x =
                        left
                                + column
                                * (cellSize + CELL_GAP);

                int y =
                        top
                                + row
                                * (cellSize + CELL_GAP);

                if (
                        mouseX >= x
                                && mouseX < x + cellSize
                                && mouseY >= y
                                && mouseY < y + cellSize
                ) {

                    hoveredColumn =
                            column;

                    hoveredRow =
                            row;

                    return;
                }
            }
        }
    }

    /*
     * =========================================================
     * MOUSE CLICK
     * =========================================================
     */

    @Override
    public boolean mouseClicked(
            double mouseX,
            double mouseY,
            int button
    ) {

        /*
         * Left mouse button.
         */
        if (
                button == 0
                        && hoveredColumn >= 0
                        && hoveredRow >= 0
        ) {

            /*
             * =================================================
             * BUILDING MODE
             * =================================================
             */

            if (
                    ChocolateDiscoSelectionState
                            .isBuildingMode()
            ) {

                /*
                 * Start drag selection.
                 */
                dragging = true;

                /*
                 * Clear the previous Building Mode selection
                 * so this drag creates a fresh selection.
                 */
                ChocolateDiscoSelectionState
                        .clearSelectedCells();

                /*
                 * Select the first cell immediately.
                 */
                ChocolateDiscoSelectionState.selectCell(
                        hoveredColumn,
                        hoveredRow
                );

                return true;
            }

            /*
             * =================================================
             * NORMAL MODE
             * =================================================
             */

            ChocolateDiscoSelectionState.setSelectedCell(
                    hoveredColumn,
                    hoveredRow
            );

            System.out.println(
                    "CHOCOLATE DISCO SELECTED "
                            + ChocolateDiscoGrid.getCellName(
                            hoveredColumn,
                            hoveredRow
                    )
            );

            if (
                    this.minecraft != null
                            && this.minecraft.player != null
            ) {

                this.minecraft.player.playSound(
                        ChocolateDiscoSounds.DISCO_SELECT,
                        1.0F,
                        1.0F
                );
            }

            /*
             * Close the HUD.
             */
            this.minecraft.setScreen(
                    null
            );

            return true;
        }

        return super.mouseClicked(
                mouseX,
                mouseY,
                button
        );
    }

    /*
     * =========================================================
     * MOUSE DRAG
     * =========================================================
     */

    @Override
    public boolean mouseDragged(
            double mouseX,
            double mouseY,
            int button,
            double dragX,
            double dragY
    ) {

        /*
         * Only handle left-mouse dragging while
         * Building Mode is active.
         */
        if (
                button == 0
                        && dragging
                        && ChocolateDiscoSelectionState.isBuildingMode()
        ) {

            /*
             * Update the hovered cell first.
             */
            updateHoveredCell(
                    mouseX,
                    mouseY
            );

            /*
             * Read the physical keyboard state directly.
             *
             * Shift held:
             *     pause selection.
             *
             * Shift released:
             *     selection immediately resumes on the
             *     next mouse movement.
             */
            if (
                    !isShiftDown()
                            && hoveredColumn >= 0
                            && hoveredRow >= 0
            ) {

                ChocolateDiscoSelectionState.selectCell(
                        hoveredColumn,
                        hoveredRow
                );
            }

            return true;
        }

        return super.mouseDragged(
                mouseX,
                mouseY,
                button,
                dragX,
                dragY
        );
    }

    /*
     * =========================================================
     * MOUSE RELEASE
     * =========================================================
     */

    @Override
    public boolean mouseReleased(
            double mouseX,
            double mouseY,
            int button
    ) {

        /*
         * Left mouse button released.
         */
        if (
                button == 0
                        && dragging
        ) {

            dragging = false;

            /*
             * Building Mode drag selection is finished.
             */
            int selectedCount =
                    ChocolateDiscoSelectionState
                            .getSelectedCellCount();

            System.out.println(
                    "CHOCOLATE DISCO BUILDING SELECTION: "
                            + selectedCount
                            + " tiles"
            );

            /*
             * Send the selected cells to the server.
             */
            java.util.ArrayList<Object> packetArgs = new java.util.ArrayList<>();
            packetArgs.add(selectedCount);

            for (int cellKey : ChocolateDiscoSelectionState.getSelectedCells()) {
                packetArgs.add(cellKey % 100);
                packetArgs.add(cellKey / 100);
            }

            ModMessageEvents.sendToServer(
                    ChocolateDiscoNetworking.BUILDING_SELECTION,
                    packetArgs.toArray()
            );

            if (
                    this.minecraft != null
                            && this.minecraft.player != null
            ) {

                this.minecraft.player.playSound(
                        ChocolateDiscoSounds.DISCO_SELECT,
                        1.0F,
                        1.0F
                );
            }

            /*
             * Close the HUD.
             */
            this.minecraft.setScreen(
                    null
            );

            return true;
        }

        return super.mouseReleased(
                mouseX,
                mouseY,
                button
        );
    }

    /*
     * =========================================================
     * SCREEN CLOSE
     * =========================================================
     */

    @Override
    public void onClose() {

        dragging = false;

        if (
                this.minecraft != null
                        && this.minecraft.player != null
        ) {

            this.minecraft.player.playSound(
                    ChocolateDiscoSounds.DISCO_SELECT,
                    1.0F,
                    1.0F
            );
        }

        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {

        /*
         * Opening this HUD should NOT pause
         * the Minecraft world.
         */
        return false;
    }

    private int getGridLeft() {

        return (
                this.width
                        - getGridPixelSize()
        ) / 2;
    }

    private int getGridTop() {

        int verticalOffset =
                getGridSize() == 15
                        ? 13
                        : 0;

        return (
                this.height
                        - getGridPixelSize()
        ) / 2
                + verticalOffset;
    }
}