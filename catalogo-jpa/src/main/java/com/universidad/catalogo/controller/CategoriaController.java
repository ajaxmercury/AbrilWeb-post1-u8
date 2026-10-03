package com.universidad.catalogo.controller;

import com.universidad.catalogo.model.Categoria;
import com.universidad.catalogo.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas(q));
        model.addAttribute("q", q);
        return "categorias/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "categorias/formulario";
        }
        try {
            categoriaService.guardar(categoria);
        } catch (IllegalStateException e) {
            result.rejectValue("nombre", "error.categoria", e.getMessage());
            return "categorias/formulario";
        }
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Categoria categoria = categoriaService.buscarPorId(id);
            model.addAttribute("categoria", categoria);
            return "categorias/formulario";
        } catch (RuntimeException e) {
            return "redirect:/categorias";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String confirmarEliminar(@PathVariable Long id, Model model) {
        try {
            Categoria categoria = categoriaService.buscarPorId(id);
            model.addAttribute("categoria", categoria);
            return "categorias/confirmar-eliminar";
        } catch (RuntimeException e) {
            return "redirect:/categorias";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, Model model) {
        try {
            categoriaService.eliminar(id);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            Categoria categoria = categoriaService.buscarPorId(id);
            model.addAttribute("categoria", categoria);
            return "categorias/confirmar-eliminar";
        } catch (RuntimeException e) {
            return "redirect:/categorias";
        }
        return "redirect:/categorias";
    }
}
