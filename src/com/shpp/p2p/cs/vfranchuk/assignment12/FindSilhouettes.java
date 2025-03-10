package com.shpp.p2p.cs.vfranchuk.assignment12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FindSilhouettes {

    private final BufferedImage image;
    private int silhouettesCount = 0;
    private static final int MIN_OBJECT_SIZE = 480;

    public FindSilhouettes(BufferedImage image) {
        this.image = image;
    }


    public void find(){
        int height = image.getHeight();
        int width = image.getWidth();
        int[][] imageMatrix = new int[height][width];
        boolean[][] visited = new boolean[height][width];

        Color backgroundColor = findBackgroundColor(image);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                imageMatrix[y][x] = color.equals(backgroundColor) ? 0 : 1; // 1 = silhouette (black), 0 = background (white)
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imageMatrix[y][x] == 1 && !visited[y][x]) {
                    int objectSize = dfs(y, x, width, height, visited, imageMatrix);
                    if (objectSize >= MIN_OBJECT_SIZE) {
                        System.out.println("Silhouette " + (silhouettesCount + 1) + " size: " + objectSize);
                        silhouettesCount++;
                    }
                }
            }
        }
    }

    public int getSilhouettesCount() {
        return silhouettesCount;
    }
    private static Color findBackgroundColor(BufferedImage image) {
        Map<Integer, Integer> colorFrequency = new HashMap<>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                colorFrequency.put(rgb, colorFrequency.getOrDefault(rgb, 0) + 1);
            }
        }

        int maxFrequency = 0;
        int backgroundColorRGB = 0;
        for (Map.Entry<Integer, Integer> entry : colorFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                backgroundColorRGB = entry.getKey();
            }
        }

        return new Color(backgroundColorRGB);
    }

    // DFS to mark connected black pixels
    private static int dfs(int startY, int startX, int width, int height,
                           boolean[][] visited, int[][] imageMatrix) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startY, startX});
        int size = 0;

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int y = current[0];
            int x = current[1];

            if (y < 0 || x < 0 || y >= height || x >= width || visited[y][x] || imageMatrix[y][x] == 0) {
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

}
