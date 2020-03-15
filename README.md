# Chess Game

A two-player chess game developed in Java. 

![screenshot of chess game](https://github.com/mesropy/chess-game/blob/master/screenshot.png)

## Features
- Checking for check, checkmate and stalemate 
- Special Rules:
  - Promoting a Pawn
  - Castling
  - En Passant 
- Calculating scores
- Help pop-up menu
- Drawing or resigning at any time
- Restarting the game
- GUIs

### My Role
- implementing Java GUIs
- designing structure (how the classes and methods in them interact with each other)
- code I wrote:
  - ChessGameRunner class
  - isLegalMove and clearPath methods in each ChessPiece class
  - moving the pieces (including checking for legal moves) in ChessGame class
  
### UML Diagram

![UML diagram image](https://github.com/mesropy/chess-game/blob/master/UML.png)

### 

## Improvements
- read instructions from text file instead of hard-coding them
- divide long methods into helper functions
- separate back-end and front-end in ChessGameRunner class

## Possible Additions / Extensions 
- highlight possible moves when a piece is selected 
- implement more special rules (e.g. 50 move rule)
- make the window size dynamic 
- add one-player option
