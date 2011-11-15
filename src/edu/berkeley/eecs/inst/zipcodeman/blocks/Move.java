package edu.berkeley.eecs.inst.zipcodeman.blocks;

public class Move {
	private int blockIndex;
	private int direction;
	private Block before;
	private Block after;
	
	Move(int index, int direction, Block block){
		this.blockIndex = index;
		this.direction = direction;
		this.before = new Block(block);
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
		Move reversed = this;
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
