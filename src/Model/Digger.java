package Model;

public class Digger extends Character {


    /**
     * constructor
     * @post dhmiourgeitai o xarakthras Digger me to onoma tou
     */
    public Digger() {
        super("Digger");
    }

    /**
     * ylopoihsh ths ikanothtas tou Ekskafea.
     * @pre o paikths prepei na exei hdh epilexei mia perioxi
     * @post o paikths pairnei alla 2 plakidia apo thn IDIA perioxi
     */
    @Override
    public void ability() {// again ola auta ginontai apo ton controller
        setUsed(true);


        // fash b - logikh gia draw 2 plakidiwn apo thn idia perioxi
        // bres thn perioxh pou dialekse o paikths se ayton ton gyro
        //elegze an h perioxh exei plakidia
        // trabikse mexri 2 plakidia apo thn perioxh kai vale ta ston paikth

        // ta abilities tha ektelestoun apo ton controller
    }
    
}
