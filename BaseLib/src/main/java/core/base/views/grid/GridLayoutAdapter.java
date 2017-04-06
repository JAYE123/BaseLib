package core.base.views.grid;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 刘红亮 on 16/2/4.
 */
public abstract class GridLayoutAdapter {
    private GridLayoutList gridLayoutList;
    protected abstract int getCount();

    protected abstract View getView(int position, View convertView, ViewGroup parent);

    protected  Integer getViewType(int position){
        return null;
    }
    public void bindGridLayoutList(GridLayoutList gridLayoutList){
        this.gridLayoutList=gridLayoutList;
    }
    public  void notifyDataSetChanged(boolean isViewTypeChange){
        gridLayoutList.updateView(isViewTypeChange);
    }

    public GridLayoutList getGridLayoutList() {
        return gridLayoutList;
    }

    public void setGridLayoutList(GridLayoutList gridLayoutList) {
        this.gridLayoutList = gridLayoutList;
    }
}
