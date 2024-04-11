
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.reader.LineReader;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    static final int GAME_TICK_DELAY = 50; // recommend to keep this at 50 for most systems.
    static final int GRID_WIDTH = 40;
    static final int GRID_HEIGHT = 20;
    static final double INITIAL_DIFFICULTY = 3.0; // doesn't really change much
    static boolean gameOver = false;
    static Grid gridController = initializeGrid();

    public static void main(String[] args) throws IOException, InterruptedException {
        NonBlockingReader reader = initTerminal();
        startInputThread(reader);
        mainLoop(reader);
    }

    private static NonBlockingReader initTerminal(){
        Terminal terminal = null;
        try {
            terminal = TerminalBuilder.terminal();
        } catch (IOException e) {
            System.err.println("Error initializing terminal: " + e.getMessage());
            System.exit(1);
        }

        terminal.enterRawMode();

        return terminal.reader();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void startInputThread(NonBlockingReader reader) {
        Thread inputThread = new Thread(() -> {
            while (!gameOver) {
                try {
                    reader.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                gridController.bird.jump();
            }
        });

        // Start the input thread
        inputThread.start();
    }

    private static void die(int height){
        String deathScreen = """
                M""MMMM""M MMP""\"""YMM M""MMMMM""M    M""\"""\"'YMM M""M MM""\"""\"""`M M""\"""\"'YMM\s
                M. `MM' .M M' .mmm. `M M  MMMMM  M    M  mmmm. `M M  M MM  mmmmmmmM M  mmmm. `M\s
                MM.    .MM M  MMMMM  M M  MMMMM  M    M  MMMMM  M M  M M`      MMMM M  MMMMM  M\s
                MMMb  dMMM M  MMMMM  M M  MMMMM  M    M  MMMMM  M M  M MM  MMMMMMMM M  MMMMM  M\s
                MMMM  MMMM M. `MMM' .M M  `MMM'  M    M  MMMM' .M M  M MM  MMMMMMMM M  MMMM' .M\s
                MMMM  MMMM MMb     dMM Mb       dM    M       .MM M  M MM        .M M       .MM\s
                MMMMMMMMMM MMMMMMMMMMM MMMMMMMMMMM    MMMMMMMMMMM MMMM MMMMMMMMMMMM MMMMMMMMMMM\s
                """;
        System.out.print(deathScreen);

    }

    public static void mainLoop(NonBlockingReader reader) throws IOException, InterruptedException {
        double diff = INITIAL_DIFFICULTY;
        String gridString = gridController.getGridString();
        clearScreen();
        System.out.println("SCORE: " + "0" + " DIFF: " + diff);
        System.out.print(gridString);

        int[] res = gridController.update(diff);

        reader.read(); //wait for key input.
        gridController.bird.jump();



        while (!gameOver) {
            diff += 0.0025;
            gridString = gridController.getGridString();
            res = gridController.update(diff);
            if (res[1] == -1) {
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println(" R I P ");
                clearScreen();
                die(gridString.length());
                TimeUnit.SECONDS.sleep(5);
                gameOver = true;

            }
            clearScreen();
            System.out.println("SCORE: " + res[0] + " DIFF: " + diff);
            System.out.print(gridString);
            TimeUnit.MILLISECONDS.sleep(GAME_TICK_DELAY);
        }
    }

    private static Grid initializeGrid() {
        return new Grid(GRID_WIDTH, GRID_HEIGHT, GAME_TICK_DELAY);
    }


}
