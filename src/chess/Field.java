package chess;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Field extends JPanel {

    private Figure figure;
    private Board owner;
    private int row;
    private int column;
    private boolean marked;
    private boolean clicked;

    public Field(int row, int column, Board owner) {
        super();
        this.figure = null;
        this.row = row;
        this.column = column;
        this.owner = owner;
        this.marked = false;
        this.clicked = false;
        Field this_field = this;
        
        addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(marked) {
					ArrayList<Field> marked_fields = owner.getMarkedFields();
					for(Field f: marked_fields) {
						f.marked = false;
					}
					marked_fields.clear();
					
					Field clicked_field = owner.getClickedField();
					if(clicked_field != null) {
					    clicked_field.clicked = false;
					    owner.setClickedField(this_field);
					}
					owner.repaint();

					Move move = new Move();
					move.setFigure(clicked_field.figure);
					move.setEatenFigure(this_field.figure);
					move.setOldField(clicked_field);
					move.setNewField(this_field);

					if(clicked_field.figure instanceof King &&
							Math.abs(clicked_field.figure.column - this_field.getColumn()) == 2) {
						Rock rock_figure;

						if(clicked_field.figure.column == this_field.getColumn() - 2)
							rock_figure = (Rock)owner.getField(clicked_field.figure.row, clicked_field.figure.column + 3).getFigure();
						else
							rock_figure = (Rock)owner.getField(clicked_field.figure.row, clicked_field.figure.column - 4).getFigure();

						move.setMoveType(Move.MoveType.CASTLING);
						move.setRockFigure(rock_figure);

						if(clicked_field.figure.column == this_field.getColumn() - 2) {
							move.setOldRockField(owner.getField(clicked_field.figure.row, clicked_field.figure.column + 3));
							move.setNewRockField(owner.getField(clicked_field.figure.row, clicked_field.figure.column + 1));
						}
						else {
							move.setOldRockField(owner.getField(clicked_field.figure.row, clicked_field.figure.column - 4));
							move.setNewRockField(owner.getField(clicked_field.figure.row, clicked_field.figure.column - 1));
						}
					}

					((UserPlayer)clicked_field.figure.getOwner()).makeMove(move);
				}
				else if(figure != null && figure.owner == owner.getOwner().getPlayer() &&
				        owner.getOwner().getCurrentPlayer() == owner.getOwner().getCurrentPlayer()) {
					ArrayList<Field> moves = figure.possibleMoves();
					ArrayList<Field> marked_fields = owner.getMarkedFields();
					
					for(Field f: marked_fields) {
						f.marked = false;
					}
					marked_fields.clear();
					
					if(!clicked) {
						for(Field f: moves) {
							f.marked = true;
							owner.addMarkedField(f);
						}
						
						Field clicked_field = owner.getClickedField();
						if(clicked_field != null)
						    clicked_field.clicked = false;
						
						clicked = true;
						owner.setClickedField(this_field);
					}
					else {
						clicked = false;
						owner.setClickedField(null);
					}
					
					owner.repaint();
				}
				else if (owner.getOwner().getCurrentPlayer() == owner.getOwner().getCurrentPlayer()) {
					ArrayList<Field> marked_fields = owner.getMarkedFields();
					for(Field f: marked_fields) {
						f.marked = false;
					}
					marked_fields.clear();
					
					Field clicked_field = owner.getClickedField();
					if(clicked_field != null) {
					    clicked_field.clicked = false;
					    owner.setClickedField(this_field);
					}
					
					owner.repaint();
				}
			}
			
		});
    }

    public void setColor(Color color) {
        this.setBackground(color);
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
    
    public boolean empty() {
    	return figure == null;
    }
    
    public Figure getFigure() {
    	return figure;
    }

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(marked) {
			if(figure == null) {
				g.setColor(new Color(0, 200, 0));
				g.fillOval(17, 17, 16, 16);
			}
			else {
				Color old_color = getBackground();
				if(old_color.equals(new Color(255, 222, 173)))
				    g.setColor(new Color(50, 205, 50));
				else
					g.setColor(new Color(0, 100, 0));
				g.fillRect(0, 0, 50, 50);
				g.setColor(old_color);
				g.fillOval(-3, -3, 56, 56);
			}
		}
		else {
			if(figure != null && figure.getOwner() == owner.getOwner().getPlayer() && 
					figure instanceof King && ((King)figure).isAttacked()) {
				g.setColor(Color.RED);
				g.fillRect(0, 0, 50, 50);
			}
		}
		if(this.clicked) {
			g.setColor(new Color(34, 139, 34));
			g.fillRect(0, 0, 50, 50);
		}
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

}
