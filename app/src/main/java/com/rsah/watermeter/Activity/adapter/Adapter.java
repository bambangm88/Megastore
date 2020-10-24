package com.rsah.watermeter.Activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.rsah.watermeter.Constant.Constant;
import com.rsah.watermeter.Model.response.ResponseCustomer;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Session.SessionManager;
import com.rsah.watermeter.Util.DbCustomerHelper;

import java.util.List;



public class Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ResponseCustomer> items;

    public Adapter(Activity activity, List<ResponseCustomer> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_filter, null);

        TextView cstname = (TextView) convertView.findViewById(R.id.tvcstname);
        TextView ref = (TextView) convertView.findViewById(R.id.tvref);
        TextView address = (TextView) convertView.findViewById(R.id.tvaddress);
        TextView cluster = (TextView) convertView.findViewById(R.id.tvcluster);
        TextView status = (TextView) convertView.findViewById(R.id.tvstatus);
        TextView check = (TextView) convertView.findViewById(R.id.tvcheck);
        TextView consum = (TextView) convertView.findViewById(R.id.tvconsum);
        TextView _prevscan = (TextView) convertView.findViewById(R.id.tvprevscan);
        DbCustomerHelper SQLite = new DbCustomerHelper(activity);

        ResponseCustomer data = items.get(position);

        cstname.setText(data.getCustomer_name());
        ref.setText(data.getReference());
        address.setText(data.getAddress());
        cluster.setText(data.getArea());
        _prevscan.setText("prev scan: " + data.getPrev_date_scan());


        String statussync = data.getStatus_sync();
        String statusserver = data.getStatus_server();
        String end = data.getFinal_meter();
        String prev_init= data.getPrev_initial_meter();
        String init = data.getInitial_meter();
        String prevscan = data.getPrev_date_scan();


        check.setVisibility(View.GONE);
        _prevscan.setVisibility(View.VISIBLE);
        if (statussync.equals(Constant.STATUS_OPEN )){
                status.setText(Constant.STATUS_OPEN);
                status.setBackgroundResource(R.color.green);

        }else if(statussync.equals(Constant.STATUS_PENDING)){
            status.setText(Constant.STATUS_PENDING);
            status.setBackgroundResource(R.color.yellow);
        }else if(statussync.equals(Constant.STATUS_PENDING_CONFIRM)){
            status.setText(Constant.STATUS_PENDING_CONFIRM);
            status.setBackgroundResource(R.color.red_btn_bg_color);
        }else if(statussync.equals(Constant.STATUS_UPDATED)){
            status.setText(Constant.STATUS_COMPLETED);
            status.setBackgroundResource(R.color.light_blue);

        }

        if (statusserver.equals("1")){ //approved
            status.setText(Constant.STATUS_APPROVED_IN_SERVER);
            status.setBackgroundResource(R.color.colorPrimaryDark);
            check.setVisibility(View.VISIBLE);

        }

        if (statusserver.equals("4")){ //sudah scan
            status.setText(Constant.STATUS_COMPLETED);
            status.setBackgroundResource(R.color.light_blue);
        }



        consum.setVisibility(View.GONE);


        if (!end.equals("")){

                Long a = Long.parseLong(init);
                Long b = Long.parseLong(end);
                Long cons = b - a;
                String hasil = "cons: " + cons ;

                if (status.equals(Constant.STATUS_UPDATED ) || statusserver.equals("1") || statusserver.equals("4") ) {
                    consum.setText(hasil);
                    consum.setVisibility(View.VISIBLE);

                }

        }

        SessionManager sessionManager = new SessionManager(activity);
        String fontsize = sessionManager.getFontSize();
        String fontstyle = sessionManager.getFontStyle();

        if (fontstyle.equals("bold")){
            cstname.setTypeface(cstname.getTypeface(), Typeface.BOLD);
            ref.setTypeface(cstname.getTypeface(), Typeface.BOLD);
            cluster.setTypeface(cstname.getTypeface(), Typeface.BOLD);
            address.setTypeface(cstname.getTypeface(), Typeface.BOLD);
        }

        if (fontsize != null ){

            if (!fontsize.equals("") ){
                cstname.setTextSize(TypedValue.COMPLEX_UNIT_PT,Integer.parseInt(fontsize));
                ref.setTextSize(TypedValue.COMPLEX_UNIT_PT,Integer.parseInt(fontsize));
                cluster.setTextSize(TypedValue.COMPLEX_UNIT_PT,Integer.parseInt(fontsize));
                address.setTextSize(TypedValue.COMPLEX_UNIT_PT,Integer.parseInt(fontsize));
            }
        }






        return convertView;
    }
}
