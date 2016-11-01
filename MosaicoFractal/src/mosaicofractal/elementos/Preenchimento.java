package mosaicofractal.elementos;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Preenchimento {
    private boolean is_textura = false;
    private Color cor = null;
    private BufferedImage textura = null;
    
    public Preenchimento(Color cor){
        this.cor = cor;
    }
    
    public Preenchimento (BufferedImage textura){
        is_textura = true;
        this.textura = textura;
    }
    
    public Preenchimento (Preenchimento copia){
        this.is_textura = copia.is_textura;
        this.cor = copia.cor;
        this.textura = copia.textura;
    }
    
    public boolean isTextura(){
        return is_textura;
    }
    
    public BufferedImage getTextura(){
        return textura;
    }
    
    public Color getCor(){
        return cor;
    }
}
