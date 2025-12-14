package Model;

public class TheCoder extends Character {
    

    public TheCoder() {
        super("The Coder");
    }
    // prosthesa ena pedio hasplayedthecoder gia thn ylopoihsh sth fash b 
    /**
     * Ylopoihsh ths ikanothtas tou Programmatisti
     * @post O paikths "kleidwnei" mia epilogh perioxis gia ton epomeno gyro
     * Shmeiwsh: H actual lhpsh twn plakidiwn tha ginei sth methodo nextTurn() tou paikth
     */
    @Override
    public void ability() {

        //todo fash B - logikh gia "kleidwma" perioxis
        // o paikths epilegei mia perioxh
        //sto player balame ena boolean hasPlayedTheCoder 
        // ston epomeno gyro otan kaleitai to nextTurn() elegxoume an o paikths einai o TheCoder kai an to hasPlayedTheCoder einai true
        // an nai tote dinoume 2 extea plakidia ston paikth

        setUsed(true);
    }


}
