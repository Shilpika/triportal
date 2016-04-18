/**
 * Copyright 2014 Magnus Woxblom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.purdue.cs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.woxthebox.draglistview.DragItemAdapter;
import edu.purdue.cs.Poi;
import edu.purdue.cs.PoiDetailView;
import edu.purdue.cs.R;
import edu.purdue.cs.fragments.BoardFragment;

import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends DragItemAdapter<Pair<Long, Poi>, BoardAdapter.ViewHolder>  {

    private int mLayoutId;
    private int mGrabHandleId;
    private BoardFragment mFragment;
    private boolean isOwned = true;
    private View mHeader;

    public BoardAdapter(ArrayList<Pair<Long, Poi>> list, int layoutId, int grabHandleId, boolean dragOnLongPress, BoardFragment fragment) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mFragment = fragment;
        setHasStableIds(true);
        setItemList(list);
    }
    public BoardAdapter(ArrayList<Pair<Long, Poi>> list,
                        int layoutId, int grabHandleId, boolean dragOnLongPress,
                        BoardFragment fragment,Boolean isOwned,View headerView) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mFragment = fragment;
        setHasStableIds(true);
        setItemList(list);
        this.isOwned = isOwned;
        this.mHeader = headerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second.getName();
        holder.mText.setText(text);
        holder.itemView.setTag(text);
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    public View getHeader() {
        return mHeader;
    }

    public int getColumnIndex() { return (Integer) mHeader.getTag();}

    public class ViewHolder extends DragItemAdapter<Pair<Long, Poi>, ViewHolder>.ViewHolder {
        public TextView mText;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId);
            //TODO: add information for the poi
            mText = (TextView) itemView.findViewById(R.id.board_item_text);
        }

        @Override
        public void onItemClicked(View view) {
            //Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();

            Activity activity = mFragment.getActivity();
            Intent intent = new Intent(activity, PoiDetailView.class);
            Poi poi = mItemList.get(BoardAdapter.this.getPositionForItemId(ViewHolder.this.mItemId)).second;
            try {
                poi.pin();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            intent.putExtra("poi_id",poi.getObjectId());
            if(isOwned) {
                intent.putExtra("edit_mode", PoiDetailView.DELETE_MODE);
            } else {
                intent.putExtra("edit_mode", PoiDetailView.VIEW_MODE);
            }
            intent.putExtra("ClickedColumn",(Integer) mHeader.getTag());
            mFragment.setLastClickedColumn((Integer) mHeader.getTag());
            intent.putExtra("ClickedID",(ViewHolder.this.mItemId));
            mFragment.startActivityForResult(intent, BoardFragment.CONFIRM_POI_DELETE);
        }

        @Override
        public boolean onItemLongClicked(View view) {
            //Toast.makeText(view.getContext(), "Show Option", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
