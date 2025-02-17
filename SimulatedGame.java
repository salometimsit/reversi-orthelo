import java.util.ArrayList;
import java.util.List;

public class SimulatedGame {

    /**
     * check what are the valid moves based on the board that changed
     * @param Board- the copy of the board
     * @param player- the player that it is his turn
     * @param board_size - the size of the board
     * @return a list of positions that they are valid
     */
    public static List<Position> Valid_moves(Disc[][] Board, Player player, int board_size){
        List<Position> valid=new ArrayList<>();
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                if (Board[i][j] == null) {
                    if (Flip.count_flip(new Position(i, j),Board,player,board_size) > 0) {
                        valid.add(new Position(i, j));
                    }
                }
            }

        }
        return valid;
    }

    /**
     * simulate a move on the board
     * @param position - the position that I want to put a disc in
     * @param Board- the copy of the board
     * @param player- the player that played
     * @param board_size- size of the board
     */
     public static void simulated_move(Position position, Disc[][] Board, Player player, int board_size){
         Flip.main_flip(position,Board,player,board_size);
     }

    /**
     * checks if the game is finished (if there are no valid moves)
     * @param Board - the copy of the board
     * @param player -the current player
     * @param board_size - the size of the board
     * @return if the game is finished
     */
     public static boolean is_game_finished(Disc[][]Board, Player player, int board_size){
         return Valid_moves(Board, player, board_size).isEmpty();
     }

    /**
     * checked in case that the game is finished who win
     * @param Board - the copy of the board
     * @param is_player_one- if our player is the first player or not
     * @param board_size - the size of the board
     * @return true if our player win and false if our player lose
     */
     public static boolean my_win(Disc[][]Board, boolean is_player_one, int board_size){
        int player1=0;
        int player2=0;
         for (int i = 0; i < board_size; i++) {
             for (int j = 0; j <board_size ; j++) {
                 if(Board[i][j]!=null){
                     if(Board[i][j].getOwner().isPlayerOne())
                         player1++;
                     else{
                         player2++;
                     }
                 }
             }
         }
         return ((is_player_one) && player1 > player2) || ((!is_player_one) && player2 > player1);
     }

    /**
     * take a board and return a deep copy of the same board
     * @param Board the original board
     * @return deep copy of the board
     */
    public static Disc[][] CopyBoard(Disc[][] Board){
        Disc[][] New=new Disc[Board.length][Board.length];
        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board.length; j++) {
                if(Board[i][j]!=null){
                    if(Board[i][j].getType().equals("⬤")){
                        New[i][j]=new SimpleDisc(Board[i][j]);
                    }
                    if(Board[i][j].getType().equals("💣")){
                        New[i][j]=new BombDisc(Board[i][j]);
                    }
                    if(Board[i][j].getType().equals("⭕")){
                        New[i][j]=new UnflippableDisc(Board[i][j]);
                    }
                }
            }

        }
        return New;
    }

    /**
     * create the board from game status by take all discs in their positions and pit them in their place
     * @param gs- the game
     * @return the same board in this position from gs
     */
    public static Disc[][] extractBoard(PlayableLogic gs){
        int BoardSize=gs.getBoardSize();
        Disc [][] New=new Disc[BoardSize][BoardSize];
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                New[i][j]=gs.getDiscAtPosition(new Position(i,j));
            }

        }
        return CopyBoard(New);
    }

    /**
     * return the other Player
     * @param gs- the game
     * @return return who is the other player
     */
    public static Player otherPlayer(PlayableLogic gs){
        if(gs.isFirstPlayerTurn()){
            return gs.getSecondPlayer();
        }
        return gs.getFirstPlayer();
    }

    /**
     * return the MinMaxAI player
     * @param gs- the game
     * @return who is this turn player
     */
    public static Player thisPlayer(PlayableLogic gs){
        if (gs.isFirstPlayerTurn()){
            return gs.getFirstPlayer();
        }
        return gs.getSecondPlayer();
    }


}
