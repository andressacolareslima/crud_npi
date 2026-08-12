package com.example.demo.services;
import com.example.demo.model.CategoriaModel;
import com.example.demo.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service


public class CategoriaServices {
    @Autowired
    CategoriaRepository categoriaRepository;

    public List<CategoriaModel> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<CategoriaModel> getCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    public CategoriaModel saveCategoria(CategoriaModel categoria) {
        return categoriaRepository.save(categoria);
    }

    public CategoriaModel updateCategoria(Long id, CategoriaModel categoria) {
        Optional<CategoriaModel> existingCategoria = categoriaRepository.findById(id);
        if (existingCategoria.isPresent()) {
            CategoriaModel updatedCategoria = existingCategoria.get();
            updatedCategoria.setNome(categoria.getNome());
            return categoriaRepository.save(updatedCategoria);
        } else {
            return null;
        }
    }

    public void deleteCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }

}
