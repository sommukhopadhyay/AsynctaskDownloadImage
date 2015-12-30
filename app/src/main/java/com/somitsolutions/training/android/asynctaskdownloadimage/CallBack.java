package com.somitsolutions.training.android.asynctaskdownloadimage;

import android.graphics.Bitmap;

public interface CallBack {
	
	public void onProgress();
	public void onResult(Bitmap result);
	public void onCancel();
}
