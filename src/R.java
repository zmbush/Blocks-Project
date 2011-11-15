
public class R {
	public static final String FLAG_NAMES[] = { "draw-board", "show-tries",
												"trivial", "init"};
	public static final String FLAG_DESC[] = { "Draw the board every move", 
											   "Print when trying moves",
											   "Print the little things",
											   "Print initialization sequence" };
	public static final int DRAW_BOARD = 1 << 0;
	public static final int SHOW_TRIES = 1 << 1;
	public static final int TRIVIAL = 1 << 2;
	public static final int INITIALIZATION = 1 << 3;
	public static final int ALL = -1;
	
	public static final int NOT_ENOUGH_ARGS = 1;
	public static final int FILE_NOT_FOUND = 2;
	public static final int SYNTAX_ERROR = 3;
	public static final int UNKNOWN_ERROR = 99;
}
