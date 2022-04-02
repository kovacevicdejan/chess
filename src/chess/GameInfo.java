package chess;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class GameInfo extends JPanel {

    static class PicturePanel extends JPanel {

        public PicturePanel() {
            setPreferredSize(new Dimension(100, 50));
            setBackground(Color.DARK_GRAY);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2D = (Graphics2D)g;
            String imgLocation = "/images/timer.png";
            URL imageURL = getClass().getResource(imgLocation);
            ImageIcon image = new ImageIcon(imageURL);
            g2D.drawImage(image.getImage(), 25, 0, 50, 50, null);
        }

    }

    static class EatenFiguresPanel extends JPanel {

        private Player player;

        public EatenFiguresPanel(Player player) {
            setPreferredSize(new Dimension(400, 100));
            setBackground(Color.DARK_GRAY);
            setBorder(BorderFactory.createEtchedBorder());
            this.player = player;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2D = (Graphics2D)g;
            int count = 0;
            int x = 0, y = 0;

            for(Figure figure: player.getEatenFigures()) {
                g2D.drawImage(figure.image, x + (50 - figure.width) / 2, y + (50 - figure.height) / 2, figure.width, figure.height, null);

                x += 50;
                count++;

                if(count == 8) {
                    count = 0;
                    x = 0;
                    y = 50;
                }
            }
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

    }

    public EatenFiguresPanel players_eaten_figures;
    public EatenFiguresPanel computers_eaten_figures;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JScrollPane scroll_pane;
    private final JLabel player_clock;
    private final JLabel computer_clock;
    private final JPanel player_clock_panel;
    private final JPanel computer_clock_panel;

    public GameInfo(Chess owner) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.DARK_GRAY);

        players_eaten_figures = new EatenFiguresPanel(owner.getPlayer());
        this.add(players_eaten_figures, BorderLayout.SOUTH);

        computers_eaten_figures = new EatenFiguresPanel(owner.getComputer());
        this.add(computers_eaten_figures, BorderLayout.NORTH);

        JPanel info_panel = new JPanel(new BorderLayout());
        info_panel.setPreferredSize(new Dimension(400, 200));
        info_panel.setBackground(Color.DARK_GRAY);

        JPanel player_panel = new JPanel(new BorderLayout());
        player_panel.setPreferredSize(new Dimension(400, 50));
        player_panel.setBackground(Color.GRAY);

        player_clock_panel = new JPanel();
        player_clock_panel.setPreferredSize(new Dimension(200, 50));
        player_clock_panel.setBackground(Color.ORANGE);
        player_clock_panel.setBorder(BorderFactory.createEtchedBorder());

        player_clock = new JLabel();
        player_clock.setForeground(Color.BLACK);
        player_clock.setFont(new Font("Arial", Font.PLAIN, 32));
        player_clock.setText("15 : 00");

        player_clock_panel.add(player_clock);
        player_panel.add(player_clock_panel, BorderLayout.CENTER);

        JButton previous_move_button = new JButton();
        previous_move_button.setPreferredSize(new Dimension(100, 50));
        previous_move_button.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/left_arrow.png"))));
        previous_move_button.setFocusable(false);
        previous_move_button.addActionListener((ae) -> {
            if(owner.getLastMove().getPreviousMove() != null) {
                owner.getLastMove().undoMove(owner.getBoard());
                owner.getLastMove().getPreviousMove().undoMove(owner.getBoard());
                owner.setLastMove(owner.getLastMove().getPreviousMove().getPreviousMove());
                owner.setMoveNumber(owner.getMoveNumber() - 2);
                deleteMove();
                deleteMove();
                owner.getBoard().repaint();
                repaint();
            }
        });
        player_panel.add(previous_move_button, BorderLayout.WEST);

        JButton next_move_button = new JButton();
        next_move_button.setPreferredSize(new Dimension(100, 50));
        next_move_button.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/right_arrow.png"))));
        next_move_button.setFocusable(false);
        next_move_button.addActionListener((ae) -> {
            if(owner.getLastMove().getNextMove() != null ) {
                owner.getLastMove().getNextMove().redoMove(owner.getBoard());
                owner.getLastMove().getNextMove().getNextMove().redoMove(owner.getBoard());
                addMove(owner.getLastMove().getNextMove().getMoveText());
                addMove(owner.getLastMove().getNextMove().getNextMove().getMoveText());
                owner.setLastMove(owner.getLastMove().getNextMove().getNextMove());
                owner.setMoveNumber(owner.getMoveNumber() + 2);
                owner.getBoard().repaint();
                repaint();
            }
        });
        player_panel.add(next_move_button, BorderLayout.EAST);
        info_panel.add(player_panel, BorderLayout.SOUTH);

        JPanel computer_panel = new JPanel(new BorderLayout());
        computer_panel.setPreferredSize(new Dimension(400, 50));
        computer_panel.setBackground(Color.DARK_GRAY);

        computer_clock_panel = new JPanel();
        computer_clock_panel.setPreferredSize(new Dimension(200, 50));
        computer_clock_panel.setBackground(Color.GRAY);
        computer_clock_panel.setBorder(BorderFactory.createEtchedBorder());

        computer_clock = new JLabel();
        computer_clock.setForeground(Color.BLACK);
        computer_clock.setFont(new Font("Arial", Font.PLAIN, 32));
        computer_clock.setText("15 : 00");

        computer_clock_panel.add(computer_clock);
        computer_panel.add(computer_clock_panel, BorderLayout.CENTER);

        JPanel left_panel = new PicturePanel();
        computer_panel.add(left_panel, BorderLayout.WEST);

        JPanel right_panel = new PicturePanel();
        computer_panel.add(right_panel, BorderLayout.EAST);
        info_panel.add(computer_panel, BorderLayout.NORTH);

        JPanel played_moves_panel = new JPanel(new BorderLayout());
        played_moves_panel.setPreferredSize(new Dimension(400, 100));
        played_moves_panel.setBackground(Color.DARK_GRAY);
        info_panel.add(played_moves_panel, BorderLayout.CENTER);

        JList<String> played_moves = new JList<>(model);
        played_moves.setBackground(Color.DARK_GRAY);
        played_moves.setForeground(Color.BLACK);
        played_moves.setFont(new Font("Arial", Font.PLAIN, 18));

        scroll_pane = new JScrollPane(played_moves);
        scroll_pane.setBackground(Color.DARK_GRAY);
        scroll_pane.setPreferredSize(new Dimension(400, 100));
        scroll_pane.setBorder(BorderFactory.createEtchedBorder());
        played_moves_panel.add(scroll_pane);

        this.add(info_panel, BorderLayout.CENTER);
    }

    public void clear(Player new_player, Player new_computer) {
        players_eaten_figures.setPlayer(new_player);
        computers_eaten_figures.setPlayer(new_computer);
        repaint();
    }

    public void addMove(String move) {
        model.addElement(move);
        boolean scrollDown = textAreaBottomIsVisible();

        if (scrollDown) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                JScrollBar bar = scroll_pane.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            });
        }
    }

    public void deleteMove() {
        model.removeElementAt(model.getSize() - 1);
        boolean scrollDown = textAreaBottomIsVisible();

        if (scrollDown) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                JScrollBar bar = scroll_pane.getVerticalScrollBar();
                bar.setValue(bar.getMaximum());
            });
        }
    }

    private boolean textAreaBottomIsVisible() {
        Adjustable sb = scroll_pane.getVerticalScrollBar();
        int val = sb.getValue();
        int lowest = val + sb.getVisibleAmount();
        int maxVal = sb.getMaximum();
        return maxVal == lowest;
    }

    public void setPlayerClock(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String text = String.format("%02d : %02d", minutes, seconds);
        player_clock.setText(text);
    }

    public void setComputerClock(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String text = String.format("%02d : %02d", minutes, seconds);
        computer_clock.setText(text);
    }

    public Color getPlayerClockColor() {
        return player_clock_panel.getBackground();
    }

    public void setPlayerClockColor(Color color) {
        player_clock_panel.setBackground(color);
    }

    public Color getComputerClockColor() {
        return computer_clock_panel.getBackground();
    }

    public void setComputerClockColor(Color color) {
        computer_clock_panel.setBackground(color);
    }

    public void clearMoves() {
        model.removeAllElements();
    }

}
