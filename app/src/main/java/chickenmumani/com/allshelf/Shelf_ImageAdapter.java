package chickenmumani.com.allshelf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class Shelf_ImageAdapter extends RecyclerView.Adapter<Shelf_ImageAdapter.ViewHolder> {

    private List<Shelf_Item> myList;
    Bitmap bitmap;

    public Shelf_ImageAdapter(List<Shelf_Item> list) {
        this.myList = list;
    }

    @Override

    public Shelf_ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_shelf_image, parent, false);
        return new Shelf_ImageAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final Shelf_ImageAdapter.ViewHolder holder, int position) {
        final Shelf_Item my = myList.get(position);
        ImageView img = holder.noimg;

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(my.getImgurl());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) { e.printStackTrace(); }
            }
        };

        mThread.start();

        try {
            mThread.join();
            img.setImageBitmap(bitmap);
        } catch (Exception e) { e.printStackTrace(); }

        holder.nolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Context context = v.getContext();
                Intent intent = new Intent(context, BookInfo_Activity.class);
                intent.putExtra("barcodeContents",my.getIsbn());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView noimg;
        public final LinearLayout nolay;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            noimg = (ImageView) view.findViewById(R.id.shelfview_img);
            nolay = (LinearLayout) view.findViewById(R.id.shelfview_ilay);

        }

    }

}