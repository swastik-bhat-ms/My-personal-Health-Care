package com.example.personalhealthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class doclistadapter extends RecyclerView.Adapter<doclistadapter.ViewHolder> {
	List<doctorlist> doclist;

	public doclistadapter(List<doctorlist> doclist) {
		this.doclist = doclist;
	}

	public void setfilteredlist(List<doctorlist> filterlist) {
		this.doclist = filterlist;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public doclistadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctornames, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull doclistadapter.ViewHolder holder, int position) {
		holder.docname.setText(doclist.get(position).getDocname());
		holder.docdes.setText(doclist.get(position).getDocdes());
		holder.docdis.setText(doclist.get(position).getDocdis());
		holder.docspec.setText(doclist.get(position).getDocspes());
	}

	@Override
	public int getItemCount() {
		return doclist.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView docname;
		TextView docdes;
		TextView docdis;
		TextView docspec;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			docname = itemView.findViewById(R.id.doctorname);
			docdes = itemView.findViewById(R.id.doctordesig);
			docdis = itemView.findViewById(R.id.city);
			docspec = itemView.findViewById(R.id.specs);
		}
	}
}
