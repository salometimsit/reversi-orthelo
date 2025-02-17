import java.util.Comparator;

/***
 * this class is a comparator, it compares rows in specific positions
 */
public class RowCompare implements Comparator<Position> {
    @Override
    public int compare(Position t1, Position t2) {
        return Integer.compare(t1.row(), t2.row());
    }
}
