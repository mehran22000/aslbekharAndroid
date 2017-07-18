package com.example.n55.jsonandvally.adater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.n55.jsonandvally.ProductActivity;
import com.example.n55.jsonandvally.R;
import com.example.n55.jsonandvally.app.AppController;
import com.example.n55.jsonandvally.model.Kala;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	Bundle bundle = new Bundle();
	private static final String TAG = RecyclerAdapter.class.getSimpleName();
	Context mcontext;

	List<Kala> mDataList;
	private LayoutInflater inflater;

	public RecyclerAdapter(Context mcontext ,List<Kala> data) {
		this.mDataList = data;
		this.mcontext=mcontext;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		inflater = LayoutInflater.from(parent.getContext());
		//list_row
		View view = inflater.inflate(R.layout.list_row, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Log.i(TAG, "onBindViewHolder" + position);

		Kala current = mDataList.get(position);
		holder.setData(current, position);
		holder.setListeners();
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}

	public void removeItem(int position) {
		mDataList.remove(position);
		notifyItemRemoved(position);
//		notifyItemRangeChanged(position, mDataList.size());
//		notifyDataSetChanged();
	}

	public void addItem(int position, Kala kala) {
		mDataList.add(position, kala);
		notifyItemInserted(position);
//		notifyItemRangeChanged(position, mDataList.size());
//		notifyDataSetChanged();
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		//ImageView imgThumb, imgDelete, imgAdd;
		int position;
		Kala current;
		NetworkImageView thumbNail;
		TextView title;

		public MyViewHolder(View itemView) {
			super(itemView);
			if (imageLoader == null)
				imageLoader = AppController.getInstance().getImageLoader();
			thumbNail= (NetworkImageView) itemView.findViewById(R.id.thumbnail);
			title = (TextView) itemView.findViewById(R.id.title);
		}

		public void setData(Kala current, int position) {

			thumbNail.setImageUrl(current.gettumbnail(), imageLoader);
			title.setText(current.getTitle());
			this.position = position;
			this.current = current;
		}

		public void setListeners() {
			//imgDelete.setOnClickListener(MyViewHolder.this);
			//imgAdd.setOnClickListener(MyViewHolder.this);
			thumbNail.setOnClickListener(MyViewHolder.this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.thumbnail:

					//Toast.makeText(mcontext, String.valueOf(mDataList.get(position).getid()), Toast.LENGTH_LONG).show();
					Intent intent = new Intent(mcontext, ProductActivity.class);

					intent.putExtra("id",mDataList.get(position).getid());
					intent.putExtra("persianname",mDataList.get(position).getTitle());
					intent.putExtra("englishname",mDataList.get(position).getenglishTitle());
					intent.putExtra("price",mDataList.get(position).getprice());
					intent.putExtra("desc",mDataList.get(position).getdesc());
					intent.putExtra("tumbnail",mDataList.get(position).gettumbnail());

					mcontext.startActivity(intent);
					break;
			}
		}
	}
}
