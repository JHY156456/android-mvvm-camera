package com.example.mvvmappapplication.custom.dto;

import com.example.mvvmappapplication.dto.SelectDialogDto;

import java.util.ArrayList;


public class PopupTypeDto {
    public int mGroupIdx = 0;
    public int mSelectedIdx = -1;
    public String[] mLabel;
    public String[] mCode;

    public PopupTypeDto() {
    }

    public PopupTypeDto(ArrayList<SelectDialogDto> _ArrSelectDialogDto, int _SelectedIdx) {
        String[] mArrItemName = new String[_ArrSelectDialogDto.size()];
        String[] mArrItemCode = new String[_ArrSelectDialogDto.size()];
        for (int i = 0; i< _ArrSelectDialogDto.size(); i++){
            mArrItemName[i] = _ArrSelectDialogDto.get(i).getValue();
            mArrItemCode[i] = _ArrSelectDialogDto.get(i).getId();
        }
        this.mLabel = mArrItemName;
        this.mCode = mArrItemCode;
        this.mSelectedIdx = _SelectedIdx;
    }

    public PopupTypeDto(String[] _label, String[] _code) {
        this.mLabel = _label;
        this.mCode = _code;
    }
}