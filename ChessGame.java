
import java.util.*;
/**
 * A game of chess that has a game board, and keeps track of who's turn it is and whether or not 
 * someone has won.
 */
public class ChessGame
{
    private ChessPiece [][] gameBoard;
    ArrayList <ChessPiece> WPieces;
    ArrayList <ChessPiece> BPieces;
    private boolean whiteTurn; // true if white player's turn, false if black player's
    private boolean win; // whether or not someone has won the game
    private int whitePoints;
    private int blackPoints;
    private boolean whiteResign;
    private boolean blackResign;

    /**
     * Creates a new chess game. 
     * A 2D array of object type ChessPiece is created that represents the game board, and 
     * the white player is set to have the first move.
     */
    public ChessGame ()
    {
        // create array for game board
        gameBoard = new ChessPiece [8][8];
        WPieces = new ArrayList <ChessPiece>();
        BPieces = new ArrayList <ChessPiece>();
    }

    /**
     * Starts a new chess game by implementing the array of GamePieces that represents the board. 
     */
    public void newGame()
    {
        // starts with the white player
        whiteTurn = true;
        // no winner in the beginning
        win = false;

        // add all chess pieces to game board

        // initialize the Arraylists for the black and white pieces
        WPieces = new ArrayList <ChessPiece>();
        BPieces = new ArrayList <ChessPiece>();
        // implement pawns:
        for (int i = 0; i < 8; i ++)
        {
            WPieces.add(new Pawn(1,i,true));
            BPieces.add(new Pawn(6,i,false));
        }
        // rooks
        WPieces.add(new Rook(0,0,true));
        WPieces.add(new Rook(0,7,true));
        BPieces.add(new Rook(7,0,false));
        BPieces.add(new Rook(7,7,false));
        // knights
        WPieces.add(new Knight(0,1,true));
        WPieces.add(new Knight(0,6,true));
        BPieces.add(new Knight(7,1,false));
        BPieces.add(new Knight(7,6,false));
        // bishops
        WPieces.add(new Bishop(0,2,true));
        WPieces.add(new Bishop(0,5,true));
        BPieces.add(new Bishop(7,2,false));
        BPieces.add(new Bishop(7,5,false));
        // queens
        WPieces.add(new Queen(0,3,true));
        BPieces.add(new Queen(7,3,false));
        // kings
        WPieces.add(new King(0,4,true));
        BPieces.add(new King(7,4,false));

        // initialize the 2D Array with all the pieces in the correct spots:
        gameBoard = new ChessPiece [8][8];
        // implement pawns:
        for (int i = 0; i < 8; i ++)
        {
            gameBoard[1][i] = WPieces.get(i);
            gameBoard[6][i] = BPieces.get(i);
        }
        // rooks
        gameBoard[0][0] = WPieces.get(8);
        gameBoard[0][7] = WPieces.get(9);
        gameBoard[7][0] = BPieces.get(8);
        gameBoard[7][7] = BPieces.get(9);
        // knights
        gameBoard[0][1] = WPieces.get(10);
        gameBoard[0][6] = WPieces.get(11);
        gameBoard[7][1] = BPieces.get(10);
        gameBoard[7][6] = BPieces.get(11);
        // bishops
        gameBoard[0][2] = WPieces.get(12);
        gameBoard[0][5] = WPieces.get(13);
        gameBoard[7][2] = BPieces.get(12);
        gameBoard[7][5] = BPieces.get(13);
        // queens
        gameBoard[0][3] = WPieces.get(14);
        gameBoard[7][3] = BPieces.get(14);
        // kings
        gameBoard[0][4] = WPieces.get(15);
        gameBoard[7][4] = BPieces.get(15);

        // initialize other instance varaibles:
        whitePoints = 0;
        blackPoints = 0;
        whiteResign = false;
        blackResign = false;

    }

