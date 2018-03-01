package me.cof.googlehash;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
	
	private int[] currentPosition;
	private int[] targetPosition;
	private List<Ride> completedRides;
	private Ride currentRide;
	
	public Vehicle(){
		currentPosition = new int[]{0, 0};
		targetPosition = new int[2];
		completedRides = new ArrayList<Ride>();
		
		// Find the first ride
		FindNewRide();
	}
	
	public void FindNewRide(){
		int highestReward = 0;
		Ride highestRide = null;
		
		for (Ride r : Main.rides){
			if (r.getIsFinished() && r.isPickedUp())
				continue;
			
			int reward = 0;
			int distancePoints = r.distance();
			int distanceToRide = GetDistance(currentPosition, r.getStartLoc());
			
			// The Ride cannot be completed on time
			if (distanceToRide + distancePoints + Main.CURRENT_TIME > r.getFinishTime())
				continue;
			
			reward += distancePoints;
			
			int arrivalTime = distanceToRide + Main.CURRENT_TIME;
			
			if (r.getStartTime() - arrivalTime > 2)
				continue;
			
			if (r.getStartTime() - arrivalTime == 0)
				reward += Main.BONUS_VALUE;
			
			if (reward > highestReward){
				highestReward = reward;
				highestRide = r;
			}
		}
		
		currentRide = highestRide;
		targetPosition[0] = currentRide.getStartLoc()[0];
		targetPosition[1] = currentRide.getStartLoc()[1];
		currentRide.setIsPickedUp();
		
		System.out.println("Found new ride: " + currentRide.getRideNum());
		
	}
	
	public void MoveTowardsTarget(){
		System.out.println("Current Pos: " + currentPosition[0] + ":" + currentPosition[1]);
		System.out.println("Target Pos: " + targetPosition[0] + ":" + targetPosition[1]);
		if (currentPosition[0] == targetPosition[0] && currentPosition[1] == targetPosition[1]){
			if (targetPosition[0] == currentRide.getStartLoc()[0] && targetPosition[1] == currentRide.getStartLoc()[1]){
				targetPosition[0] = currentRide.getFinishLoc()[0];
				targetPosition[1] = currentRide.getFinishLoc()[1];
			}
			
			if (targetPosition[0] == currentRide.getFinishLoc()[0] && targetPosition[1] == currentRide.getFinishLoc()[1]){
				System.out.println("Ride Completed!");
				Main.COMPLETED_RIDES++;
				currentRide.complete();
				FindNewRide();
			}
		}
		
		if (currentPosition[0] < targetPosition[0]) // Move Right
			currentPosition[0]++;
		else if (currentPosition[0] > targetPosition[0]) // Move Left
			currentPosition[0]--;
		else if (currentPosition[1] < targetPosition[1]) // Move Up
			currentPosition[1]++;
		else if (currentPosition[1] > targetPosition[1]) // Move Down
			currentPosition[1]--;
		
		System.out.println("Moved Car");
	}
	
	private int GetDistance(int[] a, int[] b){
		return (int) Math.hypot(a[0] - b[0], a[1] - b[1]);
	}
	
	private List<Ride> getCompletedRides(){
		return completedRides;
	}
	
	private int getAmountOfRides(){
		return completedRides.size();
	}
	
	

}
