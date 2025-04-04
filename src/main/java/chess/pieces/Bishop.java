package chess.pieces;

import chess.Board;
import chess.Piece;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Board board, int startX, int startY, int endX, int endY) {
        // Movimento diagonal
        if (Math.abs(startX - endX) != Math.abs(startY - endY)) {
            return false;
        }
        return isPathClear(board, startX, startY, endX, endY);
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'B' : 'b';
    }
}