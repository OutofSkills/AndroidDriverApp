package com.intelligentcarmanagement.carmanagementapp.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.intelligentcarmanagement.carmanagementapp.utils.FileManager;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelExporter {
    private static final String MOTION_DATA_FOLDER = "Motion Data";
    private static final String RESULT_DATA_FOLDER = "Result Data";

    private File filePath;
    Context context;

    public ExcelExporter(Context context)
    {
        this.context = context;
    }

    public void exportMotionData(List<String[]> motionData, List<String> columns) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Data Sheet");

        HSSFRow row0 = hssfSheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            HSSFCell cell = row0.createCell(i);
            cell.setCellValue(columns.get(i));
        }

        for(int i = 0; i < motionData.size(); ++i)
        {
            HSSFRow row = hssfSheet.createRow(i+1);
            for(int j = 0; j < motionData.get(i).length; ++j)
            {
                HSSFCell cell = row.createCell(j);
                cell.setCellValue((String) motionData.get(i)[j]);
            }
        }

        FileOutputStream fos = null;
        try {
            String fileName = new SimpleDateFormat("'MotionData_'yyyyMMddHHmmss'.xls'").format(new Date());
            File dirPath = FileManager.commonDocumentDirPath("MotionData");

            if(dirPath != null)
            {
                filePath = new File(dirPath + "/" + fileName);
                fos = new FileOutputStream(filePath);
                hssfWorkbook.write(fos);
            }else {
                Toast.makeText(context, "Unable to save the file " + fileName + ".", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, "Saved at: " + filePath, Toast.LENGTH_SHORT).show();
        }
    }

    public void exportResultData(List<String[]> resultData) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Data Sheet");

        HSSFRow row0 = hssfSheet.createRow(0);
        HSSFCell cell1 = row0.createCell(0);
        cell1.setCellValue("Sudden Acceleration");
        HSSFCell cell2 = row0.createCell(1);
        cell2.setCellValue("Sudden Left Turn");
        HSSFCell cell3 = row0.createCell(2);
        cell3.setCellValue("Sudden Right Turn");
        HSSFCell cell4 = row0.createCell(3);
        cell4.setCellValue("Sudden break");

        for(int i = 0; i < resultData.size(); ++i)
        {
            HSSFRow row = hssfSheet.createRow(i+1);
            for(int j = 0; j < resultData.get(i).length; ++j)
            {
                HSSFCell cell = row.createCell(j);
                cell.setCellValue((String) resultData.get(i)[j]);
            }
        }

        FileOutputStream fos = null;
        try {
            String fileName = new SimpleDateFormat("'ResultData_'yyyyMMddHHmmss'.xls'").format(new Date());
            File dirPath = FileManager.commonDocumentDirPath("ResultData");

            if(dirPath != null)
            {
                filePath = new File(dirPath + "/" + fileName);
                fos = new FileOutputStream(filePath);
                hssfWorkbook.write(fos);
            }else {
                Toast.makeText(context, "Unable to save the file " + fileName + ".", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, "Saved at: " + filePath, Toast.LENGTH_SHORT).show();
        }
    }
}