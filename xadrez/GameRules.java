package xadrez;
import xadrez.pieces.*;
import java.util.ArrayList;
import java.util.List;

// Legalidade da jogada, roque, xeque, xeque-mate e afogamento.
public class GameRules {
    private final Board board;
    private final check_move checkMove = new check_move();

    public GameRules(Board board) {
        this.board = board;
    }

    // Tenta mover a peça da posição [l1][c1] para a posição [l2][c2].
    public boolean movePiece(int l1, int c1, int l2, int c2, boolean whiteTurn) {
        return movePiece(l1, c1, l2, c2, whiteTurn, null);
    }

    // Tenta mover a peça da posição [l1][c1] para a posição [l2][c2].
    // Retorna true se o movimento for bem-sucedido, false caso contrário.
    public boolean movePiece(int l1, int c1, int l2, int c2, boolean whiteTurn, PromotionPiece promotionChoice) {
        Piece[][] grid = board.getBoard();
        // Verifica se as posições são válidas e se há uma peça na posição de origem
        if(board.ValidPosition(l1, c1) && board.ValidPosition(l2, c2) && grid[l1][c1] != null) {
            Piece piece = grid[l1][c1];

            // Verifica se a peça pertence ao jogador que está tentando moverível de cada peça vem do check_move.
            if (piece.isWhite() != whiteTurn) {
                return false;
            }

            // Roque: rei que nunca moveu tentando fazer roque... chama a função de roque (tryCastle())
            if (piece instanceof King && !piece.isMoved() && l1 == l2 && Math.abs(c2 - c1) == 2) {
                return tryCastle(l1, c1, c2);
            }

            // Verifica se o movimento é válido para a peça
            if (checkMove.canMoveTo(piece, grid, new int[]{l1, c1}, new int[]{l2, c2})) {
                // Rejeita jogada que deixaria o próprio rei em xeque
                if (moveLeavesKingInCheck(l1, c1, l2, c2, whiteTurn)) {
                    return false;
                }
                grid[l2][c2] = piece;
                grid[l1][c1] = null;
                piece.setMoved();

                // Se a peça movida for um peão e alcançar a linha de promoção, realiza a promoção
                if (piece instanceof Pawn) {
                    Pawn pawn = (Pawn) piece;
                    if (pawn.canPromote(l2)) {
                        PromotionPiece type = (promotionChoice != null) ? promotionChoice : PromotionPiece.QUEEN; // fallback para rainha, mas add um loop na main para evitar de ter entradas inválidas
                        grid[l2][c2] = pawn.promoteTo(type);
                    }
                }
                return true;
            }
        }
        // Se qualquer verificação falhar, o movimento é inválido
        return false;
    }

    // Tenta executar o roque do rei...
    // Só é chamado quando movePiece já confirmou: peça é rei, nunca moveu.
    private boolean tryCastle(int linha, int cRei, int cDestino) {
        Piece[][] grid = board.getBoard();
        Piece rei = grid[linha][cRei];

        // Lado do roque: destino à direita = torre da coluna 7 (curto),
        // à esquerda = torre da coluna 0 (longo).
        boolean ladoRei = (cDestino > cRei);
        int colTorre = ladoRei ? 7 : 0;
        Piece torre = grid[linha][colTorre];

        // Verifica a existência duma torre da mesma cor que nunca se moveu
        if (!(torre instanceof Rook) || torre.isWhite() != rei.isWhite() || torre.isMoved()) {
            return false;
        }

        // Verifica se as casas entre o rei e a torre são vazias
        int passo = ladoRei ? 1 : -1;
        for (int c = cRei + passo; c != colTorre; c += passo) {
            if (grid[linha][c] != null) {
                return false;
            }
        }

        // Estar fora de check, nem cair em check durante a movimentação.
        boolean corInimiga = !rei.isWhite();
        for (int c = cRei; c != cDestino + passo; c += passo) {
            if (isSquareAttacked(linha, c, corInimiga)) {
                return false;
            }
        }

        // Tudo validado; faz as movimentações de ambas as peças
        int colTorreDestino = cDestino - passo;   // torre fica na casa que o rei acabou de cruzar
        grid[linha][cDestino] = rei;
        grid[linha][cRei] = null;
        grid[linha][colTorreDestino] = torre;
        grid[linha][colTorre] = null;
        rei.setMoved();
        torre.setMoved();
        return true;
    }

