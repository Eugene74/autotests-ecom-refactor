package com.ecom.core.config;

public class CardConfig {
  private CardConfig() {
    // Utility class: prevent instantiation
  }

  private static final PropertiesManager PROPERTIES = PropertiesManager.getInstance();
  public static final String[] cardVISA = PROPERTIES.getCardArray("cardVISA");
  public static final String[] cardVISAmt = PROPERTIES.getCardArray("cardVISAmt");
  public static final String[] cardMC = PROPERTIES.getCardArray("cardMC");
  public static final String[] cardMC06 = PROPERTIES.getCardArray("cardMC06");
  public static final String[] cardMC07 = PROPERTIES.getCardArray("cardMC07");
  public static final String[] cardMC05 = PROPERTIES.getCardArray("cardMC05");
  public static final String[] cardMCa = PROPERTIES.getCardArray("cardMCa");
  public static final String[] cardMCInst = PROPERTIES.getCardArray("cardMCInst");
  public static final String[] cardMCmt = PROPERTIES.getCardArray("cardMCmt");
  public static final String[] cardMCmta = PROPERTIES.getCardArray("cardMCmta");
  public static final String[] cardVisaRed = PROPERTIES.getCardArray("cardVisaRed");
  public static final String cardVISArec = PROPERTIES.getCard("cardVISArec");
  public static final String cardMCfr = PROPERTIES.getCard("cardMCfr");
  public static final String cardMCardfr = PROPERTIES.getCard("cardMCardfr");
  public static final String cardVISAfr = PROPERTIES.getCard("cardVISAfr");
  public static final String[] cardVISAmtRev = PROPERTIES.getCardArray("cardVISAmtRev");
  public static final String cardMdes = PROPERTIES.getCard("cardMdes");
  public static final String cardVts = PROPERTIES.getCard("cardVts");
}
