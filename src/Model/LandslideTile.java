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
     * epistrefei to xrwma tou plakidiou
     * ta plakidia katolisthisis den exoun xrwma paixnidiou, opote epistrefoume mia typiki timi
     * @return "brown" ws endixi oti einai petra
     */
    public String getColor() {
        return "brown";
    }
    
}
