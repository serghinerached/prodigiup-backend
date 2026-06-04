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

    // IMPORT EXCEL INCIDENTS
   @PostMapping("/import-excel-incidents")
    public String importExcelIncidents(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inp)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            List<tracker> trackerList = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                // NUMBER (clé unique)
                String number = formatter.formatCellValue(row.getCell(0));
                if (number == null || number.isEmpty()) continue;

                // 🔥 UPSERT
                tracker tracker = repository.findByNumber(number)
                        .orElse(new tracker());

                tracker.setNumber(number);

                // OPENED
                Cell cellOpened = row.getCell(1);
                if (cellOpened != null && cellOpened.getCellType() == CellType.NUMERIC) {
                    tracker.setOpened(cellOpened.getLocalDateTimeCellValue());
                }

                // ASSIGNED TO
                tracker.setAssignedTo(formatter.formatCellValue(row.getCell(2)));

                // STATE
                tracker.setState(formatter.formatCellValue(row.getCell(3)));

                // ASSIGNMENT GROUP
                tracker.setAssignmentGroup(formatter.formatCellValue(row.getCell(5)));

                // REQUESTED FOR
                tracker.setRequestedFor(formatter.formatCellValue(row.getCell(7)));

                // RESOLVED
                Cell cellResolved = row.getCell(9);
                if (cellResolved != null && DateUtil.isCellDateFormatted(cellResolved)) {
                    tracker.setResolved(cellResolved.getLocalDateTimeCellValue());
                }

                // CLOSED
                Cell cellClosed = row.getCell(10);
                if (cellClosed != null && DateUtil.isCellDateFormatted(cellClosed)) {
                    tracker.setClosed(cellClosed.getLocalDateTimeCellValue());
                }

                // SERVICE
                tracker.setService(formatter.formatCellValue(row.getCell(11)));

                // REOPEN COUNT
                tracker.setReopenCount(getIntCellValue(row.getCell(13)));

                trackerList.add(tracker);
            }

            repository.saveAll(trackerList);

            return trackerList.size() + " incidents importés / mis à jour";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur import: " + e.getMessage();
        }
    } // FIN IMPORT EXCEL INCIDENTS


    //-----------------------------

     // IMPORT EXCEL TASKS
   @PostMapping("/import-excel-sctasks")
    public String importExcelTasks(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inp)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            List<tracker> trackerList = new ArrayList<>();

            String typeItem = "";

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                String shortDescription = formatter.formatCellValue(row.getCell(5));
                if(shortDescription.contains("APPL")){
                    typeItem = "packaging"; // Skip this row
                } else {
                    typeItem = "request";
                }

                // NUMBER (clé unique) : sctask ou ritm
                String number = "";
                if(typeItem == "request") {
                    number = formatter.formatCellValue(row.getCell(0));
                } else if(typeItem == "packaging") {
                    number = formatter.formatCellValue(row.getCell(1));
                }
                if (number == null || number.isEmpty()) continue;


                // 🔥 UPSERT
                tracker tracker = repository.findByNumber(number)
                        .orElse(new tracker());

                tracker.setNumber(number);

                // OPENED
                Cell cellOpened = row.getCell(2);
                if (cellOpened != null && cellOpened.getCellType() == CellType.NUMERIC) {
                    tracker.setOpened(cellOpened.getLocalDateTimeCellValue());
                }

                // ASSIGNED TO
                tracker.setAssignedTo(formatter.formatCellValue(row.getCell(3)));

                // STATE
                tracker.setState(formatter.formatCellValue(row.getCell(4)));

                //short desc -> SERVICE
                String result = "";
                String text = formatter.formatCellValue(row.getCell(5));

                if(text == null) {
                        result = text; // Fallback to the whole text if format is unexpected
                } else {
                    if(text.contains("Functional support request")) {
                        result = "Visual Studio ::C2A";
                    } else {
                        if (text.contains("for") && text.contains("(")) {
                            result = text.substring(
                                text.indexOf("for") + 4,
                                text.indexOf("(")
                            ).trim();
                        } else {
                            if (text.contains("APPL")) {
                                var start = text.indexOf(" ");
                                var end = text.indexOf("(");

                                if (start != -1 && end != -1) {
                                    result = text.substring(start + 1, end).trim();
                                }
                            } else {
                                result = text; // Fallback to the whole text if format is unexpected
                            }
                        }
                    }
                
                }
                tracker.setService(result);


                // ASSIGNMENT GROUP
                tracker.setAssignmentGroup(formatter.formatCellValue(row.getCell(6)));

                // REQUESTED FOR
                tracker.setRequestedFor(formatter.formatCellValue(row.getCell(8)));

                // CLOSED
                Cell cellClosed = row.getCell(9);
                if (cellClosed != null && DateUtil.isCellDateFormatted(cellClosed)) {
                    tracker.setResolved(cellClosed.getLocalDateTimeCellValue());
                    tracker.setClosed(cellClosed.getLocalDateTimeCellValue());
                }

                trackerList.add(tracker);
            }

            repository.saveAll(trackerList);

            return trackerList.size() + " stasks importés / mis à jour";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur import: " + e.getMessage();
        }
    }

    //-----------------------------

    // IMPORT EXCEL KBS
    @PostMapping("/import-excel-kbs")
    public String importExcelKbs(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inp)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            List<tracker> trackerList = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                // NUMBER (clé unique)
                String number = formatter.formatCellValue(row.getCell(0));
                if (number == null || number.isEmpty()) continue;

                // 🔥 UPSERT
                tracker tracker = repository.findByNumber(number)
                        .orElse(new tracker());

                tracker.setNumber(number);
                
                // OPENED
                Cell cellOpened = row.getCell(2);
                if (cellOpened != null && cellOpened.getCellType() == CellType.NUMERIC) {
                    tracker.setOpened(cellOpened.getLocalDateTimeCellValue());
                }
                
                // ASSIGNED TO
                tracker.setAssignedTo(formatter.formatCellValue(row.getCell(3)));

                // STATE
                tracker.setState(formatter.formatCellValue(row.getCell(5)));

                // SERVICE
                tracker.setService(formatter.formatCellValue(row.getCell(4)));

                // RESOLVED and CLOSED
                Cell cellPublished = row.getCell(8);
                System.out.println("Cell Published: " + cellPublished);
                if (cellPublished != null && DateUtil.isCellDateFormatted(cellPublished)) {
                    System.out.println("Cell Published is a date: " + cellPublished.getLocalDateTimeCellValue());
                  tracker.setResolved(cellPublished.getLocalDateTimeCellValue());
                  tracker.setClosed(cellPublished.getLocalDateTimeCellValue());

                }

                trackerList.add(tracker);
            }

            repository.saveAll(trackerList);

            return trackerList.size() + " Kbs importés / mis à jour";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur import: " + e.getMessage();
        }
    }

    //-----------------------------
