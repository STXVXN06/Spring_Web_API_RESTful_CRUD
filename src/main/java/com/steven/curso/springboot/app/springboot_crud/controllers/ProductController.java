package com.steven.curso.springboot.app.springboot_crud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.steven.curso.springboot.app.springboot_crud.ProductValidation;
import com.steven.curso.springboot.app.springboot_crud.entities.Product;
import com.steven.curso.springboot.app.springboot_crud.services.ProductServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductValidation productValidation;

    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public List<Product> list() {
        return productService.findAll();
    }

    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok().body(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {

        // productValidation.validate(product, result);
        
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Product productNew = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productNew);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(  @Valid @RequestBody Product product, BindingResult result, @PathVariable Long id) {
        productValidation.validate(product, result);
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        Optional<Product> productOptional = productService.update(id, product);
        
        if (productOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());
            
        }
        return ResponseEntity.notFound().build();

    }

    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Optional<Product> productOptional = productService.delete(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok().body(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    
    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        // return ResponseEntity.status(400).body(errors);
    }
}
