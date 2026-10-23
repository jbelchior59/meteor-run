package files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Shape {
    private BufferedImage img;
    private String name;

    public Shape(String name){
        this.name = name;
        getImage();
    }

    public BufferedImage img(){return this.img;}

    private void getImage(){
        String path;
        switch(name){
            case "asteroid" -> path = "src/resources/Images/asteroid.png";
            case "nave" -> path = "src/resources/Images/nave.png";
            case "nave_inimiga" -> path = "src/resources/Images/nave_inimiga.png";
            case "nave_boss" -> path = "src/resources/Images/nave_boss.png";
            case "nave_mae" -> path = "src/resources/Images/nave_mae.png";
            case "parede" -> path = "src/resources/Images/parede.png";
            case "shield" -> path = "src/resources/Images/shield.png";
            case "speed_misseis" -> path = "src/resources/Images/speed_misseis.png";
            case "speed_nave" -> path = "src/resources/Images/speed_nave.png";
            default -> throw new IllegalArgumentException("gameobject invalido");
        }
        try { img = ImageIO.read(new File(path)); }
        catch (IOException e){ throw new RuntimeException("nao encontrou o ficheiro: " + path, e); }
    }
}
