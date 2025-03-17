package com.shpp.p2p.cs.vfranchuk.assignment12;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The `Programm` class is responsible for creating a frame
 * and displaying the original image and the image with red borders around the detected silhouettes.
 */
public class Programm {

    // The frame to display the images
    private final JFrame frame;
    // The panel to add the images to
    private final JPanel panel;
    // The constraints for the panel
    private final GridBagConstraints gridBagConstraints;

    // The image to display
    private final BufferedImage image;
    // The object to find silhouettes
    private final FindSilhouettes silhouettes;

    /**
     * Constructor initializes the frame and panel.
     * Adds the panel to the frame.
     */
    public Programm(BufferedImage image, FindSilhouettes silhouettes) {
        this.image = image;
        this.silhouettes = silhouettes;
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
    public Programm initFrame(){
        JLabel label = new JLabel("Silhouettes count: " + silhouettes.getSilhouettesCount());
        frame.add(label, BorderLayout.NORTH);
        frame.setSize(Constants.WIDTH,Constants.HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return this;
    }

    /**
     * Draws the original image and the image with red borders around the detected silhouettes.
     * Scales the images, creates ImageIcons, and adds them to the panel.
     */
    public void drawImage(){
        Image scaledImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);

        Image scaledImage2 = new MarkingSilhouettes(silhouettes).createImageWithRedBorder()
                .getScaledInstance(400, 300, Image.SCALE_SMOOTH);
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
