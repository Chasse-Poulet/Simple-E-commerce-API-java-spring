package chassepoulet.simpleecommerceapijava.service;

import chassepoulet.simpleecommerceapijava.dto.UpdateProductDTO;
import chassepoulet.simpleecommerceapijava.model.Product;
import chassepoulet.simpleecommerceapijava.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product book = new Product();
        book.setName("Book");
        book.setPrice(9.99);

        Product pen = new Product();
        pen.setName("Pen");
        pen.setPrice(1.99);

        when(productRepository.findAll()).thenReturn(Arrays.asList(book, pen));

        productService.getAllProducts();

        verify(productRepository).findAll();
    }

    @Test
    void testGetProductById() {
        String id = "64abc";

        Product book = new Product();
        book.setName("Book");
        book.setPrice(9.99);
        book.setId(id);

        Optional<Product> op = Optional.of(book);

        when(productRepository.findById(id)).thenReturn(op);

        productService.getProductById(id);

        verify(productRepository).findById(id);
    }

    @Test
    void testCreateProduct() {
        Product book = new Product();
        book.setName("Book");
        book.setPrice(9.99);

        when(productRepository.save(book)).thenReturn(book);

        productService.createProduct(book);

        verify(productRepository).save(book);
    }

    @Test
    void testUpdateProduct() {
        String id = "64abc";

        Product book = new Product();
        book.setName("Book");
        book.setPrice(9.99);
        book.setId(id);

        Product updatedBook = new Product();
        updatedBook.setName("One Piece");
        updatedBook.setPrice(9.99);
        updatedBook.setId(id);

        UpdateProductDTO dto = new UpdateProductDTO();
        dto.setName("One Piece");

        Optional<Product> op = Optional.of(book);

        when(productRepository.findById(id)).thenReturn(op);
        when(productRepository.save(book)).thenReturn(updatedBook);

        productService.updateProduct(id, dto);

        verify(productRepository).findById(id);
        verify(productRepository).save(book);
    }

    @Test
    void testDeleteProductById() {
        String id = "64abc";

        Product book = new Product();
        book.setName("Book");
        book.setPrice(9.99);
        book.setId(id);

        doNothing().when(productRepository).deleteById(id);

        productService.deleteProductById(id);

        verify(productRepository).deleteById(id);
    }
}
