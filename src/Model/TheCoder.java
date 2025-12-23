package Model;

public class TheCoder extends Character {
    

    /**
     * constructor
     * @post dhmiourgeitai o xarakthras The Coder me to onoma tou
     */
    public TheCoder() {
        super("The Coder");
    }

    // prosthesa ena pedio hasplayedthecoder gia thn ylopoihsh sth fash b 
    /**
     * ylopoihsh ths ikanothtas tou Programmatisti
     * @post O paikths "kleidwnei" mia epilogh perioxis gia ton epomeno gyro
     * Shmeiwsh: H actual lhpsh twn plakidiwn tha ginei sth methodo nextTurn() tou paikth
     */
    @Override
    public void ability() {// ola auta ginontai apo ton controller

        //todo fash B - logikh gia "kleidwma" perioxis
        // o paikths epilegei mia perioxh
        //sto player balame ena boolean hasPlayedTheCoder 
        // ston epomeno gyro otan kaleitai to nextTurn() elegxoume an o paikths einai o TheCoder kai an to hasPlayedTheCoder einai true
        // an nai tote dinoume 2 extea plakidia ston paikth

        setUsed(true);
    }


}
