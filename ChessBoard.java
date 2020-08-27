// Filename: ChessBoard.java
//
// Contains class ChessBoard that creates a linkedlist of chessboard where each node holds a chesspiece to be placed
//
// This needs to be compiled with ChessPiece.java, Node.java, Utilities.java, King.java, Queen.java, Knight.java, Rook.java, Bishop.java and Pawn.java.
//
// Run make with associated Makefile to get ChessBoard.jar.
// 
// The format to run this is:
//                 java -jar ChessBoard.jar <INPUT_FILE> <OUTPUT_FILE>
// 
// Each line of the input file is a different chessboard.
// The line starts with two integers, denote (column, row) of an 8X8 chessboard.
// This is followed by a colon, and then a sequence of chesspieces given as <char> <column> <row>.
// The char denotes the chesspiece, and is capitalized if it denotes a black piece.
// Check out the HW2 description for details.
// 
//
// Santrupti Nerli, Jan 2017. C. Seshadhri, Oct 2017.
//
import java.io.*;

class Square{
  int col, row;

  public Square(){
    col = -1;
    row = -1;
  }

}
class ChessBoard {

  private static Node head; // linkedlist to store chesspieces
  private static int board_size; // board_size
  public static BufferedWriter writer; // write to write to file
  private static int king_attacked_c=-1, king_attacked_r=-1;
  private static boolean end_sq = false;
  

  // constructor
  public ChessBoard() {
    head = new Node();
  }

  // Method to perform insertion at the front of the list
  // Input: Node to be inserted
  // Output: void
  public Node insert(Node piece) {
    Node temp = head.getNext();
    head.setNext(piece);
    piece.setNext(temp);
    return head;
  }
  
  public static void delete(Node piece){
  Node temp;
  Node prev = head;
  Node curr;
  
  if(head == null){
    return;
  }
  temp = head.getNext();
  
  while(temp != null){
    if((piece.getCol() == temp.getCol()) && 
      (piece.getRow() == temp.getRow()) &&
        (piece.getColor() == temp.getColor())){
      if(temp == head){
        head = temp.getNext();
      }else{
        curr = temp.getNext();
        prev.setNext(curr);
      }
      return; 
    }else{
      prev = temp;
      temp = temp.getNext();
    }           
  }
  }

  // Method to find Node in a given location
  // Input: integer row and column to look for
  // Output: Node found
  public static Node findChessPiece(int row, int col) {
    Node piece = head.getNext();
    while(piece != null) {
      if(piece.getRow() == row && piece.getCol() == col) {
        return piece;
      }
      piece = piece.getNext();
    }
    return null;
  }

  // Method to count the number of chesspieces for a given type
  // This method will helps us check the validity case
  // Input: character color
  // Output: returns the count
  public static int countPiecesOfType(char pieceType) {
    Node piece = head.getNext();
    int pieceCtr = 0;
    // loop through to check if the same piece type is found
    while(piece != null) {
      if(Utilities.returnChessPieceType(piece) == pieceType) {
        pieceCtr++;
      }
      piece = piece.getNext();
    }
    return pieceCtr;
  }

  // Method to count the number of chesspieces on a single location
  // This method will helps us check the validity case
  // Input: integer row and column
  // Output: returns the count
  public static int countPiecesInLocation(int row, int col) {
    Node piece = head.getNext();
    int pieceCtr = 0;
    // loop through to check if any two pieces overlap
    while(piece != null) {
      if(piece.getRow() == row && piece.getCol() == col) {
        pieceCtr++;
      }
      piece = piece.getNext();
    }
    return pieceCtr;
  }

  // Method to check if two pieces occupy the same place
  // This method utilizes the countPiecesInLocation method to see
  // if there are more than two pieces in a single location
  // Input: none
  // Output: returns true if two pieces occupy same position
  public static boolean twoPiecesOccupySamePosition() {
    Node piece = head.getNext();
    // loop through and see if any of the pieces overlap
    while(piece != null) {
      if(countPiecesInLocation(piece.getRow(), piece.getCol()) > 1) {
        return true;
      }
      piece = piece.getNext();
    }
    return false;
  }

