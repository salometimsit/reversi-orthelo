public class PrintMoves {
    /**
     * print the move that the player have done
     * @param move- the current move
     * @param gs- the game status
     */
    public static void print_move(Move move,PlayableLogic gs){
        if (gs.isFirstPlayerTurn()) {
            System.out.println("Player 1 placed a " + move.disc().getType() + " in " + move.position());
        } else {
            System.out.println("Player 2 placed a " + move.disc().getType() + " in " + move.position());
        }
        for (Position p: move.getList_of_flips()) {
            if (gs.isFirstPlayerTurn()) {
                System.out.println("Player 1 Flipped the "+gs.getDiscAtPosition(p).getType() +" in "+ p);
            }
            else{
                System.out.println("Player 2 Flipped the "+gs.getDiscAtPosition(p).getType() +" in "+ p);
            }
        }
        System.out.println();
    }

    /**
     * print the move that I undo
     * @param move- the move that I took back (if there is no move it will be null)
     * @param gs-the game status
     */
    public static void print_undo(Move move,PlayableLogic gs){
        if(move==null)
            System.out.println("\tNo previous move available to undo.");
        else {
            System.out.println("Undoing last move:");
            System.out.println("\tUndo removing " + move.disc().getType() + " from " + move.position());
            for (Position p : move.getList_of_flips()) {
                System.out.println("\tUndo: flipping back " + gs.getDiscAtPosition(p).getType() + " in " + p);
            }
        }
        System.out.println();
    }

    /**
     * print who win (in case there is a winner, and it is not a tie) and with how many discs he won.
     * @param num_player1- the number of the discs on the board of player one.
     * @param num_player2- the number of the discs on the board of player two.
     */
    public static void finished_game(int num_player1,int num_player2){
        if (num_player1>num_player2){
            System.out.println("Player 1 wins with "+num_player1+" discs! Player 2 had "+ num_player2+" discs");
        }
        if(num_player2>num_player1){
            System.out.println("Player 2 wins with "+num_player2+" discs! Player 1 had "+ num_player1+" discs");
        }
        System.out.println();
    }

}
