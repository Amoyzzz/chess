import backstage.*;
import backstage.move.Move;
import java.util.*;
public class UCI {
    static String ENGINENAME="Athena v1_Fabiano";

    public static void uciCommunication(Board board) {
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
                inputUCINewGame(board);
            }
            else if (inputString.startsWith("position"))
            {
                inputPosition(inputString, board);
            }
            else if ("go".equals(inputString))
            {
                inputGo(board);
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
        if (input.contains("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            //make each of the moves
        }
    }
    public static void inputGo(Board board) {
        //search for best move
        Move bestMove = Athena.bestMove(board);
        System.out.println(bestMove.toString());
    }
}