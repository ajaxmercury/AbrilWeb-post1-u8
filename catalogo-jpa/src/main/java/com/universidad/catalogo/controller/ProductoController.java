package com.universidad.catalogo.controller;

import com.universidad.catalogo.model.Producto;
import com.universidad.catalogo.service.CategoriaService;
import com.universidad.catalogo.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodas(null));
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarTodas(null));
        return "productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto, BindingResult result, @RequestParam("categoriaId") Long categoriaId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas(null));
            return "productos/formulario";
        }
        try {
            productoService.guardar(producto, categoriaId);
        } catch (RuntimeException e) {
            return "redirect:/productos";
        }
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Producto producto = productoService.buscarPorId(id);
            model.addAttribute("producto", producto);
            model.addAttribute("categorias", categoriaService.listarTodas(null));
            return "productos/formulario";
        } catch (RuntimeException e) {
            return "redirect:/productos";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
        } catch (RuntimeException e) {
            return "redirect:/productos";
        }
        return "redirect:/productos";
    }

    @GetMapping("/categoria/{categoriaId}/precio-mayor")
    public String filtrarPorCategoriaYPrecio(@PathVariable Long categoriaId, @RequestParam BigDecimal minimo, Model model) {
        model.addAttribute("productos", productoService.listarPorCategoriaConPrecioMayorA(categoriaId, minimo));
        model.addAttribute("minimo", minimo);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("categorias", categoriaService.listarTodas(null));
        return "productos/filtrados";
    }
}
