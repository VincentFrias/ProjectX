package xadrez.pieces;

public class King extends Piece{
    public King(boolean isWhite) {
        // movement_loop = false porque o rei tem alcance fixo (não desliza).
        super(isWhite, false);

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
        return "K"; 
    }
}