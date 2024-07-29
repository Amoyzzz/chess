import backstage.*;
import backstage.move.Move;
import java.util.*;

public class UCI {
    static String ENGINENAME = "Athena v1_Fabiano";
    private static Athena a;
    private static Board board;

    public UCI() {
        uciCommunication();
    }

    public static void uciCommunication() {
        Scanner input = new Scanner(System.in);
        while (true) {
            String inputString = input.nextLine();
            if ("uci".equals(inputString)) {
                inputUCI();
            } else if (inputString.startsWith("setoption")) {
                inputSetOption(inputString);
            } else if ("isready".equals(inputString)) {
                inputIsReady();
            } else if ("ucinewgame".equals(inputString)) {
                inputUCINewGame();
            } else if (inputString.startsWith("position")) {
                inputPosition(inputString);
            } else if (inputString.contains("go")) {
                inputGo();
            } else if ("quit".equals(inputString)) {
                System.exit(0);
            }
        }
    }

    public static void inputUCI() {
        System.out.println("id name " + ENGINENAME);
        System.out.println("id author RedHatHackers");
        // options go here
        System.out.println("uciok");
    }

    public static void inputSetOption(String inputString) {
        // set options
    }

    public static void inputIsReady() {
        board = new Board();
        a = new Athena();
        System.out.println("readyok");
    }

    public static void inputUCINewGame() {

        board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public static void inputPosition(String input) {
        input = input.substring(9).concat(" "); // Remove "position " prefix and add a space at the end for easier
                                                // parsing
        if (input.contains("startpos")) {
            // Load the standard starting position
            board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            input = input.substring(9); // Remove "startpos " prefix
        } else if (input.contains("fen")) {
            // Load a position from a FEN string
            int fenEndIndex = input.indexOf("moves");
            String fen = (fenEndIndex == -1) ? input.substring(4).trim() : input.substring(4, fenEndIndex).trim();
            board.loadFromFen(fen);
            input = (fenEndIndex == -1) ? "" : input.substring(fenEndIndex);
        }
        if (input.startsWith("moves")) {
            input = input.substring(6).trim(); // Remove "moves " prefix and trim any leading/trailing spaces
            String[] moves = input.split(" ");
            for (String move : moves) {
                // Apply each move to the board
                board.doMove(move);
            }
        }
        // Print the updated board state
        System.out.println(board);
    }

    public static void inputGo() {
        // search for best move
        Move bestMove = Athena.bestMove(board);
        System.out.println(board);
        board.doMove(bestMove);
        // a.setBoard(board);
        System.out.println("bestmove " + bestMove.toString());

        System.out.println(board);
    }
}