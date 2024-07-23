public class Runner {
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println(game);
        // Scanner s = new Scanner(System.in);
        // String move = s.nextLine();
        game.makeMove("b1 c3");
    }
}