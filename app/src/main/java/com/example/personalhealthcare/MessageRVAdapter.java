package com.example.personalhealthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageRVAdapter extends RecyclerView.Adapter {
	private ArrayList<Messagemodel> messagemodelArrayList;
	private Context context;

	public MessageRVAdapter(ArrayList<Messagemodel> messagemodelArrayList, Context context) {
		this.messagemodelArrayList = messagemodelArrayList;
		this.context = context;
	}

	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;

		switch (viewType) {
			case 0:
				view = LayoutInflater.from(context).inflate(R.layout.user_message, parent, false);
				return new UserViewHolder(view);

			case 1:
				view = LayoutInflater.from(context).inflate(R.layout.bot_message, parent, false);
				return new BotViewHolder(view);
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Messagemodel model = messagemodelArrayList.get(position);

		switch (model.getSender()) {
			case 0:
				((UserViewHolder) holder).userTV.setText(model.getMessage());
				break;
			case 1:
				((BotViewHolder) holder).botTV.setText(model.getMessage());
				break;
		}
	}

	@Override
	public int getItemCount() {
		return messagemodelArrayList.size();
	}

	@Override
	public int getItemViewType(int position) {
		switch (messagemodelArrayList.get(position).getSender()) {

			case 0:
				return 0;
			case 1:
				return 1;
			default:
				return -1;
		}
	}

	public static class UserViewHolder extends RecyclerView.ViewHolder {
		TextView userTV;

		public UserViewHolder(View itemView) {
			super(itemView);
			userTV = itemView.findViewById(R.id.user_messagebox);
		}
	}

	public static class BotViewHolder extends RecyclerView.ViewHolder {
		TextView botTV;

		public BotViewHolder(View itemView) {
			super(itemView);
			botTV = itemView.findViewById(R.id.bot_messagebox);

		}
	}

}