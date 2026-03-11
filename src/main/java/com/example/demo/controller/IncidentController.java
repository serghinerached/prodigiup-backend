package com.example.demo.controller;

import com.example.demo.model.Incident;
import com.example.demo.repository.IncidentRepository;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
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

            // vider la table
            repository.deleteAllInBatch();
            repository.resetIdSequence();

            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            List<Incident> incidents = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue; // ignorer header

                String number = formatter.formatCellValue(row.getCell(0));
                if (number == null || number.trim().isEmpty()) {
                    continue; // ignorer ligne vide
                }

                Incident incident = new Incident();
                incident.setNumber(number.trim());

                // OPENED
                Cell cell2 = row.getCell(1);
                if (cell2 != null && cell2.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell2)) {
                    incident.setOpened(cell2.getDateCellValue());
                }

                // ASSIGNED TO
                String assignedTo = formatter.formatCellValue(row.getCell(2));
                incident.setAssignedTo(assignedTo);

                // STATE
                String state = formatter.formatCellValue(row.getCell(3));
                incident.setState(state);

                // ASSIGNMENT GROUP
                String assignementGroup = formatter.formatCellValue(row.getCell(5));
                incident.setAssignementGroup(assignementGroup);

                // REQUESTED FOR
                String requestedfor = formatter.formatCellValue(row.getCell(7));
                incident.setRequestedfor(requestedfor);

                // RESOLVED
                Cell cell7 = row.getCell(9);
                if (cell7 != null && cell7.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell7)) {
                    incident.setResolved(cell7.getDateCellValue());
                }

                // CLOSED
                Cell cell8 = row.getCell(10);
                if (cell8 != null && cell8.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell8)) {
                    incident.setClosed(cell8.getDateCellValue());
                }
                // SERVICE
                String service = formatter.formatCellValue(row.getCell(11));
                incident.setService(service);

                // REOPEN COUNT
                Cell cell12 = row.getCell(13);
                if (cell12 != null && cell12.getCellType() == CellType.NUMERIC) {
                    incident.setReopencount((int) cell12.getNumericCellValue());
                }
                

                incidents.add(incident);
            }

            workbook.close();

            // insertion batch (beaucoup plus rapide)
            repository.saveAll(incidents);

            return incidents.size() + " incidents importés";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'import : " + e.getMessage();
        }
    }


}