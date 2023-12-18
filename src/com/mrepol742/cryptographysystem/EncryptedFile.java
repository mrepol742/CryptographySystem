package com.mrepol742.cryptographysystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        String[] split = selectedFile.getName().split("\\.");
        fileFormat = split[split.length - 1];
        try {
            mimetype = Files.probeContentType(selectedFile.toPath());
            if (mimetype != null) {
                mimetype = mimetype.split("/")[0];
            } else {
                mimetype = "default";
            }
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Selected File: {0}\nMimetype: {1}\nFile Format:{2}", new Object[]{selectedFile.getName(), mimetype, fileFormat});
        } catch (IOException ex) {
            mimetype = "default";
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
        //if (isEncode) {
        return fileFormat;
        /*}
        String[] filename = getFile().getName().replace(".enc", "").split("\\.");
        return filename[filename.length - 1].replace(".", ""); */
    }

    public void setContent(String content) {
        this.content = content;
    }
}
