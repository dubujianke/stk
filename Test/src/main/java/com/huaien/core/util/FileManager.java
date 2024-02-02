/**
 *  Product: Posterita Web-Based POS and Adempiere Plugin
 *  Copyright (C) 2007  Posterita Ltd
 *  This file is part of POSterita
 *  
 *  POSterita is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.huaien.core.util;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	public static final String FILE_PATH_SEPARATOR = System
			.getProperty("file.separator");


	public static String read(String fileName) throws IOException {
		StringBuffer sbf = new StringBuffer();
		String line;
		InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "utf-8");
		BufferedReader bf = new BufferedReader(isr);	
		while ((line = bf.readLine()) != null) {
			sbf.append(line).append("\r\n");
		}
		bf.close();
		isr.close();
		return sbf.toString();
	}
	
	public static List<File> getFiles(String dirPath) {
		List<File> files = new ArrayList<File>();
		File dir = new File(dirPath);
		File[] fs = dir.listFiles();
		for(int i=0; i<fs.length; i++) {
			File aFile = fs[i];
			String name = aFile.getName();
			files.add(aFile);
		}
		return files;
	}

	
	public static String read3(String fileName) throws IOException {
		StringBuffer sbf = new StringBuffer();
		String line;
		InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "utf-8");
		BufferedReader bf = new BufferedReader(isr);	
		while ((line = bf.readLine()) != null) {
			sbf.append(line.trim());
		}
		bf.close();
		isr.close();
		return sbf.toString();
	}
	
	public static String read2(String fileName) throws IOException {
		StringBuffer sbf = new StringBuffer();
		String line;
		InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "utf-8");
		BufferedReader bf = new BufferedReader(isr);	
		while ((line = bf.readLine()) != null) {
			sbf.append(line).append("");
		}
		bf.close();
		isr.close();
		return sbf.toString();
	}
	
	public static List<String> readListGB(String fileName)  {
		List<String> list = new ArrayList<String>();
		String line;
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(new FileInputStream(fileName), "GB2312");
			BufferedReader bf = new BufferedReader(isr);	
			while ((line = bf.readLine()) != null) {
				if(line.contains("数据来源")) {
					continue;
				}
				if(!StrUtil.isEmpty(line)) {
					list.add(line);
				}
			}
			bf.close();
			isr.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	public static List<String> readList(String fileName)  {
		List<String> list = new ArrayList<String>();
		String line;
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(new FileInputStream(fileName), "utf-8");
			BufferedReader bf = new BufferedReader(isr);	
			while ((line = bf.readLine()) != null) {
				list.add(line);
			}
			bf.close();
			isr.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	public static List<String> readListGBK(String fileName)  {
		List<String> list = new ArrayList<String>();
		String line;
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(new FileInputStream(fileName), "gbk");
			BufferedReader bf = new BufferedReader(isr);	
			while ((line = bf.readLine()) != null) {
				list.add(line);
			}
			bf.close();
			isr.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static void write(String fileName, List<String> list) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		for(String str:list) {
			stringBuffer.append(str).append("\r\n");
		}
		write(fileName, stringBuffer.toString());
	}

	public static void writeGB(String fileName, List<String> list) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		for(String str:list) {
			stringBuffer.append(str).append("\r\n");
		}
		writeGB(fileName, stringBuffer.toString());
	}


	public static String write(String fileName, String content) throws IOException {
		StringBuffer sbf = new StringBuffer();
		String line;
		OutputStreamWriter isr = new OutputStreamWriter(new FileOutputStream(fileName), "utf-8");
		BufferedWriter bf = new BufferedWriter(isr);	
		bf.write(content);
		bf.close();
		isr.close();
		return sbf.toString();
	}

	public static String writeGB(String fileName, String content) throws IOException {
		StringBuffer sbf = new StringBuffer();
		String line;
		OutputStreamWriter isr = new OutputStreamWriter(new FileOutputStream(fileName), "gb2312");
		BufferedWriter bf = new BufferedWriter(isr);
		bf.write(content);
		bf.close();
		isr.close();
		return sbf.toString();
	}
	
	public static String append(String fileName, String content) throws IOException {
		StringBuffer sbf = new StringBuffer();
		String line;
		OutputStreamWriter isr = new OutputStreamWriter(new FileOutputStream(fileName, true), "utf-8");
		BufferedWriter bf = new BufferedWriter(isr);	
		bf.write(content);
		bf.close();
		isr.close();
		return sbf.toString();

	}
	
	public static boolean replaceFile(String oldFile, String newFile) {
		String content;
		try {
			content = read(oldFile);
			String newContent = content.replaceAll("admin_shop", "admin_meishi");
			write(newFile, newContent);
		} catch (IOException e) {
			return false;
		}
	
		return true;
	}
	
	public static List<String> readResourceList(String fileName)  {
		List<String> list = new ArrayList<String>();
		String line;
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), "utf-8");
			BufferedReader bf = new BufferedReader(isr);	
			while ((line = bf.readLine()) != null) {
				list.add(line);
			}
			bf.close();
			isr.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public static String readResource(String fileName) throws IOException {
		StringBuffer sbf = new StringBuffer();
		String line;
		InputStreamReader isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), "utf-8");
		BufferedReader bf = new BufferedReader(isr);	
		while ((line = bf.readLine()) != null) {
			sbf.append(line).append("\r\n");
		}
		bf.close();
		isr.close();
		return sbf.toString();
	}
	
	public static void mkdirs(String path) {
		File aFile = new File(path);
		if(!aFile.exists()) {
			aFile.mkdirs();
		}
	}
	
	public static boolean exist(String path) {
		File aFile = new File(path);
		if(!aFile.exists()) {
			return false;
		}
		
		return true;
	}
	public static long copy(File f1,File f2) throws Exception{
		  int length=2097152;
		  FileInputStream in=new FileInputStream(f1);
		  FileOutputStream out=new FileOutputStream(f2);
		  byte[] buffer=new byte[length];
		  while(true){
		   int ins=in.read(buffer);
		   if(ins==-1){
		    in.close();
		    out.flush();
		    out.close();
		    return 0;
		   }else
		    out.write(buffer,0,ins);
		  }
		 }

	public static void copy(String f1, String f2) {
		try {
			File aFile = new File(f1);
			String f = f2+ "/" + aFile.getName();
			copy(new File(f1), new File(f));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyTo(String f1, String f2) {
		try {
			copy(new File(f1), new File(f2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getContent(String webRootPath, String contextPath, String pagePath, String path) throws IOException {
		String content = "";
		if(path.startsWith("http:")) {
			
		}else if(path.equals("/")) {
			
		}else {
			String rPath = pagePath + path;
			content = read(rPath);
		}
		return content;
	}
	
	
	  public static void rename(String srcFileName, String destFileName) {
		  File srcFile = new File(srcFileName);  
		  File destFile = new File(destFileName);  
		  srcFile.renameTo(destFile);
	  }
	  
	 /** 
     * 复制单个文件 
     *  
     * @param srcFileName 
     *            待复制的文件名 
     * @param descFileName 
     *            目标文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,  
            boolean overlay) {
        File srcFile = new File(srcFileName);  
  
        // 判断源文件是否存在  
        if (!srcFile.exists()) {  
           
            return false;  
        } else if (!srcFile.isFile()) {  
           
            return false;  
        }  
  
        // 判断目标文件是否存在  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            if (overlay) {  
                new File(destFileName).delete();  
            }  
        } else {  
            if (!destFile.getParentFile().exists()) {  
                if (!destFile.getParentFile().mkdirs()) {  
                    return false;  
                }  
            }  
        }  
  
        int byteread = 0;
        InputStream in = null;  
        OutputStream out = null;  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }  
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * 复制整个目录的内容 
     *  
     * @param srcDirName 
     *            待复制目录的目录名 
     * @param destDirName 
     *            目标目录名 
     * @param overlay 
     *            如果目标目录存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyDirectory(String srcDirName, String destDirName,  
            boolean overlay) {  
        // 判断源目录是否存在  
        File srcDir = new File(srcDirName);  
        if (!srcDir.exists()) {  
            return false;  
        } else if (!srcDir.isDirectory()) {  
            return false;  
        }  
  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        File destDir = new File(destDirName);  
        if (destDir.exists()) {  
            if (overlay) {  
                new File(destDirName).delete();  
            } else {  
                return false;  
            }  
        } else {  
            if (!destDir.mkdirs()) {  
                return false;  
            }  
        }  
  
        boolean flag = true;  
        File[] files = srcDir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            if (files[i].isFile()) {  
                flag = copyFile(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            } else if (files[i].isDirectory()) {  
                flag = copyDirectory(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            }  
        }  
        if (!flag) {  
            return false;  
        } else {  
            return true;  
        }  
    }
    
    public static boolean isText(String fileName) {
    	boolean type = false;
    	if(fileName.equalsIgnoreCase("FileUtil.php")) {
    		int a=0;a++;
    	}
    	String names[] = fileName.split("\\.");
    	if(names.length==2) {
    		if(names[1].equalsIgnoreCase("php") || names[1].equalsIgnoreCase("css") || names[1].equalsIgnoreCase("html")  || names[1].equalsIgnoreCase("html") || names[1].equalsIgnoreCase("js")) {
    			type = true;
    		}
    	}
    	return type;
    }
    
    public static boolean generateProject(String srcDirName, String destDirName, boolean overlay) {  
        File srcDir = new File(srcDirName);  
        if (!srcDir.exists()) {  
            return false;  
        } else if (!srcDir.isDirectory()) {  
            return false;  
        }  
  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        File destDir = new File(destDirName);  
        if (destDir.exists()) {  
            if (overlay) {  
                new File(destDirName).delete();  
            } else {  
                return false;  
            }  
        } else {  
            if (!destDir.mkdirs()) {  
                return false;  
            }  
        }  
  
        boolean flag = true;  
        File[] files = srcDir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            if (files[i].isFile()) {
            	String fileName = files[i].getName();
            	if(fileName.contains("Address_add")) {
            		int a=0;a++;
            	}
            	if(isText(fileName)) {
            		flag = replaceFile(files[i].getAbsolutePath(), destDirName + files[i].getName());
            	}else {
            		flag = copyFile(files[i].getAbsolutePath(),  
                            destDirName + files[i].getName(), overlay);  
            	}
                
                if (!flag)  
                    break;  
            } else if (files[i].isDirectory()) {  
                flag = generateProject(files[i].getAbsolutePath(),  
                        destDirName + files[i].getName(), overlay);  
                if (!flag)  
                    break;  
            }  
        }  
        if (!flag) {  
            return false;  
        } else {  
            return true;  
        }  
    }
	
    
   

}
