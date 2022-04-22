package io.github.isaac.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.isaac.model.AndroidString;
import io.github.isaac.model.SupportedLanguages;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InPutStringAction extends AnAction {

    public static final int ANDROID_STRING_ID_INDEX = 0;
    public static final int ANDROID_STRING_CHINESE_SIMPLIFIED_INDEX = 1;
    public static final int ANDROID_STRING_CHINESE_TRADITIONAL_INDEX = 2;
    public static final int ANDROID_STRING_ENGLISH_INDEX = 3;
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

    }

    private void inputExcel() {
        List<SupportedLanguages> languagesList = SupportedLanguages.getAllSupportedLanguages();
        HashMap<SupportedLanguages, List<AndroidString>> hashMap = new HashMap<>();
        for (SupportedLanguages language :languagesList) {
            List<AndroidString> stringList = new ArrayList<>();
            hashMap.put(language, stringList);
        }
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("")));
            XSSFSheet sheet = workbook.getSheetAt(0);
            String id;
            String cnChineseValue;
            String hkChineseValue;
            String englishValue;
            for (int i =1;i<= sheet.getLastRowNum();i++) {
                XSSFRow row =sheet.getRow(i);
                id = row.getCell(ANDROID_STRING_ID_INDEX).getStringCellValue();
                cnChineseValue = row.getCell(ANDROID_STRING_CHINESE_SIMPLIFIED_INDEX).getStringCellValue();
                hkChineseValue = row.getCell(ANDROID_STRING_CHINESE_TRADITIONAL_INDEX).getStringCellValue();
                englishValue = row.getCell(ANDROID_STRING_ENGLISH_INDEX).getStringCellValue();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
