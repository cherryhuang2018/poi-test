package com.kcc.pojo;

import java.io.Serializable;
import java.math.BigInteger;

public class Oem extends FormBase implements Serializable {

  private static final long serialVersionUID = -4572415609450628978L;

  private String level;
  
  private BigInteger year;
  
  private String oemName;
  
  private Sys sys;
  
  private Asp asp;
  
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
  
  public String getOemName() {
    return oemName;
  }

  public void setOemName(String oemName) {
    this.oemName = oemName;
  }

  public Sys getSys() {
    return sys;
  }

  public void setSys(Sys sys) {
    this.sys = sys;
  }

  public Asp getAsp() {
    return asp;
  }

  public void setAsp(Asp asp) {
    this.asp = asp;
  }

}
