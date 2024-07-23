
import java.util.ArrayList;
public class Move {
    static ArrayList<Integer>[] knightMoves = new ArrayList[64];
    static ArrayList<Integer>[] kingMoves = new ArrayList[64];
    static ArrayList<Integer>[] rookMoves = new ArrayList[64];
    static ArrayList<Integer>[] bishopMoves = new ArrayList[64];
    static ArrayList<Integer>[] queenMoves = new ArrayList[64];
    static ArrayList<Integer>[] whitePawnMoves = new ArrayList[64];
    static ArrayList<Integer>[] blackPawnMoves = new ArrayList[64];
    static int[][] grid = new int[8][8];
    static int n = 8;
    

    public static ArrayList<Integer> knightMoves(int location) {
        return knightMoves[location];
    }
    public static ArrayList<Integer> kingMoves(int location) {
        return kingMoves[location];
    }
    public static ArrayList<Integer> queenMoves(int location) {
        return queenMoves[location];
    }
    public static ArrayList<Integer> rookMoves(int location) {
        return rookMoves[location];
    }
    public static ArrayList<Integer> bishopMoves(int location) {
        return bishopMoves[location];
    }
    public static ArrayList<Integer> pawnMoves(int location, String fen) { // 0 = white, 1 = black
        if(Character.isLowerCase(fen.charAt(0))){
            return blackPawnMoves[location];
        }
        return whitePawnMoves[location];
    }
    public static void init(){
        initGrid();
        initKnightMoves();
        initKingMoves();
        initRookMoves();
        initBishopMoves();
        initQueenMoves();
        initWhitePawnMoves();
        initBlackPawnMoves();
    }
    public static void initGrid(){
        int index = 0;
        for(int i = 7; i >= 0; i--){
            for(int j = 0; j < n; j++){
                grid[i][j] = index;
                index++;
            }
        }
        
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void initKnightMoves(){
        for(int i = 0; i < 64; i++){
            knightMoves[i] = new ArrayList<Integer>();
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i - 2 >= 0 && j - 1 >= 0){
                    knightMoves[grid[i][j]].add(grid[i - 2][j - 1]);
                }
                if(i - 2 >= 0 && j + 1 < n){
                    knightMoves[grid[i][j]].add(grid[i - 2][j + 1]);
                }
                if(i - 1 >= 0 && j - 2 >= 0){
                    knightMoves[grid[i][j]].add(grid[i - 1][j - 2]);
                }
                if(i - 1 >= 0 && j + 2 < n){
                    knightMoves[grid[i][j]].add(grid[i - 1][j + 2]);
                }
                if(i + 1 < n && j - 2 >= 0){
                    knightMoves[grid[i][j]].add(grid[i + 1][j - 2]);
                }
                if(i + 1 < n && j + 2 < n){
                    knightMoves[grid[i][j]].add(grid[i + 1][j + 2]);
                }
                if(i + 2 < n && j - 1 >= 0){
                    knightMoves[grid[i][j]].add(grid[i + 2][j - 1]);
                }
                if(i + 2 < n && j + 1 < n){
                    knightMoves[grid[i][j]].add(grid[i + 2][j + 1]);
                }
            }
        }
    }
    public static void initKingMoves(){
        for(int i = 0; i < 64; i++){
            kingMoves[i] = new ArrayList<Integer>();
        }
        int[] dir = {0, -1, 1};
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
               for(int move : dir){
                   for(int move2 : dir){
                       if(i + move >= 0 && i + move < n && j + move2 >= 0 && j + move2 < n && (move != 0 || move2 != 0)){
                           kingMoves[grid[i][j]].add(grid[i + move][j + move2]);
                       }
                   }
               } 
            }
        }
    }
    public static void initRookMoves(){
        for(int i = 0; i < 64; i++){
            rookMoves[i] = new ArrayList<Integer>();
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int move = 1; move < n; move++){
                    if(i + move >= 0 && i + move < n && j >= 0 && j < n){
                        rookMoves[grid[i][j]].add(grid[i + move][j]);
                    }
                    if(i >= 0 && i < n && j + move >= 0 && j + move < n){
                        rookMoves[grid[i][j]].add(grid[i][j + move]);
                    }
                    if(i >= 0 && i < n && j - move >= 0 && j - move < n){
                        rookMoves[grid[i][j]].add(grid[i][j - move]);
                    }
                    if(i - move >= 0 && i - move < n && j >= 0 && j < n){
                        rookMoves[grid[i][j]].add(grid[i - move][j]);
                    }
                }
            }
        }
    }
    public static void initBishopMoves(){
        for(int i = 0; i < 64; i++){
            bishopMoves[i] = new ArrayList<Integer>();
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                for(int move = 1; move < n; move++){
                    if(i + move >= 0 && i + move < n && j + move >= 0 && j + move < n){
                        bishopMoves[grid[i][j]].add(grid[i + move][j + move]);
                    }
                    if(i + move >= 0 && i + move < n && j - move >= 0 && j - move < n){ 
                        bishopMoves[grid[i][j]].add(grid[i + move][j - move]);
                    }
                    if(i - move >= 0 && i - move < n && j + move >= 0 && j + move < n){
                        bishopMoves[grid[i][j]].add(grid[i - move][j + move]);
                    }
                    if(i - move >= 0 && i - move < n && j - move >= 0 && j - move < n){
                        bishopMoves[grid[i][j]].add(grid[i - move][j - move]);
                    }
                }
            }
        }
    }
    public static void initQueenMoves(){
        for(int i = 0; i < 64; i++){
            queenMoves[i] = new ArrayList<Integer>();
        }
        for(int i = 0; i < 64; i++){
            for(int a : rookMoves[i]){
                queenMoves[i].add(a);
            }
            for(int a : bishopMoves[i]){
                queenMoves[i].add(a);
            }
        }
    }
    public static void initWhitePawnMoves(){
        for(int i = 0; i < 64; i++){
            whitePawnMoves[i] = new ArrayList<Integer>();
        }
        for(int i = 6; i>= 1; i--){
            for(int j = 0; j < n; j++){
                if(i == 6){
                    whitePawnMoves[grid[i][j]].add(grid[i - 1][j]);
                    whitePawnMoves[grid[i][j]].add(grid[i - 2][j]);
                }
                else{
                    whitePawnMoves[grid[i][j]].add(grid[i - 1][j]);
                }
            }
        }
    }
    public static void initBlackPawnMoves(){
        for(int i = 0; i < 64; i++){
            blackPawnMoves[i] = new ArrayList<Integer>();
        }
        for(int i = 1; i < n-1; i++){
            for(int j = 0; j < n; j++){
                if(i == 1){
                    blackPawnMoves[grid[i][j]].add(grid[i + 1][j]);
                    blackPawnMoves[grid[i][j]].add(grid[i + 2][j]);
                }
                else{
                    blackPawnMoves[grid[i][j]].add(grid[i + 1][j]);
                }
            }
        }
    }
}