package com.personal.finance_tracker_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvService {
	
	private final DatabaseService databaseService;
    
    @Value("${csv.unprocessed.folderpath}")
    private String FOLDER_PATH;

    @Value("${csv.processed.folderpath}")
    private String PROCESSED_FOLDER_PATH;

    @Value("${csv.partiallyProcessed.folderpath}")
    private String PARTIALLY_PROCESSED_FOLDER_PATH;
    
    @PostConstruct
    public void init() {
        new Thread(this::startWatching).start(); // Start the watcher in a separate thread
    }
    
    public void startWatching() {
    	Path folderPath = Paths.get(FOLDER_PATH);
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            folderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            log.info("Monitoring folder for new files: " + folderPath);
            while (true) {
                WatchKey key = watchService.take(); // Blocks until an event is available

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path filePath = folderPath.resolve((Path) event.context());
                        log.info("New file detected: " + filePath);
                        runJob();
                    }
                }
                // Reset the key and exit if directory is no longer accessible
                if (!key.reset()) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error watching folder: " + e.getMessage());
        }
    }
    
    public void runJob() {
        File folder = new File(FOLDER_PATH);
        if (!folder.exists() || !folder.isDirectory()) {
            log.error("Invalid folder path or folder does not exist");
            return;
        }

        // Get all CSV files in the folder
        File[] csvFiles = folder.listFiles((dir, name) -> name.endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
        	log.info("No CSV files found in the unprocessed folder");
            return;
        }

        // Process each CSV file
        for (File csvFile : csvFiles) {
        	String fileName = csvFile.getName();
            log.info("Processing file: " + fileName);
            try {
            	if (fileName.startsWith("stmt")) {
            		processBofaCsvFile(csvFile);
            	} else if (fileName.startsWith("Discover"))
            		processDiscoverCsvFile(csvFile);
            	else if (fileName.startsWith("activity"))
            		processAmexCsvFile(csvFile);
            	else if (fileName.startsWith("SOFI"))
            		processSofiCsvFile(csvFile);
                // Move to processed folder if processing was successful
                moveFileToFolder(csvFile, PROCESSED_FOLDER_PATH);
            } catch (Exception e) {
                System.err.println("Error processing file: " + csvFile.getName());
                e.printStackTrace();
                // Move to partially processed folder in case of failure
                moveFileToFolder(csvFile, PARTIALLY_PROCESSED_FOLDER_PATH);
            }
        }
    }
    
    private void processBofaCsvFile(File csvFile) {
    	try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
        	String[] nextLine;
        	// Skip first few lines
    		while ((nextLine = reader.readNext()) != null) {
                boolean isBlankLine = nextLine.length == 0 || nextLine[0].trim().isEmpty();
                if (isBlankLine) {
                    break; // Stop skipping when a blank line is found
                }
            }
        	
        	// Skip header and first line with beginning balance
            reader.readNext();
        	reader.readNext();
        	
            while ((nextLine = reader.readNext()) != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate localDate = LocalDate.parse(nextLine[0], formatter);
                if (nextLine[2].contains("-"))
	                databaseService.createExpense(
	                		Date.valueOf(localDate),
	                		nextLine[1],
	                		Double.valueOf(nextLine[2].replace("-","").replace(",", "")));
                else
                	databaseService.createIncome(
	                		Date.valueOf(localDate),
	                		nextLine[1],
	                		Double.valueOf(nextLine[2].replace(",", "")));
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + csvFile.getName());
            e.printStackTrace();
        }
    }

    private void processDiscoverCsvFile(File csvFile) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
        	String[] nextLine;
        	// Skip header
            reader.readNext();
        	
            while ((nextLine = reader.readNext()) != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate localDate = LocalDate.parse(nextLine[0], formatter);
                databaseService.createExpense(Date.valueOf(localDate), nextLine[2], Double.valueOf(nextLine[3]));
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + csvFile.getName());
            e.printStackTrace();
        } catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void processAmexCsvFile(File csvFile) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
        	String[] nextLine;
        	// Skip header
            reader.readNext();
        	
            while ((nextLine = reader.readNext()) != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate localDate = LocalDate.parse(nextLine[0], formatter);
                databaseService.createExpense(Date.valueOf(localDate), nextLine[1], Double.valueOf(nextLine[2]));
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + csvFile.getName());
            e.printStackTrace();
        } catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void processSofiCsvFile(File csvFile) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
        	String[] nextLine;
        	// Skip header
            reader.readNext();
        	
            while ((nextLine = reader.readNext()) != null) {
            	Double amount = Double.valueOf(nextLine[3]);
                if (amount < 0)
                	databaseService.createExpense(Date.valueOf(nextLine[0]), nextLine[1], Math.abs(amount));
                else
                	databaseService.createIncome(Date.valueOf(nextLine[0]), nextLine[1], amount);
                // Process the data here (for example, save to database, etc.)
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + csvFile.getName());
            e.printStackTrace();
        } catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void moveFileToFolder(File file, String folderPath) {
        File destinationFolder = new File(folderPath);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir(); // Create folder if it doesn't exist
        }

        File destinationFile = new File(destinationFolder, file.getName());
        boolean success = file.renameTo(destinationFile);
        if (success) {
            System.out.println("File moved to: " + destinationFolder.getPath()+ file.getName());
        } else {
            System.err.println("Failed to move file: " + file.getName());
        }
    }

    @Scheduled(cron = "${cron.expression.deleteCSV}")  
    public void clearProcessedFolder() {
        System.out.println("Clearing processed folder...");

        File processedFolder = new File(PROCESSED_FOLDER_PATH);
        if (!processedFolder.exists() || !processedFolder.isDirectory()) {
            System.out.println("Processed folder does not exist or is not a directory.");
            return;
        }

        // Get all files in the processed folder
        File[] processedFiles = processedFolder.listFiles();
        if (processedFiles != null) {
            for (File file : processedFiles) {
                if (file.isFile()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        System.out.println("Deleted file: " + file.getName());
                    } else {
                        System.err.println("Failed to delete file: " + file.getName());
                    }
                }
            }
        } else {
            System.out.println("No files to delete in the processed folder.");
        }
    }
}
