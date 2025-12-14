package Model;

public class CaryatidTile extends StatueTile{
    /**
     * Constructor
     * @param id To monadiko ID
     * @param imagePath To path tis eikonas
     */
    public CaryatidTile(int id, String imagePath) {
        super(id, imagePath);
    }

    /**
     * Ylopoiisi tis ypoxreotikis methodou getColor
     */
    @Override
    public String getColor() {
        return "Caryatid"; 
    }
    
}
