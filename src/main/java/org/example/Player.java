package org.example;

public enum Player {
    NONE('-'),
    BLACK('B'),
    WHITE('W');

    private final char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

    /**
     * Gibt den gegnerischen Spieler zurück.
     * @return der gegnerische Spieler.
     */
    public Player getOpponent() {
        return this == BLACK ? WHITE : BLACK;
    }
}