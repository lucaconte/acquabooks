package org.django.acquabooks;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.django.acquabooks.pojos.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SellingManager {
  public static  String STOP_READING_SEQUENCE = "q";
  public static  int CONSOLE_ROW_LENGHT = 80;
  
  private static final Logger logger = LoggerFactory.getLogger(CommandLineLauncher.class);


  private ElasticRESTClient elclient;
 

public  SellingManager(){
    elclient = ElasticRESTClient.getInstance("http","localhost", "9200");
  }
 
  public  SellingManager(ElasticRESTClient client){
    elclient = client;
  }

  public void run(){
    String in = null;
    
    Scanner sc = new Scanner(System.in);
    int lineNumber = 0;
    while (sc.hasNextLine()) {
        in = sc.nextLine();
        if(STOP_READING_SEQUENCE.equals(in)){
          return;
        }
        System.out.println("Letto: " + in);
        try {
          Libro l = elclient.getDetail(in);
          if(l == null){
            logWarn("LIBRO NON TROVATO: "+in);
          }
         l.incrementaVenduto();
         boolean r = elclient.index(l);
         if(!r){
          logErr("Errore in aggiornamento");
         }
        } catch(Exception e){
                logger.error(e.getMessage());
                e.printStackTrace();
        }
   }
  }
  public void runDetail(){
    String in = null;
    
    Scanner sc = new Scanner(System.in);
    int lineNumber = 0;
    while (sc.hasNextLine()) {
        in = sc.nextLine();
        if(STOP_READING_SEQUENCE.equals(in)){
          return;
        }
        System.out.println("Letto: " + in);
        try {
          Libro l = elclient.getDetail(in);
          if(l == null){
            logWarn("LIBRO NON TROVATO: "+in);
          }else{
            logger.info(l.toString());
          }
          
        } catch(Exception e){
                e.printStackTrace();
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