    /*
     * plays out the next turn, returns true if successful, 
     * or returns false if move was invalid
     * 
     * startCol and startRow: location of the piece the player wants to move 
     * endCol and endRow: location the square the player wants to move to piece to 
     */
    public boolean nextTurn(int startCol, int startRow, int endCol, int endRow)
    {
        // location of the player's king
        int n = findKing(whiteTurn);
        boolean incheck = false;

        // check if moving the piece to the final location is a legal move 
        // (according to the pieces' isLegalMove method)
        if (!gameBoard[startRow][startCol].isLegalMove(endRow, endCol, gameBoard))
            return false;

        //if in check you must move out of check 

        // move the piece to the specified location 
        // (has not returned false, safe to make move)
        ChessPiece tempPiece = gameBoard[endRow][endCol];

        gameBoard[endRow][endCol] = gameBoard[startRow][startCol];
        gameBoard[endRow][endCol].move(endRow,endCol);
        gameBoard[startRow][startCol] = null;
        if (tempPiece != null)
        //if a peice was taken it has to be removed from play, remove from array 
        {   
            if (tempPiece.getIsWhite())
                WPieces.remove(tempPiece);
            else
                BPieces.remove(tempPiece);
        }

        //checks for enpassent, if so must remove piece and update the score
        ChessPiece enPassent = null;//will remain null if enpassent doesnt happen 

        if (gameBoard[endRow][endCol] instanceof Pawn)
        {
            if (whiteTurn)
            {
                if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                {
                    enPassent = gameBoard[startRow][endCol];
                    BPieces.remove(gameBoard[startRow][endCol]);
                    gameBoard[startRow][endCol] = null;
                }
            }
            else
            {
                if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                {
                    enPassent = gameBoard[startRow][endCol];
                    WPieces.remove(gameBoard[startRow][endCol]);
                    gameBoard[startRow][endCol] = null;
                }
            }
        }

        //checks if the move caused the king to be in check

        if (whiteTurn)
        {
            incheck = check(n, true);
        }
        else
        {
            incheck = check(n, false);
        }            

        //if the the move causes the king to be in check it must now reverse the move and return false, unsucessful
        if (incheck)
        {
            //put piece back 
            gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
            gameBoard[startRow][startCol].move(startRow, startCol);
            gameBoard[endRow][endCol] = tempPiece;
            if (tempPiece != null)
            //if a piece was taken must put it back into the array
            {
                gameBoard[endRow][endCol].move(endRow, endCol);
                if (tempPiece.getIsWhite())
                    WPieces.add(tempPiece);
                else
                    BPieces.add(tempPiece);

            }
            //if enPassent occured and the king is now in check the process fro restoring
            //the board is slightly different
            else if (enPassent != null)//if a piece was taken must put it back into the array
            {
                gameBoard[startRow][endCol] = enPassent;
                gameBoard[startRow][endCol].move(startRow, endCol);

                if (enPassent.getIsWhite())
                    WPieces.add(enPassent);
                else
                    BPieces.add(enPassent); 
            }
            return false;
        }

        //if sucessful and a piece was taken out of play removes from the pieces array
        //if already been taken out of array it just wont be able to find it and will do nothing
        if (tempPiece != null)
        {
            if (tempPiece.getIsWhite())
            {
                WPieces.remove(tempPiece);
            }
            else
            {
                BPieces.remove(tempPiece);
            }
        }

        //update the score 
        if (tempPiece != null)
            updateScore(tempPiece);
        else if (enPassent != null)
            updateScore(gameBoard[endRow][endCol]);

        //must change pawns ability to do enPassent, only possible right after they moved into position
        if (!whiteTurn)
        {
            for (int i = 0; i < BPieces.size(); i++)
            {
                if (BPieces.get(i) instanceof Pawn && ((Pawn)BPieces.get(i)).getEnPassent())
                    ((Pawn)BPieces.get(i)).changeEnPassent();
            }
        }
        else
        {
            for (int i = 0; i < WPieces.size(); i++)
            {
                if (WPieces.get(i) instanceof Pawn && ((Pawn)WPieces.get(i)).getEnPassent())
                    ((Pawn)WPieces.get(i)).changeEnPassent();
            }
        }    

        //if king or rook have just moved their first move variables need to be false, cannot castle
        if (gameBoard[endRow][endCol] instanceof King)
            ((King)gameBoard[endRow][endCol]).trueFirstMove();
        else if (gameBoard[endRow][endCol] instanceof Rook)
            ((Rook)gameBoard[endRow][endCol]).trueFirstMove();
        else if (gameBoard[endRow][endCol] instanceof Pawn)
        {
            if (((Pawn)gameBoard[endRow][endCol]).getEnPassent() == false && (startRow - endRow == 2 || endRow - startRow == 2))
                ((Pawn)gameBoard[endRow][endCol]).changeEnPassent();
        }

        // it is now the next player's turn:
        if (whiteTurn)
            whiteTurn = false;
        else
            whiteTurn = true;

        // return true since we were able to move the piece (no invalid moves)
        return true;
    }

    public int canPromote ()
    //return index in the pieces array that there is a pawn to be promoted, return -1 if not possible
    {
        if (!whiteTurn)
        {
            for (int i = 0; i < WPieces.size(); i++)
            {   
                if (WPieces.get(i) instanceof Pawn) // find a pawn
                {
                    if (WPieces.get(i).getRow() == 7) // reaches the other end
                        return i; //no way there could be more than one pawn promoting
                }
            }
        }
        else //checks if black can promote
        {
            for (int i = 0; i < BPieces.size(); i++)
            {   
                if (BPieces.get(i) instanceof Pawn) // find a pawn
                {
                    if (BPieces.get(i).getRow() == 0) // reaches the other end
                    {
                        return i; 
                    }
                }
            }
        }
        return -1; //return -1 if no pawn can promote
    }

