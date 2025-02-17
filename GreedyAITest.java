import com.sun.source.tree.AssertTree;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class GreedyAITest {

    /**
     * for testing the greedyAI we will check if?????????????
     */
    @Test
    public void makeMove(){
        PlayableLogic gs=set_game();
        gs.reset();
        for (int i = 0; i < 60000; i++) {
            do_game(gs);
        }

        System.out.println(gs);
    }

    private void do_game(PlayableLogic gs){
        while(!gs.isGameFinished()){
            Move x;
            if(gs.isFirstPlayerTurn())
                x=((AIPlayer)gs.getFirstPlayer()).makeMove(gs);
            else {
                x = ((AIPlayer) gs.getSecondPlayer()).makeMove(gs);
                assertTrue(is_the_right_pos(gs.ValidMoves(),gs,x.position()));
            }
            gs.locate_disc(x.position(),x.disc());
        }
        gs.reset();

    }
    private PlayableLogic set_game(){
        AIPlayer p1=new RandomAI(true);
        AIPlayer p2=new GreedyAI(false);
        PlayableLogic gs=new GameLogic();
        gs.setPlayers(p1,p2);
        gs.reset();
        return gs;
    }

    private boolean is_the_right_pos(List<Position> list, PlayableLogic gs,Position pos){
        int max=-1;
        List<Position> First_list=new ArrayList<>();
        List<Position> Sec_list=new ArrayList<>();
        Position ans=new Position(-1,-1);
        int rightest=-1;
        for(Position p:list){
            int current=gs.countFlips(p);
            if(current>max)
                max=current;
        }
        for (Position p:list)
            if(gs.countFlips(p)==max) {
                First_list.add(p);
                if(p.col()>rightest)
                    rightest=p.col();
            }
        for(Position p: First_list)
            if(p.col()==rightest)
                Sec_list.add(p);
        for(Position p:Sec_list)
            if(p.row()>ans.row())
                ans=p;
        return (ans.col()== pos.col() &&ans.row()==pos.row());
    }
}
