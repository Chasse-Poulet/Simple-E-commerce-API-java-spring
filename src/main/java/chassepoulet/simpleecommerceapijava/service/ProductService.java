package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.dto.UpdateProductDTO;
import chassepoulet.simpleecommerceapijava.exception.ProductNotFoundException;
import chassepoulet.simpleecommerceapijava.model.Product;
import chassepoulet.simpleecommerceapijava.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(String id, UpdateProductDTO updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    String name = updatedProduct.getName();
                    String description = updatedProduct.getDescription();
                    Double price = updatedProduct.getPrice();

                    if(name != null) {
                        existingProduct.setName(updatedProduct.getName());
                    }

                    if(description != null) {
                        existingProduct.setDescription(updatedProduct.getDescription());
                    }

                    if(price != null) {
                        existingProduct.setPrice(updatedProduct.getPrice());
                    }

                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void deleteProductById(String id) {
        productRepository.deleteById(id);
    }
}
