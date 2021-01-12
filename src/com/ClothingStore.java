package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("hat", 500));
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("pants", 600));
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("shirts", 500));
		storeBranches.get("east").receiveShipment(new StoreBranch.Product("shoes", 600));
		
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("hats", 600));
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("pants",800));
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("shirts", 500));
		storeBranches.get("west").receiveShipment(new StoreBranch.Product("shoes", 800));
	}
	
	public void printStock() {
		System.out.println("");
		System.out.println("====================== Each Shop's Stock! ======================");
		System.out.println("East Shop: " + storeBranches.get("east").printStock());
		System.out.println("West Shop: " + storeBranches.get("west").printStock());
		System.out.println("================================================================");
		System.out.println("");
	}
	
	public void readFromFile() throws IOException {
		//C:\Users\jmparis\eclipse-workspace\HCLFileHandling\data
		
		File orderInputFile = new File("/Users/jmparis/eclipse-workspace/HCLFileHandling/data/Orders.csv");
		FileInputStream fileistream = new FileInputStream(orderInputFile);
		int r = 0;
		StringBuilder sb = new StringBuilder();
		while((r = fileistream.read()) != -1) {
			sb.append((char) r);
		}
		String orders = sb.toString();
		//System.out.println(orders);
		//System.out.println("About to parse order out. . . ");
		String[] lines = orders.split("\n");
		for(String line : lines) {
			String[] columns = line.split(",");
			if(columns.length == 6) {
				//System.out.println("CREATING ORDER: " + columns[0] + " " + columns[1] + " " + Integer.parseInt(columns[2]) + " "
					//				+ Float.parseFloat(columns[3]) + " " + Integer.parseInt(columns[4]));
				Order order = new Order(
						columns[0],
						columns[1],
						Integer.parseInt(columns[2]),
						Float.parseFloat(columns[3]),
						Integer.parseInt(columns[4])
					);
				receiveOrder(order);			
			}
		}
	}
	
	public void savetoFile() throws IOException {
		File orderOutputFile = new File("C:/Users/jmparis/eclipse-workspace/HCLFileHandling/data/OrdersUpdated.csv");
		FileOutputStream ostream = new FileOutputStream(orderOutputFile);
		int r = 0;
		
		if(orderOutputFile.createNewFile()) {
			
		} else {
			
		}
		
		for(int x=0; x<orders.size(); x++) {
			String msg = "" + orders.get(x).product + "," + orders.get(x).closestLoc + "," + orders.get(x).size + "," + orders.get(x).price + ",0,\n";
			ostream.write(msg.getBytes(), 0, msg.length());
			ostream.flush();
		}
		
		System.out.println("'OrdersUpdated.csv' has been saved successfully.");
		
	}
	
	public void receiveOrders() {
		receiveOrder(new Order("shirts", "east", 8, 10F, 0));
		receiveOrder(new Order("shoes", "east", 10, 12.5F, 0));
		receiveOrder(new Order("pants", "east", 5, 11F, 0));
		receiveOrder(new Order("hats", "east", 2, 9F, 0));
		receiveOrder(new Order("shoes", "west", 15, 12.5F, 0));
		receiveOrder(new Order("shirts", "west", 5, 10F, 0));
	}
	
	public void receiveOrder(Order order) {
		try {
			//System.out.println("Added Order: " + order.toString());
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
		System.out.println("DELIVERED! For: $" + order.price);
		
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
	    public int stepInProcess = 0;

		public Order(String prodName, String closestLoc, int size, float price, int stepInProcess) {
			this.product = prodName;
			this.closestLoc = closestLoc;
			this.size = size;
			this.price = price;
			this.stepInProcess = stepInProcess;
		}
		
		@Override
		public String toString() {
			return "Order{" +
					"processed=" + processed + 
					", product=" + product + '\'' +
					", size=" + size + 
					", price=" + price +
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
