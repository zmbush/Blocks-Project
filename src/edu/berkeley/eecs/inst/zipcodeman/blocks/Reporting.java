package edu.berkeley.eecs.inst.zipcodeman.blocks;

public class Reporting {
	private static int flagsOn = 0;
	private static boolean slowOutput = false;
	private static int slowCount = 0;
	private static int slowMax = 1000;
	
	/**
	 * Turn on a reporting flag
	 * @param substring the string to compare against
	 */
	public static void flagOn(String substring) {
		if(substring.startsWith("no")){
			flagOff(substring.substring(2));
		}else{
			for(int i = 0; i < R.FLAG_NAMES.length; i++){
				if(substring.equals(R.FLAG_NAMES[i])){
					flagsOn |= 1 << i;
					return;
				}
			}
			if(substring.equals("all")){
				flagsOn |= R.ALL;
			}else if(substring.equals("slow")){
				slowOutput = true;
			}else if(substring.equals("options")){
				printFlags();
				System.exit(0);
			}else{
				System.err.println("Unknown output mode: " + substring);
			}
		}
	}
	
	public static void flagOff(String substring) {
		for(int i = 0; i < R.FLAG_NAMES.length; i++){
			if(substring.equals(R.FLAG_NAMES[i])){
				flagsOn &= (-1 ^ 1 << i);
				return;
			}
		}
		System.err.println("Unknown output mode: " + substring);
	}
	
	public static void printFlags(){
		System.out.println("Output Flags:");
		System.out.println("\t-o[flag_name]");
		System.out.println();
		System.out.println("Flag Names:");
		for(int i = 0; i < R.FLAG_NAMES.length; i++){
			System.out.println(String.format("\t%10s: %s", R.FLAG_NAMES[i], 
					R.FLAG_DESC[i]));
		}
	}
	public static void print(int flags){
		print("", flags);
	}
	
	public static void print(String out, int flags){
		Statistics.startTracking(S.OUTPUT);
		
		if(canPrint(flags)){
			System.out.print(out);
		}
		
		Statistics.endTracking(S.OUTPUT);
	}
	
	public static void println(int flags){
		println("", flags);
	}
	public static void println(String out, int flags){
		Statistics.startTracking(S.OUTPUT);
		
		if(canPrint(flags)){
			System.out.println(out);
		}
		
		Statistics.endTracking(S.OUTPUT);
	}
	public static boolean canPrint(int flags){
		if((flags & flagsOn) == flags){
			if(slowOutput){
				slowCount += 1;
				if (slowCount > slowMax){
					slowCount = 0;
					return true;
				}
				return false;
			}
			return true;
		}
		return false;
	}
}
