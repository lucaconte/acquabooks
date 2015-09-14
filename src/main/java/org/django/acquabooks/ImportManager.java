package org.django.acquabooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.stream.JsonReader;
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
      JsonReader reader = null;
      try {
          Gson gson = new Gson();

          reader = new JsonReader(new FileReader(fileIn));
          reader.beginArray();
          while (reader.hasNext()) {

            Libro lin = new Libro();
            reader.beginObject();
               while (reader.hasNext()) {
                String prop = reader.nextName();
                 switch (prop){
                    case "barcode":
                        lin.setBarcode(reader.nextString());
                        break;
                    case "editore":
                        lin.setEditore(reader.nextString());
                        break;
                    case "tag":
                        lin.setTag(reader.nextString());
                        break;
                    case "autore":
                        lin.setAutore(reader.nextString());
                        break;
                    case "titolo":
                        lin.setTitolo(reader.nextString());
                        break;
                    case "prezzo":
                        lin.setPrezzo(reader.nextDouble());
                        break;
                    case "percentuale":
                        lin.setPercentuale(reader.nextDouble());
                        break;
                    case "qa":
                        lin.setQa(((int) reader.nextLong()));
                        break;
                    case "qv":
                        lin.setQv(((int) reader.nextLong()));
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
               }
            reader.endObject();
            if(!isEdit){
              Libro l = elclient.getDetail(lin.getBarcode());
              if(l != null){
                  logErr("FALLITO record già presente!! Barcode: "+l.getBarcode());
                  continue;
              }
            }
            if(elclient.index(lin)){
                logWarn("Indicizzato record con barcode: " + lin.getBarcode());
            }else{
                logErr("FALLITO record con barcode: " + lin.getBarcode());
            }
          }
          reader.endArray();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }finally {
          try {
              reader.close();
          } catch (IOException e) {
              logger.error("Error on closing reader",e);
          }
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
