package com.example.demo.controller;

import com.example.demo.model.Incident;
import com.example.demo.repository.IncidentRepository;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;
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
    public Incident getIncident(@PathVariable @NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
    }

    // DELETE incident
    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable @NonNull Long id) {
        repository.deleteById(id);
    }

    // IMPORT EXCEL
    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream()) {

            repository.deleteAllInBatch();

            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            List<Incident> incidents = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                String number = formatter.formatCellValue(row.getCell(0));
                if (number == null || number.trim().isEmpty()) continue;

                Incident incident = new Incident();
                incident.setNumber(number.trim());

                // OPENED
                Cell c1 = row.getCell(1);
                if (c1 != null && c1.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c1)) {
                    incident.setOpened(c1.getLocalDateTimeCellValue());
                }

                incident.setAssignedTo(formatter.formatCellValue(row.getCell(2)));
                incident.setState(formatter.formatCellValue(row.getCell(3)));

                incident.setAssignmentGroup(formatter.formatCellValue(row.getCell(5)));
                incident.setRequestedFor(formatter.formatCellValue(row.getCell(7)));

                // RESOLVED
                Cell c7 = row.getCell(9);
                if (c7 != null && c7.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c7)) {
                    incident.setResolved(c7.getLocalDateTimeCellValue());
                }

                // CLOSED
                Cell c8 = row.getCell(10);
                if (c8 != null && c8.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c8)) {
                    incident.setClosed(c8.getLocalDateTimeCellValue());
                }

                incident.setService(formatter.formatCellValue(row.getCell(11)));

                // REOPEN COUNT
                Cell c12 = row.getCell(13);
                if (c12 != null && c12.getCellType() == CellType.NUMERIC) {
                    incident.setReopenCount((int) c12.getNumericCellValue());
                }

                incidents.add(incident);
            }

            workbook.close();
            repository.saveAll(incidents);

            return incidents.size() + " incidents importés";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur import: " + e.getMessage();
        }
    }
}