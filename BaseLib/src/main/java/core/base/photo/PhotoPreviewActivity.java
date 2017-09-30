package core.base.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import core.base.R;
import core.base.application.BaseActivity;
import core.base.log.L;
import core.base.log.T;
import core.base.photo.beans.MediaBean;
import core.base.photo.beans.SelectStatusEvent;
import core.base.photo.utils.MediaManager;
import core.base.utils.ABAppUtil;
import core.base.utils.ABBitmapUtil;
import core.base.views.viewpager.MultiTouchViewPager;
import uk.co.senab.photoview.PhotoView;

/**
 * 预览界面，包括拍照预览
 */
public class PhotoPreviewActivity extends BaseActivity {
    public final static int TYPE_CAMERA=0;//照相预览
    public final static int TYPE_PHOTO=1;//预览
    public final static int TYPE_SELECT_PHOTO=2;//预览选中的照片
    private static final String TAG = "PhotoPreviewActivity";
    private int type;
    private int currentPosition;
    private String cameraPhotoPath;
    private String floderName;
    private MultiTouchViewPager viewPager;
    private ViewPagerAdapter adapter;
    ImageView ivSelect;
    Button commitBtn;
    TextView tv_num;
    View rl_show_select;
    private List<MediaBean> selectMediaBeans=null;
    private long maxSize =0l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview_2);
        type = getIntent().getIntExtra("type", 1);
        currentPosition = getIntent().getIntExtra("position", 0);
        cameraPhotoPath = getIntent().getStringExtra("cameraPhotoPath");
        floderName = getIntent().getStringExtra("floderName");
        maxSize = ABAppUtil.getDeviceWidth(mContext)
                        * ABAppUtil.getDeviceHeight(mContext);
        setupUI();
    }

    private void setupUI() {
        ivSelect = (ImageView) findViewById(R.id.iv_select);
        tv_num = (TextView) findViewById(R.id.tv_num);
        rl_show_select =  findViewById(R.id.rl_show_select);
        commitBtn = (Button) findViewById(R.id.commit);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==TYPE_CAMERA){//是相机预览
                    MediaManager.getSelectMediaBeans().clear();
                    MediaManager.getSelectMediaBeans().add(adapter.getItem(0));
                }
                if(rl_show_select.getVisibility()==View.VISIBLE) {
                    if (ivSelect.isSelected()) {
                        MediaManager.selectOK();
                        finish();
                    } else {
                        T.s(PhotoPreviewActivity.this, "请选择图片哦！");
                    }
                }else {
                    MediaManager.selectOK();
                    finish();
                }
            }
        });

        viewPager = (MultiTouchViewPager) findViewById(R.id.viewpager);
        if(type==TYPE_CAMERA){
            rl_show_select.setVisibility(View.GONE);
            MediaBean bean=new MediaBean(cameraPhotoPath);
            bean.setIsPhoto(true);
            ArrayList<MediaBean> mediaBeans = new ArrayList<>();
            L.e(TAG, "cameraPhotoPath=" + cameraPhotoPath);
            mediaBeans.add(bean);
            adapter=new ViewPagerAdapter(mediaBeans);
        }else if(type==TYPE_SELECT_PHOTO){
            selectMediaBeans=new LinkedList<>();
            selectMediaBeans.addAll(MediaManager.getSelectMediaBeans());
//            adapter=new ViewPagerAdapter(MediaManager.getSelectMediaBeans()); //不能直接设置，否则更改选中时数据全局的会减少
            adapter=new ViewPagerAdapter(selectMediaBeans);
            L.e(TAG, "选中列表预览：" + MediaManager.getSelectMediaBeans());
        } else if(type==TYPE_PHOTO){
            adapter=new ViewPagerAdapter(MediaManager.getMediaFloder(floderName).getMediaBeanList());
        }
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                L.e("hongliang", "isSelected=" + adapter.getItem(position).isSelected());
                ivSelect.setSelected(adapter.getItem(position).isSelected());
                tv_num.setText((position + 1) + "/" + adapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        L.e("hongliang", "isSelected=" + adapter.getItem(currentPosition).isSelected());
        //第一次显示状态
        ivSelect.setSelected(adapter.getItem(currentPosition).isSelected());
        tv_num.setText((currentPosition+1)+"/"+adapter.getCount());
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ivSelect.setSelected(!ivSelect.isSelected());
                int currentItem = viewPager.getCurrentItem();
                MediaBean mediaBean = adapter.getItem(currentItem);
                mediaBean.setIsSelected(ivSelect.isSelected());
                MediaManager.getSelectMediaBeans().remove(mediaBean);
                if(MediaManager.getSelectMediaBeans().size()>=PhotoPickerActivity.mMaxNum){//已达上限
                    ivSelect.setSelected(false);
                    Toast.makeText(mContext, "只能选取" + PhotoPickerActivity.mMaxNum + "张", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    ivSelect.setSelected(true);
                    if (ivSelect.isSelected()) {
                        MediaManager.getSelectMediaBeans().add(mediaBean);
                    }
                    //通知相册列表更新
                    L.e("hongliang","通知相册列表更新");
                    EventBus.getDefault().post(new SelectStatusEvent(mediaBean.getId(),ivSelect.isSelected()));
                }
            }
        });
    }

    /**
     * 预览相机拍过来的照片
     * @param context
     * @param cameraPhotoPath
     */
    public static void startFromCamera(Context context,String cameraPhotoPath) {
        L.e(TAG, "cameraPhotoPath=" + cameraPhotoPath);
        Intent starter = new Intent(context, PhotoPreviewActivity.class);
        starter.putExtra("type",TYPE_CAMERA);
        starter.putExtra("cameraPhotoPath", cameraPhotoPath);
        context.startActivity(starter);
    }
    /**
     * 预览图片
     * @param context
     * @param floderName
     * @param position
     */
    public static void startPreviewPhoto(Context context,String floderName,int position) {
        Intent starter = new Intent(context, PhotoPreviewActivity.class);
        starter.putExtra("type",TYPE_PHOTO);
        starter.putExtra("position",position);
        starter.putExtra("floderName",floderName);
        context.startActivity(starter);
    }
    /**
     * 预览选中的图片
     * @param context
     */
    public static void startPreviewSelectPhoto(Context context) {
        Intent starter = new Intent(context, PhotoPreviewActivity.class);
        starter.putExtra("type",TYPE_SELECT_PHOTO);
        context.startActivity(starter);
    }
    class ViewPagerAdapter extends PagerAdapter {
        private List<MediaBean> allPicFiles;// 所有图片
        private MediaBean mf;

        public ViewPagerAdapter(List<MediaBean> sysallPicFiles) {
            super();
            this.allPicFiles = sysallPicFiles;
        }


        @Override
        public int getCount() {
            return allPicFiles.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        public MediaBean getItem(int position){
            return allPicFiles.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mf = allPicFiles.get(position);
            if(mf.isPhoto()){
                PhotoView photoView = new PhotoView(container.getContext());
                photoView.setScaleType(ImageView.ScaleType.CENTER);
//                DrawableRequestBuilder builder =Glide.with(mContext)
//                        .load(new File(mf.getRealPath()));
//
//                builder.into(photoView);

                int[] size = ABBitmapUtil.getBitmapWidthAndHeight(mf.getRealPath());
                if(size[0]*size[1] > maxSize){

                    Glide.with(mContext)
                            .load(new File(mf.getRealPath()))
                            .dontAnimate()
                            .dontTransform()
                            .override(ABAppUtil.getDeviceWidth(mContext) > size[0] ? size[0] : ABAppUtil.getDeviceWidth(mContext),
                                    ABAppUtil.getDeviceHeight(mContext) > size[1] ? size[1] : ABAppUtil.getDeviceHeight(mContext))
                            .into(photoView);
                }else{
                    Glide.with(mContext)
                            .load(new File(mf.getRealPath()))
                            .dontAnimate()
                            .dontTransform()
                            .into(photoView);
                }






                container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                return photoView;
            }

            return null;
        }
    }
}
