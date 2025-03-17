package com.shpp.p2p.cs.vfranchuk.assignment12;

import java.awt.image.BufferedImage;
import java.util.Stack;

import static com.shpp.p2p.cs.vfranchuk.assignment12.Constants.*;

public class MarkingSilhouettes{

    private final FindSilhouettes silhouettes;

    public MarkingSilhouettes(FindSilhouettes silhouettes) {
        this.silhouettes = silhouettes;
    }
    /**
     * Creates a new image with red borders around silhouettes.
     * It iterates through each pixel of the image, performs a depth-first search (DFS)
     * to find connected components (silhouettes), and marks the border of each silhouette with red.
     * @return - new image with red borders around silhouettes
     */
    public BufferedImage createImageWithRedBorder() {
        int height = silhouettes.getImage().getHeight();
        int width = silhouettes.getImage().getWidth();
        BufferedImage newImage = new BufferedImage(width, height, silhouettes.getImage().getType());
        makeCopyOfImage(width, height, newImage);
        boolean[][] visited = new boolean[height][width];
        boolean[][] tempVisited = new boolean[visited.length][visited[0].length];

        double minObjectSize = (width * height) * CLEAR_TRASH;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int objectSize = dfs(y, x, width, height, visited);
                if (objectSize >= minObjectSize) {
                    markRedBorder(y, x, width, height, tempVisited, newImage);
                }
            }
        }

        return newImage;
    }

    /**
     * Creates a binary matrix representing the image.
     * @param width - width of the image
     * @param height - height of the image
     * @param newImage - image to copy the original image to
     */
    private void makeCopyOfImage(int width, int height, BufferedImage newImage){
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                int rgb = silhouettes.getImage().getRGB(x, y);
                newImage.setRGB(x, y, rgb); // Copy original image
            }
        }
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

            if (y < 0 || x < 0 || y >= height || x >= width || visited[y][x] || !silhouettes.getImageMatrix()[y][x]) {
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

    /**
     * Depth-first search (DFS) algorithm to find connected components (silhouettes) in the image.
     * It marks the border of each silhouette with red.
     * @param startY - starting y-coordinate
     * @param startX - starting x-coordinate
     * @param width - width of the image
     * @param height - height of the image
     * @param visited - matrix to keep track of visited pixels
     * @param newImage - image to mark the border of each silhouette with red
     */
    private void markRedBorder(int startY, int startX, int width, int height,
                               boolean[][] visited, BufferedImage newImage) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startY, startX});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int y = current[0];
            int x = current[1];

            if (y < 0 || x < 0 || y >= height || x >= width || visited[y][x] || !silhouettes.getImageMatrix()[y][x]) {
                continue;
            }

            visited[y][x] = true;

            for (int[] dir : DIRECTIONS) {
                int newY = y + dir[0];
                int newX = x + dir[1];
                markIfBackground(newY, newX, width, height, newImage);
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
     * @param newImage - image to mark the border of each silhouette with red
     */
    private void markIfBackground(int y, int x, int width, int height, BufferedImage newImage) {

        if (y > 0 && x > 0 && y < height && x < width && !silhouettes.getImageMatrix()[y][x]
                && newImage.getRGB(x, y) != COLOR_FOR_MARKING) {
            newImage.setRGB(x, y, COLOR_FOR_MARKING);

        }
    }
}
