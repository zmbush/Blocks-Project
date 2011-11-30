package edu.berkeley.eecs.inst.zipcodeman.blocks;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;


public class Board {
	int height, width;
	Block blocks[][];
	LinkedList<Block> boardBlocks;
	private LinkedList<Block> goalBlocks;

	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final String[] DIRECTION_NAMES = { "Up", "Left", "Down", "Right" };
	public static final int DIRECTIONS = 4;
	
	private int hashes = 0;
	private int equals = 0;
	
	public static void main(String[] args) {
		Board cb = new Board("puzzles/easy/big.block.4", "puzzles/easy/big.block.4.goal");
		Reporting.flagOn("draw-board");
		cb.printBoard();
		Move m = new Move(0, UP, 1, cb.boardBlocks.get(0));
		assert cb.canMove(m) == false : "canMove failure";
		m = new Move(2, RIGHT, 2, cb.boardBlocks.get(2));
		assert cb.canMove(m) == true : "canMove failure";
		m = new Move(2, RIGHT, 1, cb.boardBlocks.get(2));
		assert cb.canMove(m) == true : "canMove failure";
		m = new Move(3, DOWN, 1, cb.boardBlocks.get(3));
		assert cb.canMove(m) == true : "canMove failure";
		m = new Move(5, UP, 1, cb.boardBlocks.get(5));
		assert cb.canMove(m) == true : "canMove failure";
		Move ms[] = cb.getMoves();
		for(int i = 0; i < ms.length; i++){
			System.out.println(ms[i]);
		}
		
		/*
		for(int dist = 1; dist <= Math.max(cb.height, cb.width); dist++){
			for(int i = 0; i < cb.boardBlocks.size(); i++){
				Block c = cb.boardBlocks.get(i);
				for(int dir = UP; dir < DIRECTIONS; dir++){
					Move m = new Move(i, dir, dist, c);
					System.out.println("Dir: " + DIRECTION_NAMES[dir] + 
							" distance: " + dist + " block: " + i +
							" Can move? " + cb.canMove(m));
				}
			}
		}
		System.out.println();
		System.out.println("Moves: ");
		Move m[] = cb.getMoves();
		for(int i = 0; i < m.length; i++){
			System.out.println(m[i]);
		}
		
		System.out.println();
		System.out.println("Moving Block");
		cb.moveBlock(m[0]);
		cb.printBoard();
		System.out.println();
		System.out.println("Moves: ");
		Move m2[] = cb.getMoves();
		for(int i = 0; i < m2.length; i++){
			System.out.println(m2[i]);
		}
		System.out.println("Un-moving block");
		cb.unMoveBlock(m[0]);
		cb.printBoard();
		*/
	}
	public boolean isSolved() {
		Statistics.startTracking(S.SOLUTION_TEST);
		for(int i = 0; i < goalBlocks.size(); i++){
			if(!hasBlock(goalBlocks.get(i))){
				Statistics.endTracking(S.SOLUTION_TEST);
				return false;
			}
		}
		Statistics.endTracking(S.SOLUTION_TEST);
		return true;
	}
	
