import javax.swing.*;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Creates a frame with the chess game. 
 */
public class ChessGameRunner 
{
    public static void main (String args[]) throws IOException
    {
        // create a frame 
        ChessGameFrame frame = new ChessGameFrame();

        // terminate the program when this frame is closed:

        frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    System.exit(0);
                }
            }
        );

    }
}

// a chess game frame, which has the game board with the pieces, and other options and informatio
// on the side
class ChessGameFrame extends JFrame implements ActionListener 
{
    private JPanel pane; // main pane
    private ChessGame game; // the chess game
    private JLayeredPane layeredPane; // layered pane in main pane

    // whether or not a starting location had been selected (or piece to move selected)
    private boolean startSelect; 
    // the starting location (or the location of the piece that needs to be moved)
    private int startCol;
    private int startRow; 

    // images of chess piece icons that are added to the board
    ArrayList <JLabel> pieceLabels;
    // image of square around the piece that is selected 
    ImageIcon selectSquareImage = new ImageIcon("images/select square.png");
    JLabel selectSquare = new JLabel(selectSquareImage);
    // text added to the side:
    JLabel scoresLabel; 
    JLabel whiteScore;
    JLabel blackScore;
    JLabel whiteScoreLabel;
    JLabel blackScoreLabel;
    JLabel whoseTurnLabel;
    JLabel checkLabel;
    JLabel otherPlayerLabel;
    JLabel promotePawnLabel;
    JLabel castleLabel;
    JLabel gameResultLabel;
    JLabel gameResultReasonLabel;
    JLabel nextTurnBox;
    // images of the boxes surrounding whose turn it is
    ImageIcon smallGreenBoxImage = new ImageIcon("images/small green square.png"); 
    ImageIcon medGreenBoxImage = new ImageIcon("images/med green square.png"); 
    ImageIcon bigGreenBoxImage = new ImageIcon("images/big green square.png");  
    ImageIcon redBoxImage = new ImageIcon("images/red square.png"); 
    // buttons added to the side:
    JButton drawButton;
    JButton helpButton;
    JButton thisResignButton;
    JButton otherResignButton;
    JButton newGameButton; 
    JButton castleButton;
    JButton castleKingsideButton;
    JButton castleQueensideButton;
    JButton promoteToQueenButton;
    JButton promoteToKnightButton;
    JButton promoteToBishopButton;
    JButton promoteToRookButton;

    /**
     * Creates a new ChessGameFrame which shows the game board, a title, the status 
     * of the game (whose turn it is, who won the game etc.), the players’ scores, resign buttons, 
     * a help button, and other options the player may have. 
     */
    public ChessGameFrame() throws IOException
    {
        // call JFrame's constructor, make the title of the window "Chess"
        super("Chess");

        // assign pane to reference to this frame’s ContentPane
        pane = (JPanel)getContentPane();
        // initialize layeredPane
        layeredPane = new JLayeredPane();
        // add layered board pane to the main pane
        pane.add(layeredPane);

        // add the game board:

        // add buttons for the chess board squares
        JButton [][] squaresButtons = new JButton [8][8];
        BufferedImage darkSquare = ImageIO.read(new File("images/Dark Square Image.png")); // dark square image 
        BufferedImage lightSquare = ImageIO.read(new File("images/Light Square Image.png")); // light square image
        for (int r = 0; r < 8; r ++) // rows
        {
            for (int c = 0; c < 8; c ++) // columns 
            {
                // make a checkerboard pattern, starting with the light square in the top left corner
                if (r%2 == 0)
                {
                    if (c % 2 == 0)
                        squaresButtons[r][c] = new JButton(new ImageIcon(lightSquare));

                    else
                        squaresButtons[r][c] = new JButton(new ImageIcon(darkSquare));
                }
                else
                {
                    if (c%2==0)
                        squaresButtons[r][c] = new JButton(new ImageIcon(darkSquare));
                    else
                        squaresButtons[r][c] = new JButton(new ImageIcon(lightSquare));
                }

                squaresButtons[r][c].setBorder(BorderFactory.createEmptyBorder()); // remove border around button
                squaresButtons[r][c].setBounds(c * 80 + 30, r*80, 80, 80); // set location & size
                layeredPane.add(squaresButtons[r][c], 1); // add to layered pane, in layer 1
                squaresButtons[r][c].setActionCommand(r + " " + c); // give button a new name based on row and column
                squaresButtons[r][c].addActionListener(this); // make this frame listen to the button
            }
        }

        // add labels of rows and columns:
        JLabel [][] boardLabels = new JLabel [8][2];
        for (int r = 0; r < 8; r ++)
        {
            // row labels
            boardLabels[r][0] = new JLabel(r + 1 + ""); 
            boardLabels[r][0].setFont(new Font("Myriad Pro",1,18)); // set font 
            boardLabels[r][0].setBounds(10, Math.abs(r*80 - 560), 30, 80); // set location and size

            // column labels
            boardLabels[r][1] = new JLabel((char)(r + 'a') + "");
            boardLabels[r][1].setFont(new Font("Myriad Pro",1,18)); // set font 
            boardLabels[r][1].setBounds(r*80 + 65, 645, 80, 30); // set location and size

            // add both to pane, layer 1
            layeredPane.add(boardLabels[r][0], 1);
            layeredPane.add(boardLabels[r][1], 1);
        }

        // create and start a new game 
        game = new ChessGame();
        game.newGame();

        // display chess pieces
        pieceLabels = new ArrayList<JLabel>();
        updatePiecesDisplay();

        // Add other informaiton on the side:
        addSideInfo();

        // set the background to white
        pane.setBackground(Color.WHITE);

        // set size, and make visible
        setSize(990,710);
        setVisible(true);
    }

