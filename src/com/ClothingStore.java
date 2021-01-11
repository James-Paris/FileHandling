package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.branches.EastBranch;
import com.branches.StoreBranch;
import com.branches.WestBranch;

import possibleIssues.DeliveryRefusedException;
import possibleIssues.DeliveryUnavailableException;

/*
 * @author James Paris
 * Date: 1/1/21
 * Purpose: Practice File handling - Appending to a file
 */
public class ClothingStore {
	
	Map<String, StoreBranch> storeBranches = new HashMap<String, StoreBranch>();
	
	List<Order> orders = new ArrayList<Order>();
	
	int ordersProcessed = 0;
	float revenueProcessed = 0F;
	
	public ClothingStore() {
		storeBranches.put("east", new EastBranch());
		storeBranches.put("west", new WestBranch());	
	}
	
	public void receiveShipments() {
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("hat", 100));
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("pants", 100));
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("shirts", 100));
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("shoes", 100));
		
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("hats", 250));
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("pants", 250));
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("shirts", 250));
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("shoes", 250));
	}
	
	public void readFromFile() throws IOException {
		File orderInputFile = new File("/Users/jmparis/eclipse-workspace/HCLFileHandling/data/Orders.csv");
		FileInputStream fileistream = new FileInputStream(orderInputFile);
		int r = 0;
		StringBuilder sb = new StringBuilder();
		while((r = fileistream.read()) != -1) {
			sb.append((char) r);
		}
		String orders = sb.toString();
		System.out.println(orders);
		String[] lines = orders.split(",");
		for(String line : lines) {
			String[] columns = line.split(",");
			if(columns.length == 4) {
				Order order = new Order(
						columns[0],
						columns[1],
						Integer.parseInt(columns[2]),
						Float.parseFloat(columns[3])
					);
				receiveOrder(order);			
			}
		}
	}
	
	public void receiveOrders() {
		receiveOrder(new Order("shirts", "east", 8, 2.0F));
		receiveOrder(new Order("shoes", "east", 10, 2.0F));
		receiveOrder(new Order("pants", "east", 5, 2.0F));
		receiveOrder(new Order("hats", "east", 2, 2.0F));
		receiveOrder(new Order("shoes", "west", 15, 2.0F));
		receiveOrder(new Order("shirts", "west", 5, 2.0F));
	}
	
	public void receiveOrder(Order order) {
		try {
            orders.add(order);
        } catch (Exception e) {
        	System.out.println("ERROR! Unable to receive order. Message: " + e.getMessage());
        }
	}
	
	public void processOrders() {
		for(Order order: orders) {
			try {
				this.processOrder(order);
			} catch(DeliveryRefusedException e) {
				System.out.println("Delivery refused. Message: " + e.getMessage());
				System.out.println("Attempting to ship from easy branch instead");
				order.closestLoc = "east";
				try {
					this.processOrder(order);
				} catch(Exception error) {
					System.out.println("Unable to try delivery again after shipping from East branch instead." + error.getMessage());
				}
			} catch(DeliveryUnavailableException e) {
				System.out.println("Delivery unavailable. Message: " + e.getMessage());
				System.out.println("Fixing the truck");
				((EastBranch) storeBranches.get("east")).deliveryBike.setBroken(false);
				try {
					System.out.println("Retrying the order.");
					this.processOrder(order);
				} catch(Exception error) {
					System.out.println("Unable to try delivery after fixing truck. Message: " + error.getMessage());
				}
			} catch (Exception e) {
				System.out.println("Delivery not possible. Message: " + e.getMessage());
			} finally {
				System.out.println("====== MOVING ON ======");
			}
		}
	}
		
	private void processOrder(Order order) throws DeliveryRefusedException, DeliveryUnavailableException {
		this.storeBranches.get(order.closestLoc).shipOrder(new StoreBranch.Product(order.product, order.size));

		//Order is now processed.
		order.setProcessed(true);
		
		//track rev and orders processed
		revenueProcessed += order.price;
		ordersProcessed++;
		
	}
	
	public void printSummary() {
		System.out.println("Processed " + ordersProcessed + " orders.");
		System.out.println("Today we had $" + revenueProcessed + " in revenue.");
	}
	
	
	static class Order {
		
		boolean processed = false;
	    public String product;
	    public String closestLoc;
	    public int size = 0;
	    public float price = 0;

		public Order(String prodName, String closestLoc, int size, float price) {
			this.product = prodName;
			this.closestLoc = closestLoc;
			this.size = size;
			this.price = price;
		}
		
		@Override
		public String toString() {
			return "Order{" +
					"processed=" + processed + 
					", product=" + product + '\'' +
					", size=" + size + 
					", pricce=" + price +
					'}';
		}
		
	   public void setProcessed(boolean processed) {     
		   this.processed = processed;
	   }

	   public boolean isProcessed() {     
		   return processed;
	   }
		
	}
	
}
