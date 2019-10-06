import java.util.ArrayList;
/**
 * A Bishop chess piece. 
 */
public class Bishop extends ChessPiece
{
    /**
     * Constructs a new Bishop chess piece.
     * 
     * @param the row the piece starts at 
     * @param the column the piece start at
     * @param whether or not the piece is white (true if white, false if black)
     */
    public Bishop (int startRow, int startCol, boolean isWhite)
    {
        super(startRow, startCol, isWhite); 
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
        // can only move diagonally 
        // (row changes the same amount as the column)
        if (Math.abs(getRow() - r) != Math.abs(getCol() -c))
            return false;
        
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
            
        if (clearPath(getRow(), getCol(), r, c, gameBoard))
            return true;
        
            
        return false;
    }
    
    /**
     * Returns whether or not the path that a bishop takes is clear of any other piece.
     * 
     * @param the starting row index (of the board array) of the piece
     * @param the starting column index of the piece
     * @param the end row index of the piece 
     * @param the end column index of the piece
     * @return whether or not the path from the start index to the end index is clear of any other pieces
     */
    private boolean clearPath ( int startLocRow, int startLocCol, int endLocRow, int endLocCol, ChessPiece[][] gameBoard)
    {
        if (startLocCol > endLocCol)
        {
            // moves left (towards col a)
            if (startLocRow > endLocRow)
            {
                // moves backwards (towards row 1)
                int i = startLocRow-1;
                int j = startLocCol-1;
                while(i > endLocRow && j > endLocCol)
                {
                    if (gameBoard[i][j] != null)
                        return false;
                    i --;
                    j --;
                }
            }
            else
            {
                // moves forwards (towards row 8)
                int i = startLocRow+1;
                int j = startLocCol-1;
                while(i < endLocRow && j > endLocCol)
                {
                    if (gameBoard[i][j] != null)
                        return false;
                    i ++;
                    j--;
                }
            }
        }
        else
        {
            // moves right (towards col h)
            if (startLocRow > endLocRow)
            {
                // moves backwards (towards row 1)
                int i = startLocRow-1;
                int j = startLocCol+1;
                while(i > endLocRow && j < endLocCol)
                {
                    if (gameBoard[i][j] != null)
                        return false;
                    i --;
                    j++;
                }
            }
            else
            {
                // moves forward (towards row 8)
                int i = startLocRow+1;
                int j = startLocCol+1;
                while(i < endLocRow && j < endLocCol)
                {
                    if (gameBoard[i][j] != null)
                        return false;
                    i ++;
                    j ++;
                }
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
    public ArrayList<Coordinate> possibleMoves(ChessPiece[][] gameBoard)
    {
        ArrayList<Coordinate> moves = new ArrayList<Coordinate>();
        int r = this.getRow();
        int c = this.getCol();
        
        //the peice can still move in a givin direction, used ot exit from loop
        for(int i = 1;isLegalMove(r+i, c+i, gameBoard); i++)
        {
            moves.add(new Coordinate(r+i, c+i));
        }
        
        for(int i = 1;isLegalMove(r+i, c-i, gameBoard); i++)
        {
            moves.add(new Coordinate(r+i, c-i));
        }

        for(int i = 1;isLegalMove(r-i, c-i, gameBoard); i++)
        {
            moves.add(new Coordinate(r-i, c-i));
        }

        for(int i = 1;isLegalMove(r-i, c+i, gameBoard); i++)
        {
            moves.add(new Coordinate(r-i, c+i));
        }
        
        return moves;
    }
    
}

