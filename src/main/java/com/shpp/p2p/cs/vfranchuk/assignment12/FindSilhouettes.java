package com.shpp.p2p.cs.vfranchuk.assignment12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * The `FindSilhouettes` class is responsible for detecting and counting silhouettes in a given image.
 * It provides methods to find silhouettes, count them,
 * and create a new image with red borders around the detected silhouettes.
 * It uses a depth-first search (DFS) algorithm to find connected components (silhouettes) in the image.
 * !!!!!! The code have setter method for minimum object size, use it to change the default value or change it in the code.
 */
public class FindSilhouettes {

    private final BufferedImage image;
    private int silhouettesCount = 0;
    private int minObjectSize = 230;

    /**
     * Constructor
     * @param image - image to find silhouettes in
     */
    public FindSilhouettes(BufferedImage image) {
        this.image = image;
    }

    /**
     * Getter for silhouettes count
     * @return - number of silhouettes found
     */
    public int getSilhouettesCount() {
        return silhouettesCount;
    }

    /**
     * Setter for minimum object size
     * @param minObjectSize - minimum object size
     */
    public void setMinObjectSize(int minObjectSize) {
        this.minObjectSize = minObjectSize;
    }

    /**
     * Finds silhouettes in the image and counts them.
     * It iterates through each pixel of the image, performs a depth-first search (DFS)
     * to find connected components (silhouettes), and counts them if they are larger than the minimum object size.
     */
    public void find(){
        int width = image.getWidth();
        int height = image.getHeight();
        boolean[][] imageMatrix = makeImageMatrix(width, height);
        boolean[][] visited = new boolean[height][width];


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imageMatrix[y][x] && !visited[y][x]) {
                    int objectSize = dfs(y, x, width, height, visited, imageMatrix);
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
     * @return - binary matrix where true represents silhouette pixels and false represents background pixels
     */
    private boolean[][] makeImageMatrix(int width, int height){
        boolean[][] imageMatrix = new boolean[height][width];

        double averageBrightness = getAverageBrightness(image);
        double k = 1.0 + (1.0 - averageBrightness / 255);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double gray = rgbToGrayscale(image.getRGB(x, y));
                imageMatrix[y][x] = Math.abs(gray - averageBrightness) * k > 128;
            }
        }
        return imageMatrix;
    }

    /**
     * Creates a binary matrix representing the image.
     * @param width - width of the image
     * @param height - height of the image
     * @param newImage - image to copy the original image to
     * @return - binary matrix where true represents silhouette pixels and false represents background pixels
     */
    private boolean[][] makeImageMatrix(int width, int height, BufferedImage newImage){
        boolean[][] imageMatrix = new boolean[height][width];


        double averageBrightness = getAverageBrightness(image);
        double k = 1.0 + (1.0 - averageBrightness / 255);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                int rgb = image.getRGB(x, y);
                double gray = rgbToGrayscale(rgb);
                imageMatrix[y][x] = Math.abs(gray - averageBrightness) * k > 128;
                newImage.setRGB(x, y, rgb); // Copy original image
            }
        }
        return imageMatrix;
    }

    private double getAverageBrightness(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();
        double sum = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                sum += rgbToGrayscale(image.getRGB(x, y));
            }
        }
        return  sum / (width * height);

    }
    /**
     * reference: <a href="https://en.wikipedia.org/wiki/Grayscale">wiki</a> ,
     * <a href="https://stackoverflow.com/questions/17615963/standard-rgb-to-grayscale-conversion">stackOverflow</a>
     * @param rgb - RGB color
     * @return - grayscale color
     */
    private double rgbToGrayscale(int rgb) {

        Color color = new Color(rgb);
        double red = color.getRed() / 255.0;
        double green = color.getGreen() / 255.0;
        double blue = color.getBlue() / 255.0;
        double linear = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
        if (linear <= 0.0031308) {
            return  (linear * 12.92 * 255);
        } else {
            return  ((1.055 * Math.pow(linear, 1.0 / 2.4) - 0.055) * 255);
        }
    }

    /**
     * Depth-first search (DFS) algorithm to find connected components (silhouettes) in the image.
     * @param startY - starting y-coordinate
     * @param startX - starting x-coordinate
     * @param width - width of the image
     * @param height - height of the image
     * @param visited - matrix to keep track of visited pixels
     * @param imageMatrix - binary matrix representing the image
     * @return - size of the connected component
     */
    private int dfs(int startY, int startX, int width, int height,
                           boolean[][] visited, boolean[][] imageMatrix) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startY, startX});
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
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

            for (int[] dir : directions) {
                int newY = y + dir[0];
                int newX = x + dir[1];
                stack.push(new int[]{newY, newX});
            }
        }

        return size;
    }

    /**
     * Creates a new image with red borders around silhouettes.
     * It iterates through each pixel of the image, performs a depth-first search (DFS)
     * to find connected components (silhouettes), and marks the border of each silhouette with red.
     * @return - new image with red borders around silhouettes
     */
    public BufferedImage createImageWithRedBorder() {
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage newImage = new BufferedImage(width, height, image.getType());
        boolean[][] imageMatrix = makeImageMatrix(width, height, newImage);
        boolean[][] visited = new boolean[height][width];
        boolean[][] tempVisited = new boolean[visited.length][visited[0].length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int objectSize = dfs(y, x, width, height, visited, imageMatrix);
                if (objectSize >= minObjectSize) {
                    markRedBorder(y, x, width, height, tempVisited, imageMatrix, newImage);
                }
            }
        }

        return newImage;
    }

    /**
     * Depth-first search (DFS) algorithm to find connected components (silhouettes) in the image.
     * It marks the border of each silhouette with red.
     * @param startY - starting y-coordinate
     * @param startX - starting x-coordinate
     * @param width - width of the image
     * @param height - height of the image
     * @param visited - matrix to keep track of visited pixels
     * @param imageMatrix - binary matrix representing the image
     * @param newImage - image to mark the border of each silhouette with red
     */
    private void markRedBorder(int startY, int startX, int width, int height,
                               boolean[][] visited, boolean[][] imageMatrix, BufferedImage newImage) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startY, startX});
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int y = current[0];
            int x = current[1];

            if (y < 0 || x < 0 || y >= height || x >= width || visited[y][x] || !imageMatrix[y][x]) {
                continue;
            }

            visited[y][x] = true;

            for (int[] dir : directions) {
                int newY = y + dir[0];
                int newX = x + dir[1];
                markIfBackground(newY, newX, width, height, imageMatrix, newImage);
                stack.push(new int[]{newY, newX});
            }
        }
    }

    /**
     * Marks the pixel as red if it is a background pixel.
     * @param y - y-coordinate
     * @param x - x-coordinate
     * @param width - width of the image
     * @param height - height of the image
     * @param imageMatrix - binary matrix representing the image
     * @param newImage - image to mark the border of each silhouette with red
     */
    private void markIfBackground(int y, int x, int width, int height, boolean[][] imageMatrix,
                                  BufferedImage newImage) {

        if (y > 0 && x > 0 && y < height && x < width && !imageMatrix[y][x]
        && newImage.getRGB(x, y) != Color.GREEN.getRGB()) {
            newImage.setRGB(x, y, Color.GREEN.getRGB());

        }
    }

}
