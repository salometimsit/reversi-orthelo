import java.util.*;

/***
 * This Class represents a single move in the game
 */
public class Move {
    private final Position _pos;
    private final Disc _disc;
    private List<Position> list_of_flips;


    public Move(Position pos, Disc disc){
        _pos=pos;
        _disc=disc;

    }
    ////////////// Getters ///////////////////
    public Position position(){
        return _pos;
    }

    public Disc disc(){
        return _disc;
    }

    ////////////// Setters ////////////////////
    public void set_flips(List<Position>a){
        list_of_flips =a;
    }

    public List<Position> getList_of_flips() {
        return list_of_flips;
    }

    /***
     * The undo method reverses the last move made on the game board
     * It restores the state of the board  and gives back to the discs the other owner.
     * we did it in a way that we have a list of flips, and we used it in order to restore the board as is
     */
    public void Undo(Disc[][]Board, Player p){
            Board[_pos.row()][_pos.col()] = null;
            if (_disc instanceof BombDisc) {
                _disc.getOwner().increase_bomb();
            }
            if (_disc instanceof UnflippableDisc) {
                _disc.getOwner().increase_unflippedable();
            }
            for (Position pos: list_of_flips){
                Disc d = Board[pos.row()][pos.col()];
                d.setOwner(p);
            }
    }

}