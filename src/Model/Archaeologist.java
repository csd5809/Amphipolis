package Model;

public class Archaeologist extends Character {


    /**
     * constructor
     * @post dhmiourgeitai o xarakthras Archaeologist me to onoma tou
     */
    public Archaeologist() {
        super("Archaeologist");
    }

    /**
     * ylopoihsh ths ikanothtas tou Archaeologist.
     * @pre o paikths prepei na exei hdh kanei thn kanonikh tou kinhsh se mia perioxi
     * @post o paikths dialexei mia DIAFORETIKH perioxi kai pairnei eos 2 plakidia
     */
    @Override
    public void ability() {// ola auta ginontai apo ton controller
        //  todo fash B - elefxos oti h perioxi einai diaforetikh apo auti pou epaikse(perioxh != choosenArea)
        // ta abilities tha ektelestoun apo ton controller
        setUsed(true);
    }
    
}
