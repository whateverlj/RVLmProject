package rvdemo.example.com.rvdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    RVLayoutManager rvLayoutManager;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(rvLayoutManager = new RVLayoutManager());

        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stack_card,
                        viewGroup, false);

                BaseViewHolder holder = new BaseViewHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                ImageView img =((ImageView) viewHolder.itemView.findViewById(R.id.img));
                if(i == 0){
                    img.setBackgroundResource(R.drawable.img1);
                }else if(i == 1){
                    img.setBackgroundResource(R.drawable.img2);
                }else if(i == 2){
                    img.setBackgroundResource(R.drawable.img3);
                }else if(i == 3){
                    img.setBackgroundResource(R.drawable.img4);
                }else if(i == 4){
                    img.setBackgroundResource(R.drawable.img5);
                }
            }

            @Override
            public int getItemViewType(int position) {
                return 1;
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        });
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
