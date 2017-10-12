package userCenter.service;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.encoder.SymbolInfo;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import scala.actors.threadpool.Arrays;
import userCenter.Utils.MatrixToImageWriter;
import userCenter.mapper.UserStoreInfoMapper;
import userCenter.model.batis.UserStoreInfo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by Hayden on 2017/3/22.
 */
@Service
public class UtilService {

    public static Random random = new Random(System.currentTimeMillis());
    public static String [] types = {".png",".jpg",".jpeg"};
    public static Set<String> SUPPORT_TYPES;

    @Value("#{configProperties['host_domain']}")
    public String host_domain;

    @Value("#{configProperties['filePath']}")
    private String FILEPATH;

    Map<String,Long> pdfFileMap = Maps.newHashMap();

    @Scheduled(cron = "0 0/3 * * * ?")
    public void delePdfFile(){
        if(pdfFileMap != null){
            for(String file : pdfFileMap.keySet()){
                //删掉过期图片
                if(pdfFileMap.get(file) < System.currentTimeMillis()){
                    File pdfFile = new File(file);
                    if(pdfFile.exists()){
                        pdfFile.delete();
                    }
                }
            }
        }
    }

    static{
        SUPPORT_TYPES = new HashSet<String>();
        SUPPORT_TYPES.addAll(Arrays.asList(types));
    }

    public String generateName(String oriname) {

        if(StringUtils.isBlank(oriname) || oriname.indexOf(".") < 1){
            return null;
        }

        String type = oriname.substring(oriname.lastIndexOf("."));

        if(!SUPPORT_TYPES.contains(type)){
            return null;
        }

        Integer suffix = random.nextInt(0xffff);
        Long time = System.currentTimeMillis()/1000;
        String name = "uploadimg_"+ time + "_" + suffix + type;

        return name;

    }

    public void generateBincode(String bincodeSrc, HttpServletResponse response) {

        if(StringUtils.isBlank(bincodeSrc)){
            return;
        }
        String content = bincodeSrc;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {

            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
            MatrixToImageWriter.writeToStream(bitMatrix,"jpg", response.getOutputStream());

        } catch (Exception e) {
        }


    }

