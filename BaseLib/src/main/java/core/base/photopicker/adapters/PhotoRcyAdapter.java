package core.base.photopicker.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import core.base.R;
import core.base.log.L;
import core.base.photopicker.PhotoPickerActivity;
import core.base.photopicker.PhotoPreviewActivity_2;
import core.base.photopicker.beans.MediaBean;
import core.base.photopicker.utils.MediaManager;
import core.base.photopicker.utils.OtherUtils;
import core.base.utils.GlideDisplay;

/**
 * Created by 刘红亮 on 2015/12/3.
 */
public class PhotoRcyAdapter extends RecyclerView.Adapter {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_PHOTO = 1;
    private static final String TAG ="PhotoRcyAdapter" ;
    private List<MediaBean> mDatas;
    private String floderName;
    private Context mContext;
    private int mWidth;
    //是否显示相机，默认不显示
    private boolean mIsShowCamera = false;
    //图片选择数量
    private int mMaxNum = PhotoPickerActivity.DEFAULT_NUM;

    private PhotoSelectListener photoSelectListener;

    public PhotoRcyAdapter(Context context,String floderName, List<MediaBean> mDatas,PhotoSelectListener photoSelectListener) {
        this.mDatas = mDatas;
        this.mContext = context;
        this.floderName=floderName;
        int screenWidth = OtherUtils.getWidthInPx(mContext);
        mWidth = (screenWidth - OtherUtils.dip2px(mContext, 4)) / 3;
        this.photoSelectListener=photoSelectListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item_camera_layout, parent, false);
            //设置高度等于宽度
            GridLayoutManager.LayoutParams layoutParams = new GridLayoutManager.LayoutParams(mWidth, mWidth);
            view.setLayoutParams(layoutParams);
            return new CameraHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item_layout, parent, false);
            GridLayoutManager.LayoutParams layoutParams = new GridLayoutManager.LayoutParams(mWidth, mWidth);
            view.setLayoutParams(layoutParams);
            return new PhotoHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_CAMERA:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(photoSelectListener!=null) {
                            photoSelectListener.gotoCamera();
                        }
                    }
                });
                break;
            case TYPE_PHOTO:
                final PhotoHolder photoHolder = (PhotoHolder) holder;
                final int index=getRealPosition(position);
                photoHolder.selectView.setVisibility(View.VISIBLE);
                photoHolder.selectView.setSelected(mDatas.get(index).isSelected());//设置显示选择切换
                //设置蒙版切换
                photoHolder.maskView.setVisibility(mDatas.get(index).isSelected() ? View.VISIBLE : View.GONE);
                //设置选中切换
                photoHolder.selectView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mDatas.get(index).isSelected()){//原来选中，现在是取消选中
                            photoHolder.selectView.setSelected(false);
                            photoHolder.maskView.setVisibility(View.GONE);
                            mDatas.get(index).setIsSelected(false);//更改数据为未选中
                            MediaManager.getSelectMediaBeans().remove(mDatas.get(index));
                            L.e(TAG, "选中列表移除了：" + mDatas.get(index).getName());
                        }else{//选中
                            if(MediaManager.getSelectMediaBeans().size()>=mMaxNum){//已达上限
                                Toast.makeText(mContext,"只能选取"+mMaxNum+"张",Toast.LENGTH_LONG).show();
                                return;
                            }else{
                                photoHolder.selectView.setSelected(true);
                                photoHolder.maskView.setVisibility(View.VISIBLE);
                                //先移除，保证不重复
                                MediaManager.getSelectMediaBeans().remove(mDatas.get(index));
                                MediaManager.getSelectMediaBeans().add(mDatas.get(index));
                                mDatas.get(index).setIsSelected(true);//更改数据为选中
                                L.e(TAG, "选中列表添加了：" + MediaManager.getSelectMediaBeans());
                            }
                        }
                        if(photoSelectListener!=null) {
                            photoSelectListener.photoSelectChange(index,mDatas.get(index).getId(),mDatas.get(index).isSelected());
                        }
                    }
                });
                //预览
//                photoHolder.photoImageView.setAspectRatio(1);
                photoHolder.photoImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(mContext, "去预览", Toast.LENGTH_LONG).show();
                        PhotoPreviewActivity_2.startPreviewPhoto(mContext, floderName, index);

                    }
                });



                photoHolder.photoImageView.getLayoutParams().width=photoHolder.photoImageView.getLayoutParams().height=mWidth;
//                photoHolder.photoImageView.setImageURI(Uri.fromFile(new File(mDatas.get(index).getRealPath())));
//                ImageRequest imageRequest = ImageRequestBuilder
//                        .newBuilderWithSource(Uri.fromFile(new File(mDatas.get(index).getRealPath())))
//                        .setResizeOptions(new ResizeOptions(mWidth, mWidth))//图片目标大小
//                        .build();

                GlideDisplay.display(photoHolder.photoImageView,new File(mDatas.get(index).getRealPath()));
//                DraweeController controller = Fresco.newDraweeControllerBuilder()
//                        .setOldController(photoHolder.photoImageView.getController())
//                        .setImageRequest(imageRequest)
//                        .build();
//                photoHolder.photoImageView.setController(controller);
//                Picasso.with(mContext).load(new File(mDatas.get(index).getRealPath())).centerCrop().resize(mWidth, mWidth).placeholder(R.drawable.photo_ic_loading)
//                        .error(R.drawable.photo_ic_loading).into(photoHolder.photoImageView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(mIsShowCamera){
            return mDatas == null ? 1 : mDatas.size()+1;
        }
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mIsShowCamera) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PHOTO;
        }
    }
    public Integer getRealPosition(int position){
        if (mIsShowCamera) {
            if (position == 0) {
                return null;
            }
            return position - 1;
        } else {
            return position;
        }
    }


    public void setDatas(List<MediaBean> mDatas) {
        this.mDatas = mDatas;
    }
    public List<MediaBean> getDatas(){
        return mDatas;
    }

    public void setIsShowCamera(boolean isShowCamera) {
        this.mIsShowCamera = isShowCamera;
    }

    public boolean isShowCamera() {
        return mIsShowCamera;
    }

    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }


    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView photoImageView;
        private ImageView selectView;
        private View maskView;
        private FrameLayout wrapLayout;

        public PhotoHolder(View itemView) {
            super(itemView);
            photoImageView = (ImageView) itemView.findViewById(R.id.imageview_photo);
            selectView = (ImageView) itemView.findViewById(R.id.checkmark);
            maskView = itemView.findViewById(R.id.mask);
            wrapLayout = (FrameLayout) itemView.findViewById(R.id.wrap_layout);
        }
    }

    private class CameraHolder extends RecyclerView.ViewHolder {
        public CameraHolder(View itemView) {
            super(itemView);
        }
    }
    public interface PhotoSelectListener{
        public void photoSelectChange(int index, int ImageId, boolean isSelect);
        public void gotoCamera();
    }
}
