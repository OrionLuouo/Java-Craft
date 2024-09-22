package OrionLuouo.Craft.gui.component.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ScalableImage extends JComponent {
    Image image;
    double x , y;

    public ScalableImage(){
        x = 1;
        y = 1;
    }

    public ScalableImage(File path){
        try {
            if((image = ImageIO.read(path)) == null)
                throw new RuntimeException("Null image");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.setSize(image.getWidth(null) , image.getHeight(null));
        x = 1;
        y = 1;
    }

    public ScalableImage(Image image){
        if(image == null)
            throw new RuntimeException("Null image");
        this.image = image;
        super.setSize(image.getWidth(null) , image.getHeight(null));
        x = 1;
        y = 1;
    }

    public ScalableImage(File path , double scaleX , double scaleY){
        try {
            if((image = ImageIO.read(path)) == null)
                throw new RuntimeException("Null image");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        x = scaleX;
        y = scaleY;
        super.setSize((int) (image.getWidth(null) * x) , (int) (image.getHeight(null) * y));
    }

    public ScalableImage(Image image , double scaleX , double scaleY){
        if(image == null)
            throw new RuntimeException("Null image");
        this.image = image;
        x = scaleX;
        y = scaleY;
        super.setSize((int) (image.getWidth(null) * x) , (int) (image.getHeight(null) * y));
    }

    public void setImage(File path){
        try {
            if((image = ImageIO.read(path)) == null)
                throw new RuntimeException("Null image");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.setSize((int) (image.getWidth(null) * x) , (int) (image.getHeight(null) * y));
    }

    public void setImage(Image image){
        if(image == null)
            throw new RuntimeException("Null image");
        this.image = image;
        super.setSize((int) (image.getWidth(null) * x) , (int) (image.getHeight(null) * y));
    }

    public void setScale(double x,  double y){
        if(x < 0 || y < 0)
            throw new RuntimeException("Scale zoom can't be negative");
        this.x = x;
        this.y = y;
        super.setSize((int) (image.getWidth(null) * x) , (int) (image.getHeight(null) * y));
    }

    public void scale(double x , double y){
        if(x < 0 || y < 0)
            throw new RuntimeException("Scale zoom can't be negative");
        this.x *= x;
        this.y *= y;
        super.setSize((int) (image.getWidth(null) * this.x) , (int) (image.getHeight(null) * this.y));
    }

    @Override
    public void paintComponent(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.scale(x , y);
        graphics2D.drawImage(image , 0 , 0 , null);
    }

    @Override
    public void setSize(int x , int y){
    }
}
