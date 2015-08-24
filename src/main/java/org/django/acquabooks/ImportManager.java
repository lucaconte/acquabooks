package org.django.acquabooks;

import java.io.File;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.django.acquabooks.pojos.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ImportManager {
  public static int CONSOLE_ROW_LENGHT = 80;

  private static final Logger logger = LoggerFactory
                  .getLogger(CommandLineLauncher.class);

  private ElasticRESTClient elclient;

  private File fileIn;

  //una volta settato rimane il che impedisce alloggetto di essere
  // un singleton usabile sia per gli inport che gli export, ritenuto che
  // non è in previsione una versione ad interfaccia grafica stile ncurses
  // o swing o altro, si è ritenuto che via command line "one shot" ogni azione
  // crea un nuovo oggetto e lo termina. 
  private boolean isEdit;

  public ImportManager(String filename) {
          elclient = ElasticRESTClient.getInstance("http", "localhost", "9200");
          fileIn = new File(filename);
  }

  /*
   * default: is an Import
  */
  public ImportManager(String filename,  ElasticRESTClient client){
    this.elclient = client;
    this.fileIn = new File(filename);
    this.isEdit = false;
  }


  public ImportManager(String filename, boolean isEdit , ElasticRESTClient client){
    this.elclient = client;
    this.fileIn = new File(filename);
    this.isEdit = isEdit;
  }

  public void run(){
    String in = null;
    try{
      Scanner sc = new Scanner(fileIn);
      int lineNumber = 0;
      while (sc.hasNextLine()) {
        in = sc.nextLine();
        Gson gson = new Gson();
        Libro lin = gson.fromJson(in,Libro.class);
        lineNumber++;
        if(!isEdit){
          Libro l = elclient.getDetail(lin.getBarcode());
          if(l != null){
            logErr("FALLITO record di linea: " + lineNumber + " già presente!! Barcode: "+l.getBarcode());
            continue;
          } 
        } 
       
        if(elclient.index(lin)){
          logWarn("Indicizzato record di linea: " + lineNumber);
        }else{
          logErr("FALLITO record di linea: " + lineNumber);
        }
      }
    } catch(Exception e){
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }

  private void logWarn(String msg){
     this.logger.warn("                                        (((");
     this.logger.warn("                                       (@ @)");
     this.logger.warn("-----------------------------------ooO--(_)--Ooo--------------------------------");
     this.logger.warn("| "+ StringUtils.rightPad(msg, CONSOLE_ROW_LENGHT - 3 - msg.length())                               +"|");
     this.logger.warn("--------------------------------------------------------------------------------");
  }

  private void logErr(String msg){
     this.logger.error("                                        §§§");
     this.logger.error("                                       (+ +)");
     this.logger.error("-----------------------------------ooO--(_)--Ooo--------------------------------");
     this.logger.error("| "+ StringUtils.rightPad(msg, CONSOLE_ROW_LENGHT - 3 - msg.length())                               +"|");
     this.logger.error("--------------------------------------------------------------------------------");
        }


}
