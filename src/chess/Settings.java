package chess;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

public class Settings extends JPanel {
    
	private int width;
	private int height;
	private Chess chess;

	class SettingsDialog extends Dialog {

		public SettingsDialog(Frame owner) {
			super(owner);
			setTitle("SETTINGS");
			String image_location = "/images/chess_board.png";
			URL imageURL = getClass().getResource(image_location);
			ImageIcon image = new ImageIcon(imageURL);
			setIconImage(image.getImage());
			setBounds(chess.getX() + chess.getWidth() / 4, chess.getY() + chess.getHeight() / 4,
					chess.getWidth() / 2, chess.getHeight() / 2);
			setBackground(Color.DARK_GRAY);
			setResizable(false);

			// Create the first label and dropdown menu
			JLabel label = new JLabel("Set game format");
			label.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
			label.setForeground(Color.ORANGE);

			String[] options = {"10 + 0", "10 + 5", "15 + 10"};
			JComboBox<String> dropdown = new JComboBox<>(options);

			dropdown.addActionListener(e -> {
                String selectedItem = (String) dropdown.getSelectedItem();

                switch (selectedItem) {
                    case "10 + 0":
                        chess.setDuration(600);
						chess.setIncrement(0);
                        break;
                    case "10 + 5":
						chess.setDuration(600);
						chess.setIncrement(5);
                        break;
                    case "15 + 10":
						chess.setDuration(900);
						chess.setIncrement(10);
                        break;
                }
            });

			JPanel top_panel = new JPanel(new GridLayout(1, 2, 10, 10));
			top_panel.setBackground(Color.DARK_GRAY);
			top_panel.add(label);
			top_panel.add(dropdown);

			JPanel bottom_panel = new JPanel(new GridLayout(1, 2));
			bottom_panel.setBackground(Color.DARK_GRAY);

			JButton new_game = new JButton();
			new_game.setText("New Game");
			new_game.setFocusable(false);
			new_game.addActionListener((ae) -> {
				chess.getBoard().clear();
				UserPlayer player = new UserPlayer(chess, 'W');
				AIPlayer computer = new AIPlayer(chess, 'B');
				chess.setPlayer(player);
				chess.setComputer(computer);
				chess.setCurrentPlayer(player);
				this.dispose();
				chess.getBoard().repaint();
				chess.getGameInfo().clear(player, computer);
				chess.setMoveNumber(0);
				chess.getGameInfo().clearMoves();
				chess.getGameInfo().setPlayerClock(chess.getDuration());
				chess.getGameInfo().setComputerClock(chess.getDuration());
				chess.getGameInfo().setPlayerClockColor(Color.ORANGE);
				chess.getGameInfo().setComputerClockColor(Color.GRAY);
				chess.setLastMove(new Move());
				chess.startGame();
			});

			JButton exit = new JButton();
			exit.setText("Exit");
			exit.setFocusable(false);
			exit.addActionListener((ae) -> {
				dispose();
			});

			JPanel left_panel = new JPanel();
			left_panel.setBackground(Color.DARK_GRAY);
			left_panel.add(new_game);

			JPanel right_panel = new JPanel();
			right_panel.setBackground(Color.DARK_GRAY);
			right_panel.add(exit);

			bottom_panel.add(left_panel);
			bottom_panel.add(right_panel);

			JPanel panel = new JPanel(new GridLayout(2, 1, 50, 50));
			panel.setBackground(Color.DARK_GRAY);
			panel.add(top_panel);
			panel.add(bottom_panel);
			add(panel);

			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});

			this.setVisible(true);
		}

	}
	
	public Settings(Chess chess) {
		this.chess = chess;
		this.width = 30;
		this.height = 30;
		this.setPreferredSize(new Dimension(50, 50));
        this.setBackground(Color.DARK_GRAY);
        UIManager.put("ToolTip.background", Color.LIGHT_GRAY);
        UIManager.put("ToolTip.foreground", Color.BLACK);
        UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 12));
        this.setToolTipText("Settings");
        
        addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		setBackground(Color.LIGHT_GRAY);
        		width = 30;
        		height = 30;
        		repaint();
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
				setBackground(Color.DARK_GRAY);
				width = 30;
				height = 30;
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				new SettingsDialog(chess);
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D)g;
        String imgLocation = "/images/settings.png";
    	URL imageURL = getClass().getResource(imgLocation);
    	ImageIcon image = new ImageIcon(imageURL);
        g2D.drawImage(image.getImage(), (50 - width) / 2, (50 - height) / 2, width, height, null);
	}
	
}
