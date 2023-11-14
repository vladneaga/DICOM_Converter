package CT_Converter;

//import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.TransferSyntax;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.UID;
import org.dcm4che2.data.VR;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/*Dcm File Format
 * Id
 * Name
 * Patient Sex
 * Birth Date
 * Address
 *
 * */

public class Converter extends Converter_Interface {
    private static final Map<Integer, VR> tagVRMap= new LinkedHashMap<Integer, VR>(){{
        put(Tag.PatientName, VR.PN);
        put(Tag.PatientID,  VR.SS);
        put(Tag.PatientSex, VR.LO);
        put(Tag.PatientBirthDate, VR.DA);
        put(Tag.PatientAddress, VR.LO);

    }};
    public File convertFromDcm (File file1, File file2, File file3) throws IOException {
        if(!file1.getName().endsWith(".dcm"))  return null;
        //fillWithExperimentalData(file1);
            DicomInputStream dis = new DicomInputStream(file1);
            DicomObject dcmObject = dis.readDicomObject();
            Patient patient = new Patient(dcmObject.getString(Tag.PatientName),
                    dcmObject.getInt(Tag.PatientID), dcmObject.getString(Tag.PatientSex),
                    LocalDate.parse(dcmObject.getString(Tag.PatientBirthDate), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    dcmObject.getString(Tag.PatientAddress));
            patient.setPixelData(dcmObject.getBytes(Tag.PixelData));
        System.out.println(patient.toString() + "here");
       PrintWriter fileWriter = new PrintWriter(file2);
        // Write data to the file.txt

        fileWriter.println(patient.getId());
        fileWriter.println(patient.getName());
        fileWriter.println(patient.getSex().toString());
        fileWriter.println(patient.getBirthDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        fileWriter.println(patient.getAddress());
        // Close the file
        fileWriter.close();
        //////////////////////////// Writing to bin file
        FileOutputStream fos = new FileOutputStream(file3);
            fos.write(patient.getPixelData());


        return null;
    }
    public void fillWithExperimentalData(File file, File file2) {
        try {
            DicomOutputStream dcmOut = new DicomOutputStream(new FileOutputStream(file));
            DicomObject dicomObject = new BasicDicomObject();
            DicomInputStream dis = new DicomInputStream(file2);
            DicomObject dcmObject = dis.readDicomObject();
            dicomObject.putString(Tag.PatientName,  VR.PN, "John Doe");
            dicomObject.putInt(Tag.PatientID,  VR.SS, 111);
            dicomObject.putString(Tag.PatientSex, VR.LO, "Male");
            dicomObject.putString(Tag.PatientBirthDate, VR.DA,  "23-08-2003");
            dicomObject.putString(Tag.PatientAddress, VR.LO, "Stuttgart");
            dicomObject.putBytes(Tag.PixelData, VR.OW, dcmObject.getBytes(Tag.PixelData));
            dcmOut.setTransferSyntax(UID.ExplicitVRLittleEndian);
            dcmOut.writeDicomObject(dicomObject, TransferSyntax.ExplicitVRLittleEndian);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void convertToDcm(File fileDcm, File fileTxt, File fileBin) {
        try {
            DicomOutputStream dcmOut = new DicomOutputStream(new FileOutputStream(fileDcm));
            DicomObject dicomObject = new BasicDicomObject();
            BufferedReader reader = new BufferedReader(new FileReader(fileTxt));
            String line;
            List<String> dataFromTxtFile = new LinkedList<>();
            while ((line = reader.readLine()) != null) {
                // Process each line of the text file
                //System.out.println(line);
                dataFromTxtFile.add(line);
            }
            Patient patient = new Patient(dataFromTxtFile.get(1), Integer.parseInt(dataFromTxtFile.get(0)),
                    dataFromTxtFile.get(2), LocalDate.parse(dataFromTxtFile.get(3), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    dataFromTxtFile.get(4));

            for(Map.Entry<Integer, VR> entry: tagVRMap.entrySet()){
                if(patient.tagFieldMap.get(entry.getKey()) instanceof String) {
                    dicomObject.putString(entry.getKey(), entry.getValue(), patient.tagFieldMap.get(entry.getKey()).toString());
                }
                if(patient.tagFieldMap.get(entry.getKey()) instanceof Integer) {
                    dicomObject.putInt(entry.getKey(), entry.getValue(), (Integer) patient.tagFieldMap.get(entry.getKey()));
                }
                if(patient.tagFieldMap.get(entry.getKey()) instanceof LocalDate) {
                    dicomObject.putString(entry.getKey(), entry.getValue(),
                             ((LocalDate) patient.tagFieldMap.get(entry.getKey())).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString());
                }
            }
            FileInputStream fileInputStream = new FileInputStream(fileBin);
            patient.setPixelData(fileInputStream.readAllBytes());
            dicomObject.putBytes(Tag.PixelData, VR.OW, patient.getPixelData());
            dcmOut.setTransferSyntax(UID.ExplicitVRLittleEndian);
            dcmOut.writeDicomObject(dicomObject, TransferSyntax.ExplicitVRLittleEndian);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void experiment(File file1) throws IOException {
        DicomInputStream dis = new DicomInputStream(file1);
        DicomObject dcmObject = dis.readDicomObject();

        System.out.println(dcmObject.getString(Tag.PatientID));
        System.out.println(dcmObject.getDate(Tag.StudyTime));
        System.out.println(dcmObject.getString(Tag.PatientSex));
        byte[] pixelData = dcmObject.getBytes(Tag.PixelData);
        System.out.println(Arrays.toString(pixelData));
        //System.out.println(Arrays.toString(dcmObject.getBytes(Tag.CurveData)));
        //System.out.println(Arrays.toString(dcmObject.getBytes(Tag.PixelData)));
        //System.out.println(Arrays.toString(dcmObject.getBytes(Tag.PixelData)));

    }

}

