package xadrez.pieces;
import xadrez.Board;
import xadrez.Move;

public abstract class Piece {
	protected boolean isWhite;
	
	Piece(boolean isWhite) {
		this.isWhite = isWhite;
	}

	public boolean getIsWhite() {
		return isWhite;
	}

	public abstract boolean checkValidMove(Board board, Move move);
}
