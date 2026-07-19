package xadrez;

// Uma fonte de lances. Quem implementa decide COMO escolher (teclado, sorteio, motor);
// o loop da partida não precisa saber qual é.
public interface Player {

    // Devolve o lance escolhido, ou null se o jogador desistiu da partida.
    Move chooseMove(Board board, GameRules rules, boolean whiteTurn);

    // Rótulo exibido quando o lance é anunciado. Evita que o Main precise
    // testar o tipo concreto do jogador com instanceof.
    default String name() {
        return "JOGADOR";
    }
}
