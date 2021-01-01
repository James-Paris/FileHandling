package branches;

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
			this.productAvailable.add(product);
		}
		for(int x=0; x < productAvailable.size(); x++) {
			if(productAvailable.get(x).name.equals(product.name)) {
				productAvailable.get(x).quantityInStock += product.quantityInStock;
			}
		}
		
	}
	
	public void shipOrder(Product product);
	
}
