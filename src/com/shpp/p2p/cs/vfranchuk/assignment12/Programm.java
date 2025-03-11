package com.shpp.p2p.cs.vfranchuk.assignment12;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The `Programm` class is responsible for creating a frame
 * and displaying the original image and the image with red borders around the detected silhouettes.
 */
public class Programm {

    private JFrame frame;
    private JPanel panel;
    private GridBagConstraints gridBagConstraints;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    private BufferedImage image;
    private FindSilhouettes silhouettes;

    /**
     * Constructor initializes the frame and panel.
     * Adds the panel to the frame.
     */
    public Programm(){
        frame = new JFrame("Assignment 12 Part 1");
        panel = new JPanel(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        frame.add(panel, BorderLayout.CENTER);
    }

    /**
     * Initializes the frame by setting its size, default close operation, and visibility.
     * Adds a label to display the silhouettes count and calls the drawImage method to display images.
     */
    public void initFrame(){
        JLabel label = new JLabel("Silhouettes count: ");
        frame.add(label, BorderLayout.NORTH);
        frame.setSize(WIDTH,HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        drawImage();
    }

    /**
     * Finds silhouettes in the image and displays the original image
     * and the image with red borders around the detected silhouettes.
     * @param path - path to the image
     */
    public void findSilhouettes(String path){
        try {
            // Load the image
            image = ImageIO.read(new File(path));
            silhouettes = new FindSilhouettes(image);
            silhouettes.find();
            System.out.println(silhouettes.getSilhouettesCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the original image and the image with red borders around the detected silhouettes.
     * Scales the images, creates ImageIcons, and adds them to the panel.
     */
    private void drawImage(){
        Image scaledImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        Image scaledImage2 = silhouettes.createImageWithRedBorder().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(scaledImage);
        ImageIcon imageIcon2 = new ImageIcon(scaledImage2);
        JLabel imageLabel = new JLabel(imageIcon);
        JLabel imageLabel2 = new JLabel(imageIcon2);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(imageLabel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel.add(imageLabel2, gridBagConstraints);
        frame.revalidate();
        frame.repaint();
    }
}
