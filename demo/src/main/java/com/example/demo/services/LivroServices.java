package com.example.demo.services;

import com.example.demo.model.CategoriaModel;
import com.example.demo.model.LivroModel;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroServices {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private CategoriaRepository categoriaRepository; // Injetamos o repositório de categoria aqui!

    public LivroModel saveLivro(LivroModel livro) {
        // Valida se a categoria foi enviada e se tem um ID
        if (livro.getCategoria() != null && livro.getCategoria().getId() != null) {
            Long categoriaId = livro.getCategoria().getId();
            
            // Busca a categoria no banco de dados pelo ID informado
            CategoriaModel categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + categoriaId));
            
            // Associa a categoria completa (com nome e ID) ao livro
            livro.setCategoria(categoria);
        } else {
            throw new RuntimeException("A categoria é obrigatória e precisa de um ID válido.");
        }

        return livroRepository.save(livro);
    }

    public List<LivroModel> getAllLivros() {
        return livroRepository.findAll();
    }

    public Optional<LivroModel> getLivroById(Long id) {
        return livroRepository.findById(id);
    }

    public LivroModel updateLivro(Long id, LivroModel livroAtualizado) {
        Optional<LivroModel> livroExistente = livroRepository.findById(id);
        
        if (livroExistente.isPresent()) {
            LivroModel livro = livroExistente.get();
            livro.setTitulo(livroAtualizado.getTitulo());
            livro.setAutor(livroAtualizado.getAutor());
            livro.setPreco(livroAtualizado.getPreco());

            // Atualiza e valida a categoria caso ela tenha sido enviada
            if (livroAtualizado.getCategoria() != null && livroAtualizado.getCategoria().getId() != null) {
                Long categoriaId = livroAtualizado.getCategoria().getId();
                
                CategoriaModel categoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + categoriaId));
                
                livro.setCategoria(categoria);
            }

            return livroRepository.save(livro);
        }
        return null;
    }

    public void deleteLivro(Long id) {
        livroRepository.deleteById(id);
    }
}