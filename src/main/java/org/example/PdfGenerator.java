package org.example;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.IOException;
import java.io.InputStream;

public class PdfGenerator {

    public static void main(String[] args) {

        String dest = "output.pdf";
        String price = "800 000 руб.";
        String project = "Кухня Канти + Примавера ";
        String imgPath = "kitchen.jpg";

        try {

            InputStream resource = PdfGenerator.class.getClassLoader().getResourceAsStream("zov2slidesv2.pdf");
            if (resource == null) {
                throw new IOException("Resource not found: zov2slides.pdf");
            }

            InputStream fontResource = PdfGenerator.class.getClassLoader().getResourceAsStream("font/Montserrat-Bold.ttf");
            if (fontResource == null) {
                throw new IOException("Resource not found: Montserrat-Bold.ttf");
            }

            InputStream imageResource = PdfGenerator.class.getClassLoader().getResourceAsStream(imgPath);
            if (imageResource == null) {
                throw new IOException("Resource not found: " + imgPath);
            }

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(resource), new PdfWriter(dest));
            PdfFont font = PdfFontFactory.createFont(fontResource.readAllBytes(), PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            Document document = new Document(pdfDoc);

            PdfCanvas pdfCanvas = new PdfCanvas(pdfDoc.getPage(2));

            float pageWidth = pdfDoc.getPage(2).getPageSize().getWidth();

            float fontSize = 35;

            float fontSizeProject = 35;

            float textWidth = font.getWidth(price, fontSize);

            float textWidthProject = font.getWidth(project, fontSizeProject);

            float x = (pageWidth - textWidth) / 2;
            float y = 35;

            float xp = (pageWidth - textWidthProject) / 2;
            float yp = 655;

            pdfCanvas.setFillColor(new DeviceRgb(255, 255, 255));
            pdfCanvas.setFontAndSize(font, fontSize);

            pdfCanvas.beginText();
            pdfCanvas.moveText(x, y);
            pdfCanvas.showText(price);
            pdfCanvas.endText();


            pdfCanvas.setFillColor(new DeviceRgb(255, 255, 255));
            pdfCanvas.setFontAndSize(font, fontSizeProject);

            pdfCanvas.beginText();
            pdfCanvas.moveText(xp, yp);
            pdfCanvas.showText(project);
            pdfCanvas.endText();

            byte[] imgBytes = imageResource.readAllBytes();
            ImageData imageData = ImageDataFactory.create(imgBytes);
            Image image = new Image(imageData);

            float imgX = 53;
            float imgY = 87;
            float imgWidth = 920;
            float imgHeight = 517.5F;

            float borderThickness = 5f;

            float rectX = imgX - borderThickness;
            float rectY = imgY - borderThickness;
            float rectWidth = imgWidth + 2 * borderThickness;
            float rectHeight = imgHeight + 2 * borderThickness;

            pdfCanvas.setFillColor(ColorConstants.WHITE);
            pdfCanvas.rectangle(rectX, rectY, rectWidth, rectHeight);
            pdfCanvas.fill();

            image.setFixedPosition(2, imgX, imgY);
            image.scaleAbsolute(imgWidth, imgHeight);

            document.add(image);

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
