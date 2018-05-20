package com.kcc.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.kcc.enums.Unit;

public class Mss implements Serializable {

  private static final long serialVersionUID = 5877335527501207791L;

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
