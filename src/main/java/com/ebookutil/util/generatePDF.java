package com.ebookutil.util;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
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
        try {
            init(nomFichier, nbCol);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Méthode locale
    public final void init(String path, int nbCol){
        try {
            nom = path + "";
            String dest = System.getProperty("user.home") + "\\Downloads\\" + path + ".pdf";
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

    public void addAppIco() throws BadElementException, MalformedURLException, DocumentException, IOException{
        PdfPCell pdfPLeftCell = new PdfPCell();
        Image image = Image.getInstance("D:\\Demo-Projet\\e-Book\\src\\main\\resources\\com\\ebookutil\\ebook\\img\\eBookIco.png");
        image.scaleAbsoluteWidth(25F);
        image.scaleAbsoluteHeight(25F);
        image.setAlignment(Element.ALIGN_RIGHT);
        pdfPLeftCell.addElement(image);
        pdfPLeftCell.setPadding(5);
        pdfPLeftCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell pdfPRightCell = new PdfPCell();
        BaseFont baseFont = BaseFont.createFont("D:\\Demo-Projet\\e-Book\\src\\main\\resources\\com\\ebookutil\\ebook\\fonts\\BRUSHIA.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 15);
        font.setColor(new BaseColor(6, 53, 66));
        Paragraph p = new Paragraph("E-Ndrana", font);
        p.setAlignment(Element.ALIGN_LEFT);
        pdfPRightCell.addElement(p);
        pdfPRightCell.setBorder(Rectangle.NO_BORDER);


        PdfPTable tableHeader = new PdfPTable(2);
        tableHeader.addCell(pdfPLeftCell);
        tableHeader.addCell(pdfPRightCell);
        tableHeader.setWidthPercentage(35);
        tableHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
        document.add(tableHeader);
        this.addParag(" ");
    }

    public void addTitle(String title) throws DocumentException, IOException{
        BaseFont baseFont = BaseFont.createFont("D:\\Demo-Projet\\e-Book\\src\\main\\resources\\com\\ebookutil\\ebook\\fonts\\FREEZONE.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 10);
        Paragraph p = new Paragraph(title, font);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        this.addParag(" ");
    }

    public void addTableDescription(String tabDescription) throws DocumentException, IOException{
        BaseFont baseFont = BaseFont.createFont("D:\\Demo-Projet\\e-Book\\src\\main\\resources\\com\\ebookutil\\ebook\\fonts\\Roboto-regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 8);
        Paragraph p = new Paragraph(tabDescription, font);
        p.setAlignment(Element.ALIGN_LEFT);
        this.addParag(" ");
        document.add(p);
        this.addParag(" ");
    }


    public void addParag(String parag) throws DocumentException {
        document.add(new Paragraph(parag));
    }

    public void addHeaderRow(ObservableList<String> row) {
        try {
            PdfPCell pc = new PdfPCell();
            pc.setBorderColor(new BaseColor(149, 149, 149));
            pc.setBorderWidth(1.15F);
            pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pc.setPadding(10);
            pc.setBackgroundColor(new BaseColor(9, 63, 118));
            Font font = new Font(Font.FontFamily.UNDEFINED, 9);
            font.setColor(BaseColor.WHITE);
            this.addParag(" ");
            for (int aw = 0, nbColmns = row.size(); aw < nbColmns; aw++) {
                //Lignes titres
                pc.setPhrase(new Phrase(row.get(aw), font));
                table.addCell(pc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRow(ObservableList<String> row) {
        try {
            PdfPCell pc = new PdfPCell();
            pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            pc.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pc.setBackgroundColor(new BaseColor(242, 242, 242));
            pc.setBorderColor(new BaseColor(190, 195, 198));
            pc.setBorderWidth(0.75F);
            pc.setPadding(5);
            BaseFont baseFont = BaseFont.createFont("D:\\Demo-Projet\\e-Book\\src\\main\\resources\\com\\ebookutil\\ebook\\fonts\\Poppins-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 8);

            for (int aw = 0, nbColmns = row.size(); aw < nbColmns; aw++) {
                //Lignes simples
                pc.setPhrase(new Phrase(row.get(aw), font));
                table.addCell(pc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTable() throws DocumentException {
        table.setWidthPercentage(100);
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
                alert.setContentText("Vous-voulez ouvrir le fichier conçu?\n"+System.getProperty("user.home")+"\\Downloads\\"+nom+".pdf"+">");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    File f;
                    f = new File(System.getProperty("user.home") + "\\Downloads\\" + nom + ".pdf");
                    Desktop.getDesktop().open(f);
                }
            } else {
                Alert alert_1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert_1.setTitle("Fichier crée");
                alert_1.setContentText("Le fichier conçu est:\n<" + System.getProperty("user.home") + "\\Downloads\\" + nom + ".pdf" + ">");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
