package org.web.acesslog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.web.acesslog.parser.ApacheParserImpl;
import org.web.acesslog.parser.IParser;


/**
 * Main/Init for the Utility, we try to support a list of web servers but at the moment only tomcat access log is supported.
 * It's important to use the same access format used in the access log & this Utility to get an accurate output.
 * 
 * No support will be provided at the moment, this software is open for reuse/modification/distribution.
 * 
 * @author Senthil Balakrishnan
 */
public class AccessReader {

	/**
	 * 
	 */
	private static String format = "Time Taken: %T %h %l %u %t %r %s %b";

	/**
	 * 
	 */
	private static IParser formatter;

	/**
	 * 
	 */
	private static String fileName = "C:\\sen\\access.log.txt";

	/**
	 * 
	 */
	String line = "Time Taken: 0.034 10.40.16.140 - - [21/Jul/2011:17:28:02 -0400] \"POST /bagmanager/httpGateway/call/BagService/1.1/null HTTP/1.1\" 200 2049";

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("######### Access Log Reader Started #############");
		// Parser class
		formatter = new ApacheParserImpl(format);
		// Read/Parser logic
		List<Access> accessList = read(fileName);
		// Stats computation
		Map<String, Stat> stats = calcStats(accessList);
		// Writing Stats to Log
		writeTxtStats(stats);
		// Yet to be implemented
		// writeCSVStats();
		System.out.println("######### Access Log Reader Exited #############");
	}

	/**
	 * @param accessList
	 */
	private static Map<String, Stat> calcStats(List<Access> accessList) {
		System.out.println("### Calculating Stats...\n");
		Map<String, Stat> stats = new HashMap<String, Stat>();
		for (Access access : accessList) {
			// URL is mandatory, this the only way to uniqiely identify a
			// request.
			String key = access.getFirstLineOfRequest();
			Stat stat;
			//System.out.println(access);
			if (stats.containsKey(key)) {
				stat = stats.get(key);
			} else {
				stat = new Stat(key);
			}
			stat.addStat(Double.parseDouble(access.getTimeTakeInSec()),
					access.getDateTime());
			stats.put(key, stat);
			//System.out.println("Stat:" + stat);
		}
		return stats;
	}
	
	/**
	 * @param stats
	 * @throws IOException 
	 */
	private static void writeTxtStats(Map<String, Stat> stats) throws IOException {
		System.out.println("### Writing Stats..\n");
		File file = null;
		FileWriter writer = null;
		try {
			file = new File("accesslog_report.txt");
			System.out.println("### Stats file:"+file.getAbsolutePath());
			writer = new FileWriter(file);
			Set<Map.Entry<String, Stat>> entrySet = stats.entrySet();
			for (Map.Entry<String, Stat> entry : entrySet) {
				writer.write(entry.getValue().toString());
			}
		} finally {
			writer.close();
		}
	}

	/**
	 * Read the access log & parse each row.
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<Access> read(String fileName) {
		List<Access> accessList = new ArrayList<Access>();
		File file = null;
		FileReader fileReader = null;
		BufferedReader buffReader = null;
		try {
			file = new File(fileName);
			fileReader = new FileReader(file);
			buffReader = new BufferedReader(fileReader);
			String line = buffReader.readLine();
			while (line != null) {
				//System.out.println(line);
				Access access = formatter.parse(line);
				accessList.add(access);
				line = buffReader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					// suppressed
				}
			}
			if (buffReader != null) {
				try {
					buffReader.close();
				} catch (IOException e) {
					// suppressed
				}
			}
		}
		return accessList;
	}
}
