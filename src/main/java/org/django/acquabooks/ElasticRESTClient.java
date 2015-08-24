package org.django.acquabooks;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Get;
import io.searchbox.core.Index;

import java.io.IOException;

import org.django.acquabooks.pojos.Libro;

import com.google.gson.Gson;

public class ElasticRESTClient {
   private JestClientFactory factory;
   private JestClient client;
   

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

  public Boolean index(Libro l) throws IOException {
    Index index = new Index.Builder(l).index("acquatorbida").type("libro").build();
    JestResult r = client.execute(index);
    return r.isSucceeded();
  }
  

}
