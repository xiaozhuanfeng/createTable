package com.example.demo.util;

import com.alibaba.fastjson.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 文件工具类
 */
public class CommonFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonFileUtils.class);

    private CommonFileUtils() {
        throw new AssertionError();
    }

    /**
     * 根据路径创建文件，如果路径文件夹不存在，就创建
     *
     * @param filePath
     * @return
     */
    public static File createFileAbsolute(String filePath) throws IOException {
        File file = new File(filePath);
        File fileParent = file.getParentFile();

        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }

        if(!file.exists()){
            file.createNewFile();
        }

        return file;
    }

    /**
     * 传统写法，JDK1.6
     *
     * @param dataStr
     * @param filePath
     * @return
     */
    public static long randomWrite2FileTradition(String dataStr, String filePath) {
        long fileSize = 0L;
        RandomAccessFile raf = null;
        FileChannel fchannel = null;
        try {
            raf = new RandomAccessFile(createFileAbsolute(filePath), "rw");
            fchannel = raf.getChannel();

            ByteBuffer buf = ByteBuffer.allocate(1024);

            buf.clear();
            buf.put(dataStr.getBytes());
            buf.flip();

            while (buf.hasRemaining()) {
                fchannel.write(buf);
            }

            fileSize = fchannel.size();

        } catch (FileNotFoundException e) {
            logger.error("file not found", e);
        } catch (IOException e) {
            logger.error("IO exception", e);
        } finally {
            IOUtils.close(fchannel);
            IOUtils.close(raf);
        }
        return fileSize;
    }

    /**
     * >=JDK1.7 关闭资源语法糖
     *
     * @param dataStr
     * @param filePath
     * @param isAppendEnd 是否接着文件末尾添加
     * @return
     */
    public static long randomWrite2File(String dataStr, String filePath, boolean isAppendEnd, int bufferSize) {
        long fileSize = 0L;
        try (
                RandomAccessFile raf = new RandomAccessFile(createFileAbsolute(filePath), "rw");
                FileChannel fchannel = raf.getChannel()
        ) {
            ByteBuffer buf = ByteBuffer.allocate(bufferSize);

            buf.put(dataStr.getBytes());

            //limit = position,position=0
            buf.flip();

            if (isAppendEnd) {
                //将文件记录指针定位到pos的位置
                raf.seek(raf.length());
            }

            while (buf.hasRemaining()) {
                fchannel.write(buf);
            }

            fileSize = fchannel.size();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileSize;
    }

    /**
     * 读文件,限制：超过 buffSize 字节中文文件，由于ByteBuffer : buffSize，导致中文乱码
     *
     * @param filePath
     * @param piont
     * @param charactSet
     * @return
     */
    public static String randomReadFile(String filePath, int piont, int buffSize, String charactSet) {
        StringBuffer buffer = new StringBuffer();

        // 创建字符集
        Charset charset = Charset.forName(charactSet);

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r");
             FileChannel fchannel = raf.getChannel()
        ) {
            ByteBuffer buf = ByteBuffer.allocate(buffSize);
            //移动文件指针位置
            raf.seek(piont);

            while (fchannel.read(buf) != -1) {
                // flip方法在读缓冲区字节操作之前调用。   
                buf.flip();

                // 使用Charset.decode方法将字节转换为字符串   
                buffer.append(charset.decode(buf));

                // 清空缓冲   
                buf.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * 行读取
     *
     * @param filePath
     * @param charactSet
     * @return
     */
    public static String scannerReadFile(String filePath, String charactSet) {

        LocalDateTime startTime = LocalDateTime.now();

        StringBuffer buffer = new StringBuffer();
        try (FileInputStream inputStream = new FileInputStream(filePath);
             Scanner sc = new Scanner(inputStream, charactSet)
        ) {
            while (sc.hasNextLine()) {
                buffer.append(sc.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long timeConsuming = Duration.between(startTime, LocalDateTime.now()).toMillis();
        logger.info("耗时=" + timeConsuming / 1000 + "s");

        return buffer.toString();
    }

    /**
     * 获取文件属性
     *
     * @param filePath
     * @return
     */
    public static CommonFile getCommonFile(String filePath) {
        File file = new File(filePath);
        CommonFile commonFile = null;
        if (null != file) {
            commonFile = new CommonFile(file.getName(), file.length());
            commonFile.setParentPath(file.getParentFile().getPath());

        } else {
            throw new NullPointerException("File not found");
        }
        return commonFile;
    }

    /**
     * 文件时间戳+number
     *
     * @param number
     * @return
     */
    private static String createFileNameTimeStame(int number) {
        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "_" + number;
    }

    /**
     * 拷贝文件
     *
     * @param srcFilePath
     */
    public static long copyFile(String srcFilePath) {
        return copyFile(srcFilePath, 0L, "_bak");
    }

    /**
     * 拷贝文件
     *
     * @param srcFilePath
     */
    public static long copyFile(String srcFilePath, long position, String noteStr) {
        return copyFile(srcFilePath, position, Long.MAX_VALUE, noteStr);
    }

    /**
     * 拷贝文件
     * @param srcFilePath
     * @param position
     * @param end
     * @param noteStr 文件名标识 eg:test20190422_{noteStr}.text
     * @return
     */
    public static long copyFile(String srcFilePath, long position, long end, String noteStr) {
        logger.info("拷贝文件开始.......");
        long targetFileSize = 0L;

        LocalDateTime startTime = LocalDateTime.now();
        String timesStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String targetFilePath = getTargetFilePath(srcFilePath, timesStr + noteStr);

        try (RandomAccessFile raf = new RandomAccessFile(srcFilePath, "r");
             RandomAccessFile waf = new RandomAccessFile(createFileAbsolute(targetFilePath), "rw");
             FileChannel inFile = raf.getChannel();
             FileChannel outFile = waf.getChannel();
        ) {
            int capacity = 1024 * 10;
            ByteBuffer buf = ByteBuffer.allocate(capacity);

            //起始位
            raf.seek(position);

            while (inFile.read(buf) != -1) {
                // flip方法在读缓冲区字节操作之前调用。   
                buf.flip();

                int conL = (int) (outFile.size() - (end - position));
                if (conL < 0L) {
                    if (conL + capacity >= 0) {
                        //最后一次，定位换行符位置
                        if (buf.hasArray()) {
                            byte[] lastByteArr = getArrToLastLineBreakChar(buf.array());
                            ByteBuffer lastBuf = ByteBuffer.wrap(lastByteArr);
                            outFile.write(lastBuf);
                            targetFileSize = outFile.size();
                            break;
                        }
                    } else {
                        outFile.write(buf);
                    }

                } else {
                    targetFileSize = outFile.size();
                    break;
                }

                // 清空缓冲   
                buf.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long timeConsuming = Duration.between(startTime, LocalDateTime.now()).toMillis();
        logger.info("拷贝文件结束，耗时=" + timeConsuming / 1000 + "s");
        return targetFileSize;
    }

    /**
     * 是否是换行符
     *
     * @param b
     * @return
     */
    private static boolean isLineBreakChar(byte b) {
        char ss = (char) b;
        return '\n' == ss;
    }

    private static byte[] getArrToLastLineBreakChar(byte[] arr) {

        int lastIndex = arr.length;
        for (int i = lastIndex - 1; i >= 0; i--) {
            //找出最后一个换行符'\n'
            if (isLineBreakChar(arr[i])) {
                lastIndex = i;
                break;
            }
        }

        return Arrays.copyOfRange(arr, 0, lastIndex);
    }

    private static String getTargetFilePath(String srcFilePath, String replaceStr) {
        CommonFile commonFile = getCommonFile(srcFilePath);
        String targetFileName = commonFile.getFileNameFmt().replace("{timestamp}", replaceStr);
        return commonFile.getParentPath() + "\\" + targetFileName;
    }

    /**
     * 分割文件，速度快（自测中文也不会乱码），78MB：毫秒级
     *
     * @param srcFilePath
     * @param perMb    1M * perModel
     */
    public static void cutFile1(String srcFilePath, int perMb) {
        logger.info("分割文件开始.......");
        LocalDateTime startTime = LocalDateTime.now();
        CommonFile commonSrcFile = getCommonFile(srcFilePath);
        long maxSize = commonSrcFile.getFileSize();
        int fileNumber = 0;

        long start = 0L;
        long perFileSize = (2 << 9) * (2 << 9) * perMb;
        long end = perFileSize;

        while (true) {
            long targetFileSize = copyFile(srcFilePath, start, end, "_" + (++fileNumber));

            if (end >= maxSize) {
                break;
            }

            start += targetFileSize;
            end = (end >= maxSize) ? maxSize : start + perFileSize;
        }


        Long timeConsuming = Duration.between(startTime, LocalDateTime.now()).toMillis();
        logger.info("分割文件结束，耗时=" + timeConsuming / 1000 + "s");
    }

    /**
     * 性能比较差:96.3M 用了612s
     * 由于是行读，中文文件不乱码
     *
     * @param filePath
     * @param perFileSize
     * @param charSet
     */
    public static void cutFile2(String filePath, long perFileSize, String charSet) {
        //大文件大小
        logger.info("分割文件开始.......");
        LocalDateTime startTime = LocalDateTime.now();
        CommonFile commonFile = getCommonFile(filePath);

        try (FileInputStream inputStream = new FileInputStream(filePath);
             Scanner sc = new Scanner(inputStream, charSet)
        ) {
            int sonNum = 1;
            String sonFilePath = commonFile.getParentPath();
            String sonFileName = commonFile.getFileNameFmt().replace("{timestamp}",
                    createFileNameTimeStame(sonNum));
            sonFilePath += "\\" + sonFileName;

            while (sc.hasNextLine()) {

                long sonFileCurrentSize = randomWrite2File(sc.nextLine() + "\n", sonFilePath, true, 1024 * 10);

                if (sonFileCurrentSize >= perFileSize) {
                    sonFileName = commonFile.getFileNameFmt().replace("{timestamp}",
                            createFileNameTimeStame(++sonNum));
                    sonFilePath = commonFile.getParentPath() + "\\" + sonFileName;
                }
            }

            Long timeConsuming = Duration.between(startTime, LocalDateTime.now()).toMillis();
            logger.info("分割文件结束，耗时=" + timeConsuming / 1000 + "s");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class CommonFile {
        private String fileName;
        private long fileSize;
        private String parentPath;
        private String fileNameFmt;

        public CommonFile() {
        }

        public CommonFile(String fileName, long fileSize) {
            this.fileName = fileName;
            this.fileSize = fileSize;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getParentPath() {
            return parentPath;
        }

        public void setParentPath(String parentPath) {
            this.parentPath = parentPath;
        }

        public String getFileNameFmt() {
            int lastDot = fileName.lastIndexOf(".");

            String prefix = fileName.substring(0, lastDot);
            //文件后缀名
            String postfix = fileName.substring(lastDot + 1);
            fileNameFmt = prefix + "{timestamp}" + "." + postfix;
            return fileNameFmt;
        }

        public void setFileNameFmt(String fileNameFmt) {
            this.fileNameFmt = fileNameFmt;
        }

        @Override
        public String toString() {
            return "CommonFile{" +
                    "fileName='" + fileName + '\'' +
                    ", fileSize=" + fileSize +
                    ", parentPath='" + parentPath + '\'' +
                    ", fileNameFmt='" + getFileNameFmt() + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) throws IOException {
        //String str = "发生肯德基发生大师傅士大夫 \n  发生打开房间啊开始JFK \n";
        String filePath = ProjectPathUtils.getRootPath("file/test.txt");
        //CommonFileUtils.randomWrite2File("Hello world", filePath, true, 1024);
        File file = createFileAbsolute(filePath);

        String content = scannerReadFile(filePath,"UTF-8");

        System.out.println(content);
        //System.out.println(str.length());
        //System.out.println(str.getBytes().length);

        /*for(int i =0;i < 1000000;i++){
            long fileSize = randomWrite2File(i+ ":" +str, filePath, true,1024);
            System.out.println("fileSize = " + fileSize);
        }*/
        //0999420
        //copyFile(filePath);

        //cutFile1(filePath, 80);

        //System.out.println(getCommonFile(filePath));

        /*String str1 = CommonFileUtils.scannerReadFile(filePath, "UTF-8");
        System.out.println(str1);*/


    }


}