	Board(String file, String goal){
		boardBlocks = new LinkedList<Block>();
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			if(line != null){
				String dims[] = line.split(" ");
				if(dims.length < 2){
					System.err.println("There was a problem with the " +
							"initialConfiguration File at:");
					System.err.println(line);
					System.exit(R.SYNTAX_ERROR);
				}
				height = Integer.parseInt(dims[0]);
				width = Integer.parseInt(dims[1]);
				blocks = new Block[height][width];
				
				while((line = br.readLine()) != null){
					Block newBlock = new Block(line);
					boardBlocks.add(newBlock);
					for(int x = 0; x < newBlock.getWidth(); x++){
						for(int y = 0; y < newBlock.getHeight(); y++){
							blocks[y + newBlock.getY()][x + newBlock.getX()] 
							                            = newBlock;
						}
					}
				}
				Reporting.println("Created Blocks", R.INITIALIZATION);
			}
		}catch(FileNotFoundException fnfe){
			System.err.println("There was a problem opening the " +
					"initialConfiguration File");
			System.err.println();
			System.err.println(fnfe.getMessage());
			System.exit(R.FILE_NOT_FOUND);
		}catch(IOException ioe){
			System.err.println("Something went wrong when opening the " +
					"initialConfiguration File");
			System.err.println();
			System.err.println(ioe.getMessage());
			System.exit(R.UNKNOWN_ERROR);
		}catch(NumberFormatException nfe){
			System.err.println("You must use integers in the config file");
			System.err.println();
			System.err.println(nfe.getMessage());
			System.exit(R.SYNTAX_ERROR);			
		}
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
	Board(Board b){
		height = b.height;
		width = b.width;
		boardBlocks = new LinkedList<Block>();
		for(int i = 0; i < b.boardBlocks.size(); i++){
			boardBlocks.add(new Block(b.boardBlocks.get(i)));
		}
		blocks = new Block[height][width];
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(b.blocks[y][x] != null){
					blocks[y][x] = new Block(b.blocks[y][x]);
				}
			}
		}
	}
	public void printBoard(){
		Statistics.startTracking(S.PRINTING_BOARD);
		if(Reporting.canPrint(R.DRAW_BOARD)){
			StringBuilder displayLines[] = new StringBuilder[height*2+2];
			for(int i = 0; i < height*2+2; i++){
				displayLines[i] = new StringBuilder(width*2+2);
				StringBuilder line = displayLines[i];
				for(int j = 0; j < width*3+2; j++){
					if(j == 0){
						if(i != 0 && i != height*2 + 1){
							line.append(Boxy.VERT);
						}else{
							if(i == 0){
								line.append(Boxy.TL);
							}else{
								line.append(Boxy.BL);
							}
						}
					}else if(j == width*3+1){
						if(i != 0 && i != height*2 + 1){
							line.append(Boxy.VERT);
						}else{
							if(i == 0){
								line.append(Boxy.TR);
							}else{
								line.append(Boxy.BR);
							}
						}
					}else if(i == 0){
						line.append(Boxy.HORIZ);
					}else if(i == height*2 + 1){
						line.append(Boxy.HORIZ);
					}else{
						line.append(Boxy.SHADE);
					}
				}
			}
			
			for(int i = 0; i < this.boardBlocks.size(); i++){
				Block b = this.boardBlocks.get(i);
				displayLines[b.getY() * 2 + 1]
				             .setCharAt(b.getX() * 3 + 1, (char)(i + '0'));
				displayLines[b.getY() * 2 + 1]
				             .setCharAt(b.getX() * 3 + b.getWidth()*3, Boxy.TR);
				displayLines[b.getY() * 2 + b.getHeight()*2]
				             .setCharAt(b.getX() * 3 + 1, Boxy.BL);
				displayLines[b.getY() * 2 + b.getHeight()*2]
				             .setCharAt(b.getX() * 3 + b.getWidth()*3, Boxy.BR);
				for(int j = 0; j < b.getWidth()*3 - 2; j++){
					displayLines[b.getY() * 2 + 1]
					             .setCharAt(b.getX()*3 + j + 2, Boxy.HORIZ);
					displayLines[b.getY() * 2 + b.getHeight()*2]
					             .setCharAt(b.getX()*3 + j + 2, Boxy.HORIZ);
				}
				for(int j = 0; j < b.getHeight()*2 - 2; j++){
					displayLines[b.getY() * 2 + 2 + j]
					             .setCharAt(b.getX()*3 + 1, Boxy.VERT);
					displayLines[b.getY() * 2 + 2 + j]
					             .setCharAt(b.getX()*3 + b.getWidth()*3, Boxy.VERT);
				}
				for(int x = 0; x < b.getWidth()*3 - 2; x++){
					for(int y = 0; y < b.getHeight()*2 - 2; y++){
						displayLines[b.getY()*2 + 2 + y]
						    .setCharAt(b.getX()*3 + x + 2, ' ');
					}
				}
			}
			Statistics.endTracking(S.PRINTING_BOARD);
			Reporting.println("Current Board: ", R.DRAW_BOARD);
			for(int i = 0; i < displayLines.length; i++){
				Reporting.println(displayLines[i].toString(), R.DRAW_BOARD);
			}
			Reporting.println(new Integer(this.hashCode()).toString(), R.DRAW_BOARD);
			Reporting.println("Collisions so far: " + equals, R.DRAW_BOARD);
			return;
		}
		Statistics.endTracking(S.PRINTING_BOARD);
	}
	public boolean moveBlock(Move m){
		Statistics.startTracking(S.MOVING_BLOCKS);
		if(canMove(m)){
			int direction = m.getMovementDirection();
			int distance = m.getDistance();
			int index = m.getBlockIndex();
			Block b = boardBlocks.get(index);
			// Null out old postion. 
			for(int x = 0; x < b.getWidth(); x++){
				for(int y = 0; y < b.getHeight(); y++){
					blocks[y + b.getY()][x + b.getX()] = null;
				}
			}
			b.move(direction, distance);
			m.setAfter(b);
			// Assign new position
			for(int x = 0; x < b.getWidth(); x++){
				for(int y = 0; y < b.getHeight(); y++){
					blocks[y + b.getY()][x + b.getX()] = b;
				}
			}
			Statistics.endTracking(S.MOVING_BLOCKS);
			return true;
		}
		Statistics.endTracking(S.MOVING_BLOCKS);
		return false;
	}
	public boolean canMoveP(Move m, boolean slow){
		boolean retval = canMove(m, slow);
		System.out.println(m + " returned " + retval);
		return retval;
	}
	public boolean canMove(Move m){
		return canMove(m, true);
	}
	public boolean canMove(Move m, boolean slow){
		int index = m.getBlockIndex();
		int direction = m.getMovementDirection();
		int distance = m.getDistance();
		Block b = this.boardBlocks.get(index);
		switch(direction){
		case DOWN:
			if(b.getY() + b.getHeight() + distance - 1 >= blocks.length) return false;
			if(slow){
				for(int offset = 0; offset < distance; offset++){
					for(int i = 0; i < b.getWidth(); i++){
						if(blocks[b.getY() + b.getHeight() + offset][b.getX() + i] != null){
							return false;
						}
					}
				}
			}else{
				for(int i = 0; i < b.getWidth(); i++){
					if(blocks[b.getY() + b.getHeight() + distance - 1][b.getX() + i] != null){
						return false;
					}
				}
			}
			return true;
		case UP:
			if(b.getY() - distance + 1 <= 0) return false;
			if(slow){
				for(int offset = 0; offset < distance; offset++){
					for(int i = 0; i < b.getWidth(); i++){
						if(blocks[b.getY() - offset - 1][b.getX() + i] != null){
							return false;
						}
					}
				}
			}else{
				for(int i = 0; i < b.getWidth(); i++){
					if(blocks[b.getY() - distance][b.getX() + i] != null){
						return false;
					}
				}
				
			}
			return true;
		case LEFT:
			if(b.getX() - distance + 1 <= 0) return false;
			if(slow){
				for(int offset = 0; offset < distance; offset++){
					for(int i = 0; i < b.getHeight(); i++){
						if(blocks[b.getY() + i][b.getX() - offset - 1] != null){
							return false;
						}
					}
				}
			}else{
				for(int i = 0; i < b.getHeight(); i++){
					if(blocks[b.getY() + i][b.getX() - distance] != null){
						return false;
					}
				}
			}
			return true;
		case RIGHT:
			if(b.getX() + b.getWidth() + distance - 1 >= blocks[0].length) return false;
			if(slow){
				for(int offset = 0; offset < distance; offset++){
					for(int i = 0; i < b.getHeight(); i++){
						if(blocks[b.getY() + i][b.getX() + b.getWidth() + offset] != null){
							return false;
						}
					}
				}
			}else{
				for(int i = 0; i < b.getHeight(); i++){
					if(blocks[b.getY() + i][b.getX() + b.getWidth() + distance - 1] != null){
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	public Move[] getMoves(){
		Statistics.startTracking(S.FIND_MOVES);
		Move[] unsorted = getMovesUnsorted();
		
		/*
		for(int i = 0; i < unsorted.length; i++){
			for(int j = 0; j < unsorted.length - i - 1; j++){
				if(moveValue(unsorted[j]) > moveValue(unsorted[j + 1])){
					Move temp = unsorted[j];
					unsorted[j] = unsorted[j + 1];
					unsorted[j + 1] = temp;
				}
			}
		}
		*/
		
		
		Statistics.endTracking(S.FIND_MOVES);
		return unsorted;
	}
	public float moveValue(Move move) {
		int score = 0;
		Block aft = move.getAfter();
		Block bef = move.getBefore();
		float minDist = 100000;
		float currentDist = 0;
		for(int i = 0; i < this.goalBlocks.size(); i++){
			if(this.goalBlocks.get(i).sameSize(bef))
				if((currentDist = this.goalBlocks.get(i).distanceFrom(aft)) < minDist)
					minDist = currentDist;
		}
		return minDist;
	}

	public Move[] getMovesUnsorted(){
		Reporting.println("Starting to Get Moves", R.MOVEMENT);
		LinkedList<Move> moves = new LinkedList<Move>();

		/*/
		for(int dir = UP; dir < DIRECTIONS; dir++){
			for(int i = 0; i < this.boardBlocks.size(); i++){
				Move thisMove = new Move(i, dir, 1, boardBlocks.get(i));
				if(this.canMove(thisMove)){
					moves.add(thisMove);
				}
			}
		}	
		/**/
		/**/
		boolean stillGood[][] = new boolean[this.boardBlocks.size()][4];
		for(int i = 0; i < stillGood.length; i++){
			for(int j = 0; j < stillGood[0].length; j++){
				stillGood[i][j] = true;
			}
		}
		
		int dist = Math.max(width, height);
		for(int d = 1; d <= dist; d++){
			for(int i = 0; i < this.boardBlocks.size(); i++){
				for(int dir = UP; dir < DIRECTIONS; dir++){
					Move thisMove = new Move(i, dir, d, boardBlocks.get(i));
					if(this.canMove(thisMove) && stillGood[i][dir]){
						moves.add(thisMove);
					}else{
						stillGood[i][dir] = false;
					}
				}
			}	
		}
		/**/
		Reporting.println("Moves gotten", R.MOVEMENT);
		return moves.toArray(new Move[0]);
		
	}
	public boolean unMoveBlock(Move move) {
		return moveBlock(move.reverse());
	}
	public String toString(){
		String retval = "";
		for(int i = 0; i < this.boardBlocks.size(); i++){
			Block c = boardBlocks.get(i);
			retval += c.toString();
		}
		return retval;
	}
	
	@Override
	public int hashCode(){
		Statistics.postHash();
		Statistics.startTracking(S.HASHING);
		Reporting.println("Entering Hash Function for Board", R.HASHING);
		
		/*/
		int b     = 378551;
		int a     = 63689;
		long hash = 0;
		for(int i = 0; i < boardBlocks.size(); i++){
		   hash = hash * a + boardBlocks.get(i).hashCode();
		   a    = a * b;
		}
		Statistics.endTracking(S.HASHING);
		return (int)hash;
		/**/
		/*/
		// Probably a stupid idea
		Hashtable<Block,Boolean> seenBlocks = new Hashtable<Block,Boolean>();
		String toHash = "";
		for(int y = 0; y < blocks.length; y++){
			for(int x = 0; x < blocks[y].length; x++){
				if(blocks[y][x] != null){
					Block current =  blocks[y][x];
					if(seenBlocks.put(current, true) == null){
						toHash += current.getHeight() + " " + current.getWidth() + 
								  " " + current.getX() + " " + current.getY();
					}else{
						x = current.getX() + current.getHeight();
					}
				}
			}
		}
		/**/
		
		String toHash = "";
		for(int i = 0; i < boardBlocks.size(); i++){
			Block current =  boardBlocks.get(i);
			toHash += current.getHeight() + " " + current.getWidth() + " " + current.getX() + " " + current.getY();
		}
		
		// Really Slow for large boards. 
//		for(int y = 0; y < blocks.length; y++){
//			for(int x = 0; x < blocks[y].length; x++){
//				if(blocks[y][x] != null){
//					toHash += blocks[y][x].getHeight() + " " + blocks[y][x].getWidth();
//				}else{
//					toHash += "# #";
//				}
//			}
//		}
		Reporting.println("Leaving Hash Function for Board", R.HASHING);
		int retval = toHash.hashCode();
		Statistics.endTracking(S.HASHING);
		return retval;
		/**/
	}
	@Override
	public boolean equals(Object obj){
		Statistics.postEquals();
		Statistics.startTracking(S.COMPARING);
		Board r = (Board)obj;
		if(this.boardBlocks.size() != r.boardBlocks.size()) return false;
		/*
		for(int i = 0; i < this.boardBlocks.size(); i++){
			if(!r.hasBlock(boardBlocks.get(i))){
				Statistics.endTracking(S.COMPARING);
				Statistics.postCollide();
				return false;
			}
		}
		*/
		for(int i = 0; i < this.boardBlocks.size(); i++){
			if(!boardBlocks.get(i).equals(r.boardBlocks.get(i))){
				Statistics.endTracking(S.COMPARING);
				Statistics.postCollide();
				return false;
			}
		}
		Statistics.endTracking(S.COMPARING);
		return true;
	}
	public boolean hasBlock(Block block) {
		for(int i = 0; i < this.boardBlocks.size(); i++){
			if(boardBlocks.get(i).equals(block)) return true;
		}
		return false;
	}
}
