package com.example.alberto.facecook.PDF;

import android.content.Context;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GenerarPdfReceta {

    /* Atributos */
    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitulo = new Font(Font.FontFamily.HELVETICA, 45, Font.BOLD);
    private Font fCategoria = new Font(Font.FontFamily.HELVETICA, 35, Font.ITALIC);
    private Font fSubtitulo = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
    private Font fNormal = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);


    /**
     * Constructor de clase
     *
     * @param context :Context
     */
    public GenerarPdfReceta(Context context){
        this.context = context;
    }

    /**
     * Crea la carpeta contendora y el archivo pdf en el cual se insertarán los datos
     */
    private void createFile(){
        File directorio = new File(this.context.getFilesDir(), "pdf");
        if (!directorio.exists()){
            directorio.mkdirs();
        }
        this.pdfFile = new File(directorio, String.valueOf(System.currentTimeMillis()/100) + ".pdf");
    }

    /**
     * Abre el documento como escritura para poder modificarlo
     */
    public void openDocument(){
        this.createFile();
        try {
            this.document = new Document();
            this.pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(this.pdfFile));
            this.document.addCreationDate();
            this.document.addProducer();
            this.document.setPageSize(PageSize.A4);
            this.document.open();
        }catch (Exception e){
            Log.e("openDocument", e.toString());
        }
    }

    /**
     * Cierra el documento
     */
    public void closeDocument(){
        this.document.close();
        this.pdfWriter.close();
    }

    /**
     * Añade los metadatos del PDF
     *
     * @param titulo :String
     * @param tema :String
     * @param autor :String
     */
    public void addMetaData(String titulo, String tema, String autor){
        this.document.addTitle(titulo);
        this.document.addSubject(tema);
        this.document.addAuthor(autor);
    }

    /**
     * Añade una cabecera al PDF
     *
     * @param titulo :String
     */
    public void addTitulo(String titulo){
        try {
            this.paragraph = new Paragraph();
            this.addParagraphTitulo(new Paragraph(titulo, fTitulo));
            this.paragraph.setSpacingAfter(40);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addTtitulo", e.toString());
        }
    }

    /**
     * Crea la cabecera
     *
     * @param paragraphHijo
     */
    private void addParagraphTitulo(Paragraph paragraphHijo){
        /* Agrega dentro de paragraph otro para guardar varios como uno solo */
        paragraphHijo.setAlignment(Element.ALIGN_CENTER);
        this.paragraph.add(paragraphHijo);
    }

    /**
     * Inserta paragraph normales
     *
     * @param paragraphHijo
     */
    private void addParagraph(Paragraph paragraphHijo){
        paragraphHijo.setAlignment(Element.ALIGN_LEFT);
        this.paragraph.add(paragraphHijo);
    }

    /**
     * Añade los ingredientes al documento
     *
     * @param ingredientes :ArrayList<String>
     */
    public void addIngredientes(ArrayList<String> ingredientes){
        try {
            /* Se añade el titulo de la viñeta */
            this.paragraph = new Paragraph();
            this.addParagraph(new Paragraph("Ingredientes", fSubtitulo));
            this.document.add(this.paragraph);
            /* Se añade la viñeta */
            this.addListado(ingredientes);
        }catch (Exception e){
            Log.e("addIngredientes", e.toString());
        }
    }

    /**
     * Añade una viñeta al documento
     *
     * @param items :ArrayList<String>
     */
    private void addListado(ArrayList<String> items){
        try {
            ZapfDingbatsList listado = new ZapfDingbatsList(227); //El simbolo de la viñeta
            listado.setIndentationLeft(30); //La tabulación
            for (int i = 0; i < items.size(); i++){
                listado.add(new ListItem(items.get(i), fNormal));
            }
            this.document.add(listado);
        }catch (Exception e){
            Log.e("addListado", e.toString());
        }
    }

    /**
     * Añade la descripción al documento
     *
     * @param descrip :String
     */
    public void addDescripcion(String descrip){
        try {
            this.paragraph = new Paragraph();
            this.addParagraph(new Paragraph("Descripción", fSubtitulo));
            this.addParagraph(new Paragraph(descrip, fNormal));
            this.paragraph.setSpacingBefore(40);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addDescripcion", e.toString());
        }
    }

    /**
     * Devuelve la ruta absoluta del pdf
     *
     * @return :String
     */
    public String verPathPdf(){
        return this.pdfFile.getAbsolutePath();
    }
}
