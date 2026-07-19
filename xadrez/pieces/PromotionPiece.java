package xadrez.pieces;

public enum PromotionPiece {
    QUEEN ('q'),
    ROOK  ('r'),
    BISHOP('b'),
    KNIGHT('n');

    private final char letter;

    PromotionPiece(char letter) {
        this.letter = letter;
    }

    // Letra digitada pelo jogador -> constante. Devolve null se não corresponder a nenhuma.
    public static PromotionPiece fromLetter(String text) {
        if (text == null || text.length() != 1) {
            return null;
        }
        char input = Character.toLowerCase(text.charAt(0));
        for (PromotionPiece piece : values()) {
            if (piece.letter == input) {
                return piece;
            }
        }
        return null;
    }

    public char letter() {
        return letter;
    }
}