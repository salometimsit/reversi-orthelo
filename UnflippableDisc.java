/***
 * This class implements Disc and represents a UnflappableDisc
 */

public class UnflippableDisc implements Disc{
    private final Player Owner;
    /**
     * regular constructor
     * @param owner- the owner of the disc
     */
    public UnflippableDisc(Player owner){
        Owner=owner;
    }
    /**
     * copy constructor
     * @param disc- the disc that need deep copy of him
     */
    public UnflippableDisc(Disc disc){
        Owner=disc.getOwner();
    }
    @Override
    public Player getOwner() {
        return Owner;
    }

    /**
     * empty setter cause it is unflappable disc
     * @param player- the player
     */
    @Override
    public void setOwner(Player player) {

    }

    @Override
    public String getType() {
        return "⭕";
    }
}
