//Jaskaran Talwar
//Assignment 8

import java.util.ArrayList;
public class Cracker {
    public static void main(String[] args) {
        
        Board b = new Board();

        ArrayList<Board.Space> starts = new ArrayList<Board.Space>();

        starts.add(new Board.Space(0,0));
        starts.add(new Board.Space(1,0));
        starts.add(new Board.Space(1,1));
        starts.add(new Board.Space(2,0));
        starts.add(new Board.Space(2,1));

        for(int i = 0; i < starts.size(); i++) {

            // Get next starting position
            Board.Space start = starts.get(i);

            System.out.println("\n=== " + start + " ===");

            // Initialize the board
            b.setup(5, start);

            // Get the best sequence of moves to win
            ArrayList<Board.Move> bs = b.bestSequence();

            // Print the move
            System.out.println();
            b.printState(b.b);

            for(int j = 0; j < bs.size(); j++){
                System.out.println("\n" + bs.get(j) + "\n");

                b.move(bs.get(j));

                b.printState(b.b);
            }
        }
    }
}