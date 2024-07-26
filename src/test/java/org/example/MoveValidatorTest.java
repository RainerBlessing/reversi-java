package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MoveValidatorTest {
    private MoveValidator moveValidator;
    private Player[][] board;

    @BeforeEach
    void setUp() {
        moveValidator = new MoveValidator();
        board = new Player[8][8];

        // Initialisiere das Brett mit NONE
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], Player.NONE);
        }
    }

    @Test
    void testIsValidPosition() {
        assertThat(moveValidator.isValidPosition(board, 0, 0), is(true));
        assertThat(moveValidator.isValidPosition(board, 7, 7), is(true));
        assertThat(moveValidator.isValidPosition(board, -1, 0), is(false));
        assertThat(moveValidator.isValidPosition(board, 0, -1), is(false));
        assertThat(moveValidator.isValidPosition(board, 8, 8), is(false));
    }

    @Test
    void testIsMoveValid() {
        board = createBoard(new String[]{
                "...X..",
                "...O..",
                "......",
                "......",
                "......",
                "...XO."
        });

        // Gültiger Zug für Schwarz
        assertThat(moveValidator.isMoveValid(board, 3, 0, Player.BLACK), is(true));
        // Ungültiger Zug für Schwarz
        assertThat(moveValidator.isMoveValid(board, 2, 2, Player.BLACK), is(false));

        // Gültiger Zug für Weiß
        assertThat(moveValidator.isMoveValid(board, 4, 5, Player.WHITE), is(true));
        // Ungültiger Zug für Weiß
        assertThat(moveValidator.isMoveValid(board, 5, 5, Player.WHITE), is(false));
    }

    @Test
    void testFindMove() {
        // Setze Startpositionen
        board[3][3] = Player.WHITE;
        board[3][4] = Player.BLACK;
        board[4][3] = Player.BLACK;
        board[4][4] = Player.WHITE;

        // Suche nach einem gültigen Zug
        Move move = moveValidator.findMove(board, 2, 4, Direction.DOWN, Player.BLACK);
        assertThat(move, is(notNullValue()));
        assertThat(move.start, is(new Coordinate(2, 4)));
        assertThat(move.end, is(new Coordinate(2, 5)));

        // Suche nach einem ungültigen Zug
        move = moveValidator.findMove(board, 2, 2, Direction.DOWN, Player.BLACK);
        assertThat(move, is(nullValue()));
    }

    private Player[][] createBoard(String[] rows) {
        Player[][] board = new Player[rows.length][rows[0].length()];
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows[i].length(); j++) {
                switch (rows[i].charAt(j)) {
                    case 'X':
                        board[i][j] = Player.BLACK;
                        break;
                    case 'O':
                        board[i][j] = Player.WHITE;
                        break;
                    case '.':
                        board[i][j] = Player.NONE;
                        break;
                    default:
                        throw new IllegalArgumentException("Unbekanntes Zeichen: " + rows[i].charAt(j));
                }
            }
        }
        return board;
    }

    @Test
    void makeMove_right_to_left() {
        board = createBoard(new String[]{
                "..OXX"
        });
        Collection<Move> moves = moveValidator.findAllMoves(board, Player.BLACK);
        assertThat(moves, containsInAnyOrder(
                new Move(Direction.LEFT, new Coordinate(3, 0), new Coordinate(1, 0))
        ));
    }

    @Test
    void makeMove_left_to_right() {
        board = createBoard(new String[]{
                "XXOO."
        });
        Collection<Move> moves = moveValidator.findAllMoves(board, Player.BLACK);
        assertThat(moves, containsInAnyOrder(
                new Move(Direction.RIGHT, new Coordinate(1, 0), new Coordinate(4, 0))
        ));
    }

    @Test
    void makeMove_up_to_down() {
        board = createBoard(new String[]{
                "X",
                "O",
                ".",
                ".",
                "."
        });
        Collection<Move> moves = moveValidator.findAllMoves(board, Player.BLACK);
        assertThat(moves, containsInAnyOrder(
                new Move(Direction.DOWN, new Coordinate(0, 0), new Coordinate(0, 2))
        ));
    }

    @Test
    void makeMove_down_to_up() {
        board = createBoard(new String[]{
                ".",
                "O",
                "X"
        });
        Collection<Move> moves = moveValidator.findAllMoves(board, Player.BLACK);
        assertThat(moves, containsInAnyOrder(
                new Move(Direction.UP, new Coordinate(0, 2), new Coordinate(0, 0))
        ));
    }

    @Test
    void makeMove_diagonal_up_left() {
        board = createBoard(new String[]{
                "....",
                ".O..",
                "..X."
        });
        Collection<Move> moves = moveValidator.findAllMoves(board, Player.BLACK);

        assertThat(moves, contains(new Move(Direction.UP_LEFT, new Coordinate(2, 2), new Coordinate(0, 0))));
    }

    @Test
    void makeMove_diagonal_up_right() {
        board = createBoard(new String[]{
                "....",
                ".O..",
                "X..."
        });
        Collection<Move> moves = moveValidator.findAllMoves(board, Player.BLACK);

        assertThat(moves, contains(new Move(Direction.UP_RIGHT, new Coordinate(0, 2), new Coordinate(2, 0))));
    }

    @Test
    void makeMove_diagonal_down_left() {
        board = createBoard(new String[]{
                "..X.",
                ".O..",
                "....",
                "...."
        });
        Collection<Move> moves = moveValidator.findAllMoves(board, Player.BLACK);

        assertThat(moves, contains(new Move(Direction.DOWN_LEFT, new Coordinate(2, 0), new Coordinate(0, 2))));
    }
}
