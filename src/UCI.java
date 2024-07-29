import backstage.*;
import backstage.move.Move;
import java.util.*;
public class UCI {
    static String ENGINENAME="Athena v1_Fabiano";
    static Athena a = new Athena();
    public UCI(){
        uciCommunication();
    }
    public static void uciCommunication() {
        Scanner input = new Scanner(System.in);
        while (true){
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
            else if (inputString.contains("go"))
            {
                inputGo(a.getBoard());
            }
            else if("quit".equals(inputString)){
                System.exit(0);
            }
        }
    }
    public static void inputUCI() {
        System.out.println("id name "+ENGINENAME);
        System.out.println("id author RedHatHackers");
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
        if (input.contains("startpos") && !input.contains("moves")) {
            input=input.substring(9);
            board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
        else if (input.contains("fen")) {
            input=input.substring(4);
            board.loadFromFen(input);
        }
        if (input.startsWith("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            input = input.trim();
            input = input.substring(input.lastIndexOf(" ") + 1);
            //System.out.println(" EGIUAHFUIASHFUIFAHUIHFE " + input);
            board.doMove(input);
        }
    }
    public static void inputGo(Board board) {
        //search for best move
        Move bestMove = Athena.bestMove(board);
        board.doMove(bestMove);
        System.out.println("bestmove " + bestMove.toString());
    }
}