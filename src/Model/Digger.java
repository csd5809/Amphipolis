package Model;

public class Digger extends Character {


    public Digger() {
        super("Digger");
    }

    /**
     * Ylopoihsh ths ikanothtas tou Ekskafea.
     * @pre O paikths prepei na exei hdh epilexei mia perioxi
     * @post O paikths pairnei alla 2 plakidia apo thn IDIA perioxi
     */
    @Override
    public void ability() {
        setUsed(true);


        // fash b - logikh gia draw 2 plakidiwn apo thn idia perioxi
        // bres thn perioxh pou dialekse o paikths se ayton ton gyro
        //elegze an h perioxh exei plakidia
        // trabikse mexri 2 plakidia apo thn perioxh kai vale ta ston paikth

        // ta abilities tha ektelestoun apo ton controller
    }
    
}
