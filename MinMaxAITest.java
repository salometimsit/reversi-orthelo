import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MinMaxAITest {

    @Test
    void makeMove() {
        PlayableLogic gs=set_game();
        for (int i = 0; i < 100000; i++) {
            do_game(gs);
            ((MinMaxAI)gs.getSecondPlayer()).reset_game();
        }
        Assertions.assertTrue(gs.getFirstPlayer().getWins()*10<=gs.getSecondPlayer().getWins());

    }

    private void do_game(PlayableLogic gs){
        while(!gs.isGameFinished()){
            Move x;
            if(gs.isFirstPlayerTurn())
                x=((AIPlayer)gs.getFirstPlayer()).makeMove(gs);
            else {
                x = ((AIPlayer) gs.getSecondPlayer()).makeMove(gs);
            }
            gs.locate_disc(x.position(),x.disc());
        }
        gs.reset();

    }
    private PlayableLogic set_game(){
        AIPlayer p1=new RandomAI(true);
        AIPlayer p2=new MinMaxAI(false);
        PlayableLogic gs=new GameLogic();
        gs.setPlayers(p1,p2);
        gs.reset();
        return gs;
    }
}