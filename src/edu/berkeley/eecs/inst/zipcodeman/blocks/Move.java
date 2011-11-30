package edu.berkeley.eecs.inst.zipcodeman.blocks;

public class Move {
	private int blockIndex;
	private int direction;
	private Block before;
	private Block after;
	private int distance;
	
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
		this(index, direction, 1, block);
	}
	Move(int index, int direction, int distance, Block block){
		this.blockIndex = index;
		this.direction = direction;
		this.distance = distance;
		this.before = new Block(block);
		this.after = this.before.getMove(this.direction, this.distance);
	}

	public Move(Move move) {
		this.blockIndex = move.blockIndex;
		this.direction = move.direction;
		this.before = move.before;
		this.distance = move.distance;
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
		return "(" + blockIndex + "," + Board.DIRECTION_NAMES[direction]
		           + "," + distance + ")";
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
	public int getDistance() {
		return distance;
	}
	public Block getBefore() {
		return before;
	}
	public Block getAfter() {
		return after;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((after == null) ? 0 : after.hashCode());
		result = prime * result + ((before == null) ? 0 : before.hashCode());
		result = prime * result + blockIndex;
		result = prime * result + direction;
		result = prime * result + distance;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (after == null) {
			if (other.after != null)
				return false;
		} else if (!after.equals(other.after))
			return false;
		if (before == null) {
			if (other.before != null)
				return false;
		} else if (!before.equals(other.before))
			return false;
		if (blockIndex != other.blockIndex)
			return false;
		if (direction != other.direction)
			return false;
		if (distance != other.distance)
			return false;
		return true;
	}
}
