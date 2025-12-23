package Model;

public class AmphoraTile  extends FindingTile{


    private String color; // Blue, Brown, Red, Green, Yellow, Purple

    /**
     * constructor
     * @param id to id
     * @param imagePath To path eikonas
     * @param color To xroma tou amphorea
     */
    public AmphoraTile(int id, String imagePath, String color) {
        super(id, imagePath);
        this.color = color;
    }

    /**
     * epistrefei to xroma tou amphorea
     * @return To xroma os string
     */
    @Override
    public String getColor() {
        return color;
    }
    
}
