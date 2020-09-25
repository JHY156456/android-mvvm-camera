package com.example.mvvmappapplication.custom.dto;

import android.graphics.drawable.Drawable;

import com.example.mvvmappapplication.R;


public class TableTypeDto {

    public enum ItemViewType {
        ITEM_VIEW_TYPE_EDITTEXT_LINE_1_VERIFCODE(R.layout.layout_untact_table_type_content_edittext_line_1_verifcode),
        ITEM_VIEW_TYPE_EDITTEXT_LINE_1(R.layout.layout_untact_table_type_content_edittext_line_1),
        ITEM_VIEW_TYPE_EDITTEXT_LINE_2(R.layout.layout_untact_table_type_content_edittext_line_2),
        ITEM_VIEW_TYPE_SECRET_KEYPAD(R.layout.layout_untact_table_type_content_secret_keypad),
        ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_1(R.layout.layout_untact_table_type_content_horizontal_scroll_1),
        ITEM_VIEW_TYPE_HORIZONTAL_SCROLL_2(R.layout.layout_untact_table_type_content_horizontal_scroll_2),
        ITEM_VIEW_TYPE_SWITCH_BASE(R.layout.layout_untact_table_type_content_switch_base),
        ITEM_VIEW_TYPE_SWITCH_WITH_IMG(R.layout.layout_untact_table_type_content_switch_with_img),
        ITEM_VIEW_TYPE_BUTTON_LINE_1(R.layout.layout_untact_table_type_content_button_line_1),
        ITEM_VIEW_TYPE_BUTTON_LINE_2(R.layout.layout_untact_table_type_content_button_line_2),
        ITEM_VIEW_TYPE_BUTTON_LINE_2_JOB(R.layout.layout_untact_table_type_content_button_line_2_job),
        ITEM_VIEW_TYPE_BUTTON_LINE_2_ADDRESS(R.layout.layout_untact_table_type_content_button_line_2_address),
        ITEM_VIEW_TYPE_TEXT_LINE_1_DISPLAY(R.layout.layout_untact_table_type_content_text_line_1_display),
        ITEM_VIEW_TYPE_TEXT_LINE_1_TYPE_1(R.layout.layout_untact_table_type_content_text_line_1_type_1),
        ITEM_VIEW_TYPE_DIVIDER(R.layout.layout_untact_table_type_content_divider);

        private int itemViewType;

        ItemViewType(int _itemViewType){
            this.itemViewType = _itemViewType;
        }

        public int getItemViewType() {
            return itemViewType;
        }
    }

    public enum ValidCheckType {
        VALID_CHECK_TYPE_NONE(""),
        VALID_CHECK_TYPE_NAME("N"),
        VALID_CHECK_TYPE_PHONE("P"),
        VALID_CHECK_TYPE_EMAIL("E"),
        VALID_CHECK_TYPE_ADDRESS("A"),
        VALID_CHECK_TYPE_VERIFCODE("V"),
        VALID_CHECK_TYPE_ACCOUNT("C");

        private String type;

        ValidCheckType(String _type){
            this.type = _type;
        }

        public String getType() {
            return type;
        }
    }

    public int typeLayout;
    public String text1;
    public String text2;
    public String text2Hint;
    public ValidCheckType validCheckType;
    public Drawable icon;
    public boolean isBgSizeUp = false;
    public boolean isOnOff = false;
    public boolean isBgChangeDisable = false;
    public boolean isSomeAreaSelect = false;

    public TableTypeDto(int _typeLayout) {
        this.typeLayout = _typeLayout;
        this.text1 = "";
        this.text2 = "";
        this.text2Hint = "";
        this.validCheckType = ValidCheckType.VALID_CHECK_TYPE_NONE;
        this.icon = null;
        this.isBgSizeUp = false;
        this.isOnOff = false;
        this.isBgChangeDisable = false;
        this.isSomeAreaSelect = false;
    }

    public TableTypeDto(int _typeLayout, String _text1, String _text2, String _text2Hint, ValidCheckType _validCheckType,
                        Drawable _icon, boolean _isBgSizeUp, boolean _isOnOff, boolean _isBgChangeDisable, boolean _isSomeAreaSelect) {
        this.typeLayout = _typeLayout;
        this.text1 = _text1;
        this.text2 = _text2;
        this.text2Hint = _text2Hint;
        this.validCheckType = _validCheckType;
        this.icon = _icon;
        this.isBgSizeUp = _isBgSizeUp;
        this.isOnOff = _isOnOff;
        this.isBgChangeDisable = _isBgChangeDisable;
        this.isSomeAreaSelect = _isSomeAreaSelect;
    }
}