package com.tsmc.ta.service.impl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsmc.ta.exception.UnauthorizedException;
import com.tsmc.ta.service.CachService;
import com.tsmc.ta.service.CreateTaService;
import com.tsmc.ta.service.MaintainTaService;
import com.tsmc.ta.to.MarketShareTo;

@Service("createTaService")
public class CreateTaServiceImpl implements CreateTaService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateTaServiceImpl.class);
	
	@Autowired
	private MaintainTaService maintainTaService;
	
	@Autowired
	private CachService cachService;
	
	@Override
	public String createTa(String name)
	{
//		LOGGER.info("createTa", name);
		StringBuffer sb = new StringBuffer();
//		sb.append(maintainTaService.maintainTa(name));
		if(true)
		{
			throw new UnauthorizedException("service");
		}
		sb.append("createTa" +name);
		Map<String, String> constMap = cachService.getConstMap();
		LOGGER.info("constMap : {}", constMap.size());
		return sb.toString();
	}
	
	@Override
	public void splitExcel(String fileName)
	{
		try
		{
			FileInputStream excelFile = new FileInputStream(new File(fileName));
	        Workbook workbook = new XSSFWorkbook(excelFile);
	        LOGGER.info("number of sheet : {}", workbook.getNumberOfSheets());
	        Sheet datatypeSheet = workbook.getSheetAt(0);
	        // Get Row at index 1
	        Row row = datatypeSheet.getRow(0);
	        
	        // Get the Cell at index 2 from the above row
	        Cell cell = row.getCell(0);
	        if (cell == null)
	        {
	            cell = row.createCell(0);
	        }

	        // Update the cell's value
	        cell.setCellType(CellType.NUMERIC);
	        cell.setCellValue(2);

	        // Write the output to the file
	        FileOutputStream fileOut = new FileOutputStream(fileName);
	        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
	        workbook.write(fileOut);
	        fileOut.close();
		}
        catch(FileNotFoundException e)
        {
        		LOGGER.error(e.getMessage(), e);
        }
		catch(IOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
        
	}
	@Override
	public String parsingExcel(String fileName)
	{
		Map<Integer, Object[]> rowObjAryMap = getExcelRow(fileName);
		//get year
		Map<String, List<String>> yearCompanysMap = new TreeMap<String, List<String>>();
		
		int frozenColumn = 5;
		List<MarketShareTo> marketShareToList = new ArrayList<MarketShareTo>();
		
		for(int k=1; k<rowObjAryMap.size()+1; k++)
		{
			Object[] rowObjAry = rowObjAryMap.get(k);
			if(k==1)
			{
				for(int i=frozenColumn; i<rowObjAry.length; i++)
				{
					if(rowObjAry[i] != null)
					{
						yearCompanysMap.put(StringUtils.replace(String.valueOf((Double)rowObjAry[i]), ".0", StringUtils.EMPTY), new ArrayList<String>());
					}
				}
				
			}
			else if(k==2)
			{
				Object[] yearObjAry = yearCompanysMap.keySet().toArray();
				String[] yearAry = Arrays.copyOf(yearObjAry, yearObjAry.length, String[].class);
				int splitYear = 0;
				for(int i=frozenColumn; i<rowObjAry.length; i++)
				{
					yearCompanysMap.get(yearAry[splitYear]).add((String)rowObjAry[i]);
					if(StringUtils.equals((String)rowObjAry[i], "Total"))
					{
						++splitYear;
					}
				}
			}
			else if(k>2 && StringUtils.isNotBlank((String)rowObjAry[0]))
			{
				int ratioColumn = frozenColumn;
				for (Map.Entry<String, List<String>> entry : yearCompanysMap.entrySet())
				{
					for(String srcCompany : entry.getValue())
					{
						MarketShareTo shareTo = new MarketShareTo();
						shareTo.setPlateForm((String)rowObjAry[0]);
						shareTo.setCategory((String)rowObjAry[1]);
						shareTo.setTargetLyr((String)rowObjAry[2]);
						shareTo.setSrcLyr((String)rowObjAry[3]);
						shareTo.setTargetCompany((String)rowObjAry[4]);
						shareTo.setYear(entry.getKey());
						shareTo.setSrcCompany(srcCompany);
						shareTo.setMarketRatio(BigDecimal.valueOf((Double)rowObjAry[ratioColumn]));
						LOGGER.info("shareTo : {}", shareTo.toString());
						marketShareToList.add(shareTo);
						++ratioColumn;
					}
				}
			}
			
//			if(k==2)
//			{
//				for(int i=0; i<rowObjAry.length; i++)
//				{
//					int yearNum = 0;
//					System.out.println("=========");
//					if(rowObjAry[i] != null && yearNum < 3)
//					{
//						yearAry[yearNum] = rowObjAry[i].toString();
//						System.out.println("yearAry : "+yearAry[yearNum]);
//						
//						++yearNum;
//					}
//				}
//			}
		}
		
		
		return "yes";
	}
	
	private Map<Integer, Object[]> getExcelRow(String fileName)
	{
		Map<Integer, Object[]> rowObjAryMap = new HashMap<Integer, Object[]>();
		try
		{
			FileInputStream excelFile = new FileInputStream(new File(fileName));
	        Workbook workbook = new XSSFWorkbook(excelFile);
	        LOGGER.info("number of sheet : {}", workbook.getNumberOfSheets());
//	        LOGGER.info("name of sheet : {}", workbook.getSheet("OEM"));
	        Sheet datatypeSheet = workbook.getSheet("Market Share");
	        Iterator<Row> iterator = datatypeSheet.iterator();
	        int rowNum = 0;
	        while (iterator.hasNext()) {
	        		//row 列
	        		//cell 格
	            Row currentRow = iterator.next();
	            ++rowNum;
	            
//	            LOGGER.info("row number : {}",currentRow.getPhysicalNumberOfCells());
	            Iterator<Cell> cellIterator = currentRow.iterator();
	            Object[] objAry = new Object[currentRow.getPhysicalNumberOfCells()];
	            rowObjAryMap.put(rowNum, objAry);
	            int cellNum = 0;
	            while (cellIterator.hasNext()) 
	            {
	               Cell currentCell = cellIterator.next();
	               objAry[cellNum] = getCellValue(currentCell);
	               System.out.print("cellNum : " + cellNum );
	               System.out.print(" cellValue : " + getCellValue(currentCell));
	               ++cellNum;
	                //getCellTypeEnum shown as deprecated for version 3.15
	                //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
	            }
	            System.out.println();
        		}
		}
        catch(FileNotFoundException e)
        {
        		LOGGER.error(e.getMessage(), e);
        }
		catch(IOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		return rowObjAryMap;
	}
	
	private Object getCellValue(Cell currentCell)
	{
		if (currentCell.getCellTypeEnum() == CellType.STRING) {
			return currentCell.getStringCellValue();
        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
        		return currentCell.getNumericCellValue();
        }
        else if(currentCell.getCellTypeEnum() == CellType.FORMULA)
        {
        	 if(currentCell.getCachedFormulaResultTypeEnum() == CellType.STRING)
        	 {
        		 LOGGER.info("Formula : {}", currentCell.getStringCellValue());
        		 return currentCell.getStringCellValue();
        	 }
        	 else if(currentCell.getCachedFormulaResultTypeEnum() == CellType.NUMERIC)
        	 {
        		 LOGGER.info("Formula : {}", currentCell.getNumericCellValue());
        		 return currentCell.getNumericCellValue();
        	 }
        }
		return null;
	}

}
