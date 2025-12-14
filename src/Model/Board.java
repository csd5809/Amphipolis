package Model;
import java.util.ArrayList;

public class Board {

    private ArrayList<Tile> mosaicArea;
    private ArrayList<Tile> statueArea;
    private ArrayList<Tile> amphoraArea;
    private ArrayList<Tile> skeletonArea;
    private ArrayList<Tile> landslideArea;

    /**
    *Constructor
    *Arxikopoiei tis listes pou antistoixoun stis perioxes tou board
    *@post dimiourgountai kenes listes gia kathe perioxh tou board
    */

    public Board(){
        // todo sth fash B
        mosaicArea = new ArrayList<>();
        statueArea = new ArrayList<>();
        amphoraArea = new ArrayList<>();
        skeletonArea = new ArrayList<>();
        landslideArea = new ArrayList<>();
    }


    /**
    *Prostethei tile stin katallili perioxh tou board
    *@pre to tile prepe ina mhn einai null kai h antistoixh perioxh prepei na mhn exei gemisei
    *@post to plakidio prostithetai sth sosth lista kai to plithos ton stoixeiwn ths listas auksanetai kata ena
    * @param tile to plakidio pou tha prostethei sto board
    */
    public void addTileToArea(Tile tile){
        // todo sth fash B
        if (tile instanceof MosaicTile) {
            mosaicArea.add(tile);
        } else if (tile instanceof StatueTile) { 
            statueArea.add(tile);
        } else if (tile instanceof AmphoraTile) {
            amphoraArea.add(tile);
        } else if (tile instanceof SkeletonTile) {
            skeletonArea.add(tile);
        } else if (tile instanceof LandslideTile) {
            landslideArea.add(tile);
        }
    }


    /**
     * elegxei an exei gemish h perioxh eisodou me katolisthiseis
     * @return true an h perioxh katolisthisis exei 16 tiles alliws false.
     */
    public boolean isEntranceFull() {
        return landslideArea.size() >= 16;
    }
    
    /**
     * Afairei ena tile apo tin katallili perioxh tou board
     * @pre To tile prepei na yparxei sti sosti perioxh
     * @post To tile afaireitai apo ti lista kai to plithos meionetai kata ena
     * @param tile To plakidio pou tha afairethei
     */
    public void removeTileFromArea(Tile tile) {
        if (tile instanceof MosaicTile) {
            mosaicArea.remove(tile);
        } else if (tile instanceof StatueTile) {
            statueArea.remove(tile);
        } else if (tile instanceof AmphoraTile) {
            amphoraArea.remove(tile);
        } else if (tile instanceof SkeletonTile) {
            skeletonArea.remove(tile);
        } else if (tile instanceof LandslideTile) {
            landslideArea.remove(tile);
        }
    }


    //setters and getters
    public ArrayList<Tile> getMosaicArea() { return mosaicArea; }
    public ArrayList<Tile> getStatueArea() { return statueArea; }
    public ArrayList<Tile> getAmphoraArea() { return amphoraArea; }
    public ArrayList<Tile> getSkeletonArea() { return skeletonArea; }
    public ArrayList<Tile> getLandslideArea() { return landslideArea; }



    
}
