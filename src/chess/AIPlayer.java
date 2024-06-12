package chess;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AIPlayer extends Player {

	private static final int max_levels = 5;
	private Timer clock;

	public AIPlayer(Chess owner, char color) {
		super(owner, color);
		this.clock = new Timer(1000, (ae) -> {
			if(owner.getCurrentPlayer() == this && owner.getMoveNumber() >= 2) {
				time--;
				owner.getGameInfo().setComputerClock(time);

				if(time <= 60 && owner.getGameInfo().getComputerClockColor() == Color.ORANGE)
					owner.getGameInfo().setComputerClockColor(Color.RED);
			}
		});
		this.clock.start();
	}

	@Override
	public void setFigures(char color) {
		Board board = owner.getBoard();
		
		if(color == 'B') {
			King black_king = new King(0, 4, 'B', this, board);
	        board.getField(0, 4).setFigure(black_king);
	        this.figures.add(black_king);
	        this.king = black_king;

	        Queen black_queen = new Queen(0, 3, 'B', this, board);
	        board.getField(0, 3).setFigure(black_queen);
	        this.figures.add(black_queen);

	        Rock black_rock_left = new Rock(0, 0, 'B', this, board);
	        board.getField(0, 0).setFigure(black_rock_left);
	        this.figures.add(black_rock_left);

	        Rock black_rock_right = new Rock(0, 7, 'B', this, board);
	        board.getField(0, 7).setFigure(black_rock_right);
	        this.figures.add(black_rock_right);

	        Knight black_knight_left = new Knight (0, 1, 'B', this, board);
	        board.getField(0, 1).setFigure(black_knight_left);
	        this.figures.add(black_knight_left);

	        Knight black_knight_right = new Knight(0, 6, 'B', this, board);
	        board.getField(0, 6).setFigure(black_knight_right);
	        this.figures.add(black_knight_right);

	        Bishop black_bishop_left = new Bishop (0, 2, 'B', this, board);
	        board.getField(0, 2).setFigure(black_bishop_left);
	        this.figures.add(black_bishop_left);

	        Bishop black_bishop_right = new Bishop(0, 5, 'B', this, board);
	        board.getField(0, 5).setFigure(black_bishop_right);
	        this.figures.add(black_bishop_right);

	        Pawn black_pawn;
	        for(int i = 0; i < 8; ++i) {
	            black_pawn = new Pawn(1, i, 'B', this, board);
	            board.getField(1, i).setFigure(black_pawn);
	            this.figures.add(black_pawn);
	        }
		}
		else {
			King white_king = new King(0, 4, 'W', this, board);
	        board.getField(0, 4).setFigure(white_king);
	        this.figures.add(white_king);
	        this.king = white_king;

	        Queen white_queen = new Queen(0, 3, 'W', this, board);
	        board.getField(0, 3).setFigure(white_queen);
	        this.figures.add(white_queen);

	        Rock white_rock_left = new Rock(0, 0, 'W', this, board);
	        board.getField(0, 0).setFigure(white_rock_left);
	        this.figures.add(white_rock_left);

	        Rock white_rock_right = new Rock(0, 7, 'W', this, board);
	        board.getField(0, 7).setFigure(white_rock_right);
	        this.figures.add(white_rock_right);

	        Knight white_knight_left = new Knight (0, 1, 'W', this, board);
	        board.getField(0, 1).setFigure(white_knight_left);
	        this.figures.add(white_knight_left);

	        Knight white_knight_right = new Knight(0, 6, 'W', this, board);
	        board.getField(0, 6).setFigure(white_knight_right);
	        this.figures.add(white_knight_right);

	        Bishop white_bishop_left = new Bishop (0, 2, 'W', this, board);
	        board.getField(0, 2).setFigure(white_bishop_left);
	        this.figures.add(white_bishop_left);

	        Bishop white_bishop_right = new Bishop(0, 5, 'W', this, board);
	        board.getField(0, 5).setFigure(white_bishop_right);
	        this.figures.add(white_bishop_right);

	        Pawn white_pawn;
	        for(int i = 0; i < 8; ++i) {
	            white_pawn = new Pawn(1, i, 'W', this, board);
	            board.getField(1, i).setFigure(white_pawn);
	            this.figures.add(white_pawn);
	        }
		}
	}

	public Move getNextMove() {
		ArrayList<Move> moves = getPossibleMoves();
		Player player = getOtherPlayer();

		if(moves.isEmpty())
			return null;

		double max_score = -Double.MAX_VALUE;
		Move best_move = moves.get(0);
		double alpha = -Double.MAX_VALUE;
		double beta = Double.MAX_VALUE;

		for(Move move: moves) {
			move.simulateMove(owner.getBoard());
			double score = minimaxAB(player, 1, alpha, beta);

			if(score > max_score) {
				max_score = score;
				best_move = move;
			}

			if(score > alpha)
				alpha = score;

			if(alpha >= beta) {
				move.reverseMove(owner.getBoard());
				break;
			}

			move.reverseMove(owner.getBoard());
		}

		return best_move;
	}

	public double minimaxAB(Player curr_player, int curr_level, double alpha, double beta) {
		ArrayList<Move> moves = curr_player.getPossibleMoves();

		if(moves.isEmpty()) {
			if(curr_player == owner.getPlayer())
				return 10000;
			else
				return -10000;
		}

		if(curr_level == max_levels)
			return -owner.getBoard().evaluate();

		Player next_player = curr_player.getOtherPlayer();
		double max_score = -Double.MAX_VALUE;
		double min_score = Double.MAX_VALUE;

		for(Move move: moves) {
			move.simulateMove(owner.getBoard());
            double score = minimaxAB(next_player, curr_level + 1, alpha, beta);

			if (curr_player == owner.getComputer()) {
				if (score > max_score)
					max_score = score;

				if (score > alpha)
					alpha = score;
			} else {
				if (score < min_score)
					min_score = score;

				if (score < beta)
					beta = score;
			}

			if (alpha >= beta) {
				move.reverseMove(owner.getBoard());
				break;
		    }

			move.reverseMove(owner.getBoard());
		}

		if(curr_player == owner.getComputer())
			return max_score;
		else
			return min_score;
	}

	public void playNextMove() {
		Move move = getNextMove();

		if(move == null) {
			owner.setGameState(Chess.GameState.WIN);
			owner.stopGame();
			return;
		}

		try {
			String soundName = "src/sounds/move_sound.wav";
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		move.getFigure().moveFigure(move.getNewField().getRow(), move.getNewField().getColumn());

		if(move.getMoveType() == Move.MoveType.CASTLING)
			move.getRockFigure().moveFigure(move.getNewRockField().getRow(), move.getNewRockField().getColumn());

		if(owner.getLastMove() != null)
			owner.getLastMove().setNextMove(move);

		move.setPreviousMove(owner.getLastMove());
		owner.setLastMove(move);
		owner.setMoveNumber(owner.getMoveNumber() + 1);

		String move_text = owner.getMoveNumber() + ". Computer:   " + move.getFigure().toString() +
				" " + (char)('a' + move.getNewField().getColumn()) + (char)('8' - move.getNewField().getRow());
		owner.getGameInfo().addMove(move_text);
		move.setMoveText(move_text);
	}

}
