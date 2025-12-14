package Model;
import java.util.*;

public class Player {

    private String name;
    private int score;
    private String color; // To xrwma tou paikth (optional px "red", "blue" ktl)
    private ArrayList<Tile> myTiles; // H syllogh tou
    private ArrayList<Character> myCharacters; // Oi kartes tou

    private boolean hasPlayed; //an epaikse ston trexonta gyro
    private boolean hasPlayedCoder;//an exei xrhsimopoihsei ton xarakthra "The Coder" ston trexonta gyro
    
    private int lastSelectedArea = -1;
    private int coderSelectedArea = -1;

    


    /**
     * Constructor
     * @pre to onoma kai to xrwma na einai egkura
     * @post O paikths dhmiourgeitai me arxiko skoro 0 kai gemato xeri xarakthρων.
     * @param name To onoma tou paikth.
     * @param color To xrwma tou paikth.
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

        // Μοιράζουμε τους 5 χαρακτήρες στον παίκτη
        myCharacters.add(new Assistant());
        myCharacters.add(new Archaeologist());
        myCharacters.add(new Digger());
        myCharacters.add(new Professor());
        myCharacters.add(new TheCoder());
    }

    /**
     * ypologizei tous pontous tou paikth me bash tous kanones tou paixnidiou
     * @pre To paixnidi na exei teleiwsei h na theloume endiameso skore.
     * @post to pedio score enhmerwnetai me to neo skore tou paikth
     * @return To synoliko skore.
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
     * @pre O paikths na exei diathesimh thn karta kai na mhn thn exei ksanapaiksei
     * @post H karta markaretai ws xrhsimopoihmenh (used)
     * @param character H karta xarakthra pros xrhsh
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

    // --- Helper Methods για τον Controller ---

    public void addTiles(ArrayList<Tile> tiles) {
        this.myTiles.addAll(tiles);
    }

    public void addScore(int s) {
        this.score += s;
    }

    public int getSphinxCount() {
        int c = 0;
        for(Tile t : myTiles) if(t instanceof SphinxTile) c++;
        return c;
    }

    public int getCaryatidCount() {
        int c = 0;
        for(Tile t : myTiles) if(t instanceof CaryatidTile) c++;
        return c;
    }

    // --- Getters & Setters ---
    public String getName() { return name; }// apo tin a fash
    public ArrayList<Tile> getMyTiles() { return myTiles; } // apo thn a fash 
    public ArrayList<Character> getMyCharacters() { return myCharacters; }
    
    public boolean hasPlayed() { return hasPlayed; }
    public void setHasPlayed(boolean hasPlayed) { this.hasPlayed = hasPlayed; }

    public boolean hasPlayedCoder() { return hasPlayedCoder; }
    public void setHasPlayedCoder(boolean hasPlayedCoder) { this.hasPlayedCoder = hasPlayedCoder; }

    public String getColor() { 
        return color; 
    }


    public int getLastSelectedArea() { return lastSelectedArea; }
    public void setLastSelectedArea(int area) { this.lastSelectedArea = area; }

    public int getCoderSelectedArea() { return coderSelectedArea; }
    public void setCoderSelectedArea(int area) { this.coderSelectedArea = area; }

}


