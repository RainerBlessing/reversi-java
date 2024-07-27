package org.example;

import java.util.Arrays;
import java.util.Scanner;

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

            if (checkForGameOver(currentPlayer)) break;

            Move move = playerInput(currentPlayer, scanner);
            if (move == null) continue;

            executeMove(move, currentPlayer);
            currentPlayer = (currentPlayer == Player.BLACK) ? Player.WHITE : Player.BLACK;
        }

        scanner.close();
    }

    private boolean checkForGameOver(Player currentPlayer) {
        if (!hasValidMoves(currentPlayer)) {
            System.out.println("Keine gültigen Züge mehr für " + (currentPlayer == Player.BLACK ? "Schwarz" : "Weiß") + ".");
            return true;
        }
        return false;
    }

    private Move playerInput(Player currentPlayer, Scanner scanner) {
        System.out.println("Spieler " + (currentPlayer == Player.BLACK ? "Schwarz" : "Weiß") + ", gib deinen Zug ein (z.B. E4 E6): ");
        String moveString = scanner.nextLine().trim();
        Move move = new MoveParser().parseMove(moveString);
        if (!moveValidator.isMoveValid(board, move, currentPlayer)) {
            System.out.println("Ungültiger Zug. Bitte versuche es erneut.");
            return null;
        }
        return move;
    }

    private void executeMove(Move move, Player currentPlayer) {
        executeMove(currentPlayer,move.start.getX(),move.start.getY(),move.end.getX(),move.end.getY(),move.direction.stepValueY,move.direction.stepValueX);
    }

    /**
     * Führt einen Zug aus und kehrt alle betroffenen Steine um.
     */
    private void executeMove(Player player, int colStart, int rowStart, int colEnd, int rowEnd, int dr, int dc) {
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
