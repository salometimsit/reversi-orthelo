/***
 * This Class represents a single Position in the game
 */
public class Position {
    private final int Row;
    private final int Col;

    public Position(int row,int col){
        Row=row;
        Col=col;
    }
    ////////////////// Getters //////////

    public int row() {
        return Row;
    }

    public int col() {
        return Col;
    }

    public String toString(){
        return (" ("+Row+","+col()+")");
    }

}
