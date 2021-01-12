package com.branches;

import possibleIssues.DeliveryUnavailableException;

public class EastBranch extends StoreBranch { 
	
	public DeliveryBike deliveryBike = new DeliveryBike();
	
	@Override
	public void shipOrder(Product product) {
		deliveryBike.deliver(product);
		
	}
	
	
	public static class DeliveryBike {
		boolean isBroken = true;
		
		public void deliver(StoreBranch.Product prod) {
			if(isBroken) {
				throw new DeliveryUnavailableException("Bike is broken");
			}
			System.out.println("Bike delivering product: " + prod.name);
			
			
		}
		
		public void setBroken(boolean broken) {
			isBroken = broken;
		}
		
	}
	

}
