import java.util.ArrayList;
import java.util.List;

/**
 * MinMaxAI Class:
 * This class worked about give this AI Player avg of winning against the random player that sum to 10:1
 * The way we made this is with sum astretagies for winning:
 *  *At first if our player can do move that make him he will do that
 *  *If there is a move that makes our player lost he will do that move
 *  *Corners are the best because discs there cannot flip ever again so our player will put there with simple disc
 *  *If the player can place his disc on edge between 2 discs of the other player it is safe from flips
 *  *If the player can place his disc on the edge (not between 2 other player discs) and have unflappable disc.
 *  *otherwise if it is the start of the game our player will flip as less disc he can, and then as much.
 *All of those above make our MinMaxAI player smarter than the random player
 *
 */
public class MinMaxAI extends AIPlayer {

    private int NUMBER_OF_MOVES; //count the number of moves the player did
    private final int FLIP_FLAG=6; //how much moves it will choose the less flip
    private boolean flag;// the flag that on ,him we choose how much to flip

    public MinMaxAI(boolean is_first_player) {
        super(is_first_player);
        NUMBER_OF_MOVES = 0;
        flag=false;
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        check_flag();
        // first we extract all the things we need from the game
        Player this_player = SimulatedGame.thisPlayer(gameStatus);
        Player other_player = SimulatedGame.otherPlayer(gameStatus);
        Disc[][] Board = SimulatedGame.extractBoard(gameStatus);
        List<Position> valid_moves = gameStatus.ValidMoves();
        int board_size = gameStatus.getBoardSize();
        //choose position and disc
        Position p = smart_position(valid_moves, Board, board_size, this_player, other_player);
        Disc d=smart_disc(p,Board,board_size,this_player,other_player);
        NUMBER_OF_MOVES++; // adding 1 for the moves we did
        return new Move(p,d); //return the move we want to do
    }

    /**
     * reset the number of moves and the flag.
     */
    public void reset_game(){
        NUMBER_OF_MOVES=0;
        flag=false;
    }

    /**
     * this function return us the best type of Disk while it relies on the actual position
     * @param p the position that was chosen
     * @param Board-the board of the game
     * @param board_size- the board size
     * @param this_player - our player
     * @param other_player - the other player
     * @return the best disc
     */
    private Disc smart_disc(Position p, Disc[][]Board, int board_size, Player this_player, Player other_player){
        SimulatedGame.simulated_move(p,Board,this_player,board_size);
        if(is_corner(p,board_size))
            return new SimpleDisc(this_player);
        if(number_of_bombs>0){
            if(near_other_player(p,Board,board_size,this_player))
                return new BombDisc(this_player);
        }
        if(number_of_unflippedable>0 &&(is_UOD_edge(p,board_size)||is_LOR_edge(p,board_size))){
            if (need_unflappable(p,Board,other_player,board_size))
                return new UnflippableDisc(this_player);
        }

        return new SimpleDisc(this_player);
    }

    /**
     * the function relies on the board situation and returns the best option for close to winning
     * @param valid_moves- all the valid moves
     * @param Board- the copy of board of the game
     * @param board_size- the size of the board
     * @param this_player- our player
     * @param other_player- other player
     * @return the very best position
     */
    private Position smart_position(List<Position> valid_moves, Disc[][] Board, int board_size,
                                    Player this_player, Player other_player) {
        List<Position> valid = new ArrayList<>(valid_moves);
        Position p = pos_make_us_win(valid, Board, board_size, this_player, other_player); //check if we can win in 1 move
        if (p != null)
            return p;
        if (valid.isEmpty()) {
            return valid_moves.getFirst(); //if there are no option of winning return the first position
        }
        p=corner(valid,Board,board_size,this_player); //if we have option to put in the corner
        if (p != null)
            return p;
        p=edge(valid,Board,board_size,this_player,other_player,true); //if there are option to put between 2 of the opponent
        if (p != null)
            return p;
        if(number_of_unflippedable>0) {
            p =edge(valid,Board,board_size,this_player,other_player,false); //if we can put on the edge and we have unflappable
            if(p!=null)
                return p;
        }
        if(number_of_bombs>0){
            p= need_bomb(valid,Board,this_player,board_size);
            if(p!=null)
                return p;
        }
        return choose_pos(valid,Board,board_size,this_player); // random disc

    }

    /**
     * Method that go throws the valid moves and check if there are moves that ends the game:
     * if the position make us win we want to do that move
     * if the position make the other player win we don't want it to be an option.
     *
     * @param valid_moves - all the valid moves
     * @param Board- the copy of the board
     * @param board_size- the size of the board
     * @param this_player- this player
     * @param other_player- other player
     * @return a position that if we put in it a disc we will win, null if there isn't
     */
    private Position pos_make_us_win(List<Position> valid_moves, Disc[][] Board, int board_size, Player this_player, Player other_player) {
        List<Position>to_remove=new ArrayList<>();
        for (Position p : valid_moves) {
            Disc[][] copy_board = SimulatedGame.CopyBoard(Board);
            SimulatedGame.simulated_move(p, copy_board, this_player, board_size);
            if (SimulatedGame.is_game_finished(copy_board, other_player, board_size)) {
                if (SimulatedGame.my_win(copy_board, isPlayerOne(), board_size))
                    return p;
                else {
                    to_remove.add(p);
                }
            }
        }
        for (Position position : to_remove) {
            valid_moves.remove(position);
        }

        return null;
    }

