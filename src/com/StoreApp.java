package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/*
 * @author James Paris
 * Date: 1/1/21
 * Purpose: Practice File handling - Appending to a file
 */
public class StoreApp {

	public static void main(String[] args) throws IOException {
		
		
		while(true) {
			Scanner scan = new Scanner(System.in);
			int decision = promptUser();
			
			
			switch(decision) {
			
				case 1:
					addOrder();
					break;
					
				case 2:
					runSimulation();
					break;
				
				case 3:
					System.out.println("Thank you for using my Store Management System.");
					return;
			}
			
			
		}
		
	}
	
	
	//Will open a file, add an order and append it to the eof.
	private static void addOrder() throws IOException {
		//get curr orders.
		ClothingStore clothesStore = new ClothingStore();
		clothesStore.readFromFile();
		
		//do user prompts to get input
		Scanner scan = new Scanner(System.in);
		System.out.println("\nPlease enter the item name: ");
		String itemName = scan.nextLine();
		
		System.out.println("\nPlease the branch to order from (east/west): ");
		String storeBranch = scan.nextLine();
		
		System.out.println("\nPlease enter the number of items: ");
		int num = scan.nextInt();
		
		System.out.println("\nPlease enter the total price: ");
		float price = scan.nextFloat();
		
		//add order to orders
		ClothingStore.Order order = new ClothingStore.Order(itemName, storeBranch, num, price, 0);
		clothesStore.receiveOrder(order);
		
		//save orders by writing it back to file
		clothesStore.savetoFile();
		
		
	}


	public static int promptUser() {
		int choice;
		do {
			Scanner scan = new Scanner(System.in);
			System.out.println("");
			System.out.println("###########################################################");
			System.out.println(" [ 1 ] Add an Order");
			System.out.println(" [ 2 ] Run Order Sim");
			System.out.println(" [ 3 ] Exit");
			System.out.println("#################### SELECT A FUNCTION ####################");
			System.out.println("Please select a number 1-3: ");
				
			//grab as a string and convert it to int, in case they entered anything besides numbers.
			choice = Integer.parseInt(scan.nextLine());
		} while(choice > 3 || choice < 1);
		
		return choice;
	}

	public static void runSimulation() throws IOException {
		ClothingStore clothesStore = new ClothingStore();
		clothesStore.receiveShipments();
		
		clothesStore.readFromFile();
		//clothesStore.receiveOrders();
		clothesStore.processOrders();
		clothesStore.printSummary();
		
	}
	
	
}
