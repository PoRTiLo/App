package com.portilo.app.model;

/**
 * Created by jase on 27.02.2015.
 */
public class Consumption {

  Double minConsumption;
  Double maxConsumption;
  Double aveConsumption;

  public Consumption(Double minConsumption, Double maxConsumption, Double aveConsumption) {
    this.aveConsumption = aveConsumption;
    this.maxConsumption = maxConsumption;
    this.minConsumption = minConsumption;
  }

  public void setMinConsumption(Double minConsumption) {
    this.minConsumption = minConsumption;
  }

  public void setMaxConsumption(Double maxConsumption) {
    this.maxConsumption = maxConsumption;
  }

  public void setAveConsumption(Double aveConsumption) {
    this.aveConsumption = aveConsumption;
  }

  public Double getMinConsumption() {

    return minConsumption;
  }

  public Double getMaxConsumption() {
    return maxConsumption;
  }

  public Double getAveConsumption() {
    return aveConsumption;
  }
}
