package Model;

public class Assistant extends Character {

    /**
     * constructor
     * @post dhmiourgeitai o xarakthras Assistant me to onoma tou
     */
    public Assistant() {
        super("Assistant");
    }

    /**
     * ylopoihsh ths ikanothtas tou Assistant
     * @post o paikths epilexei mia perioxi kai pairnei 1 plakidio
     */
    @Override
    public void ability() {// ola auta ginontai apo ton controller
        //  todo fash B - logikh gia epilogh perioxhs kai draw 1 plakidiou
        // ta abilities tha ektelestoun apo ton controller
        setUsed(true);
    }


    
}
