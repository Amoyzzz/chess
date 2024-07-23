public class Runner {
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println(game);
        // Scanner s = new Scanner(System.in);
        // String move = s.nextLine();
        game.makeMove("e2 e4");
        System.out.println(game);
    }
}