    /**
     * takes all the parameter and check if there are any move that I can do in their corner
     * discs that will be in corners cannot flip again.
     * @param valid_moves - all the valid moves
     * @param Board- the copy of the board
     * @param board_size- the size of the board
     * @param this_player- this player
     * @return the position in the corner that will flip as many discs.
     */
    private Position corner(List<Position> valid_moves, Disc[][] Board, int board_size, Player this_player) {
        Position max_pos=null;
        int max_flips=0;
        for (Position p : valid_moves) {
            if(is_corner(p,board_size)){
                int x=Flip.count_flip(p,Board,this_player,board_size);
                if (x>max_flips){
                    max_flips=x;
                    max_pos=p;
                }

            }
        }
        return max_pos;
    }

    /**
     * check if the current position is in 1 of the four corners of the board
     * @param p  - th position
     * @param size- the size of the board
     * @return- true or false based on if it is in corner or not
     */
    private boolean is_corner(Position p, int size){
        int r=p.row(); int c=p.col();
        return (r == 0 && c == 0) || (r == size - 1 && c == 0) || (r == 0 && c == size - 1) || (r == size - 1 && c == size - 1);
    }

    /**
     * this method checks if there is an available move on the edge of the board in general (if the f param is false)
     * and if the f param is true it will return only a position that located between 2 opponent discs.
     * @param valid_moves - all the valid moves
     * @param Board- the copy of the board
     * @param board_size- the size of the board
     * @param this_player- this player
     * @param other_player - the other player
     * @param f- a boolean parameter that tells the func if we and between 2 opponent discs (true) or not (false)
     * @return the best position
     */
    private Position edge(List<Position> valid_moves, Disc[][] Board, int board_size,
                          Player this_player,Player other_player, boolean f){
        List<Position> optSD=new ArrayList<>(); // a list for those between the opponent discs
        List<Position> optUD=new ArrayList<>(); // a list for all other options on edge
        for(Position p:valid_moves) {
            int r = p.row();
            int c = p.col();
            if (is_UOD_edge(p, board_size)) { //check if the position is on the top or bottom edge
                if (f) { //check in witch case are we
                    if(Flip.is_valid(r, c - 1, board_size) && Flip.is_valid(r, c + 1, board_size)) {
                        if (Board[r][c - 1] != null && Board[r][c + 1] != null) {
                            if (Board[r][c - 1].getOwner() == other_player && Board[r][c + 1].getOwner() == other_player)
                                optSD.add(p); // after checkin it between 2 opponent discs
                        }
                    }
                }
                else{
                    optUD.add(p); // if the flag is off we add it just if it is on edge
                }
            }
            if (is_LOR_edge(p, board_size)) {// check if the position is on the left or right edge
                if (f) {// check the case
                    if (Flip.is_valid(r-1, c , board_size) && Flip.is_valid(r+1, c , board_size)) {
                        if (Board[r - 1][c] != null && Board[r + 1][c] != null) {
                            if (Board[r - 1][c].getOwner() == other_player && Board[r + 1][c].getOwner() == other_player)
                                optSD.add(p);// after checking that we are between 2 opponent discs
                        }
                    }
                }
                else{
                    optUD.add(p);// in the case that flag is off
                }
            }
        }
        //now we choose 1 position to return
        if(!optSD.isEmpty()){
            return edge_pos(optSD,Board,board_size, this_player);
        }
        if(!optUD.isEmpty()) {
            eliminate(optUD, board_size);
            return edge_pos(optUD, Board, board_size, this_player);
        }
        return null;
    }

    /**
     * check if the position is on the top or bottom edges (not including the corners)
     * @param p- the position
     * @param size- the size of the board
     * @return if the position in the top or bottom edge
     */
    private boolean is_UOD_edge(Position p, int size){
        int r=p.row();
        return (r == 0 || r == size - 1) && !is_corner(p, size);
    }

    /**
     * check if the position is on the left or right edges (not including the corners)
     * @param p- the position
     * @param size- the size of the board
     * @return if the position in the left or right edge
     */
    private boolean is_LOR_edge(Position p, int size){
        int c=p.col();
        return (c == 0 || c == size - 1) && !is_corner(p, size);
    }

    /**
     * we have an inner flag that tells us if in each move to play the move that's flip the most or
     * the less, i , base on how moves we have already done, (that's a strategy of the game at first flip less as the
     * player can)
     */
    private void check_flag() {
        if (NUMBER_OF_MOVES > FLIP_FLAG)
            flag = true;
    }

    /**
     * choose the best position
     * @param list- the list of the good edge position
     * @param Board- the board
     * @param board_size- the board size
     * @param this_player- this player
     * @return the best position
     */
    private Position edge_pos(List<Position>list, Disc[][]Board, int board_size, Player this_player){
        return choose(list,Board,this_player,board_size);
    }

