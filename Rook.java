import java.util.ArrayList;
/**
 * A Rook chess piece. 
 */
public class Rook extends ChessPiece
{
    private boolean firstMove;
    /**
     * Constructs a new Rook chess piece.
     * 
     * @param the row the piece starts at 
     * @param the column the piece start at
     * @param whether or not the piece is white (true if white, false if black)
     */
    public Rook (int startRow, int startCol, boolean isWhite)
    {
        super(startRow, startCol, isWhite);
        firstMove = false;
    }

    /**
     * Returns whether or not a move to a space is legal. 
     * (Does not check if there are other pieces in the way or if they are the correct colour)
     * 
     * @param the row of the space that is being checked
     * @param the coloumn of the space that is being checked
     * @return whether or not the move is legal.
     */
    public boolean isLegalMove (int r, int c, ChessPiece[][] gameBoard)
    {
        // must move to a new space
        if (r == getRow() && c == getCol())
            return false;
        // can't go off the board
        if (r > 7 || r < 0 || c < 0 || c > 7)
            return false;
        // can either move horizontally or vertically 
        // (row stays the same and column changes OR 
        // column stays the same and row changes)
        
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
        
        if (r == getRow() || c == getCol() ){
            if (clearPath(r, c, gameBoard))
                return true;
            }
        return false;
    }
    
    /**
     * Returns whether or not the path that a rook takes is clear of any other piece.
     * 
     * @param the end row index of the piece 
     * @param the end column index of the piece
     * @return whether or not the path from the start index to the end index is clear of any other pieces
     */
    private boolean clearPath (int endLocRow, int endLocCol, ChessPiece[][] gameBoard)
    {
        if (getRow() == endLocRow)
        // moves horizontally
        {
            if (getCol() < endLocCol)
            // moves right (towards col h)
                for (int i = getCol()+1; i < endLocCol; i++)
                {
                    if (gameBoard[endLocRow][i] != null)
                        return false;
                }
            else
            // moves left (towards col a)
                for (int i = getCol()-1; i > endLocCol; i--)
                {
                    if (gameBoard[endLocRow][i] != null)
                        return false;
                }
        }
        else
        // moves vertically 
        {
            if (getRow() < endLocRow)
            // moves forward (towards row 8)
                for (int i = getRow()+1; i < endLocRow; i++)
                {
                    if (gameBoard[i][endLocCol] != null)
                        return false;
                }
            else
            // moves backwards (towards row 1)
                for (int i = getRow()-1; i > endLocRow; i--)
                {
                    if (gameBoard[i][endLocCol] != null)
                        return false;
                }
        }
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
        
        int r = this.getRow();
        int c = this.getCol();

        for(int i = 1;isLegalMove(r+i, c, gameBoard); i++)
        {
            moves.add(new Coordinate(r+i, c));
        }

        for(int i = 1;isLegalMove(r, c-i, gameBoard); i++)
        {
            moves.add(new Coordinate(r, c-i));
        }

        for(int i = 1;isLegalMove(r-i, c, gameBoard); i++)
        {
            moves.add(new Coordinate(r-i, c));
        }

        for(int i = 1;isLegalMove(r, c+i, gameBoard); i++)
        {
            moves.add(new Coordinate(r, c+i));
        }
        
        return moves;
    }
    
    /**
     *   Returns a boolean, weather or not the peice has been moved, needed for castling
     *   
     *   @return firstMove
     */
    public boolean getFirstMove()
    {
        return firstMove;
    }
    
    /**
     * makes firstMove true, will make castling not possible
     */
    public void trueFirstMove()
    {
        firstMove = true;
    }
}

