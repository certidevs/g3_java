package com.demo.model.enums;

public enum HouseType {

  APARTAMENTO("Apartamento"),
  CASA("Casa"),
  HABITACION("Habitacion");


    private final String label;

  HouseType(String label) {
      this.label = label;
    }

    public String getLabel() {
      return label;
    }
}
