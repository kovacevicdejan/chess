package chess;

import java.util.ArrayList;

public class Pawn extends Figure {

	private static final double[][] evaluation_function = {
			{0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
			{5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0},
			{1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
			{0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
			{0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
			{0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
			{0.5,  1.0,  1.0, -2.0, -2.0,  1.0,  1.0,  0.5},
			{0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
	};
	
	private int move_counter;
    
    public Pawn(int x, int y, char color, Player owner, Board board) {
        super(x, y, color, owner, board);
        move_counter = 0;
        String image_location;
        
        if(color == 'B')
        	image_location = "/images/black_pawn.png";
        else
        	image_location = "/images/white_pawn.png";
        	
        setImage(image_location);
    }

	public int getMoveCounter() {
		return move_counter;
	}

	public void setMoveCounter(int move_counter) {
		this.move_counter = move_counter;
	}

	@Override
	public void moveFunction() throws InterruptedException {
		if(new_x == x) {
			int dy = (y > new_y) ? -1 : 1;
			dy = dy * Math.abs(y - new_y);
			dy = dy / 10;
	        while(y != new_y) {
	            y += dy;
	            
	            if(y > new_y && dy > 0)
	            	y = new_y;
	            if(y < new_y && dy < 0)
	            	y = new_y;
	            	
	            board.repaint();
	            Thread.sleep(15);
	        }
		}
		else {
			int dy = (y > new_y) ? -1 : 1;
			dy = dy * Math.abs(y - new_y);
			dy = dy / 10;

			int dx = (x > new_x) ? -1 : 1;
			dx = dx * Math.abs(x - new_x);
			dx = dx / 10;
			
	        while(x != new_x) {
	            x += dx;
	            y += dy;
	            
	            if(x > new_x && dx > 0)
	            	x = new_x;
	            if(x < new_x && dx < 0)
	            	x = new_x;
	            
	            if(y > new_y && dy > 0)
	            	y = new_y;
	            if(y < new_y && dy < 0)
	            	y = new_y;
	            
	            board.repaint();
	            Thread.sleep(15);
	        }
		}
		++move_counter;
	}

	@Override
	public ArrayList<Field> possibleMoves() {
		ArrayList<Field> moves = new ArrayList<>();

		if(owner == owner.getOwner().getPlayer()) {
			if(row > 0 && board.getField(row - 1, column).empty() && canMove(row - 1, column))
				moves.add(board.getField(row - 1, column));
			
			if(row > 1 && move_counter == 0 && board.getField(row - 1, column).empty()
					&& board.getField(row - 2, column).empty() && canMove(row - 2, column))
				moves.add(board.getField(row - 2, column));
			
			if(row > 0 && column > 0 && !board.getField(row - 1, column - 1).empty()
					&& board.getField(row - 1, column - 1).getFigure().color != color
					&& canMove(row - 1, column - 1))
				moves.add(board.getField(row - 1, column - 1));
			
			if(row > 0 && column < 7 && !board.getField(row - 1, column + 1).empty()
					&& board.getField(row - 1, column + 1).getFigure().color != color
					&& canMove(row - 1, column + 1))
				moves.add(board.getField(row - 1, column + 1));
		}
		else {
			if(row < 7 && board.getField(row + 1, column).empty() && canMove(row + 1, column))
				moves.add(board.getField(row + 1, column));
			
			if(row < 6 && move_counter == 0 && board.getField(row + 1, column).empty()
					&& board.getField(row + 2, column).empty() && canMove(row + 2, column))
				moves.add(board.getField(row + 2, column));
			
			if(row < 7 && column > 0 && !board.getField(row + 1, column - 1).empty()
					&& board.getField(row + 1, column - 1).getFigure().color != color
					&& canMove(row + 1, column - 1))
				moves.add(board.getField(row + 1, column - 1));
			
			if(row < 7 && column < 7 && !board.getField(row + 1, column + 1).empty()
					&& board.getField(row + 1, column + 1).getFigure().color != color
					&& canMove(row + 1, column + 1))
				moves.add(board.getField(row + 1, column + 1));
		}
		
		return moves;
	}
	
	public ArrayList<Field> diagonalMoves() {
		ArrayList<Field> moves = new ArrayList<>();

		if(owner == owner.getOwner().getPlayer()) {
			if(row > 0 && column > 0 && board.getField(row - 1, column - 1).empty())
				moves.add(board.getField(row - 1, column - 1));
			
			if(row > 0 && column < 7 && board.getField(row - 1, column + 1).empty())
				moves.add(board.getField(row - 1, column + 1));
		}
		else {
			if(row < 7 && column > 0 && board.getField(row + 1, column - 1).empty())
				moves.add(board.getField(row + 1, column - 1));
			
			if(row < 7 && column < 7 && board.getField(row + 1, column + 1).empty())
				moves.add(board.getField(row + 1, column + 1));
		}
		
		return moves;
	}

	@Override
	public boolean canAttackField(int x, int y) {
		if(owner == owner.getOwner().getPlayer() && ((x == row - 1 && y == column - 1) || (x == row - 1 && y == column + 1)))
		    return true;
		else if (owner != owner.getOwner().getPlayer() && ((x == row + 1 && y == column - 1) || (x == row + 1 && y == column + 1)))
			return true;
		else
		    return false;
	}

	@Override
	protected String printFigure() {
		return "Pa";
	}

	public double evaluate() {
		if(owner == owner.owner.getPlayer())
			return 10 + evaluation_function[row][column];
		else
			return 10 + evaluation_function[7 - row][column];
	}

}
