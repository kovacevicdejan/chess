package chess;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Board extends JPanel {
	
	private Chess owner;
    private Field[][] fields;
    public Figure movingFigure = null;
    private ArrayList<Field> marked_fields;
    private Field clicked_field;

    public Board(Chess owner) {
    	this.owner = owner;
        this.fields = new Field[8][8];
        this.setLayout(new GridLayout(8, 8, 0, 0));
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.BLACK);
        this.marked_fields = new ArrayList<Field>();
        this.clicked_field = null;
        
        for(int row = 0; row < 8; ++row) {
            for(int column = 0; column < 8; ++column) {
                Field field = new Field(row, column, this);
                if((row % 2 == 0 && column % 2 == 0) || (row % 2 == 1 && column % 2 == 1))
                    field.setColor(new Color(255, 222, 173));
                else
                    field.setColor(new Color(115, 58, 0)); //(98, 52, 18), (139, 69, 19)
                this.add(field);
                this.fields[row][column] = field;
            }
        }

    }

    public void paint(Graphics g) {
        super.paint(g);
        
        for(Figure f: owner.getPlayer().getFigures()) {
        	if(f != movingFigure) {
	            Graphics2D g2D = (Graphics2D)g;
	            g2D.drawImage(f.image, f.x, f.y, f.width, f.height, null);
        	}
        }
        
        for(Figure f: owner.getComputer().getFigures()) {
        	if(f != movingFigure) {
	            Graphics2D g2D = (Graphics2D)g;
	            g2D.drawImage(f.image, f.x, f.y, f.width, f.height, null);
        	}
        }
        
        if(movingFigure != null) {
        	Graphics2D g2D = (Graphics2D)g;
            g2D.drawImage(movingFigure.image, movingFigure.x, movingFigure.y,
            		movingFigure.width, movingFigure.height, null);
        }
    }
    
    public Field getField(int row, int column) {
    	return fields[row][column];
    }

    public void clear() {
        for(int row = 0; row < 8; ++row) {
            for (int column = 0; column < 8; ++column) {
                fields[row][column].setFigure(null);
            }
        }
    }

    public double evaluate() {
        double score = 0;
        ArrayList<Figure> players_figures = owner.getPlayer().getFigures();
        ArrayList<Figure> computers_figures = owner.getComputer().getFigures();

        for(Figure figure: players_figures) {
            if(figure instanceof King)
                score += ((King)figure).evaluate();
            else if(figure instanceof Queen)
                score += ((Queen)figure).evaluate();
            else if(figure instanceof Rock)
                score += ((Rock)figure).evaluate();
            else if(figure instanceof Bishop)
                score += ((Bishop)figure).evaluate();
            else if(figure instanceof Knight)
                score += ((Knight)figure).evaluate();
            else if(figure instanceof Pawn)
                score += ((Pawn)figure).evaluate();
        }

        for(Figure figure: computers_figures) {
            if(figure instanceof King)
                score -= ((King)figure).evaluate();
            else if(figure instanceof Queen)
                score -= ((Queen)figure).evaluate();
            else if(figure instanceof Rock)
                score -= ((Rock)figure).evaluate();
            else if(figure instanceof Bishop)
                score -= ((Bishop)figure).evaluate();
            else if(figure instanceof Knight)
                score -= ((Knight)figure).evaluate();
            else if(figure instanceof Pawn)
                score -= ((Pawn)figure).evaluate();
        }

        return score;
    }
    
    public ArrayList<Field> getMarkedFields() {
    	return marked_fields;
    }
    
    public void addMarkedField(Field field) {
    	this.marked_fields.add(field);
    }
    
    public void setClickedField(Field field) {
    	this.clicked_field = field;
    }
    
    public Field getClickedField() {
    	return clicked_field;
    }
    
    public Chess getOwner() {
    	return owner;
    }

    @Override
    public String toString() {
        String str = "";

        for(int i = 0; i < 8; ++i) {
            for(int j = 0; j < 8; ++j) {
                if(fields[i][j].getFigure() != null)
                    str += fields[i][j].getFigure().toString();
                else
                    str += "   ";

                if(j != 7)
                    str += " ";
            }

            str += "\n";
        }

        str += "\n\n";

        return str;
    }
}
