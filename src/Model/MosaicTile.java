package Model;

public class MosaicTile extends FindingTile {

    private String color; // "Green", "Red", "Yellow"

    /**
     * constructor
     * @param id to ID
     * @param imagePath to path eikonas
     * @param color to xroma tou mosaikou
     */
    public MosaicTile(int id, String imagePath, String color) {
        super(id, imagePath);
        this.color = color;
    }

    /**
     * epistrefei to xroma tou mosaikou
     * @return to xroma (Green, Red, Yellow)
     */
    @Override
    public String getColor() {
        return color;
    }
    
}
