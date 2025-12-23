package Model;

public class SphinxTile extends StatueTile {


    /**
     * constructor
     * @param id to ID
     * @param imagePath to path tis eikonas
     */
    public SphinxTile(int id, String imagePath) {
        super(id, imagePath);
    }

    /**
     * epistrefei to xrwma/typo
     * xrhsimopoioume to onoma gia na ksexwrizei
     */
    @Override
    public String getColor() {
        return "Sphinx";
    }
    
}
