// Filename: Queen.java
//
// Contains the class Queen that represents a queen chesspiece
//

class Queen extends ChessPiece {

    // Default constructor sets row and col to infeasible (negative) values
    public Queen()
    {
        super();
    }

    // Constructor creates Queen with col c and row r
    public Queen(int row, int col, boolean color)
    {
        super(row, col, color);
    }

    // Boolean function that determines if self (which is a queen) is attacking another chesspiece, given as argument
    // Input: integer row and col
    // Output: True if self is attacking another chesspiece at (row, col), false otherwise
    public boolean isAttacking(int row, int col)
    {
        if (this.getRow() == row || this.getCol() == col) // if self has same row or column as chesspiece, self is attacking
            return true;
        else if (Math.abs(this.getRow() - row) == Math.abs(this.getCol() - col)) // if self is on same diagonal as chesspiece, this is attack. we use absolute values to determine diagonal
            return true;
        else
            return false; // self is not attacking chesspiece
    }

}

// End
