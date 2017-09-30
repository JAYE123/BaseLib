package core.base.photo.beans;

import java.util.List;

/**
 * Created by 刘红亮 on 2016/4/9.
 */
public class SelectImageEvent {
    public final static int STATUS_OK=1;
    public final static int STATUS_FAIL=0;
    public final static int STATUS_CANCEL=-1;
    public String flag;
    public int status;
    public List<MediaBean> selectMediaBeans;

    public SelectImageEvent(String flag, int status, List<MediaBean> selectMediaBeans) {
        this.flag = flag;
        this.status = status;
        this.selectMediaBeans = selectMediaBeans;
    }

    public SelectImageEvent() {

    }
}