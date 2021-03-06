package com.jdroid.android.recycler;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

public abstract class RecyclerViewType<ITEM, VIEWHOLDER extends RecyclerView.ViewHolder> implements View.OnClickListener {

	private final static Logger LOGGER = LoggerUtils.getLogger(RecyclerViewType.class);

	public View inflateView(LayoutInflater inflater, ViewGroup parent) {
		View view = inflater.inflate(getLayoutResourceId(), parent, false);
		if (isClickable()) {
			view.setOnClickListener(this);
		}
		return view;
	}

	protected abstract Class<ITEM> getItemClass();

	protected abstract Integer getLayoutResourceId();

	/**
	 * Creates a VIEWHOLDER from the given view. Please declare the VIEWHOLDER class as static when possible
	 *
	 * @param view The view from the list.
	 * @return The new VIEWHOLDER.
	 */
	public abstract RecyclerView.ViewHolder createViewHolderFromView(View view);

	/**
	 * Fills the VIEWHOLDER with the Object's data.
	 *
	 * @param item The Object.
	 * @param holder The VIEWHOLDER.
	 */
	public abstract  void fillHolderFromItem(ITEM item, VIEWHOLDER holder);

	public abstract AbstractRecyclerFragment getAbstractRecyclerFragment();

	protected Boolean isClickable() {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onClick(View view) {
		int itemPosition = getAbstractRecyclerFragment().getRecyclerView().getChildAdapterPosition(view);
		if (itemPosition != RecyclerView.NO_POSITION) {
			onItemSelected((ITEM)getAbstractRecyclerFragment().getAdapter().getItem(itemPosition), view);
		} else {
			LOGGER.warn("Ignored onClick for item with no position");
		}
	}

	public void onItemSelected(ITEM item, View view) {
		// Do Nothing
	}

	/**
	 * Finds a view that was identified by the id attribute from the {@link View} view.
	 *
	 * @param containerView The view that contains the view to find.
	 * @param id The id to search for.
	 * @param <V> The {@link View} class.
	 *
	 * @return The view if found or null otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(View containerView, int id) {
		return (V)containerView.findViewById(id);
	}

	protected Context getContext() {
		return getAbstractRecyclerFragment().getContext();
	}

	protected Activity getActivity() {
		return getAbstractRecyclerFragment().getActivity();
	}
}
