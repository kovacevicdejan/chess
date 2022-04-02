package chess;

import javax.swing.*;
import java.awt.*;

public class UserPlayer extends Player {

	private Timer clock;

	public UserPlayer(Chess owner, char color) {
		super(owner, color);
		this.clock = new Timer(1000, (ae) -> {
			if(owner.getCurrentPlayer() == this && owner.getMoveNumber() >= 2) {
				time--;
				owner.getGameInfo().setPlayerClock(time);

				if(time <= 60 && owner.getGameInfo().getPlayerClockColor() == Color.ORANGE)
					owner.getGameInfo().setPlayerClockColor(Color.RED);
			}
		});
		this.clock.start();
	}

	@Override
	public void setFigures(char color) {
        Board board = owner.getBoard();
		
		if(color == 'B') {
			King black_king = new King(7, 4, 'B', this, board);
	        board.getField(7, 4).setFigure(black_king);
	        this.figures.add(black_king);
	        this.king = black_king;

	        Queen black_queen = new Queen(7, 3, 'B', this, board);
	        board.getField(7, 3).setFigure(black_queen);
	        this.figures.add(black_queen);

	        Rock black_rock_left = new Rock(7, 0, 'B', this, board);
	        board.getField(7, 0).setFigure(black_rock_left);
	        this.figures.add(black_rock_left);

	        Rock black_rock_right = new Rock(7, 7, 'B', this, board);
	        board.getField(7, 7).setFigure(black_rock_right);
	        this.figures.add(black_rock_right);

	        Knight black_knight_left = new Knight (7, 1, 'B', this, board);
	        board.getField(7, 1).setFigure(black_knight_left);
	        this.figures.add(black_knight_left);

	        Knight black_knight_right = new Knight(7, 6, 'B', this, board);
	        board.getField(7, 6).setFigure(black_knight_right);
	        this.figures.add(black_knight_right);

	        Bishop black_bishop_left = new Bishop (7, 2, 'B', this, board);
	        board.getField(7, 2).setFigure(black_bishop_left);
	        this.figures.add(black_bishop_left);

	        Bishop black_bishop_right = new Bishop(7, 5, 'B', this, board);
	        board.getField(7, 5).setFigure(black_bishop_right);
	        this.figures.add(black_bishop_right);

	        Pawn black_pawn;
	        for(int i = 0; i < 8; ++i) {
	            black_pawn = new Pawn(6, i, 'B', this, board);
	            board.getField(6, i).setFigure(black_pawn);
	            this.figures.add(black_pawn);
	        }
		}
		else {
			King white_king = new King(7, 4, 'W', this, board);
	        board.getField(7, 4).setFigure(white_king);
	        this.figures.add(white_king);
	        this.king = white_king;

	        Queen white_queen = new Queen(7, 3, 'W', this, board);
	        board.getField(7, 3).setFigure(white_queen);
	        this.figures.add(white_queen);

	        Rock white_rock_left = new Rock(7, 0, 'W', this, board);
	        board.getField(7, 0).setFigure(white_rock_left);
	        this.figures.add(white_rock_left);

	        Rock white_rock_right = new Rock(7, 7, 'W', this, board);
	        board.getField(7, 7).setFigure(white_rock_right);
	        this.figures.add(white_rock_right);

	        Knight white_knight_left = new Knight (7, 1, 'W', this, board);
	        board.getField(7, 1).setFigure(white_knight_left);
	        this.figures.add(white_knight_left);

	        Knight white_knight_right = new Knight(7, 6, 'W', this, board);
	        board.getField(7, 6).setFigure(white_knight_right);
	        this.figures.add(white_knight_right);

	        Bishop white_bishop_left = new Bishop (7, 2, 'W', this, board);
	        board.getField(7, 2).setFigure(white_bishop_left);
	        this.figures.add(white_bishop_left);

	        Bishop white_bishop_right = new Bishop(7, 5, 'W', this, board);
	        board.getField(7, 5).setFigure(white_bishop_right);
	        this.figures.add(white_bishop_right);

	        Pawn white_pawn;
	        for(int i = 0; i < 8; ++i) {
	            white_pawn = new Pawn(6, i, 'W', this, board);
	            board.getField(6, i).setFigure(white_pawn);
	            this.figures.add(white_pawn);
	        }
		}
	}

	public void makeMove(Move move) {
		move.getFigure().moveFigure(move.getNewField().getRow(), move.getNewField().getColumn());

		if(move.getMoveType() == Move.MoveType.CASTLING)
			move.getRockFigure().moveFigure(move.getNewRockField().getRow(), move.getNewRockField().getColumn());

		if(owner.getLastMove() != null)
			owner.getLastMove().setNextMove(move);

		move.setPreviousMove(owner.getLastMove());
		owner.setLastMove(move);
		owner.setMoveNumber(owner.getMoveNumber() + 1);

		String move_text = owner.getMoveNumber() + ". Player:   " + move.getFigure().toString() +
				" " + (char)('a' + move.getNewField().getColumn()) + (char)('8' - move.getNewField().getRow());
		owner.getGameInfo().addMove(move_text);
		move.setMoveText(move_text);
	}

}
