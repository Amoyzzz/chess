#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <map>
using namespace std;
// Constants for piece values
const int PAWN_VALUE = 100;
const int KNIGHT_VALUE = 320;
const int BISHOP_VALUE = 330;
const int ROOK_VALUE = 500;
const int QUEEN_VALUE = 900;
const int KING_VALUE = 1000000;

// Basic board representation
class Board {
public:
    string board[64];

    Board() {
        parseFEN("startpos");
    }
    int evaluate() {
        // Simple evaluation function
        int score = 0;
        for (const auto &piece : board) {
            if (piece == "P") score += PAWN_VALUE;
            if (piece == "N") score += KNIGHT_VALUE;
            if (piece == "B") score += BISHOP_VALUE;
            if (piece == "R") score += ROOK_VALUE;
            if (piece == "Q") score += QUEEN_VALUE;
            if (piece == "K") score += KING_VALUE;
            if (piece == "p") score -= PAWN_VALUE;
            if (piece == "n") score -= KNIGHT_VALUE;
            if (piece == "b") score -= BISHOP_VALUE;
            if (piece == "r") score -= ROOK_VALUE;
            if (piece == "q") score -= QUEEN_VALUE;
            if (piece == "k") score -= KING_VALUE;
        }
        return score;
    }

    void makeMove(const string &move) {
        // Dummy implementation
        // Actual implementation would update the board state
    }

    void undoMove(const string &move) {
        // Dummy implementation
        // Actual implementation would revert the board state
    }

    void parseFEN(const string &fen) {
        // Parse FEN chess notation and turn it into an 8 by 8 chess grid
        string row;
        int index = 0;
        istringstream iss(fen);
        board[index++] = ' ';
        while (getline(iss, row, '/')) {
            for (const auto &piece : row) {
                if (piece >= '1' && piece <= '8') {
                    int numEmpty = piece - '0';
                    while (numEmpty--) {
                        board[index++] = '.';
                    }
                } else {
                    board[index++] = piece;
                }
            }
            board[index++] = ' ';
        }
    }
};

int minimax(Board &board, int depth, int alpha, int beta, bool isMaximizing) {

}

void uciLoop() {
    Board board;
    string input;
    while (true) {
        getline(cin, input);
        if (input == "uci") {
            cout << "id name SimpleEngine\n";
            cout << "id author YourName\n";
            cout << "uciok\n";
        } else if (input == "isready") {
            cout << "readyok\n";
        } else if (input == "ucinewgame") {
            // Reset board for new game
            board.parseFEN("startpos");
        } else if (input.substr(0, 8) == "position") {
            if (input.substr(9, 3) == "fen") {
                string fen = input.substr(13);
                board.parseFEN(fen);
            } else if (input.substr(9, 8) == "startpos") {
                board.parseFEN("startpos");
            }
        } else if (input.substr(0, 2) == "go") {
            string bestmove;
            cout << "bestmove " << bestMove << "\n";
        } else if (input == "quit") {
            break;
        }
    }
}

int main() {
    uciLoop();
    return 0;
}
