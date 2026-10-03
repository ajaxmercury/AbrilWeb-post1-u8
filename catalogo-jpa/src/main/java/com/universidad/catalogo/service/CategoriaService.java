package com.universidad.catalogo.service;

import com.universidad.catalogo.model.Categoria;
import com.universidad.catalogo.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas(String q) {
        if (q != null && !q.trim().isEmpty()) {
            return categoriaRepository.findByNombreContainingIgnoreCase(q);
        }
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    @Transactional
    public Categoria guardar(Categoria categoria) {
        Optional<Categoria> existente = categoriaRepository.findByNombreIgnoreCase(categoria.getNombre());
        if (existente.isPresent() && !existente.get().getId().equals(categoria.getId())) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre.");
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}