    // Decides what to do when an action is performed (ex. A button pressed).
    public void actionPerformed (ActionEvent e) 
    {
        // only listen for board game pieces if no one has won the game yet
        if (!game.winner())
        {
            if (game.canPromote() != -1)
            {
                // listen to queen, knight, bishop and rook buttons for promoting a pawn,
                // promote a pawn accordingly 
                // (the name of the button matches the parameter in the promte method)
                // (canPromote() returns the space it can promote it to)

                game.promote(e.getActionCommand(), game.canPromote());

                // update the display of the pieces and on the side of the board
                updateDisplayNewTurn();
                updatePiecesDisplay();
            }
            else // don't listen to game board squares if the player must promote a pawn 
            // (can't make any other move)
            {
                // listen to the gameboard square buttons (name matches its index location)
                for (int r = 0; r < 8; r ++) // row
                {
                    for (int c = 0; c < 8; c ++) // column 
                    {
                        if (e.getActionCommand().equals(r + " " + c)) 
                        {
                            // variables for row and col index of clicked button
                            int row = 7 - r; // rows are opposite 
                            int col = c; // columns the same

                            if (game.validStartLoc(row, col)) 
                            // if the player selects one of its own pieces: valid start
                            {
                                // set start row and col
                                startRow = row;
                                startCol = col;
                                // the starting square has been selected
                                startSelect = true;
                                // display the green square around the square that was selected
                                displaySquare();
                            }
                            else if (startSelect && game.nextTurn(startCol, startRow, col, row))
                            // if player had already selected the start locaiton and
                            // the final location they've selected is a valid move, 
                            // make the move (done in the nextTurn method)
                            {
                                // the starting location is no longer selected
                                startSelect = false;

                                // update what is displayed:
                                updatePiecesDisplay();
                                updateDisplayNewTurn();
                            }
                        }
                    }
                }
            }

            if (e.getActionCommand().equals("kingside"))
            // castle kingside button pressed 
            {
                // castle kingside
                game.castle(true);
                // update the display
                updatePiecesDisplay();
                updateDisplayNewTurn();
            }
            else if (e.getActionCommand().equals("queenside"))
            // castle queenside button pressed
            {
                // castle queenside
                game.castle(false);
                // update the display                
                updatePiecesDisplay();
                updateDisplayNewTurn();
            }
            else if (e.getActionCommand().equals("Castle"))
            // castle button pressed (if only one option is available)
            {
                // castle kingside if it can, otherwise, castle queenside
                game.castle(game.canCastle() == 2);
                // update the display                
                updatePiecesDisplay();
                updateDisplayNewTurn();
            }
            else if (e.getActionCommand().equals("this resign"))
            // the player that is on their turn resigns
            {
                if (game.getWhiteTurn())
                    game.resign(true);
                else 
                    game.resign(false);
                updateDisplayNewTurn();
            }
            else if (e.getActionCommand().equals("other resign"))
            // the player that isn't on their turn resigns
            {
                if (game.getWhiteTurn())
                    game.resign(false);
                else 
                    game.resign(true);
                updateDisplayNewTurn();
            }
            else if ((e.getActionCommand().equals("Draw")))
            // the 2 players decide to draw
            {
                // draw 
                game.draw();
                // update the display
                updateDisplayNewTurn();
            }
        }
        else if ((e.getActionCommand().equals("New Game")))
        // the players decide to start a new game
        {
            // start a new game 
            game.newGame();
            // reset all other instance variables and update the display
            newGameDisplay();
        }

        if ((e.getActionCommand().equals("Help")))
        // the help button is pressed
        {
            // pop-up window with game rules
            showHelpWindow();
        }
    }

