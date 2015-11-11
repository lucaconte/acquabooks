package org.django.acquabooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.stream.JsonReader;
import org.apache.commons.lang3.StringUtils;
import org.django.acquabooks.io.Console;
import org.django.acquabooks.pojos.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ImportManager {
  public static int CONSOLE_ROW_LENGHT = 80;



  public static int MODE_INSERT = 0;
  public static int MODE_EDIT = 1;
  public static int MODE_AZZERA_VENDUTO = 2;

  private static final Logger logger = LoggerFactory
                  .getLogger(CommandLineLauncher.class);

  private ElasticRESTClient elclient;

  private File fileIn;

  // se MODE_EDIT una volta settato rimane, il  che impedisce all'oggetto di essere
  // un singleton usabile sia per gli import che gli export, ritenuto che
  // non è in previsione una versione ad interfaccia grafica stile ncurses
  // o swing o altro, si è ritenuto che via command line "one shot" ogni azione
  // crea un nuovo oggetto e lo termina. 
  private int mode;

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
    this.mode = MODE_INSERT;
  }


  public ImportManager(String filename, int mode , ElasticRESTClient client){
    this.elclient = client;
    this.fileIn = new File(filename);
    this.mode = mode;
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
                    case "sconto":
                        lin.setSconto(reader.nextDouble());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
               }
            reader.endObject();
            if(mode == MODE_INSERT || mode == MODE_AZZERA_VENDUTO){
              Libro l = elclient.getDetail(lin.getBarcode());
              if(l != null && MODE_INSERT == mode){
                  logErr("FALLITO record già presente!! Barcode: "+l.getBarcode());
                  continue;
              }
              if(l == null && MODE_AZZERA_VENDUTO == mode){
                  logErr("FALLITO libro NON presente!! Barcode: "+l.getBarcode());
                  continue;
              }else if(MODE_AZZERA_VENDUTO == mode){ //l !=null is implicit
                  l.setQa(l.getQa() - l.getQv());
                  l.setQv(0);
                  lin = l;
              }

            }
            if(elclient.index(lin)){
                Console.genericWarn("Indicizzato record con barcode: " + lin.getBarcode());
            }else{
                Console.genericErr("FALLITO record con barcode: " + lin.getBarcode());
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
