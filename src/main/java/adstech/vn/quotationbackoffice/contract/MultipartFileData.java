package adstech.vn.quotationbackoffice.contract;

import org.springframework.web.multipart.MultipartFile;

public class MultipartFileData {
	
	private MultipartFile fileDatas;

	public MultipartFile getFileDatas() {
		return fileDatas;
	}

	public void setFileDatas(MultipartFile fileDatas) {
		this.fileDatas = fileDatas;
	}
}
