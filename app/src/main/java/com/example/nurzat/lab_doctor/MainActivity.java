package com.example.nurzat.lab_doctor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RelativeLayout rl;

    TextView accept;
    TextView conf;
    TextView news;
    JSONArray jar = new JSONArray();
    JSONObject obj = new JSONObject();
    JSONObject obj2 = new JSONObject();

    JSONArray received = new JSONArray();
    JSONArray confirmed = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl = (RelativeLayout) findViewById(R.id.activity_main);
        rl.getBackground().setAlpha(39);
        JSONObject js = new JSONObject();
        try {
            js.put("id", "4");
            js.put("for_what","Вызов врача на дом");
            js.put("title", "Здесь будет надоевший вам симптом");
            js.put("review", 18);
            js.put("doctor_speciality_1", new String [] {"Терапевт","Терапевт-Неврапатолог","Хирург"});
            js.put("date","16 декабря");
            js.put("isaccepted",false);
            js.put("isconfirmed",true);
            js.put("time_from","08.30");
            js.put("address","Казахстан, г. Каскелен, Алтын ауыл, 4 дом 34 кв");
            js.put("time_to","12.30");
            js.put("url_icon","doctoroncall");

        } catch (JSONException e) {

        }
        confirmed.put(js);
        try {
            obj.put("id", "3");
            obj.put("for_what","Вызов врача на дом");
            obj.put("title", "Здесь будет надоевший вам симптом");
            obj.put("review", 18);
            obj.put("doctor_speciality_1", new String [] {"Терапевт","Терапевт-Неврапатолог","Хирург"});
            obj.put("date","16 декабря");
            obj.put("isaccepted",false);
            obj.put("isconfirmed",false);
            obj.put("time_from","08.30");
            obj.put("address","Казахстан, г. Каскелен, Алтын ауыл, 4 дом 34 кв");
            obj.put("time_to","12.30");
            obj.put("url_icon","doctoroncall");

        } catch (JSONException e) {

            e.printStackTrace();
        }
        try {
            obj2.put("id", "4");
            obj2.put("for_what","Запись на прием к врачу");
            obj2.put("title", "Здесь будет надоевший вам симптом");
            obj2.put("review", 12);
            obj2.put("doctor_speciality_1", new String [] {"Терапевт","Терапевт-Неврапатолог","Хирург"} );
            obj2.put("date","12 декабря");
            obj2.put("time_from","8.30");
            obj2.put("address","Абылай хана 49");
            obj2.put("isconfirmed",false);
            obj2.put("isaccepted",false);
            obj2.put("time_to","12.30");
            obj2.put("url_icon","doctorhour");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jar.put(obj2);
        jar.put(obj);
        accept = (TextView) findViewById(R.id.accepted);
        conf = (TextView) findViewById(R.id.confirmed);
        news = (TextView) findViewById(R.id.news);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SlideInRightAnimator animator = new SlideInRightAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        recyclerView.setItemAnimator(animator);
        recyclerView.setAdapter(new Adapter(jar));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accepted:
                accept.setTextColor(getResources().getColor(R.color.violet));
                conf.setTextColor(getResources().getColor(R.color.gray));
                news.setTextColor(getResources().getColor(R.color.gray));
                recyclerView.setAdapter(new Adapter(received));
                break;
            case R.id.confirmed:
                conf.setTextColor(getResources().getColor(R.color.violet));
                accept.setTextColor(getResources().getColor(R.color.gray));
                news.setTextColor(getResources().getColor(R.color.gray));
                recyclerView.setAdapter(new Adapter(confirmed));
                break;
            case R.id.news:
                news.setTextColor(getResources().getColor(R.color.violet));
                conf.setTextColor(getResources().getColor(R.color.gray));
                accept.setTextColor(getResources().getColor(R.color.gray));
                recyclerView.setAdapter(new Adapter(jar));
                break;
        }

    }
    private class Holder extends RecyclerView.ViewHolder{
        public TextView mTitleTextView;
        LinearLayout l;
        Button btn_sum;
        TextView review;

        ImageView imageView;
        LinearLayout bl;
        TextView for_what;
        TextView time;
        Date date1;
        JSONObject jar_ob;
        Date date2;
        int is_blue = 0;
        int position_r;
        TextView txt_expect;
        RowLayout rw;
        RowLayout rw_address;


        boolean is_open = false;
        public Holder(final View itemView) {
            super(itemView);
            l = (LinearLayout)itemView.findViewById(R.id.block_t1);
            l.getBackground().setAlpha(127);

            review = (TextView)itemView.findViewById(R.id.review);
            btn_sum = (Button)itemView.findViewById(R.id.btn_address);
            btn_sum.getBackground().setAlpha(127);
            bl = (LinearLayout)itemView.findViewById(R.id.block_content);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title_simptom);
            imageView = (ImageView)itemView.findViewById(R.id.image_icon);
            for_what = (TextView)itemView.findViewById(R.id.for_what);
            txt_expect = (TextView)itemView.findViewById(R.id.txt_expect);

            rw = (RowLayout)itemView.findViewById(R.id.block_speciality);
            rw_address = (RowLayout)itemView.findViewById(R.id.block_address);

            time = (TextView)itemView.findViewById(R.id.time);
            DateFormat sdf = new SimpleDateFormat("kk:mm");

            try {
                date1 = sdf.parse((time.getText()+"").substring(0,5));
                date2 = sdf.parse((time.getText()+"").substring(8));
                Log.d("my",(time.getText()+"").substring(0,5));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    is_open = !is_open;

                    LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View vi = inflater.inflate(R.layout.add_item,null);
                    final EditText time_from = (EditText)vi.findViewById(R.id.time_from);
                    final EditText time_to = (EditText)vi.findViewById(R.id.time_to);
                    final Button btn_o = (Button)vi.findViewById(R.id.button_otozvatsia);
                    btn_o.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(View v) {

                            if(is_blue==1){
                                jar_ob.remove("isaccepted");

                                Log.d("my",position_r+"");
                                ((Adapter)recyclerView.getAdapter()).remove(0);




                                try {
                                    jar_ob.put("isaccepted",true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                received.put(jar_ob);
                                btn_o.setVisibility(View.GONE);


                            }

                        }
                    });
                    time_to.setOnKeyListener(new View.OnKeyListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            DateFormat sdf = new SimpleDateFormat("kk:mm");
                            Date tmf = null;
                            Date tmt = null;
                            try {
                                tmf = sdf.parse(time_from.getText()+"");
                                tmt = sdf.parse(time_to.getText()+"");
                                if(tmf.after(date1)&&tmf.before(date2)&&tmt.after(date1)&&tmt.before(date2)){
                                    is_blue = 1;
                                    btn_o.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.shape_2));
                                    btn_o.getBackground().setAlpha(127);
                                }else{
                                    btn_o.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.gray));

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            return false;
                        }
                    });

                    if(is_open){
                        bl.addView(vi);

                    }else{
                        bl.removeViewAt(bl.getChildCount()-1);

                    }


                }
            });
        }
    }
    private class Adapter extends RecyclerView.Adapter<Holder> {
        private JSONArray titles;

        public Adapter(JSONArray t) {
            titles = t;
        }
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view = layoutInflater
                    .inflate(R.layout.item, parent, false);

            return new Holder(view);
        }
        RequestQueue queue;


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onBindViewHolder(final Holder holder, int position) {
            queue = Volley.newRequestQueue(MainActivity.this);
            try {
                holder.mTitleTextView.setText(((JSONObject)titles.get(position)).getString("title"));
                for(int i = 0;i<((String [])((JSONObject)titles.get(position)).get("doctor_speciality_1")).length;i++){
                    ViewGroup.LayoutParams pr = new ViewGroup.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tv = new TextView(MainActivity.this);

                    tv.setLayoutParams(pr);

                    tv.setText(((String [])((JSONObject)titles.get(position)).get("doctor_speciality_1"))[i]);
                    tv.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.shape_1));
                    holder.rw.addView(tv);

                }
                ViewGroup.LayoutParams pr = new ViewGroup.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tv = new TextView(MainActivity.this);
                tv.setLayoutParams(pr);
                tv.setText((((JSONObject)titles.get(position)).getString("address")));
                tv.setTextColor(getResources().getColor(R.color.gray));
                holder.rw_address.addView(tv);
                holder.review.setText(((JSONObject)titles.get(position)).getString("review")+" отзывов");
                holder.for_what.setText(((JSONObject)titles.get(position)).getString("for_what"));
                holder.jar_ob = (JSONObject)titles.get(position);
                holder.position_r = position;

                if(((JSONObject)titles.get(position)).getBoolean("isaccepted")){
                    holder.btn_sum.setVisibility(View.GONE);
                    holder.txt_expect.setVisibility(View.VISIBLE);
                    holder.mTitleTextView.setOnClickListener(null);


                }else if(((JSONObject)titles.get(position)).getBoolean("isconfirmed")){
                    holder.btn_sum.setText("Написать");
                    holder.mTitleTextView.setOnClickListener(null);

                }
                if(((JSONObject)titles.get(position)).getString("url_icon").equals("doctorhour")){
                    holder.imageView.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.doctorhour));

                }else if(((JSONObject)titles.get(position)).getString("url_icon").equals("doctoroncall")){
                    holder.imageView.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.doctoroncall));
                }

//
//
//                final ImageRequest imageRequest=new ImageRequest (((JSONObject)titles.get(position)).getString("url_icon"), new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        holder.imageView.setImageBitmap(response);
//
//                    }
//                },0,0, ImageView.ScaleType.CENTER_CROP,null, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("my",error.toString());
//                        error.printStackTrace();
//
//                    }
//                });
//                queue.add(imageRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void remove(int position) {
            titles.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return titles.length();
        }


    }
}
