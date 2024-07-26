package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ReversiTest {
    private Reversi classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new Reversi();
    }

    @Test
    void parseMove_invalid_move() {
        String inputString = "";
        Move move = classUnderTest.parseMove(inputString);

        Move invalidMove = new Move();

        assertThat(move, is(invalidMove));
    }

    @Test
    void parseMove_valid_move() {
        String inputString = "A1, C3";
        Move move = classUnderTest.parseMove(inputString);

        Move validMove = new Move(Direction.DOWN_RIGHT, new Coordinate(0, 0), new Coordinate(2, 2));

        assertThat(move, is(validMove));
    }

    @Test
    void determineSeparator() {
        String inputString = "A1, C3";

        char c = classUnderTest.determineSeparator(inputString);

        assertThat(c, is(','));

        inputString = "A1 - C3";

        c = classUnderTest.determineSeparator(inputString);

        assertThat(c, is('-'));

        inputString = "A1   C3";

        c = classUnderTest.determineSeparator(inputString);

        assertThat(c, is(' '));
    }

    @Test
    void parseMoveStringIntoToken(){
        String inputString = "A1, C3";

        ArrayList<String> trimmedTokens = classUnderTest.parseMoveStringIntoToken(inputString);

        assertThat(trimmedTokens.get(0), is("A1"));
        assertThat(trimmedTokens.get(1), is("C3"));
    }
}