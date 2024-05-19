package org.example.distancedata.controllers;

import org.example.distancedata.dto.ContinentDTO;
import org.example.distancedata.entity.Continent;
import org.example.distancedata.entity.Country;
import org.example.distancedata.exception.ResourceNotFoundException;
import org.example.distancedata.services.implementation.ContinentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ContinentController {

    private final ContinentServiceImpl continentService;

    public ContinentController(ContinentServiceImpl continentService) {
        this.continentService = continentService;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("continents", continentService.read());
        return "all";
    }

    @PostMapping("/create")
    public String createContinent(@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        try {
            // Создаем континент, используя переданное имя
            Continent newContinent = new Continent(name);
            continentService.create(newContinent); // Сохраняем континент в базу данных
            // Добавляем атрибут для передачи названия созданного континента на главную страницу
            redirectAttributes.addFlashAttribute("newContinentName", name);
        } catch (Exception e) {
            e.printStackTrace(); // Обработка ошибок
            // Возвращаемся на главную страницу с сообщением об ошибке
            return "redirect:/";
        }
        // Перенаправляем на главную страницу
        return "redirect:/all";
    }

    @PostMapping("/delete/{id}")
    public RedirectView deleteContinent(@PathVariable("id") Long id) {
        try {
            continentService.delete(id);
            // Перенаправляем на страницу со всеми континентами после успешного удаления
            return new RedirectView("/all");
        } catch (Exception e) {
            // В случае ошибки перенаправляем на страницу ошибки или показываем сообщение об ошибке
            RedirectView redirectView = new RedirectView("/error");
            redirectView.addStaticAttribute("errorMessage", "Ошибка удаления континента: " + e.getMessage());
            return redirectView;
        }
}



    @PostMapping("/update/{id}/{newName}")
    public String updateContinent(@PathVariable("id") Long id,
                                  @PathVariable("newName") String newName,
                                  Model model) {
        try {
            Continent updatedContinent = continentService.updateContinent(id, newName);
            model.addAttribute("message", "Континент успешно обновлен: " + updatedContinent.getName());
        } catch (Exception e) {
            model.addAttribute("message", "Ошибка при обновлении континента: " + e.getMessage());
        }
        return "redirect:/all";
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateContinent(@PathVariable("id") Long id,
                                             @RequestBody ContinentDTO continentDTO) {
        try {
            Continent updatedContinent = continentService.updateContinent(id, continentDTO);
            return ResponseEntity.ok(updatedContinent); // Возвращаем обновленный континент
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при обновлении континента: " + e.getMessage());
        }
    }



    @GetMapping("/")
    public String index() {
        return "index";
    }
}
