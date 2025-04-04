package chess.pieces;

import chess.Board;
import chess.Piece;

public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Board board, int startX, int startY, int endX, int endY) {
        // Movimento de Torre ou Bispo
        boolean isRookMove = (startX == endX || startY == endY);
        boolean isBishopMove = (Math.abs(startX - endX) == Math.abs(startY - endY));
        return (isRookMove || isBishopMove) && isPathClear(board, startX, startY, endX, endY);
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'Q' : 'q';
    }
}