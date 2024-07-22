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

// Base class for pieces
struct Piece {
    int x, y;
    bool isWhite;
    virtual int getValue() const = 0;
    virtual char getType() const = 0;
};

// Derived classes for each piece type
struct Pawn : public Piece {
    int getValue() const override { return PAWN_VALUE; }
    char getType() const override { return isWhite ? 'P' : 'p'; }
};

struct Knight : public Piece {
    int getValue() const override { return KNIGHT_VALUE; }
    char getType() const override { return isWhite ? 'N' : 'n'; }
};

struct Bishop : public Piece {
    int getValue() const override { return BISHOP_VALUE; }
    char getType() const override { return isWhite ? 'B' : 'b'; }
};

struct Rook : public Piece {
    int getValue() const override { return ROOK_VALUE; }
    char getType() const override { return isWhite ? 'R' : 'r'; }
};

struct Queen : public Piece {
    int getValue() const override { return QUEEN_VALUE; }
    char getType() const override { return isWhite ? 'Q' : 'q'; }
};

struct King : public Piece {
    int getValue() const override { return KING_VALUE; }
    char getType() const override { return isWhite ? 'K' : 'k'; }
};

class Board {
public:
    vector<Piece> pieces;

    Board() {
        parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }

    ~Board() {
        
    }

    int evaluate() const {
        int score = 0;
        for (const auto& piece : pieces) {
            score += (piece.isWhite ? 1 : -1) * piece.getValue();
        }
        return score;
    }
    void printBoard(){
        int index = 0;
        for(int c = 0; c < 8; c++){
            for(int j = 0; j < 8; j++){
                cout << pieces[index].getType() << " ";
                index++;
            }
            cout << endl;
        }
    }
    void parseFEN(const string& fen) {
        pieces.clear();
        string row;
        int y = 0;
        istringstream iss(fen);
        while (getline(iss, row, '/')) {
            int x = 0;
            for (const auto& ch : row) {
                if (isdigit(ch)) {
                    x += ch - '0';
                } else {
                    Piece* piece = createPiece(ch, x, y);
                    if (piece) {
                        pieces.push_back(piece);
                    }
                    x++;
                }
            }
            y++;
        }
    }

private:
    Piece createPiece(char ch, int x, int y) {
        Piece* piece = nullptr;
        switch (tolower(ch)) {
            case 'p': piece = new Pawn(); break;
            case 'n': piece = new Knight(); break;
            case 'b': piece = new Bishop(); break;
            case 'r': piece = new Rook(); break;
            case 'q': piece = new Queen(); break;
            case 'k': piece = new King(); break;
            default: break;
        }
        if (piece) {
            piece.isWhite = isupper(ch);
            piece->x = x;
            piece->y = y;
        }
        return piece;
    }
};

int main() {
    Board board;
    board.printBoard();
    return 0;
}