  // Method to check validity
  // basically looks if there are not two chesspieces in the same location and
  // one each colored king is present
  // Input: none
  // Output: returns if it is valid or not
  public static boolean checkValidity() {
    if(!twoPiecesOccupySamePosition()) {
      return true;
    }
    else {
      return false;
    }
  }

  // Method to check if any piece exists in the query location
  // Input: integer array query
  // Output: returns the piece (in character) if found otherwise just returns '-'
  public char findChessPiece(int[] query) {
    int col = query[0];
    int row = query[1];
    Node foundPiece = findChessPiece(row, col);
    if ( foundPiece != null) {
      return Utilities.returnChessPieceType(foundPiece);
    }
    return '-';
  }

  // Method to check if two nodes given are different or the same ones
  // It serves as a helper when trying to find the attack
  // Input: two nodes
  // Output: returns if they are same or different pieces
  public static boolean isDifferent(Node one, Node other) {
    if(one.getRow() == other.getRow() && one.getCol() == other.getCol() && one.getColor() == other.getColor()) {
      return false;
    }
    return true;
  }

  // Method to see if any of the pieces attack
  // as soon as you encounter the first attack, just print it and return
  // Input: Column and Row
  // Output: returns if piece here is attacking another piece
  public static boolean determineAttacking(int tcol, int trow) {
    Node piece = findChessPiece(trow, tcol); // get piece at query position
    if (piece == null) // nothing at query piece
        return false; // vacuously false

    // now loop through all pieces to check for attack
    // get the first valid chesspiece (remember not the head)
    Node other = head.getNext();
    // loop through each of the remaining chesspieces and check for attack
    while(other != null) {
        if(isDifferent(piece, other) && piece.getColor() != other.getColor() && piece.getChessPiece().isAttacking(other.getRow(), other.getCol()))  // found an attack!
            return true;
        other = other.getNext();
    }
    return false; // no attack
  }

  // Method to write to the analysis.txt file
  // Input: String to write
  // Output: void, just write
  public void writeToAnalysisFile(String stringToWrite) {
    try {
        writer.write(stringToWrite);
    }
    catch (Exception e) {
        Utilities.errExit("Exception occurred while trying to write to file: writeToAnalysisFile"); // throw a generic exception if failure to read occurs
    }
  }

  // Method to iterate through the list and update a 2D array for printing the board
  // onto the console
  // Input: integer board number read from input.txt
  // Output: void, jusr print the solution
  public static void convertFromListToMatrixAndPrint(int board_no) {
    // Initialize isFilled board
    char[][] isFilled = new char[board_size+1][board_size+1];
    for(int i = 0; i < board_size; i++) {
      for(int j = 0; j < board_size; j++) {
        isFilled[i][j] = '-';
      }
    }
    // iterate through the list and update isFilled matrix
    Node piece = head.getNext();
    while(piece != null) {
      System.out.println(piece.getRow()+" "+piece.getCol());
      isFilled[piece.getRow()][piece.getCol()] = Utilities.returnChessPieceType(piece);
      piece = piece.getNext();
    }

    System.out.println("Board No: " + board_no);
    Utilities.printSolution(isFilled, board_size);
  }

