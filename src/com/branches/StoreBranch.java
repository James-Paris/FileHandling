package com.branches;

import java.util.ArrayList;
import java.util.List;

public abstract class StoreBranch {

	List<Product> productAvailable = new ArrayList<Product>();
	
	public static class Product {
		public String name;
		public int quantityInStock = 0;
		
		public Product(String name, int quantity) {
			this.name = name;
			this.quantityInStock = quantity;
		}
	}
	
	public void receiveShipment(Product product) {
		if(!this.productAvailable.contains(product)) {
			this.productAvailable.add(new Product(product.name, 0));
		}
		for(int x=0; x < productAvailable.size(); x++) {
			if(productAvailable.get(x).name.equals(product.name)) {
				productAvailable.get(x).quantityInStock += product.quantityInStock;
			}
		}
		
	}
	
	public abstract void shipOrder(Product product);
	
	public String printStock() {
		String stock = "";
		for(int x=0; x< this.productAvailable.size(); x++) {
			if(x > 0) {
				stock = stock.concat(", ").concat(productAvailable.get(x).name).concat(": ").concat("" + productAvailable.get(x).quantityInStock);
			} else {
				stock = stock.concat(productAvailable.get(x).name + ": " + productAvailable.get(x).quantityInStock);
			}
		}	
		
		return stock;
	}
	
}
