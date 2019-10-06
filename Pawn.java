import java.util.ArrayList;
/**
 * A pawn chess piece.
 */
public class Pawn extends ChessPiece
{
    private boolean enPassent;
    /**
     * Constructs a new Pawn chess piece.
     * 
     * @param the row the piece starts at 
     * @param the column the piece start at
     * @param whether or not the piece is white (true if white, false if black)
     */
    public Pawn (int startRow, int startCol, boolean isWhite)
    {
        super(startRow, startCol, isWhite);
        enPassent = false;
    }

    /**
     * Returns whether or not a move to a space is legal. 
     * (Does not check if there are other pieces in the way or if they are the correct colour)
     * 
     * @param the row of the space that is being checked
     * @param the coloumn of the space that is being checked
     * @return whether o
     * {r not the move is legal.
     */
    public boolean isLegalMove(int r, int c, ChessPiece[][] gameBoard)
    {

        // must move to a new space forward (row must change)
        if (r == getRow())
            return false;
        // can't go off the board
        if (r > 7 || r < 0 || c < 0 || c > 7)
            return false;
        // cannot move more than one space to the side
        if (Math.abs(c - getCol()) > 1)
            return false;

        // the space that they want to move it to has their own piece
        if (gameBoard[r][c] != null)
        {
            if (getIsWhite())
            { 
                if (gameBoard[r][c].getIsWhite())
                    return false;
            }
            // black player's turn
            else if (!gameBoard[r][c].getIsWhite())
            {
                return false;
            }
        }

        //checkfor enPassent
        if (getIsWhite())
        {
            if (r == 5 && getRow() == 4 && (c == getCol() + 1 || c == getCol() - 1) && gameBoard[getRow()][c] != null && gameBoard[r][c] == null)
            {
                if (gameBoard[getRow()][c] instanceof Pawn && ((Pawn)gameBoard[getRow()][c]).getEnPassent() && gameBoard[getRow()][c].getIsWhite() == false)
                    return true;
            }
        }
        else
        {
            if (r == 2 && getRow() == 3 && (c == getCol() + 1 || c == getCol() - 1) && gameBoard[getRow()][c] != null && gameBoard[r][c] == null)
            {
                if (gameBoard[getRow()][c] instanceof Pawn && ((Pawn)gameBoard[getRow()][c]).getEnPassent() && gameBoard[getRow()][c].getIsWhite())
                    return true;
                
            }
        }

        // pawns can only move sideways if they are taking the other player's piece
        // and can only move forwards if they are not taking the other player's piece:
        if (getCol() != c)
        // if it is moving sideways:
        {
            // it can only move to a spot with another piece  
            // (already checked to make sure it isn't its own piece)
            if (gameBoard[r][c] == null)
                return false;
        }
        else // isn't moving sideways
        {
            // can only move to a spot without another piece
            if (gameBoard[r][c] != null)
                return false;
        }

        if (getIsWhite())
        //if it's white, the pieces start in the bottom of the board (row 0)
        {
            // if first move, can move forward 2 spaces
            if (getRow() == 1 && c == getCol() && r == 3)
            {
                if (clearPath(r, c, gameBoard))
                {
                    return true;
                }
                else
                    return false;
            }
            // otherwise, can only move forward 1 space or enPassent

            if (r != getRow() +1)
                return false;
        }
        else
        // if black, pieces start at top (row 8)
        {
            // if first move, can move forward 2 spaces
            if (getRow() == 6 && c == getCol() && r == 4)
            {    
                if (clearPath(r, c, gameBoard))
                {
                    return true;
                }
                else
                    return false;
            }

            // otherwise, can only move forward 1 space
            if (r != getRow() -1)
                return false;
        }
        // (Can only move forward if it is not taking another player's piece.
        // Check this later)

        return true;
    }

    /**
     * Returns whether or not the path that a pawn takes is clear of any other piece.
     * 
     * @param the starting row index (of the board array) of the piece
     * @param the starting column index of the piece
     * @param the end row index of the piece 
     * @param the end column index of the piece
     * @return whether or not the path from the start index to the end index is clear of any other pieces
     */
    private boolean clearPath (int endLocRow, int endLocCol, ChessPiece[][] gameBoard)
    {
        // for pawns, we only need to check if it is moving forward 2 spaces
        // (make sure there is no piece one space forward)

        if (gameBoard[getRow()][getCol()].getIsWhite())
        // if it is a white piece
        {
            if (gameBoard[2][getCol()] != null)
                return false;
        }
        // if it is a black piece
        else if (gameBoard[5][getCol()] != null) 
            return false;
        return true;
    }

    /**
     * finds all the possible moves a piece can make without checking if it will put 
     * thier king in check. is used to determine checkmate and stalemate
     * 
     * @param the gameBoard, needed to check if the move is legal
     * 
     * @return an arrayList of coordinates of all the legal moves a piece can make (doesnt check for check)
     */
    public ArrayList<Coordinate> possibleMoves (ChessPiece[][] gameBoard)
    {
        ArrayList<Coordinate> moves = new ArrayList<Coordinate>();
        if (this.getIsWhite())
        {
            if (isLegalMove(this.getRow()+2, this.getCol(), gameBoard))
                moves.add(new Coordinate(this.getRow()+2, this.getCol()));
            if (isLegalMove(this.getRow()+1, this.getCol(), gameBoard))
                moves.add(new Coordinate(this.getRow()+1, this.getCol()));
            if (isLegalMove(this.getRow()+1, this.getCol()+1, gameBoard))
                moves.add(new Coordinate(this.getRow()+1, this.getCol()+1));
            if (isLegalMove(this.getRow()+1, this.getCol()-1, gameBoard))
                moves.add(new Coordinate(this.getRow()+1, this.getCol()-1));
        }
        else
        {
            if (isLegalMove(this.getRow()-2, this.getCol(), gameBoard))
                moves.add(new Coordinate(this.getRow()-2, this.getCol()));
            if (isLegalMove(this.getRow()-1, this.getCol(), gameBoard))
                moves.add(new Coordinate(this.getRow()-1, this.getCol()));
            if (isLegalMove(this.getRow()-1, this.getCol()+1, gameBoard))
                moves.add(new Coordinate(this.getRow()-1, this.getCol()+1));
            if (isLegalMove(this.getRow()-1, this.getCol()-1, gameBoard))
                moves.add(new Coordinate(this.getRow()-1, this.getCol()-1));
        }
        return moves;
    }

    //return true if enPssent is possible
    public boolean getEnPassent ()
    {
        return enPassent;
    }   

    //changes enPassent if possible
    public void changeEnPassent()
    {
        enPassent = !enPassent;
    }
}
