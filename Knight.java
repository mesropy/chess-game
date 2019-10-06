import java.util.ArrayList;
/**
 * A knight chess piece. 
 */
public class Knight extends ChessPiece
{
    /**
     * Constructs a new Knight chess piece.
     * 
     * @param the row the piece starts at 
     * @param the column the piece start at
     * @param whether or not the piece is white (true if white, false if black)
     */
    public Knight (int startRow, int startCol, boolean isWhite)
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
        // can move 2 spaces vertically and 1 space horizontally
        
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
        
        if (Math.abs(getRow() - r) == 2 && Math.abs(getCol() - c) == 1)
            return true;
        // or can move 2 spaces horizontally and 1 space vertically
        if (Math.abs(getRow() - r) == 1 && Math.abs(getCol() - c) == 2)
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
        ArrayList<Coordinate> moves = new ArrayList<Coordinate>();

        int r = this.getRow();
        int c = this.getCol();

        if (isLegalMove(r+2, c-1, gameBoard))
            moves.add(new Coordinate(r+2, c-1));
        if (isLegalMove(r+2, c+1, gameBoard))
            moves.add(new Coordinate(r+2, c+1));
        if (isLegalMove(r-2, c-1, gameBoard))
            moves.add(new Coordinate(r-2, c-1));
        if (isLegalMove(r-2, c+1, gameBoard))
            moves.add(new Coordinate(r-2, c+1));
        if (isLegalMove(r+1, c-2, gameBoard))
            moves.add(new Coordinate(r+1, c-2));            
        if (isLegalMove(r-1, c-2, gameBoard))
            moves.add(new Coordinate(r-1, c-2));
        if (isLegalMove(r+1, c+2, gameBoard))
            moves.add(new Coordinate(r+1, c+2));
        if (isLegalMove(r-1, c+2, gameBoard))
            moves.add(new Coordinate(r-1, c+2));
            
        return moves;
    }
}
