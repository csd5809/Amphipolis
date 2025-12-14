package Model;
import java.util.*;



public class Bag {

    private ArrayList<Tile> contents;

    /**
     * Constructor
     * @post Αarxikopoiei th sakoula me ola ta aparaithta plakidia tou paixnidiou.
     */
    public Bag(){
        // todo sth fash B
        contents = new ArrayList<Tile>();
        initializeBag();
    }

    private void initializeBag() {
        // todo sth fash B
        int idCounter = 0;
        for (int i = 0; i < 24; i++) {
            contents.add(new LandslideTile(idCounter++, "resources/images/landslide.png"));
    
        }
        // mosaika
        String[] mosaicColors = {"Green", "Red", "Yellow"};
        for (String color : mosaicColors) {
            for (int i = 0; i < 9; i++) {
                
                contents.add(new MosaicTile(idCounter++, "resources/images/mosaic_" + color.toLowerCase() + ".png", color));
            }
        }

        // agalmata
        for (int i = 0; i < 12; i++) {
            contents.add(new CaryatidTile(idCounter++, "resources/images/caryatid.png"));
            contents.add(new SphinxTile(idCounter++, "resources/images/sphinx.png"));
        }

        // amphoreis 
        String[] amphoraColors = {"Blue", "Brown", "Red", "Green", "Yellow", "Purple"};
        for (String color : amphoraColors) {
            for (int i = 0; i < 5; i++) {
                contents.add(new AmphoraTile(idCounter++, "resources/images/amphora_" + color.toLowerCase() + ".png", color));
            }
        }

        // skeletoi
        // 10 megaloi panw
        for(int i=0; i<10; i++) contents.add(new SkeletonTile(idCounter++, "resources/images/skeleton_big_top.png", true, true));
        // 10 megaloi katw
        for(int i=0; i<10; i++) contents.add(new SkeletonTile(idCounter++, "resources/images/skeleton_big_bottom.png", true, false));
        // 5 mikroi panw
        for(int i=0; i<5; i++) contents.add(new SkeletonTile(idCounter++, "resources/images/skeleton_small_top.png", false, true));
        // 5 mikroi katw
        for(int i=0; i<5; i++) contents.add(new SkeletonTile(idCounter++, "resources/images/skeleton_small_bottom.png", false, false));
        
        // anakatema sakoulas
        Collections.shuffle(contents);






    }
    


    /**
     * travaei tuxaia enan arithmo plakidion apo ti sakoula
     * @pre Η sakoula na periexei toulaxiston count(4) plakidia
     * @post Ta plakidia afairountai apo ti sakoula kai meiwnetai to megethos tis
     * @param count O arithmos ton plakidion pou theloume na traviksoume sthn periptosh mas einai 4
     * @return Mia lista me ta plakidia pou travikthikan
     */
    public ArrayList<Tile> drawTiles(int count) {
        ArrayList<Tile> drawn = new ArrayList<>();
        for (int i = 0; i < count && !contents.isEmpty(); i++) {
            drawn.add(contents.remove(0)); // travame to proto kai to svinoume apo ti sakoula
        }
        return drawn;
    }


    /**
     *elegxei an h sakoula einai adeia
     * @return true an den yparxoun plakidia sth sakoula alliws false
     */
    public boolean isEmpty() {
        return contents.isEmpty(); 
    }
   



}
