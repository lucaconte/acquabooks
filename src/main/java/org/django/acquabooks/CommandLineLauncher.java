package org.django.acquabooks;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineLauncher {
  
  private static final Logger logger = LoggerFactory.getLogger(CommandLineLauncher.class);
  
  private Options options;
  private Parser parser;
 
  
 public static void main(String[] args) {
   CommandLineLauncher cl=new CommandLineLauncher();
   if(args.length==0){
             HelpFormatter formatter = new HelpFormatter();
             formatter.printHelp( "java -jar acquabooks<VERSION>.jar -H <HOST> <command> ", CommandLineLauncher.getOptionsInstance() );
             System.exit(1);
   }
    

   String host = "localhost";
    String port = "9200"; 
  
   try {
     CommandLine line = cl.getParser().parse( cl.getOptions(), args );

     if(line.hasOption("h")){
       HelpFormatter formatter = new HelpFormatter();
       formatter.printHelp( "java -jar acquabooks<VERSION>.jar -H <HOST> <command> ", cl.getOptions() );
       System.exit(0);
     }
     
     if(line.hasOption("H")){
       host = line.getOptionValue("H");
     }
     
     if(line.hasOption("P")){
       port = line.getOptionValue("P");
     }
   
    
     //modalità di funzionamento esclusive
     if(line.hasOption("s")){
      
        SellingManager m = new SellingManager(ElasticRESTClient.getInstance("http", host, port)); 
        m.run();
     }else if(line.hasOption("d")){
      
        SellingManager m = new SellingManager(ElasticRESTClient.getInstance("http", host, port)); 
        m.runDetail();
     }else if(line.hasOption("i")){
        ImportManager m = new ImportManager(line.getOptionValue("i"),ElasticRESTClient.getInstance("http", host, port)); 
        m.run();
     }else if(line.hasOption("u")){
        ImportManager m = new ImportManager(line.getOptionValue("u"), true, ElasticRESTClient.getInstance("http", host, port)); 
        m.run();
     }else if(line.hasOption("x")){
        ExtractionManager m = new ExtractionManager(ElasticRESTClient.getInstance("http", host, port), line.getOptionValue("x"));
        m.run();
     }else if(line.hasOption("cr")){
        CrudManager m = new CrudManager(ElasticRESTClient.getInstance("http", host, port));
        m.insert();
     }else if(line.hasOption("up")){
        CrudManager m = new CrudManager(ElasticRESTClient.getInstance("http", host, port));
        m.update();
     }else if(line.hasOption("del")){
        CrudManager m = new CrudManager(ElasticRESTClient.getInstance("http", host, port));
        m.delete();
     }
   } catch (ParseException e1) {
      System.out.println(e1.getMessage());
   
   }

 }


 public static Options getOptionsInstance(){
    DefaultParser parser = new DefaultParser();
    // create the Options

    Options options = new Options();
    options.addOption( "h", "help", false, "Print this comman line help" );
    
    options.addOption(Option.builder( "H" )
        .desc( "ElasticSearch host")
        .required(false)
        .hasArg()
        .argName("HOST")
        .longOpt("hostname").build());

    options.addOption( Option.builder( "p" )
        .desc( "ElasticSearch port [default 9200]")
        .required(false)
        .hasArg()
        .argName("PORT")
        .longOpt("port").build() );
 
    OptionGroup optionGroup = new OptionGroup();
     optionGroup.addOption( Option.builder( "x" )
        .desc( "Export: it extract data ad save to a single file in the current directory" +
                " the argument accept the type of export [more info will arrive]")
        .required(false)
        .hasArg()
        .argName("TYPE_OF_EXPORT")
        .longOpt("extract").build() );



     optionGroup.addOption( Option.builder("s")
        .desc("Selling mode")
        .required(false)
        .build());
    
    optionGroup.addOption( Option.builder("d")
        .desc("Detail mode")
        .required(false)
        .build());
     optionGroup.addOption( Option.builder("cr")
        .desc("Create new")
        .required(false)
        .build());
     optionGroup.addOption( Option.builder( "up" )
        .desc( "Update a book")
        .required(false)
        .build());
     optionGroup.addOption( Option.builder( "del" )
        .desc( "Delete book")
        .required(false)
        .build());



    optionGroup.addOption( Option.builder( "i" )
        .desc( "file to import")
        .required(false)
        .hasArg()
        .argName("FILE")
        .longOpt("import").build() );
    
    optionGroup.addOption( Option.builder( "u" )
        .desc( "file to import in update/edit mode: as 'import' but differently to it, if the record already exist it will be updated and not skipped")
        .required(false)
        .hasArg()
        .argName("FILE")
        .longOpt("update").build() );
   
    options.addOptionGroup(optionGroup); 
    return  options;
  }

  public Options getOptions() {
    if (options == null) {
      options =  getOptionsInstance();
    }

    return options;
  }

  public Parser getParser() {
    if (parser == null) {
      parser =   new PosixParser();
      
    }

    return parser;
  }

}
