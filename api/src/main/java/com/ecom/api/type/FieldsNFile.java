package com.ecom.api.type;

/**
 * Tran_type 97 2 Transaction type (Appendix 1): 05 – purchase, 25 - purchase reversal 06 – credit,
 * 26 - credit reversal 07- cash, 27 - cash reversal 08 – deposit, 28 - deposit reversal 09 –
 * purchase with cashback, 29 - purchase with cashback reversal
 *
 * <p>Appr_src 105 1 Authorization code source 1 - On-line 3 - Off-line 4 - voice authorization
 *
 * <p>Sb_cnvdate 214 8 Current year (YYYY) + Field 16 value (MMDD) from authorization response. If
 * Field 16 is empty then space filled Example: 20200917
 *
 * <p>MCC_code 339 4 Merchant category code (ISO Merchant service code) For VISA Direct / MasterCard
 * MoneySend transactions: '6012'(Visa)/'6538',’4829’*,’6540’*(MasterCard) - Funding transaction;
 * '6012'(Visa)/'6536'(MasterCard intracountry)/'6537' (MasterCard /intercountry) – Payment
 * transaction - MasterCard allows these MCC values from 05.11.2019
 *
 * <p>FLD_123_1 883 1 CVV2 Result Code (BASEII) For Visa transactions: Space – no info M – CVV2
 * matched, N – CVV2 not matched, P – not processed, S – accepter does not find CVV2 on card, U –
 * issuer is not certified.
 *
 * <p>FLD_123_1 883 1 CVV2 Result Code (BASEII)
 *
 * <p>FLD_095 Field 63 or Field 104, Dataset 01, tag 01 or tag 02 0003 = Issuer Reference Data
 * Example: “0003009MSI2VB5XX“ Field 104 Dataset 01 tag 0F VARC = Visa Additional Reason Code
 * Example: “VARC001V“
 */
public enum FieldsNFile {
  TestName(0, 0),
  Mtid(1, 2),
  Rec_centr(3, 2),
  Send_centr(5, 2),
  ISS_CMI(7, 8),
  Send_CMI(15, 8),
  Settl_CMI(23, 8),
  Send_ICA(31, 4),
  Rec_ICA(35, 4),
  Merchant(39, 7),
  Batch_nr(46, 7),
  Slip_nr(53, 7),
  Card(60, 19),
  Exp_date(79, 4),
  Date(83, 8),
  Time(91, 6),
  Tran_type(97, 2),
  Appr_code(99, 6),
  Appr_src(105, 1),
  Stan(106, 6),
  Ref_number(112, 12),
  Amount(124, 12),
  Cash_back(136, 12),
  Fee(148, 10),
  Currency(158, 3),
  Ccy_exp(161, 1),
  Sb_amount(162, 12),
  Sb_cshback(174, 12),
  Sb_fee(186, 10),
  Sbnk_ccy(196, 3),
  Sb_ccyexp(199, 1),
  Sb_cnvrate(200, 14),
  Sb_cnvdate(214, 8),
  I_amount(222, 12),
  I_cshback(234, 12),
  I_fee(246, 10),
  Ibnk_ccy(256, 3),
  I_ccyexp(259, 1),
  I_cnvrate(260, 14),
  I_cnvdate(274, 8),
  Abvr_name(282, 27),
  City(309, 15),
  Country(324, 3),
  PointCode(327, 12),
  MCC_code(339, 4), // MT
  Terminal(343, 1),
  Batch_id(344, 11),
  Settl_nr(355, 11),
  Settl_date(366, 8),
  Acqref_nr(374, 23),
  File_id(397, 18),
  Ms_number(415, 8),
  File_date(423, 8),
  Source_algo(431, 1),
  Err_code(432, 2),
  Term_nr(434, 8),
  ECMC_Fee(422, 8),
  Tran_info(450, 6),
  Pr_amount(456, 12),
  Pr_cshback(468, 12),
  Pr_fee(480, 10),
  Prnk_ccy(490, 3),
  Pr_ccyexp(493, 1),
  Pr_cnvrate(494, 12),
  Pr_cnvdate(508, 8),
  Reserved(516, 2),
  Proc_Class(518, 4),
  CARD_SEQ_NR(522, 3),
  Msg_type(525, 8), // MT
  Proc_code(533, 2), // MT
  Msg_category(535, 1), // MT
  MerchantCode(536, 15),
  MOTO_ECI_IND(551, 1),
  Filler(552, 23),
  FLD_043(575, 99),
  FLD_098(674, 25),
  Filler_(699, 56),
  FLD_104(755, 100),
  FLD_039(855, 3),
  FLD_SH6(858, 4),
  Batch_date(862, 8),
  Tr_fee(870, 10),
  FLD_040(880, 3),
  FLD_123_1(883, 1),
  EPI_42_48(884, 1),
  FLD_003(885, 6),
  MSC(891, 10),
  Account_nr(901, 35),
  EPI_42_48_FULL(936, 3),
  Other_Code(939, 20),
  FLD_015(959, 8),
  FLD_095(967, 99),
  EOL(1066, 1),

  // Master Card Electronic Commerce Security Level Indicator and UCAF Collection Indicator
  FLD43(333, 1), // (327,12) J=for recurrect

  FLD_126_master(984, 12), // for masterpass 15WPRD003999
  //    FLD_126_facilMC (1066,10), //check 15ETID001Y
  FILD_126_quasy(940, 7), // for quasy cash QUAS001

  CAVV(50, 31),

  // FOR aval
  FLD_126_facil(940, 10), // check 15ETID001Y
  FLD_126_visa(940, 14), // for visa checkout 15WPRD005VCIND
  FLD_122(940, 50), // 3DSV..SAAV..CAVV 982-50 RBI; 940 aval
  FLD_122_MC05(940, 80), // 3DSV..SAAV..CAVV 1027-rbi
  FLD_122_MC06(940, 80), // 3DSV..SAAV..CAVV 1027-rbi
  FLD_122_addendum(940, 80), // check include 3DSV..SAAV..CAVV 1015 rbi/940aval
  FLD_122_addendumMC06(1060, 80), // check include 3DSV..SAAV..CAVV
  FLD_126_reccurent(940, 10), // for recurrent include POSE001R 1046/940 990=aval

  // -----FOR RBI-----

  //    FLD_126_facil (940,10), //check 15ETID001Y 1067
  FLD_126_facilMC(940, 10), // check 15ETID001Y
  //    FLD_126_visa (940, 14),//for visa checkout 15WPRD005VCIND 1067
  //    FLD_122 (975, 50), //3DSV..SAAV..CAVV 982-50 RBI; 940 aval 982??? 1110
  //    FLD_122_MC05 (976, 80),//3DSV..SAAV..CAVV 1027-rbi 982
  //    FLD_122_MC06 (976, 80),//1027
  //    FLD_122_addendum (984, 80),//check include 3DSV..SAAV..CAVV 1015 rbi/940aval--1134
  //    FLD_122_addendumMC06 (976, 40),//check include 3DSV..SAAV..CAVV
  //    FLD_126_reccurent (998, 12),//for recurrent include POSE001R 1046/940 990=aval 1067
  FLD_122_addendum3DS(984, 80); // check include 3DSV..SAAV..CAVV 1015 rbi/940aval 1008

  private final int start;
  private final int length;

  FieldsNFile(int start, int length) {
    this.start = start;
    this.length = length;
  }

  public int getStart() {
    return this.start;
  }

  public int getLength() {
    return this.length;
  }
}
