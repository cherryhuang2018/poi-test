package com.kcc.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.enums.Unit;
import com.kcc.pojo.Asp;
import com.kcc.pojo.AttachRate;
import com.kcc.pojo.EndUser;
import com.kcc.pojo.Gdpw;
import com.kcc.pojo.Mss;
import com.kcc.pojo.Odm;
import com.kcc.pojo.Oem;
import com.kcc.pojo.Sys;
import com.kcc.utils.Constants;
import com.kcc.utils.JsonUtils;

@Service("ParseService")
public class ParseService {

  @Autowired
  private FileService fileService;

  public void parseExcel(String folderPath) {
    List<String> filePaths = fileService.getFilePath(folderPath);

    for (String filePath : filePaths) {
      try (InputStream is = new FileInputStream(filePath)) {
        Workbook workbook = WorkbookFactory.create(is);

        List<EndUser> endUsers = handleEndUserSheet(workbook, "End User");
        System.out.println(JsonUtils.toJson(endUsers));

        List<Oem> oems = handleOemSheet(workbook, "OEM");
        System.out.println(JsonUtils.toJson(oems));
        
        List<Odm> odms = handleOdmSheet(workbook, "ODM");
        System.out.println(JsonUtils.toJson(odms));
        
      } catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
        e.printStackTrace();
      }
    }
  }

  private List<EndUser> handleEndUserSheet(Workbook workbook, String sheetName) {
    int sheetIndex = workbook.getSheetIndex(sheetName);
    Sheet sheet = workbook.getSheetAt(sheetIndex);

    int rowIndex = 0;
    Row header = sheet.getRow(rowIndex);
    if (header == null) {
      header = sheet.getRow(++rowIndex);
    }

    Map<Integer, String> map = new TreeMap<>();
    boolean isHeaderValid = checkHeader(header, sheetName, map);
    if (!isHeaderValid) {
      // END USER header不正確
    }

    List<EndUser> endUsers = new ArrayList<>();
    Row valueRow = sheet.getRow(++rowIndex);
    while (valueRow != null) {
      Row row = sheet.getRow(rowIndex);

      int cellIndex = 1;
      Cell cell = row.getCell(cellIndex);
      EndUser endUser = new EndUser();
      Sys system = new Sys();
      while (cell != null) {
        String headerValue = map.get(cellIndex);
        if ("PLATFORM".equals(headerValue)) {
          endUser.setPlatform((String) getCellValue(cell));
        } else if ("CATEGORY".equals(headerValue)) {
          endUser.setCategory((String) getCellValue(cell));
        } else if ("APPLICATION".equals(headerValue)) {
          endUser.setApplication((String) getCellValue(cell));
        } else if ("LEVEL".equals(headerValue)) {
          endUser.setLevel((String) getCellValue(cell));
        } else if ("YEAR".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.0f", value);
          endUser.setYear(BigInteger.valueOf(Long.parseLong(str)));
        } else if ("END USER".equals(headerValue)) {
          endUser.setEndUserName((String) getCellValue(cell));
        } else if ("SYSTEM UNIT".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.2f", value);
          system.setNums(new BigDecimal(str));
        } else if ("UNIT".equals(headerValue)) {
          system.setUnit(Unit.valueOf((String) getCellValue(cell)));
        }
        cell = row.getCell(++cellIndex);
      }
      endUser.setSys(system);
      endUsers.add(endUser);
      valueRow = sheet.getRow(++rowIndex);
    }

    return endUsers;
  }

  private List<Oem> handleOemSheet(Workbook workbook, String sheetName) {
    int sheetIndex = workbook.getSheetIndex(sheetName);
    Sheet sheet = workbook.getSheetAt(sheetIndex);

    int rowIndex = 0;
    Row header = sheet.getRow(rowIndex);
    if (header == null) {
      header = sheet.getRow(++rowIndex);
    }

    Map<Integer, String> map = new TreeMap<>();
    boolean isHeaderValid = checkHeader(header, sheetName, map);
    if (!isHeaderValid) {
      // END USER header不正確
    }

    List<Oem> oems = new ArrayList<>();
    Row valueRow = sheet.getRow(++rowIndex);
    while (valueRow != null) {
      Row row = sheet.getRow(rowIndex);

      int cellIndex = 1;
      Cell cell = row.getCell(cellIndex);
      Oem oem = new Oem();
      Sys system = new Sys();
      Asp asp = new Asp();
      while (cell != null) {
        String headerValue = map.get(cellIndex);
        if ("PLATFORM".equals(headerValue)) {
          oem.setPlatform((String) getCellValue(cell));
        } else if ("CATEGORY".equals(headerValue)) {
          oem.setCategory((String) getCellValue(cell));
        } else if ("APPLICATION".equals(headerValue)) {
          oem.setApplication((String) getCellValue(cell));
        } else if ("LEVEL".equals(headerValue)) {
          oem.setLevel((String) getCellValue(cell));
        } else if ("YEAR".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.0f", value);
          oem.setYear(BigInteger.valueOf(Long.parseLong(str)));
        } else if ("OEM".equals(headerValue)) {
          oem.setOemName((String) getCellValue(cell));
        } else if ("SYSTEM UNIT".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.2f", value);
          system.setNums(new BigDecimal(str));
        } else if ("UNIT".equals(headerValue)) {
          if (system.getUnit() == null) {
            system.setUnit(Unit.valueOf((String) getCellValue(cell)));
          } else {
            asp.setUnit(Unit.valueOf((String) getCellValue(cell)));
          }
        } else if ("ASP".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.2f", value);
          asp.setNums(new BigDecimal(str));
        }
        cell = row.getCell(++cellIndex);
      }
      oem.setSys(system);
      oems.add(oem);
      valueRow = sheet.getRow(++rowIndex);
    }

    return oems;
  }

  private List<Odm> handleOdmSheet(Workbook workbook, String sheetName) {
    int sheetIndex = workbook.getSheetIndex(sheetName);
    Sheet sheet = workbook.getSheetAt(sheetIndex);

    int rowIndex = 0;
    Row header = sheet.getRow(rowIndex);
    if (header == null) {
      header = sheet.getRow(++rowIndex);
    }

    Map<Integer, String> map = new TreeMap<>();
    boolean isHeaderValid = checkHeader(header, sheetName, map);
    if (!isHeaderValid) {
      // END USER header不正確
    }

    List<Odm> odms = new ArrayList<>();
    Row valueRow = sheet.getRow(++rowIndex);
    while (valueRow != null) {
      Row row = sheet.getRow(rowIndex);

      int cellIndex = 1;
      Cell cell = row.getCell(cellIndex);
      Odm odm = new Odm();
      Mss mss = new Mss();
      Asp asp = new Asp();
      AttachRate rate = new AttachRate();
      Gdpw gdpw = new Gdpw();
      while (cell != null) {
        String headerValue = map.get(cellIndex);
        if ("PLATFORM".equals(headerValue)) {
          odm.setPlatform((String) getCellValue(cell));
        } else if ("CATEGORY".equals(headerValue)) {
          odm.setCategory((String) getCellValue(cell));
        } else if ("APPLICATION".equals(headerValue)) {
          odm.setApplication((String) getCellValue(cell));
        } else if ("LEVEL".equals(headerValue)) {
          odm.setLevel((String) getCellValue(cell));
        } else if ("YEAR".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.0f", value);
          odm.setYear(BigInteger.valueOf(Long.parseLong(str)));
        } else if ("ODM".equals(headerValue)) {
          odm.setOdmName((String) getCellValue(cell));
        } else if ("MSS".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.2f", value);
          mss.setNums(new BigDecimal(str));
        } else if ("ASP".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.0f", value);
          asp.setNums(new BigDecimal(str));
        } else if ("ATTACH RATE".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.2f", value);
          rate.setNums(new BigDecimal(str));
        } else if ("GDPW".equals(headerValue)) {
          Double value = (Double) getCellValue(cell);
          String str = String.format("%.2f", value);
          gdpw.setNums(new BigDecimal(str));
        } else if ("UNIT".equals(headerValue)) {
          if (mss.getUnit() == null) {
            mss.setUnit(Unit.valueOf((String) getCellValue(cell)));
          }

          if (asp.getUnit() == null) {
            asp.setUnit(Unit.valueOf((String) getCellValue(cell)));
          }
          
          if (rate.getUnit() == null) {
            rate.setUnit(Unit.PERCENTAGE);
          }
          
          if (gdpw.getUnit() == null) {
            gdpw.setUnit(Unit.valueOf((String) getCellValue(cell)));
          }
        }
        cell = row.getCell(++cellIndex);
      }
      odm.setMss(mss);
      odm.setAsp(asp);
      odm.setAttachRate(rate);
      odm.setGdpw(gdpw);
      odms.add(odm);
      valueRow = sheet.getRow(++rowIndex);
    }

    return odms;
  }

  private boolean checkHeader(Row header, String sheetName, Map<Integer, String> map) {
    Iterator<Cell> cells = header.cellIterator();
    StringBuilder headerSb = new StringBuilder();

    int i = 0;
    while (cells.hasNext()) {
      Cell cell = cells.next();
      String cellValue = cell.getStringCellValue().trim().toUpperCase();
      headerSb.append(cellValue).append(Constants.UNDERLINE);

      map.put(++i, cellValue);
    }

    headerSb.setLength(headerSb.length() - 1);

    if ("End User".equals(sheetName)) {
      return Constants.ENDUSER_HEADER.equals(headerSb.toString());
    } else if ("OEM".equals(sheetName)) {
      return Constants.OEM_HEADER.equals(headerSb.toString());
    } else if ("ODM".equals(sheetName)) {
      return Constants.ODM_HEADER.equals(headerSb.toString());
    }

    return false;
  }

  private Object getCellValue(Cell cell) {
    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
      return cell.getNumericCellValue();
    } else if (cell.getCellTypeEnum() == CellType.STRING) {
      return cell.getStringCellValue();
    }

    return null;
  }
}
