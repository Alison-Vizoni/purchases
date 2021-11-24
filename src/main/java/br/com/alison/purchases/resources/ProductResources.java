package br.com.alison.purchases.resources;

import br.com.alison.purchases.domain.Category;
import br.com.alison.purchases.domain.Product;
import br.com.alison.purchases.dto.CategoryDTO;
import br.com.alison.purchases.dto.ProductDTO;
import br.com.alison.purchases.resources.utils.URL;
import br.com.alison.purchases.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductResources {

    @Autowired
    private ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id){
        Product product = service.findProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping()
    public ResponseEntity<Page<ProductDTO>> findPage(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "categories", defaultValue = "") String categories,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        List<Long> ids = URL.decodeLongList(categories);
        String nameDecoded = URL.decodeParam(name);
        Page<Product> products = service.findAll(nameDecoded, ids, page, linesPerPage, orderBy, direction);
        Page<ProductDTO> productsDTO = products.map(newProduct -> new ProductDTO(newProduct));
        return ResponseEntity.ok().body(productsDTO);
    }

}
