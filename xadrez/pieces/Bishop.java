package xadrez.pieces;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        // movement_loop = true porque o bispo tem alcance dinâmico (desliza).
        super(isWhite, true);

        // 4 direções diagonais.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 && dy != 0) baseMovements.add(new Movement(dx, dy));
            }
        }
    }
    @Override
    public String toString() { 
        return "b"; 
    }
}
