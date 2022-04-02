package chess;

import java.util.ArrayList;

public class King extends Figure {

	private static final double[][] evaluation_function = {
		{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
		{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
		{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
		{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
		{-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
		{-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
		{ 2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0},
		{ 2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0},
	};

	private int move_counter;
    
    public King(int x, int y, char color, Player owner, Board board) {
        super(x, y, color, owner, board);
        move_counter = 0;
        String image_location;
        
        if(color == 'B')
        	image_location = "/images/black_king.png";
        else
        	image_location = "/images/white_king.png";
        	
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
		else if(new_y == y) {
			int dx = (x > new_x) ? -1 : 1;
			dx = dx * Math.abs(x - new_x);
			dx = dx / 10;
	        while(x != new_x) {
	            x += dx;
	            
	            if(x > new_x && dx > 0)
	            	x = new_x;
	            if(x < new_x && dx < 0)
	            	x = new_x;
	            	
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
        Figure old_figure;
        
        if(row > 0) {
        	if(board.getField(row - 1, column).empty() || (!board.getField(row - 1, column).empty() &&
					board.getField(row - 1, column).getFigure().owner != owner)) {
        		old_figure = board.getField(row - 1, column).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row - 1, column).setFigure(this);
        		--row;
        		
        		if(!isAttacked())
        			moves.add(board.getField(row, column));
        		
        		++row;
        		board.getField(row, column).setFigure(this);
        		board.getField(row - 1, column).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }
        
        if(row < 7) {
			if(board.getField(row + 1, column).empty() || (!board.getField(row + 1, column).empty() &&
					board.getField(row + 1, column).getFigure().owner != owner)) {
        		old_figure = board.getField(row + 1, column).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row + 1, column).setFigure(this);
        		++row;
        		
        		if(!isAttacked())
        			moves.add(board.getField(row, column));
        		
        		--row;
        		board.getField(row, column).setFigure(this);
        		board.getField(row + 1, column).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }
        
        if(column > 0) {
			if(board.getField(row, column - 1).empty() || (!board.getField(row, column - 1).empty() &&
					board.getField(row, column - 1).getFigure().owner != owner)) {
        		old_figure = board.getField(row, column - 1).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row, column - 1).setFigure(this);
        		--column;
        		
        		if(!isAttacked())
        			moves.add(board.getField(row, column));
        		
        		++column;
        		board.getField(row, column).setFigure(this);
        		board.getField(row, column - 1).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }
        
        if(column < 7) {
			if(board.getField(row, column + 1).empty() || (!board.getField(row, column + 1).empty() &&
					board.getField(row, column + 1).getFigure().owner != owner)) {
        		old_figure = board.getField(row, column + 1).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row, column + 1).setFigure(this);
        		++column;
        		
        		if(!isAttacked())
        			moves.add(board.getField(row, column));
        		
        		--column;
        		board.getField(row, column).setFigure(this);
        		board.getField(row, column + 1).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }
        
        if(row > 0 && column > 0) {
			if(board.getField(row - 1, column - 1).empty() || (!board.getField(row - 1, column - 1).empty() &&
					board.getField(row - 1, column - 1).getFigure().owner != owner)) {
        		old_figure = board.getField(row - 1, column - 1).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row - 1, column - 1).setFigure(this);
        		--row;
        		--column;
        		
        		if(!isAttacked())
        			moves.add(board.getField(row, column));
        		
        		++row;
        		++column;
        		board.getField(row, column).setFigure(this);
        		board.getField(row - 1, column - 1).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }
        
        if(row > 0 && column < 7) {
			if(board.getField(row - 1, column + 1).empty() || (!board.getField(row - 1, column + 1).empty() &&
					board.getField(row - 1, column + 1).getFigure().owner != owner)) {
        		old_figure = board.getField(row - 1, column + 1).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row - 1, column + 1).setFigure(this);
        		--row;
        		++column;
        		
        		if(!isAttacked())
        			moves.add(board.getField(row, column));
        		
        		++row;
        		--column;
        		board.getField(row, column).setFigure(this);
        		board.getField(row - 1, column + 1).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }
        
        if(row < 7 && column > 0) {
			if(board.getField(row + 1, column - 1).empty() || (!board.getField(row + 1, column - 1).empty() &&
					board.getField(row + 1, column - 1).getFigure().owner != owner)) {
        		old_figure = board.getField(row + 1, column - 1).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row + 1, column - 1).setFigure(this);
        		++row;
        		--column;
        		
        		if(!isAttacked())
        			moves.add(board.getField(row, column));
        		
        		--row;
        		++column;
        		board.getField(row, column).setFigure(this);
        		board.getField(row + 1, column - 1).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }
        
        if(row < 7 && column < 7) {
			if(board.getField(row + 1, column + 1).empty() || (!board.getField(row + 1, column + 1).empty() &&
					board.getField(row + 1, column + 1).getFigure().owner != owner)) {
        		old_figure = board.getField(row + 1, column  + 1).getFigure();
				int index = 0;

				if(old_figure != null)
					index = old_figure.owner.removeFigure(old_figure);

        		board.getField(row, column).setFigure(null);
        		board.getField(row + 1, column + 1).setFigure(this);
        		++row;
        		++column;
        		
        		if(!isAttacked())
					moves.add(board.getField(row, column));
        		
        		--row;
        		--column;
        		board.getField(row, column).setFigure(this);
        		board.getField(row + 1, column + 1).setFigure(old_figure);

				if(old_figure != null)
					old_figure.owner.addFigure(old_figure, index);
        	}
        }

		if(move_counter == 0 && board.getField(row, column + 1).empty() && board.getField(row, column + 2).empty() &&
				!board.getField(row, column + 3).empty() && board.getField(row, column + 3).getFigure() instanceof Rock &&
				((Rock)board.getField(row, column + 3).getFigure()).getMoveCounter() == 0) {
			Rock rock_figure = (Rock)board.getField(row, column + 3).getFigure();

			board.getField(row, column).setFigure(null);
			board.getField(row, column + 2).setFigure(this);
			board.getField(row, column + 3).setFigure(null);
			board.getField(row, column + 1).setFigure(rock_figure);

			column = column + 2;
			rock_figure.column = rock_figure.column - 2;

			if(!isAttacked())
			    moves.add(board.getField(row, column));

			column = column - 2;
			rock_figure.column = rock_figure.column + 2;

			board.getField(row, column).setFigure(this);
			board.getField(row, column + 2).setFigure(null);
			board.getField(row, column + 3).setFigure(rock_figure);
			board.getField(row, column + 1).setFigure(null);
		}

		if(move_counter == 0 && board.getField(row, column - 1).empty() && board.getField(row, column - 2).empty() &&
				board.getField(row, column - 3).empty() && !board.getField(row, column - 4).empty() &&
				board.getField(row, column - 4).getFigure() instanceof Rock && ((Rock)board.getField(row, column - 4).getFigure()).getMoveCounter() == 0) {
			Rock rock_figure = (Rock)board.getField(row, column - 4).getFigure();

			board.getField(row, column).setFigure(null);
			board.getField(row, column - 2).setFigure(this);
			board.getField(row, column - 4).setFigure(null);
			board.getField(row, column - 1).setFigure(rock_figure);

			column = column - 2;
			rock_figure.column = rock_figure.column + 3;

			if(!isAttacked())
				moves.add(board.getField(row, column));

			column = column + 2;
			rock_figure.column = rock_figure.column - 3;

			board.getField(row, column).setFigure(this);
			board.getField(row, column - 2).setFigure(null);
			board.getField(row, column - 4).setFigure(rock_figure);
			board.getField(row, column - 1).setFigure(null);
		}

		return moves;
	}
	
	public boolean isAttacked() {
		Player other_player = owner.getOtherPlayer();
		
		for(Figure f: other_player.getFigures()) {
			if(f.canAttackField(row, column)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canAttackField(int x, int y) {
		if((x == row || x == row - 1 || x == row + 1) && (y == column || y == column - 1 || y == column + 1))
			return true;
		else
			return false;
	}

	@Override
	protected String printFigure() {
		return "Ki";
	}

	public double evaluate() {
		if(owner == owner.owner.getPlayer())
		    return 900 + evaluation_function[row][column];
		else
			return 900 + evaluation_function[7 - row][column];
	}

}
