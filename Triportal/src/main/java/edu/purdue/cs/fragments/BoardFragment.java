/**
 * Copyright 2014 Magnus Woxblom
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.purdue.cs.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.*;

import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;
import edu.purdue.cs.*;
import edu.purdue.cs.Adapter.BoardAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class BoardFragment extends Fragment {

    private static int sCreatedItems = 0;
    private BoardView mBoardView;
    private int mColumns;
    private Itinerary mItinerary;
    private List<Day> mDayList;
    private List<View> mHeaderList;
    private ImageButton mAddBtn;
    private ImageButton mSearchBtn;
    private Object[] mColumnList;
    private int mInitColumn = 0;
    private Activity mActivity;

    private boolean isOwned;

    static final private int SEARCH_REQUEST_CODE = 789;

    public final static int CONFIRM_POI_DELETE = 1;

    //private View tClickedHeader;
    private int mlastClickedColumn = 0;
    //private ImageButton imageB

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mlastClickedColumn == mColumns -1) {
            scrollToEnd();
        }
        mBoardView.scrollToColumn(mlastClickedColumn,false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_layout, container, false);
        mAddBtn = (ImageButton) view.findViewById(R.id.board_create_btn);
        mSearchBtn = (ImageButton) view.findViewById(R.id.board_search_btn);
        //TODO: temporarily change to invisible due to workaround
        mSearchBtn.setVisibility(View.GONE);
        mBoardView = (BoardView) view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.board_item));
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
                //Toast.makeText(mBoardView.getContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = (TextView) mBoardView.getHeaderView(oldColumn).findViewById(R.id.board_header_text_2);
                itemCount1.setText(Integer.toString(mBoardView.getAdapter(oldColumn).getItemCount()));
                TextView itemCount2 = (TextView) mBoardView.getHeaderView(newColumn).findViewById(R.id.board_header_text_2);
                itemCount2.setText(Integer.toString(mBoardView.getAdapter(newColumn).getItemCount()));
            }

            @Override
            @SuppressWarnings("unchecked")
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    List<Pair<Long,Poi>> listFrom =  mBoardView.getAdapter(fromColumn).getItemList();
                    List<Pair<Long,Poi>> listTo =  mBoardView.getAdapter(toColumn).getItemList();
                    List<Poi> newPoiList = new ArrayList<>(listFrom.size() + listTo.size());



                    for(Pair<Long,Poi> pair : listFrom) {
                        newPoiList.add(pair.second);
                    }

                    try {
                        mDayList.get(fromColumn).setPoiList(newPoiList);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(fromColumn == toColumn)
                        return;
                    newPoiList.clear();
                    for(Pair<Long,Poi> pair : listTo) {
                        newPoiList.add(pair.second);
                    }
                    //newPoiList.add(toRow,tPoi);
                    try {
                        mDayList.get(toColumn).setPoiList(newPoiList);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    //Toast.makeText(mBoardView.getContext(), "End - column: " + toColumn + " row: " + toRow, Toast.LENGTH_SHORT).show();
                }
            }
        });

        setupButtonListener();


        return view;
    }

    private void setupButtonListener() {
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  int currentColumn = mBoardView.getCurrentColumn();
               // Toast.makeText(getContext(), "Add Day to Col " + currentColumn, Toast.LENGTH_SHORT).show();
                mItinerary.addOneDayInBackground(new GetCallback<Day>() {
                    @Override
                    public void done(Day day, ParseException e) {
                        List<Poi> poiList;
                        try {
                            poiList = day.getPoiList();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            return;
                        }
                        BoardFragment.this.mDayList.add(day);
                        BoardFragment.this.addColumnList(poiList);
                    }
                });

                //mBoardView.scrollToColumn(0,true );
//                try {
//                    mItinerary.addOneDay();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add Poi", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        try {
            mItinerary = Itinerary.getById(bundle.getString("it_ID"));
        } catch (Exception e) {
            Log.d("BoardFragment", "Itinerary getbyID fail");
        }

        if(!mItinerary.getOwner().getObjectId().equals(bundle.getString("user_ID"))) {
            isOwned = false;
            mBoardView.setDragEnabled(false);
            mAddBtn.setVisibility(View.GONE);
        } else {
            isOwned = true;
        }

        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Board");
        mItinerary.getDaysInBackground(new FindCallback<Day>() {
            @Override
            public void done(List<Day> objects, ParseException e) {
                Log.d("BoardFragment", "Done fetching dayList");
                BoardFragment.this.mDayList = objects;
                initiDayList();
            }
        });




        //TODO: refresh list
        //addColumnList();
        //addColumnList();
        //addColumnList();
        //addColumnList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       // inflater.inflate(R.menu.menu_board, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
       // menu.findItem(R.id.action_disable_drag).setVisible(mBoardView.isDragEnabled());
        //menu.findItem(R.id.action_enable_drag).setVisible(!mBoardView.isDragEnabled());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO: this is redundent in our project
/*        switch (item.getItemId()) {
            case R.id.action_disable_drag:
                mBoardView.setDragEnabled(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_enable_drag:
                mBoardView.setDragEnabled(true);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_add_column:
                addColumnList();
                return true;
            case R.id.action_remove_column:
                mBoardView.removeColumn(0);
                return true;
            case R.id.action_clear_board:
                mBoardView.clearBoard();
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void initiDayList() {
        mHeaderList =  new ArrayList<>();
        mColumnList = new Object[mDayList.size()];
        mInitColumn = mDayList.size();
        mColumns = 0;
        for(final Day tDay : mDayList) {
            tDay.getPoiListInBackground(new FindCallback<Poi>() {
                @Override
                public void done(List<Poi> objects, ParseException e) {
                    addColumnList(objects,tDay.getDayIndex());
                    if(mColumns == mInitColumn) {
                        onFinishInitList();
                    }
                }
            });
        }
    }

    private void addColumnList(List<Poi> PoiList) {
        //TODO: turn this into adding days

        final ArrayList<Pair<Long, Poi>> mItemArray = new ArrayList<>();
        //The initial items in a day should be zero

        for(Poi poi : PoiList) {
            long id = sCreatedItems++;
            mItemArray.add(new Pair<>(id, poi));
        }

        final int column = mColumns;
        //TODO: this adapter need to be refine to fit in out project
        final BoardAdapter listAdapter = new BoardAdapter(mItemArray, R.layout.board_item,
                                                        R.id.board_item_layout, true,this,isOwned);
        final View header = View.inflate(mActivity, R.layout.board_column_header, null);
        mHeaderList.add(header);
        ((TextView) header.findViewById(R.id.board_header_text)).setText("Day " + (mColumns + 1));
        ((TextView) header.findViewById(R.id.board_header_text_2)).setText("" + PoiList.size());
        //associate the column index to the header to be updated and used by other function
        header.setTag(column);
        if(isOwned) {
            header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: click on the title to add another POI in day; we still need a place to add day
                    Intent intent = new Intent(getActivity(), PoiSearchView.class);
                    intent.putExtra("ClickedColumn", (Integer) header.getTag());
                    startActivityForResult(intent, SEARCH_REQUEST_CODE);
                }
            });
            header.setOnLongClickListener(new PopUpMenuListtener(header));
        }




        mBoardView.addColumnList(listAdapter, header, false);
        mColumnList[column] = new Pair<>(listAdapter,header);
        mColumns++;
      //  mBoardView.scrollToColumn(mColumns-2,true);
        scrollToEnd();
    }


    private void addColumnList(List<Poi> PoiList, int index) {
        //TODO: turn this into adding days

        final ArrayList<Pair<Long, Poi>> mItemArray = new ArrayList<>(PoiList.size());
        //The initial items in a day should be zero

        for(Poi poi : PoiList) {
            long id = sCreatedItems++;
            mItemArray.add(new Pair<>(id, poi));
        }

        final int column = mColumns;
        //TODO: this adapter need to be refine to fit in out project
        final BoardAdapter listAdapter = new BoardAdapter(mItemArray, R.layout.board_item,
                                                        R.id.board_item_layout, true,this,isOwned);
        final View header = View.inflate(mActivity, R.layout.board_column_header, null);
        mHeaderList.add(header);
        ((TextView) header.findViewById(R.id.board_header_text)).setText("Day " + (index + 1));
        ((TextView) header.findViewById(R.id.board_header_text_2)).setText("" + PoiList.size());


        //mBoardView.addColumnList(listAdapter, header, false);
        mColumnList[index] = new Pair<>(listAdapter,header);
        mColumns++;

    }

    @SuppressWarnings("unchecked")
    private void removeColumn(final int cIndex) {
       // Object[] newColumnList = new Object[--mColumns];
        //Pair<BoardAdapter,View> pair = (Pair<BoardAdapter, View>) mColumnList[cIndex];
        Day day = (Day) mDayList.get(cIndex);
        List<Object> newColumnList = new ArrayList<>(Arrays.asList(mColumnList));
        mHeaderList.remove(cIndex);
        newColumnList.remove(cIndex);
        mDayList.remove(cIndex);
        mColumns--;


        //update header and index of the list
        for(int i = 0; i < newColumnList.size(); i++) {

            Pair<BoardAdapter,View> pair = (Pair<BoardAdapter, View>) newColumnList.get(i);
            pair.second.setTag(i);
            ((TextView) pair.second.findViewById(R.id.board_header_text)).setText("Day " +(i+1));
        }

        mColumnList = newColumnList.toArray();
        mBoardView.removeColumn(cIndex);

        mItinerary.removeDayAtInBackground(cIndex, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("Remove Day:","remove " +cIndex+" is done");
            }
        });


    }

    private void scrollToEnd() {
        mBoardView.post(new Runnable() {
            @Override
            public void run() {
                mBoardView.fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }


    @SuppressWarnings("unchecked")
    private void onFinishInitList() {
        int column = 0;
        for(Object o : mColumnList) {
            final int final_column = column;
            Pair<BoardAdapter,View> pair = (Pair<BoardAdapter,View>) o;
            pair.second.setTag(final_column);
            final View header = pair.second;
            if(isOwned) {
                pair.second.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: click on the title to add another POI in day; we still need a place to add day
                        Log.d("Clicked Header ", "" + (Integer) header.getTag());


                        Intent intent = new Intent(getActivity(), PoiSearchView.class);
                        intent.putExtra("ClickedColumn", (Integer) header.getTag());
                        startActivityForResult(intent, SEARCH_REQUEST_CODE);

                    }
                });
                header.setOnLongClickListener(new PopUpMenuListtener(header));
            }

            mBoardView.addColumnList(pair.first,pair.second,false);
            column++;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String poi_id = data.getStringExtra("poi_id");
                int tClickedColumn = data.getExtras().getInt("ClickedColumn");
                Poi poi;
                try {
                    poi = Poi.getById(poi_id);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                long id = sCreatedItems++;
                Pair<Long,Poi> item = new Pair<>(id, poi);
                Log.d("onActivityResult Column","" + tClickedColumn);
                mBoardView.addItemtoEnd(tClickedColumn, item, false);
                BoardAdapter adapter = (BoardAdapter)  mBoardView.getAdapter(tClickedColumn);
                ((TextView) mHeaderList.get(tClickedColumn).findViewById(R.id.board_header_text_2)).setText("" + adapter.getItemCount());
                Log.d("onActivityResult Column","" + tClickedColumn);
               // mBoardView.scrollToColumnWithUpdate(tClickedColumn,true);
                mlastClickedColumn = tClickedColumn;
                mBoardView.scrollToColumn(tClickedColumn,false);
                List<Pair<Long,Poi>> pairList = adapter.getItemList();
                List<Poi> newPoiList = new ArrayList<Poi>();



                for(Pair<Long,Poi> pair : pairList) {
                    newPoiList.add(pair.second);
                }

                try {
                    mDayList.get(tClickedColumn).setPoiList(newPoiList);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == CONFIRM_POI_DELETE) {
            if (resultCode == Activity.RESULT_OK) {
                String poi_id = data.getStringExtra("poi_id");
                Boolean deleted = data.getBooleanExtra("deleted", false);
                if (deleted) {
                    Log.d("Poi", poi_id + " deleted");
                }
            }
        }
    }

    private static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.board_item_text)).getText();
            ((TextView) dragView.findViewById(R.id.board_item_text)).setText(text);
            CardView dragCard = ((CardView) dragView.findViewById(R.id.board_item_card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.board_item_card));

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            // I know the dragView is a FrameLayout and that is why I can use setForeground below api level 23
            dragCard.setForeground(clickedView.getResources().getDrawable(R.drawable.board_card_view_drag_foreground));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.board_item_card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.board_item_card));
            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft() + dragCard.getPaddingRight() -
                    clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop() + dragCard.getPaddingBottom() -
                    clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.board_item_card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.board_item_card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

    }

    private class PopUpMenuListtener implements PopupMenu.OnMenuItemClickListener,View.OnLongClickListener {

        View mView;


        //THIS CAN ONLY USED BY HEADER VIEW
        public PopUpMenuListtener(View view) {
            this.mView =  view;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.day_list_item_delete:
                    BoardFragment.this.removeColumn((Integer) mView.getTag());
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            PopupMenu popupMenu = new PopupMenu(BoardFragment.this.getContext(), v);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.inflate(R.menu.menu_daylist_header);
            popupMenu.show();

            return false;
        }
    }



}
