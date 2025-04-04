package chess.pieces;

import chess.Board;
import chess.Piece;

public class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Board board, int startX, int startY, int endX, int endY) {
        // Movimento na mesma linha ou coluna
        if (startX != endX && startY != endY) {
            return false;
        }

        // Verifica se o caminho está livre (implemente isPathClear())
        return isPathClear(board, startX, startY, endX, endY);
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'R' : 'r';
    }
}