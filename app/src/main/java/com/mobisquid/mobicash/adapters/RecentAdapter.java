package com.mobisquid.mobicash.adapters;

import java.util.Arrays;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconTextView;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.utils.AppController;
import com.mobisquid.mobicash.utils.CircleTransform;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentChatHolder> {

	Context context;
	List<MessageDb> dependents;
	OnItemClickListener listener;
	int countmsg;
	Vars vars;

	// CONSTRUCTOR
	public RecentAdapter(Context context, List<MessageDb> dependents) {
		log("++++++++++++++++++++++: Suga Chat Adapter +++++");
		log("MESSAGE LIST  SIZE:" + dependents.size());
		vars = new Vars(context);
		this.context = context;
		this.dependents = dependents;

	}


	@Override
	public RecentChatHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View itemView = LayoutInflater.

				from(viewGroup.getContext()).
				inflate(R.layout.row_recent_chat, viewGroup, false);

		return new RecentChatHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final RecentChatHolder holder, int position) {
		holder.count.setVisibility(View.GONE);
		if(vars.chk.equalsIgnoreCase(dependents.get(position).getRecep())){
			holder.name.setText(dependents.get(position).getUserName().substring(0,1).toUpperCase()+dependents.get(position).getUserName().substring(1));

		}else{
			holder.name.setText(dependents.get(position).getOtherName().substring(0,1).toUpperCase()+dependents.get(position).getOtherName().substring(1));
		}
		holder.imagetyp.setVisibility(View.GONE);
		if (dependents.get(position).getMessageType().equalsIgnoreCase("image")){
			holder.imagetyp.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_images));
			holder.imagetyp.setVisibility(View.VISIBLE);
			holder.messageText.setText("Image");
		}else if(dependents.get(position).getMessageType().equalsIgnoreCase("audio")){
			holder.imagetyp.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio));
			holder.imagetyp.setVisibility(View.VISIBLE);
			holder.messageText.setText("Audio");
		}
		else{
			if(dependents.get(position).getMessage().length()>30) {
				holder.messageText.setText(dependents.get(position).getMessage()
						.substring(0, Math.min(dependents.get(position).getMessage().length(), 35))+"...");
			}else{
				holder.messageText.setText(dependents.get(position).getMessage());
			}

		}
		vars.log("MESS=====thread====="+dependents.get(position).getThreadid());
		String oldNotification = AppController.getInstance().getPrefManager(context).getNotifications(dependents.get(position).getThreadid());
		if(oldNotification!=null){
			List<String> messages = Arrays.asList(oldNotification.split("\\|"));
			if(messages.size()>0){
				holder.count.setVisibility(View.VISIBLE);
				holder.count.setText(""+messages.size());

			}
		}

		//List<MessageDb> unread = Select.from(MessageDb.class)
		//		.where(Condition.prop("recep")
		//				.eq(vars.chk)).and(Condition.prop("extra").notEq("read")).list();



		holder.timeS.setText(Utils.getDate(dependents.get(position).getTimeSent()));
		vars.log("sender======="+dependents.get(position).getSender());
		vars.log("recep======="+dependents.get(position).getRecep());
		vars.log("My id======="+vars.chk);
		String url = null;
		if(dependents.get(position).getSender().equalsIgnoreCase(vars.chk)){
			url = Globals.USERIMAGE+dependents.get(position).getRecep()+".png";

		}else{
			url = Globals.USERIMAGE+dependents.get(position).getSender()+".png";
		}


		final String finalUrl = url;
		Picasso.with(vars.context)
				.load(url.trim())
				.networkPolicy(NetworkPolicy.OFFLINE)
				.transform(new CircleTransform())
				.into(holder.userimg, new Callback() {
					@Override
					public void onSuccess() {

					}

					@Override
					public void onError() {
						// Try again online if cache failed
						Picasso.with(vars.context)
								.load(finalUrl.trim())
								.transform(new CircleTransform())
								.placeholder(R.drawable.noimage)
								.error(R.drawable.noimage)
								.into(holder.userimg);
					}
				});
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public int getItemCount() {

		return dependents.size();
	}

	public class RecentChatHolder extends RecyclerView.ViewHolder{
		TextView name,count;
		TextView timeS;
		EmojiconTextView messageText;
		ImageView userimg,imagetyp;

		public RecentChatHolder(final View itemView) {
			super(itemView);
			messageText = (EmojiconTextView) itemView.findViewById(R.id.lastmessage);
			name = (TextView) itemView.findViewById(R.id.name);
			timeS = (TextView) itemView.findViewById(R.id.time_lbl);
			userimg = (ImageView) itemView.findViewById(R.id.list_image);
			imagetyp = (ImageView) itemView.findViewById(R.id.image_type);
			count = (TextView) itemView.findViewById(R.id.count);
			itemView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					if (listener != null)
						listener.onItemClick(itemView, getLayoutPosition());
				}
			});
		}
	}
	public MessageDb getItem(int position) {
		return dependents != null ? dependents.get(position) : null;
	}
	public interface OnItemClickListener {
		void onItemClick(View itemView, int position);
	}
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}
	void log(String msg) {
		System.out.println(msg);
	}

}