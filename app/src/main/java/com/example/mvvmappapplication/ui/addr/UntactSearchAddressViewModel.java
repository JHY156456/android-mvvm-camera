package com.example.mvvmappapplication.ui.addr;

import android.app.Application;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.example.mvvmappapplication.custom.HSRecyclerView;
import com.example.mvvmappapplication.dto.BC1096Q7Dto;
import com.example.mvvmappapplication.dto.BC1099Q1Dto;
import com.example.mvvmappapplication.ui.BaseNavigator;
import com.example.mvvmappapplication.ui.BaseViewModel;
import com.example.mvvmappapplication.utils.AlertUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 주소 직접 검색 ViewModel
 *
 * @author ehjung
 * @since 2019-07-08
 */
public class UntactSearchAddressViewModel extends BaseViewModel<BaseNavigator> {

    /********************************************************************************************************
     * Consts
     *********************************************************************************************************/

    MutableLiveData<Boolean> mCheckListEmptyLiveData = new MutableLiveData<>();

    String mSearchKeyword = "";
    private List<BC1099Q1Dto> mResultItems = new ArrayList<>();
    MutableLiveData<String> mEndlessScrollLiveData = new MutableLiveData<>();

    MutableLiveData<BC1099Q1Dto> mSelectAddrLiveData = new MutableLiveData<>();

    /* 도로명 정제 주소 조회 결과 */
    MutableLiveData<BC1096Q7Dto> mRefineAddrLiveData = new MutableLiveData<>();
    String mPostJibunAddr = "";

    public UntactSearchAddressViewModel(Application context) {
        super(context);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /********************************************************************************************************
     * List Item
     *********************************************************************************************************/

    public int getItemCount() {
        mCheckListEmptyLiveData.setValue(mResultItems.size()==0?true:false);
        return mResultItems.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public BC1099Q1Dto getItem(int position) {
        return mResultItems.get(position);
    }

    public List<BC1099Q1Dto> getItems() {
        return mResultItems;
    }

//    /********************************************************************************************************
//     * TR
//     *********************************************************************************************************/
//
//    /**
//     * 주소 검색
//     *
//     * @param keyword
//     */
//    protected void performBC1099Q1(String keyword) {
//        performBC1099Q1(keyword, "");
//    }

//    protected void performBC1099Q1(String keyword, String nextKey) {
//        if (TextUtils.isEmpty(nextKey)) {
//            mSearchKeyword = keyword;
//            mResultItems.clear();
//            mEndlessScrollLiveData.setValue(nextKey);
//        }
//        NetworkProvider.getInstance1().performBC1099Q1(keyword, nextKey, new NetworkProvider.ListenerChild() {
//            @Override
//            public void onResponse(NetworkResultDto object) {
//                FIDOrder tr = (FIDOrder) object.getObject();
//                int size = tr.Item.RecvOutListRec.get(0).arr.size();
//                ArrayList<XmlInOutItemArrDto> arrayData = tr.Item.RecvOutListRec.get(0).arr;
//
//                List<BC1099Q1Dto> items = new ArrayList<>();
//
//                for (int i = 0; i < size; i++) {
//                    ArrayList<XmlInOutItemDto> trData = arrayData.get(i).ItemArr;
//                    BC1099Q1Dto item = new BC1099Q1Dto();
//                    item.STD_ZPCD = FidUtil.getXmlOutItem(trData, "STD_ZPCD").data;
//                    item.STD_BSIC_ADDR = FidUtil.getXmlOutItem(trData, "STD_BSIC_ADDR").data;
//                    item.ROAD_NM_ZPCD = FidUtil.getXmlOutItem(trData, "ROAD_NM_ZPCD").data;
//                    item.ROAD_NM_BSIC_ADDR = FidUtil.getXmlOutItem(trData, "ROAD_NM_BSIC_ADDR").data;
//                    items.add(item);
//                }
//                mResultItems.addAll(items);
//                mEndlessScrollLiveData.setValue(tr.CTNU_TR_KEY);
//            }
//
//            @Override
//            public void onError(NetworkErrorDto error) {
//                mEndlessScrollLiveData.setValue(HSRecyclerView.LOAD_ERROR);
//                AlertUtil.toast(getActivity(), error.getMessage());
//            }
//        });
//    }
//
//    /**
//     * 도로명정제주소조회
//     */
//    protected void performBC1096Q7(String zipCode, String basicAddr, String detailAddr, final String _PostJibunAddr) {
//        mPostJibunAddr = _PostJibunAddr;
//        NetworkProvider.getInstance1().performBC1096Q7(zipCode, basicAddr, detailAddr, new NetworkProvider.ListenerChild() {
//            @Override
//            public void onResponse(NetworkResultDto object) {
//                FIDOrder tr = (FIDOrder) object.getObject();
//                ArrayList<XmlInOutItemDto> trData = tr.Item.RecvOutListRec.get(0).arr.get(0).ItemArr;
//                BC1096Q7Dto item = new BC1096Q7Dto();
//
//                item.INP_ZPCD = FidUtil.getXmlOutItem(trData, "INP_ZPCD").data;                     // 입력우편번호(6)
//                item.INP_BSIC_ADDR = FidUtil.getXmlOutItem(trData, "INP_BSIC_ADDR").data;           // 입력기본주소(100)
//                item.INP_DETL_ADDR = FidUtil.getXmlOutItem(trData, "INP_DETL_ADDR").data;           // 입력상세주소(100)
//                item.ONNW_ADDR_SCD = FidUtil.getXmlOutItem(trData, "ONNW_ADDR_SCD").data;           // 신구주소구분코드(2)
//                item.ROAD_NM_ZPCD = FidUtil.getXmlOutItem(trData, "ROAD_NM_ZPCD").data;             // 도로명우편번호(100)
//                item.ROAD_NM_BSIC_ADDR = FidUtil.getXmlOutItem(trData, "ROAD_NM_BSIC_ADDR").data;   // 도로명기본주소(100)
//                item.ROAD_NM_DETL_ADDR = FidUtil.getXmlOutItem(trData, "ROAD_NM_DETL_ADDR").data;   // 도로명상세주소(100)
//
//                mRefineAddrLiveData.setValue(item);
//            }
//
//            @Override
//            public void onError(NetworkErrorDto error) {
//                AlertUtil.toast(getActivity(), error.getMessage());
//                mRefineAddrLiveData.setValue(null);
//            }
//        });
//    }
}