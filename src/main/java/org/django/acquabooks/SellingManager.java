package org.django.acquabooks;

import org.apache.commons.lang3.StringUtils;
import org.django.acquabooks.io.Console;
import org.django.acquabooks.pojos.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class SellingManager {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineLauncher.class);
    public static  String STOP_READING_SEQUENCE = "q";
    public static  int DEFAULT_OP_AMOUNT = 1;
    public static  String COMMAND = ":";
    private ElasticRESTClient elclient;


    public  SellingManager(){
        elclient = ElasticRESTClient.getInstance("http","localhost", "9200");
    }

    public  SellingManager(ElasticRESTClient client){
        elclient = client;
    }

    public void run(){
        String in = null;
        Console.welcome(System.out,"ACQUATORPIDA BOOK SHOCKS - SPARA VENDERE");
        Scanner sc = new Scanner(System.in);
        int opAmount = DEFAULT_OP_AMOUNT;
        while (sc.hasNextLine()) {
            in = sc.nextLine();
            if(STOP_READING_SEQUENCE.equals(in)){
                return;
            }

            if(StringUtils.isEmpty(in)){
                continue;
            }

            if(in.startsWith(COMMAND)){
                String[] parts = in.split(COMMAND);
                try {
                    opAmount = Integer.parseInt(parts[1]);
                }catch(Throwable e){
                    Console.genericErr("NUMERO NON VALIDO");
                    opAmount = DEFAULT_OP_AMOUNT;
                }
                continue;
            }

            try {
                Libro l = elclient.getDetail(in);
                if(l == null){
                    Console.genericWarn("LIBRO NON TROVATO: " + in);
                    opAmount = DEFAULT_OP_AMOUNT;
                    continue;
                }
                l.incrementaVenduto(opAmount);

                boolean r = elclient.index(l, opAmount != 0);
                if(!r){
                    Console.genericErr("Errore in aggiornamento");
                }else{
                    Console.ok(System.out);
                    Console.detail(l, System.out);
                }
                opAmount = DEFAULT_OP_AMOUNT;
            } catch(Exception e){
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public void runDetail(){
        String in = null;
        Console.welcome(System.out,"ACQUATORBIDA BOOK CHOC - SPARA PER IL DETTAGLIO");
        Scanner sc = new Scanner(System.in);
        int lineNumber = 0;
        while (sc.hasNextLine()) {
            in = sc.nextLine();
            if(STOP_READING_SEQUENCE.equals(in)){
                return;
            }

            if(StringUtils.isEmpty(in)){
                continue;
            }
            try {
                Libro l = elclient.getDetail(in);
                if(l == null){
                    Console.genericWarn("LIBRO NON TROVATO: " + in);
                }else{
                    Console.ok(System.out);
                    System.out.println(l.toString());
                }

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }



}
