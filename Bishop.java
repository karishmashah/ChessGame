// Filename: Bishop.java
//
// Contains the class Bishop that represents a bishop chesspiece
//


class Bishop extends ChessPiece {

  // Default constructor sets loc to infeasible (negative) values
  public Bishop()
  {
    super();
  }

  // Constructor creates Bishop at location (col, row) and color
  public Bishop(int row, int col, boolean color)
  {
    super(row, col, color);
  }

  // Boolean function that determines if self (which is a bishop) is attacking another chesspiece at row and col, given as argument
  // Input: int row and col
  // Output: True if self is attacking the chesspiece at position row and col, false otherwise
  public boolean isAttacking(int row, int col)
  {
      if (Math.abs(this.getRow() - row) == Math.abs(this.getCol() - col)) // if self is on same diagonal as chesspiece, this is attack. we use absolute values to determine diagonal
          return true;
      else
          return false; // self is not attacking chesspiece at position l
  }
}

// End
