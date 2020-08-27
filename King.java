// Filename: King.java
//
// Contains the class King that represents a king chesspiece
//

class King extends ChessPiece {

    // Default constructor sets row and col to infeasible (negative) values
    public King()
    {
        super();
    }

    // Constructor creates King with given row, col and color
    public King(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // Boolean function that determines if self (which is a king) is attacking another chesspiece, given as argument
    // Input: integer row, col
    // Output: True if self is attacking chesspiece at location (row, col), false otherwise
    public boolean isAttacking(int row, int col)
    {
        int attackRow[] = {-1, -1, 0, 1, 0, 1, 1, -1}; // possible attack row positions
        int attackCol[] = {0, -1, -1, -1, 1, 1, 0, 1}; // possible attack col positions

        for(int i = 0; i < 8; i++) {
          if(this.getRow() + attackRow[i] == row && this.getCol() + attackCol[i] == col) {
            return true;
          }
        }
        return false;
    }
}

// End
