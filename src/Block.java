
public class Block {
	int x,y;
	int hei,wid;
	
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
