package edu.berkeley.eecs.inst.zipcodeman.blocks;

public class Block {
	private int x,y;
	private int hei,wid;
	
	public static void main(String [] args){
		Block b = new Block("1 1 2 3");
		
		System.out.println(b);

		System.out.println("Height: " + b.getHeight());
		System.out.println("Width: " + b.getWidth());
		System.out.println("X: " + b.getX());
		System.out.println("Y: " + b.getY());
		
		b.move(Board.DOWN);
		System.out.println("After move down");
		System.out.println(b);
		b.move(Board.UP);
		System.out.println("up");
		System.out.println(b);
		b.move(Board.RIGHT);
		System.out.println("Right");
		System.out.println(b);
		b.move(Board.LEFT);
		System.out.println("Left");
		System.out.println(b);
		
		System.out.println("Hash: " + b.hashCode());
	}
	Block(String bSpec){
		Reporting.println("Block: " + bSpec, R.INITIALIZATION);
		String[] dims = bSpec.split(" ");
		if(dims.length >= 4){
			this.hei = Integer.parseInt(dims[0]);
			this.wid = Integer.parseInt(dims[1]);
			this.y = Integer.parseInt(dims[2]);
			this.x = Integer.parseInt(dims[3]);
		}else{
			System.err.println("Block lines must have four integers");
			System.exit(R.SYNTAX_ERROR);
		}
	}
	
	Block(Block b){
		x = b.x;
		y = b.y;
		wid = b.wid;
		hei = b.hei;
	}
	
	public int getHeight() {
		return hei;
	}	
	
	public int getWidth(){
		return wid;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	public void move(int direction) {
		switch(direction){
		case Board.UP:
			y--;
			break;
		case Board.DOWN:
			y++;
			break;
		case Board.LEFT:
			x--;
			break;
		case Board.RIGHT:
			x++;
			break;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hei;
		result = prime * result + wid;
		result = prime * result + x;
		result = prime * result + y;
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
		Block other = (Block) obj;
		if (hei != other.hei)
			return false;
		if (wid != other.wid)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public String toString(){
		return x + "," + y + "," + wid + "," + hei + " ";
	}
}
