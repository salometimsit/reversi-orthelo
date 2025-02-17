import java.util.ArrayList;
import java.util.List;


/***
 * we created Flip class to handle all cases of disks that needs to be flipped
 * and all kind of disks.
 */
public class Flip {

    /***
     * this function checks if we need to flip the discs in a specific direction
     * @returns true if we need to go in that direction and false otherwise
     */
    private static boolean need_to_flip(Position a, int direct_row, int direct_col, Player player, int BoardSize, Disc[][] Board) {
        int flips = 0;
        int r = a.row() + direct_row;
        int c = a.col() + direct_col;
        while (is_valid(r,c,BoardSize)) {
            if (Board[r][c] == null)
                return false;
            if (Board[r][c].getOwner() != player) {
                flips++;
                r += direct_row;
                c += direct_col;
            } else {
                return flips>0;

            }

        }
        return false;
    }
    /***
     * this function gets a direction and adds to the list all the discs on its way
     * it handles all sort of discs.
     */
    public static void add_flips(Position a, Disc[][] Board, Player p, List<Position> list, int board_size) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        for (int i = 0; i < 8; i++) {
            if (need_to_flip(a, directions[i][0], directions[i][1], p, Board.length, Board)) {
                int r = a.row() + directions[i][0];
                int c = a.col() + directions[i][1];
                while (Board[r][c].getOwner() != p) {
                    if (Board[r][c] instanceof BombDisc && !is_in_list(list,new Position(r,c))) {
                        list.add(new Position(r, c));
                        Bomb(new Position(r, c), board_size, Board, p, list);
                    }
                    if((Board[r][c] instanceof SimpleDisc)&& !is_in_list(list,new Position(r,c))){
                        list.add(new Position(r, c));
                    }
                    r += directions[i][0];
                    c += directions[i][1];
                }
            }
        }
    }

    /***
     * this function handles the bomb disk. we add to the list all the neighbours
     * of the specific bomb that need to be flipped,
     * we handled the situations of other bombs and or plain discs.
     */
    private static void Bomb(Position pos, int Board_size, Disc[][] Board, Player player, List<Position> list) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        for (int[] direction : directions) {
            int r = pos.row() + direction[0];
            int c = pos.col() + direction[1];
            if (is_valid(r, c, Board_size) && Board[r][c] != null) {
                if ((Board[r][c].getOwner() != player) && (!(Board[r][c] instanceof UnflippableDisc))) {
                    Position p = new Position(r, c);
                    if (Board[r][c] instanceof SimpleDisc) {
                        if (!is_in_list(list, p)) {
                            list.add(p);
                        }
                    }
                    if (Board[r][c] instanceof BombDisc) {
                        if (!is_in_list(list, p)) {
                            list.add(p);
                            Bomb(p, Board_size, Board, player, list);
                        }
                    }
                }
            }
        }
    }
    /***
     * this function helps us just to define if the location is even valid
     * @return True if it's in the bound and False otherwise.
     */
    public static boolean is_valid(int row, int col, int BoardSize) {
        return (row < BoardSize && col < BoardSize) && (row >= 0 && col >= 0);
    }

    /***
     * This is the function that gathers all the other functions in Flip class
     * main flip first adds the position to the list and sends it to the other
     * functions the list will be updated with all the discs we need to flip
     * and finally flip the list.
     * @return list of position
     */
    public static List<Position> main_flip(Position a, Disc[][] Board, Player p, int board_size) {
        List<Position> list = new ArrayList<>();
        add_flips(a, Board, p, list, board_size);
        for (Position position : list) {
            int r = position.row();
            int c = position.col();
            if (Board[r][c] != null) {
                Board[r][c].setOwner(p);
            }

        }
        return list;
    }

    /***
     * A function that counts how many disc a specific location will flip.
     * it iterates on the list of disc we need to flip and adds to the counter.
     * @return a counter flip.
     */
    public static int count_flip(Position a, Disc[][] Board, Player p, int board_size) {
        List<Position> list = new ArrayList<>();
        int flip = 0;
        add_flips(a, Board, p, list, board_size);
        for (Position position : list) {
            int r = position.row();
            int c = position.col();
            if ((Board[r][c] != null) && (Board[r][c].getOwner() != p)) {
                flip++;
            }

        }
        return flip;
    }

    /***
     * a method used to check if a position is in a list
     * @returns True if the position is in the list and False otherwise
     */

    public static boolean is_in_list(List<Position> list, Position pos){
        for (Position position : list) {
            if (pos.row() == position.row() && pos.col() == position.col()) {
                return true;
            }
        }
        return false;
    }

}






