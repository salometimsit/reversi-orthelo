import java.util.Comparator;
/***
 * this class is a comparator, it compares columns in specific positions
 */
public class ColumnCompare implements Comparator<Position> {
    @Override
    public int compare(Position t1, Position t2) {
        return Integer.compare(t1.col(), t2.col());
    }
}
