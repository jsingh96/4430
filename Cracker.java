//Jaskaran Talwar
//Assignment 8

import java.util.ArrayList;
public class Board {
    boolean[][] b;
    int pegsLeft;
    int size;

    public static int boolToInt(Boolean b) {
        return b ? 1 : 0;
    }

    public void printState(boolean[][] board) {
        for(int i = 0; i < size; i++) {
            System.out.print("  ");
            for(int k = 0; k < (size - i - 1); k++) {
                System.out.print(" ");
            }
            for(int j = 0; j <= i; j++) {
                System.out.print(Board.boolToInt(board[i][j]));
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static class Space {
        int row;
        int column;

        @Override
        public String toString() {
            return "(" + row + "," + column + ")";
        }
        public Space(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public boolean isValid(int board_size) {
            return (row >= 0) && (row < board_size) && (column >= 0) && (column <= row);
        }
        public void set(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public class Move {
        Board.Space from;
        Board.Space to;

        @Override
        public String toString() {
            return from.toString() + "==>" + to.toString();
        }

        public Move(Board.Space from, Board.Space to) {
            this.from = from;
            this.to = to;
        }

        public boolean isValid(Board board) {

            // Check if the coordinates are valid on the board
            if (!from.isValid(board.size) || !to.isValid(board.size)) {
                return false;
            }

            // Confirm the 'from' space is occupied
            if (!board.b[from.row][from.column]) {
                return false;
            }

            // Confirm the 'to' space is empty
            if (board.b[to.row][to.column]) {
                return false;
            }

            int rowJump = Math.abs(from.row - to.row);
            int colJump = Math.abs(from.column - to.column);

            if (rowJump == 0) {
                if (colJump != 2) {
                    return false;
                }
            }
            else if (rowJump == 2) {
                if (colJump != 0 && colJump != 2) {
                    return false;
                }
            }
            else {
                return false;
            }

            // Confirm the 'step' space is occupied
            return board.b[(from.row + to.row) / 2][(from.column + to.column) / 2];
        }
    }

    public void setup(int dim, Board.Space hole) {
        size = dim;
        b = new boolean[dim][dim];
        pegsLeft = -1;

        // Populate the initial board
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j <= i; j++) {
                b[i][j] = true;
                pegsLeft++;
            }
        }

        b[hole.row][hole.column] = false;
    }

    public boolean move(Move move) {
        if (!move.isValid(this)) {
            System.out.println("Invalid move.");
            return false;
        }

        // Set the 'from' space to empty
        b[move.from.row][move.from.column] = false;

        // Set the 'to' space to occupied
        b[move.to.row][move.to.column] = true;

        // Set the 'step' space to empty
        b[(move.from.row + move.to.row) / 2][(move.from.column + move.to.column) / 2] = false;

        // Lower the amount of pegs left
        pegsLeft--;

        return true;
    }

    public void undoMove(Move move) {

        // Set 'from' space to occupied
        b[move.from.row][move.from.column] = true;

        // Set 'to' space to free
        b[move.to.row][move.to.column] = false;

        // Set 'step' space to occupied
        b[(move.from.row + move.to.row)/2][(move.from.column + move.to.column)/2] = true;

        // Increment number of pegs left on the board
        pegsLeft++;
    }

    public ArrayList<Move> validMovesFromSpace(Space from) {
        ArrayList<Move> moves = new ArrayList<Move>();

        // Check if move is valid on the board
        if (!from.isValid(size)) {
            return moves;
        }

        // Confirm space is not empty
        if (!b[from.row][from.column]) {
            return moves;
        }

        // Check if we can move to adjacent spaces
        Move move = new Move(from, new Space(from.row - 2, from.column));

        if (move.isValid(this)) {
            moves.add(move);
        }

        move = new Move(from, new Space(from.row - 2, from.column - 2));

        if (move.isValid(this)) {
            moves.add(move);
        }

        move = new Move(from, new Space(from.row, from.column - 2));

        if (move.isValid(this)) {
            moves.add(move);
        }

        move = new Move(from, new Space(from.row, from.column + 2));

        if (move.isValid(this)) {
            moves.add(move);
        }

        move = new Move(from, new Space(from.row + 2, from.column));

        if (move.isValid(this)) {
            moves.add(move);
        }

        move = new Move(from, new Space(from.row + 2, from.column + 2));

        if (move.isValid(this)) {
            moves.add(move);
        }

        return moves;
    }

    public ArrayList<Move> validMoves() {
        Space space;
        ArrayList<Move> spaceMoves;
        ArrayList<Move> moves = new ArrayList<Move>();

        for(int i = 0; i < size; i++) {
            for (int j = 0; j <= i; j++) {
                space = new Space(i,j);
                spaceMoves = this.validMovesFromSpace(space);

                moves.addAll(spaceMoves);
            }
        }

        return moves;
    }

    public ArrayList<Move> bestSequence() {
        ArrayList<Move> sequence = new ArrayList<Move>();
        ArrayList<Move> moves = this.validMoves();

        if (moves.isEmpty()) {
            return sequence;
        }

        for (int i = 0; i < moves.size(); i++) {
            this.move(moves.get(i));

            // Win condition
            if (pegsLeft == 1) {
                sequence.add(moves.get(i));
                this.undoMove(moves.get(i));

                return sequence;
            }

            // Recurse
            ArrayList<Move> moveSequence = this.bestSequence();

            if (moveSequence.size() + 1 > sequence.size()) {
                sequence.clear();
                sequence.add(moves.get(i));
                sequence.addAll(moveSequence);
            }

            this.undoMove(moves.get(i));
        }

        return sequence;
    }
}