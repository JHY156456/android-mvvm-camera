package com.example.mvvmappapplication.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.dto.SelectDialogDto;

import java.util.List;


/**
 * Some descriptions here
 *
 * @author kck
 * @version 1.0
 * @since 2016-11-10
 */
public class HSDialogSelectAdapter extends BaseAdapter {
	LayoutInflater mInflater;
	private List<SelectDialogDto> itemList;

	int resource = R.layout.item_select_dialog;
	public HSDialogSelectAdapter(Context context, List<SelectDialogDto> items) {
		mInflater = LayoutInflater.from(context);
		this.itemList = items;
	}

	@Override
	public int getCount() {
		return itemList != null ? itemList.size() : 0;
	}

	@Override
	public SelectDialogDto getItem(int position) {
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(resource, parent, false);
			holder = new ViewHolder();
			holder.layoutItem = convertView.findViewById(R.id.layoutItem);
			holder.textViewItem = convertView.findViewById(R.id.textViewItem);
			holder.checkIcon = convertView.findViewById(R.id.checkIcon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		SelectDialogDto item = itemList.get(position);
		holder.textViewItem.setSelected(item.isSelected());
		holder.checkIcon.setSelected(item.isSelected());
		holder.textViewItem.setText(item.getValue());

		if (item.isVisible()) {
			holder.layoutItem.setVisibility(View.VISIBLE);
		} else {
			holder.layoutItem.setVisibility(View.GONE);
		}

		holder.textViewItem.setEnabled(item.isEnabled());

		return convertView;
	}

	class ViewHolder {
		View layoutItem;
		TextView textViewItem;
		View checkIcon;
	}

}