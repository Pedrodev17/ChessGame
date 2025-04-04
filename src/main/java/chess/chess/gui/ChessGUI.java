package chess.gui;

import chess.Board;
import chess.Piece;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ChessGUI {
    private JFrame frame;
    private JButton[][] squares = new JButton[8][8];
    private Board board = new Board();
    private int selectedX = -1, selectedY = -1;

    public ChessGUI() {
        frame = new JFrame("Java Chess com Imagens");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 8));

        // Cria os botões do tabuleiro
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new JButton();
                squares[i][j].setPreferredSize(new Dimension(80, 80));
                squares[i][j].setBackground((i + j) % 2 == 0 ?
                        new Color(240, 217, 181) : new Color(181, 136, 99));
                squares[i][j].setBorder(BorderFactory.createEmptyBorder());
                squares[i][j].setContentAreaFilled(false);
                squares[i][j].setOpaque(true);

                final int x = i, y = j;
                squares[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(x, y);
                    }
                });
                frame.add(squares[i][j]);
            }
        }

        updateBoard();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleSquareClick(int x, int y) {
        if (selectedX == -1) {
            // Seleciona uma peça
            if (board.getPiece(x, y) != null) {
                selectedX = x;
                selectedY = y;
                squares[x][y].setBackground(new Color(255, 255, 150)); // Amarelo mais suave
            }
        } else {
            // Tenta mover a peça
            Piece piece = board.getPiece(selectedX, selectedY);
            if (piece.isValidMove(board, selectedX, selectedY, x, y)) {
                board.movePiece(selectedX, selectedY, x, y);
                updateBoard();
            }
            // Reseta a seleção
            squares[selectedX][selectedY].setBackground(
                    (selectedX + selectedY) % 2 == 0 ?
                            new Color(240, 217, 181) : new Color(181, 136, 99));
            selectedX = -1;
            selectedY = -1;
        }
    }

    private void updateBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i, j);
                squares[i][j].setIcon(null); // Limpa o ícone anterior

                if (piece != null) {
                    // Carrega a imagem correspondente à peça
                    String color = piece.isWhite() ? "white" : "black";
                    String pieceType = "";

                    if (piece instanceof chess.pieces.Pawn) pieceType = "pawn";
                    else if (piece instanceof chess.pieces.Rook) pieceType = "rook";
                    else if (piece instanceof chess.pieces.Knight) pieceType = "knight";
                    else if (piece instanceof chess.pieces.Bishop) pieceType = "bishop";
                    else if (piece instanceof chess.pieces.Queen) pieceType = "queen";
                    else if (piece instanceof chess.pieces.King) pieceType = "king";

                    String imagePath = "/images/" + color + "_" + pieceType + ".png";
                    ImageIcon icon = createImageIcon(imagePath);

                    if (icon != null) {
                        // Redimensiona a imagem para caber no botão
                        Image img = icon.getImage().getScaledInstance(
                                60, 60, Image.SCALE_SMOOTH);
                        squares[i][j].setIcon(new ImageIcon(img));
                    } else {
                        squares[i][j].setText(String.valueOf(piece.getSymbol()));
                    }
                }
            }
        }
    }

    private ImageIcon createImageIcon(String path) {
        try {
            URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Não foi possível encontrar o arquivo: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGUI());
    }
}