    public static void main2(String [] s){
        try {
            String content = "http://local.huluweizhan.cn/customMenu/orderList/16/8";
            String path = "C:\\Users\\Hayden\\Desktop";
            String centerImg = "C:\\Users\\Hayden\\Desktop\\img\\phonebox.jpg";

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);
           // File file1 = new File(path,"firstbincode.jpg");
            //MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
            File file2 = new File(path,"secondbincode.jpg");
            MatrixToImageWriter.writeBincodeWithCenterImg(content, 300, 300, file2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String content = "http://local.huluweizhan.cn/customMenu/orderList/16/8";
        String imgsrr = "C:\\Users\\Hayden\\Desktop\\secondbincode.jpg";
        String filepath = "C:\\Users\\Hayden\\Desktop\\test.pdf";

        // step 1: creation of a document-object
        Document document = new Document();

        try {

            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            PdfWriter.getInstance(document, new FileOutputStream(filepath));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filepath));
            writer.setStrictImageSequence(true);

            // step 3: we open the document
            document.open();

            // step 4:
//            Paragraph paragraph = new Paragraph("1st image");
//            document.add(paragraph);
//            com.itextpdf.text.Image jpg = com.itextpdf.text.Image.getInstance(imgsrr);
//            jpg.scaleAbsoluteWidth(100);
//            jpg.scaleAbsoluteHeight(100);
//            document.add(jpg);
//
//            document.add(new Paragraph("2nd image"));
//            com.itextpdf.text.Image jpg2 = com.itextpdf.text.Image.getInstance(imgsrr);
//            jpg2.scaleAbsoluteWidth(100);
//            jpg2.scaleAbsoluteHeight(100);
//            document.add(jpg2);

            BaseFont bf = BaseFont.createFont( "STSong-Light", "UniGB-UCS2-H", false,false,null,null);
            com.itextpdf.text.Font fontChinese5  =  new com.itextpdf.text.Font(bf,15);
            PdfPTable table1 = new PdfPTable(3); //表格两列
            table1.setHorizontalAlignment(Element.ALIGN_CENTER); //垂直居中
            table1.setWidthPercentage(100);//表格的宽度为100%
            float[] wid1 ={0.333f,0.333f,0.333f}; //两列宽度的比例
            table1.setWidths(wid1);
            table1.getDefaultCell().setBorderWidth(0); //不显示边框
            PdfPCell c1 = new PdfPCell(new Paragraph("扫码点菜",fontChinese5));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_CENTER);
            c1.setBorderWidth(0);
            table1.addCell(c1);
            PdfPCell c3 = new PdfPCell(new Paragraph("扫码点菜",fontChinese5));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setVerticalAlignment(Element.ALIGN_CENTER);
            c3.setBorderWidth(0);
            table1.addCell(c3);
            PdfPCell c5 = new PdfPCell(new Paragraph("扫码点菜",fontChinese5));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setVerticalAlignment(Element.ALIGN_CENTER);
            c5.setBorderWidth(0);
            table1.addCell(c5);
            String imagepath = imgsrr;
            BufferedImage bufferedImage = MatrixToImageWriter.getBufferedImage4BincodeWithCenterImg(content, 300, 300);
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg", bas);
            byte[] data = bas.toByteArray();
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imagepath);
            image.scaleAbsoluteWidth(130);
            image.scaleAbsoluteHeight(130);
            PdfPCell c6 = new PdfPCell(image);
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            //c6.setVerticalAlignment(Element.ALIGN_CENTER);
            c6.setPaddingTop(1);
            c6.setBorderWidth(0);
            table1.addCell(c6);
            PdfPCell c8 = new PdfPCell(image);
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setPaddingTop(1);
            c8.setBorderWidth(0);
            table1.addCell(c8);
            PdfPCell c10 = new PdfPCell(image);
            c10.setHorizontalAlignment(Element.ALIGN_CENTER);
            c10.setPaddingTop(1);
            c10.setBorderWidth(0);
            table1.addCell(c10);


            PdfPCell c11 = new PdfPCell(new Paragraph("桌号：1",fontChinese5));
            c11.setHorizontalAlignment(Element.ALIGN_CENTER);
            c11.setBorderWidth(0);
            c11.setPaddingTop(0);
            table1.addCell(c11);
            PdfPCell c13 = new PdfPCell(new Paragraph("桌号：2",fontChinese5));
            c13.setHorizontalAlignment(Element.ALIGN_CENTER);
            c13.setPaddingTop(0);
            c13.setBorderWidth(0);
            table1.addCell(c13);
            PdfPCell c15 = new PdfPCell(new Paragraph("桌号：3",fontChinese5));
            c15.setHorizontalAlignment(Element.ALIGN_CENTER);
            c15.setPaddingTop(0);
            c15.setBorderWidth(0);
            table1.addCell(c15);

            document.add(table1);//增加到文档中
        }
        catch(Exception e) {
           e.printStackTrace();
        }


        // step 5: we close the document
        document.close();
    }

    public String generateBincodeToPdf(Long uid, Integer count) {
        if(uid == null || uid <= 0
                || count == null || count <= 0){
            return null;
        }

        //生成二维码
        String pdfFilePath = FILEPATH + uid + ".pdf";
        Document document = new Document(PageSize.A4, 50, 50, 50, 90);

        try {

            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            writer.setStrictImageSequence(true);

            // step 3: we open the document
            document.open();

            BaseFont bf = BaseFont.createFont( "STSong-Light", "UniGB-UCS2-H", false,false,null,null);
            com.itextpdf.text.Font fontChinese5  =  new com.itextpdf.text.Font(bf,15);
            PdfPTable table1 = new PdfPTable(3); //表格两列
            table1.setHorizontalAlignment(Element.ALIGN_CENTER); //垂直居中
            table1.setWidthPercentage(100);//表格的宽度为100%
            float[] wid1 ={0.333f,0.333f,0.333f}; //两列宽度的比例
            table1.setWidths(wid1);
            table1.getDefaultCell().setBorderWidth(0); //不显示边框

            for(int tableId = 1;tableId <= count;tableId += 3){
                //添加开头 扫码点菜
                for(int i = tableId ; i < 3+tableId ; i += 1){
                    if(i  <= count){
                        addPhraseTitle2Table(table1,new Paragraph("扫码点菜",fontChinese5));
                    }else{
                        addPhraseTitle2Table(table1,null);
                    }

                }

                //添加二维码
                for(int i = tableId ; i < 3+tableId ; i += 1){
                    if(i  <= count){
                        addImage2Table(table1,i,uid);
                    }else{
                        addImage2Table(table1,null,null);
                    }

                }

                //添加桌号提示
                for(int i = tableId ; i < 3+tableId ; i += 1){
                    if(i  <= count){
                        addPhraseNum2Table(table1,new Paragraph("桌号："+i,fontChinese5));
                    }else{
                        addPhraseNum2Table(table1,null);
                    }
                }

            }


            document.add(table1);//增加到文档中
        }
        catch(Exception e) {
            return null;
        }

        document.close();

        deleteTmpFile(uid,count);
        return pdfFilePath;
    }

    private void deleteTmpFile(Long uid, Integer count) {
        for(int i=1;i<=count;i+=1){
            String imgPath = FILEPATH + uid + "_" + i + ".jpg";
            File imgFile = new File(imgPath);
            if(imgFile.exists()){
                imgFile.delete();
            }
        }

        final String pdfFilePath = FILEPATH + uid + ".pdf";
        Long deleteTime = System.currentTimeMillis() + 3*61*1000;
        pdfFileMap.put(pdfFilePath,deleteTime);
    }


    private void addImage2Table(PdfPTable table, Integer tableId, Long uid) {
        if(uid == null || uid <= 0
                || tableId == null || tableId <= 0){
            PdfPCell c = new PdfPCell();
            c.setBorderWidth(0);
            table.addCell(c);
        }else{
            String content = host_domain + "customMenu/orderList/" + uid + "/" + tableId;
            String imgPath = FILEPATH + uid + "_" + tableId + ".jpg";
            try {
                BufferedImage bufferedImage = MatrixToImageWriter.getBufferedImage4BincodeWithCenterImg(content, 300, 300);
                ImageIO.write(bufferedImage,"jpg", new File(imgPath));
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imgPath);
                image.scaleAbsoluteWidth(130);
                image.scaleAbsoluteHeight(130);
                PdfPCell c = new PdfPCell(image);
                c.setHorizontalAlignment(Element.ALIGN_CENTER);
                c.setPaddingTop(1);
                c.setBorderWidth(0);
                table.addCell(c);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void addPhraseTitle2Table(PdfPTable table1, Paragraph paragraph) {
        PdfPCell c1;
        if(paragraph != null){
            c1 = new PdfPCell(paragraph);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorderWidth(0);
            c1.setPaddingTop(10);
            table1.addCell(c1);
        }else{
            c1 = new PdfPCell();
            c1.setBorderWidth(0);
            table1.addCell(c1);
        }
    }

    private void addPhraseNum2Table(PdfPTable table1, Paragraph paragraph) {
        PdfPCell c1;
        if(paragraph != null){
            c1 = new PdfPCell(paragraph);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorderWidth(0);
            c1.setPaddingTop(0);
            table1.addCell(c1);
        }else{
            c1 = new PdfPCell();
            c1.setBorderWidth(0);
            table1.addCell(c1);
        }
    }
}
