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
const int KING_VALUE = 20000;

// Basic board representation
class Board {
public:
    std::string board[64];

    Board() {
        parseFEN("startpos");
    }

    std::vector<std::string> generateMoves() {
        std::vector<std::string> moves;
        // Dummy moves for the sake of example
        moves.push_back("e2e4");
        moves.push_back("d2d4");
        moves.push_back("g1f3");
        return moves;
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

    void makeMove(const std::string &move) {
        // Dummy implementation
        // Actual implementation would update the board state
    }

    void undoMove(const std::string &move) {
        // Dummy implementation
        // Actual implementation would revert the board state
    }

    void parseFEN(const std::string& fenString) {
        std::string startingPosition[64] = {
            "r", "n", "b", "q", "k", "b", "n", "r",
            "p", "p", "p", "p", "p", "p", "p", "p",
            ".", ".", ".", ".", ".", ".", ".", ".",
            ".", ".", ".", ".", ".", ".", ".", ".",
            ".", ".", ".", ".", ".", ".", ".", ".",
            ".", ".", ".", ".", ".", ".", ".", ".",
            "P", "P", "P", "P", "P", "P", "P", "P",
            "R", "N", "B", "Q", "K", "B", "N", "R"
        };
    }
};

int minimax(Board &board, int depth, int alpha, int beta, bool isMaximizing) {

}

void uciLoop() {
    Board board;
    std::string input;
    while (true) {
        std::getline(std::cin, input);
        if (input == "uci") {
            std::cout << "id name SimpleEngine\n";
            std::cout << "id author YourName\n";
            std::cout << "uciok\n";
        } else if (input == "isready") {
            std::cout << "readyok\n";
        } else if (input == "ucinewgame") {
            // Reset board for new game
            board.parseFEN("startpos");
        } else if (input.substr(0, 8) == "position") {
            if (input.substr(9, 3) == "fen") {
                std::string fen = input.substr(13);
                board.parseFEN(fen);
            } else if (input.substr(9, 8) == "startpos") {
                board.parseFEN("startpos");
            }
        } else if (input.substr(0, 2) == "go") {
            std::vector<std::string> moves = board.generateMoves();
            std::string bestMove = moves[0];
            int bestValue = -10000;
            for (const auto &move : moves) {
                board.makeMove(move);
                int boardValue = minimax(board, 3, -10000, 10000, false);
                board.undoMove(move);
                if (boardValue > bestValue) {
                    bestValue = boardValue;
                    bestMove = move;
                }
            }
            std::cout << "bestmove " << bestMove << "\n";
        } else if (input == "quit") {
            break;
        }
    }
}

int main() {
    uciLoop();
    return 0;
}
