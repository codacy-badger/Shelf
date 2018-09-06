package chickenmumani.com.allshelf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Board_Adapter extends RecyclerView.Adapter<Board_Adapter.ViewHolder> {
    private List<Board_Item> myList;

    public Board_Adapter(List<Board_Item> list) {
        this.myList = list;
    }

    @Override
    public Board_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_board, parent, false);
        return new Board_Adapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final Board_Adapter.ViewHolder holder, int position) {
        final Board_Item my = myList.get(position);
        holder.boardTitle.setText(my.getTitle());
        holder.boardDate.setText(my.getDate());
        holder.boardUser.setText(my.getName());
        if(my.getTag().equalsIgnoreCase("토론")){
            holder.boardTag.setImageResource(R.drawable.ic_board_toron);
        }
        else{
            holder.boardTag.setImageResource(R.drawable.ic_board_change);
        }


        holder.boardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Context context = v.getContext();
                final Intent intent = new Intent(context, Board_ContentActivity.class);
                intent.putExtra("id", my.getDBID());
                intent.putExtra("name", my.getName());
                intent.putExtra("title", my.getTitle());
                intent.putExtra("date", my.getDate());
                intent.putExtra("text", my.getText());
                intent.putExtra("tag", my.getTag());
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
        public final TextView boardTitle;
        public final TextView boardDate;
        public final TextView boardUser;
        public final ImageView boardTag;
        public final LinearLayout boardLayout;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            boardTitle = (TextView) view.findViewById(R.id.boardview_title);
            boardDate = (TextView) view.findViewById(R.id.boardview_date);
            boardUser = (TextView) view.findViewById(R.id.boardview_user);
            boardTag = (ImageView) view.findViewById(R.id.board_tag_image);
            boardLayout = (LinearLayout) view.findViewById(R.id.boardview_lay);
        }
    }
}