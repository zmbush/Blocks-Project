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

	Solver(String initial, String goal){
		currentBoard = new Board(initial);
		seenConfigurations = new Hashtable<Board, Boolean>();
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
		if(!this.hasBeenSeen()){
			currentBoard.printBoard();
			if(this.isSolved()){
				return new Stack<Move>();
			}
			Move[] possibleMoves = currentBoard.getMoves();
			for(int i = 0; i < possibleMoves.length; i++){
				Reporting.println("Moving: " + possibleMoves[i], R.SHOW_TRIES);
				currentBoard.moveBlock(possibleMoves[i]);
				Stack<Move> subSol = getSolution(depth + 1);
				if(subSol != null){
					subSol.push(possibleMoves[i]);
					return subSol;
				}
				
				currentBoard.unMoveBlock(possibleMoves[i]);
			}
		}
		return null;
	}

	private boolean hasBeenSeen() {
		Board newBoard = new Board(currentBoard);
		if(seenConfigurations.get(newBoard) != null){
			return true;
		}else{
			seenConfigurations.put(newBoard, true);
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
