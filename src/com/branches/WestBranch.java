package com.branches;

import possibleIssues.DeliveryRefusedException;

public class WestBranch extends StoreBranch {

	DeliveryTruck deliveryTruck = new DeliveryTruck();
	
	@Override
	public void shipOrder(Product product) {
		// TODO Auto-generated method stub
		deliveryTruck.deliver(product);
	}
	
	static class DeliveryTruck {
		void deliver(StoreBranch.Product prod) {
			if(prod.name.contains("large")) {
				System.out.println("Large product detected - This size is an issue.");
				throw new DeliveryRefusedException("Product too large to deliver.");
			}
			System.out.println("Delivery Truck delivering product: " + prod.name);
		}
		
	}
	

}
