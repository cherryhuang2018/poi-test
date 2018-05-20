package com.kcc.pojo;

import java.io.Serializable;

public class FormBase implements Serializable {

  private static final long serialVersionUID = -3798916291411396830L;

  private String platform;

  private String category;

  private String application;

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

}
