package com.example.rezoan503s.camera;

/**
 * Created by anonymous on 11/4/16.
 */

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.view.ViewGroup;

public class HorizontalListViewFragment extends Fragment {

    ArrayList<Fruit> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String Fruits[] = {"Yellow","Black","Maroon","Black","White"};
    int  Images[] = {R.drawable.tshirtyellow,R.drawable.tshirtwritting,R.drawable.tshirtmaroon,R.drawable.tshirtsblack,R.drawable.tshirtwhite};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listitems.clear();
        for(int i =0;i<Fruits.length;i++){
            Fruit item = new Fruit();
            item.setCardName(Fruits[i]);
            item.setImageResourceId(Images[i]);
            item.setIsfav(0);
            item.setIsturned(0);
            listitems.add(item);
        }
        getActivity().setTitle("Fruit List");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_horizontal_list_view, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Fruit> list;

        public MyAdapter(ArrayList<Fruit> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            holder.titleTextView.setText(list.get(position).getCardName());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            holder.coverImageView.setTag(list.get(position).getImageResourceId());
            holder.likeImageView.setTag(R.drawable.ic_like);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnDragListener{

        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public MyViewHolder(View v) {
            super(v);
            v.setOnDragListener(this);

            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
            shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int id = (int)likeImageView.getTag();
                    if( id == R.drawable.ic_like){

                        likeImageView.setTag(R.drawable.ic_liked);
                        likeImageView.setImageResource(R.drawable.ic_liked);

                        Toast.makeText(getActivity(),titleTextView.getText()+" added to favourites",Toast.LENGTH_SHORT).show();

                    }else{

                        likeImageView.setTag(R.drawable.ic_like);
                        likeImageView.setImageResource(R.drawable.ic_like);
                        Toast.makeText(getActivity(),titleTextView.getText()+" removed from favourites",Toast.LENGTH_SHORT).show();


                    }

                }
            });



            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + getResources().getResourcePackageName(coverImageView.getId())
                            + '/' + "drawable" + '/' + getResources().getResourceEntryName((int)coverImageView.getTag()));


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                }
            });
            coverImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if((event.getAction()==MotionEvent.ACTION_DOWN)&&((ImageView)v).getDrawable()!=null) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder=new View.DragShadowBuilder(v);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            v.startDragAndDrop(data,shadowBuilder,v,0);
                        }
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });




        }


        @Override
        public boolean onDrag(View v, DragEvent event)
        {
            Log.d("Drag", "onDrag");

            // Store the action type for the incoming event
            final int action = event.getAction();

            // Handles each of the expected events
            switch (action)
            {
                case DragEvent.ACTION_DRAG_STARTED:

                    // In order to inform user that drag has started, we apply yellow tint to view
                    ((ImageView) v).setColorFilter(Color.YELLOW);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    // Returns true to indicate that the View can accept the
                    // dragged data.
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:

                    // Apply a gray tint to the View
                    ((ImageView) v).setColorFilter(Color.LTGRAY);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:

                    // Ignore the event
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:

                    // Re-sets the color tint to yellow
                    ((ImageView) v).setColorFilter(Color.YELLOW);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData dragData = event.getClipData();

                    // Gets the text data from the item.
                    final String tag = dragData.getItemAt(0).getText().toString();

                    // Displays a message containing the dragged data.
//                    Toast.makeText(MainActivity.this, "The dragged image is " + tag, Toast.LENGTH_SHORT).show();

                    // Turns off any color tints
                    ((ImageView) v).clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:

                    // Turns off any color tinting
                    ((ImageView) v).clearColorFilter();

                    // Invalidates the view to force a redraw
                    v.invalidate();

                    // Check for result
                    if (event.getResult())
                    {
                        Log.d("Drag","On IF");

                    }
                    else
                    {

                        Log.d("Drag","On else");
                    }

                    return true;

                default:
                    break;
            }

            return true;
        }



    }
}