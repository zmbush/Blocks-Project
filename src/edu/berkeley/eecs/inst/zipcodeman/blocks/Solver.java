package edu.berkeley.eecs.inst.zipcodeman.blocks;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;


public class Solver {

	private static final int MAX_DEPTH = 7000;
	private Board currentBoard;
	private Hashtable<Board, Boolean> seenConfigurations;
	/**
	 * @param args the arguments passed from the command line. 
	 */
	public static void main(String[] args) {
		int filesStart = 0;
		for(int i = 0; i < args.length; i++){
			filesStart = i;
			if(args[i].startsWith("-o")){
				Reporting.flagOn(args[i].substring(2));
			}else{
				break;
			}
		}
		if(args.length - filesStart >= 2){
		    String initial = args[filesStart];
		    String goal = args[filesStart + 1];
		    Reporting.println("Initial: " + initial, R.INITIALIZATION);
		    Reporting.println("Goal: " + goal, R.INITIALIZATION);
		    
		    Solver s = new Solver(initial, goal);
		    Stack<Move> solution = s.getSolution();
		    if(solution == null){
		    	System.exit(R.IMPOSSIBLE);
		    }else{
				while(!solution.empty()){
					System.out.println(solution.pop().getOutputString());
				}
			}
		}else{
			System.err.println("You must supply an input file and a goal file");
			System.exit(R.NOT_ENOUGH_ARGS);
		}
	}

	/**
	 * Instantiate class to solve board
	 * @param initial filename of initial configuration
	 * @param goal filename of goal configuration
	 */
	Solver(String initial, String goal){
		// Load the initial configuration into Board Class
		currentBoard = new Board(initial, goal);
		
		// Keep track of configurations that have been seen before
		seenConfigurations = new Hashtable<Board, Boolean>();
		
		
	}
	
	private Stack<Move> getSolution(){
		return getSolution(MAX_DEPTH);
	}
	
	private Stack<Move> getSolution(int maxDepth){
		return getSolution(0, maxDepth);
	}
	
	private Stack<Move> getSolution(int depth, int maxDepth) {
		if(depth > maxDepth) return null;
		String prefix = depth + "> ";
		
		Reporting.println(prefix + "Looking for solution at depth: " + depth, R.SOLVE_FLOW);
		if(!this.hasBeenSeen()){
			Reporting.println(prefix + "Board has not been seen", R.SOLVE_FLOW);
			currentBoard.printBoard();
			Reporting.println(prefix + "Checking if board is solved", R.SOLVE_FLOW);
			if(currentBoard.isSolved()){
				Reporting.println(prefix + "Board Solved", R.SOLVE_FLOW);
				return new Stack<Move>();
			}
			Reporting.println(prefix + "Board not solved", R.SOLVE_FLOW);
			Move[] possibleMoves = currentBoard.getMoves();
			Reporting.println(prefix + "Potential Moves fetched: ", R.SOLVE_FLOW);
			for(int i = 0; i < possibleMoves.length; i++){
				Reporting.println(prefix + "\t" + possibleMoves[i] + "\t" 
				                  + currentBoard.moveValue(possibleMoves[i]), 
				                  R.SOLVE_FLOW);
			}
			for(int i = 0; i < possibleMoves.length; i++){
				Reporting.println(prefix + " triyng move number " + i, R.SHOW_TRIES);
				Reporting.println("Moving: " + possibleMoves[i], R.SHOW_TRIES);
				if(currentBoard.moveBlock(possibleMoves[i])){
					Reporting.println(prefix + "Block moved", R.SOLVE_FLOW);
				}else{
					Reporting.println(prefix + "Movement Failed!" + possibleMoves[i], R.SOLVE_FLOW);
					System.exit(R.MOVE_ERROR);
				}
				try{
					Stack<Move> subSol = getSolution(depth + 1, maxDepth);
					Reporting.println(prefix + "Recieved sub-solution", R.SOLVE_FLOW);
					if(subSol != null){
						Reporting.println(prefix + "Solution found!!!", R.SOLVE_FLOW);
						subSol.push(possibleMoves[i]);
						Reporting.println(prefix + "Added current move", R.SOLVE_FLOW);
						return subSol;
					}
				}catch(StackOverflowError e){
					Reporting.println(R.SOLVE_FLOW);
					Reporting.println(R.SOLVE_FLOW);
					Reporting.println(prefix + "Maximum Recursion Depth Reached...", R.SOLVE_FLOW);
					Reporting.println(R.SOLVE_FLOW);
					Reporting.println(R.SOLVE_FLOW);
					//System.err.println(prefix + "Maximum Recursion depth Reached");
				}
				
				Reporting.println(prefix + "Solution not found... Continuing", R.SOLVE_FLOW);
				if(currentBoard.unMoveBlock(possibleMoves[i])){
					Reporting.println(prefix + "Block unmoved", R.SOLVE_FLOW);
				}else{
					Reporting.println(prefix + "Block unmove failed" + possibleMoves[i], R.SOLVE_FLOW);
					System.exit(R.MOVE_ERROR);
				}
			}
		}
		return null;
	}

	private boolean hasBeenSeen() {
		Reporting.println("** Copying Board", R.HASHING);
		Board newBoard = new Board(currentBoard);
		Reporting.println("** Board copied. Checking for duplicate", R.HASHING);
		if(seenConfigurations.get(newBoard) != null){
			Reporting.println("** Board found.", R.HASHING);
			return true;
		}else{
			Reporting.println("** Board not found, Adding it to table", R.HASHING);
			seenConfigurations.put(newBoard, true);
			Reporting.println("** Board added.", R.HASHING);
			return false;
		}
	}
}
