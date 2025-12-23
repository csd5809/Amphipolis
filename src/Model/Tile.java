package Model;

public abstract class Tile implements java.io.Serializable {

    
    private int id; // optional . to ebala gia na exw monadiko kwdiko gia kathe plakidio
    
    // gia to view na fortwsei thn eikona
    private String imagePath;

    /**
     * constructor tis klasis Tile
     * @pre to id prepei na einai monadiko. To imagePath den prepei na einai null
     * @post dimiourgeitai ena neo antikeimeno Tile me tis dosmenes times
     * @param id o monadikos kwdikos tou plakidiou
     * @param imagePath to monopati gia tin eikona tou plakidiou (p.x. "assets/green_mosaic.png")
     */
    public Tile(int id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }



    /**
     *epistrefei to monadiko id tou plakidiou
     *@return to id tou plakidiou
     */
    public int getId() {
        return id;
    }



    /**
     *epistrefei ti diadromi tis eikonas tou plakidiou
     *@return to String pou periexei to path tis eikonas
     */
    public String getImagePath() {
        return imagePath;
    }
    


    /**
     * abstract method pou epistrefei to xrwma tou plakidiou.
     * epeidh to xrwma diaferei ana katigoria (p.x. ta Agalmata den exoun xrwma opws ta Mosaika)
     * tin afhnoume abstract gia na tin ylopoihsoun ta paidia tis
     * @return to xrwma ws String
     */
    public abstract String getColor();
    
}
