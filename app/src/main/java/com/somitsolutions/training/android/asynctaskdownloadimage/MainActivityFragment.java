package com.somitsolutions.training.android.asynctaskdownloadimage;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivityFragment extends Fragment implements OnClickListener{
	
	Button mStartDownloadButton, mDisplayImageButton;
	EditText mURL;
	ImageView mImageView; 
	private CallBack c;
	ProgressDialog mProgressDialog;
	DownloadImageAsyncTask mTask;
	Context context;
	Bitmap mBitmap;
	private static MainActivityFragment app;
	Boolean isScreenRotated = false;
	boolean isDownloadFinished = false;
	boolean isDownloadStarted = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		 c = new CallBack() {
	        	
			 public void onProgress(){
				 if(c != null && !isScreenRotated){
					 if(mProgressDialog == null) {
						 mProgressDialog = new ProgressDialog(getActivity());
					 }
					 mProgressDialog.setMessage("On Progress...");
					 mProgressDialog.setCancelable(true);

					 mProgressDialog.show();
					 isDownloadStarted = true;
				 	}

	        	}
	        	
	        	public void onResult(Bitmap result){

					if (c != null && !isScreenRotated){
						mBitmap = result;

						Toast.makeText(getActivity().getApplicationContext(), "Done...", Toast.LENGTH_LONG).show();
						mStartDownloadButton.setEnabled(true);

						if(mProgressDialog != null){
							mProgressDialog.dismiss();
							mProgressDialog = null;
						}
						isDownloadFinished = true;
					}
					/*if (isScreenRotated){
						Toast.makeText(getActivity().getApplicationContext(),"Download not completed. Please retry...", Toast.LENGTH_LONG).show();
						isScreenRotated = false;
					}*/

	        	}
	        	
	        	public void onCancel(){

	        		Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Cancelled", Toast.LENGTH_LONG);
	            	toast.show();
					if(mProgressDialog != null){
						mProgressDialog.dismiss();
						mProgressDialog = null;
					}

	        	}
	        };
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//isScreenRotated = false;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_main,container,false);

		mStartDownloadButton = (Button)view.findViewById(R.id.buttonDownloadImage);
		mDisplayImageButton = (Button) view.findViewById(R.id.buttonDisplayImage);
		mURL = (EditText) view.findViewById(R.id.editTextURL);
		mImageView = (ImageView) view.findViewById(R.id.imageView1);
		mImageView.setVisibility(View.INVISIBLE);
		mStartDownloadButton.setOnClickListener(this);
		mDisplayImageButton.setOnClickListener(this);

		return view;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(mStartDownloadButton)){
			String URL = mURL.getText().toString();
			URL = URL.replace(" ", "");

			if(URL != null && !URL.isEmpty()){
				DownloadImageAsyncTask asyncTask = new DownloadImageAsyncTask(c);
				asyncTask.execute(URL);
				mStartDownloadButton.setEnabled(false);
			}
			
		}
		
		if(v.equals(mDisplayImageButton)){
			mImageView.setImageBitmap(mBitmap);
			mImageView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDetach() {
		// All dialogs should be closed before leaving the activity in order to avoid
		// the: Activity has leaked window com.android.internal.policy... exception
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		/*if(mBitmap != null){
			mBitmap = null;

		}*/
		isScreenRotated = true;
		super.onDetach();
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		//isScreenRotated = false;
		if(isScreenRotated && mProgressDialog == null && !isDownloadFinished && isDownloadStarted) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setMessage("On Progress...");
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();

		}
		isScreenRotated = false;
	}


}
