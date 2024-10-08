package lsfusion.base.file;

import lsfusion.base.BaseUtils;
import lsfusion.base.SystemUtils;
import lsfusion.base.mutability.TwinImmutableObject;
import lsfusion.interop.session.ExternalUtils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class RawFileData extends TwinImmutableObject<RawFileData> implements Serializable {

    public static final RawFileData EMPTY = new RawFileData(new byte[0]);
    
    private final byte[] array;

    public RawFileData(byte[] array) {
        this.array = array;
        assert array != null;
    }

    public RawFileData(InputStream stream) throws IOException {
        this(IOUtils.readBytesFromStream(stream));
    }

    public RawFileData(InputStream stream, int len) throws IOException {
        this(IOUtils.readBytesFromStream(stream, len));
    }

    public RawFileData(ByteArrayOutputStream array) {
        this(array.toByteArray());
    }
    
    public RawFileData(File file) throws IOException {
        this(IOUtils.getFileBytes(file));
    }

    public RawFileData(String filePath) throws IOException {
        this(IOUtils.getFileBytes(filePath));
    }

    public byte[] getBytes() {
        return array;
    }

    public RawFileData(String string, Charset charset) {
        this(string.getBytes(charset));
    }

    public RawFileData(String string, String charset) {
        this(BaseUtils.getSafeBytes(string, charset));
    }

    public String getString(Charset charset) {
        return new String(getBytes(), charset);
    }
    public String getString(String charset) {
        return BaseUtils.toSafeString(getBytes(), charset);
    }

    public String convertString() {
        return getString(ExternalUtils.fileCharset);
    }

    public int getLength() {
        return array.length;
    }
    
    public void write(OutputStream out) throws IOException {
        out.write(array);
    }

    public void write(File file) throws IOException {
        FileUtils.writeByteArrayToFile(file, array);
    }

    public void append(String filePath) throws IOException {
        Files.write(Paths.get(filePath), array, StandardOpenOption.APPEND);
    }

    public void write(String filePath) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(filePath)) {
            write(fos);
        }
    }

    // cache ???
    public ImageIcon getImageIcon() {
        return new ImageIcon(array);
    }

    private String ID;
    public String getID() {
        if(ID == null)
            ID = SystemUtils.generateID(array);
        return ID;
    }
    
    public InputStream getInputStream() {
        return new ByteArrayInputStream(array);
    }        

    @Override
    protected boolean calcTwins(TwinImmutableObject o) {
        return Arrays.equals(array, ((RawFileData) o).array);
    }

    @Override
    public int immutableHashCode() {
        return Arrays.hashCode(array);
    }

    public static RawFileData toRawFileData(Object fileData) {
        return fileData instanceof RawFileData ? (RawFileData)fileData : fileData instanceof FileData ? ((FileData)fileData).getRawFile() : ((NamedFileData) fileData).getRawFile();
    }
}
