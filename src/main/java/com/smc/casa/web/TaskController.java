package com.smc.casa.web;

import com.smc.casa.model.Task;
import com.smc.casa.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping("/indexCestaCompra")
    public String inicio(Model model) {
        try {
            List<Task> taskList = service.findAll();
            model.addAttribute("task", new Task());
            model.addAttribute("taskList", taskList);
            return "indexCestaCompra";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    @PostMapping("/addTask")
    public RedirectView addTask(Model model, @ModelAttribute Task task) {
        try {
            task.setMarked(false);
            Task t = service.addTask(task);
            List<Task> taskList = service.findAll();
            model.addAttribute("taskList", taskList);
            return new RedirectView("indexCestaCompra");
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return new RedirectView("error");
        }
    }


    @PostMapping("/editTask/{id}")
    public RedirectView editTaskForm(Model model, @PathVariable("id") long id, @ModelAttribute("task") Task task) {
        try {
            Task t = task;
            if (task.getTitle().equalsIgnoreCase("")) {
                t = service.findById(id);
                boolean marked = t.isMarked();
                t.setMarked(!marked);
            }
            service.updateOne(t, id);
            return new RedirectView("/indexCestaCompra");
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return new RedirectView("error");
        }
    }

    @PostMapping("/deleteTask/{id}")
    public RedirectView deleteTask(Model model, @PathVariable("id") long id) {
        try {
            Task taskToDelete = service.findById(id);
            if(taskToDelete.isMarked()) {
                Task deletedTask = service.deleteOne(taskToDelete, id);
                List<Task> taskList = service.findAll();
                model.addAttribute("taskList", taskList);
                return new RedirectView("/indexCestaCompra");
            }
            else {
                return new RedirectView("/indexCestaCompra");
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            return new RedirectView("error");
        }
    }

}
