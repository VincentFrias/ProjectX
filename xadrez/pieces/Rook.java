package xadrez.pieces;

import xadrez.Board;
import xadrez.Move;

public class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean checkValidMove(Board board, Move move) {
        if (move.fromLine != move.toLine && move.fromColumn != move.toColumn) {
            return false;
        }
        return true;
    }
}
