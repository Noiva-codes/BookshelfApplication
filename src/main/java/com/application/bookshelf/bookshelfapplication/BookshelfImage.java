package com.application.bookshelf.bookshelfapplication;

import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.io.File;

/*
The PDFReader class is created to handle operations involving PDF files and to be able to
assists in the creation of /BitMap Images/ for the rest of the application.

*/
public class BookshelfImage {

    Path pathToPDF;
    File bookName;
    private final String IMG_PATH = "./images/";

    public BookshelfImage(File bookName, Path pathToPDF) {
        this.pathToPDF = pathToPDF; //Path to the the PDF
        this.bookName = bookName; //get the name of the book
    }

    //This function should create an image from the first page in the PDF.
    public void thumbnail() throws IOException {
        // Size constraints: Height: 150 px, Width 222 px for the size we have the program at right now.
        //Create a PDDocument reader object and a renderer obj to create thumbnail.
        PDDocument bookFile = PDDocument.load(this.bookName);
        PDFRenderer renderer = new PDFRenderer(bookFile);
        //Create a buffered image from the first page of the book.
        //Image size we need for the ImageView is 150 height, 222 width
        //300 dpi = ??? 2k by 2.7k, Width by Height
        // 72 dpi = 540 x 666
        // 36 dpi = 270 x 333
        // 27 dpi ~ 201 x 254
        // 18 dpi = 135 x 166
        BufferedImage thumbnail = renderer.renderImageWithDPI(0, 300, ImageType.RGB);
        //Use fileName to make a complete path to images folder
        String fileName = IMG_PATH + this.bookName.getName().substring(0,this.bookName.getName().length()-4) + ".png";
        ImageIOUtil.writeImage(thumbnail, fileName, 1);
        bookFile.close();

        // Finish this return statement appropriately using ImageWriterIOUtils from PDFApache
        //String of an image will not work

        //return new Image(thumbnail.toString());
    }

    public String getImagePath() {
        return IMG_PATH;
    }

    // Returns the bookname as a String
    public String getBookName() {
        return bookName.getName();
    }
}
