package com.example.leejs4937.networkproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by leejs4937 on 2016. 5. 20..
 */
public class CustomAdapter extends BaseAdapter {

    private CustomDialog mCustomDialog;

        // 문자열을 보관 할 ArrayList
    private ArrayList<String> name_List;
    private ArrayList<String> price_List;
    private ArrayList<String> seller_List;
    private ArrayList<String> item_id;

        // 생성자
        public CustomAdapter() {
            name_List = new ArrayList<String>();
            price_List = new ArrayList<String>();
            seller_List = new ArrayList<String>();
            item_id = new ArrayList<String>();
        }

        // 현재 아이템의 수를 리턴
        @Override
        public int getCount() {
            return name_List.size();
        }

        // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
        @Override
        public Object getItem(int position) {
            return name_List.get(position);
        }

        // 아이템 position의 ID 값 리턴
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 출력 될 아이템 관리
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            TextView        name    = null;
            TextView        price    = null;
            TextView        seller    = null;
            CustomHolder    holder  = null;

            // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
            if ( convertView == null ) {
                // view가 null일 경우 커스텀 레이아웃을 얻어 옴
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_view, parent, false);

                name    = (TextView) convertView.findViewById(R.id.item_name);
                price    = (TextView) convertView.findViewById(R.id.item_price);
                seller    = (TextView) convertView.findViewById(R.id.item_seller);

                // 홀더 생성 및 Tag로 등록
                holder = new CustomHolder();
                holder.m_TextView_name   = name;
                holder.m_TextView_price   = price;
                holder.m_TextView_seller   = seller;
                convertView.setTag(holder);
            }
            else {
                holder  = (CustomHolder) convertView.getTag();
                name    = holder.m_TextView_name;
                price    = holder.m_TextView_price;
                seller    = holder.m_TextView_seller;
            }

            // Text 등록
            name.setText(name_List.get(position));
            price.setText(price_List.get(position));
            seller.setText(seller_List.get(position));

            // 리스트 아이템을 터치 했을 때 이벤트 발생
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCustomDialog = new CustomDialog(context, name_List.get(pos), item_id.get(pos));
                    mCustomDialog.show();
                    //Toast.makeText(context, "리스트 클릭 : "+m_List.get(pos), Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }

    private class CustomHolder {
        TextView    m_TextView_name;
        TextView    m_TextView_price;
        TextView    m_TextView_seller;
    }

        // 외부에서 아이템 추가 요청 시 사용
        public void add(String name, String price, String seller, String id) {
            name_List.add(name);
            price_List.add(price);
            seller_List.add(seller);
            item_id.add(id);
        }

        // 외부에서 아이템 삭제 요청 시 사용
        public void remove(int _position) {
            name_List.remove(_position);
            price_List.remove(_position);
            seller_List.remove(_position);
        }
}