  // Method to read from input.txt
  // for each chessboard and query, perform all the required operations
  // an then proceed further
  // Input: BufferedReader reader, for input file
  // Output: void, just read and perform requested operations
  public static void readFromInputFile(BufferedReader reader) {

    int lineCtr = 0;
    ChessBoard c = null;
    Node piece_move;
    Square from_sq, to_sq;
    boolean currCol;
    boolean legalmove;
    boolean ignoreMove;

    from_sq = new Square();
    to_sq = new Square();

    try {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] board_tokens = line.split(":"); // Reader assumes that the input format is as given in the instruction
            // Splitting by colon, first token should have two integers, for column and row

            String[] boardPositions = board_tokens[0].split(" "); //split arty before column

            c = new ChessBoard(); //creates a chessboard
            board_size = 8; //board size is 8

            if (board_tokens.length%3 == 1) // number of parts after colon not divisible by 3, so some problem
                Utilities.errExit("Input line before colon does not have number of strings that are divisible by 3. Incorrect format.\n"+line);

             // loop over strings in board_tokens, create piece and insert it into board
            for(int i = 0; i < boardPositions.length; i+=3){
                head = c.insert(new Node(boardPositions[i].charAt(0), Integer.parseInt(boardPositions[i+2]), Integer.parseInt(boardPositions[i+1])));
            }
          

            //query[0] = Integer.parseInt(query_tokens[0]); // parse tokens to get the query column
            //query[1] = Integer.parseInt(query_tokens[1]); // parsing to get query row

           
            String[] tokenMovement = board_tokens[1].trim().split(" "); // split part after colon by space


            ignoreMove = false;
            if(kingCheck() == false){ //checks if there are both kings on the board
              from_sq.col = Integer.parseInt(tokenMovement[0]);
              from_sq.row = Integer.parseInt(tokenMovement[1]);
              to_sq.col = Integer.parseInt(tokenMovement[2]);
              to_sq.row = Integer.parseInt(tokenMovement[3]);
              writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
              ignoreMove = true;
              System.out.println("Missing one or both of the kings");
            }else if(!checkValidity()){
              from_sq.col = Integer.parseInt(tokenMovement[0]);
              from_sq.row = Integer.parseInt(tokenMovement[1]);
              to_sq.col = Integer.parseInt(tokenMovement[2]);
              to_sq.row = Integer.parseInt(tokenMovement[3]);
              writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
              ignoreMove = true;
              System.out.println("Two pieces occupy the same spot");
            }

            if(ignoreMove == false){
              currCol = true; //white moves first
              legalmove = true;
              for(int i = 0; i < tokenMovement.length; i += 4) {

                from_sq.col = Integer.parseInt(tokenMovement[i]);
                from_sq.row = Integer.parseInt(tokenMovement[i+1]);
                to_sq.col = Integer.parseInt(tokenMovement[i+2]);
                to_sq.row = Integer.parseInt(tokenMovement[i+3]);


                  piece_move = new Node();
                  piece_move = findChessPiece(from_sq.row, from_sq.col);
                  if(piece_move == null){
                    writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
                    System.out.println("No chess Piece exist at "+ from_sq.col + " " + from_sq.row );
                    legalmove = false;
                    break;
                  }
                    //check for right color
                    //check for piece at source for first col,row
                    //check for piece at destination; if same color invalid move, if other color piece at destination delete other piece after move
                    //create intermediate postions and check for piece
                    //move the piece: add destination node and remove source node
                    //check for attack on king of same color
                  
                  if(performMove(c, piece_move, from_sq, to_sq, currCol) == false) {
                    legalmove = false;
                    break;
                  }
                    currCol = !currCol; //switches the color 
                } 
              if (legalmove) {
                  writer.write("legal\n");
                  System.out.println("Complete board number: " + lineCtr);
              }
            }
            // perform the requested operations
            lineCtr++; // move to the next line
        }
        reader.close();
    }
    catch (NumberFormatException e) {
        Utilities.errExit("All arguments must be integers"); // throw error incase parsing integer fails
    }
    catch (IndexOutOfBoundsException e) {
        Utilities.errExit("Array index is out of bounds"); // throw error when inserting elements into arrays fail
    }
    catch (Exception e) {
        Utilities.errExit("Exception occurred trying to read file"); // throw a generic exception if failure to read occurs
    }
  }

  // Method to perform all the requested operations
  // namely, check validity, perform the search query
  // check for attack
  // Input: ChessBoard and the query
  // Output: returns the count
 
