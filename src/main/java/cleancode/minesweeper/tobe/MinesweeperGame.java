package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    public static final int LAND_MINE_COUNT = 10;

    public static final Scanner SCANNER = new Scanner(System.in);

    public static final String FLAG_SIGN = "⚑";
    public static final String LAND_MINE_SIGN = "☼";
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";

    private static final int GAME_PLAYING = 0;
    private static final int GAME_CLEAR = 1;
    private static final int GAME_OVER = -1;

    private static final String USER_ACTION_OPEN = "1";
    private static final String USER_ACTION_FLAG = "2";

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializedGame();

        while (true) {
            try {
                showBoard();

                if (doseUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doseUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOrCell(cellInput, userActionInput);
            } catch (AppException e) { // 의도한 예외
                System.out.println(e.getMessage());
            } catch (Exception e) { // 의도하지 못한 예외
                System.out.println("프로그램에 문제가 생겼습니다.");
            }
        }
    }

    private static void actOrCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doseUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
            checkIfGameIsClear();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
                changeGameStatusDoLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsClear();
            return;
        }
        throw new AppException(String.format("잘못된 번호('%s') 선택하셨습니다.", userActionInput));
    }

    private static void changeGameStatusDoLose() {
        gameStatus = GAME_OVER;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return LAND_MINES[selectedRowIndex][selectedColIndex];
    }

    private static boolean doseUserChooseToOpenCell(String userActionInput) {
        return USER_ACTION_OPEN.equals(userActionInput);
    }

    private static boolean doseUserChooseToPlantFlag(String userActionInput) {
        return USER_ACTION_FLAG.equals(userActionInput);
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doseUserLoseTheGame() {
        return gameStatus == GAME_OVER;
    }

    private static boolean doseUserWinTheGame() {
        return gameStatus == GAME_CLEAR;
    }

    private static void checkIfGameIsClear() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = GAME_CLEAR;
    }

    private static boolean isAllCellOpened() {
        return Arrays.stream(BOARD) // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String>
                .noneMatch(CLOSED_CELL_SIGN::equals);
//                .noneMatch(cell -> CLOSED_CELL_SIGN.equals(cell));
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow);
        if (rowIndex > BOARD_ROW_SIZE) {
            throw new AppException(String.format("잘못된 행('%s') 입력 입니다.", cellInputRow));
        }

        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                throw new AppException(String.format("잘못된 열('%s') 입력 입니다.", cellInputCol));
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int i = 0; i < BOARD_ROW_SIZE; i++) {
            System.out.printf("%d  ", i + 1);
            for (int j = 0; j < BOARD_COL_SIZE; j++) {
                System.out.print(BOARD[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializedGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = CLOSED_CELL_SIGN;
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) {
                    NEARBY_LAND_MINE_COUNTS[row][col] = 0;
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                NEARBY_LAND_MINE_COUNTS[row][col] = count;
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && LAND_MINES[row - 1][col - 1]) {
            count++;
        }
        if (row - 1 >= 0 && LAND_MINES[row - 1][col]) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && LAND_MINES[row - 1][col + 1]) {
            count++;
        }
        if (col - 1 >= 0 && LAND_MINES[row][col - 1]) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && LAND_MINES[row][col + 1]) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && LAND_MINES[row + 1][col - 1]) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && LAND_MINES[row + 1][col]) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && LAND_MINES[row + 1][col + 1]) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        if (!BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
            return;
        }
        if (LAND_MINES[row][col]) {
            return;
        }
        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) {
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            BOARD[row][col] = OPENED_CELL_SIGN;
        }
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