    //will now carry out the pomoting, user has already entered the piece they wish to promote to using GUI button
    public void promote (String pieceType, int i)
    {

        if (!whiteTurn)
        {
            ChessPiece pawn = WPieces.get(i);//the pawn that is being promoted
            if (pieceType.equals("Queen"))
            {
                ChessPiece newPiece = new Queen(pawn.getRow(), pawn.getCol(), true);
                WPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                WPieces.remove(i);
            }
            else if (pieceType.equals("Rook"))
            {
                ChessPiece newPiece = new Rook(pawn.getRow(), pawn.getCol(), true);
                WPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                WPieces.remove(i);
            }
            else if (pieceType.equals("Bishop"))
            {
                ChessPiece newPiece = new Bishop(pawn.getRow(), pawn.getCol(), true);
                WPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                WPieces.remove(i);
            }
            else if (pieceType.equals("Knight"))
            {
                ChessPiece newPiece = new Knight(pawn.getRow(), pawn.getCol(), true);
                WPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                WPieces.remove(i);
            }
        }
        else
        {
            ChessPiece pawn = BPieces.get(i);//the pawn that is being promoted
            if (pieceType.equals("Queen"))
            {
                ChessPiece newPiece = new Queen(pawn.getRow(), pawn.getCol(), false);
                BPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                BPieces.remove(pawn);
            }
            else if (pieceType.equals("Rook"))
            {
                ChessPiece newPiece = new Rook(pawn.getRow(), pawn.getCol(), false);
                BPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                BPieces.remove(pawn);
            }
            else if (pieceType.equals("Bishop"))
            {
                ChessPiece newPiece = new Bishop(pawn.getRow(), pawn.getCol(), false);
                BPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                BPieces.remove(pawn);
            }
            else if (pieceType.equals("Knight"))
            {
                ChessPiece newPiece = new Knight(pawn.getRow(), pawn.getCol(), false);
                BPieces.add(newPiece);
                gameBoard[pawn.getRow()][pawn.getCol()] = newPiece;
                BPieces.remove(pawn);
            }
        }

    }

    public int canCastle ()
    // returns which way(s) the player can castle 
    // (1 = queenside, 2 = kingside, 3 = both)
    // or returns 0 if the player can't castle

    {
        int x = 0; //castling starts at neither, can work up from there, remains 0 if neither are possible 
        int n = -1;//the location of the king in the array
        if (whiteTurn && gameBoard[0][4] instanceof King && ((King)gameBoard[0][4]).getFirstMove() == false)
        {
            n = findKing(true); //location of the king
            if (check(n, true)) //king cannot castle out of check
                return x; 
            if (gameBoard[0][0] instanceof Rook && ((Rook)gameBoard[0][0]).getFirstMove() == false)
            {
                //checks for castling queenside 
                if (gameBoard[0][1] == null && gameBoard[0][2] == null && gameBoard[0][3] == null)
                {
                    //now must check if the king would be moving through check or would end up in check
                    boolean inCheck = false;
                    gameBoard[0][1] = gameBoard[0][4];
                    gameBoard[0][4] = null;
                    gameBoard[0][1].move(0, 1);
                    if (check(n, true))
                        inCheck = true;
                    gameBoard[0][2] = gameBoard[0][1];
                    gameBoard[0][1] = null;
                    gameBoard[0][2].move(0, 2);
                    if (check(n, true))
                        inCheck = true;
                    gameBoard[0][3] = gameBoard[0][2];
                    gameBoard[0][2] = null;
                    gameBoard[0][3].move(0, 3);
                    if (check(n, true))
                        inCheck = true;
                    gameBoard[0][4] = gameBoard[0][3];
                    gameBoard[0][3] = null;
                    gameBoard[0][4].move(0,4);
                    if (inCheck == false)
                        x ++;
                }
            }
            //checks for castling kingside 
            if (gameBoard[0][7] instanceof Rook && ((Rook)gameBoard[0][7]).getFirstMove() == false)
            {
                //now must check if the king would be moving through check or would end up in check 
                if (gameBoard[0][6] == null && gameBoard[0][5] == null)
                {
                    boolean inCheck = false;
                    gameBoard[0][6] = gameBoard[0][4];
                    gameBoard[0][4] = null;
                    gameBoard[0][6].move(0, 6);
                    if (check(n, true))
                        inCheck = true;
                    gameBoard[0][5] = gameBoard[0][6];
                    gameBoard[0][6] = null;
                    gameBoard[0][5].move(0, 5);
                    if (check(n, true))
                        inCheck = true;
                    gameBoard[0][4] = gameBoard[0][5];
                    gameBoard[0][5] = null;
                    gameBoard[0][4].move(0,4);
                    if (inCheck == false)
                        x += 2;
                }
            }
        }
        else if (!whiteTurn && gameBoard[7][4] instanceof King && ((King)gameBoard[7][4]).getFirstMove() == false)
        {
            n = findKing(false); //location of the king
            if (check(n, false)) //king cannot castle out of check
                return x; 
            //checks for castling queenside 
            if (gameBoard[7][0] instanceof Rook && ((Rook)gameBoard[7][0]).getFirstMove() == false)
            {
                if (gameBoard[7][1] == null && gameBoard[7][2] == null && gameBoard[7][3] == null)
                {  
                    //now must check if the king would be moving through check or would end up in check
                    boolean inCheck = false;
                    gameBoard[7][1] = gameBoard[7][4];
                    gameBoard[7][4] = null;
                    gameBoard[7][1].move(7, 1);
                    if (check(n, false))
                        inCheck = true;
                    gameBoard[7][2] = gameBoard[7][1];
                    gameBoard[7][1] = null;
                    gameBoard[7][2].move(7, 2);
                    if (check(n, false))
                        inCheck = true;
                    gameBoard[7][3] = gameBoard[7][2];
                    gameBoard[7][2] = null;
                    gameBoard[7][3].move(7, 3);
                    if (check(n, false))
                        inCheck = true;
                    gameBoard[7][4] = gameBoard[7][3];
                    gameBoard[7][3] = null;
                    gameBoard[7][4].move(7,4);
                    if (inCheck == false)
                        x ++;
                }
            }
            //if there is a possible kingside casle 
            if (gameBoard[7][7] instanceof Rook && ((Rook)gameBoard[7][7]).getFirstMove() == false)
            {
                if (gameBoard[7][6] == null && gameBoard[7][5] == null)
                {
                    //must cehck to see if the kign would be moving through check or would end in check 
                    boolean inCheck = false;
                    gameBoard[7][6] = gameBoard[7][4];
                    gameBoard[7][4] = null;
                    gameBoard[7][6].move(7, 6);
                    if (check(n, false))
                        inCheck = true;
                    gameBoard[7][5] = gameBoard[7][6];
                    gameBoard[7][6] = null;
                    gameBoard[7][5].move(7, 5);
                    if (check(n, false))
                        inCheck = true;
                    gameBoard[7][4] = gameBoard[7][5];
                    gameBoard[7][5] = null;
                    gameBoard[7][4].move(7,4);
                    if (inCheck == false)
                        x += 2;
                }
            }
        }
        return x; //the castling that can be done, represented as an int
    }

