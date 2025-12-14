package Model;

public class SphinxTile extends StatueTile {


    /**
     * Constructor
     * @param id To ID
     * @param imagePath To path tis eikonas
     */
    public SphinxTile(int id, String imagePath) {
        super(id, imagePath);
    }

    /**
     * Epistrefei to xrwma/typo. 
     * Xrhsimopoioume to onoma gia na ksexwrizei.
     */
    @Override
    public String getColor() {
        return "Sphinx";
    }
    
}
