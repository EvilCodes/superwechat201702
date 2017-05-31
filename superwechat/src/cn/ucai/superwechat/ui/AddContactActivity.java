/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.widget.EaseAlertDialog;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.data.OnCompleteListener;
import cn.ucai.superwechat.data.Result;
import cn.ucai.superwechat.data.net.IUserModel;
import cn.ucai.superwechat.data.net.UserModel;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MFGT;
import cn.ucai.superwechat.utils.ResultUtils;

public class AddContactActivity extends BaseActivity{
	private static final String TAG = "AddContactActivity";
	private EditText editText;
	private RelativeLayout searchedUserLayout;
	private String toAddUsername;
	private ProgressDialog progressDialog;
	IUserModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.em_activity_add_contact);
		super.onCreate(savedInstanceState);

		editText = (EditText) findViewById(R.id.edit_note);
		String strUserName = getResources().getString(R.string.user_name);
		editText.setHint(strUserName);
		searchedUserLayout = (RelativeLayout) findViewById(R.id.ll_user);
		initView();
		model = new UserModel();
	}

	private void initView() {
		titleBar.getRightLayout().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchContact();
			}
		});
		titleBar.getLeftLayout().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MFGT.finish(AddContactActivity.this);
			}
		});
	}

	private void initDialog(){
		progressDialog = new ProgressDialog(AddContactActivity.this);
		progressDialog.setMessage(getString(R.string.searching));
		progressDialog.show();
	}

	private void dismissDialog(){
		if (progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}


	public void searchContact() {
		initDialog();
		final String name = editText.getText().toString();

		toAddUsername = name;
		if(TextUtils.isEmpty(name)) {
			new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
			return;
		}

		searchContactFromAppServer();
	}

	private void searchContactFromAppServer() {
		model.loadUserInfo(AddContactActivity.this, toAddUsername,
				new OnCompleteListener<String>() {
					@Override
					public void onSuccess(String s) {
						boolean isSuccess = false;
						User user = null;
						if (s!=null){
							Result<User> result = ResultUtils.getResultFromJson(s, User.class);
							if (result!=null && result.isRetMsg()){
								user = result.getRetData();
								L.e(TAG,"searchContactFromAppServer,user="+user);
								if (user!=null){
									isSuccess = true;
								}
							}
						}
						showSearchResult(isSuccess,user);
					}

					@Override
					public void onError(String error) {
						showSearchResult(false,null);
					}
				});
	}

	private void showSearchResult(boolean isSuccess,User user) {
		dismissDialog();
		searchedUserLayout.setVisibility(isSuccess?View.GONE:View.VISIBLE);
		if (isSuccess && user!=null){
			MFGT.gotoProfile(AddContactActivity.this,user);
		}
	}
	
	public void back(View v) {
		finish();
	}
}