    //performs a caslte, called from the runner, next turn will not be called 
    public void castle (boolean kingside)
    {
        if (whiteTurn)
        {
            if (kingside) //if it is a kingside castle, other wise it is queenside 
            {
                // move the king 
                gameBoard[0][6] = gameBoard[0][4];
                gameBoard[0][6].move(0,6);
                gameBoard[0][4] = null;

                // move the rook
                gameBoard[0][5] = gameBoard[0][7];
                gameBoard[0][5].move(0,5);
                gameBoard[0][7] = null;

                //will make castling not possible
                ((King)gameBoard[0][6]).trueFirstMove();
            }
            else
            {
                // move the king 
                gameBoard[0][2] = gameBoard[0][4];
                gameBoard[0][2].move(0,2);
                gameBoard[0][4] = null;   

                // move the rook
                gameBoard[0][3] = gameBoard[0][0];
                gameBoard[0][3].move(0,3);
                gameBoard[0][0] = null;

                //will make castling not possible
                ((King)gameBoard[0][2]).trueFirstMove();
            }

            // if is now the next player's turn 
            whiteTurn = false;
        }
        else
        {
            //for kingside, otherwise queenside castle
            if (kingside)
            {
                // move the king
                gameBoard[7][6] = gameBoard[7][4];
                gameBoard[7][6].move(7,6);
                gameBoard[7][4] = null;

                // move the rook
                gameBoard[7][5] = gameBoard[7][7];
                gameBoard[7][5].move(7,5);
                gameBoard[7][7] = null;

                //will make castling not possible
                ((King)gameBoard[7][6]).trueFirstMove();
            }
            else
            {
                // move the king 
                gameBoard[7][2] = gameBoard[7][4];
                gameBoard[7][2].move(7,2);
                gameBoard[7][4] = null;

                // move the rook
                gameBoard[7][3] = gameBoard[7][0];
                gameBoard[7][3].move(7,3);
                gameBoard[7][0] = null;

                //will make castling not possible
                ((King)gameBoard[7][2]).trueFirstMove();
            }

            // it is now the next player's turn
            whiteTurn = true;
        }
    }

    // update the player's score based on the piece that was captured
    public void updateScore (ChessPiece piece)
    {
        if (whiteTurn) // update white player's score
        {
            if (piece instanceof Pawn) // pawns are worth 1 point
            {
                whitePoints += 1;   
            }
            else if (piece instanceof Knight) // knight 3 points 
            {
                whitePoints += 3;   
            }
            else if (piece instanceof Bishop) // bishop 3 points 
            {
                whitePoints += 3;   
            }
            else if (piece instanceof Rook) // rook 5 points
            {
                whitePoints += 5;
            }
            else if (piece instanceof Queen) // queen 9 points 
            {
                whitePoints += 9;
            }
        }
        else // update black player's score
        {
            if (piece instanceof Pawn)
            {
                blackPoints += 1;   
            }
            else if (piece instanceof Knight)
            {
                blackPoints += 3;   
            }
            else if (piece instanceof Bishop)
            {
                blackPoints += 3;   
            }
            else if (piece instanceof Rook)
            {
                blackPoints += 5;
            }
            else if (piece instanceof Queen)
            {
                blackPoints += 9;
            }
        }
    }

    //returns the location of the king in the ArrayList (for whichever colour boolean it is)
    public int findKing (boolean white)
    {
        if (white) // find the white player's king
        {
            for (int i = 0; i < WPieces.size(); i++)
            {
                if (WPieces.get(i) instanceof King)
                    return i;
            }
        }
        else // find the black player's king
        {
            for (int i = 0; i < BPieces.size(); i++)
            {
                if (BPieces.get(i) instanceof King)
                    return i;
            }
        }
        return -1; // this should not be reached, since the king is always on the board
    }

