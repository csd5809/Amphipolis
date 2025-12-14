package Model;

public class LandslideTile extends Tile {

    /**
     * constructor gia to plakidio katolis gia to plakidio katolisthisis
     * @param id O monadikos arithmos tou plakidiou
     * @param imagePath to path tis eikonas
     */
    public LandslideTile(int id, String imagePath) {
        super(id, imagePath);
    }



    
    /**
     * Epistrefei to xrwma tou plakidiou
     * Ta plakidia katolisthisis den exoun xrwma paixnidiou, opote epistrefoume mia typiki timi
     * @return "Brown" ws endeixi oti einai petra
     */
    public String getColor() {
        return "Brown";
    }
    
}
