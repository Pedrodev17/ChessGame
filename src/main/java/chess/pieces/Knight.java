package chess.pieces;

import chess.Board;
import chess.Piece;

public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Board board, int startX, int startY, int endX, int endY) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'N' : 'n'; // 'N' para Cavalo (Knight)
    }
}
