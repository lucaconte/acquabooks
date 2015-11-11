package org.django.acquabooks.io;

import org.apache.commons.lang3.StringUtils;
import org.django.acquabooks.pojos.Libro;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by conte on 16/10/15.
 */
public class Console {

  public static DateFormat TS_DATE_FORMAT =  new SimpleDateFormat("YYYYddMMHHmmss");
  public static DateFormat READABLE_DATE_FORMAT =  new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");


  public static  int CONSOLE_ROW_LENGHT = 80;
  private static PrintStream out;

  public static void genericWarn(String msg) {
    genericWarn(System.out,msg);
  }
  public static void genericWarn(PrintStream out, String msg){

    out.println("                                        (((");
    out.println("                                       (@ @)");
    out.println("-----------------------------------ooO--(_)--Ooo--------------------------------");
    out.println("| "+ StringUtils.rightPad(msg, CONSOLE_ROW_LENGHT - 3 - msg.length())                               +"|");
    out.println("--------------------------------------------------------------------------------");
  }
  public static void genericErr(String msg) {
    genericErr(System.err, msg);
  }
  public static void genericErr(PrintStream out, String msg){
    out.println("                                        §§§");
    out.println("                                       (+ +)");
    out.println("-----------------------------------ooO--(_)--Ooo--------------------------------");
    out.println("| "+ StringUtils.rightPad(msg, CONSOLE_ROW_LENGHT - 3 - msg.length())                               +"|");
    out.println("--------------------------------------------------------------------------------");
  }
  public static void ok(PrintStream out){


    Ansi a = new Ansi(null, Ansi.AnsiColor.GREEN, null);
    a.outLine("     OOOOOOOOO     KKKKKKKKK    KKKKKKK");
    a = new Ansi(null, Ansi.AnsiColor.BLUE, null);
    a.outLine("   OO:::::::::OO   K:::::::K    K:::::K");
    a = new Ansi(null, Ansi.AnsiColor.YELLOW, null);
    a.outLine(" OO:::::::::::::OO K:::::::K    K:::::K");
    a = new Ansi(null, Ansi.AnsiColor.WHITE, null);
    a.outLine("::::::OOO:::::::OK:::::::K   K::::::K");
    a = new Ansi(null, Ansi.AnsiColor.MAGENTA, null);
    a.outLine(":::::O   O::::::OKK::::::K  K:::::KKK");
    a = new Ansi(null, Ansi.AnsiColor.CYAN, null);
    a.outLine("::::O     O:::::O  K:::::K K:::::K   ");
    a = new Ansi(null, Ansi.AnsiColor.RED, null);
    a.outLine("::::O     O:::::O  K::::::K:::::K    ");
    a = new Ansi(null, Ansi.AnsiColor.BLUE, null);
    a.outLine("::::O     O:::::O  K:::::::::::K     ");
    a = new Ansi(null, Ansi.AnsiColor.YELLOW, null);
    a.outLine("::::O     O:::::O  K:::::::::::K     ");
    a = new Ansi(null, Ansi.AnsiColor.MAGENTA, null);
    a.outLine("::::O     O:::::O  K::::::K:::::K    ");
    a = new Ansi(null, Ansi.AnsiColor.GREEN, null);
    a.outLine("::::O     O:::::O  K:::::K K:::::K   ");
    a = new Ansi(null, Ansi.AnsiColor.WHITE, null);
    a.outLine(":::::O   O::::::OKK::::::K  K:::::KKK");
    a = new Ansi(null, Ansi.AnsiColor.RED, null);
    a.outLine("::::::OOO:::::::OK:::::::K   K::::::K");
    a = new Ansi(null, Ansi.AnsiColor.YELLOW, null);
    a.outLine(" OO:::::::::::::OO K:::::::K    K:::::K");
    a = new Ansi(null, Ansi.AnsiColor.BLUE, null);
    a.outLine("   OO:::::::::OO   K:::::::K    K:::::K");
    a = new Ansi(null, Ansi.AnsiColor.MAGENTA, null);
    a.outLine("     OOOOOOOOO     KKKKKKKKK    KKKKKKK");
    a = new Ansi(null, Ansi.AnsiColor.BLUE, null);
    a.outLine("                                       ");

  }
  public static void welcome(PrintStream out){

    welcome(out,"ACQUATORPIDA BOOK SHOCK");

  }

  public static void welcome(PrintStream out, String msg){
    Ansi a = new Ansi(Ansi.Attribute.BLINK, Ansi.AnsiColor.GREEN, null);
    out.print("           ");
    out.print("HH");
    out.println();
    out.println("           HH");
    out.println(" BBB       HH                                        ,z.");
    out.println(" === .___. HH     %%%%                   .o.       ,zZZZ>");
    out.println(" BBB |   | HH 838 \\\\\\\\ EEE    AAAAA     ,0X0'    ,zZZZ");
    out.println(" BBB |<<<| HH 838 %%%% EEE ## DDDDD    ,0X0'   ,zZZZ");

    a = new Ansi(Ansi.Attribute.BLINK, Ansi.AnsiColor.YELLOW, null);
    out.print(" BBB | ");
    a.out("Z");

    out.println(" | HH 838 %GR% +++ ## AAAAA   ,0X0'  ,zZZZ");
    out.print(" BBB | ");
    a.out("T");
    out.println(" | HH 838 %%%% EEE ## <<v>>  ,0X0' ,zZZZ");
    out.print(" BBB | ");
    a.out("L");
    out.println(" | HH 838 %%%% EEE ## AAAAA ,0X0',zZZZ\"HH$HHHHHHHDDHH$HH");
    out.println(" === |<<<| HH 838 //// EEE ## AAAAA.0X0;zZZZ\"  EE$EEEEEEEDDEE$EE");
    out.println(" BBB |___| HH 838 %%%% EEE ## AAAAA'\"0' \"Z\"    HH$HHHHHHHDDHH$HH");
    out.println("|                                                               |");
    out.println("| "+ StringUtils.rightPad(msg, CONSOLE_ROW_LENGHT - 18)+"|");
    out.println(" ---------------------------------------------------------------");
  }

  public static void main(String[] args){
    Ansi a = new Ansi(Ansi.Attribute.BLINK, Ansi.AnsiColor.GREEN, null);
    String msg = a.colorize("Porco dio");

   // a.println(System.out, msg);
Console.welcome(System.out,"ACQUATORPIDA BOOK SHOCK");
  }

  public static void detail(Libro l,PrintStream out){


    Ansi a = new Ansi(Ansi.Attribute.BLINK, Ansi.AnsiColor.YELLOW, null);

    out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    out.println("Barcode:   "+l.getBarcode() + " Ultima modifica: " + READABLE_DATE_FORMAT.format(new Date(l.getUltimoAggiornamento())));
    out.println("Titolo:   " + l.getTitolo());
    out.println("Autore:   "+l.getAutore());
    out.print("Prezzo:   " + l.getPrezzo() + " Prezzo Scontato: ");
    if(l.getSconto()!=0) {
      a.outLine(l.getPrezzoScontato().toString());
    }else{
      out.println(l.getPrezzoScontato());
    }
    out.println("QA: "+l.getQa() + " QV: " +l.getQv() + " Margine: "+ l.getPercentuale() + " Sconto: " + l.getSconto());


    out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
  }
}