/*
    // IMPORT EXCEL PACKAGES
    @PostMapping("/import-excel-Packages")
    public String importExcelPackages(@RequestParam("file") MultipartFile file) {

        try (InputStream inp = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inp)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            List<tracker> trackerList = new ArrayList<>();

            for (Row row : sheet) {

                if (row.getRowNum() == 0) continue;

                // NUMBER (clé unique)
                String number = formatter.formatCellValue(row.getCell(0));
                if (number == null || number.isEmpty()) continue;

                // 🔥 UPSERT
                tracker tracker = repository.findByNumber(number)
                        .orElse(new tracker());

                tracker.setNumber(number);
                
                // OPENED
                Cell cellOpened = row.getCell(2);
                if (cellOpened != null && cellOpened.getCellType() == CellType.NUMERIC) {
                    tracker.setOpened(cellOpened.getLocalDateTimeCellValue());
                }
                
                // ASSIGNED TO
                tracker.setAssignedTo(formatter.formatCellValue(row.getCell(3)));

                // STATE
                tracker.setState(formatter.formatCellValue(row.getCell(5)));

                // SERVICE
                tracker.setService(formatter.formatCellValue(row.getCell(4)));

                // RESOLVED and CLOSED
                Cell cellPublished = row.getCell(8);
                System.out.println("Cell Published: " + cellPublished);
                if (cellPublished != null && DateUtil.isCellDateFormatted(cellPublished)) {
                    System.out.println("Cell Published is a date: " + cellPublished.getLocalDateTimeCellValue());
                  tracker.setResolved(cellPublished.getLocalDateTimeCellValue());
                  tracker.setClosed(cellPublished.getLocalDateTimeCellValue());

                }

                trackerList.add(tracker);
            }

            repository.saveAll(trackerList);

            return trackerList.size() + " Kbs importés / mis à jour";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur import: " + e.getMessage();
        }
    }
        */
}