package xadrez;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Menu extends JFrame {
	CardLayout cardLayout = new CardLayout();
	JPanel base = new JPanel(cardLayout);

	JPanel menuInicial = new Imagens("/home/newhouse/poo/ProjectX");
	JPanel menuModoDeJogo = new Imagens("");
	JPanel creditos = new Imagens("");

	JButton botaoModoDeJogo = new JButton("Modo de Jogo");
	JButton botaoCreditos = new JButton("Créditos");
	JButton botaoRobo = new JButton("Contra a Máquina");
	JButton botaoPlayer = new JButton ("Contra outro Jogador");

	Menu() {
		base.add(menuInicial, "menuInicial");
		base.add(menuModoDeJogo, "menuModoDeJogo");
		base.add(creditos, "creditos");

		menuInicial.add(botaoModoDeJogo);
		menuInicial.add(botaoCreditos);
		menuModoDeJogo.add(botaoRobo);
		menuModoDeJogo.add(botaoPlayer);

		botaoModoDeJogo.addActionListener((e) -> {
			cardLayout.show(base, "menuModoDeJogo");
		});
		
		botaoPlayer.addActionListener((e) -> {
			try {
				new ProcessBuilder("echo", "player")
					.inheritIO()
					.start();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		botaoRobo.addActionListener((e) -> {
			try {
				new ProcessBuilder("echo", "robo")
					.inheritIO()
					.start();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		add(base);
		setSize(1000, 1000);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new Menu();
	}
}
