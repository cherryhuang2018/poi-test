package com.kcc.enums;

public enum Unit {

  MU("MU"), US("US"), PERCENTAGE("%"), P("P");

  private final String unitName;

  private Unit(String unitName) {
    this.unitName = unitName;
  }

  public String getUnitName() {
    return unitName;
  }

}
