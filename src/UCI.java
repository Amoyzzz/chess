import backstage.*;
import backstage.move.Move;
import java.util.*;
public class UCI {
    static String ENGINENAME="Athena v1_Fabiano";

    public static void uciCommunication() {
        Athena a = new Athena();
        while (true){
            Scanner input = new Scanner(System.in);
            String inputString = input.nextLine();
            if ("uci".equals(inputString))
            {
                inputUCI();
            }
            else if (inputString.startsWith("setoption"))
            {
                inputSetOption(inputString);
            }
            else if ("isready".equals(inputString))
            {
                inputIsReady();
            }
            else if ("ucinewgame".equals(inputString))
            {
                inputUCINewGame(a.getBoard());
            }
            else if (inputString.startsWith("position"))
            {
                inputPosition(inputString, a.getBoard());
            }
            else if ("go".equals(inputString))
            {
                inputGo(a.getBoard());
            }
        }
    }
    public static void inputUCI() {
        System.out.println("Engine: "+ENGINENAME);
        //options go here
        System.out.println("uciok");
    }
    public static void inputSetOption(String inputString) {
        //set options
    }
    public static void inputIsReady() {
         System.out.println("readyok");
    }
    public static void inputUCINewGame(Board board) {
        board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
    public static void inputPosition(String input, Board board) {
        input=input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input=input.substring(9);
            board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
        else if (input.contains("fen")) {
            input=input.substring(4);
            board.loadFromFen(input);
        }
        if (input.startsWith("moves")) {
            
        }
    }
    public static void inputGo(Board board) {
        //search for best move
        Move bestMove = Athena.bestMove(board);
        board.doMove(bestMove);
        System.out.println(bestMove.toString());
    }
}