
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest {
    private static GameLogic g1;
    @Test
    public void locate_disc() {
        g1=new GameLogic();
        g1.setPlayers(new HumanPlayer(true), new HumanPlayer(false));
        g1.reset();
        g1.ValidMoves();
        Position p=new Position(2,4);
        assertTrue(g1.locate_disc(p,new BombDisc(g1.getFirstPlayer())));
        //trying to locate in not valid position
        assertFalse(g1.locate_disc(p,new BombDisc(g1.getSecondPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(2,3),new BombDisc(g1.getSecondPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(1,2),new BombDisc(g1.getFirstPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(1,3),new BombDisc(g1.getSecondPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(1,4),new BombDisc(g1.getFirstPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(0,3),new BombDisc(g1.getSecondPlayer())));
        g1.ValidMoves();
        //trying to locate more than 3 bombs
        assertFalse(g1.locate_disc(new Position(0,4),new BombDisc(g1.getFirstPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(0,4),new UnflippableDisc(g1.getFirstPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(2,5),new UnflippableDisc(g1.getSecondPlayer())));
        g1.ValidMoves();
        //trying to locate a disc that it isn't his turn
        assertFalse(g1.locate_disc(new Position(3,5),new UnflippableDisc(g1.getSecondPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(3,5),new UnflippableDisc(g1.getFirstPlayer())));
        g1.ValidMoves();
        assertTrue(g1.locate_disc(new Position(4,5),new UnflippableDisc(g1.getSecondPlayer())));
        g1.ValidMoves();
        //trying to locate more than 2 unflappable
        assertFalse(g1.locate_disc(new Position(5,5),new UnflippableDisc(g1.getFirstPlayer())));
    }

    @Test
    public void validMoves() {
        g1=new GameLogic();
        g1.setPlayers(new HumanPlayer(true), new HumanPlayer(false));
        g1.reset();
        List<Position>RealList=g1.ValidMoves();
        List<Position>ExpectList=new ArrayList<>();
        ExpectList.add(new Position(2,4));
        ExpectList.add(new Position(3,5));
        ExpectList.add(new Position(4,2));
        ExpectList.add(new Position(5,3));
        for (Position position : ExpectList)
            assertTrue(is_in_list(RealList, position));
        g1.locate_disc(new Position(4,2),new UnflippableDisc(g1.getFirstPlayer()));
        assertTrue(is_in_list(g1.ValidMoves(),new Position(3,2)));
        assertTrue(is_in_list(g1.ValidMoves(),new Position(5,2)));
        assertTrue(is_in_list(g1.ValidMoves(),new Position(5,4)));
        assertEquals(3, g1.ValidMoves().size());


    }

    @Test
    public void isGameFinished() {
        g1=new GameLogic();
        g1.setPlayers(new HumanPlayer(true), new HumanPlayer(false));
        g1.reset();
        while (!g1.ValidMoves().isEmpty()){
            List<Position>list=g1.ValidMoves();
            if (g1.isFirstPlayerTurn()){
                assertFalse(g1.isGameFinished());
                g1.locate_disc(list.getFirst(),new SimpleDisc(g1.getFirstPlayer()));
            }
            else{
                assertFalse(g1.isGameFinished());
                g1.locate_disc(list.getFirst(),new SimpleDisc(g1.getSecondPlayer()));
            }
        }
        assertTrue(g1.isGameFinished());
        assertTrue(g1.getFirstPlayer().getWins()==1||
                g1.getSecondPlayer().getWins()==1);

    }

    @Test
    public void undoLastMove() {
        g1=new GameLogic();
        g1.setPlayers(new HumanPlayer(true), new HumanPlayer(false));
        g1.reset();
        Stack<List<Position>> stack= build_board();
        for (int i = 0; i < 14; i++) {
            g1.undoLastMove();
            List<Position>p=g1.ValidMoves();
            List<Position>valid=stack.pop();
            for (Position position : p) {
                assertTrue(is_in_list(valid, position));
            }
        }

    }

    private static boolean is_in_list(List<Position> list, Position pos){
        for (Position position : list) {
            if (pos.row() == position.row() && pos.col() == position.col()) {
                return true;
            }
        }
        return false;
    }
    private static Stack<List<Position>> build_board(){
        Stack <List<Position>>valid=new Stack<>();
        for (int i = 0; i < 16; i++) {
            valid.add(g1.ValidMoves());
            if (g1.isFirstPlayerTurn()){
                if (i % 3 == 0 && g1.getFirstPlayer().getNumber_of_bombs()!=0) {
                    g1.locate_disc(g1.ValidMoves().getFirst(),new BombDisc(g1.getFirstPlayer()));
                }
                else{
                    if (i%6==0 && g1.getFirstPlayer().getNumber_of_unflippedable()!=0){
                        g1.locate_disc(g1.ValidMoves().getFirst(),new UnflippableDisc(g1.getFirstPlayer()));
                    }
                    else{
                        g1.locate_disc(g1.ValidMoves().getFirst(),new SimpleDisc(g1.getFirstPlayer()));
                    }
                }
            }
            else{
                if (i % 4 == 0 && g1.getSecondPlayer().getNumber_of_bombs()!=0) {
                    g1.locate_disc(g1.ValidMoves().getFirst(),new BombDisc(g1.getSecondPlayer()));
                }
                else {
                    if (i % 5 == 0 && g1.getSecondPlayer().getNumber_of_unflippedable() != 0) {
                        g1.locate_disc(g1.ValidMoves().getFirst(), new UnflippableDisc(g1.getSecondPlayer()));
                    } else {
                        g1.locate_disc(g1.ValidMoves().getFirst(), new SimpleDisc(g1.getSecondPlayer()));
                    }
                }
            }
        }
        return valid;
    }
}