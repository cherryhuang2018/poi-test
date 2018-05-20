package com.kcc.pojo;

import java.io.Serializable;
import java.math.BigInteger;

public class Odm extends FormBase implements Serializable {

  private static final long serialVersionUID = -7697246984037104126L;

  private String level;
  
  private BigInteger year;

  private String odmName;

  private Mss mss;

  private Asp asp;

  private AttachRate attachRate;

  private Gdpw gdpw;

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public BigInteger getYear() {
    return year;
  }

  public void setYear(BigInteger year) {
    this.year = year;
  }
  
  public String getOdmName() {
    return odmName;
  }

  public void setOdmName(String odmName) {
    this.odmName = odmName;
  }

  public Mss getMss() {
    return mss;
  }

  public void setMss(Mss mss) {
    this.mss = mss;
  }

  public Asp getAsp() {
    return asp;
  }

  public void setAsp(Asp asp) {
    this.asp = asp;
  }

  public AttachRate getAttachRate() {
    return attachRate;
  }

  public void setAttachRate(AttachRate attachRate) {
    this.attachRate = attachRate;
  }

  public Gdpw getGdpw() {
    return gdpw;
  }

  public void setGdpw(Gdpw gdpw) {
    this.gdpw = gdpw;
  }

}
