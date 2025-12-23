package Model;
import java.util.*;

public class Player implements java.io.Serializable{

    private String name;
    private int score;
    private String color; // to xrwma tou paikth (optional px "red", "blue" ktl)
    private ArrayList<Tile> myTiles; // h syllogh tou
    private ArrayList<Character> myCharacters; // oi kartes tou

    private boolean hasPlayed; //an epaikse ston trexonta gyro
    private boolean hasPlayedCoder;//an exei xrhsimopoihsei ton xarakthra "The Coder" ston trexonta gyro
    
    private int lastSelectedArea = -1;
    private int coderSelectedArea = -1;
    private boolean hasSelectedFromArea = false;
    private boolean hasPickedFromBoard; // an exei parei plakidia apo perioxi sto bhma 2

    


    /**
     * Constructor
     * @pre to onoma kai to xrwma na einai egkura
     * @post o paikths dhmiourgeitai me arxiko skoro 0 kai gemato xeri xarakthrwn
     * @param name to onoma tou paikth
     * @param color to xrwma tou paikth
     */
    public Player(String name, String color) {
        // todo sth fash B
        this.name = name;
        this.color = color;
        this.score = 0;
        this.myTiles = new ArrayList<>();
        this.myCharacters = new ArrayList<>();

        
        this.hasPlayed = false;
        this.hasPlayedCoder = false;
        this.hasPickedFromBoard = false;

        // moirazoume tous 5 xarakthres ston paikth
        myCharacters.add(new Assistant());
        myCharacters.add(new Archaeologist());
        myCharacters.add(new Digger());
        myCharacters.add(new Professor());
        myCharacters.add(new TheCoder());
    }

    /**
     * ypologizei tous pontous tou paikth me bash tous kanones tou paixnidiou
     * @pre to paixnidi na exei teleiwsei h na theloume endiameso skore.
     * @post to pedio score enhmerwnetai me to neo skore tou paikth
     * @return to synoliko skore.
     */
    public int calculateScore() {
        int totalScore = 0;

        // ypologismos mosaikon
        // metrame 4ades idiou xrwmatos -> 4 pontoi
        int green = 0, red = 0, yellow = 0;
        for (Tile t : myTiles) {
            if (t instanceof MosaicTile) {
                String c = t.getColor();
                if (c.equals("Green")) green++;
                else if (c.equals("Red")) red++;
                else if (c.equals("Yellow")) yellow++;
            }
        }
        totalScore += (green / 4) * 4;
        totalScore += (red / 4) * 4;
        totalScore += (yellow / 4) * 4;
        // Υπολογισμός μωσαϊκών με μικτά χρώματα (2 πόντοι ανά 4άδα)
        int remainingMosaics = (green % 4) + (red % 4) + (yellow % 4);
        totalScore += (remainingMosaics / 4) * 2;
        

        // ypologismos amphorewn (diaforretika xrwmata)
        ArrayList<String> distinctColors = new ArrayList<>();
        for (Tile t : myTiles) {
            if (t instanceof AmphoraTile) {
                String c = t.getColor();
                if (!distinctColors.contains(c)) {
                    distinctColors.add(c);
                }
            }
        }
        int countColors = distinctColors.size();
        if (countColors >= 6) totalScore += 6;
        else if (countColors == 5) totalScore += 4;
        else if (countColors == 4) totalScore += 2;
        else if (countColors == 3) totalScore += 1;

        // ypologismos skeletwn (oikogeneiwn)
        int bigTop = 0, bigBot = 0, smallTop = 0, smallBot = 0;
        for (Tile t : myTiles) {
            if (t instanceof SkeletonTile) {
                SkeletonTile sk = (SkeletonTile) t;
                if (sk.isBig() && sk.isUpper()) bigTop++;
                else if (sk.isBig() && !sk.isUpper()) bigBot++;
                else if (!sk.isBig() && sk.isUpper()) smallTop++;
                else if (!sk.isBig() && !sk.isUpper()) smallBot++;
            }
        }
        // friaxnoume olokliromenous skeletous
        int bigSkeletons = Math.min(bigTop, bigBot);
        int smallSkeletons = Math.min(smallTop, smallBot);

        // fitaxnoume oikogeneies 2 big kai ena small
        int families = Math.min(bigSkeletons / 2, smallSkeletons);
        
        // 6 pontoi kathe oikogeneia
        totalScore += families * 6;

        // pontoi apo osous perisepsan
        int remainingBig = bigSkeletons - (families * 2);
        int remainingSmall = smallSkeletons - families;
        totalScore += remainingBig * 1;
        totalScore += remainingSmall * 1;

        return totalScore;
    }

