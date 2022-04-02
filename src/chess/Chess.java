package chess;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Chess extends JFrame implements Runnable {

    public enum GameState {ACTIVE, WIN, DEFEAT}

    private Board board;
    private UserPlayer player;
    private AIPlayer computer;
    private Thread thread;
    public boolean is_end = false;
    private Player current_player;
    private GameState game_state = GameState.ACTIVE;
    private GameInfo game_info;
    private Move last_move = new Move();
    private int move_number = 0;

    class ResultDialog extends Dialog {

        public ResultDialog(Frame owner) {
            super(owner);
            JLabel label = new JLabel();
            setTitle("GAME OVER");
            String image_location = "/images/chess_board.png";
            URL imageURL = getClass().getResource(image_location);
            ImageIcon image = new ImageIcon(imageURL);
            setIconImage(image.getImage());
            setBounds(Chess.this.getX() + Chess.this.getWidth() / 4, Chess.this.getY() + Chess.this.getHeight() / 4,
                    Chess.this.getWidth() / 2, Chess.this.getHeight() / 2);
            setBackground(Color.DARK_GRAY);
            setResizable(false);

            if(game_state == GameState.WIN) {
                label.setText("YOU WON!!!");
                label.setForeground(Color.GREEN);
            }
            else if(game_state == GameState.DEFEAT) {
                label.setText("YOU LOST...");
                label.setForeground(Color.RED);
            }

            label.setFont(new Font("Arial", Font.PLAIN, 40));
            label.setBackground(Color.DARK_GRAY);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);

            JPanel north_panel = new JPanel(new BorderLayout());
            north_panel.setBackground(Color.DARK_GRAY);
            north_panel.add(label);

            JPanel south_panel = new JPanel(new GridLayout(1, 2));
            south_panel.setBackground(Color.DARK_GRAY);

            JButton new_game = new JButton();
            new_game.setText("New Game");
            new_game.setFocusable(false);
            new_game.addActionListener((ae) -> {
                board.clear();
                player = new UserPlayer(Chess.this, 'W');
                computer = new AIPlayer(Chess.this, 'B');
                this.dispose();
                board.repaint();
                game_info.clear(player, computer);
                Chess.this.move_number = 0;
                Chess.this.game_info.clearMoves();
                Chess.this.game_info.setPlayerClock(900);
                Chess.this.game_info.setComputerClock(900);
                Chess.this.game_info.setPlayerClockColor(Color.ORANGE);
                Chess.this.game_info.setComputerClockColor(Color.GRAY);
                startGame();
            });

            JButton exit = new JButton();
            exit.setText("Exit");
            exit.setFocusable(false);
            exit.addActionListener((ae) -> {
                Chess.this.dispose();
            });

            JPanel left_panel = new JPanel();
            left_panel.setBackground(Color.DARK_GRAY);
            left_panel.add(new_game);

            JPanel right_panel = new JPanel();
            right_panel.setBackground(Color.DARK_GRAY);
            right_panel.add(exit);

            south_panel.add(left_panel);
            south_panel.add(right_panel);

            JPanel panel = new JPanel(new GridLayout(2, 1));
            panel.setBackground(Color.DARK_GRAY);
            panel.add(north_panel);
            panel.add(south_panel);
            add(panel);

            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    Chess.this.dispose();
                }
            });

            this.setVisible(true);
        }

    }

    Chess() {
        this.setTitle("Chess");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(200, 100, 850, 450);
        String image_location = "/images/chess_board.png";
        URL imageURL = getClass().getResource(image_location);
    	ImageIcon image = new ImageIcon(imageURL);
        this.setIconImage(image.getImage());
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.populateWindow();
        this.startGame();
    }
    
    public void populateWindow() {
    	board = new Board(this);
        player = new UserPlayer(this, 'W');
        computer = new AIPlayer(this, 'B');
        current_player = player;
        add(board, BorderLayout.CENTER);

        JPanel panel;
        JLabel label;
        JPanel south_panel = new JPanel();
        south_panel.setPreferredSize(new Dimension(450, 50));
        south_panel.setBackground(Color.DARK_GRAY);
        south_panel.setLayout(new BorderLayout());
        Settings settings = new Settings();
        JPanel letters = new JPanel();
        letters.setLayout(new GridLayout(1, 8, 0, 0));
        letters.setPreferredSize(new Dimension(400, 50));
        letters.setBackground(Color.DARK_GRAY);
        
        for(int i = 0; i < 8; ++i) {
            panel = new JPanel();
            panel.setBackground(Color.DARK_GRAY);
            label = new JLabel("" + (char)('A' + i));
            label.setBackground(Color.DARK_GRAY);
            label.setForeground(Color.BLACK);
            label.setFont(new Font("Arial", Font.PLAIN, 32));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            panel.add(label);
            letters.add(panel);
        }
        
        JPanel empty_panel = new JPanel();
        empty_panel.setBackground(Color.DARK_GRAY);
        empty_panel.setPreferredSize(new Dimension(400, 50));
        
        south_panel.add(settings, BorderLayout.WEST);
        south_panel.add(letters, BorderLayout.CENTER);
        south_panel.add(empty_panel, BorderLayout.EAST);
        add(south_panel, BorderLayout.SOUTH);

        JPanel west_panel = new JPanel();
        west_panel.setPreferredSize(new Dimension(50, 400));
        west_panel.setBackground(Color.DARK_GRAY);
        west_panel.setLayout(new GridLayout(8, 1, 0, 0));
        
        for(int i = 0; i < 8; ++i) {
            panel = new JPanel();
            panel.setBackground(Color.DARK_GRAY);
            label = new JLabel("" + (8 - i));
            label.setBackground(Color.DARK_GRAY);
            label.setForeground(Color.BLACK);
            label.setFont(new Font("Arial", Font.PLAIN, 32));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            panel.add(label);
            west_panel.add(panel);
        }
        
        add(west_panel, BorderLayout.WEST);

        this.game_info = new GameInfo(this);
        add(game_info, BorderLayout.EAST);
        
        pack();
    }
    
    public Board getBoard() {
    	return board;
    }

    public UserPlayer getPlayer() {
		return player;
	}

	public AIPlayer getComputer() {
		return computer;
	}

    public Player getCurrentPlayer() {
        return current_player;
    }

    public void setCurrentPlayer(Player player) {
        current_player = player;

        if(current_player == this.player) {
            if(this.player.getTime() <= 60)
                game_info.setPlayerClockColor(Color.RED);
            else
                game_info.setPlayerClockColor(Color.ORANGE);

            game_info.setComputerClockColor(Color.GRAY);

            if(move_number > 2) {
                this.computer.setTime(this.computer.getTime() + 10);
                game_info.setComputerClock(this.computer.getTime());
            }
        }
        else {
            if(this.computer.getTime() <= 60)
                game_info.setComputerClockColor(Color.RED);
            else
                game_info.setComputerClockColor(Color.ORANGE);

            game_info.setPlayerClockColor(Color.GRAY);

            if(move_number > 2) {
                this.player.setTime(this.player.getTime() + 10);
                game_info.setPlayerClock(this.player.getTime());
            }
        }
    }

    public void setGameState(GameState state) {
        game_state = state;
    }

    public GameInfo getGameInfo() {
        return game_info;
    }

    public Move getLastMove() {
        return last_move;
    }

    public void setLastMove(Move last_move) {
        this.last_move = last_move;
    }

    public int getMoveNumber() {
        return move_number;
    }

    public void setMoveNumber(int move_number) {
        this.move_number = move_number;
    }

    @Override
    public void run() {
        while(!thread.isInterrupted()) {
            synchronized(this) {
                while(current_player == player) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            computer.playNextMove();

            synchronized(this) {
                while(current_player == computer) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(is_end)
                break;
        }
    }

    public synchronized void startGame() {
        is_end = false;
        current_player = player;
        game_state = GameState.ACTIVE;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void continueGame() {
        notifyAll();
    }

    public synchronized void stopGame() {
        is_end = true;
        thread.interrupt();
        new ResultDialog(Chess.this);
    }

    public static void main(String[] args) {
        new Chess();
    }

}