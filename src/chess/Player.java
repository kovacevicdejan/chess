package chess;

import javax.swing.*;
import java.util.ArrayList;

public abstract class Player {
    
	protected ArrayList<Figure> figures;
	protected Chess owner;
	protected King king = null;
	protected ArrayList<Figure> eaten_figures;
	protected int[] eaten_figures_num = {0, 0, 0, 0, 0};
	protected int time = 900;

	public Player(Chess owner, char color) {
		this.owner = owner;
		this.figures = new ArrayList<>();
		this.setFigures(color);
		this.eaten_figures = new ArrayList<>();
	}

	public ArrayList<Figure> getFigures() {
		return figures;
	}
	
	public int removeFigure(Figure figure) {
		int index = figures.indexOf(figure);
		figures.remove(figure);
		return index;
	}
	
	public void addFigure(Figure figure, int index) {
		figures.add(index, figure);
	}
	
	public Chess getOwner() {
		return owner;
	}
	
	public King getKing() {
		return king;
	}
	
	public abstract void setFigures(char color);
	
	public Player getOtherPlayer() {
		if(owner.getComputer() == this)
			return owner.getPlayer();
		else
			return owner.getComputer();
	}

	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> moves = new ArrayList<>();
		ArrayList<Field> fields;
		Move move;

		for (Figure f : figures) {
			fields = f.possibleMoves();

			for (Field field : fields) {
				move = new Move();
				move.setFigure(f);
				move.setEatenFigure(field.getFigure());
				move.setOldField(f.getField());
				move.setNewField(field);

				if(f instanceof King && Math.abs(f.column - field.getColumn()) == 2) {
					Rock rock_figure;

					if(f.column == field.getColumn() - 2)
						rock_figure = (Rock)owner.getBoard().getField(f.row, f.column + 3).getFigure();
					else
						rock_figure = (Rock)owner.getBoard().getField(f.row, f.column - 4).getFigure();

					move.setMoveType(Move.MoveType.CASTLING);
					move.setRockFigure(rock_figure);

					if(f.column == field.getColumn() - 2) {
						move.setOldRockField(owner.getBoard().getField(f.row, f.column + 3));
						move.setNewRockField(owner.getBoard().getField(f.row, f.column + 1));
					}
					else {
						move.setOldRockField(owner.getBoard().getField(f.row, f.column - 4));
						move.setNewRockField(owner.getBoard().getField(f.row, f.column - 1));
					}
				}

				moves.add(move);
			}
		}

		return moves;
	}

	public ArrayList<Figure> getEatenFigures() {
		return eaten_figures;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void addEatenFigure(Figure figure) {
		if(figure instanceof Queen) {
			eaten_figures.add(eaten_figures_num[0], figure);

			for(int i = 0; i < 5; ++i) {
				eaten_figures_num[i]++;
			}
		}
		else if(figure instanceof Rock) {
			eaten_figures.add(eaten_figures_num[1], figure);

			for(int i = 1; i < 5; ++i) {
				eaten_figures_num[i]++;
			}
		}
		else if(figure instanceof Bishop) {
			eaten_figures.add(eaten_figures_num[2], figure);

			for(int i = 2; i < 5; ++i) {
				eaten_figures_num[i]++;
			}
		}
		else if(figure instanceof Knight) {
			eaten_figures.add(eaten_figures_num[3], figure);

			for(int i = 3; i < 5; ++i) {
				eaten_figures_num[i]++;
			}
		}
		else if(figure instanceof Pawn) {
			eaten_figures.add(eaten_figures_num[4], figure);

			for(int i = 4; i < 5; ++i) {
				eaten_figures_num[i]++;
			}
		}
	}

	public void removeEatenFigure(Figure figure) {
		if(figure instanceof Queen) {
			for(int i = 0; i < 5; ++i) {
				eaten_figures_num[i]--;
			}

			eaten_figures.remove(figure);
		}
		else if(figure instanceof Rock) {
			for(int i = 1; i < 5; ++i) {
				eaten_figures_num[i]--;
			}

			eaten_figures.remove(figure);
		}
		else if(figure instanceof Bishop) {
			for(int i = 2; i < 5; ++i) {
				eaten_figures_num[i]--;
			}

			eaten_figures.remove(figure);
		}
		else if(figure instanceof Knight) {
			for(int i = 3; i < 5; ++i) {
				eaten_figures_num[i]--;
			}

			eaten_figures.remove(figure);
		}
		else if(figure instanceof Pawn) {
			for(int i = 4; i < 5; ++i) {
				eaten_figures_num[i]--;
			}

			eaten_figures.remove(figure);
		}
	}
	
}
