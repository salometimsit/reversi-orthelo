import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomAITest {
    /**
     * test if for more than half of the bombs and unflappable will be used in the random pos
     */
    @Test
    void makeMove() {
        PlayableLogic gs=set_game();
        int[]count=new int[2];
        for (int i = 0; i < 50000; i++) {
            do_game(gs,count);
        }
        System.out.println(count[0]+"  "+count[1]);
        assertTrue(count[0]>count[1]);
        assertTrue(count[0]>82000);
        assertTrue(count[1]>58000);
    }
    private void do_game(PlayableLogic gs,int[]count){
        while(!gs.isGameFinished()){
            Move x;
            if(gs.isFirstPlayerTurn())
                x=((AIPlayer)gs.getFirstPlayer()).makeMove(gs);
            else x=((AIPlayer)gs.getSecondPlayer()).makeMove(gs);
            if(x.disc() instanceof BombDisc)
                count[0]++;
            if(x.disc() instanceof UnflippableDisc)
                count[1]++;
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

}