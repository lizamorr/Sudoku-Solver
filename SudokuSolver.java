import java.util.*;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SudokuSolver {
    private int [][] myClue;
    private int [][] mySolution;
    /** Symbol used to indicate a blank grid position */
    public static final int BLANK = 0;
    /** Overall size of the grid */
    public static final int DIMENSION = 9;
    /** Size of a sub region */
    public static final int REGION_DIM = 3;
  
    
    // For debugging purposes -- see solve() skeleton.
    private Scanner kbd;
    private static final boolean DEBUG = false;
    
    /**
     * Run the solver. If args.length >= 1, use args[0] as the name of
     * a file containing a puzzle, otherwise, allow the user to browse
     * for a file.
     * @throws Exception 
     */
    public static void main(String [] args) throws Exception{
        String filename = null;
        if (args.length < 1) {
            // file dialog
            //filename = args[0];
            JFileChooser fileChooser = new JFileChooser();
            try {
                File f = new File(new File(".").getCanonicalPath());
                fileChooser.setCurrentDirectory(f);
            } catch (Exception ex) { System.out.println(ex.getMessage()); }
                        
            int retValue = fileChooser.showOpenDialog(new JFrame());
            
            if (retValue == JFileChooser.APPROVE_OPTION) {
                File theFile = fileChooser.getSelectedFile();
                filename = theFile.getAbsolutePath();
            } else {
                System.out.println("No file selected: exiting.");
                System.exit(0);
            }
        } else {
            filename = args[0];
        }
        
        SudokuSolver s = new SudokuSolver(filename);
        if (DEBUG)
            s.print();
        
        if (s.solve(0,0)){
	    // Pop up a window with the clue and the solution.
        	s.display();
        } else {
            System.out.println("No solution is possible.");
        }
        
    }
    
    /**
     * Create a solver given the name of a file containing a puzzle. We
     * expect the file to contain nine lines each containing nine digits
     * separated by whitespace. A digit from {1...9} represents a given
     * value in the clue, and the digit 0 indicates a position that is
     * blank in the initial puzzle.
     */
    public SudokuSolver(String puzzleName){
        myClue = new int[DIMENSION][DIMENSION];
        mySolution = new int[DIMENSION][DIMENSION];
        // Set up keyboard input if we need it for debugging.
        if (DEBUG)
            kbd = new Scanner(System.in);
        
        File pf = new File(puzzleName);
        Scanner s = null;
        try {
            s = new Scanner(pf);
        } catch (FileNotFoundException f){
            System.out.println("Couldn't open file.");
            System.exit(1);
        }
        
        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                myClue[i][j] = s.nextInt();
            }
        }
        
        // Copy to solution
        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                mySolution[i][j] = myClue[i][j];
            }
        }
    }
    
    /**
     * Starting at a given grid position, generate values for all remaining
     * grid positions that do not violate the game constraints.
     *
     * @param row The row of the position to begin with.
     * @param col The column of the position to begin with.
     *
     * @return true if a solution was found starting from this position,
     *          false if not.
     */
    public boolean solve(int row, int col) throws Exception{
	// This code will print the solution array and then wait for 
	// you to type "Enter" before proceeding. Helpful for debugging.
	// Set the DEBUG constant to true at the top of the class
	// declaration to turn this on.
        if (DEBUG) {
            System.out.println("solve(" + row + ", " + col + ")");
            print();
            kbd.nextLine();
        }
        
        // Base case that ends the puzzle when all the cell positions are solved
        if (row > (DIMENSION - 1))
        {
        	return true;
        }
        
        // If the cell is not empty, continue with the next cell
        else if (myClue[row][col] != BLANK)
        {
        	// If column in the given position is not the last column in the clue, then move to the next column
			if (col < (DIMENSION - 1))
			{
				// Recursive call to next column
				return solve(row, col + 1);
			}
			else
			{
				// Recursive call to next row when the user reaches the last column in the clue
				return solve(row + 1, 0);
			}
        }
        
        else
        {
        	// Find a valid number for the empty cell 
        	for (int num = 1; num < (DIMENSION + 1); num++)
        	{
        		// Checks to make sure the number can be placed in the given column, row, and box
        		if (checkRow(row, num) && checkCol(col, num) && checkBox(row, col, num))
        		{
        			// Places the given number in the given column and row positions
        			mySolution[row][col] = num;
        			  			
        			// Delegates work to the next cell 
        			if (col < (DIMENSION - 1))
        			{
        				// Recursive call to next column
        				if (solve(row, col + 1))
        				{
        					return true;
        				}
        			}
        			else
        			{
        				// Recursive call to next row
        				if (solve(row + 1, 0))
        				{
        					return true;
        				}
        			}
        		}
        	}
        		
        	// No valid number was found, sets the cell to 0, and returns to player for a new number to be entered
        	mySolution[row][col] = BLANK;
        	return false;
        }
    }
    
    
  
    
    /**
     * Checks if the number is an acceptable value for the given row.
     * @param row The specified row in the clue.
     * @param num The number entered by the user to be inserted into the given row.
     * @return true if the number can be entered into the row not containing the same value already in the row, otherwise returns false.
     */
    private boolean checkRow(int row, int num)
    {
    	// Checks if the column already contains the given number
    	for (int col = 0; col < DIMENSION; col++)
    	{
    		// Returns false if the given number is already in the row
    		if (mySolution[row][col] == num)
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Checks if the number is an acceptable value for the given column.
     * @param col The specified column in the clue.
     * @param num The number entered by the user to be inserted into the given column.
     * @return true if the number can be entered into the column not containing the same value already in the column, otherwise returns false.
     */
    private boolean checkCol(int col, int num)
    {
    	// Checks if the row already contains the given number
    	for (int row = 0; row < DIMENSION; row++)
    	{
    		// Returns false if the given number is already in the row
    		if (mySolution[row][col] == num)
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Checks if the number is an acceptable value for the box around row and column.
     * @param row The specified row in the clue.
     * @param col The specified column in the clue.
     * @param num The number entered by the user to be inserted into the given box.
     * @return true if the number can be entered into the box not containing the same value already in the box, otherwise returns false.
     */
    private boolean checkBox(int row, int col, int num)
    {
    	// Finds the first row and first column of the box
    	int startRow = (row / REGION_DIM) * REGION_DIM;
    	int startCol = (col / REGION_DIM) * REGION_DIM;
    	
    	// Checks all of the rows in the box
    	for (int boxRow = 0; boxRow < REGION_DIM; boxRow++)
    	{
    		// Checks all of the columns in the box
    		for (int boxColumn = 0; boxColumn < REGION_DIM; boxColumn++)
    		{
    			// Returns false if the given given row and column in the box already have the given number
    			if (mySolution[startRow + boxRow][startCol + boxColumn] == num)
        		{
        			return false;
        		}
    		}	
    	}
    	return true;
    }
    
    
    /**
     * Print a character-based representation of the solution array
     * on standard output.
     */
    public void print(){
        System.out.println("+---------+---------+---------+");
        for (int i = 0; i < DIMENSION; i++){
            System.out.println("|         |         |         |");
            System.out.print("|");
            for (int j = 0; j < DIMENSION; j++){
                System.out.print(" " + mySolution[i][j] + " ");
                if (j % REGION_DIM == (REGION_DIM - 1)){
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i % REGION_DIM == (REGION_DIM - 1)){
                System.out.println("|         |         |         |");
                System.out.println("+---------+---------+---------+");
            }
        }
    }
    
    /**
     * Pop up a window containing a nice representation of the original
     * puzzle and out solution.
     */
    public void display(){
        JFrame f = new DisplayFrame();
        f.pack();
        f.setVisible(true);
    }
    
    /**
     * GUI display for the clue and solution arrays.
     */
    private class DisplayFrame extends JFrame implements ActionListener {
        private JPanel mainPanel;
        
        private DisplayFrame(){
            mainPanel = new JPanel();
            mainPanel.add(buildBoardPanel(myClue, "Clue"));
            mainPanel.add(buildBoardPanel(mySolution, "Solution"));
            add(mainPanel, BorderLayout.CENTER);
            
            JButton b = new JButton("Quit");
            b.addActionListener(this);
            add(b, BorderLayout.SOUTH);
        }
        
        private JPanel buildBoardPanel(int [][] contents, String label){
            JPanel holder = new JPanel();
            JLabel l = new JLabel(label);
            BorderLayout b = new BorderLayout();
            holder.setLayout(b);
            holder.add(l, BorderLayout.NORTH);
            JPanel board = new JPanel();
            GridLayout g = new GridLayout(9,9);
            g.setHgap(0);
            g.setVgap(0);
            board.setLayout(g);
            Color [] colorChoices = new Color[2];
            colorChoices[0] = Color.WHITE;
            colorChoices[1] = Color.lightGray;
            int colorIdx = 0;
            int rowStartColorIdx = 0;
            
            for (int i = 0; i < DIMENSION; i++){
                if (i > 0 && i % REGION_DIM == 0)
                    rowStartColorIdx = (rowStartColorIdx+1)%2;
                colorIdx = rowStartColorIdx;
                for (int j = 0; j < DIMENSION; j++){
                    if (j > 0 && j % REGION_DIM == 0)
                        colorIdx = (colorIdx+1)%2;
                    JTextField t = new JTextField(""+ contents[i][j]);
                    if (contents[i][j] == 0)
                        t.setText("");
                    t.setPreferredSize(new Dimension(35,35));
                    t.setEditable(false);
                    t.setHorizontalAlignment(JTextField.CENTER);
                    t.setBackground(colorChoices[colorIdx]);
                    board.add(t);
                }
            }
            holder.add(board, BorderLayout.CENTER);
            return holder;
        }
        
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }
}
