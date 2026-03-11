package com.example.demo.controller;

import com.example.demo.model.Incident;
import com.example.demo.repository.IncidentRepository;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.*;


@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
public class IncidentController {

    private final IncidentRepository repository;

    public IncidentController(IncidentRepository repository) {
        this.repository = repository;
    }

    // GET all incidents
    @GetMapping
    public List<Incident> getAllIncidents() {
        return repository.findAll();
    }

    // CREATE incident
    @PostMapping
    public Incident createIncident(@RequestBody @NonNull Incident incident) {
        return repository.save(incident);
    }

    // GET incident by id
    @GetMapping("/{id}")
    public Incident getIncident(@PathVariable  @NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }

    // DELETE incident
    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable  @NonNull Long id) {
        repository.deleteById(id);
    }

    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        try (InputStream inp = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // ignorer l'en-tête
                Cell cell = row.getCell(0); // Colonne "Number"
                if (cell != null) {
                    String number = cell.getStringCellValue();
                    Incident incident = new Incident();
                    incident.setNumber(number);
                    repository.save(incident);
                }
            }

            workbook.close();
            return "Import terminé";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'import : " + e.getMessage();
        }
    }


}