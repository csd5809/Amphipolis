package Model;

public class MosaicTile extends FindingTile {

    private String color; // "Green", "Red", "Yellow"

    /**
     * Constructor
     * @param id to ID
     * @param imagePath To path eikonas
     * @param color To xroma tou mosaikou
     */
    public MosaicTile(int id, String imagePath, String color) {
        super(id, imagePath);
        this.color = color;
    }

    /**
     * Epistrefei to xroma tou mosaikou
     * @return To xroma (Green, Red, Yellow)
     */
    @Override
    public String getColor() {
        return color;
    }
    
}
