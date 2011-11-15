package edu.berkeley.eecs.inst.zipcodeman.blocks;

public class Move {
	private int blockIndex;
	private int direction;
	private Block before;
	private Block after;
	
	public static void main(String[] args){
		Move m = new Move(0, Board.UP, new Block("1 1 1 1"));
		
		System.out.println(m);
		System.out.println("Reversed: " + m.reverse());
		System.out.println(m);
		
		m.setAfter(new Block("1 1 1 0"));
		System.out.println();
		System.out.println("Move Output String (for final output)");
		System.out.println(m.getOutputString());
	}
	Move(int index, int direction, Block block){
		this.blockIndex = index;
		this.direction = direction;
		this.before = new Block(block);
	}

	public Move(Move move) {
		this.blockIndex = move.blockIndex;
		this.direction = move.direction;
		this.before = move.before;
		this.after = move.after;
	}
	public void setAfter(Block b){
		this.after = new Block(b);
	}
	public int getMovementDirection() {
		return direction;
	}

	public int getBlockIndex() {
		return blockIndex;
	}
	
	public String toString(){
		return "(" + blockIndex + "," + Board.DIRECTION_NAMES[direction] + ")";
	}

	public Move reverse() {
		Move reversed = new Move(this);
		switch(direction){
		case Board.UP:
			reversed.direction = Board.DOWN;
			break;
		case Board.DOWN:
			reversed.direction = Board.UP;
			break;
		case Board.LEFT:
			reversed.direction = Board.RIGHT;
			break;
		case Board.RIGHT:
			reversed.direction = Board.LEFT;
			break;
		}
		return reversed;
	}

	public String getOutputString() {
		return String.format("%s %s %s %s", before.getY(), before.getX(), 
				after.getY(), after.getX());
	}
}
