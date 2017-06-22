# Sudoku-Solver

Liza Morrison
CS221
November 16, 2014

Programming Assignment #3: Sudoku

(Describe how the algorithm for the solve() method works, each helper method, as well as how all the methods are used together logically to solve the problem without involving any of the code)

The solve() method makes recursive calls on every cell in the Sudoku grid (“clue”) in order to populate the entire clue with valid numbers (fulfill the row, column, and box constraints). In order to solve the clue, the solve() method contains a base case, parameters to indicate the problem size, recursive calls, and “glue” (all described below).

Base Case:

	The base case in solve() method ends the code when the puzzle is solved (with every cell filled in with an appropriate number). If the code is at the last column in the 	clue, then it moves to the next row to delegate work; however, it can only do this for 	the dimension of the clue. When the code tries to move to the next row after the 	dimension’s constraints, the base case allows the process to end and signifies that the puzzle has been solved.

Parameters (problem size):
	
	The number of rows and columns in the clue dictate how many cells there are in the 	clue to be solved. The more rows and columns the clue contains, the more iterations of the solve() method are needed to go through each cell and insert an appropriate 	number that passes all of the constraints. The solve() method accepts the row of the 	position to begin with and the column of the position to begin with, meaning that the	method will start with 0,0 (row index 0, column index 0) and then pass through every 	row and column in the clue’s dimension.
	
Recursive Calls:

	In order for the solve() method to work and fill in the cells with numbers that	satisfy all of the conditions of Sudoku, several recursive calls are made for the code to access the next row or column in the clue. A number cannot be entered into a cell already containing another number so therefore, the code needs to move to the next 	cell in the clue. If the number is being inserted into a column that is not the last column 	in the clue and already contains a number, then the number has to be inserted into 	the next column over (the next column index) by calling the solve() method with the same row parameter and the next column to the right as the second parameter. 	This way the code will evaluate the next cell in the clue and insert the number only if it does not contain another number already. The second recursive call is made 	when the position is the last column in the clue and needs to move to the next cell. 	Since it is the last column in the clue, the next position to move to would be the next 	row (the next row index) and therefore, the column index would be set back to 0 	because that is the first column in the clue. Both recursive calls return the row and 	column needed to insert the given number into the cell. When an empty cell is 	reached, a number can finally be inserted into the cell if it satisfies the row, column, 	and box constraints (detailed in the checkCol(), checkRow(), and checkBox() method 	descriptions). The same recursive calls are made to delegate work to the next cell 	as with the cells already containing numbers. 

	When a recursive call is returned, the value is passed into the parameters of the 	solve method. They allow the code to access the next column or row in the clue in 	order to insert a number and eventually solve the puzzle. 

	In the beginning of the puzzle, the problem size includes all of the cells of the clue because the code has to go through each cell and insert a number. When a cell is solved, the problem size decreases because the code can move on to the next cell	until all cells are solved. The further down the row is in the clue, the smaller the 	problem size because that means all the cells above the current row are solved and 	completed. The code continues traversing through all the cells until it has to solve the 	last cell in the clue, which is located at the bottom right corner. 

	Every recursive call makes progress to the base case (until the rows exceed the clue 	dimension) to prevent infinite recursion. 

	Description of how recursive methods calls work:

	At runtime, a stack of activation records for each active (has been called, but has not	yet returned) method. The activation records contains space for the solve() method’s parameters and return address (indicates where in the code to begin executing after the method returns). When the solve() method is called, its activation record is pushed onto the stack and the return address in that activation record is the position 	in the code just after the recursive call. When the solve() method is about to return, the return address in its activation record is saved. The activation record is then popped from the stack and then the code is transferred to the place in the code 	referred to by the return address.

Glue: 
	
	The glue is the work done in addition to the recursive calls to combine the results of 	the simpler problems. The glue determines whether or not the cell is empty and what 	number can be inserted into the cell when it is not empty.  

	The glue helps solve the simpler problems by returning true if a solution is found starting 	from a specified position and false if a solution is not found (glue does not include the base case or the recursive calls). Backtracking occurs when false is returned because the cells need to contain numbers that satisfy the column, row, and box constraints. The glue includes when the solution cell is set to 0 if the number is invalid 	and allow for backtracking to continue solving any subsequent cells. When the 	solution for that cell is found, true is returned and used to move the code to the next column or row in the clue. 

Helper methods:

	1. checkCol()
	
		In order for a number to be entered into the cell, the column cannot already contain the specified number. By starting from the first row of the clue	all the way to the last row of the clue, the rows are checked to see if they already contain the given number. If any of the rows contain the given number, then that number cannot be inserted into the cell; it can only be inserted into the cell if the given number is not already in any of the rows. The method passes through all of the rows because each column is made up of	all the rows in the clue.

		Parameters: The specified row in the clue and the number to be inserted into the given row.	

	2. checkRow()

		In order for a number to be entered into the cell, the row cannot already ontain the specified number. By starting from the first column of the	clue all the way to the last column of the clue, the columns are checked to see if they already contain the given number. If any of the columns contain the given number, then that number cannot be inserted into the cell; it can only be inserted into the cell if the given number is not already in any of the columns. The method passes through all of the columns because each row is made up of all of the columns in the clue.

		Parameters: The specified column in the clue and the number to be inserted into the column.

	3. checkBox()

		In order for a number to be entered into the cell, the box cannot already contain the specified number. The method initially finds the first row and first column of the box and then passes through each row and column in the box to check if the box rows or box columns contain the given number. If any of the cells in the box contain the given number, then that number cannot be inserted into the cell; it can only be inserted into the cell if the given number is not already in the box. 

		Parameters: The specified row in the clue, the specified column in the clue, and the number to be inserted into the given box.


	If a number does not satisfy any of these conditions, then that cell is cleared (set to 0) in order for the user to enter another number into the cell. Backtracking allows for a new number to be inserted into the given cell until it has finally satisfied the given 	conditions. 
