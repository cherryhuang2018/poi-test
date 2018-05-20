package com.kcc.pojo;

import java.io.Serializable;
import java.math.BigInteger;

public class EndUser extends FormBase implements Serializable {

  private static final long serialVersionUID = 6935780660516814461L;

  private String level;
  
  private BigInteger year;

  private String endUserName;

  private Sys sys;

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

  public String getEndUserName() {
    return endUserName;
  }

  public void setEndUserName(String endUserName) {
    this.endUserName = endUserName;
  }

  public Sys getSys() {
    return sys;
  }

  public void setSys(Sys sys) {
    this.sys = sys;
  }

}
