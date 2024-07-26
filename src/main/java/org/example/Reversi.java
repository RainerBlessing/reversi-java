package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Reversi {
    private static final int SIZE = 8; // Größe des Bretts
    private final Player[][] board;
    private final MoveValidator moveValidator;

    public static void main(String[] args) {
        Reversi game = new Reversi();
        game.startGame();
    }

    public Reversi() {
        board = new Player[SIZE][SIZE];
        moveValidator = new MoveValidator();
        initializeBoard();
    }

    /**
     * Erzeuge das Spielfeld
     */
    private void initializeBoard() {
        // Setze alle Felder auf leer
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Player.NONE;
            }
        }

        // Setze die Startpositionen
        board[3][3] = Player.WHITE;
        board[4][4] = Player.WHITE;
        board[3][4] = Player.BLACK;
        board[4][3] = Player.BLACK;
    }

    /**
     * Gib das Spielfeld mit Spaltenbeschriftung aus.
     */
    public void printBoard() {
        // Drucke die Spaltenbeschriftung
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();

        // Drucke das Brett mit Zeilenbeschriftung
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Berechne die Punktzahl des Spielers
     *
     * @param player Spiele
     * @return Punktzahl
     */
    public long calculateScore(Player player) {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .filter(p -> p == player)
                .count();
    }

    /**
     * Startet das Spiel und steuert den Spielablauf.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        Player currentPlayer = Player.BLACK;

        while (true) {
            printBoard();
            System.out.println("Punktzahl Schwarz: " + calculateScore(Player.BLACK));
            System.out.println("Punktzahl Weiß: " + calculateScore(Player.WHITE));

            if (!hasValidMoves(currentPlayer)) {
                System.out.println("Keine gültigen Züge mehr für " + (currentPlayer == Player.BLACK ? "Schwarz" : "Weiß") + ".");
                break;
            }

            System.out.println("Spieler " + (currentPlayer == Player.BLACK ? "Schwarz" : "Weiß") + ", gib deinen Zug ein (z.B. E6): ");
            String move = scanner.nextLine().trim();
            if (move.length() != 2) {
                System.out.println("Ungültige Eingabe. Bitte versuche es erneut.");
                continue;
            }

            char colChar = move.charAt(0);
            char rowChar = move.charAt(1);

            int col = colChar - 'A';
            int row = rowChar - '1';

            if (col < 0 || col >= SIZE || row < 0 || row >= SIZE) {
                System.out.println("Ungültige Koordinaten. Bitte versuche es erneut.");
                continue;
            }

            if (!moveValidator.isMoveValid(board, row, col, currentPlayer)) {
                System.out.println("Ungültiger Zug. Bitte versuche es erneut.");
                continue;
            }

            executeMove(row, col, currentPlayer);
            currentPlayer = (currentPlayer == Player.BLACK) ? Player.WHITE : Player.BLACK;
        }

        scanner.close();
    }

    Move parseMove(String inputString) {
        List<String> coordinateToken = parseMoveStringIntoToken(inputString);

        String startToken = coordinateToken.get(0);
        char colCharStart = startToken.charAt(0);
        char rowCharStart = startToken.charAt(1);

        String endToken = coordinateToken.get(1);
        char colCharEnd = endToken.charAt(0);
        char rowCharEnd = endToken.charAt(1);

        return new Move(colCharStart,rowCharStart,colCharEnd,rowCharEnd);
    }

    ArrayList<String> parseMoveStringIntoToken(String inputString) {
        char separator = determineSeparator(inputString);
        String[] tokens = inputString.split(String.valueOf(separator));

        return Arrays.stream(tokens).map(String::trim).collect(Collectors.toCollection(ArrayList::new));
    }

    char determineSeparator(String inputString) {
        boolean separator = inputString.indexOf(',') > -1;
        char separatorChar = 0;
        if (!separator) {
            separator = inputString.indexOf('-') > -1;
            if (!separator) {
                separator = inputString.indexOf(' ') > -1;
                if (separator) {
                    separatorChar = ' ';
                }
            } else {
                separatorChar = '-';
            }
        } else {
            separatorChar = ',';
        }

        return separatorChar;
    }

    /**
     * Führt einen Zug aus und kehrt alle betroffenen Steine um.
     *
     * @param rowStart die Zeilenkoordinate des Zugs.
     * @param colStart die Spaltenkoordinate des Zugs.
     * @param player   der aktuelle Spieler.
     */
    private void executeMove(int rowStart, int colStart, Player player) {
        board[rowStart][colStart] = player;

        for (Direction direction : Direction.values()) {
            int dr = direction.stepValueY;
            int dc = direction.stepValueX;
            int rowEnd = rowStart + dr;
            int colEnd = colStart + dc;
            boolean foundOpponent = false;

            while (moveValidator.isValidPosition(board, colEnd, rowEnd) && board[rowEnd][colEnd] == player.getOpponent()) {
                rowEnd += dr;
                colEnd += dc;
                foundOpponent = true;
            }

            if (foundOpponent && moveValidator.isValidPosition(board, colEnd, rowEnd) && board[rowEnd][colEnd] == Player.NONE) {
                flipStones(player, colStart, rowStart, colEnd, rowEnd, dr, dc);
            }

            break;
        }
    }

    private void flipStones(Player player, int colStart, int rowStart, int colEnd, int rowEnd, int dr, int dc) {
        int flipR = rowStart + dr;
        int flipC = colStart + dc;
        while (flipR != rowEnd || flipC != colEnd) {
            board[flipR][flipC] = player;
            flipR += dr;
            flipC += dc;
        }
        board[flipR][flipC] = player;
    }

    /**
     * Überprüft, ob ein Spieler noch gültige Züge hat.
     *
     * @param player der Spieler, der überprüft werden soll.
     * @return true, wenn der Spieler noch gültige Züge hat, sonst false.
     */
    private boolean hasValidMoves(Player player) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (moveValidator.isMoveValid(board, i, j, player)) {
                    return true;
                }
            }
        }
        return false;
    }
}
