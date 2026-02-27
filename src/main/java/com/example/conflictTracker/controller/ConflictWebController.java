package com.example.conflictTracker.controller;

import com.example.conflictTracker.model.ConflictStatus;
import com.example.conflictTracker.service.ConflictService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/conflicts")
public class ConflictWebController {

    private final ConflictService conflictService;

    public ConflictWebController(ConflictService conflictService) {
        this.conflictService = conflictService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) ConflictStatus status, Model model) {
        model.addAttribute("conflicts", conflictService.list(status));
        model.addAttribute("selectedStatus", status == null ? "ALL" : status.name());
        model.addAttribute("statuses", ConflictStatus.values());
        return "conflicts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("conflict", conflictService.get(id));
        return "conflict-detail";
    }
}
