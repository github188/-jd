package taotao.com;

import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

/**
 * Created by Ellen on 2017/7/12.
 */

public class CountCodeLine {

    int codeline = 0;
    int impLine = 0;
    int pacLine = 0;
    @Test
    public void countLines(){
        try {
            File file = new File("H:\\git");
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName();
                    if (files[i].isDirectory()) {
                        //文件夹
                        getFileList(files[i].getAbsolutePath());
                    } else if (fileName.endsWith(".java")){
                        //java文件
                        String strFileName = files[i].getAbsolutePath();
                        FileReader fr = new FileReader(strFileName);
                        LineNumberReader reader = new LineNumberReader(fr);
                        String s = reader.readLine();
                        while (s != null) {
                            if (!s.contains("import") && !s.contains("package") && !"".equals(s.trim())) {
                                codeline++;
                            }
                            if (s.contains("import")) {
                                impLine++;
                            }
                            if (s.contains("package")) {
                                pacLine++;
                            }
                            s = reader.readLine();
                        }
                        reader.close();
                        fr.close();
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("4月27号到7月12号代码总行数： " + codeline);
            System.out.println("4月27号到7月12号导包数： " + impLine);
            System.out.println("4月27号到7月12号java文件总数： " + pacLine);
        }
    }

    private void getFileList(String strPath) throws Exception{
        File file = new File(strPath);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    //文件夹
                    getFileList(files[i].getAbsolutePath());
                } else if (fileName.endsWith(".java")){
                    //java文件
                    String strFileName = files[i].getAbsolutePath();
                    FileReader fr = new FileReader(strFileName);
                    LineNumberReader reader = new LineNumberReader(fr);
                    String s = reader.readLine();
                    while (s != null) {
                        if (!s.contains("import") && !s.contains("package") && !"".equals(s.trim())) {
                            codeline++;
                        }
                        if (s.contains("import")) {
                            impLine++;
                        }
                        if (s.contains("package")) {
                            pacLine++;
                        }
                        s = reader.readLine();
                    }
                    reader.close();
                    fr.close();
                } else {
                    continue;
                }
            }
        }
    }
}
