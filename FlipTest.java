import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlipTest {
    private Player p1;
    private Player p2;
    private Disc[][]Board;
    private final int BOARD_SIZE=8;

    @Test
    public void main_flip() {
        p1=new HumanPlayer(true);
        p2=new HumanPlayer(false);
        BuildBoard();
 //       PrintBoard();
        Position a=new Position(6,0);
        Board[6][0]=new SimpleDisc(p1);
        List <Position>list=Flip.main_flip(a,Board,p1,BOARD_SIZE);
        for (Position position : list) {
            assertSame(Board[position.row()][position.col()].getOwner(), p1);
        }
 //       PrintBoard();
    }

    @Test
    public void count_flip() {
        p1=new HumanPlayer(true);
        p2=new HumanPlayer(false);
        BuildBoard();
        Position a=new Position(6,0);
        assertEquals(12,Flip.count_flip(a,Board,p1,BOARD_SIZE));
    }

    @Test
    public void is_in_valid() {
        Position p1=new Position(6,0);
        Position p2=new Position(5,1);
        Position p3=new Position(0,0);
        List<Position> list=new ArrayList<>();
        list.add(new Position(6,0));
        list.add(new Position(0,4));
        list.add(new Position(2,6));
        list.add(new Position(3,0));
        list.add(new Position(3,6));
        list.add(new Position(4,1));
        list.add(new Position(7,3));
        assertTrue(Flip.is_in_list(list,p1));
        assertFalse(Flip.is_in_list(list,p2));
        assertFalse(Flip.is_in_list(list,p3));
    }

    /**
     * Build the Board for the test
     */
    private void BuildBoard(){
        Board=new Disc[BOARD_SIZE][BOARD_SIZE];
        Board[1][2]=new SimpleDisc(p1);
        Board[1][5]=new SimpleDisc(p1);
        Board[2][5]=new SimpleDisc(p1);
        Board[3][4]=new SimpleDisc(p1);
        Board[4][5]=new SimpleDisc(p1);
        Board[6][5]=new SimpleDisc(p1);
        Board[7][1]=new SimpleDisc(p1);
        Board[3][1]=new SimpleDisc(p2);
        Board[3][5]=new SimpleDisc(p2);
        Board[4][3]=new SimpleDisc(p2);
        Board[4][4]=new SimpleDisc(p2);
        Board[5][1]=new SimpleDisc(p2);
        Board[5][2]=new SimpleDisc(p2);
        Board[5][3]=new SimpleDisc(p2);
        Board[5][4]=new SimpleDisc(p2);
        Board[6][1]=new SimpleDisc(p2);
        Board[6][2]=new SimpleDisc(p2);
        Board[6][4]=new SimpleDisc(p2);
        Board[7][4]=new SimpleDisc(p2);
        Board[2][2]=new UnflippableDisc(p1);
        Board[6][3]=new UnflippableDisc(p1);
        Board[1][4]=new UnflippableDisc(p2);
        Board[3][2]=new UnflippableDisc(p2);
        Board[2][3]=new BombDisc(p1);
        Board[2][4]=new BombDisc(p2);
        Board[3][3]=new BombDisc(p2);
        Board[4][2]=new BombDisc(p2);

    }
}