package Model;

public abstract class Character implements java.io.Serializable {

    private String name;
    private boolean isUsed; // an exei "kaei" h karta

    /**
     * consructor gia ton xarakthra
     * @param name To onoma tou xarakthra (px "Professor")
     */
    public Character(String name) {
        this.name = name;
        this.isUsed = false;
    }

    /**
     * h eikanothta tou xarakthra
     * @pre h karta na mhn exei xrhsimopoih8ei (isUsed == false)
     * @post Ektelietai h eidikh energeia kai h karta "kaigetai"
     */
    public abstract void ability(); // abstract methodos thn opoia tha ylopoihsoyn oi upoklaseis


    /**
     * epistrefei to onoma tou xarakthra
     * @return to onoma tou xarakthra
     */
    public boolean isUsed() { return isUsed; }

    /**
     * orizei an exei xrhsimopoih8ei h karta
     * @param used true an exei xrhsimopoih8ei alliws false
     */
    public void setUsed(boolean used) { isUsed = used; }
    
}
