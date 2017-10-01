package com.mobisquid.mobicash.adapters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.github.clans.fab.FloatingActionButton;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.model.MyInterface_adpt;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;
import com.mobisquid.mobicash.wedget.drawer.views.AndroidUtilities;
import com.mobisquid.mobicash.wedget.drawer.views.Emoji;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import github.ankushsachdeva.emojicon.EmojiconTextView;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolderItem> {
	Context context;
	int mWidth;
	File file_exist;
	SeekBar seekBar;
	double timeElapsed = 0;

	String imageurl,status;
	long length;
	AlertDialog alertDialog;
	MediaPlayer mPlayer = null;
	String  mFileName;
	SimpleDateFormat displayFormat;
	Date date;
	List<MessageDb> dependents;
	public static LayoutInflater inflater = null;
	String filePath;

	private Handler mHandler = new Handler();
	Vars vars;
	Long totalSize=0L;
	com.android.volley.toolbox.ImageLoader imageLoaderv ;

//class
//public MyInterface_adpt listener;


	
	// CONSTRUCTOR
	public ChatAdapter(Context context, List<MessageDb> dependents) {

		vars = new Vars(context);
		mPlayer = new MediaPlayer();
		this.context = context;
		this.dependents = dependents;
		//this.listener = listener;
		//this.imageList = imageList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}


	@Override
	public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = null;

		 if (viewType == AppConstant.ROW_LEFT_MESSAGE) {
			view = inflater.inflate(R.layout.chat_message_left, parent, false);
		}else if (viewType == AppConstant.ROW_RIGHT_MESSAGE) {
			view = inflater.inflate(R.layout.self_chat_right, parent, false);
		}else if (viewType == AppConstant.ROW_RIGTH_IMAGE) {
			view = inflater.inflate(R.layout.chat_image_right, parent, false);
		}
		else if (viewType == AppConstant.ROW_LEFT_IMAGE) {
			view = inflater.inflate(R.layout.chat_image_left, parent, false);
		}
		 else if (viewType == AppConstant.AUDIO_LEFT) {
			 view = inflater.inflate(R.layout.send_audio_left, parent, false);
		 }
		 else if (viewType == AppConstant.AUDIO_RIGHT) {
			 view = inflater.inflate(R.layout.send_audio_right, parent, false);
		 }
		return new ViewHolderItem(view, viewType);
	}

	@Override
	public void onBindViewHolder(final ViewHolderItem holder, final int position) {
		mWidth = vars.context.getResources().getDisplayMetrics().widthPixels;
		if(dependents.get(position).getIsself()&&dependents.get(position)
				.getMessageType().equalsIgnoreCase("image")) {
			RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) holder.showimage.getLayoutParams();
			imageParams.width = mWidth / 2;
			imageParams.height = mWidth / 2;

			holder.showimage.setLayoutParams(imageParams);

			holder.firstcheck.setVisibility(View.GONE);
			// TextView percentage = (TextView) vi.findViewById(R.id.percentage);
			//	 viewHolder.progress = (DonutProgress) vi.findViewById(R.id.progressbar);

			if (dependents.get(position).getDelServ().equalsIgnoreCase("yes") && dependents.get(position).getDelRecep().equalsIgnoreCase("no")){
				holder.firstcheck.setVisibility(View.VISIBLE);
				holder.firstcheck.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_single_tick));
				//	 viewHolder.progress.setVisibility(View.GONE);
				log("getDelServ()===================FIRST tick enabled=============yes======no");
			}
			if (dependents.get(position).getDelRecep().equalsIgnoreCase("yes"))
			{
				holder.firstcheck.setVisibility(View.VISIBLE);
				//	 viewHolder.progress.setVisibility(View.GONE);
				holder.firstcheck.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_tick));

				log("===================FIRST tick enabled======yes======yes=======");
			}

			/*imageLoader.displayImage("file://" + dependents.get(position).getMessage(),
					holder.showimage, options, imageListener);

			holder.showimage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent fullimage = new Intent(vars.context, ViewImageActivity.class);
					fullimage.putExtra("image", dependents.get(position).getMessage());
					vars.context.startActivity(fullimage);
				}
			});*/

		} else if(!dependents.get(position).getIsself() &&dependents.get(position)
				.getMessageType().equalsIgnoreCase("image")){
			RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) holder.sent_left.getLayoutParams();
			imageParams.width = mWidth / 2;
			imageParams.height = mWidth / 2;
			holder.sent_left.setLayoutParams(imageParams);
			vars.log("======BEFORE ANYTHING========");
			vars.log("getMessageId******************"+dependents.get(position).getMessageId());
			vars.log("getMessage******************"+dependents.get(position).getMessage());
		/*	if(dependents.get(position).getMessageId().equalsIgnoreCase("done")){
				vars.log("===========Done download============");
				holder.progressbar.setVisibility(View.GONE);
				holder.downloadimage.setVisibility(View.GONE);
				imageLoader.displayImage("file://" + dependents.get(position).getMessage(), holder.sent_left, options, imageListener);


			}else {

				vars.log("senders image=====================" + dependents.get(position).getMessage().trim());
				holder.progressbar.setVisibility(View.GONE);
				Picasso.with(vars.context)
						.load(vars.server_mb + "uploads/thumbs/"+ dependents.get(position).getMessage().trim())
						.networkPolicy(NetworkPolicy.OFFLINE)
						.into(holder.sent_left, new Callback() {
							@Override
							public void onSuccess() {

							}

							@Override
							public void onError() {
								// Try again online if cache failed
								Picasso.with(vars.context)
										.load(vars.server_mb + "uploads/thumbs/" + dependents.get(position).getMessage().trim())
										.placeholder(R.color.White)
										.error(R.color.White)
										.into(holder.sent_left);
							}
						});
				if (holder.downloadimage.getVisibility() == View.VISIBLE) {
					holder.downloadimage.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							holder.downloadimage.setVisibility(View.GONE);
							holder.progressbar.setVisibility(View.VISIBLE);
							DownloadImage downloadImage = new DownloadImage("image",holder.progressbar, vars.server_mb + "uploads/" + dependents.get(position).getMessage().trim(),
									holder.sent_left, new AsyncResponse() {
								@Override
								public void processFinish(final String output) {
									imageLoader.displayImage("file://" + output, holder.sent_left, options, imageListener);
									vars.log("am done=============++++++++++++" + output);
									holder.progressbar.setVisibility(View.GONE);
									MessageDb update = MessageDb.findById(MessageDb.class, dependents.get(position).getId());
									update.setMessageId("done");
									update.setMessage(output);
									update.save();

									holder.sent_left.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {

												Intent fullimage = new Intent(vars.context, ViewImageActivity.class);
												fullimage.putExtra("image", output);
												vars.context.startActivity(fullimage);
																						//	holder.downloadimage.setVisibility(View.GONE);
											//	holder.progressbar.setVisibility(View.VISIBLE);


										}
									});

								}
							});
							downloadImage.execute(vars.server_mb + "uploads/" + dependents.get(position).getMessage().trim());
						}
					});
				}

			}*/
			/*holder.sent_left.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(dependents.get(position).getMessageId().equalsIgnoreCase("done")){

						Intent fullimage = new Intent(vars.context, ViewImageActivity.class);
						fullimage.putExtra("image",dependents.get(position).getMessage());
						vars.context.startActivity(fullimage);
					}
					//	holder.downloadimage.setVisibility(View.GONE);
					//	holder.progressbar.setVisibility(View.VISIBLE);


				}
			});*/
		}else if(dependents.get(position).getIsself()&&dependents.get(position)
				.getMessageType().equalsIgnoreCase("chat")){

            try {
                holder.right_msg.setText(Emoji.replaceEmoji(URLDecoder.decode(dependents.get(position).getMessage(), "UTF-8"),
                        holder.right_msg.getPaint().getFontMetricsInt()
                        , AndroidUtilities.dp(14) ));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

			//holder.right_msg.setText(dependents.get(position).getMessage());
			displayFormat = new SimpleDateFormat("HH:mm");
			date = new Date(dependents.get(position).getTimeSent());
			holder.time_text.setText(displayFormat.format(date));
			if(vars.font==null){

			}else if(vars.font.contains("font")){
				final Typeface tf = Typeface.createFromAsset(vars.context.getAssets(), vars.font);
				setFont(holder.group, tf);

			}

			holder.firstcheck.setVisibility(View.INVISIBLE);
			if (dependents.get(position).getDelServ().equalsIgnoreCase("yes") && dependents.get(position).getDelRecep().equalsIgnoreCase("no")){
				holder.firstcheck.setVisibility(View.VISIBLE);
				holder.firstcheck.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_single_tick));

				log("getDelServ()===================FIRST tick enabled=============yes======no");
			}
			if (dependents.get(position).getDelRecep().equalsIgnoreCase("yes"))
			{
				holder.firstcheck.setVisibility(View.VISIBLE);
				holder.firstcheck.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_tick));

				log("===================FIRST tick enabled======yes======yes=======");
			}

		}else if(!dependents.get(position).getIsself()&& dependents.get(position)
				.getMessageType().equalsIgnoreCase("chat")){

			//final ImageView profile_img = (ImageView) vi.findViewById(R.id.profilepic);
			holder.left_msg.setText(Emoji.replaceEmoji(dependents.get(position).getMessage(),
					holder.left_msg.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14) ));
		//	holder.left_msg.setText(dependents.get(position).getMessage());
			vars.log("TIME========"+dependents.get(position).getTimeSent());
			displayFormat = new SimpleDateFormat("HH:mm");
			date = new Date(dependents.get(position).getTimeSent());
			holder.time_text.setText(displayFormat.format(date));
			holder.language.setText(dependents.get(position).getLanguage());
			if(vars.font==null){

			}else if(vars.font.contains("font")){
				final Typeface tf = Typeface.createFromAsset(context.getAssets(), vars.font);
				setFont(holder.group, tf);

			}

		}else if(dependents.get(position).getIsself()&& dependents.get(position)
				.getMessageType().equalsIgnoreCase("audio")){
			holder.tick_audio.setVisibility(View.GONE);
			if (dependents.get(position).getDelServ().equalsIgnoreCase("yes")){
				holder.tick_audio.setVisibility(View.VISIBLE);
				holder.tick_audio.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_single_tick));
				//	 viewHolder.progress.setVisibility(View.GONE);
				log("getDelServ()===================FIRST tick enabled=============yes======no");
			}
			if (dependents.get(position).getDelRecep().equalsIgnoreCase("yes"))
			{
				holder.tick_audio.setVisibility(View.VISIBLE);
				//	 viewHolder.progress.setVisibility(View.GONE);
				holder.tick_audio.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_tick));

				log("===================FIRST tick enabled======yes======yes=======");
			}
			displayFormat = new SimpleDateFormat("HH:mm");
			date = new Date(dependents.get(position).getTimeSent());
			holder.time_sent_audio.setText(displayFormat.format(date));
			length = dependents.get(position).getTimeRec();
			if(length>1024){
				long v = length/1024;
				holder.audio_size.setText(v +" MB");
			}else{
				holder.audio_size.setText(length +" KB");
			}

			holder.floatingActionButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mPlayer.isPlaying()){
						restPlaying();
						holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_play));

					}else{

						file_exist= new File(dependents.get(position).getMessage());
						if(file_exist.exists()){
							seekBar =holder.seekerbar;
							startPlaying(dependents.get(position).getMessage());
							holder.seekerbar.setMax(mPlayer.getDuration());
							timeElapsed = mPlayer.getCurrentPosition();
							holder.seekerbar.setProgress((int) timeElapsed);
							mHandler.postDelayed(run, 100);
							holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_pause));
							mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mp) {
									mp.reset();
								//	holder.seekerbar.setProgress(0);
									holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_play));
								}
							});
						}else {
							Toast.makeText(vars.context,"Audio file removed",Toast.LENGTH_LONG).show();
						}

					}
				}
			});
			if(dependents.get(position).getMessageId().equalsIgnoreCase("200")){

				holder.floatingActionButton.setShowProgressBackground(true);
				holder.floatingActionButton.setIndeterminate(true);

				//holder.floatingActionButton.setProgress(100,true);
			}else{
				holder.floatingActionButton.hideProgress();
			}

		}
		else if(!dependents.get(position).getIsself()&& dependents.get(position)
				.getMessageType().equalsIgnoreCase("audio")){
			displayFormat = new SimpleDateFormat("HH:mm");
			date = new Date(dependents.get(position).getTimeSent());
			holder.time_sent_audio.setText(displayFormat.format(date));
			length = dependents.get(position).getTimeRec();
			if(length>1024){
				long v = length/1024;
				holder.audio_size.setText(v +" MB");
			}else{
				holder.audio_size.setText(length +" KB");
			}

			holder.floatingActionButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(dependents.get(position).getMessageId().equalsIgnoreCase("done")){
						file_exist= new File(dependents.get(position).getMessage());
						if(file_exist.exists()){
							seekBar =holder.seekerbar;
							startPlaying(dependents.get(position).getMessage());
							holder.seekerbar.setMax(mPlayer.getDuration());
							timeElapsed = mPlayer.getCurrentPosition();
							holder.seekerbar.setProgress((int) timeElapsed);
							mHandler.postDelayed(run, 100);
							holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_pause));
							mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mp) {
									mp.reset();
									//	holder.seekerbar.setProgress(0);
									holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_play));
								}
							});
						}else {
							Toast.makeText(vars.context,"Audio file removed",Toast.LENGTH_LONG).show();
						}

					}else{
						holder.floatingActionButton.setShowProgressBackground(true);
						holder.floatingActionButton.setIndeterminate(true);
/*

						DownloadImage downloadImage = new DownloadImage(vars.server_mb + "uploads/" +
								dependents.get(position).getMessage().trim(),"audio", new AsyncResponse() {
							@Override
							public void processFinish(final String output) {

								vars.log("am done=============++++++++++++" + output);

								MessageDb update = MessageDb.findById(MessageDb.class, dependents.get(position).getId());
								update.setMessageId("done");
								update.setMessage(output);
								update.save();
								holder.floatingActionButton.hideProgress();

								holder.floatingActionButton.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {

										if(mPlayer.isPlaying()){
											restPlaying();
											holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_play));

										}else{

											file_exist= new File(output);
											if(file_exist.exists()){
												seekBar =holder.seekerbar;
												startPlaying(output);
												holder.seekerbar.setMax(mPlayer.getDuration());
												timeElapsed = mPlayer.getCurrentPosition();
												holder.seekerbar.setProgress((int) timeElapsed);
												mHandler.postDelayed(run, 100);
												holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_pause));
												mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
													@Override
													public void onCompletion(MediaPlayer mp) {
														mp.reset();
														//	holder.seekerbar.setProgress(0);
														holder.floatingActionButton.setImageDrawable(vars.context.getResources().getDrawable(R.drawable.ic_play));
													}
												});
											}else {
												Toast.makeText(vars.context,"Audio file removed",Toast.LENGTH_LONG).show();
											}

										}

									}
								});

							}
						});
						downloadImage.execute(vars.server_mb + "uploads/" + dependents.get(position).getMessage().trim());
*/

					}

				}
			});
		}

		else{
			vars.log("I REALLY DON'T KNOW"+dependents.get(position).getMessageType()+"==============="
					+dependents.get(position).getIsself());
		}

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemCount() {

	//	return (dependents != null && dependents.size() > 0 ) ? dependents.size() : 0;
		return dependents.size();
	}

	@Override
	public int getItemViewType(int position) {
		if(dependents.get(position).getIsself()&&dependents.get(position)
				.getMessageType().equalsIgnoreCase("image")) {
			return AppConstant.ROW_RIGTH_IMAGE;

		}else if(!dependents.get(position).getIsself() &&dependents.get(position)
				.getMessageType().equalsIgnoreCase("image")){

			return AppConstant.ROW_LEFT_IMAGE;
		}
		else if(dependents.get(position).getIsself()&&dependents.get(position)
				.getMessageType().equalsIgnoreCase("chat")){
			return AppConstant.ROW_RIGHT_MESSAGE;
		}
		else if(!dependents.get(position).getIsself()&& dependents.get(position)
				.getMessageType().equalsIgnoreCase("chat")){
			return AppConstant.ROW_LEFT_MESSAGE;

		}else if(!dependents.get(position).getIsself()&& dependents.get(position)
				.getMessageType().equalsIgnoreCase("audio")){
			return AppConstant.AUDIO_LEFT;

		}else if(dependents.get(position).getIsself()&& dependents.get(position)
				.getMessageType().equalsIgnoreCase("audio")){
			return AppConstant.AUDIO_RIGHT;

		}
	//	MultipleRowModel multipleRowModel = multipleRowModelList.get(position);

	//	if (multipleRowModel != null)
	//		return multipleRowModel.type;

		return super.getItemViewType(position);
		//return super.getItemViewType(position);
	}


	void log(String msg) {
		System.out.println(msg);
	}
	public void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof EmojiconTextView || v instanceof Button /* etc. */)
				((EmojiconTextView) v).setTypeface(font);
			else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	class ViewHolderItem extends RecyclerView.ViewHolder {
		private int type;
		public SeekBar seekerbar;
		public ImageView firstcheck,downloadimage;
		public ImageView showimage,sent_left,tick_audio;
		public ViewGroup group;
		public TextView time_text,audio_size,time_sent_audio,language;
		FloatingActionButton floatingActionButton;
		ProgressBar progressbar;
		public EmojiconTextView left_msg,right_msg;

		public ViewHolderItem(View itemView, int type) {
			super(itemView);
			if (type == AppConstant.ROW_LEFT_IMAGE){
				progressbar = (ProgressBar) itemView.findViewById(R.id.progressbar);
				sent_left = (ImageView) itemView.findViewById(R.id.sent_image);
				downloadimage = (ImageView) itemView.findViewById(R.id.downloadimage);
			}else if(type == AppConstant.ROW_RIGTH_IMAGE) {
				showimage = (ImageView) itemView.findViewById(R.id.image_test);
				firstcheck = (ImageView) itemView.findViewById(R.id.delivery_img);
			}else if(type == AppConstant.ROW_LEFT_MESSAGE){
				language = (TextView) itemView.findViewById(R.id.language);
				left_msg = (EmojiconTextView) itemView.findViewById(R.id.txtMsg_left);
				group = (ViewGroup) itemView.findViewById(R.id.wrapper_gr);
				time_text = (TextView) itemView.findViewById(R.id.time_text);
			}else if(type == AppConstant.ROW_RIGHT_MESSAGE){
				time_text = (TextView) itemView.findViewById(R.id.time_text);
				firstcheck = (ImageView) itemView.findViewById(R.id.right_delivery);
				right_msg = (EmojiconTextView) itemView.findViewById(R.id.txtMsg);
				group = (ViewGroup) itemView.findViewById(R.id.wrapper_gr);
			}else if(type == AppConstant.AUDIO_LEFT){
				seekerbar = (SeekBar) itemView.findViewById(R.id.seekerbar);
				floatingActionButton = (FloatingActionButton) itemView.findViewById(R.id.fab);
				audio_size= (TextView) itemView.findViewById(R.id.audio_size);
				time_sent_audio = (TextView) itemView.findViewById(R.id.time_sent);

			}else if(type == AppConstant.AUDIO_RIGHT){
				seekerbar = (SeekBar) itemView.findViewById(R.id.seekerbar);
				floatingActionButton = (FloatingActionButton) itemView.findViewById(R.id.fab);
				audio_size= (TextView) itemView.findViewById(R.id.audio_size);
				time_sent_audio = (TextView) itemView.findViewById(R.id.time_sent);
				tick_audio = (ImageView) itemView.findViewById(R.id.tick_audio);

			}
		}
	}

	/*public static class ImageDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
									  Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}*/
	public class AppConstant {
		public static final int AUDIO_LEFT = 4;
		public static final int AUDIO_RIGHT = 5;
		public static final int ROW_RIGTH_IMAGE = 0;
		public static final int ROW_LEFT_MESSAGE = 2;
		public static final int ROW_RIGHT_MESSAGE = 3;
		public static final int ROW_LEFT_IMAGE = 1;

	}


	/*class DownloadImage extends AsyncTask<String, String, String> {
		DonutProgress progressBar;
		String imageUrl;
		String type;
		File f;
		public AsyncResponse delegate = null;
		ImageView showimage;

		public DownloadImage(String type,DonutProgress mprogressBar, String imageUrl,ImageView showimage, AsyncResponse delegate){
        this.progressBar = mprogressBar;
        this.delegate = delegate;
		this.showimage = showimage;
        this.imageUrl = imageUrl;
		this.type = type;
        // this.progressBar.setVisibility(View.VISIBLE);

    }
		*//*public DownloadImage( String imageUrl,String type, AsyncResponse delegate){
			this.delegate = delegate;
			this.type = type;
			this.imageUrl = imageUrl;
			// this.progressBar.setVisibility(View.VISIBLE);

		}
*//*

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// this will be useful so that you can show a tipical 0-100% progress bar
				int lenghtOfFile = conection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream(), 8192);
				File m = new File(Environment.getExternalStorageDirectory(), "/Mobisquid/media");
				if (!m.exists()) {
					m.mkdirs();
				}


				if (!m.exists()) {
					m.mkdirs();
				}
				if(type.equalsIgnoreCase("audio")){
					f = new File(m, "Mobisquid"+ Utils.radomchr()+".3gp");
				}else{
					f = new File(m, "Mobisquid"+Utils.radomchr()+".jpg");
				}

				// Output stream
				OutputStream output = new FileOutputStream(f);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress(""+(int)((total*100)/lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();
				return f.getAbsolutePath();

			} catch (Exception e) {
				//Log.e("Error: ", e.getMessage());
				return null;
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(type.equalsIgnoreCase("audio")){

			}else {
				progressBar.setProgress(0);
			}
		}


		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			if(type.equalsIgnoreCase("audio")){

			}else {
				progressBar.setProgress(Integer.parseInt(progress[0]));
			}
		}

		@Override
		protected void onPostExecute(String imagepath) {
			delegate.processFinish(imagepath);

		}

	}*/
	interface AsyncResponse {
		void processFinish(String output);
	}
	private void restPlaying() {
		mPlayer.reset();
	}


	private void startPlaying(String mFile_Name) {

		try {
			vars.log("file============="+mFile_Name);
			mPlayer.setDataSource(mFile_Name);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			vars.log("prepare() failed");
		}
	}
	Runnable run = new Runnable() {

		@Override
		public void run() {
			if(mPlayer != null){
				//get current position
				timeElapsed = mPlayer.getCurrentPosition();
				//set seekbar progress
				seekBar.setProgress((int) timeElapsed);
				mHandler.postDelayed(this, 100);

			}
		}
	};

	private void upDateTextBox(TextView tv, int progress){

		tv.setText(String.valueOf(progress));

	}
}
