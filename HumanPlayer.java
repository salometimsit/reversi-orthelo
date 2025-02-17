/***
 * this Class was created as a subclass of Player to
 * indicate if the player is a human player
 */
public class HumanPlayer extends Player{
    public HumanPlayer(boolean player1){
        super(player1);
    }

    @Override
    boolean isHuman() {
        return true;
    }

}
