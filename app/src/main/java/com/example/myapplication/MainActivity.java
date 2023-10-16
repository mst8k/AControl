package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    int room = 0;
    MaterialButton btnPowerOff, btnPowerOn, btnVolUp, btnVolDown, btnTv, btnTape, btnMute, btnUnMute;
    SwitchMaterial switchRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPowerOff = findViewById(R.id.btn_power_off);
        btnPowerOn = findViewById(R.id.btn_power_on);
        btnVolUp = findViewById(R.id.btn_volUp);
        btnVolDown = findViewById(R.id.btn_volDown);
        btnTv = findViewById(R.id.btn_tv);
        btnTape = findViewById(R.id.btn_tape);
        btnMute = findViewById(R.id.btn_mute);
        btnUnMute = findViewById(R.id.btn_unmute);
        switchRoom = findViewById(R.id.swt_room);

        btnPowerOff.setOnClickListener(this);
        btnPowerOn.setOnClickListener(this);
        btnVolUp.setOnClickListener(this);
        btnVolDown.setOnClickListener(this);
        btnTv.setOnClickListener(this);
        btnTape.setOnClickListener(this);
        btnMute.setOnClickListener(this);
        btnUnMute.setOnClickListener(this);

        // Set up switch state change listener
        switchRoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    room = 1;
                    showToast("Kitchen room selected");
                } else {
                    room = 0;
                    showToast("Salon room selected");
                }
            }
        });

    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view) {
        if(view.getId() ==  R.id.btn_power_off) {
            sendCommand("Sound_Off");
        }  if(view.getId() ==  R.id.btn_power_on) {
            if(room == 0) {
                sendCommand("A_On");
            } else {
                sendCommand("B_On");
            }
        } else if(view.getId() ==  R.id.btn_volUp) {
            if(room == 0) {
                sendCommand("A_vol_up");
            } else {
                sendCommand("B_vol_up");
            }
        } else if(view.getId() ==  R.id.btn_volDown) {
            if(room == 0) {
                sendCommand("A_vol_down");
            } else {
                sendCommand("B_vol_down");
            }
        } else if(view.getId() ==  R.id.btn_unmute) {
            if(room == 0) {
                sendCommand("A_unmute");
            } else {
                sendCommand("B_unmute");
            }
        } else if(view.getId() ==  R.id.btn_mute) {
            if(room == 0) {
                sendCommand("A_mute");
            } else {
                sendCommand("B_mute");
            }
        } else if(view.getId() ==  R.id.btn_tv) {
            if(room == 0) {
                sendCommand("A_tv");
            } else {
                sendCommand("B_tv");
            }
        } else if(view.getId() ==  R.id.btn_tape) {
            if(room == 0) {
                sendCommand("A_tape");
            } else {
                sendCommand("B_tape");
            }
        }
    }

    private void sendCommand(String command) {
        new SendGetRequest(command).execute();
    }

    private class SendGetRequest extends AsyncTask<Void, Void, String> {
        private String command;

        public SendGetRequest(String command) {
            this.command = command;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Replace with your local IP address and path
                URL url = new URL("http://192.168.0.152/" + command);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                conn.disconnect();

                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }


        protected void onPostExecute(String result) {
            showToast(result);
        }
    }
}
