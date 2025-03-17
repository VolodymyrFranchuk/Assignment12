package com.shpp.p2p.cs.vfranchuk.assignment12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

import static com.shpp.p2p.cs.vfranchuk.assignment12.Constants.*;

/**
 * The `FindSilhouettes` class is responsible for detecting and counting silhouettes in a given image.
 * It provides methods to find silhouettes, count them,
 * and create a new image with red borders around the detected silhouettes.
 * It uses a depth-first search (DFS) algorithm to find connected components (silhouettes) in the image.
 */
public class FindSilhouettes {

    /**
     * The image to find silhouettes in.
     */
    private final BufferedImage image;
    /**
     * The number of silhouettes found.
     */
    private int silhouettesCount = 0;
    /**
     * Binary matrix representing the image where true represents silhouette pixels and false represents background pixels.
     */
    private final boolean[][] imageMatrix;

    /**
     * Constructor
     * @param image - image to find silhouettes in
     */
    public FindSilhouettes(BufferedImage image) {
        this.image = image;
        imageMatrix = new boolean[image.getHeight()][image.getWidth()];
    }

    /**
     * Getter for silhouettes count
     * @return - number of silhouettes found
     */
    public int getSilhouettesCount() {
        return silhouettesCount;
    }

    /**
     * Getter for the image
     * @return - image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Getter for the image matrix
     * @return - binary matrix representing the image
     */
    public boolean[][] getImageMatrix() {
        return imageMatrix;
    }

    /**
     * Finds silhouettes in the image and counts them.
     * It iterates through each pixel of the image, performs a depth-first search (DFS)
     * to find connected components (silhouettes), and counts them if they are larger than the minimum object size.
     */
    public void find(){
        int width = image.getWidth();
        int height = image.getHeight();
        makeImageMatrix(width, height);
        boolean[][] visited = new boolean[height][width];

        double minObjectSize = (width * height) * CLEAR_TRASH;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imageMatrix[y][x] && !visited[y][x]) {
                    int objectSize = dfs(y, x, width, height, visited);
                    if (objectSize >= minObjectSize) {
                        silhouettesCount++;
                    }
                }
            }
        }
    }

    /**
     * Creates a binary matrix representing the image.
     * @param width - width of the image
     * @param height - height of the image
     */
    private void makeImageMatrix(int width, int height){
        double averageBrightness = getAverageBrightness(image);
        // k is a coefficient to adjust the threshold
        double k = 1.0 + (1.0 - averageBrightness / 255);

        // threshold to consider a pixel as a silhouette
        double brightnessThreshold = (MAX_CHANGE - STEP * averageBrightness) + DEFAULT_BRIGHTNESS_THRESHOLD;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double gray = rgbToGrayscale(image.getRGB(x, y));
                // if the pixel is brighter than the average brightness, it is a silhouette pixel
                imageMatrix[y][x] = Math.abs(gray - averageBrightness) * k > brightnessThreshold;
            }
        }
    }

    /**
     * Calculates the average brightness of the image.
     * @param image - image
     * @return - average brightness
     */
    private double getAverageBrightness(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();
        double sumOfPixelsGrayscale = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                sumOfPixelsGrayscale += rgbToGrayscale(image.getRGB(x, y));
            }
        }
        return  sumOfPixelsGrayscale / (width * height);

    }
    /**
     * reference: <a href="https://en.wikipedia.org/wiki/Grayscale">wiki</a> ,
     * <a href="https://stackoverflow.com/questions/17615963/standard-rgb-to-grayscale-conversion">stackOverflow</a>
     * Converts RGB color to grayscale using formula from the wiki.
     * It also takes into account the alpha channel.
     * @param rgb - RGB color
     * @return - grayscale color
     */
    private double rgbToGrayscale(int rgb) {

        Color color = new Color(rgb, true);

        double alpha = color.getAlpha();
        double red, green, blue;
        if (alpha < ALPHA_THRESHOLD) {
            red = 1.0;
            green = 1.0;
            blue = 1.0;
        } else{
            red = color.getRed() / 255.0;
            green = color.getGreen() / 255.0;
            blue = color.getBlue() / 255.0;
        }

        double linear = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
        double grayscale;
        if (linear <= 0.0031308) {
            grayscale = linear * 12.92 * 255;
        } else {
            grayscale = (1.055 * Math.pow(linear, 1.0 / 2.4) - 0.055) * 255;
        }
        return grayscale;
    }

    /**
     * Depth-first search (DFS) algorithm to find connected components (silhouettes) in the image.
     * @param startY - starting y-coordinate
     * @param startX - starting x-coordinate
     * @param width - width of the image
     * @param height - height of the image
     * @param visited - matrix to keep track of visited pixels
     * @return - size of the connected component
     */
    private int dfs(int startY, int startX, int width, int height, boolean[][] visited) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startY, startX});
        int size = 0;

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int y = current[0];
            int x = current[1];

            if (y < 0 || x < 0 || y >= height || x >= width || visited[y][x] || !imageMatrix[y][x]) {
                continue;
            }

            visited[y][x] = true;
            size++;

            for (int[] dir : DIRECTIONS) {
                int newY = y + dir[0];
                int newX = x + dir[1];
                stack.push(new int[]{newY, newX});
            }
        }
        return size;
    }

}
