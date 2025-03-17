package com.shpp.p2p.cs.vfranchuk.assignment12;

import java.awt.*;

public abstract class Constants {

    /**
     * Width of the frame
     */
    public static final int WIDTH = 1000;
    /**
     * Height of the frame
     */
    public static final int HEIGHT = 1000;

    /**
     * Minimum object size to be considered a silhouette
     */
    public static final int MIN_OBJECT_SIZE = 290;

    public static final double STEP_SHOW_TRASH = 5;

    public static final double CLEAR_TRASH = 0.001514 / Math.pow(1.1, STEP_SHOW_TRASH);
    /**
     * Alpha threshold to consider a pixel as a silhouette
     */
    public static final int ALPHA_THRESHOLD = 30;
    /**
     * Average brightness threshold to consider a pixel as a silhouette
     */
    public static final double DEFAULT_BRIGHTNESS_THRESHOLD = 128.0;

    public static final double MAX_CHANGE = 80.0;

    public static final double STEP = (MAX_CHANGE * 2) / 255.0;
    /**
     * Default path to the image
     */
    public static final String DEFAULT_PATH = "test.jpg";
    /**
     * Directions to move in the image
     */
    public static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};


    /**
     * Debug mode to display the frame
     */
    public static final boolean DEBUG = false;
    /**
     * Color for marking the silhouettes
     */
    public static final int COLOR_FOR_MARKING = Color.GREEN.getRGB();
}
