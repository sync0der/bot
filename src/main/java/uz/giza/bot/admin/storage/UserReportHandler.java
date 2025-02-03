package uz.giza.bot.admin.storage;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.giza.bot.entity.User;
import uz.giza.bot.service.SendMessageService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReportHandler {
    private final SendMessageService sendMessageService;

    @Async
    public void sendUserReport(Long chatId, String fileName, String caption, byte[] data) {
        File tempFile;
        try {
            tempFile = File.createTempFile(fileName, ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(data);
            }

            SendDocument document = SendDocument.builder()
                    .chatId(chatId)
                    .caption(caption)
                    .document(new InputFile(tempFile, fileName + ".xlsx"))
                    .build();

            sendMessageService.sendFile(document);

            tempFile.delete();

        } catch (IOException e) {
            throw new RuntimeException("Error sending Excel file", e);

        }
    }

    public byte[] generateUserExcelReport(List<User> users, String sheetName) {

        try (Workbook workbook = new XSSFWorkbook()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            String[] columns = {"№", "Ism", "Telefon raqam", "Username", "Chat_Id", "Utm tag", "Status", "Target course", "Registratsiya vaqti"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowNum = 1;
            String targetCourse;
            for (User user : users) {
                targetCourse = user.getTargetCourse() != null
                        ? user.getTargetCourse().getName()
                        : null;
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(user.getFullName());
                row.createCell(2).setCellValue(user.getPhoneNumber());
                row.createCell(3).setCellValue(user.getUserName());
                row.createCell(4).setCellValue(user.getChatId());
                row.createCell(5).setCellValue(user.getUtmTag());
                row.createCell(6).setCellValue(user.getStatus().toString());
                row.createCell(7).setCellValue(targetCourse);
                row.createCell(8).setCellValue(user.getRegisteredAt().toString());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }

    }


    public CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Times New Roman");
        cellStyle.setFont(font);
        return cellStyle;
    }

}
