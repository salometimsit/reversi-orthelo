import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyAI extends AIPlayer {
    public GreedyAI(boolean is_player_one) {
        super(is_player_one);
    }

    /**
     * a method that return a move that include simple disc and a position
     * in that position it will flip as most discs it can,
     * and it will be the one is the right and down
     * @param gameStatus- the game
     * @return the move of the greedy
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        int max_moves=0;
        List<Position> list = gameStatus.ValidMoves();
        List<Position> max_valid = new ArrayList<>();
        max_valid.add(list.getFirst());
        max_moves = gameStatus.countFlips(list.getFirst());
        for (int i = 1; i < list.size(); i++) { //make a list with all the max flips positions
            int x = gameStatus.countFlips(list.get(i));
            if (max_moves == x) {
                max_valid.add(list.get(i));
            }
            if (max_moves < x) {
                max_moves=x;
                max_valid = new ArrayList<>();
                max_valid.add(list.get(i));
            }
        }
        ColumnCompare ColCompare = new ColumnCompare();
        RowCompare rowCompare = new RowCompare();
        //it will sort the list and put the positions from the left top pos to the right bottom
        Comparator<Position> col_row_comp = ColCompare.thenComparing(rowCompare);
        max_valid.sort(col_row_comp);
        return new Move(max_valid.getLast(), new SimpleDisc(this));
    }
}
