package com.shpp.p2p.cs.vfranchuk.assignment12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Assignment12Part1 {

    private static BufferedImage image;
    private static FindSilhouettes silhouettes;

    public static void main(String[] args) {
        try {
            if (args.length > 0){
                image = ImageIO.read(new File(args[0]));
            } else{
                image = ImageIO.read(new File(Constants.DEFAULT_PATH));
            }
            silhouettes = new FindSilhouettes(image);
            silhouettes.find();
            System.out.println(silhouettes.getSilhouettesCount());

        } catch (Exception e) {
            System.out.println("Error: Something went wrong");
        }
        // if DEBUG is true, then the frame will be displayed
        if (Constants.DEBUG) {
            debugFunction();
        }

    }

    private static void debugFunction() {
            try {
                Programm programm = new Programm(image, silhouettes);
                programm.initFrame().drawImage();
            } catch (Exception e) {
                System.out.println("Error: Cannot display the frame");
            }
    }


    /**
     * use for quick testing
     * @return the number of detected silhouettes
     */
    public int getSilhouettesCount(){
        return silhouettes.getSilhouettesCount();
    }
}