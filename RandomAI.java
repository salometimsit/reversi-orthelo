import java.util.List;

/***
 * This class represents the Random AI that is extended of AI player
 */
public class RandomAI extends AIPlayer{
    public RandomAI(boolean is_player_one){
        super(is_player_one);
    }
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> list=gameStatus.ValidMoves();
        int move_p=(int) (Math.random()*list.size());
        return new Move(list.get(move_p),RandomDisc());
    }

    /***
     *
     * @return true if the AI still has bombs and false if not
     */
    private Boolean have_bombs(){
        return number_of_bombs > 0;
    }

    /***
     *
     * @return true if the AI still has unflappable discs and false if not
     */
    private Boolean have_unflappable(){
        return number_of_unflippedable > 0;
    }

    /***
     *here the generates start, checking if the AI still has bombs and or unflappable discs
     * we did a random system that will give us random disc with the same weight of each disc:
     * for simple disc we gave 80% chance
     * for bomb disc we gave 15% chance
     * for unflappable disc we gave 5% chance
     * @return a random disc
     */
    private Disc RandomDisc(){
        if (!have_bombs() && !have_unflappable()) {
            return new SimpleDisc(this);
        }
        if (!have_bombs()){
           int r=  (int)(Math.random()*100);
           if (r<=91){
               return new SimpleDisc(this);
           }
           else{
               return new UnflippableDisc(this);
           }

        }
        if (!have_unflappable()){
            int r=  (int)(Math.random()*1000);
            if (r<=930){
                return new SimpleDisc(this);
            }
            else{
                return new BombDisc(this);
            }

        }
        int r=  (int)(Math.random()*1200);
        if (r<1060){
            return new SimpleDisc(this);
        }
        if(r<1140){
            return new BombDisc(this);
        }
        if(r<1200){
            return  new UnflippableDisc(this);
        }
        return null;
    }
}
