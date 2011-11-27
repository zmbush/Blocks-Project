package edu.berkeley.eecs.inst.zipcodeman.blocks;

import java.lang.management.ManagementFactory;
import java.util.Calendar;

public class Statistics {
	private static long[] startTimes = new long[S.STAT_TYPES.length];
	private static long[] accumulatedTimes = new long[S.STAT_TYPES.length];
	private static long startTime = 0;
	private static long totalTime = 0;
	
	public static void startTracking(){
		startTime = ManagementFactory.getRuntimeMXBean().getUptime();
	}
	public static void startTracking(int Type){	
		startTimes[Type] = ManagementFactory.getRuntimeMXBean().getUptime();
	}
	
	public static void endTracking(int Type){
		long difference = ManagementFactory.getRuntimeMXBean().getUptime() - 
							startTimes[Type];
		accumulatedTimes[Type] += difference;
	}
	public static void endTracking(){
		totalTime = ManagementFactory.getRuntimeMXBean().getUptime() - startTime;
		//totalTime -= accumulatedTimes[S.OUTPUT];
	}
	public static void printStats(){
		System.err.println();
		long tim = totalTime;
		int minutes = (int)(tim / (1000*60));
		tim -= minutes * (1000*60);
		int seconds = (int)(tim / 1000);
		tim -= seconds * 1000;
		int mili = (int)tim;
		System.err.println(
				String.format("Time Spent: %02d:%02d.%d", minutes, seconds, mili));
		long other = totalTime;
		for(int i = 0; i < S.STAT_TYPES.length; i++){
			other -= accumulatedTimes[i];
			float perc = (accumulatedTimes[i] / (float)totalTime) * 100;
			System.err.println(S.STAT_TYPES[i] + perc + "%");
		}
		System.err.println("Other:           " + (other / (float)totalTime) * 100 + "%");
		System.err.println();
	}
}
