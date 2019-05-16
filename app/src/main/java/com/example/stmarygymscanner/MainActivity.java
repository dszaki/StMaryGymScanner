package com.example.stmarygymscanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.SparseArray;
import android.widget.TextView;

import com.example.stmarygymscanner.model.Member;
import com.example.stmarygymscanner.model.StatusEnum;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BarcodeDetector barcodeDetector;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    String filename = "barcodes.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.cameraPreview);
        textView =  findViewById(R.id.textView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_8).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }try {
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();

                if(barcodeSparseArray.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textView.setText(getMemeberStatus(Integer.getInteger(barcodeSparseArray.valueAt(0).displayValue)));

//                            FileOutputStream fos = null;
//
//                            try {
//                                System.out.println("Saving "+ barcodeSparseArray.valueAt(0).displayValue+" to file");
//                                fos = openFileOutput(filename, Context.MODE_PRIVATE);
//                                fos.write(barcodeSparseArray.valueAt(0).displayValue.getBytes());
//                                fos.close();
//                            } catch ( IOException e) {
//                                e.printStackTrace();
//                            }


//
                        }


                    });

                }
            }
            public List<Member> readFile(int id) {
                List<Member> members = new ArrayList<>();
                try {
                    FileInputStream fileInputStream = openFileInput(filename);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();

                    String lines;
                    while ((lines = bufferedReader.readLine()) != null) {
                        stringBuffer.append(lines + "\n");
                    }

                    System.out.println(stringBuffer);
                    String name = "";
                    StatusEnum.status active = StatusEnum.status.ACTIVE;

                    //next line
                    // id , name, status
                    // java builder
                    Member member = new Member(id, name, active);

                    members.add(member);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return members;
            }

            public String getMemeberStatus(final int id){
               List<Member> memberList = readFile(id);

//               return memberList.stream().filter(member -> member.getId() == id).findFirst().orElse(null);

                for(Member member: memberList ){

                    if(member.getId() == id){
                        return member.getStatAnEnum().name();
                    }
                }
                return "MEMBER NOT FOUND";
            }
        });
    }

}
