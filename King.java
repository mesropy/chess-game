import java.util.ArrayList;
/**
 * A King chess piece. 
 */
public class King extends ChessPiece
{
    private boolean firstMove;
    /**
     * 
     * Constructs a new King chess piece.
     * 
     * @param the row the piece starts at 
     * @param the column the piece start at
     * @param whether or not the piece is white (true if white, false if black)
     */
    public King (int startRow, int startCol, boolean isWhite)
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
    public boolean isLegalMove(int r, int c, ChessPiece[][] gameBoard)
    {
        // must move to a new space
        if (r == getRow() && c == getCol())
            return false;
        // can't go off the board
        if (r > 7 || r < 0 || c < 0 || c > 7)
            return false;
        // can move only one spot over 
        // (row changes by one and/or column changes by one)
        
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
        
        if ((Math.abs(getRow()-r) == 1 || Math.abs(getRow()-r) == 0) && (Math.abs(getCol()-c) == 1 || Math.abs(getCol()-c) == 0))
            return true;
         
            
        return false;
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
        int r = this.getRow();
        int c = this.getCol();
        ArrayList<Coordinate> moves = new ArrayList<Coordinate>();
        if (gameBoard[r][c].isLegalMove(r+1, c, gameBoard))
            moves.add(new Coordinate(r+1, c));       
        if (gameBoard[r][c].isLegalMove(r-1, c, gameBoard))
            moves.add(new Coordinate(r-1, c));   
        if (gameBoard[r][c].isLegalMove(r, c-1, gameBoard))
            moves.add(new Coordinate(r, c-1));   
        if (gameBoard[r][c].isLegalMove(r, c+1, gameBoard))
            moves.add(new Coordinate(r, c+1));   
        if (gameBoard[r][c].isLegalMove(r-1, c-1, gameBoard))
            moves.add(new Coordinate(r-1, c-1));   
        if (gameBoard[r][c].isLegalMove(r+1, c-1, gameBoard))
            moves.add(new Coordinate(r+1, c-1));   
        if (gameBoard[r][c].isLegalMove(r-1, c+1, gameBoard) )
            moves.add(new Coordinate(r-1, c+1));   
        if (gameBoard[r][c].isLegalMove(r+1, c+1, gameBoard))
            moves.add(new Coordinate(r+1, c+1));   
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
