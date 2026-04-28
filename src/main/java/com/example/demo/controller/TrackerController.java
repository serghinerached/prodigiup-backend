package com.example.demo.controller;

import com.example.demo.model.tracker;
import com.example.demo.repository.TrackerRepository;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;

@RestController
@RequestMapping("/api/tracker")
@CrossOrigin(origins = "*")
public class TrackerController {

    private final TrackerRepository repository;

    public TrackerController(TrackerRepository repository) {
        this.repository = repository;
    }

    // GET all tracker
    @GetMapping
    public List<tracker> getAllTracker() {
        return repository.findAll();
    }

    // CREATE tracker
    @PostMapping
    public tracker createTracker(@RequestBody @NonNull tracker tracker) {
        return repository.save(tracker);
    }

    // GET tracker by id
    @GetMapping("/{id}")
    public tracker getTracker(@PathVariable @NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("tracker not found"));
    }

    // DELETE tracker
    @DeleteMapping("/{id}")
    public void deleteTracker(@PathVariable @NonNull Long id) {
        repository.deleteById(id);
    }


    private int getIntCellValue(Cell cell) {
    if (cell == null) return 0;
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();

            case STRING:
                String value = cell.getStringCellValue().trim();
                return value.isEmpty() ? 0 : Integer.parseInt(value);

            default:
                return 0;
        }
    }

    // IMPORT EXCEL
    @PostMapping("/import-excel")
    public String importExcel(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream()) {

            repository.deleteAllInBatch();

            Workbook workbook = WorkbookFactory.create(inp);
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();
            List<tracker> trackerList = new ArrayList<>();

            for (Row row : sheet) { // FEUILLE et COLONNES EXCEL

                if (row.getRowNum() == 0) continue;

                // creation objet tracker
                tracker tracker = new tracker();

                // col 1 = number
                tracker.setNumber(formatter.formatCellValue(row.getCell(0)));

                // col 1 = week
            //    tracker.setWeek(getIntCellValue(row.getCell(0)));

                // col 2 = opened
                Cell cellOpened = row.getCell(1);
                if (cellOpened != null && cellOpened.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellOpened)) {
                    tracker.setOpened(cellOpened.getLocalDateTimeCellValue());
                }
  
                // col 3 = assigned to
                tracker.setAssignedTo(formatter.formatCellValue(row.getCell(2)));
                // col 4 = state
                tracker.setState(formatter.formatCellValue(row.getCell(3)));
                // col 6 = assignment group
                tracker.setAssignmentGroup(formatter.formatCellValue(row.getCell(5)));
                // col 8 = requested for
                tracker.setRequestedFor(formatter.formatCellValue(row.getCell(7)));

                // col 10 = resolved
                Cell cellResolved = row.getCell(9);
                if (cellResolved != null && cellResolved.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellResolved)) {
                    tracker.setResolved(cellResolved.getLocalDateTimeCellValue());
                }

                // col 11 = closed
                Cell cellClosed = row.getCell(10);
                if (cellClosed != null && cellClosed.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellClosed)) {
                    tracker.setClosed(cellClosed.getLocalDateTimeCellValue());
                }

                //col 12 = service
                tracker.setService(formatter.formatCellValue(row.getCell(11)));
                // col 14 = reopen count
                tracker.setReopenCount(getIntCellValue(row.getCell(13)));

                // AJOUT OBJET tracker à la liste
                trackerList.add(tracker);
            }

            workbook.close();
            repository.saveAll(trackerList);

            return trackerList.size() + " tracker importés";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur import: " + e.getMessage();
        }
    }
}