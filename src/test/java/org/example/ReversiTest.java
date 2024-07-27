package org.example;

import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ReversiTest {
    private Reversi classUnderTest;

    @BeforeEach
    public void setup() {
        classUnderTest = new Reversi();
    }
}