package net.hydra.jojomod.chocolatedisco.client;

import java.util.HashSet;
import java.util.Set;

public class ChocolateDiscoSelectionState {

    private static int selectedColumn = 3;
    private static int selectedRow = 6;

    private static boolean largeGrid = false;

    private static boolean buildingMode = false;

    /*
     * Multiple selected cells used by Building Mode.
     *
     * Each cell is stored as:
     *
     * row * 100 + column
     *
     * This gives us a simple way to store grid coordinates
     * without creating another class yet.
     */
    private static final Set<Integer> selectedCells =
            new HashSet<>();


    public static int getSelectedColumn() {
        return selectedColumn;
    }

    public static int getSelectedRow() {
        return selectedRow;
    }


    public static void setSelectedCell(
            int column,
            int row
    ) {

        selectedColumn = column;
        selectedRow = row;
    }


    /*
     * =========================================================
     * GRID SIZE
     * =========================================================
     */

    public static int getGridSize() {
        return largeGrid ? 15 : 7;
    }


    public static boolean isLargeGrid() {
        return largeGrid;
    }


    public static void toggleGridSize() {

        largeGrid = !largeGrid;

        int gridSize = getGridSize();

        /*
         * Keep the closest row as the reference row.
         *
         * 7x7  -> 4G
         * 15x15 -> 8O
         */
        selectedColumn = gridSize / 2;
        selectedRow = gridSize - 1;
    }


    /*
     * =========================================================
     * BUILDING MODE
     * =========================================================
     */

    public static boolean isBuildingMode() {
        return buildingMode;
    }


    public static void setBuildingMode(
            boolean enabled
    ) {

        buildingMode = enabled;

        /*
         * Changing Building Mode always starts
         * with no multi-tile selection.
         */
        clearSelectedCells();
    }


    /*
     * =========================================================
     * MULTI-TILE SELECTION
     * =========================================================
     */

    private static int getCellKey(
            int column,
            int row
    ) {

        return row * 100 + column;
    }


    public static void selectCell(
            int column,
            int row
    ) {

        selectedCells.add(
                getCellKey(column, row)
        );
    }


    public static boolean isCellSelected(
            int column,
            int row
    ) {

        return selectedCells.contains(
                getCellKey(column, row)
        );
    }


    public static void clearSelectedCells() {
        selectedCells.clear();
    }


    public static Set<Integer> getSelectedCells() {
        return selectedCells;
    }


    public static int getSelectedCellCount() {
        return selectedCells.size();
    }
}