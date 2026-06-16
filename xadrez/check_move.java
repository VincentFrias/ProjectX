package xadrez;

import xadrez.pieces.Movement;
import xadrez.pieces.MoveType;
import xadrez.pieces.Piece;

// Validador de movimento — responsável por dizer se uma peça PODE ir
// da posição startSet até a posição finalSet, considerando geometria,
// cor do alvo e regra de captura do Movement (MoveType).
// Não verifica obstáculos no caminho (use pathIsClear) nem xeque.
// Vicente: deixei as funções como privadas, e a única pública é
// canMoveTo() que orquestra as demais, e será apenas a necessária.
public class check_move {

    // piece    : a peça que está tentando se mover 
    // board    : matriz 8x8 do tabuleiro; null = casa vazia
    // startSet : posição atual {linha, coluna}
    // finalSet : posição destino {linha, coluna}
    // Retorna true se algum dos movimentos permitidos da peça leva
    // exatamente de startSet a finalSet E é compatível com o que tem
    // no destino (vazio ou inimigo).
    private boolean simpleCheckMove(Piece piece, Piece[][] board, int[] startSet, int[] finalSet) {

        Piece target = board[finalSet[0]][finalSet[1]];

        // Verificação universal: aliada no destino é sempre inválido.
        if (target != null && target.isWhite() == piece.isWhite()) {
            return false;
        }

        boolean destEmpty = (target == null);

        // Caso 1: peça de alcance fixo (peão, cavalo, rei).
        // Cada movimento é um salto único; basta somar e comparar.
        if (!piece.isMovement_loop()) {
            for (Movement m : piece.getMovements()) {
                int[] candidate = m.applyTo(startSet);

                if (candidate[0] == finalSet[0] && candidate[1] == finalSet[1]) {
                    if (isCompatible(m.type(), destEmpty)) return true;
                }
            }
            return false;

        // Caso 2: peça deslizante (torre, bispo, dama).
        // Cada Movement é uma DIREÇÃO; itera passo a passo até a borda
        // ou até bater no destino.
        } else {
            for (Movement m : piece.getMovements()) {
                for (int step = 1; step < 8; step++) {
                    int newX = startSet[0] + m.dx() * step;
                    int newY = startSet[1] + m.dy() * step;

                    // saiu do tabuleiro: para esta direção
                    if (newX < 0 || newX > 7 || newY < 0 || newY > 7) {
                        break;
                    }

                    // Bateu no destino? Checa compatibilidade do MoveType
                    if (newX == finalSet[0] && newY == finalSet[1]) {
                        if (isCompatible(m.type(), destEmpty)) return true;
                        break;
                    }
                }
            }
            return false;
        }
    }

    // Cruza a intenção do Movement com o estado real do destino.
    //   MOVE_ONLY        -> destino tem que estar vazio
    //   CAPTURE_ONLY     -> destino tem que ter peça (inimiga, já filtrada antes)
    //   MOVE_OR_CAPTURE  -> tanto faz
    private boolean isCompatible(MoveType type, boolean destEmpty) {
        return switch (type) {
            case MOVE_ONLY       -> destEmpty;
            case CAPTURE_ONLY    -> !destEmpty;
            case MOVE_OR_CAPTURE -> true;
        };
    }

    // Verifica se NÃO há peças no caminho entre startSet e finalSet (exclusivo).
    // Pressuposto: simpleCheckMove já validou que (start -> final) é geometricamente
    // possível pra essa peça (linha reta, diagonal, salto do peão etc.).
    //
    // piece    : a peça que está se movendo (usada só pra ler canJump)
    // board    : matriz 8x8; posição vazia = null
    // startSet : posição atual {linha, coluna}
    // finalSet : posição destino {linha, coluna}
    private boolean pathIsClear(Piece piece, Piece[][] board, int[] startSet, int[] finalSet) {

        // Peças que pulam (cavalo) não se importam com o que tem no meio.
        if (piece.canJump()) return true;

        // signum devolve -1, 0 ou +1 — é exatamente o "passo" em cada eixo.
        // Funciona pra movimentos retos, diagonais, e pro avanço duplo do peão.
        int stepX = Integer.signum(finalSet[0] - startSet[0]);
        int stepY = Integer.signum(finalSet[1] - startSet[1]);

        int x = startSet[0] + stepX;
        int y = startSet[1] + stepY;

        // Caminha do start (exclusivo) até o final (exclusivo).
        // A casa de destino NÃO é checada aqui — pode ter peça inimiga (captura).
        while (x != finalSet[0] || y != finalSet[1]) {
            if (board[x][y] != null) return false;
            x += stepX;
            y += stepY;
        }
        return true;
    }

    // Vicente: Função orquestradora que eu tinha mencionado antes... apenas para uso inGame.
    public boolean canMoveTo(Piece piece, Piece[][] board, int[] startSet, int[] finalSet) {
        return simpleCheckMove(piece, board, startSet, finalSet)
            && pathIsClear(piece, board, startSet, finalSet);
    }
}
