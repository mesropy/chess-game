import java.util.ArrayList;
/**
 * A chess piece of a chess game.
 */
abstract class ChessPiece
{
    private int row;
    private int col;
    private boolean isWhite; // false = black
    
    // creates a chess piece
    public ChessPiece(int startRow, int startCol, boolean white)
    {
        row = startRow;
        col = startCol;
        isWhite = white;
    }

    abstract boolean isLegalMove(int r, int c,  ChessPiece[][] gameBoard);

    // moves the piece to the specified location (r = row, c = column)
    final void move(int r, int c)
    {
        // (use isLegalMove method in ChessGame class)
        row = r;
        col = c;
    }

    // returns the row location of the chess piece
    public int getRow ()
    {
        return row;
    } 

    // returns the column location of the chess piece
    public int getCol()
    {
        return col;
    }

    // returns whether or not the player is white
    public boolean getIsWhite()
    {
        return isWhite;
    }
    
    // return all the possible moves of the piece
    abstract public ArrayList<Coordinate> possibleMoves (ChessPiece[][] gameBoard);
    
}