package org.django.acquabooks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.django.acquabooks.io.Console;
import org.django.acquabooks.pojos.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;


/**
 * Created by conte on 25/08/15.
 */
public class ExtractionManager {

    /*
    extract all books
     */
    public static final String TYPE_ALL = "T";
    public static final String TYPE_BY_PUBLISHER = "E";
    public static final String TYPE_VENDUTO_BY_PUBLISHER = "VE";

    public static final String COMMAND_PARAMETERS_SEPARATOR = ",";
    public static final String COMMAND_PARAMETERS_ASSIGNATION = ":";
    public static final String JSON_EXTRACTION_TYPE = "json";
    public static final String EXCEL_EXTRACTION_TYPE = "excel";

    public static int CONSOLE_ROW_LENGHT = 80;

    private static final Logger logger = LoggerFactory
            .getLogger(CommandLineLauncher.class);

    private ElasticRESTClient elclient;

    private String commandString;



    public ExtractionManager(ElasticRESTClient elclient, String command) {
        this.elclient = elclient;
        this.commandString = command;
    }

    public ExtractionManager(ElasticRESTClient elclient) {
        this.elclient = elclient;
        this.commandString = ExtractionManager.TYPE_ALL;
    }

    public boolean run(){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        String filename = "";
        List<Libro> r = null;
        File fout = null;
        JsonWriter writer = null;
        try {


            if(commandString.startsWith(TYPE_ALL)){
                r = elclient.getAll();
                filename += TYPE_ALL + "_";
            }else if(commandString.startsWith(TYPE_BY_PUBLISHER)){
                r = elclient.getByPublisher(getPublisher());
                filename += TYPE_BY_PUBLISHER + "_" + getPublisher() + "_";
            }else if(commandString.startsWith(TYPE_VENDUTO_BY_PUBLISHER)){
                r = elclient.getVendutoByPublisher(getPublisher());
                filename += TYPE_VENDUTO_BY_PUBLISHER + "_" + getPublisher() + "_";
            }

            filename += df.format(new Date());

            if(isJsonExtraction()){
                filename += ".json";
                fout =  new File(filename);
                fout.createNewFile();
                writer = new JsonWriter(new FileWriter(fout));
                writer.setIndent("  ");
                Gson g = new GsonBuilder().setPrettyPrinting().create();
                g.toJson(r,new TypeToken<Collection<Libro>>(){}.getType(),writer);
                logger.info("Created file: " + filename);
            } else if(isExcelExtraction()){
                filename += ".xls";
                fout =  new File(filename);
                fout.createNewFile();
                manageExcelExtraction(fout, r);
            }
                Console.genericWarn("Creato file: "+fout.getAbsolutePath());
                } catch (Throwable e) {
                        logger.error("Error on extracting data: " + e.getMessage());
                        e.printStackTrace();
                } finally {
                        if(writer !=null){
                         try {
                            writer.flush();
                            writer.close();
                          } catch(Exception e){
                                  e.printStackTrace();
                          }
                        }
                }
                return true;
        }

        private void manageExcelExtraction(File fout, List<Libro> r) {
          FileOutputStream fileOut = null;
          try{
            HSSFWorkbook wb = new HSSFWorkbook();
            fileOut = new FileOutputStream(fout);
            Sheet sheet = wb.createSheet("Estrazione");
            int rowCount = 0;
           //headers
            Row row = sheet.createRow(rowCount++);
            Cell c = row.createCell(0);
            c.setCellValue("ISBN");
            c = row.createCell(1);
            c.setCellValue("EDITORE");
            c = row.createCell(2);
            c.setCellValue("AUTORE");
            c = row.createCell(3);
            c.setCellValue("TITOLO");
            c = row.createCell(4);
            c.setCellValue("PREZZO");
            c = row.createCell(5);
            c.setCellValue("PERCENTUALE");
            c = row.createCell(6);
            c.setCellValue("Qta stock");
            c = row.createCell(7);
            c.setCellValue("Qta venduta");
            c = row.createCell(8);
            c.setCellValue("TAG");
            c = row.createCell(9);
            c.setCellValue("DA VERSARE");
              for (Libro l: r) {
                  row = sheet.createRow(rowCount++);
                  c = row.createCell(0);
                  c.setCellValue(l.getBarcode());  
                  c = row.createCell(1);
                  c.setCellValue(l.getEditore());
                  c = row.createCell(2);
                  c.setCellValue(l.getAutore());
                  c = row.createCell(3);
                  c.setCellValue(l.getTitolo());
                  c = row.createCell(4);
                  c.setCellValue(l.getPrezzo());
                  c = row.createCell(5);
                  c.setCellValue(l.getPercentuale());
                  c = row.createCell(6);
                  c.setCellValue(l.getQa()-l.getQv());
                  c = row.createCell(7);
                  c.setCellValue(l.getQv());
                  c = row.createCell(8);
                  c.setCellValue(l.getTag());
                  c = row.createCell(9);
                  c.setCellFormula("H" + rowCount + "*(E" + rowCount +"-(E" + rowCount + "*F" + rowCount + "))");

              }
              row = sheet.createRow(rowCount++);
              row = sheet.createRow(rowCount++);
              c = row.createCell(8);
              c.setCellValue("TOTALE: ");
              c = row.createCell(9);
//              c.setCellFormula("SUM(J2:J+" + (rowCount-2) + ")");

            wb.write(fileOut);

          }catch(Exception e) {
            e.printStackTrace();
          }finally{
            try {
              fileOut.close();
            } catch(Exception e){
                    e.printStackTrace();
            }

          }
        

        }

        private boolean isExcelExtraction(){
        if(!StringUtils.isEmpty(commandString)){
            String[] parts = commandString.split(COMMAND_PARAMETERS_SEPARATOR);
            return parts[parts.length-1].equals(EXCEL_EXTRACTION_TYPE);
        }

        return false;
    }

    private boolean isJsonExtraction(){
        if(!StringUtils.isEmpty(commandString)){
            String[] parts = commandString.split(COMMAND_PARAMETERS_SEPARATOR);
            return parts[parts.length-1].equals(JSON_EXTRACTION_TYPE);
        }

        return false;
    }

    private String getPublisher(){
        if(!StringUtils.isEmpty(commandString)){
            String[] parts = commandString.split(COMMAND_PARAMETERS_SEPARATOR);
            return parts[1];
        }

        return null;
    }

}
