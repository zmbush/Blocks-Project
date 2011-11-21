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

	private static final int MAX_DEPTH = 500;
	private Board currentBoard;
	private Hashtable<Board, Boolean> seenConfigurations;
	private LinkedList<Block> goalBlocks;
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
		    	System.exit(1);
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
		currentBoard = new Board(initial);
		
		// Keep track of configurations that have been seen before
		seenConfigurations = new Hashtable<Board, Boolean>();
		
		// An array of Blocks to compare against
		goalBlocks = new LinkedList<Block>();
		try{
			FileReader fr = new FileReader(goal);
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			
			while((line = br.readLine()) != null){
				goalBlocks.add(new Block(line));
			}
		}catch(FileNotFoundException fnfe){
			System.err.println("There was a problem opening the " +
			"goalConfiguration File");
			System.err.println();
			System.err.println(fnfe.getMessage());
			System.exit(R.FILE_NOT_FOUND);
		}catch(IOException ioe){
			System.err.println("Something went wrong when opening the " +
					"goalConfiguration File");
			System.err.println();
			System.err.println(ioe.getMessage());
			System.exit(R.UNKNOWN_ERROR);
		}catch(NumberFormatException nfe){
			System.err.println("You must use integers in the config file");
			System.err.println();
			System.err.println(nfe.getMessage());
			System.exit(R.SYNTAX_ERROR);			
		}
	}
	
	private Stack<Move> getSolution(){
		return getSolution(0);
	}
	
	private Stack<Move> getSolution(int depth) {
		//if(depth > MAX_DEPTH) return null;
		String prefix = ">";
		for(int i = 0; i < depth; i++){
			prefix += ">";
		}
		Reporting.println(prefix + "Looking for solution at depth: " + depth, R.SOLVE_FLOW);
		if(!this.hasBeenSeen()){
			Reporting.println(prefix + "Board has not been seen", R.SOLVE_FLOW);
			currentBoard.printBoard();
			Reporting.println(prefix + "Checking if board is solved", R.SOLVE_FLOW);
			if(this.isSolved()){
				Reporting.println(prefix + "Board Solved", R.SOLVE_FLOW);
				return new Stack<Move>();
			}
			Reporting.println(prefix + "Board not solved", R.SOLVE_FLOW);
			Move[] possibleMoves = currentBoard.getMoves();
			Reporting.println(prefix + "Potential Moves fetched", R.SOLVE_FLOW);
			for(int i = 0; i < possibleMoves.length; i++){
				Reporting.println(prefix + " triyng move number " + i, R.SHOW_TRIES);
				Reporting.println("Moving: " + possibleMoves[i], R.SHOW_TRIES);
				currentBoard.moveBlock(possibleMoves[i]);
				Reporting.println(prefix + "Block moved", R.SOLVE_FLOW);
				Stack<Move> subSol = getSolution(depth + 1);
				Reporting.println(prefix + "Recieved sub-solution", R.SOLVE_FLOW);
				if(subSol != null){
					Reporting.println(prefix + "Solution found!!!", R.SOLVE_FLOW);
					subSol.push(possibleMoves[i]);
					Reporting.println(prefix + "Added current move", R.SOLVE_FLOW);
					return subSol;
				}
				Reporting.println(prefix + "Solution not found... Continuing", R.SOLVE_FLOW);
				currentBoard.unMoveBlock(possibleMoves[i]);
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

	private boolean isSolved() {
		for(int i = 0; i < goalBlocks.size(); i++){
			if(!currentBoard.hasBlock(goalBlocks.get(i))){
				return false;
			}
		}
		return true;
	}
}
