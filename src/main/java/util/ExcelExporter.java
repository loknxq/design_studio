package util;

import model.DesignOrder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static void exportOrders(List<DesignOrder> orders, String fileName) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Заказы");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Название");
            header.createCell(2).setCellValue("Описание");
            header.createCell(3).setCellValue("Цена");
            header.createCell(4).setCellValue("Статус");
            header.createCell(5).setCellValue("Дата создания");
            header.createCell(6).setCellValue("ID клиента");
            header.createCell(7).setCellValue("ID дизайнера");

            int rowNum = 1;
            for (DesignOrder order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getTitle());
                row.createCell(2).setCellValue(order.getDescription());
                row.createCell(3).setCellValue(order.getPrice().doubleValue());
                row.createCell(4).setCellValue(order.getStatus().name());
                row.createCell(5).setCellValue(order.getCreatedDate().toString());
                row.createCell(6).setCellValue(order.getClientId());
                row.createCell(7).setCellValue(order.getDesignerId());
            }

            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка экспорта в Excel: " + e.getMessage(), e);
        }
    }
}