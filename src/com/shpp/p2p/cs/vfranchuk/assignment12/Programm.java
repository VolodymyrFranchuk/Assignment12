package com.shpp.p2p.cs.vfranchuk.assignment12;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Programm {

    private JFrame frame;
    private JPanel panel;
    private GridBagConstraints gridBagConstraints;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    public Programm(){
        frame = new JFrame("Assignment 12 Part 1");
        panel = new JPanel(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        frame.add(panel, BorderLayout.CENTER);
    }


    public void init(){
        JLabel label = new JLabel("Silhouettes count: ");
        frame.add(label, BorderLayout.NORTH);
        frame.setSize(WIDTH,HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void start(){
        FindSilhouettes findSilhouettes;
        try {
            // Load the image
            BufferedImage image = ImageIO.read(new File("assets/test2.png"));
            findSilhouettes = new FindSilhouettes(image);
            findSilhouettes.find();
            System.out.println(findSilhouettes.getSilhouettesCount());

            Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(imageIcon);
            JLabel imageLabel2 = new JLabel(imageIcon);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            panel.add(imageLabel, gridBagConstraints);

            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            panel.add(imageLabel2, gridBagConstraints);
            frame.revalidate();
            frame.repaint();
//            frame.pack();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
