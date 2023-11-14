package CT_Converter;

import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class Patient {

    public enum Sex {
        Male, Female, Divers
    }
    private String name;
    private int id;
    private Sex sex;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private LocalDate birthDate;
    private String address;
    byte[] pixelData;
    public  final Map<Integer, Object> tagFieldMap= new HashMap<>(){};
public void initializeMap() {
    tagFieldMap.put(Tag.PatientName, name);
    tagFieldMap.put(Tag.PatientID,  id);
    tagFieldMap.put(Tag.PatientSex, sex.toString());////
    tagFieldMap.put(Tag.PatientBirthDate, birthDate);
    tagFieldMap.put(Tag.PatientAddress, address);
}
    public Patient(String name, int id, String sex, LocalDate birthDate, String address) {
        this.name = name;
        this.id = id;
        this.sex = Sex.valueOf(sex);
        this.birthDate = birthDate;
        this.address = address;
        initializeMap();
    }

    public byte[] getPixelData() {
        return pixelData;
    }

    public void setPixelData(byte[] pixelData) {
        this.pixelData = pixelData;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", sex=" + sex +
                ", birthDate=" + birthDate +
                ", address='" + address + '\'' +
                ", pixelData=" + Arrays.toString(pixelData) +
                '}';
    }

    public Patient(String name, int id, Sex sex, LocalDate birthDate, String address) {
        this.name = name;
        this.id = id;
        this.sex = sex;
        this.birthDate = birthDate;
        this.address = address;
        initializeMap();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
