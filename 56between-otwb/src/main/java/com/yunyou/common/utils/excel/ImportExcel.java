package com.yunyou.common.utils.excel;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.utils.Reflections;
import com.yunyou.common.utils.excel.annotation.ExcelField;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 */
public class ImportExcel {

    private static Logger logger = LoggerFactory.getLogger(ImportExcel.class);

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 标题行号
     */
    private int headerNum;

    /**
     * 构造函数
     *
     * @param fileName  导入文件，读取第一个工作表
     * @param headerNum 标题行号，数据行号=标题行号+1
     */
    public ImportExcel(String fileName, int headerNum) throws IOException {
        this(new File(fileName), headerNum);
    }

    /**
     * 构造函数
     *
     * @param file      导入文件对象，读取第一个工作表
     * @param headerNum 标题行号，数据行号=标题行号+1
     */
    public ImportExcel(File file, int headerNum) throws IOException {
        this(file, headerNum, 0);
    }

    /**
     * 构造函数
     *
     * @param fileName   导入文件名称
     * @param headerNum  标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     */
    public ImportExcel(String fileName, int headerNum, int sheetIndex) throws IOException {
        this(new File(fileName), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     *
     * @param file       导入文件对象
     * @param headerNum  标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     */
    public ImportExcel(File file, int headerNum, int sheetIndex) throws IOException {
        this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     *
     * @param multipartFile 导入文件对象
     * @param headerNum     标题行号，数据行号=标题行号+1
     * @param sheetIndex    工作表编号
     */
    public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex) throws IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
    }

    /**
     * 构造函数
     *
     * @param fileName   导入文件对象
     * @param headerNum  标题行号，数据行号=标题行号+1
     * @param sheetIndex 工作表编号
     */
    public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            throw new GlobalException("导入文档不存在!");
        } else if (fileName.toLowerCase().endsWith(".xls")) {
            this.wb = new HSSFWorkbook(is);
        } else if (fileName.toLowerCase().endsWith(".xlsx")) {
            this.wb = new XSSFWorkbook(is);
        } else {
            throw new GlobalException("文档格式不正确!");
        }
        if (this.wb.getNumberOfSheets() < sheetIndex) {
            throw new GlobalException("文档中没有工作表!");
        }
        this.sheet = this.wb.getSheetAt(sheetIndex);
        this.headerNum = headerNum;
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize success.");
        }
    }

    /**
     * 获取行对象
     */
    public Row getRow(int rowNum) {
        return this.sheet.getRow(rowNum);
    }

    /**
     * 获取数据行号
     */
    public int getDataRowNum() {
        return headerNum + 1;
    }

    /**
     * 获取最后一个数据行号
     */
    public int getLastDataRowNum() {
        return this.sheet.getLastRowNum() + headerNum;
    }

    /**
     * 获取最后一个列号
     */
    public int getLastCellNum() {
        return this.getRow(headerNum).getLastCellNum();
    }

    /**
     * 获取单元格值
     *
     * @param row    获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column) {
        Cell cell = row.getCell(column);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            // 当excel中的数据为数值或日期是需要特殊处理
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                // 日期
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
            } else {
                // 数值
                NumberFormat nf = NumberFormat.getInstance();
                // true时的格式：1,234,567,890
                nf.setGroupingUsed(false);
                // 设置数值的小数部分允许的最大位数
                nf.setMaximumFractionDigits(8);
                // 数值类型的数据为小数时，所以需要转换一下
                return nf.format(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return cell.getErrorCellValue();
        }
        return null;
    }

    public <E> List<Object[]> getAnnotationList(Class<E> cls, int... groups) {
        List<Object[]> annotationList = Lists.newArrayList();
        // Get annotation field
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            // 没有注解ExcelField 或者 ExcelField的type不含导入，跳过
            if (ef == null || !(ef.type() == 0 || ef.type() == 2)) {
                continue;
            }
            if (groups == null || groups.length <= 0) {
                annotationList.add(new Object[]{ef, f});
            } else if (ef.groups() != null && ef.groups().length > 0) {
                int[] efGroups = ef.groups();
                inGroup:
                for (int g : groups) {
                    for (int efg : efGroups) {
                        if (g == efg) {
                            annotationList.add(new Object[]{ef, f});
                            break inGroup;
                        }
                    }
                }
            }
        }
        // Get annotation method
        Method[] ms = cls.getDeclaredMethods();
        for (Method m : ms) {
            ExcelField ef = m.getAnnotation(ExcelField.class);
            // 没有注解ExcelField 或者 ExcelField的type不含导入，跳过
            if (ef == null || !(ef.type() == 0 || ef.type() == 2)) {
                continue;
            }
            if (groups == null || groups.length <= 0) {
                annotationList.add(new Object[]{ef, m});
            } else if (ef.groups() != null && ef.groups().length > 0) {
                int[] efGroups = ef.groups();
                inGroup:
                for (int g : groups) {
                    for (int efg : efGroups) {
                        if (g == efg) {
                            annotationList.add(new Object[]{ef, m});
                            break inGroup;
                        }
                    }
                }
            }
        }
        // Field sorting
        annotationList.sort(Comparator.comparingInt(o -> ((ExcelField) o[0]).sort()));
        return annotationList;
    }

    /**
     * 获取导入数据列表
     *
     * @param cls    导入对象类型
     * @param groups 导入分组
     */
    public <E> List<E> getDataList(Class<E> cls, int... groups) throws InstantiationException, IllegalAccessException {
        List<Object[]> annotationList = getAnnotationList(cls, groups);
        // Get excel data
        List<E> dataList = Lists.newArrayList();
        for (int i = this.getDataRowNum(); i <= this.getLastDataRowNum(); i++) {
            Row row = this.getRow(i - 1);

            E e = cls.newInstance();
            int column = 0;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < annotationList.size(); j++) {
                column++;
                Object[] os = annotationList.get(j);
                Object o1 = os[1];
                ExcelField ef = (ExcelField) os[0];
                Object val = this.getCellValue(row, j);
                if (val == null) {
                    continue;
                }
                // Get param type and type cast
                Class<?> valType = Class.class;
                if (o1 instanceof Field) {
                    valType = ((Field) o1).getType();
                } else if (o1 instanceof Method) {
                    Method method = ((Method) o1);
                    if ("get".equals(method.getName().substring(0, 3))) {
                        valType = method.getReturnType();
                    } else if ("set".equals(method.getName().substring(0, 3))) {
                        valType = ((Method) o1).getParameterTypes()[0];
                    }
                }
                // 如果导入的java对象，需要在这里自己进行变换。
                if (valType == String.class) {
                    val = getStringValue(val, ef, i, column);
                } else if (valType == Integer.class) {
                    val = getIntegerValue(val, ef, i, column);
                } else if (valType == Long.class) {
                    val = getLongValue(val, ef, i, column);
                } else if (valType == Double.class) {
                    val = getDoubleValue(val, ef, i, column);
                } else if (valType == Float.class) {
                    val = getFloatValue(val, ef, i, column);
                } else if (valType == BigDecimal.class) {
                    val = getBigDecimalValue(val, ef, i, column);
                } else if (valType == Date.class) {
                    val = getDateValue(val, ef, i, column);
                } else if (valType == User.class) {
                    val = UserUtils.getByUserName(val.toString());
                } else if (valType == Office.class) {
                    val = UserUtils.getByOfficeName(val.toString());
                } else if (valType == Area.class) {
                    val = UserUtils.getByAreaName(val.toString());
                } else {
                    val = getOtherValue(val, ef, i, column, valType);
                }
                // set entity value
                if (o1 instanceof Field) {
                    Reflections.invokeSetter(e, ((Field) o1).getName(), val);
                } else if (o1 instanceof Method) {
                    String methodName = ((Method) o1).getName();
                    if ("get".equals(methodName.substring(0, 3))) {
                        methodName = "set" + StringUtils.substringAfter(methodName, "get");
                    }
                    Reflections.invokeMethod(e, methodName, new Class[]{valType}, new Object[]{val});
                }
                sb.append(val).append(", ");
            }
            dataList.add(e);
            if (logger.isDebugEnabled()) {
                logger.debug("Read success: [{}] {}", i, sb);
            }
        }
        return dataList;
    }

    private Integer getIntegerValue(Object v, ExcelField ef, int rowNum, int colNum) {
        if (v instanceof Integer) {
            return (Integer) v;
        }
        String s = v.toString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return Double.valueOf(s).intValue();
        } catch (NumberFormatException e) {
            throw new GlobalException(MessageFormat.format("第[{0}, {1}]列格式不符合，要求为整数", rowNum, colNum));
        }
    }

    private Long getLongValue(Object v, ExcelField ef, int rowNum, int colNum) {
        if (v instanceof Long) {
            return (Long) v;
        }
        String s = v.toString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return Double.valueOf(s).longValue();
        } catch (NumberFormatException e) {
            throw new GlobalException(MessageFormat.format("第[{0}, {1}]列格式不符合，要求为整数", rowNum, colNum));
        }
    }

    private Float getFloatValue(Object v, ExcelField ef, int rowNum, int colNum) {
        if (v instanceof Float) {
            return (Float) v;
        }
        String s = v.toString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return Float.valueOf(s);
        } catch (NumberFormatException e) {
            throw new GlobalException(MessageFormat.format("第[{0}, {1}]列格式不符合，要求为小数", rowNum, colNum));
        }
    }

    private Double getDoubleValue(Object v, ExcelField ef, int rowNum, int colNum) {
        if (v instanceof Double) {
            return (Double) v;
        }
        String s = v.toString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return Double.valueOf(s);
        } catch (NumberFormatException e) {
            throw new GlobalException(MessageFormat.format("第[{0}, {1}]列格式不符合，要求为小数", rowNum, colNum));
        }
    }

    private BigDecimal getBigDecimalValue(Object v, ExcelField ef, int rowNum, int colNum) {
        if (v instanceof BigDecimal) {
            return (BigDecimal) v;
        }
        String s = v.toString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            throw new GlobalException(MessageFormat.format("第[{0}, {1}]列格式不符合，要求为数字", rowNum, colNum));
        }
    }

    private Date getDateValue(Object v, ExcelField ef, int rowNum, int colNum) {
        if (v instanceof Date) {
            return (Date) v;
        }
        String s = v.toString();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return new SimpleDateFormat(ef.format()).parse(s);
        } catch (ParseException e) {
            throw new GlobalException(MessageFormat.format("第[{0}, {1}]列格式不符合，要求格式【{2}】", rowNum, colNum, ef.format()));
        }
    }

    private String getStringValue(Object v, ExcelField ef, int rowNum, int colNum) {
        String s;
        if (v instanceof String) {
            s = (String) v;
        } else {
            s = v.toString();
        }
        if (StringUtils.isBlank(ef.dictType())) {
            return StringUtils.endsWith(s, ".0") ? StringUtils.substringBefore(s, ".0") : s;
        }
        String dictValue = DictUtils.getDictValue(s, ef.dictType(), null);
        if (StringUtils.isNotBlank(dictValue)) {
            return dictValue;
        }
        String dictLabel = DictUtils.getDictLabel(s, ef.dictType(), null);
        if (StringUtils.isNotBlank(dictLabel)) {
            return s;
        }
        throw new GlobalException(MessageFormat.format("第[{0}, {1}]列字典类型【{2}】不包含该键值", rowNum, colNum, ef.dictType()));
    }

    private Object getOtherValue(Object v, ExcelField ef, int rowNum, int colNum, Class<?> valType) {
        if (ef.fieldType() != Class.class) {
            try {
                return ef.fieldType().getMethod("getValue", String.class).invoke(null, v.toString());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.info("Get cell value [{}, {}] error: {}", rowNum, colNum, e.toString());
            }
        } else {
            try {
                return Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), "fieldtype." + valType.getSimpleName() + "Type"))
                        .getMethod("getValue", String.class).invoke(null, v.toString());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                logger.info("Get cell value [{}, {}] error: {}", rowNum, colNum, e.toString());
            }
        }
        return null;
    }

}