    // returns whether or not the player is in check 
    // (in a position where one of the opponent's pieces can capture it)
    // based on the location of the player's king (n) and what colour the king is 
    public boolean check(int n, boolean white)
    {
        //int r and c are the row and column of the kings location
        int r = -1;
        int c = -1;
        if (white)
        {
            r = WPieces.get(n).getRow();
            c = WPieces.get(n).getCol();
            for (int i = 0; i < BPieces.size(); i++)
            //will go through all blackpieces to check if taking the king would be a legal move
            {
                if (BPieces.get(i).isLegalMove(r, c, gameBoard))
                {
                    return true;
                }
            }
        }
        else
        {
            r = BPieces.get(n).getRow();
            c = BPieces.get(n).getCol();
            for (int i = 0; i < WPieces.size(); i++)
            //will go through all white pieces to check if taking the king would be a legal move
            {
                if (WPieces.get(i).isLegalMove(r, c, gameBoard))
                {
                    return true;
                }
            }
        }

        return false;
    }

    // returns whether or not the player is in checkmate, based on the loaction of the player's king (n)
    public boolean checkmate (int n)//int n is the location of the king in the array
    {           
        ArrayList<Coordinate> moves = new ArrayList<Coordinate>();

        //r and c will be the row and column of the king
        int r = -1;
        int c = -1;
        if (whiteTurn)
        {
            r = WPieces.get(n).getRow();
            c = WPieces.get(n).getCol();
            //goes through array of pieces and calls the possibleMoves method to find all 
            //possible moves a piece has then checks if any of them would get the king out of chekc
            //if not moves can get the king out of check then the game is over
            for (int i = 0; i < WPieces.size(); i++)
            {
                moves = WPieces.get(i).possibleMoves(gameBoard);
                for (int a = 0; a < moves.size(); a++)
                {
                    int startRow = WPieces.get(i).getRow();
                    int startCol = WPieces.get(i).getCol();
                    int endRow = moves.get(a).x;
                    int endCol = moves.get(a).y;
                    ChessPiece tempPiece = gameBoard[endRow][endCol];
                    gameBoard[endRow][endCol] = gameBoard[startRow][startCol];
                    gameBoard[endRow][endCol].move(endRow,endCol);
                    gameBoard[startRow][startCol] = null;
                    //checks for enpassent, if so must remove piece 
                    ChessPiece enPassent = null;//will remain null if enpassent doesnt happen
                    if (gameBoard[endRow][endCol] instanceof Pawn)
                    {
                        if (whiteTurn)
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                BPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;                      
                            }
                        }
                        else
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                WPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;
                            }
                        }
                    }
                    if (tempPiece != null)//to try the move it must also take a piece if needed
                    //if taking a piece it must be removed from the array for full effect
                    {
                        if (tempPiece.getIsWhite())
                            WPieces.remove(tempPiece);
                        else
                            BPieces.remove(tempPiece);
                    }
                    else if (enPassent != null)
                    {
                        if (enPassent.getIsWhite())
                            WPieces.remove(enPassent);
                        else
                            BPieces.remove(enPassent);
                    }

                    //if not in check that means the king is not in checkmate
                    //must move pieces back into position and return false
                    if (check(n, true) == false)
                    {   
                        gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                        gameBoard[startRow][startCol].move(startRow, startCol);
                        gameBoard[endRow][endCol] = tempPiece;
                        if (tempPiece != null)//add piece back into array
                        {
                            gameBoard[endRow][endCol].move(endRow, endCol);
                            if (tempPiece.getIsWhite())
                                WPieces.add(tempPiece);
                            else
                                BPieces.add(tempPiece);
                        }
                        //if enPassent occured and the king is now in check the process fro restoring 
                        //the board is slightly different
                        else if (enPassent != null)//if a piece was taken must put it back into the array
                        {
                            gameBoard[startRow][endCol] = enPassent;
                            gameBoard[startRow][endCol].move(startRow, endCol);
                            if (enPassent.getIsWhite())
                                WPieces.add(enPassent);
                            else
                                BPieces.add(enPassent);
                        }
                        return false;
                    }

