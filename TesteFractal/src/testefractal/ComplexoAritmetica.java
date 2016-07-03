package testefractal;

public class ComplexoAritmetica {  
    public static Complexo adiciona(Complexo primeiro, Complexo segundo) {
        return new Complexo(primeiro.getReal() + segundo.getReal(),
                      primeiro.getImaginario() + segundo.getImaginario());
    }

    public static Complexo subtrai(Complexo primeiro, Complexo segundo) {
        return new Complexo(primeiro.getReal() - segundo.getReal(),
                       primeiro.getImaginario() - segundo.getImaginario());
    }
	
	// return abs/modulus/magnitude and angle/phase/argument
    public static double abs(Complexo a)   { return Math.hypot(a.getReal(), a.getImaginario()); }  // Math.sqrt(getReal()*getReal() + getImaginario()*getImaginario())
    public double phase(Complexo a) { return Math.atan2(a.getImaginario(), a.getReal()); }  // between -pi and pi

    // return a new Complexo object whose value is (this * b)
    public static Complexo multiplica(Complexo primeiro, Complexo segundo) {
        double real = primeiro.getReal() * segundo.getReal() - primeiro.getImaginario() * segundo.getImaginario();
        double imag = primeiro.getReal() * segundo.getImaginario() + primeiro.getImaginario() * segundo.getReal();
        return new Complexo(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    public static Complexo multiplica(Complexo a, double alpha) {
        return new Complexo(alpha * a.getReal(), alpha * a.getImaginario());
    }

    // return a new Complexo object whose value is the conjugate of this
    public static Complexo conjugate(Complexo a) {  return new Complexo(a.getReal(), -a.getImaginario()); }

    // return a new Complexo object whose value is the reciprocal of this
    public static Complexo reciproco(Complexo a) {
        double scale = a.getReal()*a.getReal() + a.getImaginario()*a.getImaginario();
        return new Complexo(a.getReal() / scale, -a.getImaginario() / scale);
    }

    // return a / b
    public static Complexo divide(Complexo primeiro, Complexo segundo) {
        return multiplica(primeiro, reciproco(segundo));
    }

    // return a new Complexo object whose value is the Complexo exponential of this
    public static Complexo exp(Complexo a) {
        return new Complexo(Math.exp(a.getReal()) * Math.cos(a.getImaginario()), Math.exp(a.getReal()) * Math.sin(a.getImaginario()));
    }

    // return a new Complexo object whose value is the Complexo sine of this
    public static Complexo sin(Complexo a) {
        return new Complexo(Math.sin(a.getReal()) * Math.cosh(a.getImaginario()), Math.cos(a.getReal()) * Math.sinh(a.getImaginario()));
    }

    // return a new Complexo object whose value is the Complexo cosine of this
    public static Complexo cos(Complexo a) {
        return new Complexo(Math.cos(a.getReal()) * Math.cosh(a.getImaginario()), -Math.sin(a.getReal()) * Math.sinh(a.getImaginario()));
    }

    // return a new Complexo object whose value is the Complexo tangent of this
    public static Complexo tan(Complexo a) {
        return divide(sin(a), cos(a));
    }
}
