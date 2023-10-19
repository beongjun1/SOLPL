package com.example.solpl1.chat.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solpl1.R;
import com.example.solpl1.chat.Activity.ChatDetailActivity;
import com.example.solpl1.chat.Adapters.Chat3Adapter;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.chat.Utils.RecyclerViewDecoration;
import com.example.solpl1.databinding.FragmentChat3Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ChatFragment3 extends Fragment {

    FragmentChat3Binding binding;
    ArrayList<ChatItem> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    LocationManager locationManager;
    String userLongitude, userLatitude, address1, address2, address3, address4;
    String userLocation;

    private final static int REQUEST_LOCATION=1;

    public ChatFragment3(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChat3Binding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        binding.chatRvBackground.setVisibility(View.GONE);



        Chat3Adapter adapter = new Chat3Adapter(getContext(),list);
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.addItemDecoration(new RecyclerViewDecoration(20));
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        // 현재위치 스위치버튼을 누르면 현재 위치 가져오기
        binding.switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CheckedLocationPermission();
                binding.chatLocation.setText(userLocation);


                // 어댑터에 데이터 넣기
                database.getReference().child("chat").child("chat_local")
                        .child(address1).child(address2).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    binding.chatRvBackground.setVisibility(View.VISIBLE);
                                    list.clear();
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        ChatItem chatItem = dataSnapshot.getValue(ChatItem.class);
                                        chatItem.setChatRoomId(dataSnapshot.getKey());
                                        list.add(chatItem);
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    binding.chatRvBackground.setVisibility(View.GONE);
                                    binding.textView6.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });




        // chat add 버튼 누르면
        binding.chatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userLatitude == null || userLongitude == null){
                    Toast.makeText(getContext(),"위치를 확인해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else showBottomDialog(userLatitude, userLongitude);
            }
        });

        return binding.getRoot();
    }

    private void CheckedLocationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Log.w("getSystemService", String.valueOf(locationManager));
        }
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Log.w("GPS", "Mobile에서 GPS 시작");
            OnGPS();
        } else {
            Log.w("getCurrentLocation", "getCurrentLocation 시작");
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            Log.w("checkSelfPermission", "Denied");
        } else {
            Location GpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.w("checkSelfPermission", "GPS");
            // GPS 보다 Network가 더 나은듯??
            if(GpsLocation != null){
                double latitude = GpsLocation.getLatitude();
                double Longitude = GpsLocation.getLongitude();

                userLatitude = String.valueOf(latitude);
                userLongitude = String.valueOf(Longitude);

                getAddressFromLatLong(getContext(),latitude,Longitude);
                Log.w("Location", userLocation);
            }else {
                Log.w("GpsLocation", "null");
                Log.w("NetworkLocation", "start");
                Location NetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                double latitude = NetworkLocation.getLatitude();
                double Longitude = NetworkLocation.getLongitude();
                userLatitude = String.valueOf(latitude);
                userLongitude = String.valueOf(Longitude);

                getAddressFromLatLong(getContext(),latitude,Longitude);


            }

        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Enable GPS").setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getAddressFromLatLong(Context context, double LATITUDE, double LONGITUDE){
        // address 설정
        try{
            Geocoder geocoder = new Geocoder(context, Locale.KOREA);
            // 주소를 위한 list
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE,1);
            if(addresses != null && addresses.size()>0){
                address1 = addresses.get(0).getAdminArea();     // 경기도
                address2 = addresses.get(0).getLocality();      // 안양시
//                address3 = addresses.get(0).getSubLocality();   // 동안구
//                address4 = addresses.get(0).getThoroughfare();  // 부흥동
                // AdminArea => 경기도 , Locality => 안양시,subLocality => 동안구, Thoroughfare : 부흥동

//                userLocation = address1 + " " + address2 +" " + address3 + " " + address4;
                userLocation = address1 + " " + address2;
                return userLocation;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return userLocation;
    }



    private void showBottomDialog(String mLatitude, String mLongitude) {

        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_chat3);


        EditText title = dialog.findViewById(R.id.chat_title);
        EditText description = dialog.findViewById(R.id.chat_description);
        Button chatAddNewBtn = dialog.findViewById(R.id.chat_add_new_btn);
        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
        TextView city = dialog.findViewById(R.id.chat_add_location);
        city.setText(userLocation);

        // 방 만들기 버튼을 누르면
        chatAddNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatItem chatItem = new ChatItem();
                if(!title.getText().toString().equals("") && !description.getText().toString().equals("")){
                    chatItem.setChatRoomBy(auth.getCurrentUser().getUid());
                    chatItem.setDescription(description.getText().toString());
                    chatItem.setTitle(title.getText().toString());

                    DatabaseReference pushedChatRef = database.getReference().child("chat").child("chat_local")
                            .child(address1).child(address2).push();
                    String chatRoomId = pushedChatRef.getKey();
                    chatItem.setChatRoomId(chatRoomId);
                    pushedChatRef.setValue(chatItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // chatUser DB에 방장 추가
                            pushedChatRef.child("chatUser")
                                    .child(auth.getCurrentUser().getUid()).setValue(true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(getContext(), ChatDetailActivity.class);
                                            intent.putExtra("chatRoomId", chatItem.getChatRoomId());
                                            intent.putExtra("chatType", "chat_local");
                                            intent.putExtra("title", chatItem.getTitle());
                                            intent.putExtra("address1", address1);
                                            intent.putExtra("address2", address2);
                                            startActivity(intent);
                                            Toast.makeText(getContext(), "채팅방 생성", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
                } else{
                    Toast.makeText(getContext(),"빈 칸을 채워주세요.",Toast.LENGTH_LONG).show();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}