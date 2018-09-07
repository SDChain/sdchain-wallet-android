package com.io.sdchain.net;

import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


public final class FileParams {

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private String boundary = null;
    private String charSet = "utf-8";

    public String getContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    public FileParams() {
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
        final StringBuffer buf = new StringBuffer();
        final Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = buf.toString();
    }

    /**
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * @param key
     * @param file
     */
    public void put(String key, File file) {
        if (file.exists())
            fileParams.put(key, new FileWrapper(file));
    }

    public void writeTo(ByteArrayOutputStream bos) throws IOException {
        int lastIndex = fileParams.entrySet().size();
        StringBuffer sb = new StringBuffer();
        if(lastIndex==0)
        {
            final StringBuffer buf = new StringBuffer();
            final Random rand = new Random();
            for (int i = 0; i < 16; i++) {
                buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
            }
            this.boundary = "Boundary+"+buf.toString();

            sb.append("--" + boundary + "\r\n");
            int mSize=urlParams.size();
            int mCount=0;
            // Add string params
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                sb.append(entry.getValue());
                if (++mCount == mSize) {
                    sb.append("\r\n--" + boundary + "--\r\n");
                } else {
                    sb.append("\r\n--" + boundary + "\r\n");
                }
            }
            bos.write(sb.toString().getBytes());
            bos.flush();
            return;
        }

        sb.append("--" + boundary + "\r\n");
        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            sb.append(entry.getValue());
            sb.append("\r\n--" + boundary + "\r\n");
        }
        bos.write(sb.toString().getBytes());
        bos.flush();
        // Add file params
        int currentIndex = 0;

        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            FileWrapper file = entry.getValue();
            StringBuffer sbf = new StringBuffer();
            sbf.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + file.getFileName() + "\"\r\n");
            sbf.append("Content-Type: " + file.getContentType() + "\r\n");
            sbf.append("Content-Transfer-Encoding: binary\r\n\r\n");
            bos.write(sbf.toString().getBytes());
            final byte[] tmp = new byte[4096];
            int l = 0;
            InputStream is = new FileInputStream(file.getUploadFile());
            while ((l = is.read(tmp)) != -1) {
                bos.write(tmp, 0, l);
            }
            is.close();
            bos.flush();
            if (++currentIndex == lastIndex) {
                bos.write(("\r\n--" + boundary + "--\r\n").getBytes(charSet));
            } else {
                bos.write(("\r\n--" + boundary + "\r\n").getBytes(charSet));
            }
        }
    }

    private static class FileWrapper {
        private File uploadFile;
        private String fileName;
        private String contentType;

        public FileWrapper(File uploadFile) {
            this.uploadFile = uploadFile;
            this.fileName = uploadFile.getName();
            String extension = MimeTypeMap.getFileExtensionFromUrl(uploadFile.getPath());
            if (extension != null) {
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                this.contentType = mime.getMimeTypeFromExtension(extension);
            }
        }

        public File getUploadFile() {
            return uploadFile;
        }

        public String getFileName() {
            return fileName;
        }

        public String getContentType() {
//            return contentType != null ? contentType : "application/octet-stream";
//            return "application/octet-stream";
            return "image/png";
        }
    }
}
