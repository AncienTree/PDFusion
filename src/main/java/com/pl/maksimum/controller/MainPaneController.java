package com.pl.maksimum.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import com.pl.maksimum.util.Alerts;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainPaneController implements Initializable {

    // JavaFx kontrolery
    @FXML
    private BottomPaneController bottomPaneController;
    @FXML
    private ChoiceOfConsentController choiceOfConsentController;
    @FXML
    private FileAAController fileAAController;
    @FXML
    private MenuController menuController;
    @FXML
    private SettingsController settingsController;

    // Zmienne
    String currentDir = new File("").getAbsolutePath();
    private String dirFile;
    private String fileName;
    private String filePath;
    private long fileSize;

    File plik1;
    File plik2;
    File plik3;
    File plik4;
    File plik5;
    File yesFile;
    File noFile;
    File aaFile;
    File reg;
    File pgsAA5;
    File pgsAA6;
    File pgsAA7;

    // Główna funkcja do złączania plików w całośc.
    public void merge() {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        try {
            // Zmienne/funckje przygotowawcze
            reg = bottomPaneController.getPlik();
            aaFile = fileAAController.getPlik();
            createOuptuAA(aaFile);

            // Scalanie plików
            pdfMerger.addSource(plik1);						// Strona startowa
            pdfMerger.addSource(pgsAA5);					// Strona 5 pliku AA
            pdfMerger.addSource(plik2);						// Regulaminy
//            pdfMerger.addSource(pgsAA6);					// Strona 6 pliku AA
            pdfMerger.addSource(pgsAA7);    				// Strona 7 pliku AA
            pdfMerger.addSource(plik3);						// Załącznik od umowy
            if (choiceOfConsentController.isSelectedYes) {                      // Oświadczenie Tak/Nie
                pdfMerger.addSource(yesFile);
            } else {
                pdfMerger.addSource(noFile);
            }
            pdfMerger.addSource(plik4);						// Odstąpienie + regulamin konwersji
            pdfMerger.addSource(reg);						// Regulamin z dysku
            pdfMerger.addSource(plik5);						// Cenniki
            pdfMerger.setDestinationFileName(getDirFile());
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

            // Zakończenie i sprzątanie
            Alerts.alertInfo("Utworzono plik.", null, "Utworzono plik o nazwie " + getFileMergedName() + "\nPlik zapisano: " + getFileMergedPath());
            alertFileSize();
            deleteTmpFile();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("File no fund");
            alert.setHeaderText(null);
            alert.setContentText("Java FileNotFoundException, nie znaleziono plików domyślnych!\n\n" + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("...................................................");
            System.err.println("Nastąpił wyjątek przy wczytywaniu pliku domyślnych.");
            System.err.println("...................................................");
        }
    }

    // Alert z podaniem rozmiaru pliku.
    public void alertFileSize() {
        try {
            FileChannel fileChannel;
            Path filePath = Paths.get(dirFile);
            fileChannel = FileChannel.open(filePath);
            fileSize = fileChannel.size();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Stworzono plik.");
            alert.setHeaderText(null);
            alert.setContentText("Plik ma rozmiar " + getFileMergedSize());
            alert.showAndWait();

            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("...................................................................");
            System.err.println("Nastąpił wyjątek przy podawaniu danych o rozmiarze pliku w alercie.");
            System.err.println("...................................................................");
        }
    }

    // Wyodrębnienie stron z pliku AA
    public void createOuptuAA(File file) {
        pgsAA5 = file;
        pgsAA6 = file;
        pgsAA7 = file;

        try {
            // 5 strona
            PDDocument doc5 = PDDocument.load(pgsAA5);
            for (int i = doc5.getNumberOfPages() - 1; i >= 0; i--) {
                if (i == 4) {
                    continue;
                }
                doc5.removePage(i);
            }
            doc5.save(currentDir + "\\tmp\\1.pdf");
            doc5.close();
            pgsAA5 = new File(currentDir + "\\tmp\\1.pdf");

            // 6 strona
            PDDocument doc6 = PDDocument.load(pgsAA6);
            for (int i = doc6.getNumberOfPages() - 1; i >= 0; i--) {
                if (i == 5) {
                    continue;
                }
                doc6.removePage(i);
            }
            doc6.save(currentDir + "\\tmp\\2.pdf");
            doc6.close();
            pgsAA6 = new File(currentDir + "\\tmp\\2.pdf");

            // 7 strona + podpis
            PDDocument doc7 = PDDocument.load(pgsAA7);
            for (int i = doc7.getNumberOfPages() - 1; i >= 0; i--) {
                if (i == 6) {
                    continue;
                }
                doc7.removePage(i);
            }
            doc7.save(currentDir + "\\tmp\\3.pdf");
            doc7.close();
            pgsAA7 = new File(currentDir + "\\tmp\\3.pdf");
            insertSignatureAA(pgsAA7);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("..............................................");
            System.err.println("Nastąpił wyjątek przy wyodrębnianiu plików AA.");
            System.err.println("..............................................");
        }
    }

    // Wstawianie podpisu do pliku AA
    public void insertSignatureAA(File file) {
        try {
            PDDocument doc = PDDocument.load(file);
            PDPage page = doc.getPage(0);
            PDImageXObject pdImage = PDImageXObject.createFromFile(currentDir + "\\sign\\stamp1.gif", doc);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

            //ELA
            contentStream.drawImage(pdImage, 320, 60, 260, 80);
            contentStream.close();
            doc.save(file);
            doc.close();
        } catch (Exception e) {
            Alerts.alertError("Error", null, "Error, insert signature file AA encountered an error.");

            System.err.println(".....................................................");
            System.err.println("Nastąpił wyjątek przy wstawianiu podpisu do pliku AA.");
            System.err.println(".....................................................");
            e.printStackTrace();
        }
    }

    // informacje o stworzonym pliku
    public void setFileMergedSize(File fileMerged) {
        fileSize = fileMerged.length();
    }

    public void setFileMergedName(File fileMerged) {
        fileName = fileMerged.getName();
    }

    public void setFileMergedPath(File fileMerged) {
        filePath = fileMerged.getPath();
    }

    public String getFileMergedSize() {
        return Size(fileSize);
    }

    public String getFileMergedName() {
        return fileName;
    }

    public String getFileMergedPath() {
        return filePath;
    }

    public void fileInfo(File file) {
        setFileMergedName(file);
        setFileMergedPath(file);
        setFileMergedSize(file);
    }

    // obliczanie rozmiaru pliku
    public String Size(long size) {
        String hrSize = "";
        long b = size;
        float k = size / 1024F;
        float m = size / 1048576F;

        DecimalFormat dec = new DecimalFormat("0");

        if (b > 0) {

            hrSize = dec.format(b).concat("B");
        }
        if (k > 0) {

            hrSize = dec.format(k).concat("," + (size % 1024F) + "kB");
        }
        if (m > 0) {

            hrSize = dec.format(m).concat("," + (size % 1048576F / 1000F) + "MB");
        }

        return hrSize;
    }

    public void dir(File f) {
        setDirFile(f.getPath());
        fileInfo(f);
        merge();
    }

    public String getDirFile() {
        return dirFile;
    }

    public void setDirFile(String dirFile) {
        this.dirFile = dirFile;
    }

    public void createAgreement() {
        FileChooser save = new FileChooser();
        save.setTitle("Wybierz ścieżkę zapisu");
        save.getExtensionFilters().addAll(new ExtensionFilter("PDF plik", "*.pdf"));
        save.setInitialFileName(bottomPaneController.date());
        File newer = save.showSaveDialog(new Stage());
        dir(newer);
    }

    public void deleteTmpFile() {
        // 1 strona
        File temp1 = new File(currentDir + "\\tmp\\1.pdf");
        File temp2 = new File(currentDir + "\\tmp\\2.pdf");
        File temp3 = new File(currentDir + "\\tmp\\3.pdf");
        File temp4 = new File(currentDir + "\\tmp\\4.pdf");
        File temp5 = new File(currentDir + "\\tmp\\5.pdf");

        try {
            temp1.delete();
            temp2.delete();
            temp3.delete();
            temp4.delete();
            temp5.delete();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("..................................");
            System.err.println("Nie można usunąć plików temporary.");
            System.err.println("..................................");
        }
    }

    ////////////////////////////////////////////////////////////////
    //                                                            //
    //                       initialize                           //
    //                                                            //
    ////////////////////////////////////////////////////////////////
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        plik1 = new File(currentDir + "\\doc\\1.first.pdf");
        plik2 = new File(currentDir + "\\doc\\2.regulamin.pdf");
        plik3 = new File(currentDir + "\\doc\\3.attachment.pdf");
        plik4 = new File(currentDir + "\\doc\\5.ods_prom.pdf");
        plik5 = new File(currentDir + "\\doc\\6.pricelist.pdf");
        yesFile = new File(currentDir + "\\doc\\4.ostak.pdf");
        noFile = new File(currentDir + "\\doc\\4.osnie.pdf");

        bottomPaneController.getCreateButton().setOnAction(event -> {
            // Protection statement
            if (choiceOfConsentController.getTakZgodaCheckBox().isSelected()
                    || choiceOfConsentController.getNieZgodaCheckBox().isSelected()) {
                createAgreement();
            } else {
                Alerts.alertError("Nie wybrano opcji.", null, "Proszę zaznaczyć jedną z opcji oświadczenie zgody!");
            }
        });

    }
}
