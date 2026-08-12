package com.example.demo.controller;

import com.example.demo.model.LivroModel;
import com.example.demo.services.LivroServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroServices livroServices;

    @PostMapping
    public ResponseEntity<LivroModel> createLivro(@Valid @RequestBody LivroModel livro) {
        LivroModel savedLivro = livroServices.saveLivro(livro);
        return new ResponseEntity<>(savedLivro, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LivroModel>> getAllLivros() {
        List<LivroModel> livros = livroServices.getAllLivros();
        return new ResponseEntity<>(livros, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroModel> getLivroById(@PathVariable Long id) {
        Optional<LivroModel> livro = livroServices.getLivroById(id);
        return livro.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroModel> updateLivro(@PathVariable Long id, @Valid @RequestBody LivroModel livro) {
        LivroModel updatedLivro = livroServices.updateLivro(id, livro);
        if (updatedLivro != null) {
            return new ResponseEntity<>(updatedLivro, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        Optional<LivroModel> livro = livroServices.getLivroById(id);
        if (livro.isPresent()) {
            livroServices.deleteLivro(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}