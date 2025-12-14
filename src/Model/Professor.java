package Model;

public class Professor extends Character {

    public Professor() {
        super("Professor");
    }

    /**
     * Ylopoihsh ths ikanothtas tou Kathghth
     * @post O paikths lambanei 1 plakidio apo tis 3 perioxes pou den dialekse arxika
     */
    @Override
    public void ability() {

        // todo fash B - logikh gia epilogh 1 plakidiou apo tis 3 perioxes pou den dialekse arxika
        // bres tin perioxh pou dialekse o paikths ston trexonta gyro
        //diatrexe oles tis ypoloipes 3 perioxes tou board
        // apo thn kathe mia pare ena plakidio an yparxei kai baleto ston player
        setUsed(true);
    }
    
}
