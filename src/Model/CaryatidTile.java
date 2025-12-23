package Model;

public class CaryatidTile extends StatueTile{
    /**
     * constructor
     * @param id to monadiko ID
     * @param imagePath to path tis eikonas
     */
    public CaryatidTile(int id, String imagePath) {
        super(id, imagePath);
    }

    /**
     * 
     * @return to onoma/"xrwma" tou plakidiou
     */
    @Override
    public String getColor() {
        return "Caryatid"; 
    }
    
}
