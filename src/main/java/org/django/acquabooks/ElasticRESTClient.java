package org.django.acquabooks;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;

import java.io.IOException;
import java.util.List;

import org.django.acquabooks.pojos.Libro;


public class ElasticRESTClient {
   private JestClientFactory factory;
   private JestClient client;

    public static final Integer MAX_RESULTS = 1000;
   

  public static ElasticRESTClient getInstance(String protocol, String host, String port){
    ElasticRESTClient r = new ElasticRESTClient();
    r.setFactory(new JestClientFactory());
    r.getFactory().setHttpClientConfig(new HttpClientConfig
                          .Builder(protocol + "://" + host + ":" + port)
                          .multiThreaded(true)
                          .build());
   
    r.setClient(r.getFactory().getObject());
    
    return r;
   
  }

  /**
   * @return the factory
   */
  public JestClientFactory getFactory() {
          return factory;
  }

  /**
   * @param factory the factory to set
   */
  public void setFactory(JestClientFactory factory) {
          this.factory = factory;
  }

  /**
   * @return the client
   */
  public JestClient getClient() {
          return client;
  }

  /**
   * @param client the client to set
   */
  public void setClient(JestClient client) {
          this.client = client;
  }


  public Libro getDetail(String barcode) throws IOException {
    Get get = new Get.Builder("acquatorbida", barcode).type("libro").build();
    JestResult result = client.execute(get);
    if(result.isSucceeded()){
      return result.getSourceAsObject(Libro.class);
    }
    return null;
  }

    public boolean delete(String barcode) throws IOException {
     JestResult result = client.execute(new Delete.Builder(barcode)
                .index("acquatorida")
                .type("libro")
                .build());

        return result.isSucceeded();

    }

  public Boolean index(Libro l) throws IOException {
    Index index = new Index.Builder(l).index("acquatorbida").type("libro").build();
    JestResult r = client.execute(index);
    return r.isSucceeded();
  }


    public List<Libro> getAll() throws IOException {

        String query = "{" +
                "\"query\": {" +
                     "\"match_all\": {}" +
                "}" +
             "}";

        Search search = new Search.Builder(query)
                // multiple index or types can be added.
                .addIndex("acquatorbida")
                //        .setSearchType(SearchType.COUNT)
                .setParameter("size", MAX_RESULTS)
                .build();


        SearchResult result = client.execute(search);
        //result.getTotal()
        return  result.getSourceAsObjectList(Libro.class);
    }

    public List<Libro> getByPublisher(String publisher) throws IOException {

        String query = "{" +
                "\"query\": {" +
                     "\"match\": {" +
                            "\"editore\":\""+publisher+"\""+
                        "}" +
                "}" +
             "}";

        Search search = new Search.Builder(query)
                // multiple index or types can be added.
                .addIndex("acquatorbida").addType("libro")
                //        .setSearchType(SearchType.COUNT)
                .setParameter("size", MAX_RESULTS)
                .build();


        SearchResult result = client.execute(search);
        //result.getTotal()
        return  result.getSourceAsObjectList(Libro.class);

    }

    public List<Libro> getVendutoByPublisher(String publisher) throws IOException {
        //in range lt è un numero alto (per i volumi di acquatorbida) a caso
        String query = "{\"query\" : {" +
                "      \"bool\" : {" +
                "         \"must\" : [" +
                "            {" +
                            "  \"multi_match\": {" +

                                "\"query\":" +
                                    "\"" + publisher +"\"," +
                                "\"fields\":" +
                                    "[ \"editore\", \"tag\" ]" +
                "               }" +
                            "}," +


//               "            {" +
//                "               \"term\" : {" +
//                "                  \"editore\" : \""+ publisher + "\""+
//                "                 }" +
//                "            }," +


                "            {" +
                "              \"range\" : {" +
                "                \"qv\" : { \"gt\" : 0, \"lt\" : 20000 }" +
                "               }" +
                "            }" +
                "         ]" +
                "      }" +
                    "}" +
                "}";

        Search search = new Search.Builder(query)
                // multiple index or types can be added.
                .addIndex("acquatorbida").addType("libro")
                //        .setSearchType(SearchType.COUNT)
                .setParameter("size", MAX_RESULTS)
                .build();


        SearchResult result = client.execute(search);
        //result.getTotal()
        return  result.getSourceAsObjectList(Libro.class);
    }

}
