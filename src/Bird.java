public class Bird {
    private final double delayFactor;
    public int x, y;
    public double velocity;

    private static final int JUMP_HEIGHT = 4;

    public Bird(int x, int y, double initialVelocity, double delayFactor) {
        this.delayFactor = delayFactor;
        this.x = x;
        this.y = y;
        this.velocity = initialVelocity;
    }

    public void updatePosition() {
        if (y + (int) velocity * delayFactor > 0) {
            y += (int) (velocity * delayFactor);
        }
    }

    public void jump() {
        for (int i = 0; i < JUMP_HEIGHT * delayFactor && y - i >= 0; i++) {
            y -= (int) (i / delayFactor);
            velocity = -2 / delayFactor;  // Apply an upward velocity during jump
        }
    }

    public void clear(char[][] grid) {
        for (int i = y; i < y + 2; i++) {
            if (i >= 0 && i < grid.length) {
                for (int j = x; j < x + 2; j++) {
                    grid[i][j] = '.';
                }
            }
        }
    }

    public void draw(char[][] grid) {
        for (int i = y; i < y + 2 && i < grid.length; i++) {
            for (int j = x; j < x + 2 && j < grid[0].length; j++) {
                grid[i][j] = 'B';
            }
        }
    }

    public boolean isTouchingPipe(char[][] grid) {
        for (int i = y; i < y + 1; i++) {
            if (i >= 0 && i < grid.length) // check parts of bird if inside the grid
                for (int j = x; j < x + 1; j++) {
                    if (grid[i][j] == '█') {
                        return true;  // bird is touching a pipe
                    }
                }
        }
        return false;  // Bird is not touching any pipes
    }
}