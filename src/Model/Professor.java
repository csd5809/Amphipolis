package Model;

public class Professor extends Character {

    /**
     * constructor
     * @post dhmiourgeitai o xarakthras Professor me to onoma tou
     */
    public Professor() {
        super("Professor");
    }

    /**
     * ylopoihsh ths ikanothtas tou Kathghth
     * @post o paikths lambanei 1 plakidio apo tis 3 perioxes pou den dialekse arxika
     */
    @Override
    public void ability() {// pali ola auta ginontai apo ton controller

        // todo fash B - logikh gia epilogh 1 plakidiou apo tis 3 perioxes pou den dialekse arxika
        // bres tin perioxh pou dialekse o paikths ston trexonta gyro
        //diatrexe oles tis ypoloipes 3 perioxes tou board
        // apo thn kathe mia pare ena plakidio an yparxei kai baleto ston player
        setUsed(true);
    }
    
}
