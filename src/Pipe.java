import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pipe {
    private int top_height;
    private int bottom_height;
    private double x;
    private static final char PIPE_CHAR = '█';

    public boolean passedBird = false;
    public static int PIPE_WIDTH = 3;

    public Pipe(double x, int top_height, int bottom_height) {
        this.x = x;
        this.top_height = top_height;
        this.bottom_height = bottom_height;
    }



    public double getX() {
        return x;
    }

    public void move(double delay) {
        x -= 1*delay;
    }

    public static Pipe spawnRandomPipe(int maxWidth, int maxHeight, int diff) {
        Random random = new Random();
        if (diff >= 5) diff = 5;
        int minGap = 4;  // Set your desired minimum gap here
        int minTopHeight = 1;  // Set your desired minimum top height here

        int gap = random.nextInt(5) + minGap + (5-diff);  // Ensure the gap is at least minGap
        int top_height = random.nextInt(maxHeight - minTopHeight - gap) + minTopHeight;  // Ensure top_height is at least minTopHeight
        int bottom_height = maxHeight - top_height - gap;
        int pipeX = maxWidth - 1;
        return new Pipe(pipeX, top_height, bottom_height);
    }

    public char[][] getPipeRepresentation(char[][] grid) {
        // Draw the top part of the pipe at the new position
        for (int i = 0; i < top_height; i++) {
            for (int j = (int) x; j < (int) x + 3; j++) {
                if (j >= 0 && j < grid[0].length) {
                    grid[i][j] = PIPE_CHAR;
                }
            }
        }

        // Draw the bottom part of the pipe at the new position
        for (int i = grid.length - bottom_height; i < grid.length; i++) {
            for (int j = (int) x; j < (int) x + 3; j++) {
                if (j >= 0 && j < grid[0].length) {
                    grid[i][j] = (char) PIPE_CHAR;
                }
            }
        }

        return grid;
    }



}
