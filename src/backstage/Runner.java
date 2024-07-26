package backstage;
public class Runner {
    public static void main(String[] args) {
        Board board = new Board();
        board.doMove("e4");
        System.out.println(board);
    }
}
