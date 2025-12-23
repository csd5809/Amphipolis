package Model;
import java.util.ArrayList;

public class Board implements java.io.Serializable {

    private ArrayList<Tile> mosaicArea;
    private ArrayList<Tile> statueArea;
    private ArrayList<Tile> amphoraArea;
    private ArrayList<Tile> skeletonArea;
    private ArrayList<Tile> landslideArea;

    /**
    *constructor
    *arxikopoiei tis listes pou antistoixoun stis perioxes tou board
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
    *prostethei tile stin katallili perioxh tou board
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
     * afairei ena tile apo tin katallili perioxh tou board
     * @pre to tile prepei na yparxei sti sosti perioxh
     * @post to tile afaireitai apo ti lista kai to plithos meionetai kata ena
     * @param tile to plakidio pou tha afairethei
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

    /**
     * 
     * @return tin mosaikh perioxh tou board
     */
    public ArrayList<Tile> getMosaicArea() { return mosaicArea; }

    /**
     * 
     * @return tin perioxh twn agalmatwn tou board
     */
    public ArrayList<Tile> getStatueArea() { return statueArea; }


    /**
     * 
     * @return tin perioxh twn amphorwn tou board
     */
    public ArrayList<Tile> getAmphoraArea() { return amphoraArea; }

    /**
     * 
     * @return tin perioxh twn skeletwn tou board
     */

    public ArrayList<Tile> getSkeletonArea() { return skeletonArea; }

    /**
     * 
     * @return tin perioxh twn katolisthisewn tou board
     */
    public ArrayList<Tile> getLandslideArea() { return landslideArea; }




    
}