    public void addSideInfo()
    // add all of the inforamtion on the side of the game board initially, and 
    // initialize the instance varaibles of other components that will be added later 
    {
        // chess title:
        JLabel chessLabel = new JLabel("CHESS"); // create and set text
        chessLabel.setFont(new Font("Times New Roman",1,88)); // set font 
        chessLabel.setBounds(680, 0, 300, 100); // set location and size
        layeredPane.add(chessLabel, 1); // add to pane, 1st layer (bottom)

        // draw button
        drawButton = new JButton("Draw");
        drawButton.setBounds(700, 130, 120, 35);
        drawButton.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        drawButton.addActionListener(this); // make the frame listen to this button
        layeredPane.add(drawButton, 1);

        // help button
        helpButton = new JButton("Help");
        helpButton.setBounds(830, 130, 120, 35);
        helpButton.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        helpButton.addActionListener(this);
        layeredPane.add(helpButton, 1);

        // "Scores"
        scoresLabel = new JLabel("SCORES");
        scoresLabel.setFont(new Font("Myriad Pro",1,20));
        scoresLabel.setBounds(785, 210, 300, 50);
        layeredPane.add(scoresLabel, 1);

        // "Black Player" (label for score)
        blackScoreLabel = new JLabel("Black Player");
        blackScoreLabel.setFont(new Font("Myriad Pro",0,20));
        blackScoreLabel.setBounds(700, 240, 300, 50);
        layeredPane.add(blackScoreLabel, 1);

        // "White Player" (label for score)
        whiteScoreLabel = new JLabel("White Player");
        whiteScoreLabel.setFont(new Font("Myriad Pro",0,20));
        whiteScoreLabel.setBounds(700, 275, 300, 50);
        layeredPane.add(whiteScoreLabel, 1);

        // black player's score
        blackScore = new JLabel("0");
        blackScore.setFont(new Font("Myriad Pro",0,20));
        blackScore.setBounds(920, 240, 200, 50);
        layeredPane.add(blackScore, 1);

        // white player's score
        whiteScore = new JLabel("0");
        whiteScore.setFont(new Font("Myriad Pro",0,20));
        whiteScore.setBounds(920, 275, 200, 50);
        layeredPane.add(whiteScore, 1);

        // next turn box
        nextTurnBox = new JLabel(smallGreenBoxImage); // the small green box that surrounds whose turn it is
        nextTurnBox.setBounds(675, 300 , 300, 230);
        layeredPane.add(nextTurnBox, 1);

        // whose turn it is
        whoseTurnLabel = new JLabel("It's the WHITE PLAYER'S turn");
        whoseTurnLabel.setFont(new Font("Myriad Pro",0,18));
        whoseTurnLabel.setBounds(700, 355, 300, 50);
        whoseTurnLabel.setForeground(new Color(0, 166, 81)); // set green colour
        layeredPane.add(whoseTurnLabel, 1);

        // other player label (for resigning)
        otherPlayerLabel = new JLabel("BLACK PLAYER");
        otherPlayerLabel.setFont(new Font("Myriad Pro",0,18));
        otherPlayerLabel.setBounds(700, 500, 300, 50);
        layeredPane.add(otherPlayerLabel, 1);

        // resign buttons 
        thisResignButton = new JButton("Resign");
        thisResignButton.setBounds(700, 405, 250, 40);
        thisResignButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        thisResignButton.setActionCommand("this resign"); 
        thisResignButton.addActionListener(this);
        layeredPane.add(thisResignButton, 1);
        otherResignButton = new JButton("Resign");
        otherResignButton.setBounds(700, 550, 250, 40);
        otherResignButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        otherResignButton.setActionCommand("other resign");
        otherResignButton.addActionListener(this);
        layeredPane.add(otherResignButton, 1);

        // initialize other labels and buttons that aren't yet added:

        // during game
        // castle label (when the player can caslte both ways) :
        castleLabel = new JLabel("Castle:");
        castleLabel.setBounds(700, 430, 250, 40);
        castleLabel.setFont(new Font("Myriad Pro",0,16));
        gameResultLabel = new JLabel("");
        gameResultReasonLabel = new JLabel("");
        // castle button (when only one way is possible) :
        castleButton = new JButton("Castle");
        castleButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        castleButton.setBounds(700, 440, 250, 40);
        castleButton.addActionListener(this);
        // castle queenside button:
        castleQueensideButton = new JButton("queenside");
        castleQueensideButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        castleQueensideButton.setBounds(700, 460, 125, 40);
        castleQueensideButton.addActionListener(this);
        // castle kingside button: 
        castleKingsideButton = new JButton("kingside");
        castleKingsideButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        castleKingsideButton.setBounds(825, 460, 125, 40);
        castleKingsideButton.addActionListener(this);
        // promote label:
        promotePawnLabel = new JLabel("Promote your pawn to a:");
        promotePawnLabel.setBounds(700, 430, 250, 40);
        promotePawnLabel.setFont(new Font("Myriad Pro",0,16));
        // promote a pawn to queen button: 
        promoteToQueenButton = new JButton("Queen");
        promoteToQueenButton.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
        promoteToQueenButton.setBounds(700, 460, 62, 40);
        promoteToQueenButton.addActionListener(this);
        // to a knight:
        promoteToKnightButton = new JButton("Knight");
        promoteToKnightButton.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
        promoteToKnightButton.setBounds(762, 460, 63, 40);
        promoteToKnightButton.addActionListener(this);
        // to a bishop:
        promoteToBishopButton = new JButton("Bishop");
        promoteToBishopButton.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
        promoteToBishopButton.setBounds(825, 460, 63, 40);
        promoteToBishopButton.addActionListener(this);
        // to a rook:
        promoteToRookButton = new JButton("Rook");
        promoteToRookButton.setFont(new Font("Lucida Grande", Font.PLAIN, 8));
        promoteToRookButton.setBounds(888, 460, 62, 40);
        promoteToRookButton.addActionListener(this);
        // "you're in check" label:
        checkLabel = new JLabel("YOU'RE IN CHECK");
        checkLabel.setForeground(Color.RED); // set red colour
        checkLabel.setFont(new Font("Myriad Pro",1,18));
        checkLabel.setBounds(700, 361, 300, 50);

        // end of game
        // result label:
        gameResultLabel.setForeground(new Color(28, 117, 188)); // set blue colour
        // set semi-bold font
        Map<TextAttribute, Object> attributes = new HashMap<>(); // the new font
        attributes.put(TextAttribute.FAMILY, Font.DIALOG);
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD);
        attributes.put(TextAttribute.SIZE, 24); // set size 
        gameResultLabel.setFont(Font.getFont(attributes));
        // set size and location
        gameResultLabel.setBounds(675, -240, 300, 950);
        // center horizontally
        gameResultLabel.setHorizontalAlignment(0);
        // result reason label:
        gameResultReasonLabel.setForeground(new Color(28, 117, 188)); // set blue colour
        gameResultReasonLabel.setFont(new Font("Myriad Pro",0,20));
        gameResultReasonLabel.setBounds(670, 160, 300, 100);
        gameResultReasonLabel.setHorizontalAlignment(0);
        // new game button:
        newGameButton = new JButton("New Game"); 
        newGameButton.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        newGameButton.addActionListener(this);
        newGameButton.setBounds(725, 350, 200, 40);
    }

    /**
     * Update the info that is displayed (whose turn it is, the players’ scores, etc) after each turn. 
     */
    public void updateDisplayNewTurn()
    {
        if (!game.winner())
        // game hasn't ended 
        {
            // switch whose turn it is and display other options available, 
            // but only if the currnet player can't promote a pawn
            // (it would then stay that player's turn)
            if (game.canPromote() == -1)
            // can't promote a pawn
            {
                // update whose turn it is 
                if (game.getWhiteTurn())
                {
                    whoseTurnLabel.setText("It's the WHITE PLAYER'S turn");
                    otherPlayerLabel.setText("BLACK PLAYER");

                }
                else 
                {
                    whoseTurnLabel.setText("It's the BLACK PLAYER'S turn");
                    otherPlayerLabel.setText("WHITE PLAYER");
                }

                // display other information 
                if (game.check(game.findKing(game.getWhiteTurn()), game.getWhiteTurn()))
                // player is in check
                {
                    if (game.checkmate(game.findKing(game.getWhiteTurn())))
                    // player is in checkmate
                    {
                        // the game is over: checkmate 
                        displayEndGame(2);
                        // make sure the chess game register that the game has ended (sets win to false)
                        game.setWin(); 
                    }
                    else 
                    // player is in check 
                        displayCheck();
                }
                else if (game.stalemate(game.findKing(game.getWhiteTurn())))
                // game is in stalemate
                {
                    // the game is over: stalemate
                    displayEndGame(1);
                    // make sure the chess game registers that the game has ended (sets win to false)
                    game.setWin();
                }
                else if (game.checkmateImpossible())
                {
                    // the game is over: insufficient material 
                    displayEndGame(3);
                }
                else
                // player is not in check, and game hasn't ended from checkmate or stalemate
                {
                    if (game.canCastle() == 3)
                    // can castle both ways
                        displayCastle(true);
                    else if (game.canCastle() != 0) 
                    // can only castle one way
                        displayCastle(false);
                    else 
                    // doesn't have any of the above options: regular turn
                        displayRegularTurn();
                }
            }
            else 
            // can promte a pawn 
            {
                // so it must promote a pawn
                displayPromotePawn();
            }

            // update the other player's score (from previous turn)
            blackScore.setText(game.getBlackScore() + "");

            // update the score 
            whiteScore.setText(game.getWhiteScore() + "");
        }
        else
        // game has ended
            displayEndGame(0); // neither because of checkmate nor stalemate
    }

    // show the display when a piece has to promote a pawn 
    // (this appears after a player makes a certain move with a pawn, so castling and check 
    // are not possible)
    public void displayPromotePawn()
    {
        // remove components that don't need to be displayed:
        layeredPane.remove(castleLabel);
        layeredPane.remove(castleKingsideButton);
        layeredPane.remove(castleQueensideButton);
        layeredPane.remove(castleButton);

        // set locations of the components that need to be displayed
        // (they may have been moved from other displays)
        whoseTurnLabel.setLocation(700, 345);
        thisResignButton.setLocation(700, 390);
        otherPlayerLabel.setLocation(700, 540);
        otherResignButton.setLocation(700, 580);
        // remove the old next turn box
        layeredPane.remove(nextTurnBox);
        // add a new one of a different size and location
        nextTurnBox = new JLabel(bigGreenBoxImage);
        nextTurnBox.setBounds(675, 310, 300, 250);
        layeredPane.add(nextTurnBox, 1);
        // update the scores

        // display promoting pawn buttons
        layeredPane.add(promotePawnLabel, 1); // and label
        layeredPane.add(promoteToQueenButton, 1);
        layeredPane.add(promoteToKnightButton, 1);
        layeredPane.add(promoteToBishopButton, 1);
        layeredPane.add(promoteToRookButton, 1);
    }

    // show the display when a piece is in check
    // (the player can't castle if it's in check)
    public void displayCheck()
    {
        // remove components that don't need to be displayed:
        layeredPane.remove(castleLabel);
        layeredPane.remove(castleButton);
        layeredPane.remove(castleKingsideButton);
        layeredPane.remove(castleQueensideButton);
        layeredPane.remove(promotePawnLabel);
        layeredPane.remove(promoteToQueenButton);
        layeredPane.remove(promoteToKnightButton);
        layeredPane.remove(promoteToBishopButton);
        layeredPane.remove(promoteToRookButton);
        layeredPane.remove(promotePawnLabel);

        // set locations of the components that need to be displayed:
        otherPlayerLabel.setLocation(700, 500);
        otherResignButton.setLocation(700, 550);
        thisResignButton.setLocation(700, 405);
        whoseTurnLabel.setLocation(703, 338);

        // show that the player is in check:
        // remove the old next turn box
        layeredPane.remove(nextTurnBox);
        // add a new one of a different colour and size 
        nextTurnBox = new JLabel(redBoxImage);
        nextTurnBox.setBounds(675, 293, 300, 230);
        layeredPane.add(nextTurnBox, 1);
        // set colour of whose turn label to red
        whoseTurnLabel.setForeground(Color.RED);
        // add the check label ("YOU'RE IN CHECK")
        layeredPane.add(checkLabel,1);
    }

    // show the display when the player can castle, depending on whether or not it can caslte both ways
    // (this is not pssible when the player is in check)
    public void displayCastle (boolean bothWays)
    {
        // remove components that don't need to be displayed:
        layeredPane.remove(promotePawnLabel);
        layeredPane.remove(promoteToQueenButton);
        layeredPane.remove(promoteToKnightButton);
        layeredPane.remove(promoteToBishopButton);
        layeredPane.remove(promoteToRookButton);
        layeredPane.remove(promotePawnLabel);
        layeredPane.remove(checkLabel);
        // make the whose turn label green 
        whoseTurnLabel.setForeground(new Color(0, 166, 81));

        // set locations of some of the components that need to be displayed
        whoseTurnLabel.setLocation(700, 345);
        thisResignButton.setLocation(700, 390);

        if (bothWays)
        {
            // remove more components that don't need to be displayed:
            layeredPane.remove(castleButton);

            // set locations of the rest of the components that need to be displayed
            otherPlayerLabel.setLocation(700, 550);
            otherResignButton.setLocation(700, 600);
            // remove the old green box 
            layeredPane.remove(nextTurnBox);
            // add a new green box with a different colour and size
            nextTurnBox = new JLabel(bigGreenBoxImage);
            nextTurnBox.setBounds(675, 310, 300, 250);
            layeredPane.add(nextTurnBox, 1);

            // display queenside and kingside buttons
            layeredPane.add(castleKingsideButton, 1);
            layeredPane.add(castleQueensideButton, 1);
            layeredPane.add(castleLabel, 1); // and the castle label
        }
        else
        {
            // remove more components that don't need to be displayed:
            layeredPane.remove(castleLabel);
            layeredPane.remove(castleKingsideButton);
            layeredPane.remove(castleQueensideButton);

            // set locations of the rest of the components that need to be displayed
            thisResignButton.setLocation(700, 390);
            otherPlayerLabel.setLocation(700, 530);
            otherResignButton.setLocation(700, 570);
            // remove the old green box 
            layeredPane.remove(nextTurnBox);
            // add a new green box with a different colour and size
            nextTurnBox = new JLabel(medGreenBoxImage);
            nextTurnBox.setBounds(675, 300, 300, 250);
            layeredPane.add(nextTurnBox, 1);

            // display castle button
            layeredPane.add(castleButton, 1);
        }
    }

    public void displayRegularTurn()
    {
        // remove components that don't need to be displayed:
        layeredPane.remove(checkLabel);
        layeredPane.remove(castleLabel);
        layeredPane.remove(castleKingsideButton);
        layeredPane.remove(castleButton);
        layeredPane.remove(castleQueensideButton);
        layeredPane.remove(promotePawnLabel);
        layeredPane.remove(promoteToQueenButton);
        layeredPane.remove(promoteToKnightButton);
        layeredPane.remove(promoteToBishopButton);
        layeredPane.remove(promoteToRookButton);
        //removeSquare(); // remove the green square before the start of each turn 

        // reset locations of the components that need to be displayed:
        whoseTurnLabel.setLocation(700, 355);
        whoseTurnLabel.setForeground(new Color(0, 166, 81)); // set colour to green
        otherPlayerLabel.setLocation(700, 500);
        otherResignButton.setLocation(700, 550);
        thisResignButton.setLocation(700, 405);
        // remove the old green box 
        layeredPane.remove(nextTurnBox);
        // add a new green box with a different colour and size
        nextTurnBox = new JLabel(smallGreenBoxImage);
        nextTurnBox.setBounds(675, 300, 300, 230);
        layeredPane.add(nextTurnBox, 1);
    }

    public void displayEndGame(int endCase)
    // shows the display for the end of the game, based on how the game ended: 
    // 1 = stalemate
    // 2 = checkmate
    // 3 = checkmate impossible / insufficient material
    // 0 = neither option (one or both of the players resign) 
    {
        // remove components that don't need to be displayed
        layeredPane.remove(scoresLabel);
        layeredPane.remove(whiteScore);
        layeredPane.remove(blackScore);
        layeredPane.remove(whiteScoreLabel);
        layeredPane.remove(blackScoreLabel);
        layeredPane.remove(whoseTurnLabel);
        layeredPane.remove(otherPlayerLabel);
        layeredPane.remove(nextTurnBox);
        layeredPane.remove(drawButton);
        layeredPane.remove(thisResignButton);
        layeredPane.remove(otherResignButton);
        layeredPane.remove(castleLabel);
        layeredPane.remove(promotePawnLabel);
        layeredPane.remove(castleButton);
        layeredPane.remove(castleKingsideButton);
        layeredPane.remove(castleQueensideButton);
        layeredPane.remove(promoteToQueenButton);
        layeredPane.remove(promoteToKnightButton);
        layeredPane.remove(promoteToBishopButton);
        layeredPane.remove(promoteToRookButton);
        layeredPane.remove(checkLabel);
        layeredPane.remove(nextTurnBox);
        removeSquare();

        // set the game result label and reason for result label
        if (endCase == 1) // stalemate
        {
            gameResultReasonLabel.setText("Stalemate");
            gameResultLabel.setText("YOU TIED!");
        }
        else if (endCase == 2) // checkmate
        {
            gameResultReasonLabel.setText("Checkmate");
            if (game.getWhiteTurn())
                gameResultLabel.setText("BLACK PLAYER WON!");
            else 
                gameResultLabel.setText("WHITE PLAYER WON!");
        }
        else if (endCase == 3)
        // tie (checkmate impossible / insufficient material) 
        {
            gameResultLabel.setText("YOU TIED!");
            gameResultReasonLabel.setText("Draw: Insufficient Material");
        }
        else 
        {
            // tie (players decide to draw)
            if (game.tie())
            {
                gameResultLabel.setText("YOU TIED!");
                gameResultReasonLabel.setText("Draw: Mutual Agreement");
            }
            // white resigns 
            else if (game.whiteResign())
            {
                gameResultLabel.setText("BLACK PLAYER WON!");
                gameResultReasonLabel.setText("The White Player Resigned");
            }
            // black resigns
            else
            {
                gameResultLabel.setText("WHITE PLAYER WON!");
                gameResultReasonLabel.setText("The Black Player Resigned");
            }
        }
        // add them to the pane
        layeredPane.add(gameResultLabel, 1);
        layeredPane.add(gameResultReasonLabel, 1);

        // change location and size of help button
        helpButton.setBounds(725, 300, 200, 40);
        // add the 'new game' button 
        layeredPane.add(newGameButton, 1);
    }

    // show the display after a new game
    public void newGameDisplay()
    {
        // reinitialize this instance variable
        startSelect = false;

        // remove buttons and labels that are not needed:
        layeredPane.remove(gameResultLabel);
        layeredPane.remove(gameResultReasonLabel);
        layeredPane.remove(newGameButton);
        layeredPane.remove(checkLabel);
        layeredPane.remove(castleLabel);
        layeredPane.remove(castleButton);
        layeredPane.remove(castleKingsideButton);
        layeredPane.remove(castleQueensideButton);
        layeredPane.remove(promotePawnLabel);
        layeredPane.remove(promoteToQueenButton);
        layeredPane.remove(promoteToKnightButton);
        layeredPane.remove(promoteToBishopButton);
        layeredPane.remove(promoteToRookButton);
        layeredPane.remove(promotePawnLabel);

        // reset and add the text and buttons to the side:

        // draw button
        layeredPane.add(drawButton, 1);
        // help button 
        helpButton.setBounds(830, 130, 120, 35);
        layeredPane.add(helpButton, 1);
        // scores 
        layeredPane.add(scoresLabel, 1);
        whiteScore.setText("0");
        layeredPane.add(whiteScore, 1);
        blackScore.setText("0");
        layeredPane.add(blackScore, 1);
        layeredPane.add(whiteScoreLabel, 1);
        layeredPane.add(blackScoreLabel, 1);
        // next turn box
        nextTurnBox = new JLabel(smallGreenBoxImage);
        nextTurnBox.setBounds(675, 300 , 300, 230);
        layeredPane.add(nextTurnBox, 1);
        // whose turn it is label
        whoseTurnLabel.setText("It's the WHITE PLAYER'S turn");
        whoseTurnLabel.setBounds(700, 355, 300, 50);
        whoseTurnLabel.setForeground(new Color(0, 166, 81));
        layeredPane.add(whoseTurnLabel, 1);
        // other player label
        otherPlayerLabel = new JLabel("BLACK PLAYER");
        otherPlayerLabel.setBounds(700, 500, 300, 50);
        otherPlayerLabel.setFont(new Font("Myriad Pro",0,18));
        layeredPane.add(otherPlayerLabel, 1);
        // resign buttons 
        thisResignButton.setBounds(700, 405, 250, 40);
        layeredPane.add(thisResignButton, 1);
        otherResignButton.setBounds(700, 550, 250, 40);
        layeredPane.add(otherResignButton, 1);

        // update the display of the pieces on the board:
        updatePiecesDisplay();
    }

    // display the square that surrounds the piece that the player wants to move 
    public void displaySquare()
    {
        // set location based on the start row and column 
        // (the loaction of the piece that the player wants to move)
        selectSquare.setBounds(
            startCol * 80 - 5 +30, 
            Math.abs(7-startRow) * 80 - 5, 
            90, 
            90
        );
        // add to pane
        layeredPane.add(selectSquare, new Integer(3));
    }

    // remove the square that surrounds the piece that the player wants to move
    public void removeSquare()
    {
        // remove from pane
        layeredPane.remove(selectSquare);

        // use an empty jLabel to cover the removed square that would 
        // otherwise still shows up:
        JLabel tempLabel = new JLabel("");
        tempLabel.setBounds(0, 0, 670, 670);
        layeredPane.add(tempLabel, new Integer(3));
    }

    // display the pieces in the right spots 
    public void updatePiecesDisplay() 
    {
        // add the square border around the piece that the player wants to move 
        // if the player is selecting this peice
        // remove the square that surrounds the piece that the player wants to move
        // if the player already selected it 
        if (!startSelect || !game.winner())
            removeSquare();

        // remove the old pieces
        for (int i = 0; i < pieceLabels.size(); i ++)
        {
            layeredPane.remove(pieceLabels.get(i));
        }

        // create a new array for the new pieces
        pieceLabels = new ArrayList<JLabel>();

        // set temporary variables:
        // the white and black pieces in the game
        ArrayList <ChessPiece> WPieces = game.getWhitePieces();
        ArrayList <ChessPiece> BPieces = game.getBlackPieces();
        // the image of the piece that is added 
        ImageIcon image;

        // go through arrays of chess pieces
        // white pieces
        for (int i = 0; i < WPieces.size(); i ++)
        {
            // check type of piece, add image accordingly
            if (WPieces.get(i) instanceof King)
                image = new ImageIcon("images/White King.png");
            else if (WPieces.get(i) instanceof Queen)
                image = new ImageIcon("images/White Queen.png");
            else if (WPieces.get(i) instanceof Knight)
                image = new ImageIcon("images/White Knight.png");
            else if (WPieces.get(i) instanceof Bishop)
                image = new ImageIcon("images/White Bishop.png");
            else if (WPieces.get(i) instanceof Pawn)
                image = new ImageIcon("images/White Pawn.png");
            else // rook
                image = new ImageIcon("images/White Rook.png");

            // add image for the piece to the pieceLabels array in correct locaiton
            pieceLabels.add(new JLabel(image));
            pieceLabels.get(i).setBounds((WPieces.get(i).getCol()) * 80 +30, (7 - WPieces.get(i).getRow()) * 80, 80, 80);
        }
        // black pieces
        for (int i = 0; i < BPieces.size(); i ++)
        {
            // check type of piece, add image accordingly
            if (BPieces.get(i) instanceof King)
                image = new ImageIcon("images/Black King.png");
            else if (BPieces.get(i) instanceof Queen)
                image = new ImageIcon("images/Black Queen.png");
            else if (BPieces.get(i) instanceof Knight)
                image = new ImageIcon("images/Black Knight.png");
            else if (BPieces.get(i) instanceof Bishop)
                image = new ImageIcon("images/Black Bishop.png");
            else if (BPieces.get(i) instanceof Pawn)
                image = new ImageIcon("images/Black Pawn.png");
            else // rook
                image = new ImageIcon("images/Black Rook.png");

            // add image for the piece to the pieceLabels array in correct locaiton 
            pieceLabels.add(new JLabel(image));
            pieceLabels.get(i+WPieces.size()).setBounds((BPieces.get(i).getCol()) * 80 +30, (7 - BPieces.get(i).getRow()) * 80, 80, 80);
        }

        // add all of the piece images to the board 
        for (int i = 0; i < pieceLabels.size(); i ++)
        {
            layeredPane.add(pieceLabels.get(i), new Integer(2));
        }
    }

    public void showHelpWindow()
    // show the help dialog window
    {
        // make a text area where the rules will go 
        JTextArea helpText = new JTextArea("HOW TO PLAY CHESS");
        // change its font
        helpText.setFont(new Font("Helvectia", Font.PLAIN, 14));

        // add the rules

        // the set up 
        helpText.append("\n\n\nSET UP");
        helpText.append("\n\nAt the beginning of a game, the chessboard with its pieces " + 
            "is always laid out the same way. Each player has the light colour square in the " +
            "bottom right-hand side. The second row/rank (row 2 for white pieces and row 7 for " +
            "black pieces) is filled with pawns. The rooks go in the corners, then the knights next " +
            "to them, followed by the bishops. The queen then goes on the remaining square of " +
            "her similar colour (white on light, black on dark), and the king goes on the remaining " +
            "square in that row. The pieces are arranged like this at the start of every game. ");

        // how to start the game 
        helpText.append("\n\n\nSTARTING THE GAME");
        helpText.append("\n\nThe player with the white pieces always makes the first move. " +
            "Players may decide who will be white by chance (ex. flipping a coin). After white " +
            "makes its move, black makes its move, then it alternates between white and black until " +
            "the end of the game. To start a new game after one has ended, simply click the " +
            "new game button.");

        // how to move the pieces
        helpText.append("\n\n\nMOVING THE PIECES");
        helpText.append("\n\nThere are 6 different kinds of pieces, each of which move differently. " +
            "Pieces cannot pass through other pieces, with the exception of the knight, which can " +
            "jump over pieces. They also cannot move to a square with one of their own pieces (of " +
            "the same colour), or move off the chessboard (must move to another square). A piece " +
            "can capture an opponent’s piece if it moves to a spot with the piece." +
            "\n\nTo move a piece, select the piece that you would like to move, then select the " +
            "location that you would like to move that piece to. You will only be able to select " +
            "your own pieces, and you will not be able to make illegal moves. If you decide to move a " +
            "different piece, you can select that other piece instead. However, after a move is made," +
            " it cannot be undone.");
        // moving the king
        helpText.append("\n\nKing:\nThe king can only move one square in any direction" +
            " (up, down, sideways, or diagonally). The King may never move himself into check. It would" +
            " be in check if it is in a spot where an opponent’s piece can capture it.");
        // queen
        helpText.append("\n\nQueen:\nThe queen can move any number of squares in any one " +
            "straight direction (forward, backward, sideways, or diagonally). So it can move both like" +
            " a rook and a bishop (see bellow).");
        // rook
        helpText.append("\n\nRook:\nThe rook may move any number of squares either forwards" +
            ", backwards or sideways.");
        // bishop
        helpText.append("\n\nBishop:\nThe bishop may move any number of squares but only " +
            "diagonally.");
        // knight
        helpText.append("\n\nKnight:\nKnights move two squares in one direction, and then " +
            "one more square at a 90 degree angle, its path making an “L” shape.");
        // pawn
        helpText.append("\n\nPawn:\nPawns can only move forward one space, with the exception of their " +
            "first move, where they can move forward two spaces. They also move and capture in different " +
            "ways. To move (move to an empty square) they have to move forwards, and to capture a piece " +
            "(move to a square with one of the opponent’s piece) they must move diagonally.");

        // the special rules
        helpText.append("\n\n\nSPECIAL RULES");
        // promoting a pawn
        helpText.append("\n\nPromoting a pawn:\nIf a pawn reaches the other side of the board it " +
            "must be promoted to either a queen, knight, bishop or rook (meaning it becomes that " +
            "chess piece). To promote a pawn, simply click the button that shows the piece that " +
            "you would like to promote your pawn to (queen, knight, bishop, or rook).");
        // en passant
        helpText.append("\n\nEn Passant:\nIf a pawn moves two squares on its first move, and by " +
            "doing so lands to the side of an opponent's pawn, that other pawn has the option of " +
            "capturing the first pawn as if it had only moved forward one square. This special " +
            "move must be done immediately after the first pawn moves forward two squares. ");
        // castling
        helpText.append("\n\nCastling:\nCastling counts as one turn that the player makes. A " +
            "player can castle by moving its king two squares to one side and the rook from that " +
            "side's corner right next to the king on the opposite side. However, in order to " +
            "castle, it must be that king's and rook’s very first move, there cannot be any " +
            "pieces between the king and rook, and the king may not be in check or pass through " +
            "check. If you castle with the rook on the left side (the side closer to the queen), " +
            "you castle “queenside”, and if you castle to the right side (the side closer to the " +
            "king), you castle “kingside”.\nIf castling is possible, a button will show up on the " +
            "side and you must press that to castle (DO NOT move the king and rook yourself). " +
            "If castling is possible both ways, you will have the option to castle queenside or " +
            "kingside (click the “queenside” or “kingside” button). ");

        // the scores
        helpText.append("\n\n\nTHE SCORES");
        helpText.append("\n\nThe scores of each player are only relative and are based on the number " +
            "and type of the opponent’s pieces that each player captures. In our point system, a " +
            "pawn is worth 1 point, a knight 3 points, a bishop 3 points, a rook 5 points, and a " +
            "queen 9 points. The scores can help you know when to capture, exchange, or make other " +
            "moves, as well as get an understanding of who has captured more valuable pieces. " +
            "However, at the end of the game, these points don’t mean anything."
        );

        // winning the game
        helpText.append("\n\n\nWINNING THE GAME");
        helpText.append("\n\nA player wins a game of chess either when the opponent’s king is put " +
            "into checkmate or when the opponent resigns.");
        // checkmate
        helpText.append("\n\nCheckmate:\nThe purpose of chess is to checkmate the opponent’s " +
            "king. This happens when the king is put into check (it is in a spot where an " +
            "opponent’s piece can capture it) and cannot get out of check. There are three " +
            "ways a king can get out of check: move out of the way, block the check with " +
            "another piece, or capture the piece threatening the king. ");
        // resigning
        helpText.append("\n\nResigning:\nEach player can resign at any point in the game. " +
            "To do so, simply press the resign button with the appropriate label (ex. if it " +
            "is the white player’s turn, and the black player wants to resign, click the " +
            "resign button at the bottom under “Black Player”). When one player decides to " +
            "resign, the other player wins.");

        // drawing the game
        helpText.append("\n\n\nDRAWING THE GAME ");
        helpText.append("\n\nA game can end in a draw (neither player wins, the players tie) " +
            "in multiple ways.");
        // stalemate
        helpText.append("\n\nStalemate:\nA player is not in check and there are no legal " +
            "moves that it can make.");
        // insufficient material 
        helpText.append("\n\nInsufficient Material:\nCheckmate is impossible with the " +
            "number and type of pieces in the game that haven’t been captured (ex. two " +
            "kings and a bishop).");
        // mutual agreement
        helpText.append("\n\nMutual Agreement:\nBoth players decide to end the game in " +
            "a draw. To do so, simply click the “draw” button at the right side of the board, " +
            "at the top (make sure that both players have agreed to draw). ");
        // other cases not included in this verison 
        helpText.append("\n\nOther cases:\nOther cases of draw that are not included in this " +
            "version of the game are threefold repetition and the 50 moves rule.");

        // source 
        helpText.append("\n\n\n\nRules are adapted from: https://www.chess.com/learn-" +
            "how-to-play-chess");

        // make the text fit in the text area (go to a new line at the end)
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true); // (make the whole word go to a new line) 
        // make it not editable 
        helpText.setEditable(false);

        // add the text area to a scroll pane (this allows the area to be scrollable)
        JScrollPane helpPane = new JScrollPane(helpText);
        helpPane.setPreferredSize(new Dimension(600, 400)); // set size of pane 

        // display the rulesPane as a dialog, from this frame (ChessGameFrame)
        // the title of the window is "Help" and it is a plain message (no icons shown)
        JOptionPane.showMessageDialog(this, helpPane, "Help", JOptionPane.PLAIN_MESSAGE);
    }
}