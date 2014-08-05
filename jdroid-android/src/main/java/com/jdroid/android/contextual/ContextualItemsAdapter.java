package com.jdroid.android.contextual;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.ActionItem;
import com.jdroid.android.R;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.contextual.ContextualItemsAdapter.ContextualItemHolder;

public class ContextualItemsAdapter extends BaseHolderArrayAdapter<ActionItem, ContextualItemHolder> {
	
	public ContextualItemsAdapter(Activity context, List<ActionItem> actions) {
		super(context, R.layout.contextual_list_item, actions);
	}
	
	@Override
	protected void fillHolderFromItem(ActionItem action, ContextualItemHolder holder) {
		holder.image.setImageResource(action.getIconResource());
		holder.name.setText(action.getNameResource());
	}
	
	@Override
	protected ContextualItemHolder createViewHolderFromConvertView(View convertView) {
		ContextualItemHolder holder = new ContextualItemHolder();
		holder.image = findView(convertView, R.id.image);
		holder.name = findView(convertView, R.id.name);
		return holder;
	}
	
	public static class ContextualItemHolder {
		
		protected ImageView image;
		protected TextView name;
	}
}
