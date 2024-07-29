import backstage.*;
import backstage.move.Move;
import java.util.*;
public class UCI {
    static String ENGINENAME="Athena v1_Fabiano";
    private static Board board;

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board board) {
        UCI.board = board;
    }

    public UCI() {
        this.board = new Board();
        Athena athena = new Athena();
    }

    public void uciCommunication() {
        Scanner input = new Scanner(System.in);
        while (true) {
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
                inputUCINewGame();
            }
            else if (inputString.startsWith("position"))
            {
                inputPosition(inputString);
            }
            else if ("go".equals(inputString))
            {
                inputGo();
            }
            else if (inputString.startsWith("move")){
                incomingMove(inputString);
            }
        }
    }
    private static void inputUCI() {
        System.out.println("Engine: " + ENGINENAME);
        //options go here
        System.out.println("uciok");
    }
    private static void inputSetOption(String inputString) {
        //set options
    }
    private static void inputIsReady() {
         System.out.println("readyok");
    }
    private static void inputUCINewGame() {
        board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
    private static void inputPosition(String input) {
        input = input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input=input.substring(9);
            board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        } else if (input.contains("fen")) {
            input=input.substring(4);
            board.loadFromFen(input);
            System.out.println(board);
        }
        if (input.contains("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            //make each of the moves
        }
    }
    private static void inputGo() {
        //search for best move
        Move bestMove = Athena.bestMove(board);
        board.doMove(bestMove);
        System.out.println(bestMove.toString());
    }
    private static void incomingMove(String input){
        input = input.substring(5);
        board.doMove(input.substring(2, 4));

        //move e4e5
    }
}