                    //king still in check, must keep searching, move enerything back
                    gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                    gameBoard[startRow][startCol].move(startRow, startCol);
                    gameBoard[endRow][endCol] = tempPiece;
                    if (tempPiece != null)//add piece back into array
                    {
                        gameBoard[endRow][endCol].move(endRow, endCol);
                        if (tempPiece.getIsWhite())
                            WPieces.add(tempPiece);
                        else
                            BPieces.add(tempPiece);
                    }
                    //if enPassent occured and the king is now in check the process fro restoring 
                    //the board is slightly different
                    else if (enPassent != null)//if a piece was taken must put it back into the array
                    {
                        gameBoard[startRow][endCol] = enPassent;
                        gameBoard[startRow][endCol].move(startRow, endCol);
                        if (enPassent.getIsWhite())
                            WPieces.add(enPassent);
                        else
                            BPieces.add(enPassent);
                    }
                }
                moves.clear();//clear moves, will try a new pieces set of moves
            }
        }
        else
        {
            r = BPieces.get(n).getRow();
            c = BPieces.get(n).getCol();
            //goes through array of pieces and calls the possibleMoves method to find all 
            //possible moves a piece has then checks if any of them would get the king out of chekc

            for (int i = 0; i < BPieces.size(); i++)
            {
                moves = BPieces.get(i).possibleMoves(gameBoard);
                for (int a = 0; a < moves.size(); a++)
                {
                    int startRow = BPieces.get(i).getRow();
                    int startCol = BPieces.get(i).getCol();
                    int endRow = moves.get(a).x;
                    int endCol = moves.get(a).y;
                    ChessPiece tempPiece = gameBoard[endRow][endCol];
                    gameBoard[endRow][endCol] = gameBoard[startRow][startCol];
                    gameBoard[endRow][endCol].move(endRow,endCol);
                    gameBoard[startRow][startCol] = null;

                    //checks for enpassent, if so must remove piece 
                    ChessPiece enPassent = null;//will remain null if enpassent doesnt happen
                    if (gameBoard[endRow][endCol] instanceof Pawn)
                    {
                        if (whiteTurn)
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                BPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;                      
                            }
                        }
                        else
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                WPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;
                            }
                        }
                    }
                    if (tempPiece != null)//to try the move it must also take a piece if needed
                    //if taking a piece it must be removed from the array for full effect
                    {
                        if (tempPiece.getIsWhite())
                            WPieces.remove(tempPiece);
                        else
                            BPieces.remove(tempPiece);
                    }
                    else if (enPassent != null)
                    {
                        if (enPassent.getIsWhite())
                            WPieces.remove(enPassent);
                        else
                            BPieces.remove(enPassent);
                    }

                    if (check(n, false) == false)
                    {   
                        gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                        gameBoard[startRow][startCol].move(startRow, startCol);
                        gameBoard[endRow][endCol] = tempPiece;
                        if (tempPiece != null)//add piece back into array
                        {
                            gameBoard[endRow][endCol].move(endRow, endCol);
                            if (tempPiece.getIsWhite())
                                WPieces.add(tempPiece);
                            else
                                BPieces.add(tempPiece);
                        }
                        //if enPassent occured and the king is now in check the process fro restoring 
                        //the board is slightly different
                        else if (enPassent != null)//if a piece was taken must put it back into the array
                        {
                            gameBoard[startRow][endCol] = enPassent;
                            gameBoard[startRow][endCol].move(startRow, endCol);
                            if (enPassent.getIsWhite())
                                WPieces.add(enPassent);
                            else
                                BPieces.add(enPassent);
                        }
                        return false;
                    }

                    //king still in check, must keep searching, move everythign back
                    gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                    gameBoard[startRow][startCol].move(startRow, startCol);
                    gameBoard[endRow][endCol] = tempPiece;
                    if (tempPiece != null)//add piece back into array
                    {
                        gameBoard[endRow][endCol].move(endRow, endCol);
                        if (tempPiece.getIsWhite())
                            WPieces.add(tempPiece);
                        else
                            BPieces.add(tempPiece);
                    }
                    //if enPassent occured and the king is now in check the process fro restoring 
                    //the board is slightly different
                    else if (enPassent != null)//if a piece was taken must put it back into the array
                    {
                        gameBoard[startRow][endCol] = enPassent;
                        gameBoard[startRow][endCol].move(startRow, endCol);
                        if (enPassent.getIsWhite())
                            WPieces.add(enPassent);
                        else
                            BPieces.add(enPassent);
                    }
                }
                moves.clear();//clear moves, will try a new pieces set of moves
            }
        }
        //no moves have been able to get the king out of check, checkmate
        return true;
    } 

    //checks for stalemate, king is not in check but the player has no legal move ei they will all result in check
    public boolean stalemate (int n)
    {
        //will only be called if king is not in check or checkmate, therefore do not need to check for it
        ArrayList<Coordinate> moves = new ArrayList<Coordinate>();

        int r = -1;
        int c = -1;

        //will find all possible moves of a piece, (some may not actually be possible, must check if it would
        //result in check) if all of what seem to be possible move result in check then it is a stalemate
        if (whiteTurn)
        {
            r = WPieces.get(n).getRow();
            c = WPieces.get(n).getCol();
            for (int i = 0; i < WPieces.size(); i++)
            {
                moves = WPieces.get(i).possibleMoves(gameBoard);
                for (int a = 0; a < moves.size(); a++)
                {
                    int startRow = WPieces.get(i).getRow();
                    int startCol = WPieces.get(i).getCol();
                    int endRow = moves.get(a).x;
                    int endCol = moves.get(a).y;
                    ChessPiece tempPiece = gameBoard[endRow][endCol];
                    gameBoard[endRow][endCol] = gameBoard[startRow][startCol];
                    gameBoard[endRow][endCol].move(endRow,endCol);
                    gameBoard[startRow][startCol] = null;
                    
                    //checks for enpassent, if so must remove piece 
                    ChessPiece enPassent = null;//will remain null if enpassent doesnt happen
                    if (gameBoard[endRow][endCol] instanceof Pawn)
                    {
                        if (whiteTurn)
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                BPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;                      
                            }
                        }
                        else
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                WPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;
                            }
                        }
                    }
                    
                    if (tempPiece != null)
                    {
                        if (tempPiece.getIsWhite())
                            WPieces.remove(tempPiece);
                        else
                            BPieces.remove(tempPiece);
                    }
                    else if (enPassent != null)
                    {
                        if (enPassent.getIsWhite())
                            WPieces.remove(enPassent);
                        else
                            BPieces.remove(enPassent);
                    }

                    if (check(n, true) == false)
                    {   
                        gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                        gameBoard[startRow][startCol].move(startRow, startCol);
                        gameBoard[endRow][endCol] = tempPiece;
                        if (tempPiece != null)
                        {
                            gameBoard[endRow][endCol].move(endRow, endCol);
                            if (tempPiece.getIsWhite())
                                WPieces.add(tempPiece);
                            else
                                BPieces.add(tempPiece);
                        }
                        //if enPassent occured and the king is now in check the process fro restoring 
                        //the board is slightly different
                        else if (enPassent != null)//if a piece was taken must put it back into the array
                        {
                            gameBoard[startRow][endCol] = enPassent;
                            gameBoard[startRow][endCol].move(startRow, endCol);
                            if (enPassent.getIsWhite())
                                WPieces.add(enPassent);
                            else
                                BPieces.add(enPassent);
                        }
                        return false;
                    }

                    gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                    gameBoard[startRow][startCol].move(startRow, startCol);
                    gameBoard[endRow][endCol] = tempPiece;
                    if (tempPiece != null)
                    {
                        gameBoard[endRow][endCol].move(endRow, endCol);
                        if (tempPiece.getIsWhite())
                            WPieces.add(tempPiece);
                        else
                            BPieces.add(tempPiece);
                    }
                    //if enPassent occured and the king is now in check the process fro restoring 
                    //the board is slightly different
                    else if (enPassent != null)//if a piece was taken must put it back into the array
                    {
                        gameBoard[startRow][endCol] = enPassent;
                        gameBoard[startRow][endCol].move(startRow, endCol);
                        if (enPassent.getIsWhite())
                            WPieces.add(enPassent);
                        else
                            BPieces.add(enPassent);
                    }
                }
                moves.clear();
            }
        }
        else
        {
            r = BPieces.get(n).getRow();
            c = BPieces.get(n).getCol();
            for (int i = 0; i < BPieces.size(); i++)
            {
                moves = BPieces.get(i).possibleMoves(gameBoard);
                for (int a = 0; a < moves.size(); a++)
                {
                    int startRow = BPieces.get(i).getRow();
                    int startCol = BPieces.get(i).getCol();
                    int endRow = moves.get(a).x;
                    int endCol = moves.get(a).y;
                    ChessPiece tempPiece = gameBoard[endRow][endCol];
                    gameBoard[endRow][endCol] = gameBoard[startRow][startCol];
                    gameBoard[endRow][endCol].move(endRow,endCol);
                    gameBoard[startRow][startCol] = null;

                    //checks for enpassent, if so must remove piece 
                    ChessPiece enPassent = null;//will remain null if enpassent doesnt happen
                    if (gameBoard[endRow][endCol] instanceof Pawn)
                    {
                        if (whiteTurn)
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                BPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;                      
                            }
                        }
                        else
                        {
                            if ((endCol == startCol +1 || endCol == startCol -1) && tempPiece == null)
                            {
                                enPassent = gameBoard[startRow][endCol];
                                WPieces.remove(gameBoard[startRow][endCol]);
                                gameBoard[startRow][endCol] = null;
                            }
                        }
                    }
                    
                    if (tempPiece != null)//remove the piece fromn the array
                    {
                        if (tempPiece.getIsWhite())
                            WPieces.remove(tempPiece);
                        else
                            BPieces.remove(tempPiece);
                    }
                    else if (enPassent != null)
                    {
                        if (enPassent.getIsWhite())
                            WPieces.remove(enPassent);
                        else
                            BPieces.remove(enPassent);
                    }

                    if (check(n, false) == false)//if it can get out of check can get out of stalemate
                    {   
                        gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                        gameBoard[startRow][startCol].move(startRow, startCol);
                        gameBoard[endRow][endCol] = tempPiece;
                        if (tempPiece != null)
                        {
                            gameBoard[endRow][endCol].move(endRow, endCol);
                            if (tempPiece.getIsWhite())
                                WPieces.add(tempPiece);
                            else
                                BPieces.add(tempPiece);
                        }
                        //if enPassent occured and the king is now in check the process fro restoring 
                        //the board is slightly different
                        else if (enPassent != null)//if a piece was taken must put it back into the array
                        {
                            gameBoard[startRow][endCol] = enPassent;
                            gameBoard[startRow][endCol].move(startRow, endCol);
                            if (enPassent.getIsWhite())
                                WPieces.add(enPassent);
                            else
                                BPieces.add(enPassent);
                        }
                        return false;
                    }

                    gameBoard[startRow][startCol] = gameBoard[endRow][endCol];
                    gameBoard[startRow][startCol].move(startRow, startCol);
                    gameBoard[endRow][endCol] = tempPiece;
                    if (tempPiece != null)
                    {
                        gameBoard[endRow][endCol].move(endRow, endCol);
                        if (tempPiece.getIsWhite())
                            WPieces.add(tempPiece);
                        else
                            BPieces.add(tempPiece);
                    }
                    //if enPassent occured and the king is now in check the process fro restoring 
                    //the board is slightly different
                    else if (enPassent != null)//if a piece was taken must put it back into the array
                    {
                        gameBoard[startRow][endCol] = enPassent;
                        gameBoard[startRow][endCol].move(startRow, endCol);
                        if (enPassent.getIsWhite())
                            WPieces.add(enPassent);
                        else
                            BPieces.add(enPassent);
                    }

                }
                moves.clear();
            }
        }
        return true;
    }

    public boolean checkmateImpossible()
    //checks for cases in which there are insuffucint pieces for either oponent to checkmate 
    //will result in a tie
    {
        if (WPieces.size() == 2 && BPieces.size() == 1)
        {
            if (WPieces.get(0) instanceof Bishop ||WPieces.get(1) instanceof Bishop )
            {
                win = true;
                return true;
            }
            else if (WPieces.get(0) instanceof Knight ||WPieces.get(1) instanceof Knight)
            {
                win = true;
                return true;
            }
        }
        else if (WPieces.size() == 1 && BPieces.size() == 2)
        {
            if (BPieces.get(0) instanceof Bishop ||BPieces.get(1) instanceof Bishop )
            {
                win = true;
                return true;
            }
            else if (BPieces.get(0) instanceof Knight ||BPieces.get(1) instanceof Knight)
            {
                win = true;
                return true;
            }
        }
        else if (WPieces.size() == 1 && BPieces.size() == 1)
        {
            win = true;
            return true;
        }
        else if (WPieces.size() == 3 && BPieces.size() == 1)
        {
            if (WPieces.get(0) instanceof Knight && WPieces.get(1) instanceof Knight)
            {
                win = true;
                return true;
            }
            else if (WPieces.get(2) instanceof Knight && WPieces.get(1) instanceof Knight)
            {
                win = true;
                return true;
            }
            else if (WPieces.get(0) instanceof Knight && WPieces.get(2) instanceof Knight)
            {
                win = true;
                return true;
            }
        }
        else if (WPieces.size() == 1 && BPieces.size() == 3)
        {
            if (BPieces.get(0) instanceof Knight && BPieces.get(1) instanceof Knight)
            {
                win = true;
                return true;
            }
            else if (BPieces.get(2) instanceof Knight && BPieces.get(1) instanceof Knight)
            {
                win = true;
                return true;
            }
            else if (BPieces.get(0) instanceof Knight && BPieces.get(2) instanceof Knight)
            {
                win = true;
                return true;
            }
        }

        // none of the cases of insufficient moves occured
        return false;
    }

    // return whether or not the piece at the specified row and column (indeces) contain
    // a peice that is the player's (ex. if it's white turn, whether or not it selected a whote piece)
    public boolean validStartLoc (int startLocRow, int startLocCol)
    {
        // if there is no gamePiece at the specified location: illegal move
        if (gameBoard[startLocRow][startLocCol] == null)
            return false;
        // if the piece the player wants to move is not their own: illegal move 
        if (whiteTurn)
        // white player's turn 
        {
            if (!gameBoard[startLocRow][startLocCol].getIsWhite())
                return false;
        } 
        else if (gameBoard[startLocRow][startLocCol].getIsWhite())
        // black player's turn
            return false;
        return true;
    }

    /**
     * returns whether or not the game has been won by someone
     * 
     * @return true if the game has been won by someone, false if it hasn't
     */
    public boolean winner()
    {
        return win;
    }

    /**
     * Returns whether or not it is the white player's turn. 
     * 
     * @return true if it is the white player's turn and false if it is the black player's. 
     */
    public boolean getWhiteTurn()
    {
        return whiteTurn;
    }

    // return the array of the white pieces in the game
    public ArrayList <ChessPiece> getWhitePieces ()
    {
        return WPieces;
    }

    // return the array of the black chess pieces in the game
    public ArrayList <ChessPiece> getBlackPieces ()
    {
        return BPieces;
    }

    // return the black player's points
    public int getBlackScore()
    {
        return blackPoints;
    }

    // return the white player's points
    public int getWhiteScore()
    {
        return whitePoints;
    }

    // a player resigns 
    public void resign (boolean whitePlayer)
    {
        if (whitePlayer) // white player resigns
            whiteResign = true;
        else // black player resigns
            blackResign = true;
        // game is over
        win = true;
    }

    // return whether or not the white player resigned
    public boolean whiteResign()
    {
        return whiteResign;
    }

    // return whether or not the black player resigned
    public boolean blackResign()
    {
        return blackResign;
    }

    // both players decide to draw 
    public void draw ()
    {
        whiteResign = true;
        blackResign = true;
        win = true;
    }

    // returns whether or not the players decided to draw 
    public boolean tie ()
    {
        if(whiteResign && blackResign)
            return true;
        return false;
    }

    // set win to true (game is over)
    public void setWin()
    {
        win = true;
    }
}

