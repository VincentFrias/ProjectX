package xadrez;

import java.util.List;
import java.util.Random;

// Jogador artificial mais simples possível: sorteia um lance legal qualquer.
// É o modo "mega fácil" — não avalia posição, não prefere captura, não vê xeque-mate.
public class RandomPlayer implements Player {

    private final Random random = new Random();

    @Override
    public Move chooseMove(Board board, GameRules rules, boolean whiteTurn) {
        List<Move> legalMoves = rules.generateLegalMoves(whiteTurn);

        // Não deveria acontecer: o Main testa mate e afogamento antes de pedir o lance.
        if (legalMoves.isEmpty()) {
            return null;
        }
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }

    @Override
    public String name() {
        return "COMPUTADOR (ALEATÓRIO)";
    }
}