    /**
     * xrhsimopoiei mia karta xarakthra
     * @pre o paikths na exei diathesimh thn karta kai na mhn thn exei ksanapaiksei
     * @post h karta markaretai ws xrhsimopoihmenh (used)
     * @param character h karta xarakthra pros xrhsh
     */
    public void useCharacter(Character character) {
        if (!character.isUsed()) {
            character.ability(); // kalei tin ikanothta
            // special elegxos gia ton coder
            if (character instanceof TheCoder) {
                this.hasPlayedCoder = true;
            }
        }
    }

    // helper methods gia ton controller


    /**
     * prosthetei mia lista apo plakidia sti sylloji tou paikti.
     * @param tiles h lista me ta nea plakidia.
     * @post ta plakidia prostithentai sti lista myTiles tou paikti.
     */
    public void addTiles(ArrayList<Tile> tiles) {
        this.myTiles.addAll(tiles);
    }

    public void addScore(int s) {
        this.score += s;
    }


    /**
     * metraei poses Sfigges exei o paiktis.
     * xrisimopoieitai gia ton ypologismo twn pontwn twn agalmatwn sto telos.
     * @return to plithos twn plakidiwn SphinxTile.
     */
    public int getSphinxCount() {
        int c = 0;
        for(Tile t : myTiles) if(t instanceof SphinxTile) c++;
        return c;
    }


    /**
     * metraei poses Karyatides exei o paiktis.
     * @return to plithos twn plakidiwn CaryatidTile.
     */
    public int getCaryatidCount() {
        int c = 0;
        for(Tile t : myTiles) if(t instanceof CaryatidTile) c++;
        return c;
    }

    // setters and getters


    /**
     * 
     * @return to onoma tou paikth
     */
    public String getName() { return name; }// apo tin a fash


    /**
     * 
     * @return ta tiles tou paikth
     */
    public ArrayList<Tile> getMyTiles() { return myTiles; } // apo thn a fash 

    /**
     * 
     * @return tous xarakthres tou paikth
     */
    public ArrayList<Character> getMyCharacters() { return myCharacters; }

    /**
     * 
     * @return an exei paixtei o paikths ston trexonta gyro
     */
    public boolean hasPlayed() { return hasPlayed; }


    /**
     * 
     * @param hasPlayed orisma gia na orisoume an exei paixtei o paikths ston trexonta gyro
     */
    public void setHasPlayed(boolean hasPlayed) { this.hasPlayed = hasPlayed; }


    /**
     * 
     * @return an exei xrhsimopoihsei ton xarakthra "The Coder" ston trexonta gyro
     */
    public boolean hasPlayedCoder() { return hasPlayedCoder; }


    /**
     * 
     * @param hasPlayedCoder orisma gia na orisoume an exei xrhsimopoihsei ton xarakthra "The Coder" ston trexonta gyro
     */
    public void setHasPlayedCoder(boolean hasPlayedCoder) { this.hasPlayedCoder = hasPlayedCoder; }



    /**
     * 
     * @return to xrwma tou paikth
     */
    public String getColor() { 
        return color; 
    }


    /**
     * 
     * @return thn teleytaia perioxh pou epilekse o paikths
     */
    public int getLastSelectedArea() { return lastSelectedArea; }

    /**
     * 
     * @param area orisma gia na orisoume tin teleytaia perioxh pou epilekse o paikths
     */
    public void setLastSelectedArea(int area) { this.lastSelectedArea = area; }


    /**
     * 
     * @return tin perioxh pou epilekse o paikths me ton xarakthra "The Coder"
     */
    public int getCoderSelectedArea() { return coderSelectedArea; }


    /**
     * 
     * @param area orisma gia na orisoume tin perioxh pou epilekse o paikths me ton xarakthra "The Coder"
     */
    public void setCoderSelectedArea(int area) { this.coderSelectedArea = area; }


    /**
     * 
     * @return an o paikths exei epileksei plakidia apo mia perioxi
     */
    public boolean hasSelectedFromArea() { return hasSelectedFromArea; }

    /**
     * 
     * @param val orisma gia na orisoume an o paikths exei epileksei plakidia apo mia perioxi
     */
    public void setHasSelectedFromArea(boolean val) { this.hasSelectedFromArea = val; }

    /**
     * 
     * @return an o paikths exei parei plakidia apo tin pinaka ston 2o bhma
     */
    public boolean hasPickedFromBoard() { return hasPickedFromBoard; }

    /**
     * 
     * @param hasPickedFromBoard orisma gia na orisoume an o paikths exei parei plakidia apo tin pinaka ston 2o bhma
     */
    public void setHasPickedFromBoard(boolean hasPickedFromBoard) { this.hasPickedFromBoard = hasPickedFromBoard; }

}


