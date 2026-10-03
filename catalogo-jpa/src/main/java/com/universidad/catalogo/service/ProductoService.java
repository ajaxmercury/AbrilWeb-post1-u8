package com.universidad.catalogo.service;

import com.universidad.catalogo.model.Categoria;
import com.universidad.catalogo.model.Producto;
import com.universidad.catalogo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepository, CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAllConCategoria();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Transactional
    public Producto guardar(Producto producto, Long categoriaId) {
        Categoria categoria = categoriaService.buscarPorId(categoriaId);
        categoria.agregarProducto(producto);
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        Categoria categoria = producto.getCategoria();
        if (categoria != null) {
            categoria.quitarProducto(producto);
        }
        productoRepository.delete(producto);
    }

    public List<Producto> listarPorCategoriaConPrecioMayorA(Long categoriaId, BigDecimal precioMinimo) {
        return productoRepository.buscarPorCategoriaConPrecioMayorA(categoriaId, precioMinimo);
    }
}
