package com.masterthesis.personaldata.symptoms.dragNdrop;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.masterthesis.personaldata.symptoms.R;

import java.util.List;

/**
 * Created by Konstantinos Michail on 2/21/2016.
 */
public class SymptomItemAdapter extends ArrayAdapter<SymptomItem> implements SpanVariableGridView.CalculateChildrenPosition {

    private Context mContext;
    private LayoutInflater mLayoutInflater = null;
    private View.OnClickListener onRemoveItemListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Integer position = (Integer) view.getTag();
            removeItem(getItem(position));

        }
    };

    public SymptomItemAdapter(Context context, List<SymptomItem> plugins) {

        super(context, R.layout.item, plugins);

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void insertItem(SymptomItem item, int where) {

        if (where < 0 || where > (getCount() - 1)) {

            return;
        }

        insert(item, where);
    }

    public boolean removeItem(SymptomItem item) {

        remove(item);

        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ItemViewHolder itemViewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item, parent, false);

            itemViewHolder = new ItemViewHolder();
            itemViewHolder.itemTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            itemViewHolder.itemDescription = (TextView) convertView.findViewById(R.id.textViewDescription);
            itemViewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            // The first item of the list is considered the active Diary so make it green
            if (position == 0) {
                convertView.setBackgroundColor(Color.GREEN);
            }
            convertView.setTag(itemViewHolder);

        } else {

            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }

        final Item item = getItem(position);

        SpanVariableGridView.LayoutParams lp = new SpanVariableGridView.LayoutParams(convertView.getLayoutParams());
        lp.span = item.getSpans();
        convertView.setLayoutParams(lp);

        itemViewHolder.itemTitle.setText(item.getTitle());
        itemViewHolder.itemDescription.setText(item.getDescription());
        itemViewHolder.itemIcon.setImageResource(item.getIcon());

        return convertView;
    }

    @Override
    public void onCalculatePosition(View view, int position, int row, int column) {

    }

    private final class ItemViewHolder {

        public TextView itemTitle;
        public TextView itemDescription;
        public ImageView itemIcon;

    }
}