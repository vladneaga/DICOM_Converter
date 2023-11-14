import CT_Converter.Converter;

import java.io.*;

//import DCM_Library.*;
public class Main {
    public static void main(String[] args) throws IOException {
        Converter cv = new Converter();
        File file1 = new File("src\\main\\java\\aaa.dcm");
        File fileBrain = new File("src\\main\\java\\MRBRAIN.DCM");
        File file2 = new File("src\\main\\java\\aaa.txt");
        File file3 = new File("src\\main\\java\\bbb.txt");
        File file4 = new File("src\\main\\java\\aaa.bin");

        //cv.fillWithExperimentalData(file1, fileBrain);
         cv.convertFromDcm(file1, file2, file4);
         //cv.experiment(file1);
         cv.convertToDcm(file1, file2, file4);
         cv.convertFromDcm(file1, file2,file4);
    }
}
/*
*/
