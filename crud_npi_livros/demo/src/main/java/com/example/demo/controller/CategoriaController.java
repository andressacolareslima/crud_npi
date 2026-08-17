package com.example.demo.controller;

import com.example.demo.model.CategoriaModel;
import com.example.demo.services.CategoriaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaServices categoriaServices; 
    
    @PostMapping
    public ResponseEntity<CategoriaModel> createCategoria(@RequestBody CategoriaModel categoria) {
        CategoriaModel savedCategoria = categoriaServices.saveCategoria(categoria);
        return new ResponseEntity<>(savedCategoria, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaModel>> getAllCategorias() {
        List<CategoriaModel> categorias = categoriaServices.getAllCategorias();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaModel> getCategoriaById(@PathVariable Long id) {
        Optional<CategoriaModel> categoria = categoriaServices.getCategoriaById(id);
        return categoria.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaModel> updateCategoria(@PathVariable Long id, @RequestBody CategoriaModel categoria) {
        CategoriaModel updatedCategoria = categoriaServices.updateCategoria(id, categoria);
        if (updatedCategoria != null) {
            return new ResponseEntity<>(updatedCategoria, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        Optional<CategoriaModel> categoria = categoriaServices.getCategoriaById(id);
        if (categoria.isPresent()) {
            categoriaServices.deleteCategoria(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}