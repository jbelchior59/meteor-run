package files;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Shape {
    private BufferedImage img;
    private final String name;

    public Shape(String name) {
        this.name = name;
        getImage();
    }

    public BufferedImage img() { return this.img; }

    private void getImage() {
        String resource;
        switch (name) {
            case "asteroid" -> resource = "/resources/Images/asteroid.png";
            case "nave" -> resource = "/resources/Images/nave.png";
            case "nave_inimiga" -> resource = "/resources/Images/nave_inimiga.png";
            case "nave_boss" -> resource = "/resources/Images/nave_boss.png";
            case "nave_mae" -> resource = "/resources/Images/nave_mae.png";
            case "parede" -> resource = "/resources/Images/parede.png";
            case "shield" -> resource = "/resources/Images/shield.png";
            case "speed_misseis" -> resource = "/resources/Images/speed_misseis.png";
            case "speed_nave" -> resource = "/resources/Images/speed_nave.png";
            default -> throw new IllegalArgumentException("gameobject invalido");
        }

        try {
            img = ImageIO.read(Shape.class.getResource(resource));
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Nao encontrou o recurso: " + resource, e);
        }
    }
}
