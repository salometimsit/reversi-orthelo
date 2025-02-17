import java.util.*;

// this class is the main one in our game, we implemented from PlayableLogic
// we got all the functions and added some private methods for help
public class GameLogic implements PlayableLogic {

    private final int BOARD_SIZE = 8;
    private Disc[][] Board;
    protected List<Position> ValidMoves;
    private Stack<Move> moves;
    private Player firstplayer;
    private Player secondplayer;
    private Player CurrentPlayer;

    public GameLogic() {
        Board = new Disc[BOARD_SIZE][BOARD_SIZE];
        moves = new Stack<>();
        CurrentPlayer=firstplayer;

    }

    /***
     * Attempt to locate a disc on the game board
     * @param a The position for locating a new disc on the board.
     * @param disc- the disc the player need to locate
     * @return true if the move is valid and successful, false otherwise.
     */
    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if(disc.getOwner()==CurrentPlayer) {
            if ((disc instanceof BombDisc && CurrentPlayer.getNumber_of_bombs() == 0) ||
                    (disc instanceof UnflippableDisc && CurrentPlayer.getNumber_of_unflippedable() == 0)) {
                return false;
            }
            if (Board[a.row()][a.col()] == null && Flip.is_in_list(ValidMoves,a)) {
                Move move = new Move(a, disc);
                moves.add(move);
                Board[a.row()][a.col()] = disc;
                move.set_flips(Flip.main_flip(a, Board, CurrentPlayer, BOARD_SIZE));
                if (disc instanceof BombDisc) {
                    CurrentPlayer.reduce_bomb();
                }
                if (disc instanceof UnflippableDisc) {
                    CurrentPlayer.reduce_unflippedable();
                }
                PrintMoves.print_move(move,this);
                flip_turns();
                return true;
            }
        }
        return false;
    }

    /***
     * Get the disc located at a given position on the game board.
     * @param position The position for which to retrieve the disc.
     * @return a specific disc
     */

    @Override
    public Disc getDiscAtPosition(Position position) {

        return Board[position.row()][position.col()];
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    @Override
    public List<Position> ValidMoves() {
        ValidMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (Board[i][j] == null) {
                    if (countFlips(new Position(i, j)) > 0) {
                        ValidMoves.add(new Position(i, j));
                    }
                }
            }

        }
        return ValidMoves;
    }

    @Override
    public int countFlips(Position a) {
        return Flip.count_flip(a, Board, CurrentPlayer,getBoardSize());
    }

    @Override
    public Player getFirstPlayer() {
        return firstplayer;
    }

    @Override
    public Player getSecondPlayer() {
        return secondplayer;
    }

    @Override
    public void setPlayers(Player player1, Player player2) {
        firstplayer = player1;
        secondplayer = player2;
        CurrentPlayer=firstplayer;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        if(!(CurrentPlayer==null)) {
            return CurrentPlayer.isPlayerOne();
        }
        else{
            return true;
        }
    }

    @Override
    public boolean isGameFinished() {
        ValidMoves();
        if(ValidMoves.isEmpty()) {
            int num_player1=0;
            int num_player2 = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if(Board[i][j]!=null){
                        if (Board[i][j].getOwner().isPlayerOne()){
                            num_player1++;
                        }
                        else{
                            num_player2++;
                        }
                    }
                }

            }
            if (num_player1>num_player2){
                firstplayer.addWin();
            }
            if(num_player2>num_player1) {
                secondplayer.addWin();
            }
            PrintMoves.finished_game(num_player1,num_player2);
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        firstplayer.reset_bombs_and_unflippedable();
        secondplayer.reset_bombs_and_unflippedable();
        Board = new Disc[BOARD_SIZE][BOARD_SIZE];
        CurrentPlayer=firstplayer;
        moves=new Stack<>();
        //placed the discs in their start positions
        Board[BOARD_SIZE/2-1][BOARD_SIZE/2-1] = new SimpleDisc(firstplayer);
        Board[BOARD_SIZE/2][BOARD_SIZE/2] = new SimpleDisc(firstplayer);
        Board[BOARD_SIZE/2-1][BOARD_SIZE/2] = new SimpleDisc(secondplayer);
        Board[BOARD_SIZE/2][BOARD_SIZE/2-1] = new SimpleDisc(secondplayer);
        //reset the variables for the MinMaxAI
        if(firstplayer instanceof MinMaxAI)
            ((MinMaxAI) firstplayer).reset_game();
        if (secondplayer instanceof MinMaxAI)
            ((MinMaxAI) secondplayer).reset_game();


    }

    @Override
    public void undoLastMove() {
        if(firstplayer.isHuman()&&secondplayer.isHuman()) {
            if (!moves.isEmpty()) {
                Move last = moves.pop();
                if (CurrentPlayer.isPlayerOne()) {
                    last.Undo(Board, firstplayer);
                } else {
                    last.Undo(Board, secondplayer);
                }
                PrintMoves.print_undo(last,this);
                flip_turns();
            } else {
                PrintMoves.print_undo(null,this);
            }
        }
    }

    /**
     * flip the turns between player one and player two
     */
    private void flip_turns(){
        if (isFirstPlayerTurn()) {
            CurrentPlayer=secondplayer;
        } else {
            CurrentPlayer=firstplayer;
        }
    }
}