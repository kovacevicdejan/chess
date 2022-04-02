package chess;

import java.util.ArrayList;

public class Bishop extends Figure {

	private static final double[][] evaluation_function = {
		{-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
		{-1.0, -0.0, -0.0, -0.0, -0.0, -0.0, -0.0, -1.0},
		{-1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
		{-1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
		{-1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
		{-1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
		{-1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
		{-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
	};
    
    public Bishop(int x, int y, char color, Player owner, Board board) {
        super(x, y, color, owner, board);
        String image_location;
        
        if(color == 'B')
        	image_location = "/images/black_bishop.png";
        else
        	image_location = "/images/white_bishop.png";
        	
        setImage(image_location);
    }

	@Override
	public void moveFunction() throws InterruptedException {
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

	@Override
	public ArrayList<Field> possibleMoves() {
        ArrayList<Field> moves = new ArrayList<>();
		int i = 1;
		
		while(row - i >= 0 && column - i >= 0 && board.getField(row - i, column - i).empty()) {
			if(canMove(row - i, column - i))
				moves.add(board.getField(row - i, column - i));
			++i;
		}
		
		if(row - i >= 0 && column - i >= 0 && board.getField(row - i, column - i).getFigure().color != color) {
			if(canMove(row - i, column - i))
				moves.add(board.getField(row - i, column - i));
		}
		
		i = 1;
		while(row - i >= 0 && column + i < 8 && board.getField(row - i, column + i).empty()) {
			if(canMove(row - i, column + i))
			    moves.add(board.getField(row - i, column + i));
			++i;
		}
		
		if(row - i >= 0 && column + i < 8 && board.getField(row - i, column + i).getFigure().color != color) {
			if(canMove(row - i, column + i))
				moves.add(board.getField(row - i, column + i));
		}
		
		i = 1;
		while(row + i < 8 && column - i >= 0 && board.getField(row + i, column - i).empty()) {
			if(canMove(row + i, column - i))
				moves.add(board.getField(row + i, column - i));
			++i;
		}
		
		if(row + i < 8 && column - i >= 0 && board.getField(row + i, column - i).getFigure().color != color) {
			if(canMove(row + i, column - i))
				moves.add(board.getField(row + i, column - i));
		}
		
		i = 1;
		while(row + i < 8 && column + i < 8 && board.getField(row + i, column + i).empty()) {
			if(canMove(row + i, column + i))
				moves.add(board.getField(row + i, column + i));
			++i;
		}
		
		if(row + i < 8 && column + i < 8 && board.getField(row + i, column + i).getFigure().color != color) {
			if(canMove(row + i, column + i))
				moves.add(board.getField(row + i, column + i));
		}
		
		return moves;
	}

	@Override
	public boolean canAttackField(int x, int y) {
		if(Math.abs(row - x) != Math.abs(column - y))
			return false;

		int dx = (row > x) ? -1 : 1;
		int dy = (column > y) ? -1 : 1;
		int sx = row, sy = column;

		while(sx != x) {
			sx += dx;
			sy += dy;

			if(sx != x && !board.getField(sx, sy).empty())
				return false;
		}

		return true;
	}

	@Override
	protected String printFigure() {
		return "Bi";
	}

	public double evaluate() {
		if(owner == owner.owner.getPlayer())
			return 30 + evaluation_function[row][column];
		else
			return 30 + evaluation_function[7 - row][column];
	}

}
