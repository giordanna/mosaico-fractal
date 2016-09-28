package testefractal.dependencias;

public class Complexo {
    public double real;
    public double imaginaria;
    
    public Complexo(double real, double imaginaria){
        this.real = real;
        this.imaginaria = imaginaria;
    }
    
    public static Complexo conjugado(Complexo c1){
        return new Complexo(c1.real, -c1.imaginaria);
    }
    
    public static Complexo soma(Complexo c1, Complexo c2){
        return new Complexo(c1.real + c2.real, c1.imaginaria + c2.imaginaria);
    }
    
    public static Complexo subtracao(Complexo c1, Complexo c2){
        return new Complexo(c1.real - c2.real, c1.imaginaria - c2.imaginaria);
    }
    
    public static Complexo multiplicacao(Complexo c1, Complexo c2){
        return new Complexo(c1.real * c2.real - c1.imaginaria * c2.imaginaria,
                c1.real * c2.imaginaria + c1.imaginaria * c2.real);
    }
    
    public static Complexo raizq(Complexo c1){
        return new Complexo(
                Math.sqrt(Math.sqrt(c1.real * c1.real + c1.imaginaria * c1.imaginaria)) * Math.cos(Math.atan2(c1.imaginaria, c1.real)/2),
                Math.sqrt(Math.sqrt(c1.real * c1.real + c1.imaginaria * c1.imaginaria)) * Math.sin(Math.atan2(c1.imaginaria, c1.real)/2)
        ); 
    }
}
