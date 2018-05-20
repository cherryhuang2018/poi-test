package com.kcc.enums;

public enum SheetType {

  ENDUSER("End User"), OEM("OEM"), ODM("ODM"),ENDUSERvsOEM("End User vs OEM"), OEMvsODM("OEM vs ODM");
  
  private final String sheepTypeName;
  
  private SheetType(String sheepTypeName) {
    this.sheepTypeName = sheepTypeName;
  }

  public String getSheepTypeName() {
    return sheepTypeName;
  }
  
}
