package chess;

public class Move {

    public enum MoveType{NORMAL, CASTLING}

    private Figure figure;
    private Figure eaten_figure;
    private Figure rock_figure;
    private Field old_field;
    private Field new_field;
    private Field old_rock_field;
    private Field new_rock_field;
    private int index = 0;
    private MoveType move_type = MoveType.NORMAL;
    private Move previous_move = null;
    private Move next_move = null;
    private String move_text;

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public void setEatenFigure(Figure eaten_figure) {
        this.eaten_figure = eaten_figure;
    }

    public void setOldField(Field prev_field) {
        this.old_field = prev_field;
    }

    public Field getNewField() {
        return new_field;
    }

    public void setNewField(Field new_field) {
        this.new_field = new_field;
    }

    public MoveType getMoveType() {
        return move_type;
    }

    public void setMoveType(MoveType move_type) {
        this.move_type = move_type;
    }

    public Figure getRockFigure() {
        return rock_figure;
    }

    public void setRockFigure(Figure rock_figure) {
        this.rock_figure = rock_figure;
    }

    public void setOldRockField(Field rock_old_field) {
        this.old_rock_field = rock_old_field;
    }

    public Field getNewRockField() {
        return new_rock_field;
    }

    public void setNewRockField(Field rock_new_field) {
        this.new_rock_field = rock_new_field;
    }

    public Move getPreviousMove() {
        return previous_move;
    }

    public void setPreviousMove(Move previous_move) {
        this.previous_move = previous_move;
    }

    public Move getNextMove() {
        return next_move;
    }

    public void setNextMove(Move next_move) {
        this.next_move = next_move;
    }

    public String getMoveText() {
        return move_text;
    }

    public void setMoveText(String move_text) {
        this.move_text = move_text;
    }

    public void simulateMove(Board board) {
        int new_row = new_field.getRow();
        int new_column = new_field.getColumn();
        int old_row = old_field.getRow();
        int old_column = old_field.getColumn();

        switch(move_type) {
            case NORMAL:
                if(eaten_figure != null)
                    index = eaten_figure.owner.removeFigure(eaten_figure);

                board.getField(old_row, old_column).setFigure(null);
                board.getField(new_row, new_column).setFigure(figure);
                figure.row = new_row;
                figure.column = new_column;

                if(figure instanceof King)
                    ((King)figure).setMoveCounter(((King)figure).getMoveCounter() + 1);

                if(figure instanceof Rock)
                    ((Rock)figure).setMoveCounter(((Rock)figure).getMoveCounter() + 1);

                if(figure instanceof Pawn)
                    ((Pawn)figure).setMoveCounter(((Pawn)figure).getMoveCounter() + 1);
                break;
            case CASTLING:
                int rock_new_row = new_rock_field.getRow();
                int rock_new_column = new_rock_field.getColumn();
                int rock_old_row = old_rock_field.getRow();
                int rock_old_column = old_rock_field.getColumn();

                board.getField(old_row, old_column).setFigure(null);
                board.getField(new_row, new_column).setFigure(figure);
                board.getField(rock_old_row, rock_old_column).setFigure(null);
                board.getField(rock_new_row, rock_new_column).setFigure(rock_figure);

                figure.row = new_row;
                figure.column = new_column;
                rock_figure.row = rock_new_row;
                rock_figure.column = rock_new_column;

                ((King)figure).setMoveCounter(((King)figure).getMoveCounter() + 1);
                ((Rock)rock_figure).setMoveCounter(((Rock)rock_figure).getMoveCounter() + 1);
                break;
        }
    }

    public void reverseMove(Board board) {
        int new_row = new_field.getRow();
        int new_column = new_field.getColumn();
        int old_row = old_field.getRow();
        int old_column = old_field.getColumn();

        switch(move_type) {
            case NORMAL:
                figure.row = old_row;
                figure.column = old_column;
                board.getField(old_row, old_column).setFigure(figure);
                board.getField(new_row, new_column).setFigure(eaten_figure);

                if(eaten_figure != null)
                    eaten_figure.owner.addFigure(eaten_figure, index);

                if(figure instanceof King)
                    ((King)figure).setMoveCounter(((King)figure).getMoveCounter() - 1);

                if(figure instanceof Rock)
                    ((Rock)figure).setMoveCounter(((Rock)figure).getMoveCounter() - 1);

                if(figure instanceof Pawn)
                    ((Pawn)figure).setMoveCounter(((Pawn)figure).getMoveCounter() - 1);
                break;
            case CASTLING:
                int rock_new_row = new_rock_field.getRow();
                int rock_new_column = new_rock_field.getColumn();
                int rock_old_row = old_rock_field.getRow();
                int rock_old_column = old_rock_field.getColumn();

                figure.row = old_row;
                figure.column = old_column;
                rock_figure.row = rock_old_row;
                rock_figure.column = rock_old_column;

                board.getField(old_row, old_column).setFigure(figure);
                board.getField(new_row, new_column).setFigure(null);
                board.getField(rock_old_row, rock_old_column).setFigure(rock_figure);
                board.getField(rock_new_row, rock_new_column).setFigure(null);

                ((King)figure).setMoveCounter(((King)figure).getMoveCounter() - 1);
                ((Rock)rock_figure).setMoveCounter(((Rock)rock_figure).getMoveCounter() - 1);
                break;
        }
    }

    public void undoMove(Board board) {
        reverseMove(board);
        figure.x = 50 * figure.column + (50 - figure.width) / 2;
        figure.y = 50 * figure.row + (50 - figure.height) / 2;

        if(move_type == MoveType.CASTLING) {
            rock_figure.x = 50 * rock_figure.column + (50 - rock_figure.width) / 2;
            rock_figure.y = 50 * rock_figure.row + (50 - rock_figure.height) / 2;
        }

        if(eaten_figure != null) {
            figure.getOwner().removeEatenFigure(eaten_figure);
        }
    }

    public void redoMove(Board board) {
        simulateMove(board);
        figure.x = 50 * figure.column + (50 - figure.width) / 2;
        figure.y = 50 * figure.row + (50 - figure.height) / 2;

        if(move_type == MoveType.CASTLING) {
            rock_figure.x = 50 * rock_figure.column + (50 - rock_figure.width) / 2;
            rock_figure.y = 50 * rock_figure.row + (50 - rock_figure.height) / 2;
        }

        if(eaten_figure != null) {
            figure.getOwner().addEatenFigure(eaten_figure);
        }
    }

}
