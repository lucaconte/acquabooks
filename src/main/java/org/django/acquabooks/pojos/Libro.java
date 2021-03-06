package org.django.acquabooks.pojos;

import io.searchbox.annotations.JestId;

import java.util.Date;

public class Libro{

  @JestId
  private String barcode;
  private String titolo;
  private String autore;
  private String editore;
  private String tag;
  private Double prezzo;
  private Double sconto;
  private Double percentuale;
  private Integer qa;
  private Integer qv;
  private Long ultimoAggiornamento;

  
  /**
   * @return the editore
   */
  public String getEditore() {
          return editore;
  }

  /**
   * @param editore the editore to set
   */
  public void setEditore(String editore) {
          this.editore = editore;
  }

        public String getTag() {
        return tag;
}

public void setTag(String tag) {
        this.tag = tag;
}
  @Override
public String toString() {
        return "Libro [barcode=" + barcode + ", editore=" + editore + ", titolo=" + titolo + ", autore="
                        + autore + ", tag=" + tag + ", prezzo=" + prezzo + ", sconto=" + prezzo
                        + ", percentuale=" + percentuale + ", qa=" + qa + ", qv=" + qv
                        + "]";
}

/**
   * @return the barcode
   */
  public String getBarcode() {
          return barcode;
  }

  /**
   * @param barcode the barcode to set
   */
  public void setBarcode(String barcode) {
          this.barcode = barcode;
  }

  /**
   * @return the titolo
   */
  public String getTitolo() {
          return titolo;
  }

  /**
   * @param titolo the titolo to set
   */
  public void setTitolo(String titolo) {
          this.titolo = titolo;
  }

  /**
   * @return the autore
   */
  public String getAutore() {
          return autore;
  }

  /**
   * @param autore the autore to set
   */
  public void setAutore(String autore) {
          this.autore = autore;
  }

        /**
   * @return the prezzo
   */
  public Double getPrezzo() {
          return prezzo!=null?prezzo:-9999;
  }

  /**
   * @param prezzo the prezzo to set
   */
  public void setPrezzo(Double prezzo) {
          this.prezzo = prezzo;
  }

  /**
   * @return the percentuale
   */
  public Double getPercentuale() {
          return percentuale;
  }

  /**
   * @param percentuale the percentuale to set
   */
  public void setPercentuale(Double percentuale) {
          this.percentuale = percentuale;
  }

  /**
   * @return the qa
   */
  public Integer getQa() {
          return qa;
  }

  /**
   * @param qa the qa to set
   */
  public void setQa(Integer qa) {
    this.qa = qa;
  }

  /**
   * @return the qv
   */
  public Integer getQv() {
          return qv;
  }

  /**
   * @param qv the qv to set
   */
  public void setQv(Integer qv) {
          this.qv = qv;
  }
    
  public void incrementaVenduto(int amount){
    this.setQv(this.getQv()+ amount);
  }


  public Double getSconto() {
      if(sconto == null){
          sconto = 0D;
      }
     return sconto;
  }

  public void setSconto(Double sconto) {
      this.sconto = sconto;
  }

  public Double getPrezzoScontato(){
      return getPrezzo() - (getPrezzo() * getSconto());
  }

  public Long getUltimoAggiornamento() {
      return ultimoAggiornamento;
  }

  public void setUltimoAggiornamento(Long ultimoAggiornamento) {
      this.ultimoAggiornamento = ultimoAggiornamento;
  }
}

