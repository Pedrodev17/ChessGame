package chess;

public abstract class Piece {
    private boolean isWhite;  // true para peças brancas, false para pretas
    private boolean hasMoved; // Rastreia se a peça já foi movida (útil para roque, peão, etc.)

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        this.hasMoved = false; // Inicialmente, a peça não foi movida
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public abstract boolean isValidMove(Board board, int startX, int startY, int endX, int endY);
    public abstract char getSymbol();

    protected boolean isPathClear(Board board, int startX, int startY, int endX, int endY) {
        int dx = Integer.compare(endX, startX); // Direção X (-1, 0, 1)
        int dy = Integer.compare(endY, startY); // Direção Y (-1, 0, 1)

        int x = startX + dx;
        int y = startY + dy;

        while (x != endX || y != endY) {
            if (board.getPiece(x, y) != null) {
                return false; // Peça no caminho
            }
            x += dx;
            y += dy;
        }
        return true;
    }

    protected boolean isValidDestination(Board board, int endX, int endY) {
        Piece destinationPiece = board.getPiece(endX, endY);
        return destinationPiece == null || destinationPiece.isWhite() != this.isWhite();
    }
}