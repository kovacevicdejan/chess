package chess;

import java.util.ArrayList;

public class Knight extends Figure {

	private static final double[][] evaluation_function = {
		{-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
		{-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
		{-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
		{-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
		{-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
		{-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
		{-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
		{-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
	};
    
    public Knight(int x, int y, char color, Player owner, Board board) {
        super(x, y, color, owner, board);
        String image_location;
        
        if(color == 'B')
        	image_location = "/images/black_knight.png";
        else
        	image_location = "/images/white_knight.png";
        	
        setImage(image_location);
    }

	@Override
	public void moveFunction() throws InterruptedException {
		int dy = (y > new_y) ? -1 : 1;
		int dx = (x > new_x) ? -1 : 1;
		
		if(Math.abs(y - new_y) < Math.abs(x - new_x))
			dx = dx * 2;
		else
			dy = dy * 2;
		
        while(y != new_y) {
        	x += dx;
            y += dy;
            board.repaint();
            Thread.sleep(2);
        }
	}

	@Override
	public ArrayList<Field> possibleMoves() {
		ArrayList<Field> moves = new ArrayList<>();
		
		if(row > 0 && column > 1 && (board.getField(row - 1, column - 2).empty() || (!board.getField(row - 1, column - 2).empty()
				&& board.getField(row - 1, column - 2).getFigure().color != color)) && canMove(row - 1, column - 2))
			moves.add(board.getField(row - 1, column - 2));
		
		if(row > 1 && column > 0 && (board.getField(row - 2, column - 1).empty() || (!board.getField(row - 2, column - 1).empty()
				&& board.getField(row - 2, column - 1).getFigure().color != color)) && canMove(row - 2, column - 1))
			moves.add(board.getField(row - 2, column - 1));
		
		if(row > 0 && column < 6 && (board.getField(row - 1, column + 2).empty() || (!board.getField(row - 1, column + 2).empty()
				&& board.getField(row - 1, column + 2).getFigure().color != color)) && canMove(row - 1, column + 2))
			moves.add(board.getField(row - 1, column + 2));
		
		if(row > 1 && column < 7 && (board.getField(row - 2, column + 1).empty() || (!board.getField(row - 2, column + 1).empty()
				&& board.getField(row - 2, column + 1).getFigure().color != color)) && canMove(row - 2, column + 1))
			moves.add(board.getField(row - 2, column + 1));
		
		if(row < 7 && column > 1 && (board.getField(row + 1, column - 2).empty() || (!board.getField(row + 1, column - 2).empty()
				&& board.getField(row + 1, column - 2).getFigure().color != color)) && canMove(row + 1, column - 2))
			moves.add(board.getField(row + 1, column - 2));
		
		if(row < 6 && column > 0 && (board.getField(row + 2, column - 1).empty() || (!board.getField(row + 2, column - 1).empty()
				&& board.getField(row + 2, column - 1).getFigure().color != color)) && canMove(row + 2, column - 1))
			moves.add(board.getField(row + 2, column - 1));
		
		if(row < 7 && column < 6 && (board.getField(row + 1, column + 2).empty() || (!board.getField(row + 1, column + 2).empty()
				&& board.getField(row + 1, column + 2).getFigure().color != color)) && canMove(row + 1, column + 2))
			moves.add(board.getField(row + 1, column + 2));
		
		if(row < 6 && column < 7 && (board.getField(row + 2, column + 1).empty() || (!board.getField(row + 2, column + 1).empty()
				&& board.getField(row + 2, column + 1).getFigure().color != color)) && canMove(row + 2, column + 1))
			moves.add(board.getField(row + 2, column + 1));
		
		return moves;
	}

	@Override
	public boolean canAttackField(int x, int y) {
		if((x == row - 1 && y == column - 2) || (x == row - 2 && y == column - 1) || (x == row - 1 && y == column + 2) ||
				(x == row - 2 && y == column + 1) || (x == row + 1 && y == column - 2) || (x == row + 2 && y == column - 1) ||
				(x == row + 1 && y == column + 2) || (x == row + 2 && y == column + 1))
			return true;
		else
			return false;
	}

	@Override
	protected String printFigure() {
		return "Kn";
	}

	public double evaluate() {
		if(owner == owner.owner.getPlayer())
			return 30 + evaluation_function[row][column];
		else
			return 30 + evaluation_function[7 - row][column];
	}

}