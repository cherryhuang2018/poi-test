package com.kcc.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kcc.enums.Unit;

public class Asp implements Serializable {

  private static final long serialVersionUID = 2332411895003962725L;

  private BigDecimal nums;

  private Unit unit;

  public BigDecimal getNums() {
    return nums;
  }

  public void setNums(BigDecimal nums) {
    this.nums = nums;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
  
}
