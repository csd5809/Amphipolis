package Model;

public class Archaeologist extends Character {

    public Archaeologist() {
        super("Archaeologist");
    }

    /**
     * Ylopoihsh ths ikanothtas tou Archaeologist.
     * @pre O paikths prepei na exei hdh kanei thn kanonikh tou kinhsh se mia perioxi
     * @post O paikths dialexei mia DIAFORETIKH perioxi kai pairnei eos 2 plakidia
     */
    @Override
    public void ability() {
        //  todo fash B - elefxos oti h perioxi einai diaforetikh apo auti pou epaikse(perioxh != choosenArea)
        // ta abilities tha ektelestoun apo ton controller
        setUsed(true);
    }
    
}
