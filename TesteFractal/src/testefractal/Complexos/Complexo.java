package testefractal.Complexos;

public class Complexo {
    private double real;
    private double imaginario;

    public Complexo(){
        this( 0.0, 0.0 );
    }

    public Complexo( double r, double i ){
        real = r;
        imaginario = i;
    }

    public double getReal() {
        return this.real;
    }

    public double getImaginario() {
        return this.imaginario;
    }
    
	@Override
    public String toString() {
        if (imaginario == 0) return real + "";
        if (real == 0) return imaginario + "i";
        if (imaginario <  0) return real + " - " + (-imaginario) + "i";
        return real + " + " + imaginario + "i";
    }
}
