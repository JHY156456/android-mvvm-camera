package com.example.mvvmappapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.dto.SelectDialogDto;
import com.example.mvvmappapplication.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 목록 선택 다이얼로그
 *
 * @author kck
 * @version 1.0
 * @since 2016-11-10
 */
public class SelectListItemDialog extends Dialog {

	private TextView textViewTitle;
	private ListView listView;
	private ImageView mClose;
	private String title;
	private List<SelectDialogDto> selectDialogList = new ArrayList<>();
	private HSDialogSelectAdapter adapter;
	private Context context;
	private onDialogSelectListener dialogItemSelectListener;
	/* 다중 선택 기능 활성화 여부 */
	private boolean isMultySelected = false;

	public interface SORT_TYPE {
		int center = 0;
		int left = 1;
		int right = 2;
	}

	public interface onDialogSelectListener {
		void onSelectedItem(int position, SelectDialogDto item);
	}

	public SelectListItemDialog(Context context) {
		super(context);
		this.context = context;
	}

	public SelectListItemDialog(Context context, String title, String[] itemList, int selectedPos) {
		super(context);
		this.context = context;
		this.title = title;

		int i=0;
		for(String item : itemList) {
			selectDialogList.add(new SelectDialogDto(item, item, (i==selectedPos)));
			i++;
		}
	}



	public SelectListItemDialog(Context context, String title, List<SelectDialogDto> dataList, int selectPosition) {
		super(context);
		this.context = context;
		this.title = title;
		this.selectDialogList = dataList;

		for (int i = 0; i < selectDialogList.size(); i++) {
			SelectDialogDto data = selectDialogList.get(i);
			data.setSelected(i == selectPosition);
		}
	}

	public SelectListItemDialog(Context context, String title, List<SelectDialogDto> items, boolean isMultySelected) {
		super(context);
		this.context = context;
		this.title = title;
		this.selectDialogList = items;
		this.isMultySelected = isMultySelected;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_default_list);

		// 팝업 가로, 세로 사이즈 변경
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.copyFrom(getWindow().getAttributes());
		int maxWidth = (int) (DisplayUtil.getWidthPixels() * 0.88);
		layoutParams.width = maxWidth;
		if (selectDialogList.size() >= 7) {
			layoutParams.height = (int) (DisplayUtil.getHeightPixels() * 0.60);
		}
		getWindow().setAttributes(layoutParams);

		listView = findViewById(R.id.listView);
		textViewTitle = findViewById(R.id.textViewTitle);

		adapter = new HSDialogSelectAdapter(context, selectDialogList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemclicklistener);
		textViewTitle.setText(title);
	}

	AdapterView.OnItemClickListener itemclicklistener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parents, View view, int position, long id) {
			SelectDialogDto selected = selectDialogList.get(position);
			if(selected.isEnabled()) {
				if (isMultySelected) {
					selected.setSelected(!selected.isSelected());
					dialogItemSelectListener.onSelectedItem(position, selected);
					adapter.notifyDataSetChanged();
				} else {
					dialogItemSelectListener.onSelectedItem(position, selected);
					dismiss();
				}
			}
		}
	};

	public void setDialogItemSelectListener(onDialogSelectListener dialogItemSelectListener) {
		this.dialogItemSelectListener = dialogItemSelectListener;
	}
}