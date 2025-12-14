package Model;

public class Assistant extends Character {


    public Assistant() {
        super("Assistant");
    }

    /**
     * Ylopoihsh ths ikanothtas tou Assistant
     * @post O paikths epilexei mia perioxi kai pairnei 1 plakidio
     */
    @Override
    public void ability() {
        //  todo fash B - logikh gia epilogh perioxhs kai draw 1 plakidiou
        // ta abilities tha ektelestoun apo ton controller
        setUsed(true);
    }


    
}
