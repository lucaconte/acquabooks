package org.django.acquabooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;
import org.django.acquabooks.pojos.Libro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.List;


/**
 * Created by conte on 25/08/15.
 */
public class ExtractionManager {

    /*
    extract all books
     */
    public static final String TYPE_ALL = "A";
    public static final String TYPE_BY_PUBLISHER = "P";

    public static final String COMMAND_PARAMETERS_SEPARATOR = ";";
    public static final String COMMAND_PARAMETERS_ASSIGNATION = ":";
    public static final String JSON_EXTRACTION_TYPE = "j";

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
        String filename = "./";
        List<Libro> r = null;

        try {


            if(commandString.startsWith(TYPE_ALL)){
                r = elclient.getAll();
                filename += TYPE_ALL + "_" + System.currentTimeMillis();
            }else if(commandString.startsWith(TYPE_BY_PUBLISHER)){
                r = elclient.getByPublisher(getPublisher());
                filename += TYPE_BY_PUBLISHER + "_" + getPublisher() + "_" + System.currentTimeMillis();
            }


            if(isJsonExtraction()){
                filename += ".json";
                File fout =  new File(filename);
                fout.createNewFile();
                JsonWriter writer = new JsonWriter(new FileWriter(fout));
                writer.setIndent("  ");
                Gson g = new GsonBuilder().setPrettyPrinting().create();
                g.toJson(r,new TypeToken<Collection<Libro>>(){}.getType(),writer);
                writer.flush();
            }
        }catch (Throwable e){
            logger.error("Error on extracting data: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
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
