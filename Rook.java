// Filename: Rook.java
//
// Contains the class Rook that represents a rook chesspiece
//
// Santrupti Nerli, Jan 2017

class Rook extends ChessPiece {

  // Default constructor sets loc to infeasible (negative) values
  public Rook()
  {
    super();
  }

  // Constructor creates Rook with row, col and color
  public Rook(int row, int col, boolean color)
  {
    super(row, col, color);
  }

  // Boolean function that determines if self (which is a rook) is attacking another chesspiece at location row, col, given as argument
  // Input: integer row and col
  // Output: True if self is attacking the chesspiece at position (row, col), false otherwise
  public boolean isAttacking(int row, int col)
  {
      if (this.getRow() == row || this.getCol() == col) // if self has same row or column as chesspiece, self is attacking
          return true;
      else
          return false; // self is not attacking chesspiece
  }
}

// End
