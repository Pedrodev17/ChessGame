package chess;

import java.util.Scanner;

public class ChessGame {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);
        boolean isWhiteTurn = true;

        while (true) {
            board.printBoard();
            System.out.println((isWhiteTurn ? "Brancas" : "Pretas") + " - Digite seu movimento (ex: e2 e4): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("sair")) {
                break;
            }

            try {
                String[] parts = input.split(" ");
                int startX = 8 - Integer.parseInt(parts[0].substring(1, 2));
                int startY = parts[0].charAt(0) - 'a';
                int endX = 8 - Integer.parseInt(parts[1].substring(1, 2));
                int endY = parts[1].charAt(0) - 'a';

                Piece piece = board.getPiece(startX, startY);
                if (piece != null && piece.isWhite() == isWhiteTurn && piece.isValidMove(board, startX, startY, endX, endY)) {
                    board.movePiece(startX, startY, endX, endY);
                    isWhiteTurn = !isWhiteTurn;
                } else {
                    System.out.println("Movimento inválido!");
                }
            } catch (Exception e) {
                System.out.println("Formato inválido! Use ex: e2 e4");
            }
        }
        scanner.close();
    }
}