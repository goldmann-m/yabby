package at.w0mb.jQuery.fileUpload.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public interface iFileMeta {

    @JsonProperty
    public abstract String getDelete_type();

    @JsonProperty
    public abstract String getDelete_url();

    @JsonProperty
    public abstract String getName();

    @JsonProperty
    public abstract long getSize();

    @JsonProperty
    public abstract String getThumbnail_url();

    @JsonProperty
    public abstract String getUrl();

    public abstract void setDelete_type(String delete_type);

    public abstract void setDelete_url(String delete_url);

    public abstract void setName(String name);

    public abstract void setSize(long size);

    public abstract void setThumbnail_url(String thumbnail_url);

    public abstract void setUrl(String url);

    public abstract String getContentType();

    public abstract void setContentType(String contentType);

}