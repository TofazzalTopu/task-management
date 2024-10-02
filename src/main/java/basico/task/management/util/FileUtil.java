package basico.task.management.util;

import basico.task.management.dto.CategoryUploadDTO;
import basico.task.management.exception.NotAllowedException;
import basico.task.management.exception.NotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERS = {"Programme", "Division", "Region/District", "Area/Upazila", "Union", "Village",
            "Branch", "Branch Code", "VO Code", "Member Name", "Member No", "Gender", "NID", "Contact Number",
            "Profession", "Total Family Member", "Did the participant receive contribution from BRAC", "Relationship With Client(bkash wallet)",
            "Bkash Wallet No", "Amount", "ParentRequestId", "rejectionReason"};
    static String SHEET = "RequestData";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<CategoryUploadDTO> readCategoryUploadDTO(InputStream is) throws Exception {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<CategoryUploadDTO> categoryUploadDTOArrayList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                CategoryUploadDTO categoryUploadDTO = new CategoryUploadDTO();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            categoryUploadDTO.setCategoryName(readCellValue(currentCell));
                            break;
                        case 1:
                            categoryUploadDTO.setSubCategoryName(readCellValue(currentCell));
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }
                categoryUploadDTOArrayList.add(categoryUploadDTO);
            }

            workbook.close();

            categoryUploadDTOArrayList = categoryUploadDTOArrayList.stream().filter(data -> (!data.getCategoryName().equals(null) && !data.getCategoryName().equals("") && (!data.getSubCategoryName().equals(null) && !data.getSubCategoryName().equals("")))).collect(Collectors.toList());
            if (categoryUploadDTOArrayList.isEmpty()) {
                throw new NotFoundException("no.data.exist.in.the.file");
            }
            return categoryUploadDTOArrayList;
        } catch (IOException e) {
            throw new NotAllowedException("fail to parse Excel file: " + e.getMessage());
        }

    }

    private static String readCellValue(Cell currentCell) {
        try {
            if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                Double data = currentCell.getNumericCellValue();
                if (data.toString().contains("E") || data.toString().contains("#")) {
                    BigInteger value = new BigDecimal(currentCell.getNumericCellValue()).toBigInteger();
                    return new BigDecimal(value).toString();
                } else {
                    BigDecimal cellValue = new BigDecimal(data);
                    return cellValue.doubleValue() + "";
                }
            } else {
                return currentCell.getStringCellValue();
            }
        } catch (Exception e) {

        }
        return "";
    }
}
