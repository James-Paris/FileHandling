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
	
}