//my methods
    public static boolean kingCheck(){
      boolean foundBKing = false;
      boolean foundWKing = false; 

      Node piece = head.getNext();
      while(piece != null) {
        if((piece.getChessPiece() instanceof King) &&
            (piece.getColor() == true)) {
          foundWKing = true;
          break;
        }
        piece = piece.getNext();
      }

      piece = head.getNext();  
      while(piece != null) {
        if((piece.getChessPiece() instanceof King) &&
            (piece.getColor() == false)) {
          foundBKing = true;
          break;
        }
        piece = piece.getNext();
      }

      if((foundBKing == true) && (foundWKing == true)){
        return true;
      }else{
        return false;
      }

    }
  
    private static boolean kingAttacked(boolean currColor) {
      Node king_piece=null;
      boolean found_king = false;
      boolean kattack = false;
      ChessPiece cpiece;
      Square from_sq = new Square();
      Square to_sq = new Square();
     
 
      Node piece = head.getNext();
      while(piece != null) {
        if((piece.getChessPiece() instanceof King) &&
            (piece.getColor() == currColor)) {
          king_piece = piece;
          found_king = true;
          break;
        }
        piece = piece.getNext();
      }
      if (found_king == false)
        return false;
      to_sq.col = king_piece.getCol();
      to_sq.row = king_piece.getRow();
      piece = head.getNext();
      while(piece != null) {
        if(isDifferent(king_piece, piece) && king_piece.getColor() != piece.getColor() && 
            piece.getChessPiece().isAttacking(king_piece.getRow(), king_piece.getCol())) {  
          // found a possible attack!
          cpiece = piece.getChessPiece();
          from_sq.col = piece.getCol();
          from_sq.row = piece.getRow();
          if(cpiece instanceof Queen) {
            if((moveQueen(piece, from_sq, to_sq) == false) && end_sq)
              kattack = true;
          } else if(cpiece instanceof Rook) {
            if((moveRook(piece, from_sq, to_sq) == false) && end_sq)
              kattack = true;   
          } else if(cpiece instanceof Bishop) {
            if((moveBishop(piece, from_sq, to_sq) == false) && end_sq)
              kattack = true;
          } else {
            kattack = true;
          }
        }
        if (kattack) {
          king_attacked_c = piece.getCol();  // debug statement
          king_attacked_r = piece.getRow(); // debug statement
          return true;
        } else {
          piece = piece.getNext();
          }
        }  
      return false;
    }
    
    public static boolean rowUp(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextR = from_sq.row+1; nextR <= to_sq.row; nextR++){
        tempN = findChessPiece(nextR, from_sq.col);
        if(tempN != null){
          if (nextR == to_sq.row)
            end_sq = true;
          return false;
        }
      }
      return true;
    }

    public static boolean rowDown(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextR = from_sq.row-1; nextR >= to_sq.row; nextR--){
        tempN = findChessPiece(nextR, from_sq.col);
        if(tempN != null){
          if (nextR == to_sq.row)
            end_sq = true;
          return false;
        }
      }
      return true;
    }

    public static boolean colRight(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextC = from_sq.col+1; nextC <= to_sq.col; nextC++){
        tempN = findChessPiece(from_sq.row, nextC);
        if(tempN != null){
          if (nextC == to_sq.col)
            end_sq = true;
          return false;
        }
      }
      return true;
    }

    public static boolean colLeft(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextC = from_sq.col-1; nextC >= to_sq.col; nextC--){
        tempN = findChessPiece(from_sq.row, nextC);
        if(tempN != null){
          if (nextC == to_sq.col)
            end_sq = true;
          return false;
        }
      }
      return true;
    }

    public static boolean upDiagRight(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextC = from_sq.col+1, nextR = from_sq.row+1; (nextC <= to_sq.col) && (nextR <= to_sq.row) ; nextC++, nextR++){
        tempN = findChessPiece(nextR, nextC);
        if(tempN != null){
          if ((nextC == to_sq.col) && (nextR == to_sq.row))
            end_sq = true;
          return false;
        }
      }
      return true;
    }

    public static boolean upDiagLeft(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextC = from_sq.col-1, nextR = from_sq.row+1; (nextC >= to_sq.col) && (nextR <= to_sq.row) ; nextC--, nextR++){
        tempN = findChessPiece(nextR, nextC);
        if(tempN != null){
          if ((nextC == to_sq.col) && (nextR == to_sq.row))
            end_sq = true;
          return false;
        }
      }
      return true;
    }

    public static boolean lowDiagRight(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextC = from_sq.col+1, nextR = from_sq.row-1; (nextC <= to_sq.col) && (nextR >= to_sq.row) ; nextC++, nextR--){
        tempN = findChessPiece(nextR, nextC);
        if(tempN != null){
          if ((nextC == to_sq.col) && (nextR == to_sq.row))
            end_sq = true;
          return false;
        }
      }
      return true;
    }
    public static boolean lowDiagLeft(Node node_mv, Square from_sq, Square to_sq){
      Node tempN;

      for(int nextC = from_sq.col-1, nextR = from_sq.row-1; (nextC >= to_sq.col) && (nextR >= to_sq.row) ; nextC--, nextR--){
        tempN = findChessPiece(nextR,nextC);
        if(tempN != null){
          if ((nextC == to_sq.col) && (nextR == to_sq.row))
            end_sq = true;
          return false;
        }
      }
      return true;
    }

    public static boolean moveRook(Node node_mv, Square from_sq, Square to_sq){
      int c1, c2, r1, r2;

      c1 = from_sq.col;
      r1 = from_sq.row;
      c2 = to_sq.col;
      r2 = to_sq.row;

      if(c1 == c2){
        if(r1 < r2){
          return (rowUp(node_mv, from_sq, to_sq));
        }
        else{
          return (rowDown(node_mv, from_sq, to_sq));
        }
      }else if(r1 == r2){
        if(c1 < c2){
          return (colRight(node_mv, from_sq, to_sq));
        }
        else{
          return (colLeft(node_mv, from_sq, to_sq));
        }
      }
      return false;
    }
    
    public static boolean moveBishop(Node node_mv, Square from_sq, Square to_sq){
      int c1, c2, r1, r2;

      c1 = from_sq.col;
      r1 = from_sq.row;
      c2 = to_sq.col;
      r2 = to_sq.row;
      
      if((Math.abs(r1 - r2)) == (Math.abs(c1 - c2))){
        if((c1 > c2) && (r1 > r2 )){
          return (lowDiagLeft(node_mv, from_sq, to_sq));
        }else if((c1 > c2) && (r1 < r2)){
          return (upDiagLeft(node_mv, from_sq, to_sq)); 
        }else if((c1 < c2) && (r1 < r2)){
          return (upDiagRight(node_mv, from_sq, to_sq));
        }else
          return (lowDiagRight(node_mv, from_sq, to_sq));
      }
      return false;
      
    }
    
    public static boolean moveQueen(Node node_mv, Square from_sq, Square to_sq){
      int c1, c2, r1, r2;

      c1 = from_sq.col;
      r1 = from_sq.row;
      c2 = to_sq.col;
      r2 = to_sq.row;

      if(c1 == c2){
        if(r1 < r2){
          return (rowUp(node_mv, from_sq, to_sq));
        }
        else{
          return (rowDown(node_mv, from_sq, to_sq));
        }
      }else if(r1 == r2){
        if(c1 < c2){
          return (colRight(node_mv, from_sq, to_sq));
        }
        else{
          return (colLeft(node_mv, from_sq, to_sq));
        }
      }else if((Math.abs(r1 - r2)) == (Math.abs(c1 - c2))){
        if((c1 > c2) && (r1 > r2 )){
          return (lowDiagLeft(node_mv, from_sq, to_sq));
        }else if((c1 > c2) && (r1 < r2)){
          return (upDiagLeft(node_mv, from_sq, to_sq)); 
        }else if((c1 < c2) && (r1 < r2)){
          return (upDiagRight(node_mv, from_sq, to_sq));
        }else
          return (lowDiagRight(node_mv, from_sq, to_sq));
      }
      return false;
    }
    
    public static boolean reachPawn(Node node_mv, Square from_sq, Square to_sq) {
      ChessPiece piece = node_mv.getChessPiece();
      
      if(piece instanceof Pawn) {
        if((node_mv.getColor() == true) && ((node_mv.getRow() + 1) == to_sq.row)&&(node_mv.getCol() == to_sq.col)) {
            return true;
          }else if((node_mv.getColor() == true) && ((node_mv.getRow() - 1) == to_sq.row) && (node_mv.getCol() == to_sq.col)){
            return true;
          }else {
            return false;
          } 
      }
      return false;
      
    }

  public static boolean performMove(ChessBoard c, Node node_mv, Square from_sq, Square to_sq, boolean currCol) {

      
      try{// Check for validity here  
        Node destinationP = new Node();

        if(node_mv.getColor() != currCol){
          writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
          System.out.println("Other color Piece should move "+ from_sq.col + " " + from_sq.row
              + " " + to_sq.col + " " + to_sq.row);
          return false;
        }
        
        ChessPiece piece = node_mv.getChessPiece();
        destinationP = findChessPiece(to_sq.row, to_sq.col);
        //Check if source piece can reach to destination
        if (node_mv.getChessPiece().isAttacking(to_sq.row, to_sq.col) == false) {
          //special case for Pawn
          if(reachPawn(node_mv, from_sq, to_sq) == false) {
            writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
            System.out.println("Piece can not reach destination "+ from_sq.col + " " + 
                          from_sq.row + " " + to_sq.col + " " + to_sq.row);
            return false;
          }
        }
        if((destinationP != null) && destinationP.getColor() == currCol){  //same color
          writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
          System.out.println("Same color Piece exist at "+ from_sq.col + " " + from_sq.row
                                + " " + to_sq.col + " " + to_sq.row);
          return false;
        } else {
          //delete piece if getting attacked
          if (destinationP != null) {
            delete(destinationP); //delete from linked list
          }
          
          //create moves
          
          if(piece instanceof King) {
            //moveKing    
            //The destination is null or removed, just update the location
          } else if(piece instanceof Queen) {
            //moveQueen  - need to write
            if(moveQueen(node_mv, from_sq, to_sq)  == false){
              writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
              System.out.println("Queen can not move "+ from_sq.col + " " + from_sq.row
                + " " + to_sq.col + " " + to_sq.row);
              return false;
            }
          } else if(piece instanceof Rook) {
            if(moveRook(node_mv, from_sq, to_sq)  == false){
              writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
              System.out.println("Rook can not move "+ from_sq.col + " " + from_sq.row
                + " " + to_sq.col + " " + to_sq.row);
              return false;
            }
          } else if(piece instanceof Bishop) {
            //moveBishop - need to write
            if(moveBishop(node_mv, from_sq, to_sq)  == false){
              writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
              System.out.println("Bishop can not move "+ from_sq.col + " " + from_sq.row
                + " " + to_sq.col + " " + to_sq.row);
              return false;
            }
          } else if(piece instanceof Knight) {
              //moveKnight   
            //The destination is null or removed, just update the location
          } else if(piece instanceof Pawn) {
            //movePawn
            //The destination is null or removed, just update the location
          }
          
          // Update location
          piece.setRow(to_sq.row);
          piece.setCol(to_sq.col);
          
          // Check for anyone attacking current king
          end_sq = false;
          if (kingAttacked (currCol)) {
            writer.write(from_sq.col + " " + from_sq.row + " " + to_sq.col + " " + to_sq.row + " " + "illegal\n");
            System.out.println("Attack on King from "+ king_attacked_c + " " + king_attacked_r);
            return false;
          }
          
          //check for both kings, if one not found, illegal
          
          // Utilities.printList(head);
          return true;
      }
    }catch(Exception e){
      Utilities.errExit("Error while performing operations");
    }
      return(false);
  }



  // main method
  public static void main(String[] args) {

      if (args.length < 2) // input format invalid
          Utilities.errExit("Must provide two arguments: input file and output file");

      try{
          BufferedReader reader = new BufferedReader(new FileReader(args[0])); // open the file to reader
          writer = new BufferedWriter(new FileWriter(args[1])); // setting up file I/O for output
          readFromInputFile(reader); // read from input file and perform operations
          writer.close(); // close the writer
      }
      catch(Exception e) {
        Utilities.errExit("Error in reading input file. Sure it exists?");
      }

  }
}
// End
