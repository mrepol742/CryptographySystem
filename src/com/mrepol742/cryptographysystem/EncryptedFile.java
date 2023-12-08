package com.mrepol742.cryptographysystem;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.Tika;

/**
 *
 * @author mrepol742
 */
public class EncryptedFile {

    private File selectedFile;
    private String content;
    private String mimetype;
    private String fileFormat;

    public EncryptedFile(File selectedFile, String content) {
        this.selectedFile = selectedFile;
        this.content = content;
        try {
            Tika tika = new Tika();
            String mimeType = tika.detect(selectedFile);
            mimetype = mimeType.split("/")[0];
            fileFormat = mimeType.split("/")[1];
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public File getFile() {
        return selectedFile;
    }
    
    public String getContent() {
        return content;
    }

    public String getMimetype() {
        return mimetype;
    }
    
    public String getFileFormat(boolean isEncode) {
     //   if (isEncode) {
            return fileFormat;
      /*  }
        String[] filename = getFile().getName().replace(".enc", "").split("\\.");
        return filename[filename.length - 1].replace(".", ""); */
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
