package chess;

import chess.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Board {
    private Piece[][] board;
    private ImageIcon[][] pieceIcons;
    private Piece lastMovedPawn;
    private int lastMovedPawnRow;
    private int lastMovedPawnCol;

    // Construtor
    public Board() {
        board = new Piece[8][8];
        pieceIcons = new ImageIcon[8][8];
        initializeBoard();
        loadPieceImages();
    }

    // --- Métodos de Inicialização ---
    private void initializeBoard() {
        // Peões
        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(false);  // Peões pretos
            board[6][col] = new Pawn(true);   // Peões brancos
        }

        // Peças pretas (linha 0)
        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][3] = new Queen(false);
        board[0][4] = new King(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);

        // Peças brancas (linha 7)
        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][3] = new Queen(true);
        board[7][4] = new King(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);
    }

    private void loadPieceImages() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    updatePieceIcon(row, col, piece);
                }
            }
        }
    }

    // --- Métodos de Acesso ---
    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return board[row][col];
    }

    public ImageIcon getPieceIcon(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return pieceIcons[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            board[row][col] = piece;
            if (piece != null) {
                updatePieceIcon(row, col, piece);
            } else {
                pieceIcons[row][col] = null;
            }
        }
    }

    // --- Lógica do Jogo ---
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = board[startRow][startCol];
        if (piece == null || !piece.isValidMove(this, startRow, startCol, endRow, endCol)) {
            return false;
        }

        // Atualiza o último peão movido (para en passant)
        if (piece instanceof Pawn && Math.abs(endRow - startRow) == 2) {
            lastMovedPawn = piece;
            lastMovedPawnRow = endRow;
            lastMovedPawnCol = endCol;
        } else {
            lastMovedPawn = null;
        }

        // Move a peça
        board[endRow][endCol] = piece;
        board[startRow][startCol] = null;
        pieceIcons[endRow][endCol] = pieceIcons[startRow][startCol];
        pieceIcons[startRow][startCol] = null;
        piece.setMoved(true);

        // Promoção de peão
        if (piece instanceof Pawn && (endRow == 0 || endRow == 7)) {
            board[endRow][endCol] = new Queen(piece.isWhite());
            updatePieceIcon(endRow, endCol, board[endRow][endCol]);
        }

        return true;
    }

    public boolean isInCheck(boolean isWhite) {
        // Encontra a posição do rei
        int kingRow = -1, kingCol = -1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
            if (kingRow != -1) break;
        }

        // Verifica se alguma peça adversária pode atacar o rei
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.isWhite() != isWhite) {
                    if (piece.isValidMove(this, row, col, kingRow, kingCol)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isCheckmate(boolean isWhite) {
        if (!isInCheck(isWhite)) {
            return false;
        }

        // Verifica se existe algum movimento que tire o rei do xeque
        for (int startRow = 0; startRow < 8; startRow++) {
            for (int startCol = 0; startCol < 8; startCol++) {
                Piece piece = board[startRow][startCol];
                if (piece != null && piece.isWhite() == isWhite) {
                    for (int endRow = 0; endRow < 8; endRow++) {
                        for (int endCol = 0; endCol < 8; endCol++) {
                            if (piece.isValidMove(this, startRow, startCol, endRow, endCol)) {
                                // Simula o movimento
                                Piece capturedPiece = board[endRow][endCol];
                                movePiece(startRow, startCol, endRow, endCol);
                                boolean stillInCheck = isInCheck(isWhite);

                                // Desfaz o movimento
                                movePiece(endRow, endCol, startRow, startCol);
                                board[endRow][endCol] = capturedPiece;
                                piece.setMoved(piece.hasMoved());

                                if (!stillInCheck) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    // --- Regras Especiais ---
    public boolean canCastle(int kingRow, int kingCol, int rookRow, int rookCol) {
        Piece king = board[kingRow][kingCol];
        Piece rook = board[rookRow][rookCol];

        if (!(king instanceof King) || !(rook instanceof Rook)) {
            return false;
        }
        if (king.hasMoved() || rook.hasMoved()) {
            return false;
        }
        if (isInCheck(king.isWhite())) {
            return false;
        }

        int direction = rookCol > kingCol ? 1 : -1;
        int currentCol = kingCol + direction;

        while (currentCol != rookCol) {
            if (board[kingRow][currentCol] != null ||
                    isSquareUnderAttack(kingRow, currentCol, king.isWhite())) {
                return false;
            }
            currentCol += direction;
        }

        return true;
    }

    public void doCastle(int kingRow, int kingCol, int rookRow, int rookCol) {
        if (canCastle(kingRow, kingCol, rookRow, rookCol)) {
            int direction = rookCol > kingCol ? 1 : -1;

            // Move o rei
            movePiece(kingRow, kingCol, kingRow, kingCol + 2 * direction);

            // Move a torre
            movePiece(rookRow, rookCol, kingRow, kingCol + direction);
        }
    }

    public boolean canEnPassant(int pawnRow, int pawnCol, int targetRow, int targetCol) {
        Piece pawn = board[pawnRow][pawnCol];

        if (!(pawn instanceof Pawn)) return false;
        if (Math.abs(pawnCol - targetCol) != 1 || Math.abs(pawnRow - targetRow) != 1) return false;

        return lastMovedPawn != null &&
                lastMovedPawn.isWhite() != pawn.isWhite() &&
                lastMovedPawnRow == pawnRow &&
                lastMovedPawnCol == targetCol;
    }

    public void doEnPassant(int pawnRow, int pawnCol, int targetRow, int targetCol) {
        if (canEnPassant(pawnRow, pawnCol, targetRow, targetCol)) {
            // Move o peão
            movePiece(pawnRow, pawnCol, targetRow, targetCol);

            // Remove o peão capturado
            board[pawnRow][targetCol] = null;
            pieceIcons[pawnRow][targetCol] = null;
        }
    }

    // --- Métodos Auxiliares ---
    private void updatePieceIcon(int row, int col, Piece piece) {
        String color = piece.isWhite() ? "white" : "black";
        String pieceName = "";

        if (piece instanceof Pawn) pieceName = "pawn";
        else if (piece instanceof Rook) pieceName = "rook";
        else if (piece instanceof Knight) pieceName = "knight";
        else if (piece instanceof Bishop) pieceName = "bishop";
        else if (piece instanceof Queen) pieceName = "queen";
        else if (piece instanceof King) pieceName = "king";

        String imagePath = "/images/" + color + "_" + pieceName + ".png";
        pieceIcons[row][col] = loadImageIcon(imagePath);
    }

    private ImageIcon loadImageIcon(String path) {
        try {
            URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        60, 60, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + path);
        }
        return null;
    }

    private boolean isSquareUnderAttack(int row, int col, boolean isWhite) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.isWhite() != isWhite) {
                    if (piece.isValidMove(this, r, c, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // --- Método de Depuração ---
    public void printBoard() {
        System.out.println("   a  b  c  d  e  f  g  h");
        System.out.println("  -------------------------");
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + "|");
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece == null) {
                    System.out.print("   ");
                } else {
                    System.out.print(" " + piece.getSymbol() + " ");
                }
            }
            System.out.println("|" + (8 - row));
        }
        System.out.println("  -------------------------");
        System.out.println("   a  b  c  d  e  f  g  h");
    }
}