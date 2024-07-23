import java.util.Scanner;
public class Runner {
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println(game);
        Scanner s = new Scanner(System.in);
        String move = s.nextLine();
        game.makeMove(move);
    }

}