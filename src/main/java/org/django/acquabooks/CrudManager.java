package org.django.acquabooks;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.django.acquabooks.pojos.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrudManager {
  public static String STOP_READING_SEQUENCE = "q";
  public static int CONSOLE_ROW_LENGHT = 80;

  private static final Logger logger = LoggerFactory
                        .getLogger(CommandLineLauncher.class);

  private ElasticRESTClient elclient;

  public CrudManager() {
          elclient = ElasticRESTClient.getInstance("http", "localhost", "9200");
  }

  public CrudManager(ElasticRESTClient client){
    elclient = client;
  }
  public boolean delete(){
        String in = null;
        InputStreamReader isr= null;
        BufferedReader inp = null;


        try {
            isr =  new InputStreamReader(System.in);
            inp = new BufferedReader(isr);

            logWarn("CANCELLAZIONE LIBRO");
            Libro l = new Libro();
            System.out.println("Codice a barre: ");
            l.setBarcode(inp.readLine());

             return  elclient.delete(l.getBarcode());

        }catch(Exception ex){
            logErr(ex.getMessage());
        }finally{
            if(inp!=null){
                try {
                    inp.close();
                }catch(Exception e){}
            }
            if(isr!=null){
                try {
                    isr.close();
                }catch(Exception e){}
            }
        }
        return false;
    }

  public int insert(){
    String in = null;
      InputStreamReader isr= null;
    BufferedReader inp = null;


      try {
          isr =  new InputStreamReader(System.in);
          inp = new BufferedReader(isr);

          logWarn("INSERIMENTO NUOVO LIBRO");
          Libro l = new Libro();
          System.out.println("Codice a barre: ");
          l.setBarcode(inp.readLine());
          Libro lin = elclient.getDetail(l.getBarcode());
          if(lin!=null){
              logWarn("Libro già presente usa la funzionalità di edit per modificarne i contenuti");
              return 1;
          }


          System.out.println("Editore: ");
          l.setEditore(inp.readLine());

          System.out.println("Autore: ");
          l.setAutore(inp.readLine());

          System.out.println("Titolo: ");
          l.setTitolo(inp.readLine());

          System.out.println("Prezzo [Es 14.00] : ");
          l.setPrezzo(Double.parseDouble(inp.readLine()));

          System.out.println("Percentuale notra [Es 0.25]: ");
          l.setPercentuale(Double.parseDouble(inp.readLine()));

          System.out.println("QA: ");
          l.setQa(Integer.parseInt(inp.readLine()));

          System.out.println("QV: ");
          l.setQv(Integer.parseInt(inp.readLine()));

          System.out.println("Tags [separati da virgola]: ");
          l.setTag(inp.readLine());


          //qui occorrerebbe validare
           elclient.index(l);

      }catch(Exception ex){
          logErr(ex.getMessage());
      }finally{
        if(inp!=null){
            try {
                inp.close();
            }catch(Exception e){}
        }
        if(isr!=null){
            try {
                isr.close();
            }catch(Exception e){}
        }
      }
    return 0;
  }

    public int update(){
    String in = null;
      InputStreamReader isr= null;
    BufferedReader inp = null;


      try {
          isr =  new InputStreamReader(System.in);
          inp = new BufferedReader(isr);

          logWarn("MODIFICA LIBRO (tra parentesi graffe l'attuale valore del campo)");
          System.out.println("Codice a barre: ");
          Libro l = elclient.getDetail(inp.readLine());
          if(l==null){
              logWarn("Non eiste alcun libro con il codice a barre inserito");
              return 1;
          }

          String sTmp = null;
          System.out.println("Editore {" + l.getEditore() + "}: ");
          sTmp = inp.readLine();
          l.setEditore(!StringUtils.isEmpty(sTmp)?sTmp:l.getEditore());

          System.out.println("Autore {" + l.getAutore() + "}: ");
          sTmp = inp.readLine();
          l.setAutore(!StringUtils.isEmpty(sTmp)?sTmp:l.getAutore());

          System.out.println("Titolo {" + l.getTitolo() + "}: ");
          sTmp = inp.readLine();
          l.setTitolo(!StringUtils.isEmpty(sTmp)?sTmp:l.getTitolo());


          System.out.println("Prezzo [Es 14.00]  {" + l.getPrezzo() + "}: ");
          sTmp = inp.readLine();
          l.setPrezzo(!StringUtils.isEmpty(sTmp)?Double.parseDouble(sTmp):l.getPrezzo());

          System.out.println("Percentuale notra [Es 0.25] {" + l.getPercentuale() + "}: ");
          sTmp = inp.readLine();
          l.setPercentuale(!StringUtils.isEmpty(sTmp)?Double.parseDouble(sTmp):l.getPercentuale());

          System.out.println("QA {" + l.getQa() + "}: ");
          sTmp = inp.readLine();
          l.setQa(!StringUtils.isEmpty(sTmp)?Integer.parseInt(sTmp):l.getQa());

          System.out.println("QV {" + l.getQv() + "}: ");
          sTmp = inp.readLine();
          l.setQv(!StringUtils.isEmpty(sTmp)?Integer.parseInt(sTmp):l.getQv());

          System.out.println("Tags [separati da virgola] {"+l.getTag()+"}: ");
          sTmp = inp.readLine();
          l.setTag(!StringUtils.isEmpty(sTmp)?sTmp:l.getTag());


          //qui occorrerebbe validare
           elclient.index(l);

      }catch(Exception ex){
          logErr(ex.getMessage());
      }finally{
        if(inp!=null){
            try {
                inp.close();
            }catch(Exception e){}
        }
        if(isr!=null){
            try {
                isr.close();
            }catch(Exception e){}
        }
      }
    return 0;
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
