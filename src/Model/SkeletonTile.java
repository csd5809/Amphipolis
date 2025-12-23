package Model;

public class SkeletonTile  extends FindingTile {
    

    private boolean isBig;   // true = Megalos/Enilikas h false = Mikros/Paidi
    private boolean isUpper; // true = Pano merosh false = Kato meros

    /**
     * constructor
     * @param id to ID
     * @param imagePath to path eikonas
     * @param isBig an einai megalos skeleto (true) i mikros (false)
     * @param isUpper an einai to pano meros (true) i to kato meros (false)
     */
    public SkeletonTile(int id, String imagePath, boolean isBig, boolean isUpper) {
        super(id, imagePath);
        this.isBig = isBig;
        this.isUpper = isUpper;
    }

    /**
     * epistrefei an o skeleto einai megalos enilikas
     * @return true an einai megalos, false an einai mikros
     */
    public boolean isBig() {
        return isBig;
    }

    /**
     * epistrefei an einai to pano meros tou skeleto
     * @return true an einai pano meros, false an einai kato meros
     */
    public boolean isUpper() {
        return isUpper;
    }

    /**
     * oi skeletoi den exoun xrwma opws ta mosaica h ta amphorea ara
     * epistrefoume "White" i perigrafi typou
     */
    @Override
    public String getColor() {
        return "White"; 
    }
}
