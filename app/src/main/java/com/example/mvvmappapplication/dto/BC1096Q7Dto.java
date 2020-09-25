package com.example.mvvmappapplication.dto;


public class BC1096Q7Dto {

	public String INP_ZPCD;				// 입력우편번호(6)
	public String INP_BSIC_ADDR;		// 입력기본주소(100)
	public String INP_DETL_ADDR;		// 입력상세주소(100)
	public String ONNW_ADDR_SCD;		// 신구주소구분코드(2)
	public String ROAD_NM_ZPCD;			// 도로명우편번호(100)
	public String ROAD_NM_BSIC_ADDR;	// 도로명기본주소(100)
	public String ROAD_NM_DETL_ADDR;	// 도로명상세주소(100)
	
	public String inputZipCode;			// 표기하기 위해 수정한 우편번호 - 입력
	public String roadZipCode;			// 표기하기 위해 수정한 우편번호 - 도로명
	
	public void formatChgZipCod() {
		inputZipCode = this.INP_ZPCD;
		roadZipCode = this.ROAD_NM_ZPCD;
	}
}
