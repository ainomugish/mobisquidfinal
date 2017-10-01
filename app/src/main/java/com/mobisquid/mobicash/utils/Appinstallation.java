/*
 * Copyright 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobisquid.mobicash.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.mobisquid.mobicash.model.NewApp;


public class Appinstallation {

  public static final int REQUEST_CODE = 0x0000c0de; // Only use bottom 16 bits
  private static final String TAG = IntentIntegrator.class.getSimpleName();

  public static final String DEFAULT_TITLE = "Install Finance?";
  public static final String DEFAULT_MESSAGE =
          "Finance requires Mobisquid. Would you like to install it?";
  public static final String DEFAULT_YES = "Yes";
  public static final String DEFAULT_NO = "No";

  private static final String BS_PACKAGE = "com.mobisquid.mobiroot";
//  private static final String BSPLUS_PACKAGE = "com.srowen.bs.android";


  public static final Collection<String> ALL_CODE_TYPES = null;



  private final Activity activity;
  private String title;
  private String message;
  private String buttonYes;
  private String buttonNo;
  private List<String> targetApplications;
  private final Map<String,Object> moreExtras;

  public Appinstallation(Activity activity) {
    this.activity = activity;
    title = DEFAULT_TITLE;
    message = DEFAULT_MESSAGE;
    buttonYes = DEFAULT_YES;
    buttonNo = DEFAULT_NO;

    moreExtras = new HashMap<String,Object>(3);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setTitleByID(int titleID) {
    title = activity.getString(titleID);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setMessageByID(int messageID) {
    message = activity.getString(messageID);
  }

  public String getButtonYes() {
    return buttonYes;
  }

  public void setButtonYes(String buttonYes) {
    this.buttonYes = buttonYes;
  }

  public void setButtonYesByID(int buttonYesID) {
    buttonYes = activity.getString(buttonYesID);
  }

  public String getButtonNo() {
    return buttonNo;
  }

  public void setButtonNo(String buttonNo) {
    this.buttonNo = buttonNo;
  }

  public void setButtonNoByID(int buttonNoID) {
    buttonNo = activity.getString(buttonNoID);
  }

  public Collection<String> getTargetApplications() {
    return targetApplications;
  }

  public final void setTargetApplications(List<String> targetApplications) {
    if (targetApplications.isEmpty()) {
      throw new IllegalArgumentException("No target applications");
    }
    this.targetApplications = targetApplications;
  }
  private boolean isPackageInstalled(String packagename, Context context) {
    PackageManager pm = context.getPackageManager();
    try {
      pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      return false;
    }
  }


  private AlertDialog showDownloadDialog() {
    AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
    downloadDialog.setTitle(title);
    downloadDialog.setMessage(message);
    downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        String packageName = "com.mobisquid.mobiroot";
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
          activity.startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
          // Hmm, market is not installed
          Log.w(TAG, "Google Play is not installed; cannot install " + packageName);
        }
      }
    });
    downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {}
    });
    return downloadDialog.show();
  }

  public final AlertDialog shareText(NewApp newApp) {

    Intent intent = new Intent();
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setAction(BS_PACKAGE);
    Boolean is_installed = isPackageInstalled(BS_PACKAGE,activity);

    if (is_installed) {
      PackageManager pm = activity.getPackageManager();
      Intent voice = pm.getLaunchIntentForPackage(BS_PACKAGE);
      voice.putExtra("newapp",new Gson().toJson(newApp));
      activity.startActivity(voice);
      //  intent.setPackage(BS_PACKAGE);
      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
      //  activity.startActivity(intent);
      return null;
    }else{
      return showDownloadDialog();
    }


  }


}