    /**
     * the method first eliminate the position that close to the corner, because it can give the opponent an advantage
     * @param valid_moves - all the valid moves
     * @param Board- the copy of the board
     * @param board_size- the size of the board
     * @param this_player- this player
     * @return a position based on the better options
     */
    private  Position choose_pos(List<Position> valid_moves, Disc[][] Board, int board_size, Player this_player){
        eliminate(valid_moves,board_size);
        return choose(valid_moves,Board,this_player,board_size);
    }

    /**
     * eliminate the positions are close to the corner (those who I step away from them)
     * @param valid - all the valid moves
     * @param board_size- the size of the board
     */
    private void eliminate(List<Position>valid,int board_size){
        List<Position> list=new ArrayList<>(valid);
        List<Position> to_remove=new ArrayList<>();
        int [] range={0,1,board_size-2,board_size-1};
        for (int i = 0; i <range.length ; i++) {
            for (int j = 0; j < range.length; j++) { //going trow all the 4 corners and the three pos on the bord close to
                if(!is_corner(new Position(i,j),board_size))
                    to_remove.add(new Position(i,j));
            }
        }
        for (int i = list.size()-1; i >=0; i--) {
            if (Flip.is_in_list(to_remove,list.get(i)))//checks for each pos in the lost if is it in the valid and removed it
                list.remove(i);
        }
        if (!list.isEmpty()){
            valid=list;
        }
    }

    /**
     * a method that chooses the position that return the best position based on how much it
     * flips and relies on the flag
     * @param list the list of position we need to choose from
     * @param Board- the copy of the board
     * @param this_player- this player
     * @param board_size- the size of the board
     * @return a random position in case of the flag
     */
    private Position choose(List<Position> list, Disc[][]Board, Player this_player,int board_size){
        Position ans=null;
        if(flag) {// if the flag is true we want the position that will flip as much.
            int max_flips=Integer.MIN_VALUE;
            for (Position p:list){
                int x=Flip.count_flip(p,Board,this_player,board_size);
                if (x>max_flips){
                    max_flips=x;
                    ans=p;
                }
            }
        }
        else{// if the flag is false we want the position that will flip as less.
            int min_flips=Integer.MAX_VALUE;
            for (Position p:list){
                int x=Flip.count_flip(p,Board,this_player,board_size);
                if(x<min_flips){
                    min_flips=x;
                    ans=p;
                }
            }
        }
        return ans;
    }

    /**
     * method that checks for how much opponent discs I close to (for knowing putting bomb) cause
     * that more chance the opponent wil flip the bomb and will not flip for him a lot
     * but more chance that I flip it back
     * @param p- the position
     * @param Board- the copy of the board
     * @param board_size- the size of the board
     * @param this_player- this player
     * @return if my position is near a lot of opponent discs
     */
    private boolean near_other_player(Position p, Disc[][]Board, int board_size, Player this_player){
        int count=0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        for (int i = 0; i < 8; i++) {//going throw all the position directions
            int r=p.row()+directions[i][0];
            int c=p.col()+directions[i][1];
            if(Flip.is_valid(r,c,board_size))
                if(Board[r][c]!=null )
                    if (Board[r][c].getOwner()!=this_player)
                        count++;
        }
        return count >= 6;
    }

    /**
     * check if the position on the edge is between 2 opponent discs
     * @param p- the position I want to check
     * @param Board- the copy of the board
     * @param board_size- the size of the board
     * @param other_player- opponent player
     * @return return true if the position isn't near the opponent
     *           return false if the position between the opponent
     */
    private boolean need_unflappable(Position p, Disc [][] Board, Player other_player, int board_size){
        int r=p.row(); int c=p.col();
        if(Flip.is_valid(r-1,c,board_size)&&Flip.is_valid(r+1,c,board_size)) {
            if (Board[r - 1][c] != null && Board[r + 1][c] != null) {
                if (Board[r - 1][c].getOwner() == other_player && Board[r + 1][c].getOwner() == other_player)
                    return false;
            }
        }
        if(Flip.is_valid(r,c-1,board_size)&&Flip.is_valid(r,c+1,board_size)) {
            if (Board[r][c - 1] != null && Board[r][c + 1] != null) {
                return Board[r][c - 1].getOwner() != other_player || Board[r][c + 1].getOwner() != other_player;
            }
        }
        return true;
    }

    /**
     * check if there is a place that good enough if we will put a bomb in it
     * @param valid al valid moves
     * @param Board- the copy of the board
     * @param this_player- our player
     * @param board_size- the size of the board
     * @return the position if there is one
     */
    private Position need_bomb(List<Position>valid, Disc [][] Board, Player this_player, int board_size){
        List <Position> options=new ArrayList<>();
        for (Position p:valid) {
            if(near_other_player(p,Board,board_size,this_player))
                options.add(p);//add if it is near a lot of other opponent discs
        }
        if(!options.isEmpty())
            return choose_pos(options,Board,board_size,this_player);
        return null;
    }

}