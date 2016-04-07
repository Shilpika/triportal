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
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;
import edu.purdue.cs.R;
import edu.purdue.cs.Adapter.BoardAdapter;
import edu.purdue.cs.Day;
import edu.purdue.cs.Poi;
import edu.purdue.cs.Itinerary;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {

    private static int sCreatedItems = 0;
    private BoardView mBoardView;
    private int mColumns;
    private Itinerary mItinerary;
    private List<Day> mDayList;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_layout, container, false);

        mBoardView = (BoardView) view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.board_item));
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
                Toast.makeText(mBoardView.getContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = (TextView) mBoardView.getHeaderView(oldColumn).findViewById(R.id.board_header_text_2);
                itemCount1.setText(Integer.toString(mBoardView.getAdapter(oldColumn).getItemCount()));
                TextView itemCount2 = (TextView) mBoardView.getHeaderView(newColumn).findViewById(R.id.board_header_text_2);
                itemCount2.setText(Integer.toString(mBoardView.getAdapter(newColumn).getItemCount()));
            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    Toast.makeText(mBoardView.getContext(), "End - column: " + toColumn + " row: " + toRow, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
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
        for(Day tDay : mDayList) {
            tDay.getPoiListInBackground(new FindCallback<Poi>() {
                @Override
                public void done(List<Poi> objects, ParseException e) {
                    addColumnList(objects);

                }
            });
        }
    }

    private void addColumnList(List<Poi> PoiList) {
        //TODO: turn this into adding days

        final ArrayList<Pair<Long, String>> mItemArray = new ArrayList<>();
        //The initial items in a day should be zero
        int addItems = 2;
        for (int i = 0; i < addItems; i++) {
            long id = sCreatedItems++;
            mItemArray.add(new Pair<>(id, "POI " + id));
        }

/*        for(Poi poi : PoiList) {
            long id = sCreatedItems++;
            mItemArray.add(new Pair<>(id, poi.getName()));
        }*/

        final int column = mColumns;
        //TODO: this adapter need to be refine to fit in out project
        final BoardAdapter listAdapter = new BoardAdapter(mItemArray, R.layout.board_item, R.id.board_item_layout, true);
        final View header = View.inflate(getActivity(), R.layout.board_column_header, null);
        ((TextView) header.findViewById(R.id.board_header_text)).setText("Day " + (mColumns + 1));
        ((TextView) header.findViewById(R.id.board_header_text_2)).setText("" + addItems);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: click on the title to add another POI in day; we still need a place to add day
                //clicking this will direct the user to search screen to add POI
               // long id = sCreatedItems++;
               // Pair item = new Pair<>(id, "Test " + id);
                //mBoardView.addItem(column, 0, item, true);
                //mBoardView.moveItem(4, 0, 0, true);
                //mBoardView.removeItem(column, 0);
                //mBoardView.moveItem(0, 0, 1, 3, false);
                //mBoardView.replaceItem(0, 0, item1, true);
               // ((TextView) header.findViewById(R.id.board_header_text_2)).setText("" + mItemArray.size());
            }
        });

        mBoardView.addColumnList(listAdapter, header, false);
        mColumns++;
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
}
