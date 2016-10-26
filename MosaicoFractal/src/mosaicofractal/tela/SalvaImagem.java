package mosaicofractal.tela;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SalvaImagem {
    private static int i = 0;
    
    public static void salvarPNG() {
        File file = new File("imagens");
        if (!file.exists()) {
            file.mkdirs();
        }
        
        Area.instancia().revalidate();
        Area.instancia().repaint();
        
        BufferedImage bImg = new BufferedImage(Area.instancia().getLargura(), Area.instancia().getAltura(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();

        Area.instancia().paintAll(cg);
        
        try {
            File salva = new File("./imagens/imagem_"+i+".png");
            while (salva.exists()){
                i++;
                salva = new File("./imagens/imagem_"+i+".png");
            }
            
            if (ImageIO.write(bImg, "png", new File("./imagens/imagem_"+i+".png"))){
            }
        }
        catch (IOException e) {}
    }
    
    public static void salvarSVG() {
        
    }
}
