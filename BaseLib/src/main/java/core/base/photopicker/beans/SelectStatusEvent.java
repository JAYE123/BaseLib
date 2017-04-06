package core.base.photopicker.beans;

/**
 * Created by 刘红亮 on 2015/12/4.
 */
public class SelectStatusEvent {
    public int imageId;
    public boolean isSelect;

    public SelectStatusEvent(int imageId, boolean isSelect) {
        this.imageId = imageId;
        this.isSelect = isSelect;
    }

    @Override
    public String toString() {
        return "SelectStatusEvent{" +
                "imageId=" + imageId +
                ", isSelect=" + isSelect +
                '}';
    }
}
