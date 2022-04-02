package chess;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class Figure implements Runnable {
    
    protected Image image;
    protected char color;
    protected Player owner;
    protected Board board;
    public int x;
    public int y;
    public int row;
    public int column;
    public int width;
    public int height;
    protected int new_x;
    protected int new_y;
    protected int new_row;
    protected int new_column;
    private Thread thread = null;

    public Figure(int row, int column, char color, Player owner, Board board) {
        this.width = 40;
        this.height = 40;
        this.row = row;
        this.column = column;
        this.owner = owner;
        this.board = board;
        this.x = 50 * column + (50 - width) / 2;
        this.y = 50 * row + (50 - height) / 2;
        this.color = color;
    }

    public void setImage(String image_location) {
    	URL imageURL = getClass().getResource(image_location);
    	ImageIcon image = new ImageIcon(imageURL);
        this.image = image.getImage();
    }

    @Override
    public void run() {
    	try {
			moveFunction();
		} catch (InterruptedException e) {}
    	
    	if(!board.getField(new_row, new_column).empty()) {
    		Figure figure = board.getField(new_row, new_column).getFigure();
        	figure.owner.removeFigure(figure);
            figure.owner.getOtherPlayer().addEatenFigure(figure);
        	board.repaint();

            if(owner == owner.getOwner().getPlayer())
                owner.getOwner().getGameInfo().players_eaten_figures.repaint();
            else
                owner.getOwner().getGameInfo().computers_eaten_figures.repaint();
        }
    	
    	board.getField(row, column).setFigure(null);
        row = new_row;
        column = new_column;
        board.getField(row, column).setFigure(this);
        board.movingFigure = null;
        board.repaint();
        thread = null;

        if(owner == owner.getOwner().getComputer() && owner.getOwner().getPlayer().getPossibleMoves().isEmpty()) {
            owner.getOwner().setGameState(Chess.GameState.DEFEAT);
            owner.getOwner().is_end = true;
            owner.getOwner().setCurrentPlayer(owner.getOtherPlayer());
            owner.getOwner().continueGame();
            owner.getOwner().stopGame();
        }
        else {
            owner.getOwner().setCurrentPlayer(owner.getOtherPlayer());
            owner.getOwner().continueGame();
        }
    }

    public void moveFigure(int row, int column) {
        new_x = (board.getWidth() / 8) * column + (board.getWidth() / 8 - width) / 2;
        new_y = (board.getHeight() / 8) * row + (board.getHeight() / 8 - height) / 2;
        new_row = row;
        new_column = column;
        board.movingFigure = this;
        thread = new Thread(this);
        thread.start();
    }
    
    public boolean canMove(int new_row, int new_column) {
    	Figure figureToEat = null;
    	if(board.getField(new_row, new_column).getFigure() != null)
    		figureToEat = board.getField(new_row, new_column).getFigure();
    	
    	int old_row = row;
    	int old_column = column;
    	row = new_row;
    	column = new_column;
        int index = 0;

    	board.getField(new_row, new_column).setFigure(this);
    	board.getField(old_row, old_column).setFigure(null);
    	
    	if(figureToEat != null) {
            index = figureToEat.owner.removeFigure(figureToEat);
        }

        if(this instanceof King)
            ((King)this).setMoveCounter(((King)this).getMoveCounter() + 1);

        if(this instanceof Rock)
            ((Rock)this).setMoveCounter(((Rock)this).getMoveCounter() + 1);

        if(this instanceof Pawn)
            ((Pawn)this).setMoveCounter(((Pawn)this).getMoveCounter() + 1);
    	
    	King king = owner.getKing();
    	boolean res;

    	if(king.isAttacked())
    		res = false;
    	else
    		res = true;
    	
    	row = old_row;
    	column = old_column;
    	board.getField(old_row, old_column).setFigure(this);
    	board.getField(new_row, new_column).setFigure(figureToEat);
    	
    	if(figureToEat != null)
    	    figureToEat.owner.addFigure(figureToEat, index);

        if(this instanceof King)
            ((King)this).setMoveCounter(((King)this).getMoveCounter() - 1);

        if(this instanceof Rock)
            ((Rock)this).setMoveCounter(((Rock)this).getMoveCounter() - 1);

        if(this instanceof Pawn)
            ((Pawn)this).setMoveCounter(((Pawn)this).getMoveCounter() - 1);
    	
    	return res;
    }

    public Field getField() {
        return board.getField(row, column);
    }
    
    public abstract boolean canAttackField(int x, int y);
    
    public abstract void moveFunction() throws InterruptedException;
    
    public abstract ArrayList<Field> possibleMoves();
    
    public Player getOwner() {
    	return owner;
    }

    protected abstract String printFigure();

    @Override
    public String toString() {
        return printFigure();
    }
}
