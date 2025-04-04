package chess.pieces;

import chess.Board;
import chess.Piece;

public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Board board, int startX, int startY, int endX, int endY) {
        int direction = isWhite() ? -1 : 1; // Direção do movimento (brancas: -1, pretas: +1)

        // Movimento para frente (1 casa)
        if (startY == endY && endX == startX + direction && board.getPiece(endX, endY) == null) {
            return true;
        }

        // Movimento inicial (2 casas)
        if (!hasMoved() && startY == endY && endX == startX + 2 * direction
                && board.getPiece(endX, endY) == null) {
            return true;
        }

        // Captura (movimento diagonal)
        if (Math.abs(startY - endY) == 1 && endX == startX + direction
                && board.getPiece(endX, endY) != null
                && board.getPiece(endX, endY).isWhite() != isWhite()) {
            return true;
        }

        return false;
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'P' : 'p';
    }
}