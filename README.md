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
- Implementing Java GUIs
- Designing the structure (how the classes and methods in them interact with each other)
- Code I wrote:
  - `ChessGameRunner` class
  - `isLegalMove` and `clearPath` methods in each `ChessPiece` class
  - Moving the pieces (including checking for legal moves) in the `ChessGame` class
  
### UML Diagram

![UML diagram image](https://github.com/mesropy/chess-game/blob/master/UML.png)

### 

## Improvements
- Read instructions from text file instead of hard-coding them
- Divide long methods into helper functions
- Separate back-end and front-end in `ChessGameRunner` class

## Possible Additions / Extensions 
- Highlight possible moves when a piece is selected 
- Implement more special rules (e.g. 50 move rule)
- Make the window size dynamic 
- Add one-player option
