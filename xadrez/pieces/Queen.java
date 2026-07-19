package xadrez.pieces;

public class Queen extends Piece {
    public Queen(boolean isWhite) {
        // movement_loop = true porque a rainha tem alcance dinâmico (desliza).
        super(isWhite, true);

        // 8 direções ao redor: combina dx, dy em {-1, 0, +1} e pula o (0,0)
        // (o (0,0) seria "ficar parado", que não é um movimento válido).
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                baseMovements.add(new Movement(dx, dy));
            }
        }
    }
    @Override
    public String toString() {
        return "Q";
    }

    @Override
    public char fenLetter() { return 'q'; }
}
