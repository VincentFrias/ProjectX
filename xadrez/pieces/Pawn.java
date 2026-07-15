package xadrez.pieces;

public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        // movement_loop = false porque o peão tem alcance fixo (não desliza).
        super(isWhite, false);
        
        // branco começa na linha 6 da matriz e vai para a linha 0 (linha diminui, logo -1)
        // branco começa na linha1 da matriz e vai para a linha 7 (linha aumenta, logo +1)
        int dir = isWhite ? -1 : 1;

        // Avanço normal: 1 casa pra frente, mesma coluna. Só vale se destino vazio.
        baseMovements.add(new Movement(1 * dir, 0, MoveType.MOVE_ONLY));

        // Capturas diagonais: só valem se destino tiver inimigo.
        baseMovements.add(new Movement(1 * dir,  1, MoveType.CAPTURE_ONLY));
        baseMovements.add(new Movement(1 * dir, -1, MoveType.CAPTURE_ONLY));

        // Avanço duplo: 2 casas pra frente, mesma coluna. Só na primeira jogada
        // e (como todo avanço) só se destino vazio.
        specialMovements.add(new Movement(2 * dir, 0, MoveType.MOVE_ONLY));
    }

    // Verifica se o peão pode ser promovido na linha especificada.
    public boolean canPromote(int row) { 
        // "row" é a linha de destino do peão
        // Branco promove na linha 0, preto na linha 7
        return (isWhite && row == 0) || (!isWhite && row == 7);
    }

    public Piece promoteTo(String pieceType) {
        switch (pieceType.toLowerCase()) {
            case "q": //Promoção para Rainha
                return new Queen(isWhite);
            case "r": //Promoção para Torre
                return new Rook(isWhite);
            case "b": //Promoção para Bispo
                return new Bishop(isWhite);
            case "c": //Promoção para Cavalo
                return new Knight(isWhite);
            default:
                throw new IllegalArgumentException("Tipo de peça inválido para promoção: " + pieceType);
        }
    }

    @Override
    public String toString() { 
        return "p"; 
    }
}
