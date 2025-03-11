package com.shpp.p2p.cs.vfranchuk.assignment12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * The `FindSilhouettes` class is responsible for detecting and counting silhouettes in a given image.
 * It provides methods to find silhouettes, count them,
 * and create a new image with red borders around the detected silhouettes.
 * It uses a depth-first search (DFS) algorithm to find connected components (silhouettes) in the image.
 * !!!!!! The code have setter method for minimum object size, use it to change the default value.
 */
public class FindSilhouettes {

    private final BufferedImage image;
    private int silhouettesCount = 0;
    private int minObjectSize = 130;
    private final Color backgroundColor;

    /**
     * Constructor
     * @param image - image to find silhouettes in
     */
    public FindSilhouettes(BufferedImage image) {
        this.image = image;
        backgroundColor = findBackgroundColor();
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
                        System.out.println("Silhouette " + (silhouettesCount + 1) + " size: " + objectSize);
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
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                imageMatrix[y][x] = !color.equals(backgroundColor);
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
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                imageMatrix[y][x] = !color.equals(backgroundColor); // true = silhouette, false = background
                newImage.setRGB(x, y, image.getRGB(x, y)); // Copy original image
            }
        }
        return imageMatrix;
    }

    /**
     * Finds the most frequent color in the image, assuming it is the background color.
     * It iterates through each pixel of the image, counts the frequency of each color,
     * and returns the color with the highest frequency.
     * @return - binary matrix where true represents silhouette pixels and false represents background pixels
     */
    private Color findBackgroundColor() {
        Map<Integer, Integer> colorFrequency = new HashMap<>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                colorFrequency.put(rgb, colorFrequency.getOrDefault(rgb, 0) + 1);
            }
        }
        return new Color(colorFrequency.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey());
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

            // Push adjacent pixels onto the stack
            stack.push(new int[]{y - 1, x}); // Up
            stack.push(new int[]{y + 1, x}); // Down
            stack.push(new int[]{y, x - 1}); // Left
            stack.push(new int[]{y, x + 1}); // Right
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

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int y = current[0];
            int x = current[1];

            if (y < 0 || x < 0 || y > height || x > width || visited[y][x] || !imageMatrix[y][x]) {
                continue;
            }

            visited[y][x] = true;
            // Check and mark adjacent background pixels as red
            markIfBackground(y - 1, x, width, height, imageMatrix, newImage); // Up
            markIfBackground(y + 1, x, width, height, imageMatrix, newImage); // Down
            markIfBackground(y, x - 1, width, height, imageMatrix, newImage); // Left
            markIfBackground(y, x + 1, width, height, imageMatrix, newImage); // Right

            // Push adjacent pixels onto the stack
            stack.push(new int[]{y - 1, x}); // Up
            stack.push(new int[]{y + 1, x}); // Down
            stack.push(new int[]{y, x - 1}); // Left
            stack.push(new int[]{y, x + 1}); // Right
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
        && newImage.getRGB(x, y) != Color.RED.getRGB()) {
            newImage.setRGB(x, y, Color.RED.getRGB());

        }
    }

}
