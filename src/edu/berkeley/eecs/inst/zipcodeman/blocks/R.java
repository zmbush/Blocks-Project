package edu.berkeley.eecs.inst.zipcodeman.blocks;

public class R {
	public static final String FLAG_NAMES[] = { "draw-board", "show-tries",
												"trivial", "init", "movement",
												"solve-flow", "hashing"};
	public static final String FLAG_DESC[] = { "Draw the board every move", 
											   "Print when trying moves",
											   "Print the little things",
											   "Print initialization sequence",
											   "Display relating to Movement",
											   "Things done in solving process",
											   "Hashing related operations"};
	public static final int DRAW_BOARD = 1 << 0;
	public static final int SHOW_TRIES = 1 << 1;
	public static final int TRIVIAL = 1 << 2;
	public static final int INITIALIZATION = 1 << 3;
	public static final int MOVEMENT = 1 << 4;
	public static final int SOLVE_FLOW = 1 << 5;
	public static final int HASHING = 1 << 6;
	public static final int ALL = -1;
	
	public static final int NOT_ENOUGH_ARGS = 1;
	public static final int FILE_NOT_FOUND = 2;
	public static final int SYNTAX_ERROR = 3;
	public static final int UNKNOWN_ERROR = 99;
}
