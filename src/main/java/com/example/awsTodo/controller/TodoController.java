package com.example.awsTodo.controller;


import com.example.awsTodo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TodoController {

    private final TodoRepository repository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("todos", repository.findAll());
        return "index";
    }

    @PostMapping("/add")
    public String add(@RequestParam String task) {
        repository.save(task);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        repository.delete(id);
        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @RequestParam String task) {
        repository.update(id, task);
        return "redirect:/";
    }
}
