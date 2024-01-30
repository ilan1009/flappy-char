import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Grid {
    private final int width;
    private final int height;
    private char[][] grid;
    private List<Pipe> pipes;
    Bird bird;
    private int score = 0;
    private static final int MAX_VELOCITY = 5;
    private static final double GRAVITY = 1;
    private static double delayFactor;

    public Grid(int width, int height, int delay) {
        delayFactor = (double) delay / 100;


        this.width = width;
        this.height = height;
        this.grid = fillGrid(height, width);

        this.bird = new Bird(width / 4, height / 2, GRAVITY*delayFactor, delayFactor);
        this.pipes = new ArrayList<>();
        pipes.add(Pipe.spawnRandomPipe(width, height, 0));

    }


    static char[][] fillGrid(int rows, int cols) {
        char[][] grid = new char[rows][cols];
        char[] row = new char[cols];
        Arrays.fill(row, '⠀');
        grid[0] = row;
        for (int i = 1; i < rows; i++) {
            Arrays.fill(grid[i], '⠀');
        }
        return grid;
    }






    public String getGridString(){
        return Arrays.deepToString(this.grid)
                .replace(",", "")
                .replace("[", "\n")
                .replace("]", "") + " " + "/\\".repeat(width-1)
                .trim();
    }

    public int[] update(double diff) {
        boolean end = false;

        if (diff >= 5) diff = 5; // diff cant be harder than 5
        this.grid = fillGrid(height, width);

        bird.clear(grid);

        // Apply gravity
        if (bird.velocity < MAX_VELOCITY) {
            bird.velocity += GRAVITY;
        }

        bird.updatePosition();



        bird.draw(grid);


        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.move(delayFactor);

            // Check if a pipe is off the left side of the screen
            if (pipe.getX() + Pipe.PIPE_WIDTH < 0) {
                iterator.remove();  // Remove the pipe from the list
            } else {
                if (pipe.getX() < bird.x && !pipe.passedBird) {
                    score++;
                    pipe.passedBird = true;
                }
                this.grid = pipe.getPipeRepresentation(this.grid);
            }
        }

        // Check if a new pipe should be spawned
        if (pipes.get(pipes.size() - 1).getX() < width - 15 + (diff-2)*2) {
            pipes.add(Pipe.spawnRandomPipe(width, height, (int) diff));
        }

        if (bird.isTouchingPipe(grid) || bird.y > grid.length) {
            bird.draw(grid); // draw final frame again? IDK.

            return new int[]{score, -1};  // return a code to indicate collision
        }

        return new int[]{score, 0};
    }




}