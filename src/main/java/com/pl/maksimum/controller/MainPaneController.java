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
    File plik6;
    File plik7;
    File yesFile;
    File noFile;
    File aaFile;
    File reg;
    File AAContractPlk;
    File AAContractAbo;
    File AAAttachment;

    // Główna funkcja do złączania plików w całośc.
    public void merge() {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        try {
            // Zmienne/funckje przygotowawcze
            reg = bottomPaneController.getPlik();
            aaFile = fileAAController.getPlik();
            createOuptuAA(aaFile);
//            insertSignature(plik2, 2,360, 640, 230, 80);

            // Scalanie plików
            pdfMerger.addSource(plik1);						// Check Lista
            pdfMerger.addSource(AAContractPlk);				// Strona X pliku AA Umowa
            pdfMerger.addSource(plik2);						// Regulaminy + podpis
            pdfMerger.addSource(plik3);						// Załącznik do umowy numer 3
            pdfMerger.addSource(AAAttachment);    			// Strona X pliku AA Umowa śut
            pdfMerger.addSource(plik4);						// Załącznik do umowy numer 5
            if (choiceOfConsentController.isSelectedYes) {  // Oświadczenie Tak/Nie
                pdfMerger.addSource(yesFile);
            } else {
                pdfMerger.addSource(noFile);
            }
            pdfMerger.addSource(plik5);						// Pakiet + oświadzcenie
            pdfMerger.addSource(AAContractAbo); 			// Strona X pliku AA dla klienta
            pdfMerger.addSource(plik2);						// Regulaminy + podpis
            pdfMerger.addSource(plik3);						// Załącznik do umowy numer 3
            pdfMerger.addSource(AAAttachment);  			// Strona X pliku AA Umowa śut
            pdfMerger.addSource(reg);						// Regulamin promocji z dysku
            pdfMerger.addSource(plik6);						// Cenniki
            pdfMerger.addSource(plik4);						// Załącznik do umowy numer 5
            if (choiceOfConsentController.isSelectedYes) {  // Oświadczenie Tak/Nie
                pdfMerger.addSource(yesFile);
            } else {
                pdfMerger.addSource(noFile);
            }
            pdfMerger.addSource(plik7);						// Odstąpienie od umowy
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
        AAContractPlk = file;
        AAContractAbo = file;
        AAAttachment = file;

        try {
            // 1 strona Umowa dla PLK
            PDDocument docPlk = PDDocument.load(AAContractPlk);
            for (int i = docPlk.getNumberOfPages() - 1; i >= 0; i--) {
                if (i == 0) {
                    continue;
                }
                docPlk.removePage(i);
            }
            docPlk.save(currentDir + "\\tmp\\1.pdf");
            docPlk.close();
            AAContractPlk = new File(currentDir + "\\tmp\\1.pdf");

            // 5 strona Umowa dla abonenta
            PDDocument docAbo = PDDocument.load(AAContractAbo);
            for (int i = docAbo.getNumberOfPages() - 1; i >= 0; i--) {
                if (i == 4) {
                    continue;
                }
                docAbo.removePage(i);
            }
            docAbo.save(currentDir + "\\tmp\\2.pdf");
            docAbo.close();
            AAContractAbo = new File(currentDir + "\\tmp\\2.pdf");

            // 7 Załącznik + podpis
            PDDocument docAtt = PDDocument.load(AAAttachment);
            for (int i = docAtt.getNumberOfPages() - 1; i >= 0; i--) {
                if (i == 6) {
                    continue;
                }
                docAtt.removePage(i);
            }
            docAtt.save(currentDir + "\\tmp\\3.pdf");
            docAtt.close();
            AAAttachment = new File(currentDir + "\\tmp\\3.pdf");
            insertSignature(AAAttachment, 0,320, 60, 260, 80);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("..............................................");
            System.err.println("Nastąpił wyjątek przy wyodrębnianiu plików AA.");
            System.err.println("..............................................");
        }
    }

    // Wstawianie podpisu do pliku
    public void insertSignature(File file, int pageIndex, int x, int y, int width, int height) {
        try {
            PDDocument doc = PDDocument.load(file);
            PDPage page = doc.getPage(pageIndex);
            PDImageXObject pdImage = PDImageXObject.createFromFile(currentDir + "\\sign\\stamp1.gif", doc);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

            //ELA
            contentStream.drawImage(pdImage, x, y, width, height);
            contentStream.close();
            doc.save(file);
            doc.close();
        } catch (Exception e) {
            Alerts.alertError("Error", null, "Error, insert signature file encounter an error. \n" + e.toString());

            System.err.println(".....................................................");
            System.err.println("Nastąpił wyjątek przy wstawianiu podpisu do pliku.");
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

        try {
            temp1.delete();
            temp2.delete();
            temp3.delete();
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
        plik1 = new File(currentDir + "\\doc\\1.CheckList.pdf");
        plik2 = new File(currentDir + "\\doc\\2.Reg.pdf");
        plik3 = new File(currentDir + "\\doc\\3.Att3.pdf");
        plik4 = new File(currentDir + "\\doc\\4.Att5.pdf");
        plik5 = new File(currentDir + "\\doc\\5.Package.pdf");
        plik6 = new File(currentDir + "\\doc\\6.PriceList.pdf");
        plik7 = new File(currentDir + "\\doc\\7.Withdrawal.pdf");
        yesFile = new File(currentDir + "\\doc\\osw_tak.pdf");
        noFile = new File(currentDir + "\\doc\\osw_nie.pdf");

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
