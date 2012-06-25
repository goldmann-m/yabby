package at.w0mb.jQuery.fileUpload.domain;

public interface iFile {

    public abstract byte[] getData();

    public abstract iFileMeta getMeta();

    public abstract String getUuid();

    public abstract void setData(byte[] data);

    public abstract void setMeta(iFileMeta meta);

    public abstract void setUuid(String uuid);
}