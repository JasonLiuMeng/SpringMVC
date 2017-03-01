package com.cml.common;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.cml.bean.Person;

public class CSVFileUtil {

	/** CSV文件列分隔符 */
	private static final String CSV_COLUMN_SEPARATOR = ",";

	/** CSV文件列分隔符 */
	private static final String CSV_RN = "\r\n";

	public static boolean doExport(List<Person> persons, String[] colNames, OutputStream os ) {
		try {
			StringBuffer buf = new StringBuffer();
			for( String str : colNames ){
				buf.append(str).append(CSV_COLUMN_SEPARATOR);
			}
			buf.append(CSV_RN);
			Person p = null;
			for (int i = 0; i < persons.size(); i++) {
				p = persons.get(i);
				buf.append( i+1 ).append(CSV_COLUMN_SEPARATOR).append(p.getP_name()).append(CSV_COLUMN_SEPARATOR).append(p.getMorningStr())
						.append(CSV_COLUMN_SEPARATOR).append(p.getAfternoonStr()).append(CSV_COLUMN_SEPARATOR)
						.append(p.getDateStr()).append(CSV_COLUMN_SEPARATOR).append(CSV_RN);
			}
			os.write(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF });  
			os.write(buf.toString().getBytes("utf-8"));
			os.flush();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void responseSetProperties(String fileName, HttpServletResponse response)
			throws UnsupportedEncodingException {
		// 设置文件后缀
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fn = fileName + sdf.format(new Date()).toString() + ".csv";
		// 读取字符编码
		String utf = "UTF-8";

		// 设置响应
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding(utf);
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=30");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fn, utf));
	}
}
