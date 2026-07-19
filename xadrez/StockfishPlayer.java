package xadrez;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import xadrez.pieces.PromotionPiece;

// Jogador controlado pelo motor Stockfish, conversando pelo protocolo UCI.
//
// O processo fica vivo pela PARTIDA INTEIRA. Abrir e fechar um Stockfish a cada
// lance seria lento e, pior, faria o motor abortar a busca no meio: quando a
// entrada padrão fecha, ele devolve o melhor lance que tiver até ali — que pode
// ser um lance ruim de uma busca incompleta.
public class StockfishPlayer implements Player {

    private final Process process;
    private final BufferedWriter toEngine;
    private final BufferedReader fromEngine;
    private final int depth;

    // depth controla a força: 5 já é um adversário duro, 12 é impiedoso.
    // Lança IOException se o binário "stockfish" não estiver no PATH.
    public StockfishPlayer(int depth) throws IOException {
        this.depth = depth;
        this.process = new ProcessBuilder("stockfish").redirectErrorStream(true).start();
        this.toEngine = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        this.fromEngine = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Handshake do UCI: sem ele o motor pode ignorar os comandos seguintes.
        send("uci");
        waitFor("uciok");
        send("isready");
        waitFor("readyok");
    }

    @Override
    public Move chooseMove(Board board, GameRules rules, boolean whiteTurn) {
        try {
            // O FEN carrega tudo que o motor precisa: peças, vez, roque e en passant.
            send("position fen " + rules.toFen(whiteTurn));
            send("go depth " + depth);

            String answer = waitFor("bestmove");
            if (answer == null) {
                System.out.println("STOCKFISH NAO RESPONDEU.");
                return null;
            }

            // Formato: "bestmove e2e4" ou "bestmove e7e8q ponder d1h5".
            String[] parts = answer.trim().split("\\s+");
            if (parts.length < 2 || parts[1].equals("(none)")) {
                return null;
            }
            return parseUci(parts[1]);

        } catch (IOException e) {
            System.out.println("ERRO NA COMUNICACAO COM O STOCKFISH: " + e.getMessage());
            return null;
        }
    }

    // O UCI manda as casas coladas, com a promoção grudada no fim: "e2e4", "e7e8q".
    // O Move.parse espera o formato que o jogador digita ("e2 e4"), então traduzimos.
    private Move parseUci(String uci) {
        if (uci.length() < 4) {
            return null;
        }

        Move move = Move.parse(uci.substring(0, 2) + " " + uci.substring(2, 4));
        if (move == null) {
            return null;
        }

        // A 5ª letra, quando existe, é a peça da promoção — e as letras do UCI são
        // as mesmas do PromotionPiece, por isso 'n' de knight e não 'c' de cavalo.
        if (uci.length() >= 5) {
            PromotionPiece promotion = PromotionPiece.fromLetter(uci.substring(4, 5));
            if (promotion != null) {
                move = move.withPromotion(promotion);
            }
        }
        return move;
    }

    private void send(String command) throws IOException {
        toEngine.write(command);
        toEngine.newLine();
        toEngine.flush();   // sem o flush o comando fica no buffer e o motor nunca recebe
    }

    // Consome linhas até achar a que começa com o prefixo pedido. Tudo que vem antes
    // são linhas "info" com o progresso da busca, que não interessam aqui.
    private String waitFor(String prefix) throws IOException {
        String line;
        while ((line = fromEngine.readLine()) != null) {
            if (line.startsWith(prefix)) {
                return line;
            }
        }
        return null;
    }

    // Encerra o motor. Deve ser chamado ao fim da partida, senão o processo fica órfão.
    public void close() {
        try {
            send("quit");
            toEngine.close();
        } catch (IOException ignored) {
            // O processo já morreu; não há o que encerrar.
        }
        process.destroy();
    }

    @Override
    public String name() {
        return "STOCKFISH (profundidade " + depth + ")";
    }
}
