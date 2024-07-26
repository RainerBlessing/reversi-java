package org.example;

import java.util.Collection;
import java.util.HashSet;

public class MoveValidator {
    /**
     * Überprüft, ob eine Position innerhalb der Brettgrenzen liegt.
     *
     * @param board das Reversi-Brett.
     * @param x die x-Koordinate.
     * @param y die y-Koordinate.
     * @return true, wenn die Position innerhalb der Brettgrenzen liegt, sonst false.
     */
    public boolean isValidPosition(Player[][] board, int x, int y) {
        return x >= 0 && x < board[0].length && y >= 0 && y < board.length;
    }

    /**
     * Überprüft, ob ein Zug gültig ist.
     *
     * @param board das Reversi-Brett.
     * @param y die Zeilenkoordinate.
     * @param x die Spaltenkoordinate.
     * @param player der aktuelle Spieler.
     * @return true, wenn der Zug gültig ist, sonst false.
     */
    public boolean isMoveValid(Player[][] board, int x, int y, Player player) {
        if (!isValidPosition(board, x, y) || board[y][x] != player) {
            return false;
        }

        for (Direction direction : Direction.values()) {
            int dr = direction.stepValueX;
            int dc = direction.stepValueY;
            int yMove = y + dr;
            int xMove = x + dc;
            boolean foundOpponent = false;

            while (isValidPosition(board, xMove, yMove) && board[yMove][xMove] == player.getOpponent()) {
                yMove += dr;
                xMove += dc;
                foundOpponent = true;
            }

            if (foundOpponent && isValidPosition(board, xMove, yMove) && board[yMove][xMove] == Player.NONE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Findet das Ende eines gültigen Zugs in einer bestimmten Richtung.
     *
     * @param board das Reversi-Brett.
     * @param x die Start-x-Koordinate.
     * @param y die Start-y-Koordinate.
     * @param dir die Bewegungsrichtung.
     * @param otherPlayer der gegnerische Spieler.
     * @return ein Move-Objekt, das den Zug repräsentiert, oder null, wenn kein gültiger Zug gefunden wurde.
     */
    public Move findMove(Player[][] board, int x, int y, Direction dir, Player otherPlayer) {
        int endX = x + dir.stepValueX;
        int endY = y + dir.stepValueY;

        while (isValidPosition(board, endX, endY) && board[endY][endX] == otherPlayer) {
            endX += dir.stepValueX;
            endY += dir.stepValueY;
        }

        if (isValidPosition(board, endX, endY) && board[endY][endX] == Player.NONE) {
            return new Move(dir, new Coordinate(x, y), new Coordinate(endX, endY));
        }
        return null;
    }

    /**
     * Findet alle möglichen Züge für einen Spieler.
     *
     * @param board das Reversi-Brett.
     * @param player der aktuelle Spieler.
     * @return eine Sammlung von möglichen Zügen.
     */
    public Collection<Move> findAllMoves(Player[][] board, Player player) {
        Collection<Move> moves = new HashSet<>();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == player) {
                    iterateAllDirections(board, player, x, y, moves);
                }
            }
        }
        return moves;
    }

    private void iterateAllDirections(Player[][] board, Player player, int x, int y, Collection<Move> moves) {
        for (Direction dir : Direction.values()) {
            int newX = x + dir.stepValueX;
            int newY = y + dir.stepValueY;

            if (isValidPosition(board, newX, newY) && board[newY][newX] == player.getOpponent()) {
                Move move = findMove(board, x, y, dir, player.getOpponent());
                if (move != null) {
                    moves.add(move);
                }
            }
        }
    }
}
