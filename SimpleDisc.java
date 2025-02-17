/***
 * This class implements Disc and represents a simple disc
 */
public class SimpleDisc implements Disc {
    private Player Owner;
    /**
     * regular constructor
     * @param owner- the owner of the disc
     */
    public SimpleDisc(Player owner){
        Owner=owner;
    }
    /**
     * copy constructor
     * @param disc- the disc that need deep copy of him
     */
    public SimpleDisc(Disc disc){
        Owner=disc.getOwner();
    }
    @Override
    public Player getOwner() {
        return Owner;
    }

    @Override
    public void setOwner(Player player) {
        Owner=player;
    }

    @Override
    public String getType() {
        return "⬤";
    }
}
