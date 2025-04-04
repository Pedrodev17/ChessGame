package chess.pieces;

import chess.Board;
import chess.Piece;

public class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(Board board, int startX, int startY, int endX, int endY) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);

        // Movimento normal (1 casa)
        if (dx <= 1 && dy <= 1) {
            return isValidDestination(board, endX, endY);
        }

        // Roque (movimento de 2 casas horizontal)
        if (dx == 0 && dy == 2 && !hasMoved()) {
            int rookY = (endY > startY) ? 7 : 0; // Torre no canto
            Piece rook = board.getPiece(startX, rookY);

            return rook != null && !rook.hasMoved()
                    && isPathClear(board, startX, startY, startX, rookY);
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return isWhite() ? 'K' : 'k';
    }
}