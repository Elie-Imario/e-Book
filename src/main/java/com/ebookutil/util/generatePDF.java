package com.ebookutil.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.awt.*;
import java.util.Optional;



public class generatePDF {
    //Variable locale
    private String nom;
    private OutputStream file = null;
    private Document document = null;
    private PdfPTable table;
    private int nombreColonne;
    private String chemin;

    public generatePDF(String nomFichier, int nbCol) throws FileNotFoundException, DocumentException {
        init(nomFichier, nbCol);
    }

    //Méthode locale
    public final void init(String path, int nbCol) {
        try {
            nom = path + "";
            String dest = System.getProperty("user.home") + "\\Desktop\\" + path + ".pdf";
            file = new FileOutputStream(new File(dest));
            document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            this.nombreColonne = nbCol;
            table = new PdfPTable(nbCol);

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addTitle(String title) throws DocumentException {
        Paragraph p = new Paragraph(title, new Font(Font.FontFamily.TIMES_ROMAN, 22));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        this.addParag(" ");
    }


    public void addParag(String parag) throws DocumentException {
        document.add(new Paragraph(parag));
    }

    public void addHeaderRow(ObservableList<String> row) {
        try {
            PdfPCell pc = new PdfPCell();
            pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            pc.setBackgroundColor(BaseColor.CYAN);

            for (int aw = 0, nbColmns = row.size(); aw < nbColmns; aw++) {
                //Lignes titres
                pc.setPhrase(new Phrase(row.get(aw), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
                table.addCell(pc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRow(ObservableList<String> row) {
        try {
            PdfPCell pc = new PdfPCell();
            pc.setHorizontalAlignment(Element.ALIGN_LEFT);

            for (int aw = 0, nbColmns = row.size(); aw < nbColmns; aw++) {
                //Lignes simples
                pc.setPhrase(new Phrase(row.get(aw), new Font(Font.FontFamily.HELVETICA, 12)));
                table.addCell(pc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTable() throws DocumentException {
        document.add(table);
    }

    public void generate() throws DocumentException {
        document.close();
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (Desktop.isDesktopSupported()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ouverture");
                alert.setContentText("Vous-voulez ouvrir le fichier conçu?\n"+System.getProperty("user.home")+"\\Desktop\\"+nom+".pdf"+">");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    File f;
                    f = new File(System.getProperty("user.home") + "\\Desktop\\" + nom + ".pdf");
                    Desktop.getDesktop().open(f);
                }
            } else {
                Alert alert_1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert_1.setTitle("Fichier crée");
                alert_1.setContentText("Le fichier conçu est:\n<" + System.getProperty("user.home") + "\\Desktop\\" + nom + ".pdf" + ">");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
