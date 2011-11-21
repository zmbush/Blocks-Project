package edu.berkeley.eecs.inst.zipcodeman.blocks;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class Board {
	int height, width;
	Block blocks[][];
	LinkedList<Block> boardBlocks;

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final String[] DIRECTION_NAMES = { "up", "down", "left", "right" };
	public static final int DIRECTIONS = 4;
	
	public static void main(String[] args) {
		Board cb = new Board("puzzles/easy/big.block.4");
		Reporting.flagOn("draw-board");
		cb.printBoard();
		for(int i = 0; i < cb.boardBlocks.size(); i++){
			Block c = cb.boardBlocks.get(i);
			Move mu = new Move(i, UP, c);
			Move md = new Move(i, DOWN, c);
			Move ml = new Move(i, LEFT, c);
			Move mr = new Move(i, RIGHT, c);
		}
	}
	
	Board(String file){
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
			}
			
			Reporting.println("Current Board: ", R.DRAW_BOARD);
			for(int i = 0; i < displayLines.length; i++){
				Reporting.println(displayLines[i].toString(), R.DRAW_BOARD);
			}
		}
	}
	public boolean moveBlock(Move m){
		if(canMove(m)){
			int direction = m.getMovementDirection();
			int index = m.getBlockIndex();
			Block b = boardBlocks.get(index);
			// Null out old postion. 
			for(int x = 0; x < b.getWidth(); x++){
				for(int y = 0; y < b.getHeight(); y++){
					blocks[y + b.getY()][x + b.getX()] = null;
				}
			}
			b.move(direction);
			m.setAfter(b);
			// Assign new position
			for(int x = 0; x < b.getWidth(); x++){
				for(int y = 0; y < b.getHeight(); y++){
					blocks[y + b.getY()][x + b.getX()] = b;
				}
			}
			return true;
		}
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
		Block b = this.boardBlocks.get(index);
		switch(direction){
		case DOWN:
			if(b.getY() + b.getHeight() >= blocks.length) return false;
			for(int i = 0; i < b.getWidth(); i++){
				if(blocks[b.getY() + b.getHeight()][b.getX() + i] != null){
					return false;
				}
			}
			return true;
		case UP:
			if(b.getY() <= 0) return false;
			for(int i = 0; i < b.getWidth(); i++){
				if(blocks[b.getY() - 1][b.getX() + i] != null){
					return false;
				}
			}
			return true;
		case LEFT:
			if(b.getX() <= 0) return false;
			for(int i = 0; i < b.getHeight(); i++){
				if(blocks[b.getY() + i][b.getX() - 1] != null){
					return false;
				}
			}
			return true;
		case RIGHT:
			if(b.getX() + b.getWidth() >= blocks[0].length) return false;
			for(int i = 0; i < b.getHeight(); i++){
				if(blocks[b.getY() + i][b.getX() + b.getWidth()] != null){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public Move[] getMoves(){
		Reporting.println("Starting to Get Moves", R.MOVEMENT);
		LinkedList<Move> moves = new LinkedList<Move>();
		for(int i = 0; i < this.boardBlocks.size(); i++){
			for(int dir = UP; dir < DIRECTIONS; dir++){
				Move thisMove = new Move(i, dir, boardBlocks.get(i));
				if(this.canMove(thisMove)){
					moves.add(thisMove);
				}
			}
		}
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
		Reporting.println("Entering Hash Function for Board", R.HASHING);
		String toHash = "";
		for(int i = 0; i < boardBlocks.size(); i++){
			Block current =  boardBlocks.get(i);
			toHash += current.getHeight() + " " + current.getWidth() + 
					  " " + current.getX() + " " + current.getY();
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
		return retval;
	}
	
	@Override
	public boolean equals(Object obj){
		Board r = (Board)obj;
		if(this.boardBlocks.size() != r.boardBlocks.size()) return false;
		for(int i = 0; i < this.boardBlocks.size(); i++){
			if(!boardBlocks.get(i).equals(r.boardBlocks.get(i))){
				return false;
			}
		}
		return true;
	}
	public boolean hasBlock(Block block) {
		for(int i = 0; i < this.boardBlocks.size(); i++){
			if(boardBlocks.get(i).equals(block)) return true;
		}
		return false;
	}
}