    // Verifica se a casa [linha][col] está sob ataque de alguma peça da cor oposta....
    // Varre o tabuleiro inteiro por força bruta, para cada peça inimiga.
    public boolean isSquareAttacked(int linha, int col, boolean cor_oposta) {
        Piece[][] grid = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = grid[i][j];
                // pula casa vazia e peças que não são da cor atacante
                if (p == null || p.isWhite() != cor_oposta) {
                    continue;
                }

                // O peão ataca diferente de como anda... logo precisa de uma verificação diferente
                if (p instanceof Pawn) {
                    int dir = p.isWhite() ? -1 : 1;
                    if (i + dir == linha && (j - 1 == col || j + 1 == col)) {
                        return true;
                    }
                    continue;
                }

                // Demais peças atacam normal, verificação normal
                if (checkMove.canMoveTo(p, grid, new int[]{i, j}, new int[]{linha, col})) {
                    return true;
                }
            }
        }
        // Nenhuma peça inimiga alcança a casa... logo, está livre
        return false;
    }

    // Localiza o rei da cor pedida, e retorna sua posição.
    private int[] findKing(boolean cor) {
        Piece[][] grid = board.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = grid[i][j];
                if (p instanceof King && p.isWhite() == cor) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // O rei da cor está sob ataque de alguma peça inimiga?
    public boolean isInCheck(boolean cor) {
        int[] k = findKing(cor);
        if (k == null) return false;
        return isSquareAttacked(k[0], k[1], !cor);
    }

    // Executa a jogada temporariamente, checa o próprio rei, e DESFAZ tudo.
    // Serve para verificar se a posição que ele vair ir é permitido, ou seja, não entra em check
    private boolean moveLeavesKingInCheck(int l1, int c1, int l2, int c2, boolean white) {
        Piece[][] grid = board.getBoard();
        Piece movendo   = grid[l1][c1];
        Piece capturada = grid[l2][c2];

        // Simula o movimento
        grid[l2][c2] = movendo;
        grid[l1][c1] = null;

        boolean emXeque = isInCheck(white);

        // Desfaz o movimento
        grid[l1][c1] = movendo;
        grid[l2][c2] = capturada;

        return emXeque;
    }

    // Todos os lances legais da cor pedida. Força bruta: testa cada peça da cor
    // contra cada casa do tabuleiro, e descarta o que deixaria o próprio rei em xeque.
    // NÃO gera roque — ver comentário em movePiece.
    public List<Move> generateLegalMoves(boolean cor) {
        Piece[][] grid = board.getBoard();
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = grid[i][j];
                if (p == null || p.isWhite() != cor) continue;

                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        if (checkMove.canMoveTo(p, grid, new int[]{i, j}, new int[]{r, c})
                                && !moveLeavesKingInCheck(i, j, r, c, cor)) {
                            moves.add(buildMove(p, i, j, r, c));
                        }
                    }
                }
            }
        }
        return moves;
    }

    // Peão chegando na última linha vira promoção; o resto é lance simples.
    // Só geramos promoção para dama: subpromoção é legal, mas raríssima, e
    // multiplicaria a lista por 4 sem ganho prático.
    private Move buildMove(Piece piece, int fromRow, int fromCol, int toRow, int toCol) {
        if (piece instanceof Pawn && ((Pawn) piece).canPromote(toRow)) {
            return new Move(fromRow, fromCol, toRow, toCol, PromotionPiece.QUEEN);
        }
        return new Move(fromRow, fromCol, toRow, toCol);
    }

    // Xeque-mate e afogamento só precisam saber SE existe jogada, não quais.
    private boolean hasAnyLegalMove(boolean cor) {
        return !generateLegalMoves(cor).isEmpty();
    }

    // Xeque-mate = está em xeque && não tem nenhuma jogada legal para escapar.
    public boolean isCheckmate(boolean white) {
        return isInCheck(white) && !hasAnyLegalMove(white);
    }

    // Afogamento (stalemate) = NÃO está em xeque, mas nenhuma peça tem jogada legal.
    public boolean isStalemate(boolean white) {
        return !isInCheck(white) && !hasAnyLegalMove(white);
    }
}
