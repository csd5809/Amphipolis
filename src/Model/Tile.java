package Model;

public abstract class Tile {

    
    private int id; // optional . to ebala gia na exw monadiko kwdiko gia kathe plakidio
    
    // gia to view na fortwsei thn eikona
    private String imagePath;

    /**
     * Constructor tis klasis Tile
     * @pre To id prepei na einai monadiko. To imagePath den prepei na einai null
     * @post Dimiourgeitai ena neo antikeimeno Tile me tis dosmenes times
     * @param id O monadikos kwdikos tou plakidiou
     * @param imagePath To monopati gia tin eikona tou plakidiou (p.x. "assets/green_mosaic.png")
     */
    public Tile(int id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }



    /**
     *Epistrefei to monadiko id tou plakidiou
     *@return To id tou plakidiou
     */
    public int getId() {
        return id;
    }



    /**
     *Epistrefei ti diadromi tis eikonas tou plakidiou
     *@return To String pou periexei to path tis eikonas
     */
    public String getImagePath() {
        return imagePath;
    }
    


    /**
     * Abstract method pou epistrefei to xrwma tou plakidiou.
     * Epeidh to xrwma diaferei ana katigoria (p.x. ta Agalmata den exoun xrwma opws ta Mosaika)
     * tin afhnoume abstract gia na tin ylopoihsoun ta paidia tis
     * @return To xrwma ws String
     */
    public abstract String getColor();
    
}
